package org.tinycloud.tinymock.modules.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-25 11:11
 */
@Getter
@Setter
@TableName("t_upload_file")
public class TUploadFile implements Serializable {
    private static final long serialVersionUID = -1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件原名称
     */
    @TableField("file_name_old")
    private String fileNameOld;

    /**
     * 文件新名称
     */
    @TableField("file_name_new")
    private String fileNameNew;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件大小（单位b）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件大小（带单位,KB）
     */
    @TableField("file_size_str")
    private String fileSizeStr;

    /**
     * 文件后缀
     */
    @TableField("file_suffix")
    private String fileSuffix;

    /**
     * 文件md5
     */
    @TableField("file_md5")
    private String fileMd5;

    /**
     * 文件sha1
     */
    @TableField("file_sha1")
    private String fileSha1;


    @TableField("created_at")
    private Date createdAt;
}
