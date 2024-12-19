package org.tinycloud.tinymock.modules.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.common.annotation.OperLog;
import org.tinycloud.tinymock.common.enums.BusinessType;
import org.tinycloud.tinymock.common.enums.OperatorType;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.dto.FeedbackAddDto;
import org.tinycloud.tinymock.modules.service.FeedbackService;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-19 15:31
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 新增
     *
     * @return
     */
    @OperLog(title = "新增反馈信息", code = "10041", operatorType = OperatorType.TENANT, businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public ApiResult<Boolean> add(@Validated @RequestBody FeedbackAddDto dto) {
        return ApiResult.success(feedbackService.add(dto), "新增成功!");
    }
}
