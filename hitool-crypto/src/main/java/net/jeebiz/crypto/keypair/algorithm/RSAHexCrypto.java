 package net.jeebiz.crypto.keypair.algorithm;
 import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import net.jeebiz.crypto.enums.Algorithm;
import net.jeebiz.crypto.utils.SignatureUtils;


 /**
  * RSA安全编码组件
  */
 public class RSAHexCrypto extends RSACrypto {

	private static class InstanceHolder {
		private static final RSAHexCrypto INSTANCE = new RSAHexCrypto();
	}

	public static RSAHexCrypto instance() {
		return InstanceHolder.INSTANCE;
	}
	
	@Override
	public byte[] encrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException {
		return Hex.encodeHexString(super.encrypt(plainBytes, pubKey)).getBytes();
	}

	@Override
	public String encrypt(String plainText, RSAPublicKey pubKey) throws GeneralSecurityException {
		return Hex.encodeHexString(super.encrypt(plainText.getBytes(), pubKey));
	}

	@Override
	public byte[] segmentEncrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException {
		return Hex.encodeHexString(super.segmentEncrypt(plainBytes, pubKey)).getBytes();
	}

	@Override
	public byte[] decrypt(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		try {
			return super.decrypt(Hex.decodeHex(new String(encryptedBytes)), priKey);
		} catch (DecoderException e) {
			throw new GeneralSecurityException(e);
		}
	}

	@Override
	public String decrypt(String encryptedText, RSAPrivateKey priKey) throws GeneralSecurityException {
		try {
			return super.decrypt(new String(Hex.decodeHex(encryptedText)), priKey);
		} catch (DecoderException e) {
			throw new GeneralSecurityException(e);
		}
	}

	@Override
	public byte[] segmentDecrypt(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		try {
			return super.segmentDecrypt(Hex.decodeHex(new String(encryptedBytes)), priKey);
		} catch (DecoderException e) {
			throw new GeneralSecurityException(e);
		}
	}

	@Override
	public String sign(byte[] encryptedBytes, RSAPrivateKey priKey) throws GeneralSecurityException {
		try {
			Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
			return Hex.encodeHexString(SignatureUtils.sign(Hex.decodeHex(new String(encryptedBytes)), signature, priKey));
		} catch (DecoderException e) {
			throw new GeneralSecurityException(e);
		}
	}

	@Override
	public boolean verify(byte[] encryptedBytes, RSAPublicKey pubKey, String signText) throws GeneralSecurityException {
		try {
			Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_SHA1_RSA);
			// 验证签名是否正常
			return SignatureUtils.verify(Hex.decodeHex(new String(encryptedBytes)), signText.getBytes(), signature, pubKey);
		} catch (DecoderException e) {
			throw new GeneralSecurityException(e);
		}
	}
 	
 }

