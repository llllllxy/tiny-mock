package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.dto.MailConfigEditDto;
import org.tinycloud.tinymock.modules.bean.vo.MailConfigVo;
import org.tinycloud.tinymock.modules.service.MailConfigService;


@RestController
@RequestMapping("/mailconfig")
public class MailConfigController {

    @Autowired
    private MailConfigService mailConfigService;

    @GetMapping("/detail")
    public ApiResult<MailConfigVo> detail() {
        return ApiResult.success(mailConfigService.detail(), "获取成功");
    }

    @PostMapping("/edit")
    public ApiResult<Boolean> edit(@Validated @RequestBody MailConfigEditDto dto) {
        return ApiResult.success(mailConfigService.edit(dto), "修改成功");
    }
}
