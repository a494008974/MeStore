// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package com.mylove.module_base.utils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Md5 {

	// ----------------------- MD5 start-----------------------------------
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
		} catch (Exception exception) {
		}
		Logger.i("================MD5：" + resultString.toString());
		return resultString;
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
            n += 256;
        }
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] + HEX_DIGITS[d2];
	}

	private static final String HEX_DIGITS[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f'};
	// ----------------------- MD5 end-----------------------------------

/*	*//**
	 * 生产appId
	 * 
	 * @param uId
	 * @param id
	 * @return
	 *//*
	public static String creatAppId(String uId, String id) {
		int resultInt = StringUtil.getAsInt(uId) + StringUtil.getAsInt(id) + 101603;
		return resultInt + "";
	}*/

	/**
	 * 生产appKey
	 * 
	 * @param appId
	 * @param timeLong
	 * @return
	 */
	public static String creatAppKey(String appId, long timeLong) {
		Random ran = new Random();
		String result = appId + timeLong + ran.nextInt(1000);
		return MD5Encode(result, "UTF-8");
	}

	/**
	 * 生产openId
	 * 
	 * @param aid
	 *            用户id
	 * @param appKey
	 *            应用密钥
	 * @param deviceId
	 *            设备唯一标识
	 *            当前请求时间
	 * @return
	 */
	public static String creatOpendId(String aid, String appKey, String deviceId) {
		Random ran = new Random();
		String result = aid + appKey  + deviceId;
		return MD5Encode(result, "UTF-8");
	}


	public static String getFileMD5(File file) {

		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			return MD5(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
		} catch (FileNotFoundException e) {
			return "";
		} catch (IOException e) {
			return "";
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// 关闭流产生的错误一般都可以忽略
				}
			}
		}

	}

	/**
	 * 计算MD5校验
	 * @param buffer
	 * @return 空串，如果无法获得 MessageDigest实例
	 */
	private static String MD5(ByteBuffer buffer) {
		String s = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(buffer);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>,
				// 逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}


	/***
	 * Get MD5 of one file！test ok!
	 *
	 * @param filepath
	 * @return
	 */
	public static String getFileMD5(String filepath) {
		File file = new File(filepath);
		return getFileMD5(file);
	}

	/***
	 * compare two file by Md5
	 *
	 * @param file1
	 * @param file2
	 * @return
	 */
	public static boolean isSameMd5(File file1,File file2){
		String md5_1 = getFileMD5(file1);
		String md5_2 = getFileMD5(file2);
		return md5_1.equals(md5_2);
	}
	/***
	 * compare two file by Md5
	 *
	 * @param filepath1
	 * @param filepath2
	 * @return
	 */
	public static boolean isSameMd5(String filepath1,String filepath2){
		File file1=new File(filepath1);
		File file2=new File(filepath2);
		return isSameMd5(file1, file2);
	}
}
