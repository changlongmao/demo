package com.example.demo.util;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName AESUtil
 * @Description: AES加解密工具类
 * @Author zhujb
 * @Date 2020/12/18
 * @Version V1.0
 **/
public class AESUtil {

    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);

    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     *
     * @param password 加密密码
     * @param content  待加密内容
     * @return 返回Hex转码后的加密数据
     */
    public static String encrypt(String password, String content) {
        try {
            if (StringUtils.isBlank(password) || password.length() != 16) {
                throw new RuntimeException("加密秘钥不能为空且必须为16位");
            }
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password.getBytes(), KEY_ALGORITHM));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            return Hex.encodeToString(result).toUpperCase();
        } catch (Exception ex) {
            log.error("加密操作失败", ex);
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param password
     * @param content
     * @return
     */
    public static String decrypt(String password, String content) {
        try {
            if (StringUtils.isBlank(password) || password.length() != 16) {
                throw new RuntimeException("加密秘钥不能为空且必须为16位");
            }
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password.getBytes(), KEY_ALGORITHM));
            // 执行操作
            byte[] result = cipher.doFinal(Hex.decode(content));

            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("解密操作失败", ex);
        }
        return null;
    }

}
