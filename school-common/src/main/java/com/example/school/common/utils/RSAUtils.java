package com.example.school.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/13 16:37
 * description: 非对称加密算法RSA算法组件
 * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 */
public class RSAUtils {
    //非对称密钥算法
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 1024;
    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }

    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] key) {
        return Base64.encodeBase64String(key);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //初始化密钥
//        //生成密钥对
        Map<String, Object> keyMap = RSAUtils.initKey();
//        //公钥
//        String publicKey = RSAUtils.getPublicKey(keyMap);
//
//        //私钥
//        String privateKey = RSAUtils.getPrivateKey(keyMap);
//
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTCrGsS/CSdwMsPdHICRajFzi8KSn7d2Btw+G+ci564RRK7uA5GORJxM8t+lFHncqOtGKJquXrT1n6cACzC+m/jJlu7r3omSO7Nk3IXICiHwnRGhcvakP3tjeVpxqr3ShoHkYqfYNgDbotc8XAo64Affh0c+jGYsfTttBrjdanKQIDAQAB";
//        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJMKsaxL8JJ3Ayw90cgJFqMXOLwpKft3YG3D4b5yLnrhFEru4DkY5EnEzy36UUedyo60Yomq5etPWfpwALML6b+MmW7uveiZI7s2TchcgKIfCdEaFy9qQ/e2N5WnGqvdKGgeRip9g2ANui1zxcCjrgB9+HRz6MZix9O20GuN1qcpAgMBAAECgYBsL7MX/OutJ8l61KUz05QHPP+uWW8dKPEW8cd3UxVlrZM7qtPozqqwqBBzWLhSxAMTyh6yAb6WylR+kcfB9KLvAdu2IwTPYqKNKNSKLyZVzTXm5DxLDQDtay2+TP59Mpq9ZwA2xKmqOEKS2zUiAUuIU/WvEHH3wDhkCIH2BPRprQJBAMWkLIqoJahdqT82YnhiFrpd+M48pyvMuR+tyyeyRJSn3TPg/FBnAEZvTwfKKN6t/SvjyiGdUmVd1C1+vA5POCMCQQC+dau6/tRMiAokkiJKtbYIeupk8uZFp4oKYdZgJMJRapxUs5uXosZfRE+8PZxKZjMNXsJW1Xe5/NNawhpofZJDAkB7g89mVBE8uFP2Mkm1zC9CB0pfsR4UTFwBRT6qL0mW0ZV3P4rPwJ82ZYexaZDDIV8QF8qR2VzOBqZ8TDDAjJebAkAZhY8jma49KyWgzOxHcp4X+NCcAmiVAORgi8e8TnCzlEOqnf7FjQxkC7Vbli3xUkyZCA+mryhCBf0UdlovklQdAkAcSw63rhqBwrvHfZRdbAsBXk3otzmb+2cueAlnxl1Fur1NTjkReplqVVduzbIp3HtNmdc73NOQ9tRU6Kc015Gm";

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkMWUosmatAqweMO/WazJ7DYaa\n" +
                "Nq90L9vHiR5O9+ml8PQpdDCChzGZWQ2NC8kNu6rE3X9f08lP5zenpRNZb2rMIp5D\n" +
                "roz435EPxjJLPagpA/rH3kDp1sT6bqz9S9nXAoJRmuuKuJuml1UQHzBwPftOHHlw\n" +
                "L4Qn80yFldgnJIttOQIDAQAB";

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAOQxZSiyZq0CrB4w\n" +
                "79ZrMnsNhpo2r3Qv28eJHk736aXw9Cl0MIKHMZlZDY0LyQ27qsTdf1/TyU/nN6el\n" +
                "E1lvaswinkOujPjfkQ/GMks9qCkD+sfeQOnWxPpurP1L2dcCglGa64q4m6aXVRAf\n" +
                "MHA9+04ceXAvhCfzTIWV2Ccki205AgMBAAECgYAagRI6093RIopTR2PUB8dpO3zy\n" +
                "1H80g4qHdomqqmjc+UuitPgdcQ51vT4xr7i+e5muG/v7aWHsgfKY6jeBH8vLiddn\n" +
                "cndh7KTXDyLgk0YYz0AISXNBoUnlpHTnvdwVwNjoSuYQiueZMTJ0hO0r4iKpvO67\n" +
                "rFv8/CPt33+Rbas4AQJBAPwyF3206nCFTJq7UzqmTgTzXnPYjeqfw/8uZrrAD874\n" +
                "5zZ9JZ5IQarln6vycdg6OVD+jOwA0vHUFt7aBFdH+cECQQDnopyXHzToKNRjw+0t\n" +
                "bVZxv/6wG064gP6HWqnYZ4vYPV722VxTfnBU1o/QrgZ+BHtzWcY8qeU2LFvEIK10\n" +
                "dKF5AkArcBVLuZWHu+3t2MYFHA/kGmbyXSJUfyArG7rl/565b3WjlPqdwKRO4y4V\n" +
                "V7T0IVMt+CodnCa5MGagC70jq2oBAkBjuichCVaF/c4zhc8/l6t4HelRePBTj3YG\n" +
                "YIpvmWUkHIgeT0boy68hV59jgQlaiCN+blBjHeKJiF+Z+Ve0o+JBAkAD8oVa1jvG\n" +
                "2MsUjGK3L0RH1gVUlqxrN9Uh62ewbA9ASaVICO83aDGLO2E3hgPZkJNALSKOAl/f\n" +
                "vX+A42luiw8X";

        System.out.println("公钥：\n" + publicKey);
        System.out.println("私钥：\n" + privateKey);


        System.out.println("================密钥对构造完毕 开始进行加密数据的传输=============");
        String str = "lixin123";
        System.out.println("原文:" + str);

        //公钥加密
        System.out.println("\n===========公钥加密==============");
        byte[] code1 = RSAUtils.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
        System.out.println("===========私钥解密==============");
        //乙方进行数据的解密
        byte[] decode1 = RSAUtils.decryptByPrivateKey(code1, privateKey);
        System.out.println("解密后的数据：" + new String(decode1) + "\n\n");

//        私钥加密
//        System.out.println("\n===========私钥加密==============");
//        byte[] code1 = RSAUtils.encryptByPrivateKey(str.getBytes(), privateKey);
//        System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
//        System.out.println("===========公钥解密==============");
//        //公钥解密
//        byte[] decode1 = RSAUtils.decryptByPublicKey(code1, publicKey);
//        System.out.println("解密后的数据：" + new String(decode1) + "\n\n");

//
    }
}
