package com.talebase.cloud.common.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Des3Util {
    //des3 加解密需要的key
    public static final String DES3_KEY = "cABlAHIAcwBvAG4AcgBlAHMAdQBtAGUA";

    //des3 加解密需要的keyiv
    public static final String DES3_KEY_IV = "YgBhAHMAZQA=";

    public static void main(String[] args) throws Exception {

        String data = "123456";
//		
//		System.out.println("ECB加密解密");
//		byte[] str3 = des3EncodeECB(key,data );
//		byte[] str4 = ees3DecodeECB(key, str3);
//		System.out.println(new BASE64Encoder().encode(str3));
//		System.out.println(new String(str4, "UTF-8"));
//
//		System.out.println();

        System.out.println("CBC加密解密");
        String str5 = des3EncodeCBC(data);
        String str6 = des3DecodeCBC(str5);
        System.out.println(str5);
        System.out.println(str6);

    }

    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(Base64.decode(DES3_KEY));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);

        return bOut;
    }

    /**
     * ECB解密,不要IV
     *
     * @param key  密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(Base64.decode(DES3_KEY));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, deskey);

        byte[] bOut = cipher.doFinal(data);

        return bOut;

    }

    /**
     * CBC加密
     *
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static String des3EncodeCBC(String data)
            throws Exception {
        byte[] str = data.getBytes("UTF-16le");
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(Base64.decode(DES3_KEY));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(Base64.decode(DES3_KEY_IV));
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(str);

        return new BASE64Encoder().encode(bOut);
    }

    /**
     * CBC解密
     *
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeCBC(String data)
            throws Exception {
        byte[] str = new BASE64Decoder().decodeBuffer(data);
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(Base64.decode(DES3_KEY));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(Base64.decode(DES3_KEY_IV));

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] bOut = cipher.doFinal(str);

        return new String(bOut, "UTF-16le");

    }

}

