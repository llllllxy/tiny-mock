package org.tinycloud.tinymock.modules.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.annotation.OperLog;
import org.tinycloud.tinymock.common.enums.BusinessType;
import org.tinycloud.tinymock.common.enums.OperatorType;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.modules.bean.dto.*;
import org.tinycloud.tinymock.modules.bean.vo.MockAccessLogVo;
import org.tinycloud.tinymock.modules.bean.vo.MockInfoHistoryVo;
import org.tinycloud.tinymock.modules.bean.vo.MockInfoVo;
import org.tinycloud.tinymock.modules.service.MockAccessLogService;
import org.tinycloud.tinymock.modules.service.MockInfoHistoryService;
import org.tinycloud.tinymock.modules.service.MockInfoService;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-14 11:26
 */
@RestController
@RequestMapping("/mockinfo")
public class MockInfoController {

    @Autowired
    private MockInfoService mockInfoService;

    @Autowired
    private MockInfoHistoryService mockInfoHistoryService;

    @Autowired
    private MockAccessLogService mockAccessLogService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ApiResult<PageModel<MockInfoVo>> query(@RequestBody MockInfoQueryDto dto) {
        return ApiResult.success(mockInfoService.query(dto), "查询成功!");
    }

    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public ApiResult<PageModel<MockInfoHistoryVo>> history(@RequestBody MockInfoHistoryQueryDto dto) {
        return ApiResult.success(mockInfoHistoryService.query(dto), "查询成功!");
    }

    @RequestMapping(value = "/accessLog", method = RequestMethod.POST)
    public ApiResult<PageModel<MockAccessLogVo>> accessLog(@RequestBody MockAccessLogQueryDto dto) {
        return ApiResult.success(mockAccessLogService.query(dto), "查询成功!");
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ApiResult<MockInfoVo> detail(@RequestParam("id") Long id) {
        return ApiResult.success(mockInfoService.detail(id), "获取成功!");
    }

    @OperLog(title = "删除接口", code = "10011", operatorType = OperatorType.TENANT, businessType = BusinessType.DELETE)
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ApiResult<Boolean> delete(@RequestParam("id") Long id) {
        return ApiResult.success(mockInfoService.delete(id), "删除成功!");
    }

    @OperLog(title = "启用接口", code = "10012", operatorType = OperatorType.TENANT, businessType = BusinessType.ENABLE)
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    public ApiResult<Boolean> enable(@RequestParam("id") Long id) {
        return ApiResult.success(mockInfoService.enable(id), "启用成功!");
    }

    @OperLog(title = "禁用接口", code = "10013", operatorType = OperatorType.TENANT, businessType = BusinessType.DISABLE)
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    public ApiResult<Boolean> disable(@RequestParam("id") Long id) {
        return ApiResult.success(mockInfoService.disable(id), "禁用成功!");
    }

    @OperLog(title = "新增接口", code = "10014", operatorType = OperatorType.TENANT, businessType = BusinessType.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResult<Boolean> add(@Validated @RequestBody MockInfoAddDto dto) {
        return ApiResult.success(mockInfoService.add(dto), "新增成功!");
    }

    @OperLog(title = "修改接口", code = "10015", operatorType = OperatorType.TENANT, businessType = BusinessType.UPDATE)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ApiResult<Boolean> edit(@Validated @RequestBody MockInfoEditDto dto) {
        return ApiResult.success(mockInfoService.edit(dto), "修改成功!");
    }

    @OperLog(title = "导出接口", code = "10016", operatorType = OperatorType.TENANT, businessType = BusinessType.EXPORT)
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(@RequestParam("projectId") Long projectId, HttpServletResponse response) {
        mockInfoService.export(projectId, response);
    }

    @OperLog(title = "导入项目", code = "10017", operatorType = OperatorType.TENANT, businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public ApiResult<Boolean> importZip(@RequestParam("file") MultipartFile multipartFile,
                                        @RequestParam(value = "projectId", required = false) Long projectId,
                                        @RequestParam(value = "fileName", required = false) String fileName) {
        return ApiResult.success(mockInfoService.importZip(multipartFile, fileName, projectId), "上传成功！");
    }
}
