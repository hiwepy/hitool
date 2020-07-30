package hitool.crypto.algorithm;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.CipherUtils;
import hitool.crypto.utils.SecretKeyUtils;

/**
 * IDEA对称加密算法，java6不支持这个算法的实现，bouncycastle支持IDEA对称加密算法
 * 这是一款对称分组密码。是目前比较常用的电子邮件加密算法之一
 * 我们可以参照这个算法的实现来完成其他算法的实现：Rijndael,Serpent,Twofish等
 * @author kongqz
 * */
public class IDEACrypto {
	
	/**
	 * 
	 * 生成密钥，只有bouncycastle支持
	 * @return byte[] 二进制密钥
	 * */
	public static byte[] initkey() throws Exception{
		//实例化密钥生成器；初始化密钥生成器，IDEA要求密钥长度为128位；生成密钥
		SecretKey secretKey = SecretKeyUtils.genSecretKey(Algorithm.KEY_IDEA, 128);
		//获取二进制密钥编码形式
		return secretKey.getEncoded();
	}
	/**
	 * 转换密钥
	 * @param key 二进制密钥
	 * @return Key 密钥
	 * */
	public static Key toKey(byte[] key) throws Exception{
		//生成密钥
		return SecretKeyUtils.genSecretKey(key,Algorithm.KEY_IDEA);
	}
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		//还原密钥
		Key secretKey = toKey(key);
		//实例化加密模式Cipher
		Cipher cipher = CipherUtils.getEncryptCipher(Algorithm.KEY_CIPHER_IDEA, secretKey);
		//执行操作
		return cipher.doFinal(data);
	}
	/**
	 * 解密数据
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		//还原密钥
		Key secretKey = toKey(key);
		//实例化解密模式Cipher
		Cipher cipher = CipherUtils.getDecryptCipher(Algorithm.KEY_CIPHER_IDEA, secretKey);
		//执行操作
		return cipher.doFinal(data);
	}
	
}
