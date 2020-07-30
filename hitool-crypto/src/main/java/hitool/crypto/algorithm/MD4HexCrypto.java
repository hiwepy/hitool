package hitool.crypto.algorithm;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import hitool.crypto.InputStreamEncryptor;
import hitool.crypto.StringVerifier;
import hitool.crypto.digest.DigestUtils;
import hitool.crypto.utils.StringUtils;
/**
 * 
 * bouncy castle扩展支持的MD4的算法实现
 */
public class MD4HexCrypto implements StringEncoder,BinaryEncoder,InputStreamEncryptor,StringVerifier  {

	private static MD4HexCrypto instance = null;
	private MD4HexCrypto(){};
	public static MD4HexCrypto getInstance(){
		instance= (instance==null)?instance=new MD4HexCrypto():instance;
		return  instance;
	}
	
	public Object encode(Object source) throws EncoderException{
		if (source == null) {
            return null;
        } else if (source instanceof String) {
            return encode((String) source);
        } else {
            throw new EncoderException("Objects of type " + source.getClass().getName() + " cannot be encoded using MD4HexCodec");
        }
	}

	public String encode(String source){
		return Hex.encodeHexString(DigestUtils.md4(source.getBytes()));
	}
	
	public String encode(String source, int times){
		String encoded = encode(source);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	
	public byte[] encode(byte[] source){
		return StringUtils.getBytesUtf8(Hex.encodeHexString(DigestUtils.md4(source)));
	}
	
	public byte[] encode(byte[] bytes, int times){
		byte[] encoded = encode(bytes);
		for (int i = 0; i < times - 1; i++) {
			encoded = encode(encoded);
		}
		return encoded;
	}
	
	public String encode(InputStream source) throws IOException {
		return Hex.encodeHexString(DigestUtils.md4(source));
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

}
