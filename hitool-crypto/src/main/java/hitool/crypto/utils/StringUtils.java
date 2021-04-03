package hitool.crypto.utils;

import java.util.Random;

 /*
 * @package com.jeekit.encrypt.utils
 * @className: StringUtils
 *  TODO
 */

public class StringUtils extends org.apache.commons.codec.binary.StringUtils{
	
	/*
     * Fetches a UTF8-encoded String from the specified byte array.
     */
	public static String getUTF8String(byte[] bytes) {
		// First, count the number of characters in the sequence
		int count = 0 , off = 0,len  = bytes.length;
		int max = off + len;
		int i = off;
		while (i < max) {
		    int c = bytes[i++] & 0xff;
		    switch (c >> 4) {
		    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
			// 0xxxxxxx
			count++;
			break;
		    case 12: case 13:
			// 110xxxxx 10xxxxxx
			if ((int)(bytes[i++] & 0xc0) != 0x80) {
			    throw new IllegalArgumentException();
			}
			count++;
			break;
		    case 14:
			// 1110xxxx 10xxxxxx 10xxxxxx
			if (((int)(bytes[i++] & 0xc0) != 0x80) ||
			    ((int)(bytes[i++] & 0xc0) != 0x80)) {
			    throw new IllegalArgumentException();
			}
			count++;
			break;
		    default:
			// 10xxxxxx, 1111xxxx
			throw new IllegalArgumentException();
		    }
		}
		if (i != max) {
		    throw new IllegalArgumentException();
		}
		// Now decode the characters...
		char[] cs = new char[count];
		i = 0;
		while (off < max) {
		    int c = bytes[off++] & 0xff;
		    switch (c >> 4) {
		    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
			// 0xxxxxxx
			cs[i++] = (char)c;
			break;
		    case 12: case 13:
			// 110xxxxx 10xxxxxx
			cs[i++] = (char)(((c & 0x1f) << 6) | (bytes[off++] & 0x3f));
			break;
		    case 14:
			// 1110xxxx 10xxxxxx 10xxxxxx
			int t = (bytes[off++] & 0x3f) << 6;
			cs[i++] = (char)(((c & 0x0f) << 12) | t | (bytes[off++] & 0x3f));
			break;
		    default:
			// 10xxxxxx, 1111xxxx
			throw new IllegalArgumentException();
		    }
		}
		return new String(cs, 0, count);
    }

    
	
	/*
	 * 字节标准移位转十六进制方法
	 */
	public static String getHexString(byte[] bs) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append(StringUtils.getHexString(b));
		}
		return sb.toString();
	}
	
	/*
	 * 字节标准移位转十六进制方法
	 */
	public static String getHexString(byte b) {
		String hexStr = null;
		int n = b;
		if (n < 0) {
			//若需要自定义加密,请修改这个移位算法即可
			n = b & 0x7F + 128;
		}
		hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
		return hexStr.toUpperCase();
	}
	
	public static byte[] getHexBytes(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
    
	/*
	  * 生成随即密码
	  * @author 来自网上
	  * @param pwd_len 生成的密码的总长度
	  * @return  密码的字符串
	  */
	 public static String genRandomNum(int pwd_len){
		 //36是因为数组是从0开始的，26个字母+10个数字 + 下划线
		 final int  maxNum = 37;
		 int i;  //生成的随机数
		 int count = 0; //生成的密码的长度
		 char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_' };
		 StringBuilder pwd = new StringBuilder("");
		 Random r = new Random();
		 while(count < pwd_len){
			 //生成随机数，取绝对值，防止生成负数，
			 i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1
			 if (i >= 0 && i < str.length) {
				 pwd.append(str[i]);
				 count++;
			 }
		 }
		 return pwd.toString();
	 }
}



