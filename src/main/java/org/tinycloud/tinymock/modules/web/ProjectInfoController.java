package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.vo.ProjectInfoVo;
import org.tinycloud.tinymock.modules.service.ProjectInfoService;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/11 22:04
 */
@RestController
@RequestMapping("/projectinfo")
public class ProjectInfoController {

    @Autowired
    private ProjectInfoService projectInfoService;

    @GetMapping("/query")
    public ApiResult<List<ProjectInfoVo>> query() {
        return ApiResult.success(projectInfoService.query(), "获取成功");
    }
}
