package org.tinycloud.tinymock.modules.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.enums.CoreErrorCode;
import org.tinycloud.tinymock.common.exception.CoreException;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.common.utils.DateUtils;
import org.tinycloud.tinymock.common.utils.FileTools;
import org.tinycloud.tinymock.modules.bean.entity.TUploadFile;
import org.tinycloud.tinymock.modules.bean.vo.UploadFileVo;
import org.tinycloud.tinymock.modules.mapper.UploadFileMapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-25 11:16
 */
@Slf4j
@Service
public class UploadFileService {

    // 传入的64位编码带有头部
    private static final int WITH_HEAD = 2;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private UploadFileMapper uploadFileMapper;

    public UploadFileVo put(MultipartFile multipartFile, String fileName) {
        // 文件原名称
        String oldName;
        if (StringUtils.hasLength(fileName)) {
            oldName = fileName;
        } else {
            oldName = multipartFile.getOriginalFilename();
        }
        // 文件后缀
        String extension = FileTools.getFileExtension(multipartFile.getOriginalFilename());
        // 新文件名
        String newName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        // 文件大小（单位B）
        long fileSize = multipartFile.getSize();

        // 文件类型
        String contentType = multipartFile.getContentType();
        String md5 = FileTools.getFileMD5(multipartFile);
        String sha1 = FileTools.getFileMD5(multipartFile);

        // 在 basePath 文件夹中通过日期对上传的文件归类保存
        // 比如：/2019/06/06/cf13891e4b95400081ebb6d70ae44930.png
        String datePath = DateUtils.today("/yyyy/MM/dd/");
        String folderPath = applicationConfig.getUploadPath() + datePath;
        // 根据folderPath创建文件夹
        Path folder = Paths.get(folderPath);
        try {
            Files.createDirectories(folder);
            // 文件保存到指定位置
            multipartFile.transferTo(folder.resolve(newName));

            // 上传文件的详细路径(不带basePath)
            String filePath = datePath + newName;

            // 保存文件信息到数据库
            TUploadFile uploadFile = new TUploadFile();
            uploadFile.setFileNameOld(oldName);
            uploadFile.setFileNameNew(newName);
            uploadFile.setFileSize((fileSize));
            uploadFile.setFileSizeStr(FileTools.formatFileSize(fileSize));
            uploadFile.setFileSuffix(extension);
            uploadFile.setFilePath(filePath);
            uploadFile.setFileMd5(md5);
            uploadFile.setFileSha1(sha1);

            // 将文件数据存入数据库
            this.uploadFileMapper.insert(uploadFile);
            return BeanConvertUtils.convertTo(uploadFile, UploadFileVo::new);
        } catch (IOException e) {
            log.error("UploadFileService -- put -- IOException = {e}", e);
            throw new CoreException(CoreErrorCode.FILE_UPLOAD_FAILED);
        }
    }


