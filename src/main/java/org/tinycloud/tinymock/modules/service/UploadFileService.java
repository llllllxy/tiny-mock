package org.tinycloud.tinymock.modules.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.enums.error.CoreErrorCode;
import org.tinycloud.tinymock.common.exception.CoreException;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.common.utils.FileTools;
import org.tinycloud.tinymock.modules.bean.entity.TUploadFile;
import org.tinycloud.tinymock.modules.bean.vo.UploadFileVo;
import org.tinycloud.tinymock.modules.helper.storage.api.StorageService;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageFile;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageStreamFile;
import org.tinycloud.tinymock.modules.mapper.UploadFileMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Objects;

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
    private StorageService storageService;

    @Autowired
    private UploadFileMapper uploadFileMapper;

    public String imageToBase64(Long id) {
        // 根据fileId获取文件信息
        TUploadFile uploadFile = this.uploadFileMapper.selectById(id);
        // 如若文件不存在，则直接返回
        if (Objects.isNull(uploadFile)) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        StorageStreamFile storageStreamFile = storageService.findById(uploadFile.getFilePath());
        if (storageStreamFile == null) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        try (InputStream inputStream = storageStreamFile.getInputStream()) {
            if (uploadFile.getFileSuffix().equals("jpg")) {
                uploadFile.setFileSuffix("jpeg");
            }
            return "data:image/" + uploadFile.getFileSuffix() + ";base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            log.error("获取fileId为: " + id + " 的文件出现异常", e);
            throw new CoreException(CoreErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    public UploadFileVo put(MultipartFile multipartFile, String fileName) {
        // 文件原名称
        String oldName;
        if (StringUtils.hasLength(fileName)) {
            oldName = fileName;
        } else {
            oldName = multipartFile.getOriginalFilename();
        }
        // 文件类型
        String contentType = multipartFile.getContentType();
        try (InputStream inputStream = multipartFile.getInputStream()){
            StorageFile storageFile = storageService.store(inputStream, oldName, contentType);
            // 保存文件信息到数据库
            TUploadFile uploadFile = new TUploadFile();
            uploadFile.setFileNameOld(oldName);
            uploadFile.setFileNameNew(storageFile.getFileNameNew());
            uploadFile.setFileSize(storageFile.getLength());
            uploadFile.setFileSizeStr(FileTools.formatFileSize(storageFile.getLength()));
            uploadFile.setFileSuffix(FileTools.getFileExtension(oldName));
            uploadFile.setFilePath(storageFile.getFileId());
            uploadFile.setFileMd5(storageFile.getMd5());
            uploadFile.setFileSha1(storageFile.getSha1());
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
        byte[] byteArr = Base64.getDecoder().decode(base64Str);
        try (InputStream inputStream = new ByteArrayInputStream(byteArr)){
            StorageFile storageFile = storageService.store(inputStream, fileName);
            // 保存文件信息到数据库
            TUploadFile uploadFile = new TUploadFile();
            uploadFile.setFileNameOld(fileName);
            uploadFile.setFileNameNew(storageFile.getFileNameNew());
            uploadFile.setFileSize(storageFile.getLength());
            uploadFile.setFileSizeStr(FileTools.formatFileSize(storageFile.getLength()));
            uploadFile.setFileSuffix(FileTools.getFileExtension(fileName));
            uploadFile.setFilePath(storageFile.getFileId());
            uploadFile.setFileMd5(storageFile.getMd5());
            uploadFile.setFileSha1(storageFile.getSha1());
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
        StorageStreamFile storageStreamFile = storageService.findById(uploadFile.getFilePath());
        if (storageStreamFile == null) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        try (ServletOutputStream outputStream = response.getOutputStream();
             InputStream inputStream = storageStreamFile.getInputStream()) {
            // 设置文件下载方式：附件下载
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(uploadFile.getFileNameOld(), "UTF-8"));
            response.setContentLengthLong(uploadFile.getFileSize());
            // 下载文件
            IOUtils.copy(inputStream, outputStream);
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
        try {
            storageService.deleteById(uploadFile.getFilePath());
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
        StorageStreamFile storageStreamFile = storageService.findById(uploadFile.getFilePath());
        if (storageStreamFile == null) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        try (InputStream inputStream = storageStreamFile.getInputStream();
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
                IOUtils.copy(inputStream, outputStream);
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
        StorageStreamFile storageStreamFile = storageService.findById(uploadFile.getFilePath());
        if (storageStreamFile == null) {
            throw new CoreException(CoreErrorCode.FILE_NOT_EXIST);
        }
        try (InputStream inputStream = storageStreamFile.getInputStream();
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
                IOUtils.copy(inputStream, outputStream);
            }
        } catch (Exception e) {
            log.error("下载fileId为: " + id + " 的文件出现异常", e);
            throw new CoreException(CoreErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }
}
