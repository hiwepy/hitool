package net.jeebiz.crypto.utils;

import java.io.UnsupportedEncodingException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * 
 * 数组工具类
 */
public class BinaryUtils {

	static {
		if(Security.getProvider("BC") == null){
			// 加入bouncyCastle支持
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	/**
	 * 
	 *  获取16进制字节数组的二级制数组
	 * @param bytes
	 * @return
	 */
	public static byte[] getHex2BinaryBytes(byte[] bytes) {
        if ((bytes.length % 2) != 0){
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] result = new byte[bytes.length / 2];
        for (int n = 0; n < bytes.length; n += 2) {
            String item = new String(bytes, n, 2);
            result[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return result;
    }
	
	/**
	 * 
	 *  获取字符串的ASCII数组
	 * @param data
	 * @return
	 */
	public static byte[] getAsciiBytes(final String data) {
		if (data == null) {
			throw new IllegalArgumentException("Parameter may not be null");
		}
		try {
			return data.getBytes("US-ASCII"); // 就是这一句拉
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/*
	 * Returns an array of bytes representing the UTF8 encoding of the specified
	 * String.
	 */
	public static byte[] getUTF8Bytes(String s) {
		char[] c = s.toCharArray();
		int len = c.length;
		// Count the number of encoded bytes...
		int count = 0;
		for (int i = 0; i < len; i++) {
			int ch = c[i];
			if (ch <= 0x7f) {
				count++;
			} else if (ch <= 0x7ff) {
				count += 2;
			} else {
				count += 3;
			}
		}
		// Now return the encoded bytes...
		byte[] b = new byte[count];
		int off = 0;
		for (int i = 0; i < len; i++) {
			int ch = c[i];
			if (ch <= 0x7f) {
				b[off++] = (byte) ch;
			} else if (ch <= 0x7ff) {
				b[off++] = (byte) ((ch >> 6) | 0xc0);
				b[off++] = (byte) ((ch & 0x3f) | 0x80);
			} else {
				b[off++] = (byte) ((ch >> 12) | 0xe0);
				b[off++] = (byte) (((ch >> 6) & 0x3f) | 0x80);
				b[off++] = (byte) ((ch & 0x3f) | 0x80);
			}
		}
		return b;
	}
}
