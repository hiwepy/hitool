package net.jeebiz.crypto;

import java.security.GeneralSecurityException;
/**
 * 
 * 密钥加密算法接口
 */
public interface SecretKeyEncryptor {

	public String encode(String plainText,String key) throws GeneralSecurityException ;
	public String encode(String plainText,byte[] key) throws GeneralSecurityException ;
	public byte[] encode(byte[] plainBytes,String key) throws GeneralSecurityException;
	public byte[] encode(byte[] plainBytes,byte[] key) throws GeneralSecurityException;
	
	
}
