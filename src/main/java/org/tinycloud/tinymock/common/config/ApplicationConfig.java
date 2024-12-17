package org.tinycloud.tinymock.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 映射项目配置为配置类
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-12-05 15:29
 */
@Component
public class ApplicationConfig {

    /**
     * 系统名称
     */
    @Value("${tinymock.name}")
    private String name;

    /**
     * 系统版本
     */
    @Value("${tinymock.version}")
    private String version;

    /**
     * 系统数据库类型
     */
    @Value("${tinymock.dbType:mysql}")
    private String dbType;

    /**
     * 文件存储类型 local / minio / oss / tos
     */
    @Value("${tinymock.storageType}")
    private String storageType;

    /**
     * 文件存储路径（当storageType=local需要用到）
     */
    @Value("${tinymock.uploadPath}")
    private String uploadPath;

    /**
     * 租户系统会话时长
     */
    @Value("${tinymock.tenantAuthTimeout:1800}")
    private Integer tenantAuthTimeout = 1800;

    /**
     * 项目导出备份加密密钥（sm4）
     */
    @Value("${tinymock.projectExportDek}")
    private String projectExportDek;

    /**
     * jwt密钥
     */
    @Value("${tinymock.jwtSecret}")
    private String jwtSecret;

    /**
     * minio-地址
     */
    @Value("${tinymock.minio.endpoint}")
    private String minioEndpoint;

    /**
     * minio-用户名
     */
    @Value("${tinymock.minio.accessKey}")
    private String minioAccessKey;

    /**
     * minio-密码
     */
    @Value("${tinymock.minio.secretKey}")
    private String minioSecretKey;

    /**
     * minio-存储桶
     */
    @Value("${tinymock.minio.bucket}")
    private String minioBucket;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDbType() {
        return dbType;
    }

    public String getStorageType() {
        return storageType;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public Integer getTenantAuthTimeout() {
        return tenantAuthTimeout;
    }

    public String getProjectExportDek() {
        return projectExportDek;
    }

    public String getMinioEndpoint() {
        return minioEndpoint;
    }

    public String getMinioAccessKey() {
        return minioAccessKey;
    }

    public String getMinioSecretKey() {
        return minioSecretKey;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
}
