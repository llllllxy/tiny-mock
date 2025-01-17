package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tinycloud.tinymock.common.annotation.OperLog;
import org.tinycloud.tinymock.common.enums.BusinessType;
import org.tinycloud.tinymock.common.enums.OperatorType;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.dto.ProjectAddDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectDeleteDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectEditDto;
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

    /**
     * 卡片表格查询项目
     *
     * @return
     */
    @GetMapping("/query")
    public ApiResult<List<ProjectInfoVo>> query() {
        return ApiResult.success(projectInfoService.query(), "获取成功");
    }

    /**
     * 获取项目明细项目
     *
     * @return
     */
    @GetMapping("/detail")
    public ApiResult<ProjectInfoVo> detail(@RequestParam("id") Long id) {
        return ApiResult.success(projectInfoService.detail(id), "获取成功");
    }

    /**
     * 新增项目
     *
     * @return
     */
    @OperLog(title = "新增项目", code = "10001", operatorType = OperatorType.TENANT, businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public ApiResult<Boolean> add(@Validated @RequestBody ProjectAddDto dto) {
        return ApiResult.success(projectInfoService.add(dto), "新增成功!");
    }


    /**
     * 修改项目
     *
     * @return
     */
    @OperLog(title = "修改项目", code = "10002", operatorType = OperatorType.TENANT, businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public ApiResult<Boolean> edit(@Validated @RequestBody ProjectEditDto dto) {
        return ApiResult.success(projectInfoService.edit(dto), "修改成功!");
    }

    /**
     * 删除项目
     *
     * @return
     */
    @OperLog(title = "删除项目", code = "10003", operatorType = OperatorType.TENANT, businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public ApiResult<Boolean> delete(@Validated @RequestBody ProjectDeleteDto dto) {
        return ApiResult.success(projectInfoService.delete(dto), "删除成功!");
    }
}
