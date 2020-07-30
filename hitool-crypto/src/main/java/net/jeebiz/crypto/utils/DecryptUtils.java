package net.jeebiz.crypto.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import net.jeebiz.crypto.enums.Algorithm;

/**
 * 
 * 解密工具类
 */
public class DecryptUtils {

	private static final int CACHE_SIZE = 1024;
	
	static {
		// 加入bouncyCastle支持
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			//Security.addProvider(new BouncyCastleProvider());
			Security.insertProviderAt(new BouncyCastleProvider(), 1);
		}
		if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastlePQCProvider());
		}
	}
	
	public static byte[] decrypt(Cipher cipher, byte[] encryptedBytes, PrivateKey privateKey)
			throws GeneralSecurityException {
		// 用密钥初始化此 cipher ，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(encryptedBytes);
	}

	public static String decryptHex(Cipher cipher, String cipherTextHex, PrivateKey privateKey)
			throws GeneralSecurityException, DecoderException {
		return new String(Hex.encodeHex(decrypt(cipher, Hex.decodeHex(cipherTextHex.toCharArray()), privateKey)));
	}

	public static String decryptBase64(Cipher cipher, String cipherTextBase64, PrivateKey privateKey)
			throws GeneralSecurityException {
		return new String(
				Base64.encodeBase64(decrypt(cipher, Base64.decodeBase64(cipherTextBase64.getBytes()), privateKey)));
	}

	public static byte[] decrypt(String algorithm, String encryptedText, SecretKey secretKey)
			throws GeneralSecurityException, UnsupportedEncodingException {
		return DecryptUtils.decrypt(algorithm, URLEncoder.encode(encryptedText, "UTF-8").getBytes(), secretKey);
	}

	/**
	 * @param algorithm
	 *            解密算法
	 * @param secretKey
	 *            密钥
	 * @param encryptedBytes
	 *            待解密数据
	 * @return byte[] 解密后的数据
	 */
	public static byte[] decrypt(String algorithm, byte[] encryptedBytes, SecretKey secretKey)
			throws GeneralSecurityException {
		// 实例化
		Cipher cipher = CipherUtils.getCipher(algorithm);
		return DecryptUtils.decrypt(cipher, encryptedBytes, secretKey);
	}

	/**
	 * 
	 * 解密数据
	 * 
	 * @param cipher
	 *            加密组件
	 * @param secretKey
	 *            密钥
	 * @param encryptedBytes
	 *            待解密数据
	 * @return byte[] 解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(Cipher cipher, byte[] encryptedBytes, Key secretKey) throws GeneralSecurityException {
		// 用密钥初始化此 cipher ，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		// 执行解密操作;得到解密后的字节数组
		return cipher.doFinal(encryptedBytes);
	}

	/**
	 * 
	 * 解密数据
	 * 
	 * @param cipher
	 *            加密组件
	 * @param secretKey
	 *            密钥
	 * @param encryptedText
	 *            待解密数据
	 * @return byte[] 解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(Cipher cipher, String encryptedText, Key secretKey) throws GeneralSecurityException {
		return DecryptUtils.decrypt(cipher, encryptedText.getBytes(), secretKey);
	}

	/**
	 * 
	 * 解密数据
	 * 
	 * @param cipher
	 *            加密组件
	 * @param secretKey
	 *            密钥
	 * @param encryptedBytes
	 *            待解密数据
	 * @return string 解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static String decryptString(Cipher cipher, byte[] encryptedBytes, Key secretKey)
			throws GeneralSecurityException {
		return StringUtils.newStringUtf8(DecryptUtils.decrypt(cipher, encryptedBytes, secretKey));
	}

	/**
	 * 
	 * 解密数据
	 * 
	 * @param cipher
	 *            加密组件
	 * @param secretKey
	 *            密钥
	 * @param encryptedText
	 *            待解密数据
	 * @return string 解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static String decryptString(Cipher cipher, String encryptedText, Key secretKey)
			throws GeneralSecurityException {
		return DecryptUtils.decryptString(cipher, encryptedText.getBytes(), secretKey);
	}

	/**
	 * 
	 * 解密文件
	 * 
	 * @param algorithm
	 *            算法名称
	 * @param encryptedFile
	 *            加密过的文件
	 * @param decryptedFile
	 *            解密后的文件
	 * @param keyStream
	 *            证书
	 * @throws GeneralSecurityException
	 */
	public static void decrypt(String algorithm, InputStream encryptedFile, OutputStream decryptedFile,
			InputStream keyStream) throws GeneralSecurityException {
		try {
			// 还原密钥
			Key key = SecretKeyUtils.readKey(keyStream);
			// 实例化
			Cipher cipher = CipherUtils.getCipher(algorithm);
			// 初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 执行操作
			decrypt(cipher, encryptedFile, decryptedFile);
			// 关闭输入输出流
			encryptedFile.close();
			decryptedFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void decrypt(Cipher cipher, String encryptedFile, String edcryptedFile)
			throws GeneralSecurityException, IOException {
		DecryptUtils.decrypt(cipher, new FileInputStream(encryptedFile), new FileOutputStream(edcryptedFile));
	}

	public static void decrypt(Cipher cipher, InputStream in, OutputStream out)
			throws GeneralSecurityException, IOException {
		CipherOutputStream cout = new CipherOutputStream(out, cipher);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			cout.write(cache, 0, nRead);
			cout.flush();
		}
		cout.close();
		out.close();
		in.close();
		/*
		 * int blockSize = cipher.getBlockSize(); int outputSize =
		 * cipher.getOutputSize(blockSize); byte[] inBytes = new byte[blockSize]; byte[]
		 * outBytes = new byte[outputSize]; int inLength = 0; boolean more = true; while
		 * (more) { inLength = in.read(inBytes); if (inLength == blockSize){ int
		 * outLength = cipher.update(inBytes, 0, blockSize, outBytes);
		 * out.write(outBytes, 0, outLength); }else{ more = false; } } if (inLength >
		 * 0){ outBytes = cipher.doFinal(inBytes, 0, inLength); }else{ outBytes =
		 * cipher.doFinal(); } out.write(outBytes);
		 */
	}

	public static void main(String[] args) throws Exception {

		/*
		 * java AESTest -genkey secret.key java AESTest -encrypt plaintextFile
		 * encryptedFile secret.key java AESTest -decrypt encryptedFile decryptedFile
		 * secret.key
		 */

		try {

			InputStream encryptedFileIn = new FileInputStream("D:/java环境变量设置说明-encrypt.txt");
			OutputStream decryptedFile = new FileOutputStream("D:/java环境变量设置说明-decrypt.txt");

			// 对文件进行解密
			DecryptUtils.decrypt(Algorithm.KEY_AES, encryptedFileIn, decryptedFile,
					new FileInputStream("D:/secret.key"));

		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

	}

}
