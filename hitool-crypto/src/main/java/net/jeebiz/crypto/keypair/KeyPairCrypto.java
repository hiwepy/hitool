package net.jeebiz.crypto.keypair;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 公钥加密 私钥解密 私钥签名
 * @param <Pub>
 * @param <Pri>
 */
public abstract class KeyPairCrypto<Pub extends PublicKey, Pri extends PrivateKey> implements
		KeyPairEncryptor<Pub, Pri>, KeyPairDecryptor<Pub, Pri>, KeyPairSigner<Pub, Pri>, 
		KeyPairVerifier<Pub, Pri>{

	public static interface KeyPairEntry<Pub extends PublicKey, Pri extends PrivateKey> {

		Pub getPublicKey();

		Pri getPrivateKey();

	}

	/**
	 * 
	 * 初始化 钥匙对 @throws GeneralSecurityException @return Map<String,Object>
	 * 返回类型 @throws
	 */
	public abstract KeyPair genKeyPair() throws GeneralSecurityException;

	public abstract KeyPair genKeyPair(int keysize) throws GeneralSecurityException;

	public abstract KeyPairEntry<Pub, Pri> genKeyEntry() throws GeneralSecurityException;

	public abstract KeyPairEntry<Pub, Pri> genKeyEntry(int keysize) throws GeneralSecurityException;

	/**
	 * 
	 * 还原公钥 @param key @return @throws GeneralSecurityException @return PublicKey
	 * 返回类型 @throws
	 */
	public abstract Pub toPublicKey(byte[] keyBytes) throws GeneralSecurityException;

	public abstract Pub toPublicKey(String keyText) throws GeneralSecurityException;

	/**
	 * 
	 * 还原私钥
	 * 
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 * @return PrivateKey 返回类型
	 */
	public abstract Pri toPrivateKey(byte[] keyBytes) throws GeneralSecurityException;

	public abstract Pri toPrivateKey(String keyText) throws GeneralSecurityException;

}
