package org.tinycloud.tinymock.modules.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-25 11:38
 */
@Getter
@Setter
@ToString
public class UploadFileVo implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    /**
     * 文件原名称
     */
    private String fileNameOld;

    /**
     * 文件新名称
     */
    private String fileNameNew;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小（单位b）
     */
    private Long fileSize;

    /**
     * 文件大小（带单位,KB）
     */
    private String fileSizeStr;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件md5
     */
    private String fileMd5;

    /**
     * 文件sha1
     */
    private String fileSha1;

    private Date createdAt;
}
