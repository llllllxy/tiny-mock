package org.tinycloud.tinymock.common.config.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.utils.JacksonUtils;
import org.tinycloud.tinymock.common.utils.web.IpGetUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 统一日志拦截打印切面
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-03-07 15:47:38
 */
@Aspect
@Component
public class HttpRequestAspect {
    static final Logger log = LoggerFactory.getLogger(HttpRequestAspect.class);

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("execution(* org.tinycloud..web..*.*(..))")
    public void webAPILog() {
    }

    /**
     * 以 provider 包下定义的所有请求为切入点
     */
    @Pointcut("execution(* org.tinycloud..rmi..*.*(..))")
    public void rmiAPILog() {
    }


    /**
     * 环绕通知
     */
    @Around("webAPILog() || rmiAPILog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("========================================== Start ==========================================");
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(IpGetUtils.getIpAddr(request));
        requestInfo.setUrl(request.getRequestURL().toString());
        requestInfo.setClassMethod(proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName());
        requestInfo.setHttpMethod(request.getMethod());

        Map<String, Object> param = buildRequestParams(proceedingJoinPoint);
        requestInfo.setRequestParams(param);


        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        requestInfo.setTimeConsuming(System.currentTimeMillis() - startTime);
        requestInfo.setResponseArgs(result);
        log.info("requestInfo : {}", JacksonUtils.toJsonString(requestInfo));

        log.info("=========================================== End ===========================================");
        return result;
    }


    @AfterThrowing(pointcut = "webAPILog() || rmiAPILog()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(IpGetUtils.getIpAddr(request));
        requestInfo.setUrl(request.getRequestURL().toString());
        requestInfo.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        requestInfo.setHttpMethod(request.getMethod());

        Map<String, Object> param = buildRequestParams(joinPoint);
        requestInfo.setRequestParams(param);

        long startTime = System.currentTimeMillis();
        requestInfo.setTimeConsuming(System.currentTimeMillis() - startTime);
        requestInfo.setResponseArgs(null);
        log.info("requestInfo : {}", JacksonUtils.toJsonString(requestInfo));

        log.error("errorMessage : {}", e.getMessage());
        log.error("=========================================== End ===========================================");
    }

    private Map<String, Object> buildRequestParams(JoinPoint joinPoint) {
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> requestParamsMap = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            if (value instanceof HttpServletRequest || value instanceof HttpServletResponse) {
                continue;
            }
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename(); // 获取文件名
            }
            requestParamsMap.put(paramNames[i], value);
        }
        return requestParamsMap;
    }


    public static class RequestInfo {
        private String ip;
        private String url;
        private String httpMethod;
        private String classMethod;
        private Object requestParams;
        private Object responseArgs;
        private String errorMessage;
        private long timeConsuming;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
        }

        public String getClassMethod() {
            return classMethod;
        }

        public void setClassMethod(String classMethod) {
            this.classMethod = classMethod;
        }

        public Object getRequestParams() {
            return requestParams;
        }

        public void setRequestParams(Object requestParams) {
            this.requestParams = requestParams;
        }

        public Object getResponseArgs() {
            return responseArgs;
        }

        public void setResponseArgs(Object responseArgs) {
            this.responseArgs = responseArgs;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public long getTimeConsuming() {
            return timeConsuming;
        }

        public void setTimeConsuming(long timeConsuming) {
            this.timeConsuming = timeConsuming;
        }

        @Override
        public String toString() {
            return "RequestInfo{" +
                    "ip='" + ip + '\'' +
                    ", url='" + url + '\'' +
                    ", httpMethod='" + httpMethod + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", requestParams=" + requestParams +
                    ", responseArgs=" + responseArgs +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", timeConsuming=" + timeConsuming +
                    '}';
        }
    }
}
