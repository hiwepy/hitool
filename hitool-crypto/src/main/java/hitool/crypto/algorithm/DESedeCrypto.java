package hitool.crypto.algorithm;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;

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
/**
 *  DESede对称加密算法
 */
public class DESedeCrypto implements Crypto,SecretKeyEncryptor,SecretKeyDecryptor,FileEncryptor,FileDecryptor{
	
	private static DESedeCrypto instance = null;
	private DESedeCrypto(){};
	public static DESedeCrypto getInstance(){
		instance= (instance==null)?instance=new DESedeCrypto():instance;
		return  instance;
	}
	
	public byte[] initkey() throws GeneralSecurityException {
        //获取二进制密钥编码形式  ；并进行Base64加密
        return Base64.encodeBase64(SecretKeyUtils.genSecretKey(null,Algorithm.KEY_DESEDE, 168).getEncoded()); 
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
		//还原密钥
		Key k = toKey(base64Key);
		return EncryptUtils.encrypt(CipherUtils.getCipher(Algorithm.KEY_CIPHER_DESEDE), plainBytes, k);
	}
	
	public void encode(String base64Key, String sourceFilePath, String destFilePath) throws GeneralSecurityException, IOException {
		encode(base64Key.getBytes(), sourceFilePath,destFilePath);
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
		//还原密钥
		Key k = toKey(base64Key);
		return DecryptUtils.decrypt(CipherUtils.getCipher(Algorithm.KEY_CIPHER_DESEDE), encryptedBytes, k);
	}
	
	public void decode(String base64Key, String encryptedFilePath, String destFilePath) throws GeneralSecurityException, IOException {
		decode(base64Key.getBytes(), encryptedFilePath,destFilePath);
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
