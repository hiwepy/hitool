package hitool.crypto.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import hitool.crypto.enums.Algorithm;

/*
 * 
 * 加密工具
 */
public final class CipherUtils {

	/*
	 * 
	 * 生成一个实现RSA转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getRSACipher() throws GeneralSecurityException {
		return Cipher.getInstance(Algorithm.KEY_CIPHER_RSA_ECB_PKCS1PADDING);
	}

	/*
	 * 
	 * 生成一个实现AES转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getAESCipher() throws GeneralSecurityException {
		// 生成一个实现AES转换的 Cipher 对象
		return Cipher.getInstance(Algorithm.KEY_CIPHER_AES);
	}

	/*
	 * 
	 * 生成一个实现DES转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getDESCipher() throws GeneralSecurityException {
		// 生成一个实现DES转换的 Cipher 对象
		return Cipher.getInstance(Algorithm.KEY_CIPHER_DES);
	}

	/*
	 * 
	 * 加密解密第2步：生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * 
	 * @param algorithm
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getCipher(String algorithm) throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getCipher(algorithm, null);
	}

	public static Cipher getCipher(String algorithm, String provider) throws GeneralSecurityException {
		// 生成一个实现转换的 Cipher 对象
		if (null != provider) {
			return Cipher.getInstance(algorithm, provider);
		} else {
			return Cipher.getInstance(algorithm);
		}
	}

	public static Cipher getEncryptCipher(String algorithm, Key key) throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getEncryptCipher(algorithm, key, null, null);
	}

	public static Cipher getEncryptCipher(String algorithm, Key key, String provider) throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getEncryptCipher(algorithm, key, null, provider);
	}

	public static Cipher getEncryptCipher(String algorithm, Key key, SecureRandom random)
			throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getEncryptCipher(algorithm, key, null, null);
	}

	public static Cipher getEncryptCipher(String algorithm, Key key, SecureRandom random, String provider)
			throws GeneralSecurityException {
		// 生成一个实现转换的 Cipher 对象
		Cipher cipher = CipherUtils.getCipher(algorithm, provider);
		// 用密钥初始化此 cipher ，设置为解密模式
		if (null == random) {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, key, random);
		}
		// 返回 cipher
		return cipher;
	}

	public static Cipher getDecryptCipher(String algorithm, Key key) throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getDecryptCipher(algorithm, key, null, null);
	}

	public static Cipher getDecryptCipher(String algorithm, Key key, String provider) throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getDecryptCipher(algorithm, key, null, provider);
	}

	public static Cipher getDecryptCipher(String algorithm, Key key, SecureRandom random)
			throws GeneralSecurityException {
		// 返回 cipher
		return CipherUtils.getDecryptCipher(algorithm, key, null, null);
	}

	public static Cipher getDecryptCipher(String algorithm, Key key, SecureRandom random, String provider)
			throws GeneralSecurityException {
		// 生成一个实现RSA转换的 Cipher 对象
		Cipher cipher = CipherUtils.getCipher(algorithm, provider);
		// 用密钥初始化此 cipher ，设置为解密模式
		if (null == random) {
			cipher.init(Cipher.DECRYPT_MODE, key);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key, random);
		}
		// 返回 cipher
		return cipher;
	}

	/*
	 * 
	 * 数据分段加密/解密
	 * 
	 * @param cipher
	 * @param bytes
	 * @param block
	 * @return
	 * @throws GeneralSecurityException
	 * @return byte[] 返回类型
	 */
	public static byte[] segment(Cipher cipher, byte[] bytes, int block) throws GeneralSecurityException {
		// 分段加密
		int inputLen = bytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > block) {
				cache = cipher.doFinal(bytes, offSet, block);
			} else {
				cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * block;
		}
		byte[] binaryData = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			return null;
		}
		return binaryData;
	}

}
