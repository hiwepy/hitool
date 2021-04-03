package hitool.crypto.utils;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Hex;
import hitool.crypto.enums.Algorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class HmacUtils {

	static {
		if(Security.getProvider("BC") == null){
			// 加入bouncyCastle支持
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	
	/*
	 * 
	 *  Hmac消息摘要
	 * @param plainBytes 待做摘要处理的数据
	 * @param key   密钥
	 * @return byte[] 消息摘要
	 * @throws GeneralSecurityException 
	 * @throws Exception
	 */
	public static byte[] hmac(byte[] plainBytes,byte[] key,String algorithm) throws GeneralSecurityException  {
		//还原密钥，因为密钥是以byte形式为消息传递算法所拥有
		SecretKey secretKey = SecretKeyUtils.genSecretKey(key, algorithm);
		//实例化Mac
		Mac mac = Mac.getInstance(algorithm);
		//初始化Mac
		mac.init(secretKey);
		//执行消息摘要处理
		return mac.doFinal(plainBytes);
	}


	public static byte[] hmacMD5(String plainText, String key) throws GeneralSecurityException {
		return hmacMD5(plainText.getBytes(), key.getBytes());
	}
	
	public static byte[] hmacMD5(byte[] plainBytes, String key) throws GeneralSecurityException {
		return hmacMD5(plainBytes, key.getBytes());
	}
	
	public static byte[] hmacMD5(String plainText, byte[] key) throws GeneralSecurityException {
		return hmacMD5(plainText.getBytes(), key);
	}
	
	/*
	 * 生成签名数据
	 * 
	 * @param plainBytes 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成MD5编码的字符串 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hmacMD5(byte[] plainBytes, byte[] key) throws GeneralSecurityException  {
		return  HmacUtils.hmac(plainBytes, key, Algorithm.KEY_HMAC_MD5);
	}
	
	public byte[] hmacSHA1(String plainText, String key) throws GeneralSecurityException {
		return hmacSHA1(plainText.getBytes(), key.getBytes());
	}
	
	public byte[] hmacSHA1(byte[] plainBytes, String key) throws GeneralSecurityException {
		return hmacSHA1(plainBytes, key.getBytes());
	}
	
	public byte[] hmacSHA1(String plainText, byte[] key) throws GeneralSecurityException {
		return hmacSHA1(plainText.getBytes(), key);
	}
	
	/*
	 * 生成签名数据
	 * 
	 * @param plainBytes 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成MD5编码的字符串 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hmacSHA1(byte[] plainBytes, byte[] key) throws GeneralSecurityException  {
		return  HmacUtils.hmac(plainBytes, key, Algorithm.KEY_HMAC_SHA1);
	}
	
	

	public byte[] hmacSHA224(String plainText, String key) throws GeneralSecurityException {
		return hmacSHA224(plainText.getBytes(), key.getBytes());
	}
	
	public byte[] hmacSHA224(byte[] plainBytes, String key) throws GeneralSecurityException {
		return hmacSHA224(plainBytes, key.getBytes());
	}
	
	public byte[] hmacSHA224(String plainText, byte[] key) throws GeneralSecurityException {
		return hmacSHA224(plainText.getBytes(), key);
	}
	
	/*
	 * 生成签名数据
	 * 
	 * @param plainBytes 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成MD5编码的字符串 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hmacSHA224(byte[] plainBytes, byte[] key) throws GeneralSecurityException  {
		return  HmacUtils.hmac(plainBytes, key, Algorithm.KEY_HMAC_SHA224);
	}
	
	public byte[] hmacSHA256(String plainText, String key) throws GeneralSecurityException {
		return hmacSHA256(plainText.getBytes(), key.getBytes());
	}
	
	public byte[] hmacSHA256(byte[] plainBytes, String key) throws GeneralSecurityException {
		return hmacSHA256(plainBytes, key.getBytes());
	}
	
	public byte[] hmacSHA256(String plainText, byte[] key) throws GeneralSecurityException {
		return hmacSHA256(plainText.getBytes(), key);
	}
	
	/*
	 * 生成签名数据
	 * 
	 * @param plainBytes 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成MD5编码的字符串 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hmacSHA256(byte[] plainBytes, byte[] key) throws GeneralSecurityException  {
		return  HmacUtils.hmac(plainBytes, key, Algorithm.KEY_HMAC_SHA256);
	}
	
	public byte[] hmacSHA384(String plainText, String key) throws GeneralSecurityException {
		return hmacSHA384(plainText.getBytes(), key.getBytes());
	}
	
	public byte[] hmacSHA384(byte[] plainBytes, String key) throws GeneralSecurityException {
		return hmacSHA384(plainBytes, key.getBytes());
	}
	
	public byte[] hmacSHA384(String plainText, byte[] key) throws GeneralSecurityException {
		return hmacSHA384(plainText.getBytes(), key);
	}
	
	/*
	 * 生成签名数据
	 * 
	 * @param plainBytes 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成MD5编码的字符串 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hmacSHA384(byte[] plainBytes, byte[] key) throws GeneralSecurityException  {
		return  HmacUtils.hmac(plainBytes, key, Algorithm.KEY_HMAC_SHA384);
	}
	
	public byte[] hmacSHA512(String plainText, String key) throws GeneralSecurityException {
		return hmacSHA512(plainText.getBytes(), key.getBytes());
	}
	
	public byte[] hmacSHA512(byte[] plainBytes, String key) throws GeneralSecurityException {
		return hmacSHA512(plainBytes, key.getBytes());
	}
	
	public byte[] hmacSHA512(String plainText, byte[] key) throws GeneralSecurityException {
		return hmacSHA512(plainText.getBytes(), key);
	}
	
	/*
	 * 生成签名数据
	 * 
	 * @param plainBytes 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成MD5编码的字符串 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hmacSHA512(byte[] plainBytes, byte[] key) throws GeneralSecurityException  {
		return  HmacUtils.hmac(plainBytes, key, Algorithm.KEY_HMAC_SHA512);
	}
	
	public static void main(String[] args) throws Exception {
		String str="HmacMD5消息摘要";
		//初始化HmacMD5的密钥
		byte[] key1 = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_MD5);
		//获取HmacMD5消息摘要信息
		byte[] data1=  HmacUtils.hmac(str.getBytes(), key1, Algorithm.KEY_HMAC_MD5);
		System.out.println("原文："+str);
		System.out.println();
		System.out.println("HmacMD5的密钥:"+ new String(key1));
		System.out.println("HmacMD5算法摘要："+ new String(data1));
		System.out.println();
		
		//初始化密钥
		byte[] key2= SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_SHA256);
		//获取摘要信息
		byte[] data2 = HmacUtils.hmac(str.getBytes(), key2, Algorithm.KEY_HMAC_SHA256);
		System.out.println("HmacSHA256的密钥:"+ new String(key2));
		System.out.println("HmacSHA256算法摘要："+ new String(data2));
		System.out.println();
		
		
		//初始化密钥
		byte[] key3 = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_SHA1);
		//获取摘要信息
		byte[] data3= HmacUtils.hmac(str.getBytes(), key3, Algorithm.KEY_HMAC_SHA1);
		System.out.println("HmacSHA1的密钥:"+ new String(key3));
		System.out.println("HmacSHA1算法摘要："+ new String(data3));
		System.out.println();
		
		//初始化密钥
		byte[] key4= SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_SHA384); 
		//获取摘要信息
		byte[] data4= HmacUtils.hmac(str.getBytes(), key4, Algorithm.KEY_HMAC_SHA384);
		System.out.println("HmacSHA384的密钥:"+new String(key4));
		System.out.println("HmacSHA384算法摘要："+new String(data4));
		System.out.println();
		
		
		//初始化密钥
		byte[] key5= SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_SHA512); 
		//获取摘要信息
		byte[] data5= HmacUtils.hmac(str.getBytes(), key5, Algorithm.KEY_HMAC_SHA512);
		System.out.println("HmacSHA512的密钥:"+ new String(key5));
		System.out.println("HmacSHA512算法摘要："+ new String(data5));
		System.out.println();
		
		System.out.println("================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================");
		//初始化密钥
		byte[] key6 = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_MD2); 
		//获取摘要信息
		byte[] data6= HmacUtils.hmac(str.getBytes(), key6, Algorithm.KEY_HMAC_MD2); 
		//做十六进制转换
		String datahex6= Hex.encodeHexString(data6);
		System.out.println("Bouncycastle HmacMD2的密钥:"+ new String(key6));
		System.out.println("Bouncycastle HmacMD2算法摘要："+ new String(data6));
		System.out.println("Bouncycastle HmacMD2Hex算法摘要："+ new String(datahex6));
		System.out.println();
		
		//初始化密钥
		byte[] key7= SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_MD4); 
		//获取摘要信息
		byte[] data7 =HmacUtils.hmac(str.getBytes(), key7, Algorithm.KEY_HMAC_MD4); 
		String datahex7= Hex.encodeHexString(data7);
		System.out.println("Bouncycastle HmacMD4的密钥:"+new String(key7));
		System.out.println("Bouncycastle HmacMD4算法摘要："+new String(data7));
		System.out.println("Bouncycastle HmacMD4Hex算法摘要："+new String(datahex7));
		System.out.println();
		
		//初始化密钥
		byte[] key8= SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_SHA224);  
		//获取摘要信息
		byte[] data8= HmacUtils.hmac(str.getBytes(), key8, Algorithm.KEY_HMAC_SHA224); 
		String datahex8= Hex.encodeHexString(data8);
		System.out.println("Bouncycastle HmacSHA224的密钥:"+ new String(key8));
		System.out.println("Bouncycastle HmacSHA224算法摘要："+ new String(data8));
		System.out.println("Bouncycastle HmacSHA224算法摘要："+ new String(datahex8));
		System.out.println();
		
	}
}
