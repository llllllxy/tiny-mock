package org.tinycloud.tinymock.modules.helper.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.http.Method;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.utils.FileTools;
import org.tinycloud.tinymock.modules.helper.storage.api.StorageService;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageFile;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageStreamFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-11-05 13:28
 */
@Service
@ConditionalOnProperty(prefix = "tinymock", name = "storageType", havingValue = "minio")
public class MinioStorageServiceImpl implements StorageService {
    final static Logger logger = LoggerFactory.getLogger(MinioStorageServiceImpl.class);

    private static String defaultBucket = null;

    @Autowired
    private ApplicationConfig applicationConfig;


    public MinioClient getMinioClient() throws Exception {
        MinioClient minioClient = null;
        if (!StringUtils.hasText(defaultBucket)) {
            defaultBucket = applicationConfig.getMinioBucket();
        }

        if (StringUtils.hasText(applicationConfig.getMinioEndpoint())
                && StringUtils.hasText(applicationConfig.getMinioAccessKey())
                && StringUtils.hasText(applicationConfig.getMinioSecretKey())) {
            minioClient = MinioClient.builder()
                    .endpoint(applicationConfig.getMinioEndpoint())
                    .credentials(applicationConfig.getMinioAccessKey(), applicationConfig.getMinioSecretKey())
                    .build();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(defaultBucket).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(defaultBucket).build());
            }
        }
        return minioClient;
    }


    /**
     * 获取文件
     *
     * @param fileId
     * @return
     */
    @Override
    public StorageStreamFile findById(String fileId) {
        StorageStreamFile storageStreamFile = null;
        try {
            MinioClient minioClient = getMinioClient();
            // 获取文件信息
            StatObjectResponse statObjectResponse = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(defaultBucket)
                            .object(fileId)
                            .build()
            );
            Headers headers = statObjectResponse.headers();
            storageStreamFile = new StorageStreamFile();
            // userMetadata 中设置自定义属性，statObjectResponse.userMetadata()中获取不到，需要到headers中获取
            // 自定义属性在获取时需要添加前缀 X-Amz-Meta-
            // 文件名称也会发生相应的改变，例如：fileName -> Filename,description -> Description,
            storageStreamFile.setFileId(fileId);
            storageStreamFile.setMd5(headers.get("Etag"));
            storageStreamFile.setFileName(headers.get("X-Amz-Meta-Filename"));
            String Extendinfo = headers.get("X-Amz-Meta-Extendinfo");
            if (StringUtils.hasText(Extendinfo)) {
                storageStreamFile.setMetaData(new ObjectMapper().readValue(Extendinfo, Map.class));
            }
            storageStreamFile.setContentType(statObjectResponse.contentType());
            storageStreamFile.setLength(statObjectResponse.size());
            storageStreamFile.setUploadDate(Date.from(statObjectResponse.lastModified().toInstant()));
            // 获取文件
            GetObjectResponse object = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucket)
                            .object(fileId)
                            .build());
            storageStreamFile.setInputStream(object);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("MinioStorageServiceImpl - findById - 下载文件异常：", e);
            }
        }
        return storageStreamFile;
    }

    @Override
    public StorageFile store(InputStream inputStream, String fileName) {
        return store(inputStream, fileName, null, null);
    }

    @Override
    public StorageFile store(InputStream inputStream, String fileName, String contentType) {
        return store(inputStream, fileName, contentType, null);
    }

    @Override
    public StorageFile store(InputStream inputStream, String fileName, Map<String, String> metaData) {
        return store(inputStream, fileName, "application/octet-stream", metaData);
    }

    @Override
    public StorageFile store(InputStream inputStream, String fileName, String contentType, Map<String, String> metaData) {
        String fileType = FileTools.getFileExtension(fileName);
        String fileId = UUID.randomUUID().toString().replace("-", "") + "." + fileType;
        return store(fileId, inputStream, fileName, contentType, metaData);
    }

    /**
     * 上传文件
     *
     * @param fileId      文件id
     * @param inputStream 输入流
     * @param fileName    文件名字
     * @param contentType 上下文类型
     * @param metaData
     * @return
     */
    private StorageFile store(String fileId, InputStream inputStream, String fileName, String contentType, Map<String, String> metaData) {
        StorageFile storageFile = null;
        try {
            MinioClient minioClient = getMinioClient();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            if (metaData == null) {
                metaData = new HashMap<String, String>();
            }
            String fileType = FileTools.getFileExtension(fileName);
            long fileSize = inputStream.available();

            metaData.put("fileId", fileId);
            metaData.put("fileName", fileName);
            metaData.put("fileType", fileType);
            // 使用putObject上传一个文件到存储桶中。

            ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(defaultBucket)
                            .object(fileId)
                            .contentType(contentType)
                            // userMetadata 中放置的属性要到  headers 中获取
                            .userMetadata(metaData)
                            .stream(inputStream, fileSize, -1)
                            .build()
            );
            String md5 = objectWriteResponse.etag();
            storageFile = new StorageFile();
            storageFile.setFileId(fileId);
            storageFile.setFileNameNew(fileId);
            storageFile.setFileName(fileName);
            storageFile.setMd5(md5);
            storageFile.setLength(fileSize);
            storageFile.setContentType(contentType);
            storageFile.setUploadDate(new Date());
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("MinioStorageServiceImpl -- store -- 上传minio异常：", e);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("MinioStorageServiceImpl -- store -- IOException = ", e);
            }
        }
        return storageFile;
    }


    /**
     * 获取文件外链
     *
     * @param fileId  文件名称
     * @param expires 过期时间 单位小时
     * @return url
     */
    @Override
    public String getExpiryUrlById(String fileId, Integer expires) {
        if (expires < 1 || expires > (7 * 24)) {
            logger.error("MinioStorageServiceImpl -- 过期时间必须在1小时和七天之间！");
            return "";
        }
        try {
            MinioClient minioClient = getMinioClient();
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(defaultBucket)
                            .object(fileId)
                            .expiry(expires, TimeUnit.HOURS)
                            .build());
        } catch (Exception e) {
            logger.error("MinioStorageServiceImpl -- getExpiryUrlById -- Exception = {e}", e);
            return null;
        }
    }


    /**
     * 获取文件外链（默认一小时）
     *
     * @param fileId 文件名称
     * @return url
     */
    @Override
    public String getExpiryUrlById(String fileId) {
        try {
            MinioClient minioClient = getMinioClient();
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(defaultBucket)
                            .object(fileId)
                            .expiry(1, TimeUnit.HOURS) // 按小时传参数
                            //.expiry(24 * 60 * 60) // 默认单位是秒
                            //.expiry(1, TimeUnit.DAYS) // 按天传参
                            .build());
        } catch (Exception e) {
            logger.error("MinioStorageServiceImpl -- getExpiryUrlById -- Exception = {e}", e);
            return null;
        }
    }


    /**
     * 根据文件id删除文件
     *
     * @param fileId 文件id
     */
    @Override
    public boolean deleteById(String fileId) {
        try {
            MinioClient minioClient = getMinioClient();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(defaultBucket)
                            .object(fileId)
                            .build()
            );
            return true;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("MinioStorageServiceImpl -- deleteById -- Exception = {e}", e);
            }
            return false;
        }
    }
}
