package org.tinycloud.tinymock.common.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * ZIP解压压缩工具类
 * zip4j解压压缩、加密压缩、解压
 * 参考文档：https://blog.csdn.net/sanhewuyang/article/details/108108598
 * https://github.com/srikanth-lingala/zip4j
 *
 * @author liuxingyu01
 * @since 2023-01-29
 */
public class Zip4jUtils {

    /**
     * 压缩
     *
     * @param srcFilePath 要压缩的源目录或源文件-绝对路径 /opt/server/xxx
     * @param dest        生成的压缩包的目录和名字，例如： /opt/server/xxx/xxx.zip
     * @param passwd      密码 不是必填
     */
    public static void zip(String srcFilePath, String dest, String passwd) {
        File srcFile = new File(srcFilePath);

        ZipParameters par = new ZipParameters();
        par.setCompressionMethod(CompressionMethod.DEFLATE);
        // 设置压缩水平
        par.setCompressionLevel(CompressionLevel.NORMAL);
        // 可以设置压缩包内是否包含根路径，即位是否包xxx那一级目录
        par.setIncludeRootFolder(false);
        if (passwd != null && !passwd.isEmpty()) {
            par.setEncryptFiles(true);
            par.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
            try (ZipFile zipfile = new ZipFile(dest, passwd.toCharArray())) {
                if (srcFile.isDirectory()) {
                    zipfile.addFolder(srcFile, par);
                } else {
                    zipfile.addFile(srcFile, par);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (ZipFile zipfile = new ZipFile(dest)) {
                if (srcFile.isDirectory()) {
                    zipfile.addFolder(srcFile, par);
                } else {
                    zipfile.addFile(srcFile, par);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 解压zip中的所有文件
     *
     * @param zipFilePath 压缩包zip文件-绝对路径
     * @param dest        目标文件夹（要解压到哪个文件夹）-绝对路径
     * @param passwd      密码（有的话就传）
     */
    public static void unZip(String zipFilePath, String dest, String passwd) {
        try (ZipFile zFile = new ZipFile(zipFilePath)) {
            // 解决文件名中文乱码的问题
            zFile.setCharset(Charset.forName(getEncoding(zFile)));
            if (!zFile.isValidZipFile()) {
                throw new ZipException("压缩文件不合法！");
            }
            File file = new File(dest);
            if (file.isDirectory() && !file.exists()) {
                file.mkdirs();
            }
            if (zFile.isEncrypted() && passwd != null && !passwd.isEmpty()) {
                zFile.setPassword(passwd.toCharArray());
            }
            zFile.extractAll(dest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取压缩文件的编码格式
     *
     * @param zipFile 压缩包路径
     * @return 编码类型
     * @throws ZipException 抛出异常
     */
    private static String getEncoding(ZipFile zipFile) throws ZipException {
        String encoding = "GBK";
        zipFile.setCharset(Charset.forName(encoding));
        List<FileHeader> list = zipFile.getFileHeaders();
        for (int i = 0; i < list.size(); i++) {
            FileHeader fileHeader = list.get(i);
            String fileName = fileHeader.getFileName();
            if (isMessyCode(fileName)) {
                encoding = "UTF-8";
                break;
            }
        }
        return encoding;
    }

    private static boolean isMessyCode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            // 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            if ((int) c == 0xfffd) {
                // 存在乱码
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        String passwd = "123456";
        // 测试压缩
        Zip4jUtils.zip("D:/opt/coolcars/logs", "D:/opt/coolcars/logs/logs2.zip", passwd);
        // 测试解压
        //Zip4jUtils.unZip("D:/opt/coolcars/logs2.zip", "D:/opt/csp-app-server/dest", passwd);
    }
}
