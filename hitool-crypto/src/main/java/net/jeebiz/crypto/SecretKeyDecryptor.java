package net.jeebiz.crypto;

import java.security.GeneralSecurityException;
/**
 * 
 * 密钥解密算法接口
 */
public interface SecretKeyDecryptor {
	
	public String decode(String encryptedText,String key) throws GeneralSecurityException ;
	public String decode(String encryptedText,byte[] key) throws GeneralSecurityException ;
	public byte[] decode(byte[] encryptedBytes,String key) throws GeneralSecurityException;
	public byte[] decode(byte[] encryptedBytes,byte[] key) throws GeneralSecurityException;

}
