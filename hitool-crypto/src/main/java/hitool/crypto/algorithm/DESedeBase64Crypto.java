package hitool.crypto.algorithm;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import hitool.crypto.Crypto;
import hitool.crypto.FileDecryptor;
import hitool.crypto.FileEncryptor;
import hitool.crypto.SecretKeyDecryptor;
import hitool.crypto.SecretKeyEncryptor;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.CipherUtils;
import hitool.crypto.utils.DecryptUtils;
import hitool.crypto.utils.EncryptUtils;
import hitool.crypto.utils.SecretKeyUtils;
import hitool.crypto.utils.StringUtils;
/*
 *  DESede+ Base64 加密算法
 */
public class DESedeBase64Crypto implements Crypto,SecretKeyEncryptor,SecretKeyDecryptor,FileEncryptor,FileDecryptor{
	
	private static DESedeBase64Crypto instance = null;
	
	private DESedeBase64Crypto(){};
	
	public static DESedeBase64Crypto getInstance(){
		instance= (instance==null)?instance = new DESedeBase64Crypto() :instance;
		return  instance;
	}
	
	public byte[] initkey() throws GeneralSecurityException {
        //获取二进制密钥编码形式  ；并进行Base64加密
        return Base64.encodeBase64(SecretKeyUtils.genSecretKey(null,Algorithm.KEY_DESEDE, 56).getEncoded()); 
	}
	
	public Key toKey(byte[] base64Key) throws GeneralSecurityException {
		return SecretKeyUtils.genDESedeKey(Base64.decodeBase64(base64Key));
	}
	
	public Key toKey(String key) throws GeneralSecurityException{
		return toKey(key.getBytes());
	}
	
	public String encode(String plainText, String base64Key) throws GeneralSecurityException {
		return encode(plainText, base64Key.getBytes());
	}
	
	public String encode(String plainText, byte[] base64Key) throws GeneralSecurityException {
		return StringUtils.newStringUtf8(encode(plainText.getBytes(), base64Key));
	}
	
	public byte[] encode(byte[] plainBytes, String base64Key) throws GeneralSecurityException {
		return encode(plainBytes, base64Key.getBytes());
	}
	
	public byte[] encode(byte[] plainBytes, byte[] base64Key) throws GeneralSecurityException {
		System.out.println("base64Key：/t"+new String(Base64.decodeBase64(base64Key)));
		//还原密钥
		SecretKey secretKey = SecretKeyUtils.genDESedeKey(Base64.decodeBase64(base64Key));
		// 根据秘钥和算法获取加密执行对象；用密钥初始化此 cipher ，设置为加密模式  ;
		Cipher enCipher = CipherUtils.getEncryptCipher(Algorithm.KEY_CIPHER_DESEDE, secretKey);
		//执行加密操作
		plainBytes = EncryptUtils.encrypt(enCipher, plainBytes, secretKey);
		/*使用base64加密算法对DES摘要算法结果进行加密
		   1、为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher异常，  
        2、不能把加密后的字节数组直接转换成字符串  
		*/
		return Base64.encodeBase64(plainBytes);	
	}
	
	public void encode(String key, String sourceFilePath, String destFilePath) throws GeneralSecurityException, IOException {
		encode(key.getBytes(), sourceFilePath,destFilePath);
	}

	public void encode(byte[] base64Key, String sourceFilePath, String destFilePath) throws GeneralSecurityException, IOException {
		File sourceFile = new File(sourceFilePath);
		if (sourceFile.exists() && sourceFile.isFile()) {
			//还原密钥
			Key secretKey = toKey(base64Key);
			// 根据秘钥和算法获取加密执行对象；用密钥初始化此 cipher ，设置为加密模式  ;
			Cipher enCipher = CipherUtils.getEncryptCipher(Algorithm.KEY_CIPHER_DESEDE, secretKey);
			//使用初始化的加密对象对文件进行加密处理
			EncryptUtils.encrypt(enCipher, sourceFile.getAbsolutePath(), destFilePath);
		}
	}
	
	public String decode(String encryptedText, String base64Key) throws GeneralSecurityException {
		return decode(encryptedText, base64Key.getBytes());
	}
	
	public String decode(String encryptedText, byte[] base64Key) throws GeneralSecurityException {
		return StringUtils.newStringUtf8(decode(encryptedText.getBytes(), base64Key));
	}
	
	public byte[] decode(byte[] encryptedBytes, String base64Key) throws GeneralSecurityException {
		return decode(encryptedBytes, base64Key.getBytes());
	}
	
	public byte[] decode(byte[] encryptedBytes, byte[] base64Key) throws GeneralSecurityException {
		System.out.println("base64Key：/t"+new String(Base64.decodeBase64(base64Key)));
		//还原密钥
		SecretKey secretKey = SecretKeyUtils.genDESKey(Base64.decodeBase64(base64Key));
		// 根据秘钥和算法获取解密执行对象；用密钥初始化此 cipher ，设置为解密模式  ;
		Cipher deCipher = CipherUtils.getDecryptCipher(Algorithm.KEY_CIPHER_DESEDE, secretKey);
		//使用base64加密算法对base64Bytes数组解密；
		encryptedBytes = Base64.decodeBase64(encryptedBytes);
		//DES摘要算法对base64解密后的结果进行解密
		return DecryptUtils.decrypt(deCipher , encryptedBytes, secretKey);
	}
	
	public void decode(String key, String encryptedFilePath, String destFilePath) throws GeneralSecurityException, IOException {
		decode(key.getBytes(), encryptedFilePath,destFilePath);
	}

	public void decode(byte[] base64Key, String encryptedFilePath, String destFilePath) throws GeneralSecurityException, IOException {
		File sourceFile = new File(encryptedFilePath);
		if (sourceFile.exists() && sourceFile.isFile()) {
			//还原密钥
			Key secretKey = toKey(base64Key);
			// 根据秘钥和算法获取加密执行对象；用密钥初始化此 cipher ，设置为解密模式  ;
			Cipher deCipher = CipherUtils.getDecryptCipher(Algorithm.KEY_CIPHER_DESEDE, secretKey);
			//使用初始化的加密对象对文件进行解密处理
			DecryptUtils.decrypt(deCipher, sourceFile.getAbsolutePath(), destFilePath);
		}
	}
	
}
