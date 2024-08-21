package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.vo.ProjectMemberVo;
import org.tinycloud.tinymock.modules.service.ProjectMemberService;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/21 23:24
 */
@RestController
@RequestMapping("/projectmember")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    /**
     * 卡片表格查询项目
     *
     * @return
     */
    @GetMapping("/list")
    public ApiResult<List<ProjectMemberVo>> list(@RequestParam("projectId") Long projectId) {
        return ApiResult.success(projectMemberService.list(projectId), "获取成功");
    }
}
