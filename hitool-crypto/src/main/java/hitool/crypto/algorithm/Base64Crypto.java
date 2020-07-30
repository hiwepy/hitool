package hitool.crypto.algorithm;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import hitool.crypto.BinaryDecryptor;
import hitool.crypto.BinaryEncryptor;
import hitool.crypto.BinaryVerifier;
import hitool.crypto.StringDecryptor;
import hitool.crypto.StringEncryptor;
import hitool.crypto.StringVerifier;
import hitool.crypto.utils.StringUtils;
/**
 * 
 * 基于apache codec 的base64加密解密实现的扩展
 */
public class Base64Crypto implements StringEncryptor,StringDecryptor,BinaryEncryptor,BinaryDecryptor,StringVerifier,BinaryVerifier {
	
	private static Base64Crypto instance = null;
	private Base64Crypto(){};
	public static Base64Crypto getInstance(){
		instance= (instance==null)?instance=new Base64Crypto():instance;
		return  instance;
	}
	
	public Object encode(Object source) throws EncoderException{
		if (source == null) {
            return null;
        } else if (source instanceof String) {
            return encode((String) source);
        } else {
            throw new EncoderException("Objects of type " + source.getClass().getName() + " cannot be encoded using Base64Codec");
        }
	}
	
	public String encode(String source){
		return Base64.encodeBase64String(source.getBytes());
	}
	
	
	public String encode(String source, int times){
		String encoded = encode(source);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	
	
	public byte[] encode(byte[] source){
		return Base64.encodeBase64(source);
	}
	
	public byte[] encode(byte[] bytes, int times){
		byte[] encoded = encode(bytes);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	
	public Object decode(Object source) throws DecoderException {
		if (source == null) {
            return null;
        } else if (source instanceof String) {
            return decode((String) source);
        } else {
            throw new DecoderException("Objects of type " + source.getClass().getName() + " cannot be decoded using Base64Codec");
        }
	}
	
	public String decode(String source) throws DecoderException {
		return StringUtils.newStringUtf8(Base64.decodeBase64(source));
	}
	
	public String decode(String source, int times) throws DecoderException {
		String encoded = decode(source);
		for (int i = 0; i < times - 1; i++) {
			encoded = decode(encoded);
		}
		return encoded;
	}
	
	public byte[] decode(byte[] base64Bytes) throws DecoderException {
		return Base64.decodeBase64(base64Bytes);
	}
	
	public byte[] decode(byte[] base64Bytes, int times) throws DecoderException {
		byte[] encoded = decode(base64Bytes);
		for (int i = 0; i < times - 1; i++) {
			encoded = decode(encoded);
		}
		return encoded;
	}
	
	/**
	 * 密码验证方法
	 */
	public boolean verify(String source, String encrypt){
		return encode(source).equals(encrypt);
	}

	/**
	 * 重载一个多次加密时的密码验证方法
	 * @throws EncoderException 
	 */
	public boolean verify(String source, String encrypt, int times){
		return encode(source, times).equals(encrypt);
	}
	
	
	public boolean verify(byte[] source, byte[] encrypt) throws EncoderException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean verify(byte[] source, byte[] encrypt, int times)
			throws EncoderException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String toString(String text) throws EncoderException{
		return "{Base64}" + encode(text);
	}
	
}
