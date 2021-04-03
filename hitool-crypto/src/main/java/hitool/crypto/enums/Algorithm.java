package hitool.crypto.enums;

public final class Algorithm {

	/*
	 * AES算法
	 * java6支持56位密钥，bouncycastle支持64位
	 * */
	public final static String KEY_AES  = "AES";
	/*
	 * 解密算法/工作模式/填充方式
	 * 
	 * JAVA6 支持PKCS5PADDING填充方式
	 * Bouncy castle支持PKCS7Padding填充方式
	 * */
	public final static String KEY_CIPHER_AES  = "AES/ECB/PKCS5Padding";
	
	public final static String KEY_BASE64  = "Base64";
	
	public final static String KEY_DES  = "DES";
	
	public final static String KEY_CIPHER_DES  = "DES/ECB/PKCS7Padding";
	
	public final static String KEY_DESEDE  = "DESede";
	
	public final static String KEY_CIPHER_DESEDE  = "DESede/ECB/PKCS7Padding";
	
	/*
	 * RSA对称加密算法
	 */
	
	public final static String KEY_RSA  = "RSA";
 	public final static String KEY_SIGNATURE_MD5_RSA = "MD5withRSA";
 	public final static String KEY_SIGNATURE_SHA1_RSA = "SHA1withRSA";
	/*
	 * 加密/解密算法/工作模式/填充方式
	 * */
	public final static String KEY_CIPHER_RSA_ECB_NOPADDING  = "RSA/ECB/NoPadding";
	public final static String KEY_CIPHER_RSA_ECB_PKCS1PADDING  = "RSA/ECB/PKCS1Padding";
	
	/*
	 * IDEA对称加密算法，java6不支持这个算法的实现，bouncycastle支持IDEA对称加密算法
	 */
	public final static String KEY_IDEA  = "IDEA";
	/*
	 * 加密/解密算法/工作模式/填充方式
	 * */
	public final static String KEY_CIPHER_IDEA  = "IDEA/ECB/ISO10126Padding";
	
	public final static String KEY_SHA1  = "sha1";
	
	public final static String KEY_SHA256  = "sha256";
	
	public final static String KEY_SHA384  = "sha384";
	
	public final static String KEY_SHA512  = "sha512";
	
	public final static String KEY_SIGNATURE_SHA512_RSA = "SHA512withRSA";
	
	
	
	public final static String KEY_SM3  = "SM3";
	/*
	 * BouncyCastle实现;摘要长度 16 位
	 */
	public final static String KEY_MD2  = "MD2";
	/*
	 * BouncyCastle实现;摘要长度 16 位
	 */
	public final static String KEY_MD4  = "MD4";
	/*
	 * SUN 实现;摘要长度 16 位
	 */
	public final static String KEY_MD5  = "MD5";
	/*
	 * BouncyCastle实现;摘要长度 32 位
	 */
	public final static String KEY_GOST3411  = "GOST3411";
	/*
	 * BouncyCastle实现;摘要长度 128位
	 */
	public final static String KEY_RIPEMD128  = "RipeMD128";
	/*
	 * BouncyCastle实现;摘要长度 160位
	 */
	public final static String KEY_RIPEMD160  = "RipeMD160";
	/*
	 * BouncyCastle实现;摘要长度 256位
	 */
	public final static String KEY_RIPEMD256  = "RipeMD256";
	/*
	 * BouncyCastle实现;摘要长度 320位
	 */
	public final static String KEY_RIPEMD320  = "RipeMD320";
	/*
	 * HmacSHA1-BouncyCastle才支持的实现
	 */
	public final static String KEY_HMAC_SHA1  = "HmacSHA1";
	/*
	 * HmacMD2-BouncyCastle才支持的实现
	 */
	public final static String KEY_HMAC_MD2  = "HmacMD2";
	/*
	 * HmacMD4-BouncyCastle才支持的实现
	 */
	public final static String KEY_HMAC_MD4  = "HmacMD4";
	
	public final static String KEY_HMAC_MD5  = "HmacMD5";
	/*
	 * HmacSHA224-BouncyCastle才支持的实现
	 */
	public final static String KEY_HMAC_SHA224  = "HmacSHA224";
	
	public final static String KEY_HMAC_SHA256  = "HmacSHA256";
	
	public final static String KEY_HMAC_SHA384  = "HmacSHA384";
	
	public final static String KEY_HMAC_SHA512  = "HmacSHA512";
	/*
	 * BouncyCastle实现;摘要长度 128位
	 */
	public final static String KEY_HMAC_RipeMD128  = "HmacRipeMD128";
	/*
	 * BouncyCastle实现;摘要长度 160位
	 */
	public final static String KEY_HMAC_RipeMD160  = "HmacRipeMD160";
	
	public final static String KEY_PBE_MD5_DES  = "PBEWITHMD5ANDDES";
	
	public final static String KEY_PBE_MD5_TRIPLEDES  = "PBEWITHMD5ANDTRIPLEDES";
	
	public final static String KEY_PBE_SHA1_DESEDE  = "PBEWITHSHA1ANDDESEDE";
	
	public final static String KEY_PBE_SHA1_RC2_40  = "PBEWITHSHA1ANDRC2_40";
	
	public final static String KEY_PBE_HMACSHA1  = "PBKDF2WITHHMACSHA1";
	

	
}
