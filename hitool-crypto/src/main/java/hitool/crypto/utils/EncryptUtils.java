package hitool.crypto.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import org.apache.commons.codec.binary.Base64;

import hitool.crypto.enums.Algorithm;

/*
 * 
 * 加密工具类
 */
public class EncryptUtils {
	
	private static final int CACHE_SIZE = 1024;

	public static byte[] encrypt(String algorithm, String text, PublicKey pubKey) throws Exception {
		return EncryptUtils.encrypt(algorithm, URLEncoder.encode(text, "UTF-8").getBytes(), pubKey);
	}

	/*
	 * 加密数据
	 * 
	 * @param algorithm
	 *            加密算法
	 * @param key
	 *            密钥
	 * @param plainBytes
	 *            待加密数据
	 * @return byte[] 加密后的数据
	 */
	public static byte[] encrypt(String algorithm, byte[] plainBytes, Key pubKey)
			throws GeneralSecurityException {
		// 实例化
		Cipher cipher = CipherUtils.getCipher(algorithm);
		// 执行加密操作;得到加密后的字节数组
		return EncryptUtils.encrypt(cipher, plainBytes, pubKey);
	}

	public static byte[] encrypt(Cipher cipher, byte[] plainBytes, Key pubKey) throws GeneralSecurityException {
		// 用密钥初始化此 cipher ，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		// 执行加密操作;得到加密后的字节数组
		return cipher.doFinal(plainBytes);
	}

	public static String encryptString(Cipher cipher, byte[] plainBytes, Key pubKey)
			throws GeneralSecurityException {
		return StringUtils.newStringUtf8(EncryptUtils.encrypt(cipher, plainBytes, pubKey));
	}

	public static String encryptString(Cipher cipher, String plainText, Key key) throws GeneralSecurityException {
		return EncryptUtils.encryptString(cipher, plainText.getBytes(), key);
	}

	public static byte[] encrypt(Cipher cipher, String plainText, PublicKey pubKey)
			throws GeneralSecurityException {
		try {
			return encrypt(cipher, URLEncoder.encode(plainText, "UTF-8").getBytes(), pubKey);
		} catch (UnsupportedEncodingException e) {
			throw new GeneralSecurityException(e);
		}
	}

	public static String encryptHex(Cipher cipher, byte[] plainBytes, PublicKey pubKey)
			throws GeneralSecurityException {
		return StringUtils.getHexString(encrypt(cipher, plainBytes, pubKey));
	}

	public static String encryptHex(Cipher cipher, String plainText, PublicKey pubKey)
			throws GeneralSecurityException {
		return StringUtils.getHexString(encrypt(cipher, plainText, pubKey));
	}

	public static String encryptBase64(Cipher cipher, byte[] plainBytes, PublicKey pubKey)
			throws GeneralSecurityException {
		return Base64.encodeBase64String(encrypt(cipher, plainBytes, pubKey));
	}

	public static String encryptBase64(Cipher cipher, String plainText, PublicKey pubKey)
			throws GeneralSecurityException, UnsupportedEncodingException {
		return Base64.encodeBase64String(encrypt(cipher, plainText, pubKey));
	}

	/*
	 * 
	 * 加密文件
	 * 
	 * @param algorithm
	 *            算法名称
	 * @param plaintextFile
	 *            未加密过的文件
	 * @param encryptedFile
	 *            加密过的文件
	 * @param keyStream
	 *            证书
	 * @throws GeneralSecurityException
	 */
	public static void encrypt(String algorithm, InputStream plaintextFile, OutputStream encryptedFile,
			InputStream keyStream) throws GeneralSecurityException {
		try {
			// 还原密钥
			Key secretKey = SecretKeyUtils.readKey(keyStream);
			// 实例化
			Cipher cipher = CipherUtils.getCipher(algorithm);
			// 初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			// 执行操作
			EncryptUtils.encrypt(cipher, plaintextFile, encryptedFile);
			// 关闭输入输出流
			plaintextFile.close();
			encryptedFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void encrypt(Cipher cipher, String plaintextFile, String encryptedFile)
			throws GeneralSecurityException, IOException {
		EncryptUtils.encrypt(cipher, new FileInputStream(plaintextFile), new FileOutputStream(encryptedFile));
	}

	public static void encrypt(Cipher cipher, InputStream in, OutputStream out)
			throws GeneralSecurityException, IOException {
		CipherInputStream cin = new CipherInputStream(in, cipher);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = cin.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		cin.close();
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

			InputStream plaintextFile = new FileInputStream("D:/java环境变量设置说明.txt");
			OutputStream encryptedFileOut = new FileOutputStream("D:/java环境变量设置说明-encrypt.txt");

			// 对文件进行加密
			EncryptUtils.encrypt(Algorithm.KEY_AES, plaintextFile, encryptedFileOut,
					new FileInputStream("D:/secret.key"));

		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

	}

}
