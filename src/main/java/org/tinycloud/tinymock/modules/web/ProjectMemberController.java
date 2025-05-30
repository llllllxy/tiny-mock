package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tinycloud.tinymock.common.annotation.OperLog;
import org.tinycloud.tinymock.common.enums.BusinessType;
import org.tinycloud.tinymock.common.enums.OperatorType;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.dto.ProjectMemberAddDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectMemberListDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectMemberSearchDto;
import org.tinycloud.tinymock.modules.bean.vo.ProjectMemberVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantInfoChooseVo;
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
     * 协作人列表
     */
    @PostMapping("/list")
    public ApiResult<List<ProjectMemberVo>> list(@Validated @RequestBody ProjectMemberListDto dto) {
        return ApiResult.success(projectMemberService.list(dto.getProjectId()), "获取成功");
    }

    /**
     * 查询未绑定的协作人
     */
    @PostMapping("/search")
    public ApiResult<List<TenantInfoChooseVo>> search(@Validated @RequestBody ProjectMemberSearchDto dto) {
        return ApiResult.success(projectMemberService.search(dto), "获取成功");
    }

    /**
     * 删除协作人
     */
    @OperLog(title = "删除协作人", code = "10021", operatorType = OperatorType.TENANT, businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public ApiResult<Boolean> delete(@RequestParam("id") Long id) {
        return ApiResult.success(projectMemberService.delete(id), "删除成功");
    }

    /**
     * 新增协作人
     */
    @OperLog(title = "新增协作人", code = "10022", operatorType = OperatorType.TENANT, businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public ApiResult<Boolean> add(@Validated @RequestBody ProjectMemberAddDto dto) {
        return ApiResult.success(projectMemberService.add(dto), "新增成功");
    }
}
