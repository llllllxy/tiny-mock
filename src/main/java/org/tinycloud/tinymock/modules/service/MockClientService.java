package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.constant.BusinessConstant;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.CoreErrorCode;
import org.tinycloud.tinymock.common.exception.CoreException;
import org.tinycloud.tinymock.common.utils.*;
import org.tinycloud.tinymock.common.utils.web.UserAgentUtils;
import org.tinycloud.tinymock.modules.bean.entity.TMockAccessLog;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.mapper.MockAccessLogMapper;
import org.tinycloud.tinymock.modules.mapper.MockInfoMapper;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;

import javax.script.ScriptException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/9 18:46
 */
@Slf4j
@Service
public class MockClientService {

    @Autowired
    private MockInfoMapper mockInfoMapper;

    @Autowired
    private MockAccessLogMapper mockAccessLogMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Qualifier("asyncServiceExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Object mock(HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();

        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        log.info("MockClientService mock requestUri = {}, method= {}", requestUri, method);

        // uri = /mock/929292901911010/first/cainiao, method= GET
        String[] strArray = requestUri.split("/");
        try {
            log.info("MockClientController mockClient strs = {}", Arrays.toString(strArray));
            if (strArray.length < 5) {
                throw new CoreException(CoreErrorCode.MOCK_ADDRESS_RESOLUTION_ERROR);
            }
            String projectId = strArray[2];
            String projectPath = "/" + strArray[3];
            // 因为url可能有多个斜杠，所以特殊处理
            StringBuilder sb = new StringBuilder();
            for (int i = 4; i < strArray.length; i++) {
                sb.append(strArray[i]).append("/");
            }
            String url = "/" + sb.substring(0, sb.length() - 1);
            if (StrUtils.isBlank(projectId) || StrUtils.isBlank(url)) {
                throw new CoreException(CoreErrorCode.MOCK_ADDRESS_RESOLUTION_ERROR);
            }

            // 校验projectId和projectPath是否匹配
            this.checkProjectInfo(projectId, projectPath);

            // 然后校验url是否存在
            TMockInfo mockInfo = this.getMockInfo(projectId, url);

            // 判断 请求类型是否相同，如果不同，则返回错误
            String mockMethod = mockInfo.getMethod();
            if (!mockMethod.equals(method)) {
                throw new CoreException(CoreErrorCode.MOCK_METHOD_IS_NOT_MATCHED);
            }

            // 如果相同，则返回表里存的json数据
            String jsonData = mockInfo.getJsonData();
            Integer mockjsFlag = mockInfo.getMockjsFlag();

            // 判断是否需要使用mockjs进行解析
            Map<String, Object> map;
            if (mockjsFlag == 0) {
                map = DataMockUtils.mock(jsonData);
            } else {
                map = JacksonUtils.readMap(jsonData);
            }
            this.saveAccessLog(mockInfo, request);

            // 模拟返回延时
            if (Objects.nonNull(mockInfo.getDelay())) {
                long end = System.currentTimeMillis();
                long hasBeenConsumed = end - start;
                if (hasBeenConsumed < mockInfo.getDelay()) {
                    ThreadUtils.sleep(mockInfo.getDelay() - hasBeenConsumed);
                }
            }
            // 模拟http status code
            if (Objects.nonNull(mockInfo.getHttpCode())) {
                response.setStatus(mockInfo.getHttpCode());
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }

            return map;
        } catch (ScriptException e) {
            throw new CoreException(CoreErrorCode.MOCKJS_PARSING_ERROR);
        } catch (Exception e2) {
            if (e2 instanceof CoreException) {
                throw new CoreException(((CoreException) e2).getCode(), e2.getMessage());
            } else {
                throw new CoreException(CoreErrorCode.MOCK_ADDRESS_RESOLUTION_ERROR);
            }
        }
    }

    private void checkProjectInfo(String projectId, String projectPath) {
        String projectInfoString = this.redisTemplate.opsForValue().get(BusinessConstant.TENANT_PROJECT_REDIS_KEY + projectId);
        if (StringUtils.hasText(projectInfoString)) {
            TProjectInfo projectInfo = JacksonUtils.readValue(projectInfoString, TProjectInfo.class);
            if (!projectPath.equals(projectInfo.getPath())) {
                throw new CoreException(CoreErrorCode.MOCK_PROJECT_PATH_IS_NOT_EXIST);
            }
        } else {
            TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                    Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getId, projectId).eq(TProjectInfo::getPath, projectPath)
                            .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
            if (Objects.isNull(projectInfo)) {
                throw new CoreException(CoreErrorCode.MOCK_PROJECT_PATH_IS_NOT_EXIST);
            }
            this.redisTemplate.opsForValue().set(BusinessConstant.TENANT_PROJECT_REDIS_KEY + projectId, JacksonUtils.toJsonString(projectInfo), BusinessConstant.CACHE_SESSION_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    private TMockInfo getMockInfo(String projectId, String url) {
        String urlKey = url.replace("/", "_");
        String mockInfoString = this.redisTemplate.opsForValue().get(BusinessConstant.TENANT_MOCK_REDIS_KEY + projectId + urlKey);
        TMockInfo mockInfo;
        if (StringUtils.hasText(mockInfoString)) {
            mockInfo = JacksonUtils.readValue(mockInfoString, TMockInfo.class);
        } else {
            mockInfo = this.mockInfoMapper.selectOne(
                    Wrappers.<TMockInfo>lambdaQuery().eq(TMockInfo::getProjectId, projectId)
                            .eq(TMockInfo::getUrl, url)
                            .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED));
            if (Objects.isNull(mockInfo)) {
                throw new CoreException(CoreErrorCode.MOCK_URL_IS_NOT_EXIST);
            }
            this.redisTemplate.opsForValue().set(BusinessConstant.TENANT_MOCK_REDIS_KEY + projectId + urlKey, JacksonUtils.toJsonString(mockInfo), BusinessConstant.CACHE_SESSION_TIMEOUT, TimeUnit.SECONDS);
        }
        if (Objects.isNull(mockInfo)) {
            throw new CoreException(CoreErrorCode.MOCK_URL_IS_NOT_EXIST);
        }
        if (GlobalConstant.DISABLED.equals(mockInfo.getStatus())) {
            throw new CoreException(CoreErrorCode.MOCK_URL_IS_DISABLED);
        }
        return mockInfo;
    }

    private void saveAccessLog(TMockInfo mockInfo, HttpServletRequest request) {
        final String accessIp = IpGetUtils.getIpAddr(request);
        final String accessUserAgent = JacksonUtils.toJsonString(UserAgentUtils.getUserAgent(request));
        this.threadPoolTaskExecutor.execute(() -> {
            // 插入访问日志
            TMockAccessLog tMockAccessLog = new TMockAccessLog();
            tMockAccessLog.setTenantId(mockInfo.getTenantId());
            tMockAccessLog.setMockId(mockInfo.getId());
            tMockAccessLog.setProjectId(mockInfo.getProjectId());
            tMockAccessLog.setAccessIp(accessIp);
            String accessAddress = IpAddressUtils.getAddressByIP(accessIp);
            tMockAccessLog.setAccessAddress(accessAddress);
            tMockAccessLog.setAccessUserAgent(accessUserAgent);
            tMockAccessLog.setAccessTime(new Date());
            this.mockAccessLogMapper.insert(tMockAccessLog);
        });
    }
}
