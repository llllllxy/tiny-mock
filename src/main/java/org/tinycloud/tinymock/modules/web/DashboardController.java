package org.tinycloud.tinymock.modules.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.vo.DashboardQuantityVo;
import org.tinycloud.tinymock.modules.service.DashboardService;

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

    /**
     * 新增
     *
     * @return
     */
    @GetMapping("/quantity")
    public ApiResult<DashboardQuantityVo> quantity() {
        return ApiResult.success(dashboardService.quantity(), "获取成功!");
    }
}
