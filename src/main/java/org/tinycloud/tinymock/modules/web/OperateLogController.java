package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.modules.bean.dto.OperateLogQueryDto;
import org.tinycloud.tinymock.modules.bean.vo.OperateLogVo;
import org.tinycloud.tinymock.modules.service.OperateLogService;

/**
 * <p>
 *     操作日志查询
 * </p>
 *
 * @author liuxingyu01
 * @since 2025-03-27 14:11
 */
@RestController
@RequestMapping("/operatelog")
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ApiResult<PageModel<OperateLogVo>> query(@RequestBody OperateLogQueryDto dto) {
        return ApiResult.success(operateLogService.query(dto), "查询成功!");
    }
}
