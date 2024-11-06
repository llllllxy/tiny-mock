package org.tinycloud.tinymock.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author liuxingyu01
 * @date 2022-12-14 10:55
 * @description 文件处理工具类
 **/
public class FileTools {
    private final static Logger logger = LoggerFactory.getLogger(FileTools.class);


    /**
     * 获取文件后缀
     *
     * @param originalFilename 原始文件名
     * @return 文件后缀扩展名
     */
    public static String getFileSuffix(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        String[] arr = originalFilename.split("\\.");
        if (arr.length == 1) {
            return "";
        }
        return arr[arr.length - 1];
    }

    /**
     * 获取文件扩展名(返回小写)
     *
     * @param fileName 文件名
     * @return 例如：test.jpg  返回：jpg
     */
    public static String getFileExtension(String fileName) {
        if ((fileName == null) || (fileName.lastIndexOf(".") == -1)
                || (fileName.lastIndexOf(".") == fileName.length() - 1)) {
            return null;
        }
        String str = fileName.substring(fileName.lastIndexOf(".") + 1);
        return str.toLowerCase();
    }

    /**
     * 获取文件名
     *
     * @param originalFilename 原始文件名
     * @return 例如：test.jpg  返回：test
     */
    public static String getFilePrefix(String originalFilename) {
        if (originalFilename == null) {
            return null;
        }
        if (originalFilename.contains(".")) {
            int splitIndex = originalFilename.lastIndexOf(".");
            return originalFilename.substring(0, splitIndex);
        }
        return originalFilename;
    }


    /**
     * 文件输入流转 byte[]
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }


    /**
     * Image转BufferedImage
     *
     * @param image Image
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            //logger.error("The system does not have a screen");
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * 获取文件的MD5值
     *
     * @param uploadBytes byte[]
     * @return 字符串
     */
    public static String getFileMD5(byte[] uploadBytes) {
        String fileMd5 = "";
        try {
            fileMd5 = DigestUtils.md5Hex(uploadBytes);
        } catch (Exception e) {
            fileMd5 = UUID.randomUUID().toString().replace("-", "");
        }
        return fileMd5;
    }

    /**
     * 获取文件的SHA1值
     *
     * @param uploadBytes byte[]
     * @return 字符串
     */
    public static String getFileSHA1(byte[] uploadBytes) {
        String fileMd5 = "";
        try {
            fileMd5 = DigestUtils.sha1Hex(uploadBytes);
        } catch (Exception e) {
            fileMd5 = UUID.randomUUID().toString().replace("-", "");
        }
        return fileMd5;
    }

    /**
     * 字节转 KB/MB/GB 保留两位小数四舍五入
     *
     * @param size 字节大小
     * @return String
     */
    public static String formatFileSize(long size) {
        double length = Double.parseDouble(String.valueOf(size));
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (length < 1024) {
            return length + "B";
        } else {
            length = length / 1024.0;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (length < 1024) {
            return Math.round(length * 100) / 100.0 + "KB";
        } else {
            length = length / 1024.0;
        }
        if (length < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            return Math.round(length * 100) / 100.0 + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            return Math.round(length / 1024 * 100) / 100.0 + "GB";
        }
    }

    /**
     * 判断文件大小是否合法，超过指定大小返回false
     *
     * @param file MultipartFile 文件类
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     */
    public static boolean fileSizeCheck(InputStream file, int size, String unit) {
        // 获取文件实际大小
        long len = 0;
        try {
            len = file.available();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }
}