    public UploadFileVo putBase64(String base64Str, String fileName) {
        String[] tempFileArr = base64Str.split(",");
        if (tempFileArr.length == WITH_HEAD) {
            base64Str = tempFileArr[1];
        }
        // 文件原名称，要求传的文件名必须带有后缀扩展名
        String oldName = fileName;
        // 文件后缀
        String extension = FileTools.getFileExtension(fileName);
        // 新文件名
        String newName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        // 在 basePath 文件夹中通过日期对上传的文件归类保存
        // 比如：/2022/12/14/cf13891e4b95400081ebb6d70ae44930.png
        String datePath = DateUtils.today("/yyyy/MM/dd/");
        String folderPath = applicationConfig.getUploadPath() + datePath;
        // 根据folderPath创建文件夹
        Path folder = Paths.get(folderPath);
        try {
            byte[] byteArr = Base64.getDecoder().decode(base64Str);
            String md5 = FileTools.getFileMD5(byteArr);
            String sha1 = FileTools.getFileMD5(byteArr);
            long fileSize = byteArr.length;
            Files.createDirectories(folder);
            // 文件保存到指定位置
            Files.write(folder.resolve(newName), byteArr);

            // 上传文件的详细路径(不带basePath)
            String filePath = datePath + newName;
            // 保存文件信息到数据库
            TUploadFile uploadFile = new TUploadFile();
            uploadFile.setFileNameOld(oldName);
            uploadFile.setFileNameNew(newName);
            uploadFile.setFileSize(fileSize);
            uploadFile.setFileSizeStr(FileTools.formatFileSize(fileSize));
            uploadFile.setFileSuffix(extension);
            uploadFile.setFilePath(filePath);
            uploadFile.setFileMd5(md5);
            uploadFile.setFileSha1(sha1);

            // 将文件存入数据库
            this.uploadFileMapper.insert(uploadFile);

            return BeanConvertUtils.convertTo(uploadFile, UploadFileVo::new);
        } catch (IOException e) {
            log.error("UploadFileService -- putBase64 -- IOException = {e}", e);
            throw new CoreException(CoreErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public void get(Long id, HttpServletResponse response) {
        // 根据fileId获取文件信息
        TUploadFile uploadFile = this.uploadFileMapper.selectById(id);
        // 如若文件不存在，则直接返回
        if (Objects.isNull(uploadFile)) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 获取文件路径
            String realpath = applicationConfig.getUploadPath() + uploadFile.getFilePath();
            Path filePath = Paths.get(realpath);
            // 设置文件下载方式：附件下载
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(uploadFile.getFileNameOld(), "UTF-8"));
            response.setContentLengthLong(uploadFile.getFileSize());
            // 下载文件
            Files.copy(filePath, outputStream);
        } catch (Exception e) {
            log.error("下载fileId为: " + id + " 的文件出现异常", e);
            throw new CoreException(CoreErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    public boolean delete(Long id) {
        // 根据fileId获取文件信息
        TUploadFile uploadFile = this.uploadFileMapper.selectById(id);
        // 如若文件不存在，则直接返回
        if (Objects.isNull(uploadFile)) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        // 获取文件路径
        String realpath = applicationConfig.getUploadPath() + uploadFile.getFilePath();
        try {
            Files.deleteIfExists(Paths.get(realpath));
            // 删除数据库信息
            int num = this.uploadFileMapper.deleteById(id);
            return num > 0;
        } catch (Exception e) {
            log.error("删除fileId为: " + id + " 的文件出现异常", e);
            throw new CoreException(CoreErrorCode.FILE_DELETE_FAILED);
        }
    }

    /**
     * 下载一个图片
     *
     * @param id       文件id
     * @param sizes    图片缩略尺寸，可不传，不传的话则返回原始图片；传的话格式为width*height
     * @param response HttpServletResponse
     */
    public void getImage(Long id, String sizes, HttpServletResponse response) {
        // 根据fileId获取文件信息
        TUploadFile uploadFile = this.uploadFileMapper.selectById(id);
        // 如若文件不存在，则直接返回
        if (Objects.isNull(uploadFile)) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        // 获取文件路径
        String realpath = applicationConfig.getUploadPath() + uploadFile.getFilePath();
        Path filePath = Paths.get(realpath);
        try (InputStream inputStream = Files.newInputStream(filePath);
             ServletOutputStream outputStream = response.getOutputStream()) {

            // 设置文件下载方式：附件下载
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(uploadFile.getFileNameOld(), "UTF-8"));
            response.setContentLengthLong(uploadFile.getFileSize());

            // 没有此参数则返回图片上传原始尺寸图片
            if (StringUtils.hasText(sizes)) {
                int w = Integer.parseInt(sizes.split("\\*")[0]);
                int h = Integer.parseInt(sizes.split("\\*")[1]);
                // 进行图片裁剪压缩
                Thumbnails.of(inputStream).size(w, h)
                        .outputFormat(uploadFile.getFileSuffix())  // 设置输出格式
                        .toOutputStream(outputStream);
            } else {
                // 下载文件
                Files.copy(filePath, outputStream);
            }
        } catch (Exception e) {
            log.error("下载fileId为: " + id + " 的文件出现异常", e);
            throw new CoreException(CoreErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    /**
     * 预览一个图片
     *
     * @param id       文件id
     * @param sizes    图片缩略尺寸，可不传，不传的话则返回原始图片；传的话格式为width*height
     * @param response HttpServletResponse
     */
    public void previewImage(Long id, String sizes, HttpServletResponse response) {
        // 根据fileId获取文件信息
        TUploadFile uploadFile = this.uploadFileMapper.selectById(id);
        // 如若文件不存在，则直接返回
        if (Objects.isNull(uploadFile)) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        // 获取文件路径
        String realpath = applicationConfig.getUploadPath() + uploadFile.getFilePath();
        Path filePath = Paths.get(realpath);
        try (InputStream inputStream = Files.newInputStream(filePath);
             ServletOutputStream outputStream = response.getOutputStream()) {

            // 设置文件为预览
            response.setHeader("Content-Type", "image/jpeg");

            // 没有此参数则返回图片上传原始尺寸图片
            if (StringUtils.hasText(sizes)) {
                int w = Integer.parseInt(sizes.split("\\*")[0]);
                int h = Integer.parseInt(sizes.split("\\*")[1]);
                // 进行图片裁剪压缩
                Thumbnails.of(inputStream).size(w, h)
                        .outputFormat(uploadFile.getFileSuffix())  // 设置输出格式
                        .toOutputStream(outputStream);
            } else {
                // 下载文件
                Files.copy(filePath, outputStream);
            }
        } catch (Exception e) {
            log.error("下载fileId为: " + id + " 的文件出现异常", e);
            throw new CoreException(CoreErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }
}
