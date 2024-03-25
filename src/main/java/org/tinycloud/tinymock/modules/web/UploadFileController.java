package org.tinycloud.tinymock.modules.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.model.ApiResult;
import org.tinycloud.tinymock.modules.bean.vo.UploadFileVo;
import org.tinycloud.tinymock.modules.service.UploadFileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件上传控制器
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-25 11:16
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadFileController {

    @Autowired
    private UploadFileService uploadService;

    @PostMapping("/put")
    public ApiResult<UploadFileVo> put(@RequestParam("file") MultipartFile multipartFile,
                                       @RequestParam(value = "fileName", required = false) String fileName) {
        return ApiResult.success(uploadService.put(multipartFile, fileName), "上传成功！");
    }

    @GetMapping("/getImage")
    public void getImage(@RequestParam(value = "sizes", required = false, defaultValue = "") String sizes,
                         @RequestParam(value = "id", required = true) Long id,
                    HttpServletResponse response) {
        uploadService.getImage(id, sizes, response);
    }
}
