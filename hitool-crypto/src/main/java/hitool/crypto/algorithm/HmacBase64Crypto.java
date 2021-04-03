package hitool.crypto.algorithm;

import java.security.GeneralSecurityException;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;
import hitool.crypto.StringVerifier;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.HmacUtils;
import hitool.crypto.utils.SecretKeyUtils;
import hitool.crypto.utils.StringUtils;
/*
 *  MAC消息摘要组件
 */
public class HmacBase64Crypto implements StringEncoder,BinaryEncoder,StringVerifier {
	
	private byte[] base64Key = null;
	private String algorithm = Algorithm.KEY_HMAC_MD5;
	private static HmacBase64Crypto instance = null;
	
	private HmacBase64Crypto(){};
	
	private HmacBase64Crypto(byte[] base64Key){
		this.base64Key = base64Key;
	};
	
	private HmacBase64Crypto(byte[] base64Key,String algorithm){
		this.base64Key = base64Key;
		this.algorithm = algorithm;
	};
	
	public static HmacBase64Crypto getInstance(byte[] base64Key){
		instance= (instance==null)?instance=new HmacBase64Crypto(base64Key):instance;
		instance.base64Key = base64Key;
		return  instance;
	}
	
	public static HmacBase64Crypto getInstance(byte[] base64Key,String algorithm){
		instance= (instance==null)?instance=new HmacBase64Crypto(base64Key):instance;
		instance.base64Key = base64Key;
		instance.algorithm = algorithm;
		return  instance;
	}
	
	public Object encode(Object plainObject) throws EncoderException {
		if (plainObject == null) {
            return null;
        } else if (plainObject instanceof String) {
            return encode((String) plainObject);
        } else {
            throw new EncoderException("Objects of type " + plainObject.getClass().getName() + " cannot be encoded using MD4HexCodec");
        }
	}
	
	public byte[] encode(byte[] plainBytes) throws EncoderException {
		try {
			//获取Hmac消息摘要信息
			return Base64.encodeBase64(HmacUtils.hmac(plainBytes, base64Key, algorithm));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] encode(byte[] plainBytes, int times) throws EncoderException{
		try {
			//第一次加密
			byte[] binaryData = HmacUtils.hmac(plainBytes, base64Key, algorithm);
			for (int i = 0; i < times - 1; i++) {
				//多次加密
				binaryData = HmacUtils.hmac(binaryData, base64Key, algorithm);
			}
			return Base64.encodeBase64(binaryData, true);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encode(String plainText) throws EncoderException {
		try {
			//获取Hmac消息摘要信息
			return Base64.encodeBase64String(HmacUtils.hmac(plainText.getBytes(), base64Key, algorithm));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encode(String plainText, int times)  throws EncoderException{
		try {
			//第一次加密
			byte[] binaryData = HmacUtils.hmac(plainText.getBytes(), base64Key, algorithm);
			for (int i = 0; i < times - 1; i++) {
				//多次加密
				binaryData = HmacUtils.hmac(binaryData, base64Key, algorithm);
			}
			return Base64.encodeBase64String(binaryData);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] encodeHmacMD2(byte[] plainBytes) throws EncoderException {
		//获取HmacMD2消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD2).encode(plainBytes);
	}
	
	public String encodeHmacMD2(String plainText) throws EncoderException {
		//获取HmacMD2消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD2).encode(plainText);
	}
	
	public byte[] encodeHmacMD4(byte[] plainBytes) throws EncoderException {
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD4).encode(plainBytes);
	}
	
	public String encodeHmacMD4(String plainText) throws EncoderException {
		//获取HmacMD4消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD4).encode(plainText);
	}
	
	public byte[] encodeHmacSHA1(byte[] plainBytes) throws EncoderException {
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA1).encode(plainBytes);
	}
	
	public String encodeHmacSHA1(String plainText) throws EncoderException {
		//获取HmacSHA1消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA1).encode(plainText);
	}
	
	public byte[] encodeHmacSHA224(byte[] plainBytes) throws EncoderException {
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA224).encode(plainBytes);
	}
	
	public String encodeHmacSHA224(String plainText) throws EncoderException {
		//获取HmacSHA224消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA224).encode(plainText);
	}
	
	public byte[] encodeHmacSHA256(byte[] plainBytes) throws EncoderException {
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA256).encode(plainBytes);
	}
	
	public String encodeHmacSHA256(String plainText) throws EncoderException {
		//获取HmacSHA256消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA256).encode(plainText);
	}
	
	public byte[] encodeHmacSHA384(byte[] plainBytes) throws EncoderException {
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA384).encode(plainBytes);
	}
	
	public String encodeHmacSHA384(String plainText) throws EncoderException {
		//获取HmacSHA384消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA384).encode(plainText);
	}
	
	public byte[] encodeHmacSHA512(byte[] plainBytes) throws EncoderException {
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA512).encode(plainBytes);
	}
	
