package org.tinycloud.tinymock.modules.service;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.CoreErrorCode;
import org.tinycloud.tinymock.common.exception.CoreException;
import org.tinycloud.tinymock.common.utils.DataMockUtils;
import org.tinycloud.tinymock.common.utils.IpAddressUtils;
import org.tinycloud.tinymock.common.utils.IpGetUtils;
import org.tinycloud.tinymock.common.utils.JsonUtils;
import org.tinycloud.tinymock.common.utils.web.UserAgentUtils;
import org.tinycloud.tinymock.modules.bean.entity.TMockAccessLog;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.mapper.MockAccessLogMapper;
import org.tinycloud.tinymock.modules.mapper.MockInfoMapper;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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

    @Qualifier("asyncServiceExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Object mock(HttpServletRequest request) {
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
            if (StringUtils.isBlank(projectId) || StringUtils.isBlank(url)) {
                throw new CoreException(CoreErrorCode.MOCK_ADDRESS_RESOLUTION_ERROR);
            }

            // 校验projectId和projectPath是否匹配
            TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                    Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getId, projectId).eq(TProjectInfo::getPath, projectPath)
                            .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
            if (Objects.isNull(projectInfo)) {
                throw new CoreException(CoreErrorCode.MOCK_PROJECT_PATH_IS_NOT_EXIST);
            }

            // 然后校验url是否存在
            TMockInfo mockInfo = this.mockInfoMapper.selectOne(
                    Wrappers.<TMockInfo>lambdaQuery().eq(TMockInfo::getProjectId, projectId)
                            .eq(TMockInfo::getUrl, url)
                            .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED));
            if (Objects.isNull(mockInfo)) {
                throw new CoreException(CoreErrorCode.MOCK_URL_IS_NOT_EXIST);
            }
            // 判断 请求类型是否相同，如果不同，则返回错误
            String mockMethod = mockInfo.getMethod();
            if (!mockMethod.equals(method)) {
                throw new CoreException(CoreErrorCode.MOCK_METHOD_IS_NOT_MATCHED);
            }

            // 如果相同，则返回表里存的json数据
            String jsonData = mockInfo.getJsonData();
            Integer mockjsFlag = mockInfo.getMockjsFlag();

            // 判断是否需要使用mockjs进行解析
            Map map;
            if (mockjsFlag == 0) {
                map = DataMockUtils.mock(jsonData);
            } else {
                map = JsonUtils.readValue(jsonData, Map.class);
            }
            this.saveAccessLog(mockInfo, request);

            // 模拟返回延时
            if (Objects.nonNull(mockInfo.getDelay())) {
                long end = System.currentTimeMillis();
                long hasBeenConsumed = end - start;
                if (hasBeenConsumed < mockInfo.getDelay()) {
                    ThreadUtil.sleep(mockInfo.getDelay() - hasBeenConsumed);
                }
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

    private void saveAccessLog(TMockInfo mockInfo, HttpServletRequest request) {
        final String accessIp = IpGetUtils.getIpAddr(request);
        final String accessUserAgent = JsonUtils.toJsonString(UserAgentUtils.getUserAgent(request));
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
