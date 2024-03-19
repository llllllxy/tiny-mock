package org.tinycloud.tinymock.modules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.modules.bean.dto.FeedbackAddDto;
import org.tinycloud.tinymock.modules.bean.entity.TFeedback;
import org.tinycloud.tinymock.modules.mapper.FeedbackMapper;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-19 15:31
 */
@Service
public class FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    public boolean add(FeedbackAddDto dto) {
        TFeedback tFeedback = new TFeedback();
        tFeedback.setContent(dto.getContent());
        tFeedback.setFeedType(dto.getFeedType());
        tFeedback.setEmail(dto.getEmail());
        tFeedback.setTenantId(TenantHolder.getTenantId());
        tFeedback.setDelFlag(GlobalConstant.NOT_DELETED);
        int num = this.feedbackMapper.insert(tFeedback);
        return num > 0;
    }
}
