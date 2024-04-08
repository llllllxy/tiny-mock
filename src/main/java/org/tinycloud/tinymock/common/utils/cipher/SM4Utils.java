package org.tinycloud.tinymock.common.utils.cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

/**
 * 国密4加密算法工具类（ECB模式）
 * 无线局域网标准的分组数据算法。对称加密算法，密钥长度和分组长度均为128位。同类型的有AES，DES等
 * 经过在线网站验证 https://lzltool.cn/SM4，算法正确
 *
 * @author liuxingyu01
 * @since 2022-03-11-16:47
 **/
public class SM4Utils {
    // 算法
    public static final String ALGORITHM_NAME = "SM4";
    // 定义分组加密模式使用：PKCS5Padding
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成密钥，一般传入128位
     *
     * @param keySize 密钥位数
     * @return byte[]
     */
    public static byte[] generateKey(int keySize) {
        try {
            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
            Security.addProvider(new BouncyCastleProvider());
            KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
            kg.init(keySize, new SecureRandom());
            return kg.generateKey().getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成128位密钥-base64格式的
     * SM4算法只支持128位的密钥（转换成byte[]后，长度应为16）
     *
     * @return String
     */
    public static String generateKey() {
        byte[] bytes = generateKey(128);
        return bytes == null ? null : Base64.toBase64String(bytes);
    }

    /**
     * sm4加密
     */
    public static byte[] encrypt(byte[] key, byte[] data) {
        try {
            Cipher cipher = generateEcbCipher(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sm4加密
     * @param keyStr 密钥（128位，base64格式）
     * @param content 要加密的字符串原文
     * @return 加密后的密文，base64格式
     */
    public static String encrypt(String keyStr, String content) {
        try {
            byte[] key = Base64.decode(keyStr);
            Cipher cipher = generateEcbCipher(Cipher.ENCRYPT_MODE, key);
            return Base64.toBase64String(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sm4解密
     */
    public static byte[] decrypt(byte[] key, byte[] data) {
        try {
            Cipher cipher = generateEcbCipher(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sm4解密
     * @param keyStr 密钥（128位，base64格式）
     * @param content 要解密的字符串密文，base64格式
     * @return 解密后的原文字符串
     */
    public static String decrypt(String keyStr, String content) {
        try {
            byte[] key = Base64.decode(keyStr);
            Cipher cipher = generateEcbCipher(Cipher.DECRYPT_MODE, key);
            byte[] decrypt = cipher.doFinal(Base64.decode(content));
            return new String(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成ECB暗号
     */
    private static Cipher generateEcbCipher(int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(SM4Utils.ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    public static void main(String[] args) {
        // 生成密钥（密钥必须16字节(128位，1个字节由8位二进制码组成)，此处生成密钥为base64编码的，转换成byte[]后，长度应为16）
        String keyStr = SM4Utils.generateKey();
        System.out.println("密钥：" + keyStr);

        // 加密
        String encodeStr = SM4Utils.encrypt(keyStr, "12345");
        System.out.println("加密后：" + encodeStr);

        // 解密
        String decrypt = SM4Utils.decrypt(keyStr, encodeStr);
        System.out.println("解密后：" + decrypt);
    }
}
