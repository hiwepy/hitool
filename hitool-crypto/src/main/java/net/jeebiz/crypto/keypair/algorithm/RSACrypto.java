package net.jeebiz.crypto.keypair.algorithm;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import net.jeebiz.crypto.enums.Algorithm;
import net.jeebiz.crypto.keypair.KeyPairCrypto;
import net.jeebiz.crypto.utils.CipherUtils;
import net.jeebiz.crypto.utils.DecryptUtils;
import net.jeebiz.crypto.utils.EncryptUtils;
import net.jeebiz.crypto.utils.SecretKeyUtils;
import net.jeebiz.crypto.utils.SignatureUtils;
import net.jeebiz.crypto.utils.StringUtils;

/**
 * RSA加解密
 */
public class RSACrypto extends KeyPairCrypto<RSAPublicKey, RSAPrivateKey> {

	/**
     * RSA最大加密明文大小 
     */  
    protected static final int MAX_ENCRYPT_BLOCK = 117;  
    /**
     * RSA最大解密密文大小 
     */  
    protected static final int MAX_DECRYPT_BLOCK = 128; 
    
	private static class InstanceHolder {
		private static final RSACrypto INSTANCE = new RSACrypto();
	}

	public static RSACrypto instance() {
		return InstanceHolder.INSTANCE;
	}
	
