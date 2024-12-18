package org.tinycloud.tinymock.common.config.aspect;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.annotation.OperLog;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.OperResultCode;
import org.tinycloud.tinymock.common.enums.OperatorType;
import org.tinycloud.tinymock.common.utils.DateUtils;
import org.tinycloud.tinymock.common.utils.JacksonUtils;
import org.tinycloud.tinymock.common.utils.cipher.SM3Utils;
import org.tinycloud.tinymock.common.utils.web.IpAddressUtils;
import org.tinycloud.tinymock.common.utils.web.IpGetUtils;
import org.tinycloud.tinymock.common.utils.web.ServletUtils;
import org.tinycloud.tinymock.modules.bean.entity.TOperateLog;
import org.tinycloud.tinymock.modules.service.OperateLogService;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 15:01
 */
@Aspect
@Component
public class OperLogAspect {
    private static final Logger log = LoggerFactory.getLogger(OperLogAspect.class);

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private ApplicationConfig applicationConfig;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerOperLog)")
    public void boBefore(JoinPoint joinPoint, OperLog controllerOperLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerOperLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OperLog controllerOperLog, Object jsonResult) {
        handleLog(joinPoint, controllerOperLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerOperLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperLog controllerOperLog, Exception e) {
        handleLog(joinPoint, controllerOperLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, OperLog controllerOperLog, final Exception e, Object jsonResult) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            if (Objects.isNull(request)) {
                return;
            }
            TOperateLog tOperateLog = new TOperateLog();
            tOperateLog.setDelFlag(GlobalConstant.NOT_DELETED);
            tOperateLog.setOperResult(OperResultCode.SUCCESS.getCode());
            // 请求的地址
            String ip = IpGetUtils.getIpAddr(ServletUtils.getRequest());
            tOperateLog.setOperIp(ip);
            tOperateLog.setOperLocation(IpAddressUtils.getAddressByIP(ip));
            tOperateLog.setOperUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
            if (e != null) {
                tOperateLog.setOperResult(OperResultCode.FAIL.getCode());
                tOperateLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            tOperateLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            tOperateLog.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            this.getControllerMethodDescription(joinPoint, controllerOperLog, tOperateLog, jsonResult);
            // 设置消耗时间
            tOperateLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            tOperateLog.setOperateAt(DateUtils.format(new Date(TIME_THREADLOCAL.get()), DateUtils.DATE_TIME_MILLIS_PATTERN));
            // 计算审计哈希值
            this.calculateHash(tOperateLog);
            // 保存操作日志到数据库
            this.operateLogService.insert(tOperateLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息: {}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log         操作日志注解
     * @param tOperateLog 操作日志实体
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, OperLog log, TOperateLog tOperateLog, Object jsonResult) throws Exception {
        // 设置action动作
        tOperateLog.setBusinessType(log.businessType().getCode());
        // 设置标题
        tOperateLog.setTitle(log.title());
        // 设置操作码
        tOperateLog.setCode(log.code());
        // 设置操作人类别
        tOperateLog.setOperatorType(log.operatorType().getCode());

        if (log.operatorType() == OperatorType.TENANT) {
            // 获取当前的租户
            Long loginUser = TenantHolder.getTenantId();
            tOperateLog.setOperator(loginUser);
            // 此处亦可以存储部门编码，这里暂时不需要
            // operLog.setDeptName(getDeptName(loginUser));
        } else if (log.operatorType() == OperatorType.MANAGE) {
            // 暂未实现后台管理的功能，先写死一个超级管理员
            tOperateLog.setOperator(1L);
        }

        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, tOperateLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && Objects.nonNull(jsonResult)) {
            tOperateLog.setJsonResult(StringUtils.substring(JacksonUtils.toJsonString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, TOperateLog operLog, String[] excludeParamNames) throws Exception {
        Map<?, ?> paramsMap = ServletUtils.getParameters(ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();
        if (CollectionUtils.isEmpty(paramsMap) && StringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            operLog.setOperParam(StringUtils.substring(JacksonUtils.toJsonString(paramsMap), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (Objects.nonNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JacksonUtils.toJsonString(o);
                        params.append(jsonObj).append(" ");
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 忽略敏感属性（暂未实现，jackson里不好实现，又不想引入fastjson）
     */
//    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
//        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
//    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }


    private void calculateHash(TOperateLog tOperateLog) {
        String macKey = applicationConfig.getProjectExportDek();
        StringBuilder sb = new StringBuilder();
        sb.append(tOperateLog.getCode());
        sb.append(tOperateLog.getTitle());
        sb.append(tOperateLog.getOperateAt());
        sb.append(tOperateLog.getOperator());
        sb.append(tOperateLog.getOperIp());
        sb.append(tOperateLog.getOperUrl());
        sb.append(tOperateLog.getMethod());
        sb.append(tOperateLog.getOperParam());
        sb.append(tOperateLog.getOperResult());
        sb.append(tOperateLog.getErrorMsg());
        sb.append(tOperateLog.getJsonResult());
        tOperateLog.setAuditHash(SM3Utils.hmac(macKey, sb.toString()));
    }
}
