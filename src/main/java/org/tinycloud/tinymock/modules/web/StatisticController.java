package org.tinycloud.tinymock.modules.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.modules.bean.dto.StatisticQueryDto;
import org.tinycloud.tinymock.modules.bean.vo.StatisticDataVo;
import org.tinycloud.tinymock.modules.service.StatisticService;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-04-2024/4/9 21:54
 */
@Slf4j
@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @PostMapping("/query")
    public ApiResult<PageModel<StatisticDataVo>> query(@Validated @RequestBody StatisticQueryDto dto) {
        return ApiResult.success(statisticService.query(dto), "查询成功！");
    }
}
