package com.example.demo.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Rsa公钥私钥加密工具类
 * @author ChangLF 2023-05-25
 */
public class RSAUtil {
    private static final String RSA_ALGORITHM = "RSA";

    /**
     * 生成公钥私钥对
     * @author ChangLF 2023/5/25 14:32
     * @return java.security.KeyPair
     **/
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取字符串格式的公钥
     * @param keyPair 公钥私钥对
     * @author ChangLF 2023/5/25 14:38
     * @return java.lang.String
     **/
    public static String getPublicKeyString(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取字符串格式的私钥
     * @param keyPair 公钥私钥对
     * @author ChangLF 2023/5/25 14:40
     * @return java.lang.String
     **/
    public static String getPrivateKeyString(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * 公钥加密
     * @param data 要加密的数据
	 * @param publicKeyString 公钥
     * @author ChangLF 2023/5/25 14:32
     * @return java.lang.String 签名结果
     **/
    public static String encrypt(String data, String publicKeyString) throws Exception {
        PublicKey publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString)));
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 私钥解密
     * @param encryptedData 要解密的数据
	 * @param privateKeyString 私钥
     * @author ChangLF 2023/5/25 14:32
     * @return java.lang.String
     **/
    public static String decrypt(String encryptedData, String privateKeyString) throws Exception {
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString)));
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)), StandardCharsets.UTF_8);
    }

    /**
     * 生成签名
     * @param data 原始数据
	 * @param privateKeyString 私钥
     * @author ChangLF 2023/5/25 14:33
     * @return java.lang.String 签名
     **/
    public static String sign(String data, String privateKeyString) throws Exception {
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString)));
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 验证签名
     * @param data 原始数据
	 * @param signatureBytes 签名数据
	 * @param publicKeyString 公钥
     * @author ChangLF 2023/5/25 14:34
     * @return boolean 验证结果
     **/
    public static boolean verify(String data, String signatureBytes, String publicKeyString) throws Exception {
        PublicKey publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString)));
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(signatureBytes));
    }

//    public static void main(String[] args) throws Exception {
//        // 生成密钥对
//        KeyPair keyPair = RSAUtil.generateKeyPair();
//        String publicKeyString = getPublicKeyString(keyPair);
//        String privateKeyString = getPrivateKeyString(keyPair);
//        System.out.println("publicKeyString：   " + publicKeyString);
//        System.out.println("privateKeyString：   " + privateKeyString);
//        // 要加密的数据
//        String plainText = "Hello, RSA!";
//
//        // 加密
//        String encrypt = RSAUtil.encrypt(plainText, publicKeyString);
//        System.out.println("encrypt Text: " + encrypt);
//
//        // 解密
//        String decryptedText = RSAUtil.decrypt(encrypt, privateKeyString);
//        System.out.println("Decrypted Text: " + decryptedText);
//
//        // 签名
//        String signature = RSAUtil.sign(plainText, privateKeyString);
//
//        // 验证签名
//        boolean isVerified = RSAUtil.verify(plainText, signature, publicKeyString);
//        System.out.println("Signature Verified: " + isVerified);
//    }
}
