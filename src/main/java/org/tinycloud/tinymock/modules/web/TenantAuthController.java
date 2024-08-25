package org.tinycloud.tinymock.modules.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tinycloud.tinymock.common.annotation.AccessLimit;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.dto.TenantEditDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantEditPasswordDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantLoginDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantRegisterDto;
import org.tinycloud.tinymock.modules.bean.vo.TenantCaptchaCodeVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantInfoVo;
import org.tinycloud.tinymock.modules.service.TenantAuthService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 租户会话管理-控制器
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-12-2023/12/3 21:03
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class TenantAuthController {

    @Autowired
    private TenantAuthService tenantAuthService;

    @GetMapping("/getCode")
    @ResponseBody
    public ApiResult<TenantCaptchaCodeVo> getCode() {
        return ApiResult.success(tenantAuthService.getCode(), "获取验证码成功");
    }

    @PostMapping("/login")
    public ApiResult<String> login(@Validated @RequestBody TenantLoginDto dto, HttpServletRequest request) {
        return ApiResult.success(tenantAuthService.login(dto, request), "登录成功，欢迎回来！");
    }

    @GetMapping("/logout")
    public ApiResult<Boolean> logout(HttpServletRequest request) {
        return ApiResult.success(tenantAuthService.logout(request), "退出登录成功！");
    }

    @GetMapping("/initMenu")
    public ApiResult<?> initMenu() {
        Map<String, Object> initInfo = new HashMap<>();


        Map<String, String> menuItem2 = new HashMap<>();
        menuItem2.put("title", "我的项目");
        menuItem2.put("href", "page/project.html");
        menuItem2.put("icon", "fa fa-navicon");
        menuItem2.put("target", "_self");

        Map<String, String> menuItem3 = new HashMap<>();
        menuItem3.put("title", "数据统计");
        menuItem3.put("href", "page/statistic.html");
        menuItem3.put("icon", "fa fa-navicon");
        menuItem3.put("target", "_self");

        Map<String, String> menuItem4 = new HashMap<>();
        menuItem4.put("title", "使用文档");
        menuItem4.put("href", "page/handbook.html");
        menuItem4.put("icon", "fa fa-navicon");
        menuItem4.put("target", "_self");

        Map<String, String> menuItem5 = new HashMap<>();
        menuItem5.put("title", "问题和建议");
        menuItem5.put("href", "page/feedback.html");
        menuItem5.put("icon", "fa fa-navicon");
        menuItem5.put("target", "_self");

        Map<String, String> menuItem6 = new HashMap<>();
        menuItem6.put("title", "Github");
        menuItem6.put("href", "https://github.com/llllllxy/tiny-mock");
        menuItem6.put("icon", "fa fa-navicon");
        menuItem6.put("target", "_blank");

        List<Map<String, String>> menuList = new ArrayList<>();
        menuList.add(menuItem2);
        menuList.add(menuItem3);
        menuList.add(menuItem4);
        menuList.add(menuItem5);
        menuList.add(menuItem6);


        Map<String, String> homeInfo = new HashMap<>();
        homeInfo.put("title", "首页");
        homeInfo.put("href", "page/dashboard.html");

        Map<String, String> logoInfo = new HashMap<>();
        logoInfo.put("title", "TinyMock平台");
        logoInfo.put("image", "/images/logo.png");
        logoInfo.put("href", "");

        initInfo.put("homeInfo", homeInfo);
        initInfo.put("logoInfo", logoInfo);
        initInfo.put("menuInfo", menuList);

        return ApiResult.success(initInfo, "获取成功");
    }

    @GetMapping("/getTenantInfo")
    public ApiResult<TenantInfoVo> getTenantInfo() {
        return ApiResult.success(tenantAuthService.getTenantInfo(), "获取租户信息成功，欢迎回来！");
    }

    @PostMapping("/editTenantInfo")
    public ApiResult<Boolean> editTenantInfo(@Validated @RequestBody TenantEditDto dto) {
        return ApiResult.success(tenantAuthService.editTenantInfo(dto), "修改租户信息成功！");
    }

    @GetMapping("/sendEmail")
    public ApiResult<Map<String, String>> sendEmail(@RequestParam(value = "receiveEmail") String receiveEmail) {
        return ApiResult.success(tenantAuthService.sendEmail(receiveEmail), "邮件发送成功！");
    }

    @AccessLimit(seconds = 300, maxCount = 3, msg = "5分钟内只能注册三次，请稍后再试")
    @PostMapping("/register")
    public ApiResult<Boolean> register(@Validated @RequestBody TenantRegisterDto dto) {
        return ApiResult.success(tenantAuthService.register(dto), "注册成功！");
    }

    @GetMapping("/refreshInvitationCode")
    public ApiResult<String> refreshInvitationCode() {
        return ApiResult.success(tenantAuthService.refreshInvitationCode(), "重置邀请码成功！");
    }

    @PostMapping("/editPassword")
    public ApiResult<Boolean> editPassword(@Validated @RequestBody TenantEditPasswordDto dto) {
        return ApiResult.success(tenantAuthService.editPassword(dto), "修改密码成功，即将跳转到登录页！");
    }
}
