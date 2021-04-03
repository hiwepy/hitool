package hitool.crypto.utils;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/*
 * 
 * 签名工具类
 */
public class SignatureUtils {
	
	public static Signature getSignature(String algorithm) throws GeneralSecurityException {
		return Signature.getInstance(algorithm);
	}

	public static Signature getSignature(String algorithm, String provider) throws GeneralSecurityException {
		return Signature.getInstance(algorithm, provider);
	}

	public static byte[] sign(byte[] encryptedBytes, Signature signature, PrivateKey privateKey) throws GeneralSecurityException {
		signature.initSign(privateKey);
		signature.update(encryptedBytes);
		// 用私钥对信息生成数字签名
		return signature.sign();
	}

	public static boolean verify(byte[] encryptedBytes, byte[] signBytes, Signature signature, PublicKey publicKey)
			throws GeneralSecurityException {
		signature.initVerify(publicKey);
		signature.update(encryptedBytes);
		// 验证签名是否正常
		return signature.verify(signBytes);
	}

}
