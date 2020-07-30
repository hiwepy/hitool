package net.jeebiz.crypto.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.jeebiz.crypto.enums.Algorithm;

/**
 * 
 * @package net.jeebiz.crypto.digest
 * @className: DigestUtils TODO
 */
public class DigestUtils extends org.apache.commons.codec.digest.DigestUtils {

	private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static final int STREAM_BUFFER_LENGTH = 1024;

	public static byte[] md4(String source) {
		return md4(source.getBytes());
	}

	public static byte[] md4(byte[] source) {
		return DigestUtils.getDigest(Algorithm.KEY_MD4).digest(source);
	}

	public static byte[] md4(InputStream source) throws IOException {
		return digest(DigestUtils.getDigest(Algorithm.KEY_MD4), source);
	}

	public static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
		byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
		int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

		while (read > -1) {
			digest.update(buffer, 0, read);
			read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
		}

		return digest.digest();
	}

	public static MessageDigest getDigest(Algorithm algorithm) {
		return DigestUtils.getDigest(algorithm.toString());
	}

	public static MessageDigest getDigest(Algorithm algorithm, String provider) {
		return DigestUtils.getDigest(algorithm.toString(), provider);
	}

	public static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
		}
	}

	public static MessageDigest getDigest(String algorithm, String provider) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
		}
	}

	public static byte[] digest(String algorithm, byte[] bytes) {
		return getDigest(algorithm).digest(bytes);
	}

	public static String digestAsHexString(String algorithm, byte[] bytes) {
		char[] hexDigest = digestAsHexChars(algorithm, bytes);
		return new String(hexDigest);
	}

	public static StringBuilder appendDigestAsHex(String algorithm, byte[] bytes, StringBuilder builder) {
		char[] hexDigest = digestAsHexChars(algorithm, bytes);
		return builder.append(hexDigest);
	}

	private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
		byte[] digest = digest(algorithm, bytes);
		return encodeHex(digest);
	}

	private static char[] encodeHex(byte[] bytes) {
		char chars[] = new char[32];
		for (int i = 0; i < chars.length; i = i + 2) {
			byte b = bytes[i / 2];
			chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
			chars[i + 1] = HEX_CHARS[b & 0xf];
		}
		return chars;
	}

}
