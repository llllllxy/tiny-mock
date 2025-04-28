package org.tinycloud.tinymock.modules.helper.storage.api;


import org.tinycloud.tinymock.modules.helper.storage.model.StorageFile;
import org.tinycloud.tinymock.modules.helper.storage.model.StorageStreamFile;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 存储层接口
 * @author liuxingyu01
 * @date 2021-07-09-9:11
 **/
public interface StorageService {

    /**
     * 根据文件id获取文件
     *
     * @param fileId
     * @return
     */
    StorageStreamFile findById(String fileId);

    /**
     * @param is
     * @param filename
     * @return
     */
     StorageFile store(InputStream is, String filename);

    /**
     * @param is
     * @param filename
     * @param contentType
     * @return
     */
     StorageFile store(InputStream is, String filename, String contentType);

    /**
     * @param is
     * @param filename
     * @param contentType
     * @param metaData
     * @return
     */
     StorageFile store(InputStream is, String filename, String contentType, Map<String, String> metaData);


    /**
     * @param is
     * @param filename
     * @param metaData
     * @return
     * @Description:
     * @Date 2021/2/26 10:10
     */
     StorageFile store(InputStream is, String filename, Map<String, String> metaData);


    /**
     * 根据文件id删除文件
     *
     * @param fileId
     * @return
     */
     boolean deleteById(String fileId);

    String getExpiryUrlById(String fileId, Integer expires, TimeUnit timeUnit);

    String getExpiryUrlById(String fileId);
}
