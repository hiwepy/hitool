package hitool.crypto.algorithm;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import hitool.crypto.Crypto;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.CipherUtils;
import hitool.crypto.utils.SecretKeyUtils;
import hitool.crypto.utils.StringUtils;
/*
 * 
 * PBE——Password-based encryption（基于密码加密）。其特点在于口令由用户自己掌管，不借助任何物理媒体；采用随机数（这里我们叫做盐）杂凑多重加密等方法保证数据的安全性。是一种简便的加密方式。 
 * 使用java6提供的PBEWITHMD5andDES算法进行展示
 * JAVA6支持以下任意一种算法
 * PBEWITHMD5ANDDES
 * PBEWITHMD5ANDTRIPLEDES
 * PBEWITHSHAANDDESEDE
 * PBEWITHSHA1ANDRC2_40
 * PBKDF2WITHHMACSHA1
 */
public class PBECrypto implements Crypto {
	
	private static PBECrypto instance = null;
	private PBECrypto(){};
	public static PBECrypto getInstance(){
		instance= (instance==null)?instance=new PBECrypto():instance;
		return  instance;
	}
	
	/*
	 * 迭代次数
	 * */
	public static final int ITERATION_COUNT=100;
	
	/*
	 * 
	 *  盐初始化:盐长度必须为8字节
	 * @return byte[] 盐
	 * @throws GeneralSecurityException
	 */
	public byte[] initkey() throws GeneralSecurityException {
		//实例化安全随机数
		SecureRandom random = new SecureRandom();
		//产出盐
		return random.generateSeed(8);
	}

	public Key toKey(byte[] password) throws GeneralSecurityException {
		return toKey(StringUtils.newStringUtf8(password));
	}
	
	/*
	 * 
	 *  转换密钥
	 * @param password 密码
	 * @return Key 密钥
	 * @throws GeneralSecurityException
	 */
	public Key toKey(String password) throws GeneralSecurityException{
		return SecretKeyUtils.genPBEKey(password, Algorithm.KEY_PBE_MD5_DES);
	}
	
	/*
	 * 加密
	 * @param data 待加密数据
	 * @param password 密码
	 * @param salt 盐
	 * @return byte[] 加密数据
	 * 
	 * */
	public byte[] encode(byte[] plainBytes,String password,byte[] salt) throws Exception{
		//转换密钥
		Key key= toKey(password);
		//实例化PBE参数材料
		PBEParameterSpec paramSpec = new  PBEParameterSpec(salt,ITERATION_COUNT);
		//实例化
		Cipher cipher = CipherUtils.getCipher(Algorithm.KEY_PBE_MD5_DES);
		//初始化
		cipher.init(Cipher.ENCRYPT_MODE, key,paramSpec);
		//执行操作
		return cipher.doFinal(plainBytes);
	}
	
	/*
	 * 解密
	 * @param encryptedBytes 待解密数据
	 * @param password 密码
	 * @param salt 盐
	 * @return byte[] 解密数据
	 * 
	 * */
	public byte[] decode(byte[] encryptedBytes,String password,byte[] salt) throws Exception{
		//转换密钥
		Key key = toKey(password);
		//实例化PBE参数材料
		PBEParameterSpec paramSpec=new PBEParameterSpec(salt,ITERATION_COUNT);
		//实例化
		Cipher cipher=Cipher.getInstance(Algorithm.KEY_PBE_MD5_DES);
		//初始化
		cipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
		//执行操作
		return cipher.doFinal(encryptedBytes);
	}
	
}
