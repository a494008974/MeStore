package com.mylove.store.utils;


import android.util.Base64;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesHelper {
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;     
    public AesHelper(String iv, String SecretKey){
    	 ivspec = new IvParameterSpec(iv.getBytes());
         keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
         try {
             cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         } catch (NoSuchAlgorithmException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (NoSuchPaddingException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    }
    public byte[] encrypt(String text) throws Exception
    {
        if(text == null || text.length() == 0)
            throw new Exception("Empty string");
        byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted = cipher.doFinal(padString(text).getBytes());
            encrypted = Base64.encode(encrypted,0);
        } catch (Exception e)
        {           
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }
     
    public byte[] decrypt(String code) throws Exception
    {
        if(code == null || code.length() == 0)
            throw new Exception("Empty string");
        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            code = new String(Base64.decode(code,0));

            decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e)
        {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }
     
 
     
    public static String bytesToHex(byte[] data)
    {
        if (data==null)
        {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16)
                str = str + "0" + Integer.toHexString(data[i]&0xFF);
            else
                str = str + Integer.toHexString(data[i]&0xFF);
        }
        return str;
    }
     
         
    public static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }
 
    private static String padString(String source)
    {
      char paddingChar = ' ';
      int size = 16;
      int x = source.length() % size;
      int padLength = size - x;
      for (int i = 0; i < padLength; i++)
      {
          source += paddingChar;
      }
      return source;
    }


    private static final String AESTYPE ="AES/ECB/PKCS5Padding";

    public static String AES_Encrypt(String keyStr, String plainText) {
        byte[] encrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(Base64.encode(encrypt,0));
    }

    public static String AES_Decrypt(String keyStr, String encryptData) {
        byte[] decrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decode(encryptData,0));
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(decrypt).trim();
    }

    private static Key generateKey(String key)throws Exception{
        try{
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
