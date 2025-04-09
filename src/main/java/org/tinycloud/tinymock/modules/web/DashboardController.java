package org.tinycloud.tinymock.modules.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.vo.DashboardQuantityVo;
import org.tinycloud.tinymock.modules.service.DashboardService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-22 15:32
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/quantity")
    public ApiResult<DashboardQuantityVo> quantity() {
        return ApiResult.success(dashboardService.quantity(), "获取成功!");
    }

    @PostMapping(value = "/topList")
    public ApiResult<List<Map<String, Object>>> topList() {
        return ApiResult.success(dashboardService.topList(), "查询成功");
    }

    @GetMapping(value = "/chartsInfo")
    public ApiResult<Map<String, Object>> chartsInfo() {
        return ApiResult.success(dashboardService.chartsInfo(), "查询成功");
    }
}
