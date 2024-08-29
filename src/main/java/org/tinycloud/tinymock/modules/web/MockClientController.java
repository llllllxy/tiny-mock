package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.modules.service.MockClientService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 *     mock接口控制核心控制类
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/9 18:42
 */
@RestController
@RequestMapping("/mock")
public class MockClientController {

    @Autowired
    private MockClientService mockClientService;

    /**
     * 匹配mock请求，
     * 地址示例：GET请求 http://127.0.0.1:8077/api/929292901911010/first/cainiao
     * POST请求 http://127.0.0.1:8077/api/939393939393/first/cainiao2
     * 地址详解：mock / projectId / projectPath / url（url里可能还有好多斜杠）
     *
     * @param request HttpServletRequest
     * @return mock后的数据
     */
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public Object mock(HttpServletRequest request, HttpServletResponse response) {
        return mockClientService.mock(request, response);
    }
}
