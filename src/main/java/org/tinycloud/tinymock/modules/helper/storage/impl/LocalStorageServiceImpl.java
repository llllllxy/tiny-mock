package org.tinycloud.tinymock.modules.helper.storage.impl;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.utils.DateUtils;
import org.tinycloud.tinymock.common.utils.FileTools;
import org.tinycloud.tinymock.modules.helper.storage.api.StorageService;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageFile;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageStreamFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-11-05 14:25
 */
@Service
@ConditionalOnProperty(prefix = "tinymock", name = "storageType", havingValue = "local")
public class LocalStorageServiceImpl implements StorageService {
    final static Logger log = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    @Override
    public StorageStreamFile findById(String filePath) {
        StorageStreamFile storageStreamFile = null;
        // 获取文件路径
        String realpath = applicationConfig.getUploadPath() + filePath;
        Path path = Paths.get(realpath);
        try {
            InputStream inputStream = Files.newInputStream(path);
            storageStreamFile = new StorageStreamFile();
            storageStreamFile.setInputStream(inputStream);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("MinioStorageServiceImpl - findById - 下载文件异常：", e);
            }
        }
        return storageStreamFile;
    }

    @Override
    public StorageFile store(InputStream is, String filename) {
        return store(is, filename, null, null);
    }

    @Override
    public StorageFile store(InputStream is, String filename, String contentType) {
        return store(is, filename, contentType, null);
    }

    @Override
    public StorageFile store(InputStream is, String filename, Map<String, String> metaData) {
        return store(is, filename, null, metaData);
    }

    @Override
    public StorageFile store(InputStream is, String filename, String contentType, Map<String, String> metaData) {
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        String datePath = DateUtils.today("/yyyy/MM/dd/");
        String folderPath = applicationConfig.getUploadPath() + datePath;
        // 文件后缀
        String extension = FileTools.getFileExtension(filename);
        String newFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        StorageFile storageFile = null;
        // 根据folderPath创建文件夹
        Path path = Paths.get(folderPath + newFilename);
        try {
            storageFile = new StorageFile();
            storageFile.setFileName(filename);
            storageFile.setFileNameNew(newFilename);
            byte[] fileBytes = IOUtils.toByteArray(is);
            storageFile.setMd5(FileTools.getFileMD5(fileBytes));
            storageFile.setSha1(FileTools.getFileSHA1(fileBytes));
            storageFile.setLength(fileBytes.length);
            storageFile.setContentType(contentType);
            storageFile.setMetaData(metaData);
            storageFile.setUploadDate(new Date());

            // 文件保存到指定位置
            Files.write(path, fileBytes, StandardOpenOption.CREATE);
            // 上传文件的详细路径(不带basePath)
            String filePath = datePath + newFilename;
            storageFile.setFileId(filePath);
        } catch (IOException e) {
            log.error(filename + "store to " + folderPath + " IOException: ", e);
        }
        return storageFile;
    }

    @Override
    public boolean deleteById(String filePath) {
        // 获取文件路径
        String realpath = applicationConfig.getUploadPath() + filePath;
        try {
            return Files.deleteIfExists(Paths.get(realpath));
        } catch (IOException e) {
            log.error("delete the filePath=" + filePath + " file IOException: ", e);
            return false;
        }
    }

    @Override
    public String getExpiryUrlById(String fileId, Integer expires) {
        return null;
    }

    @Override
    public String getExpiryUrlById(String fileId) {
        return null;
    }
}