	@Override
	public KeyPair genKeyPair() throws GeneralSecurityException {
		return SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA, 512);
	}

	@Override
	public KeyPair genKeyPair(int keysize) throws GeneralSecurityException {
		return SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA, keysize);
	}

	@Override
	public KeyPairEntry<RSAPublicKey, RSAPrivateKey> genKeyEntry() throws GeneralSecurityException {

		final KeyPair keyPair = SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA, 512);
		return new KeyPairEntry<RSAPublicKey, RSAPrivateKey>() {
			
			@Override
			public RSAPublicKey getPublicKey() {
				return (RSAPublicKey) keyPair.getPublic();
			}
			
			@Override
			public RSAPrivateKey getPrivateKey() {
				return (RSAPrivateKey) keyPair.getPrivate();
			}
			
		};
	}

	@Override
	public KeyPairEntry<RSAPublicKey, RSAPrivateKey> genKeyEntry(int keysize) throws GeneralSecurityException {
		final KeyPair keyPair = SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA, keysize);
		return new KeyPairEntry<RSAPublicKey, RSAPrivateKey>() {
			
			@Override
			public RSAPublicKey getPublicKey() {
				return (RSAPublicKey) keyPair.getPublic();
			}
			
			@Override
			public RSAPrivateKey getPrivateKey() {
				return (RSAPrivateKey) keyPair.getPrivate();
			}
		};
	}

	@Override
	public RSAPublicKey toPublicKey(byte[] keyBytes) throws GeneralSecurityException {
		// 取公钥匙对象
 		return (RSAPublicKey) SecretKeyUtils.genPublicKey(Algorithm.KEY_RSA, keyBytes);
	}

	@Override
	public RSAPublicKey toPublicKey(String keyText) throws GeneralSecurityException {
		return this.toPublicKey(StringUtils.getBytesUtf8(keyText));
	}

	@Override
	public RSAPrivateKey toPrivateKey(byte[] keyBytes) throws GeneralSecurityException {
		// 取私钥匙对象
 	 	return (RSAPrivateKey) SecretKeyUtils.genPrivateKey(Algorithm.KEY_RSA, keyBytes);
	}

	@Override
	public RSAPrivateKey toPrivateKey(String keyText) throws GeneralSecurityException {
		return this.toPrivateKey(StringUtils.getBytesUtf8(keyText));
	}
	
	@Override
	public byte[] encrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException {
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_RSA);
		return EncryptUtils.encrypt(cipher, plainBytes, pubKey);
	}

	@Override
	public byte[] encrypt(byte[] plainBytes, byte[] keyBytes) throws GeneralSecurityException {
		return this.encrypt( plainBytes, this.toPublicKey(keyBytes));
	}
	
	@Override
	public String encrypt(String plainText, RSAPublicKey pubKey) throws GeneralSecurityException {
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_RSA);
		return EncryptUtils.encryptString(cipher, plainText, pubKey);
		/*
		StringBuffer sbf = new StringBuffer(200);
		try {
			plainText = URLEncoder.encode(plainText, "UTF-8");// 用这个的原因是为了支持汉字、汉字和英文混排,解密方法中同理
			byte[] plainByte = plainText.getBytes();
			ByteArrayInputStream bays = new ByteArrayInputStream(plainByte);
			// 每次加密100字节
			byte[] readByte = new byte[100];
			int n = 0;
			// 为了支持超过117字节，每次加密100字节。
			while ((n = bays.read(readByte)) > 0) {
				if (n >= 100) {
					sbf.append(StringUtils.getHexString(encrypt(readByte, pubKey)));
				} else {
					byte[] tt = new byte[n];
					for (int i = 0; i < n; i++) {
						tt[i] = readByte[i];
					}
					sbf.append(StringUtils.getHexString(encrypt(tt, pubKey)));
				}
			}
			return sbf.toString();
		} catch (Exception e) {
			throw new GeneralSecurityException(e);
		}*/
	}

	@Override
	public String encrypt(String plainText, byte[] keyBytes) throws GeneralSecurityException {
		return this.encrypt( plainText, this.toPublicKey(keyBytes));
	}
	
	@Override
	public byte[] segmentEncrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException{
		// 取得Cipher对象
 	 	Cipher cipher = CipherUtils.getEncryptCipher(Algorithm.KEY_RSA, pubKey);
 	 	// 对数据分段加密  
 	 	return CipherUtils.segment(cipher, plainBytes, MAX_ENCRYPT_BLOCK);
	}

	@Override
	public byte[] decrypt(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_CIPHER_RSA_ECB_PKCS1PADDING);
		return DecryptUtils.decrypt(cipher, encryptedBytes, priKey);
	}

	@Override
	public byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes) throws GeneralSecurityException {
		return this.decrypt( encryptedBytes, this.toPrivateKey(keyBytes));
	}

	@Override
	public String decrypt(String encryptedText, RSAPrivateKey priKey) throws GeneralSecurityException {
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_CIPHER_RSA_ECB_PKCS1PADDING);
		return DecryptUtils.decryptString(cipher, encryptedText, priKey);
	}

	@Override
	public String decrypt(String encryptedText, byte[] keyBytes) throws GeneralSecurityException {
		return this.decrypt( encryptedText, this.toPrivateKey(keyBytes));
	}
	
	@Override
	public byte[] segmentDecrypt(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException{
		// 取得Cipher对象
 		Cipher cipher = CipherUtils.getDecryptCipher(Algorithm.KEY_RSA, priKey);
		// 对数据分段解密  
		return CipherUtils.segment(cipher, encryptedBytes, MAX_DECRYPT_BLOCK);
	}
	
	@Override
	public String sign(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
		return StringUtils.getUTF8String(SignatureUtils.sign(encryptedBytes, signature, priKey));
	}

	@Override
	public String sign(byte[] encryptedBytes, byte[] keyBytes) throws GeneralSecurityException {
		return this.sign(encryptedBytes, toPrivateKey(keyBytes));
	}

	@Override
	public boolean verify(byte[] encryptedBytes, RSAPublicKey pubKey, String signText) throws GeneralSecurityException {
		Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
		// 验证签名是否正常
		return SignatureUtils.verify(encryptedBytes, signText.getBytes(), signature, pubKey);
	}

	@Override
	public boolean verify(byte[] encryptedBytes, byte[] keyBytes, String signText) throws GeneralSecurityException {
		return this.verify(encryptedBytes, this.toPublicKey(keyBytes), signText);
	}

	@Override
	public boolean verify(byte[] encryptedBytes, RSAPublicKey pubKey, byte[] signBytes)
			throws GeneralSecurityException {
		Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
		// 验证签名是否正常
		return SignatureUtils.verify(encryptedBytes, signBytes, signature, pubKey);
	}

	@Override
	public boolean verify(byte[] encryptedBytes, byte[] keyBytes, byte[] signBytes) throws GeneralSecurityException {
		return this.verify(signBytes, this.toPublicKey(keyBytes), signBytes);
	}
	 
}
