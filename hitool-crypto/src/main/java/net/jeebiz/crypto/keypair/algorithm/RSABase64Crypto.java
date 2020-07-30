 package net.jeebiz.crypto.keypair.algorithm;
 import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import net.jeebiz.crypto.enums.Algorithm;
import net.jeebiz.crypto.utils.CipherUtils;
import net.jeebiz.crypto.utils.DecryptUtils;
import net.jeebiz.crypto.utils.SignatureUtils;


 /**
  * RSA安全编码组件
  */ 
 public class RSABase64Crypto extends RSACrypto {


	private static class InstanceHolder {
		private static final RSABase64Crypto INSTANCE = new RSABase64Crypto();
	}

	public static RSABase64Crypto instance() {
		return InstanceHolder.INSTANCE;
	}
	
	@Override
	public byte[] encrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException {
		return Base64.encodeBase64(super.encrypt(plainBytes, pubKey));
	}

	@Override
	public String encrypt(String plainText, RSAPublicKey pubKey) throws GeneralSecurityException {
		return Base64.encodeBase64String(super.encrypt(plainText.getBytes(), pubKey));
	}

	@Override
	public byte[] segmentEncrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException {
		return Base64.encodeBase64(super.segmentEncrypt(plainBytes, pubKey));
	}

	@Override
	public byte[] decrypt(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_CIPHER_RSA_ECB_PKCS1PADDING);
		return DecryptUtils.decrypt(cipher, Base64.decodeBase64(encryptedBytes), priKey);
	}

	@Override
	public String decrypt(String encryptedText, RSAPrivateKey priKey) throws GeneralSecurityException {
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_CIPHER_RSA_ECB_PKCS1PADDING);
		return DecryptUtils.decryptString(cipher, Base64.decodeBase64(encryptedText), priKey);
	}

	@Override
	public byte[] segmentDecrypt(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		// 取得Cipher对象
 		Cipher cipher = CipherUtils.getDecryptCipher(Algorithm.KEY_RSA, priKey);
		// 对数据分段解密  
		return CipherUtils.segment(cipher, Base64.decodeBase64(encryptedBytes), MAX_DECRYPT_BLOCK);
	}

	@Override
	public String sign(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
		return Base64.encodeBase64String(SignatureUtils.sign(Base64.decodeBase64(encryptedBytes), signature, priKey));
	}

	@Override
	public boolean verify(byte[] encryptedBytes, RSAPublicKey pubKey, String signText) throws GeneralSecurityException {
		Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
		// 验证签名是否正常
		return SignatureUtils.verify(Base64.decodeBase64(encryptedBytes), signText.getBytes(), signature, pubKey);
	}
 	    
 }

