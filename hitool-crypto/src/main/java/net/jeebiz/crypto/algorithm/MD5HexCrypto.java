package net.jeebiz.crypto.algorithm;


import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import net.jeebiz.crypto.InputStreamEncryptor;
import net.jeebiz.crypto.StringVerifier;
import net.jeebiz.crypto.utils.StringUtils;
/**
 * 
 * 标准MD5+Hex加密方法，使用java类库的security包的MessageDigest类处理
 */
public class MD5HexCrypto implements StringEncoder,BinaryEncoder,InputStreamEncryptor,StringVerifier {
	
	private static MD5HexCrypto instance = null;
	private MD5HexCrypto(){};
	public static MD5HexCrypto getInstance(){
		instance= (instance==null)?instance=new MD5HexCrypto():instance;
		return  instance;
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

	@Override
	public Object encode(Object source) throws EncoderException{
		if (source == null) {
            return null;
        } else if (source instanceof String) {
            return encode((String) source);
        } else {
            throw new EncoderException("Objects of type " + source.getClass().getName() + " cannot be encoded using MD5HexCodec");
        }
	}

	@Override
	public byte[] encode(byte[] source){
		return StringUtils.getBytesUtf8(DigestUtils.md5Hex(source));
	}

	@Override
	public String encode(String source){
		return DigestUtils.md5Hex(source);
	}
	
	/**
	 * 提供一个MD5多次加密方法
	 */
	public String encode(String source, int times){
		String encoded = encode(source);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	
	public String encode(InputStream source) throws IOException {
		return Hex.encodeHexString(DigestUtils.md5(source));
	}
	
}