	public String encodeHmacSHA512(String plainText) throws EncoderException {
		//获取HmacSHA512消息摘要信息
		return HmacBase64Crypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA512).encode(plainText);
	}
	
	public static String getEncryptKey(String plainText,String base64Key) throws GeneralSecurityException{
		return DESBase64Crypto.getInstance().encode(plainText, base64Key);
	}

	public static  String getDecryptKey(String encryptedText,String base64Key) throws GeneralSecurityException{
		return DESBase64Crypto.getInstance().decode(encryptedText, base64Key);
	}
	
	/*
	 * 密码验证方法
	 * @throws EncoderException 
	 */
	public boolean verify(String plainText, String encrypt) throws EncoderException{
		return encode(plainText).equals(encrypt);
	}

	/*
	 * 重载一个多次加密时的密码验证方法
	 * @throws EncoderException 
	 */
	public boolean verify(String plainText, String encrypt, int times) throws EncoderException{
		return encode(plainText, times).equals(encrypt);
	}
	
	/*
	 * 进行相关的摘要算法的处理展示
	 * @throws Exception 
	 * **/
	public static void main(String[] args) throws Exception {
		

		String base64Key1 = StringUtils.newStringUtf8(DESBase64Crypto.getInstance().initkey());
		System.out.println("base64Key："+base64Key1);
		
		System.out.println("学校随机秘钥："+getEncryptKey("011111454",base64Key1));
		//成绩加密密钥：学号、课程号、成绩、学校随机密钥（存二维表实例下的表）
		String keyText = "3000611031-72120170-不及格-"+ getDecryptKey(getEncryptKey("011111454",base64Key1),base64Key1) ;
		System.out.println("keyText:"+keyText);
		
		HmacBase64Crypto codec1 = HmacBase64Crypto.getInstance(Base64.encodeBase64(keyText.getBytes()));
		
		System.out.println("================================================================:" );
		System.out.println("test:" +codec1.encode("不及格"));
		System.out.println("123:" + codec1.encode("123"));
		System.out.println("123456789:" + codec1.encode("123456789"));
		System.out.println("sarin:" + codec1.encode("sarin").length());
		
		
		System.out.println("================================================================:" );
		
		String plainText="HmacMD5消息摘要";
		//初始化HmacMD5的密钥
		byte[] base64Key = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_MD5);
		System.out.println("Hmac的密钥:"+ new String(base64Key));
		System.out.println("原文："+plainText);
		
		HmacBase64Crypto codec = HmacBase64Crypto.getInstance(base64Key);
		//获取HmacMD5消息摘要信息
		System.out.println("HmacMD5算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA256);
		//获取摘要信息
		System.out.println("HmacSHA256算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA1);
		//获取摘要信息
		System.out.println("HmacSHA1算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA384);
		//获取摘要信息
		System.out.println("HmacSHA384算法摘要："+codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA512);
		//获取摘要信息
		System.out.println("HmacSHA512算法摘要："+ codec.encode(plainText));
		
		System.out.println("================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================");
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_MD2);
		//获取摘要信息
		System.out.println("Bouncycastle HmacMD2算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_MD4);
		//获取摘要信息
		System.out.println("Bouncycastle HmacMD4算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA224);
		//获取摘要信息
		System.out.println("Bouncycastle HmacSHA224算法摘要："+ codec.encode(plainText));
		
		/*base64Key：UkC58t+bfPc=
		学校随机秘钥：huNw6LTXyLY1hUbU2YMYOQ==
		keyText:3000611031-72120170-不及格-011111454
		================================================================:
		test:3mE2dFHRQHxY7ZXptopAow==

		123:o4IwTP5APYnMpFQQQkBnQw==

		123456789:jrHX0YdhCmzJ7qPUe+3lJQ==

		sarin:26
		================================================================:
		Hmac的密钥:,�B�oN]촧��(&�]<'�8\��j���)��/2�GT9Q2�7o1C�O�2jQ��"�v�T
		原文：HmacMD5消息摘要
		HmacMD5算法摘要：8gBN6cKizw83l0Zbacq7cw==

		HmacSHA256算法摘要：1y2b6Sm42Ouomm/5TPX0wCTTM7vzGr0vOEG6bzaoR28=

		HmacSHA1算法摘要：y1yhPN74Lla06uT6MPlKFN2+7oo=

		HmacSHA384算法摘要：5VcKQFB1MVhusqzjp7uoobPnGK/ZiGb5E6ETdN1ZVbiKl3QcWvrpq8HIuwVmW3wz

		HmacSHA512算法摘要：69mkMxBDmCKwHwKnpcbIRR0zJDrwZaWu/waXFE8J/QpNCMgPQ38ZtXRyZhSbNCULZ9rK6+1WKBru
		/ZjAgxCOcA==

		================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================
		Bouncycastle HmacMD2算法摘要：hnpnB1Ry65Af68T5Ngz0/A==

		Bouncycastle HmacMD4算法摘要：68U8Z7ulMICJNgcToGJyjw==

		Bouncycastle HmacSHA224算法摘要：+/R0v8A7bEAaAEPrIGJCoRj8yDSU7AzIWPa4fg==*/

	}
	
}
