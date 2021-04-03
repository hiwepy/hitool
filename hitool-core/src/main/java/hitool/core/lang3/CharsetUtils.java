/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CharsetUtils extends org.apache.commons.lang3.CharSetUtils {

	public static void main(String[] args) {
		System.out.println(CharsetUtils.getHexString("你好0110w".getBytes()));
		System.out.println(CharsetUtils.getHexString2("你好0110w".getBytes()));
		try {
			System.out.println(URLDecoder.decode("%E6%B5%8B%E8%AF%95", "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 将字符串转换成ISO-8859-1编码 
	 */
	public static final String toISO_8859_1(String input) {
        try {
            return new String(input.getBytes(CharEncoding.UTF_8),CharEncoding.ISO_8859_1);
        } catch (Exception ex) {
            try {
				return new String(input.getBytes(),CharEncoding.ISO_8859_1);
			} catch (UnsupportedEncodingException e) {
				ex.printStackTrace();
			}
        }
        return input;
    }
	
	
    /*
     * Fetches a UTF8-encoded String from the specified byte array.
     */
	public static final String getUTF8String(byte[] bytes) {
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
	public static final String getHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(CharsetUtils.getHexString(b));
		}
		return sb.toString();
	}
	
	public static final String getHexString(byte bt) {
		String hexStr = null;
		int n = bt;
		if (n < 0) {
			//移位算法
			n = bt & 0x7F + 128;
		}
		hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
		return hexStr.toUpperCase();
	}
	
	/*
	 * 把字节数组转换成16进制字符串
	 */
	public static final String getHexString2(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String stmp;
		for (int i = 0; i < bytes.length; i++) {
			stmp = Integer.toHexString(0xFF & bytes[i]);
			if (stmp.length() < 2) {
				sb.append(0);
			}
			sb.append(stmp.toUpperCase());
		}
		return sb.toString();
	}
	
	public static final byte[] getHexBytes(byte[] bytes) {
        if ((bytes.length % 2) != 0){
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[bytes.length / 2];
        for (int n = 0; n < bytes.length; n += 2) {
            String item = new String(bytes, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
	
	/*
	 * 把16进制字符串转换成字节数组
	 */
	public static final byte[] getBytesUsHexString(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	
    /*
     * Encodes the given string into a sequence of bytes using the ISO-8859-1 charset, storing the result into a new
     * byte array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static final byte[] getBytesIso8859_1(String string) {
        return CharsetUtils.getBytesUnchecked(string, CharEncoding.ISO_8859_1);
    }

    /*
     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static final byte[] getBytesUsAscii(String string) {
        return CharsetUtils.getBytesUnchecked(string, CharEncoding.US_ASCII);
    }

    /*
     * Encodes the given string into a sequence of bytes using the UTF-16 charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static final byte[] getBytesUtf16(String string) {
        return CharsetUtils.getBytesUnchecked(string, CharEncoding.UTF_16);
    }

    /*
     * Encodes the given string into a sequence of bytes using the UTF-16BE charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static final byte[] getBytesUtf16Be(String string) {
        return CharsetUtils.getBytesUnchecked(string, CharEncoding.UTF_16BE);
    }

    /*
     * Encodes the given string into a sequence of bytes using the UTF-16LE charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static final byte[] getBytesUtf16Le(String string) {
        return CharsetUtils.getBytesUnchecked(string, CharEncoding.UTF_16LE);
    }

    /*
     * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static final byte[] getBytesUtf8(String string) {
        return CharsetUtils.getBytesUnchecked(string, CharEncoding.UTF_8);
    }

    /*
     * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
     * array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param string
     *            the String to encode
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#getBytes(String)
     */
    public static final byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw CharsetUtils.newIllegalStateException(charsetName, e);
        }
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and re-throws it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#String(byte[], String)
     */
    public static final String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw CharsetUtils.newIllegalStateException(charsetName, e);
        }
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the ISO-8859-1 charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static final String newStringIso8859_1(byte[] bytes) {
        return CharsetUtils.newString(bytes, CharEncoding.ISO_8859_1);
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static final String newStringUsAscii(byte[] bytes) {
        return CharsetUtils.newString(bytes, CharEncoding.US_ASCII);
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16 charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static final String newStringUtf16(byte[] bytes) {
        return CharsetUtils.newString(bytes, CharEncoding.UTF_16);
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16BE charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static final String newStringUtf16Be(byte[] bytes) {
        return CharsetUtils.newString(bytes, CharEncoding.UTF_16BE);
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16LE charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static final String newStringUtf16Le(byte[] bytes) {
        return CharsetUtils.newString(bytes, CharEncoding.UTF_16LE);
    }

    /*
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8 charset.
     * 
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static final String newStringUtf8(byte[] bytes) {
        return CharsetUtils.newString(bytes, CharEncoding.UTF_8);
    }
    
	public static String UnicodeToGBK(String original) {
		if (original != null) {
			try {
				String str = new String(original.getBytes("ISO8859_1"), "GBK");
				return str;
			} catch (Exception e) {
				// e.printStackTrace();

				return "";
			}
		} else {
			return "";
		}
	}

	public static String GBKToUnicode(String original) {
		if (original != null) {
			try {
				return new String(original.getBytes("GBK"), "ISO8859_1");
			} catch (Exception e) {
				// e.printStackTrace();

				return "";
			}
		} else {
			return "";
		}
	}

	public static String Gb2312ToUnicode(String original) {
		if (original != null) {
			try {
				return new String(original.getBytes("gb2312"), "ISO8859_1");
			} catch (Exception e) {
				// e.printStackTrace();

				return "";
			}
		} else {
			return "";
		}
	}

	public static String UnicodeToGb2312(String original) {
		if (original != null) {
			try {
				return new String(original.getBytes("ISO8859_1"), "gb2312");
			} catch (Exception e) {
				// e.printStackTrace();

				return "";
			}
		} else {
			return "";
		}
	}

	public static String GBK2UTF8(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("GBK"), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String UTF82GBK(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("GBK"), "GBK");
		} catch (UnsupportedEncodingException ex) {
			// ex.printStackTrace();
		}
		return result;
	}
		 
}
