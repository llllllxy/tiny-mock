package org.tinycloud.tinymock.modules.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 *     主动转发index.html页面
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-11 10:36
 */
@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }
}
