package net.jeebiz.crypto.algorithm;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;
import net.jeebiz.crypto.StringVerifier;
import net.jeebiz.crypto.digest.DigestUtils;
/**
 * 
 * bouncy castle扩展支持的MD4的算法实现
 */
public class MD4Base64Crypto implements StringEncoder,BinaryEncoder,StringVerifier  {

	private static MD4Base64Crypto instance = null;
	private MD4Base64Crypto(){};
	public static MD4Base64Crypto getInstance(){
		instance= (instance==null)?instance=new MD4Base64Crypto():instance;
		return  instance;
	}
	
	public Object encode(Object plainObject) throws EncoderException{
		if (plainObject == null) {
            return null;
        } else if (plainObject instanceof String) {
            return encode((String) plainObject);
        } else {
            throw new EncoderException("Objects of type " + plainObject.getClass().getName() + " cannot be encoded using MD4HexCodec");
        }
	}

	public String encode(String plainText){
		return Base64.encodeBase64String(DigestUtils.md4(plainText));
	}
	
	public String encode(String plainText, int times){
		String encoded = encode(plainText);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	
	public byte[] encode(byte[] source){
		return Base64.decodeBase64(DigestUtils.md4(source));
	}
	
	public byte[] encode(byte[] bytes, int times){
		byte[] encoded = encode(bytes);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	/**
	 * 密码验证方法
	 */
	public boolean verify(String plainText, String encrypt){
		return encode(plainText).equals(encrypt);
	}

	/**
	 * 重载一个多次加密时的密码验证方法
	 * @throws EncoderException 
	 */
	public boolean verify(String plainText, String encryptedText, int times){
		return encode(plainText, times).equals(encryptedText);
	}

}
