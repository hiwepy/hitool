package hitool.crypto.algorithm;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import hitool.crypto.StringVerifier;
import hitool.crypto.digest.DigestUtils;
import hitool.crypto.utils.StringUtils;

public class SHAHexCrypto  implements StringEncoder,BinaryEncoder,StringVerifier  {
	
	private static SHAHexCrypto instance = null;
	private SHAHexCrypto(){};
	public static SHAHexCrypto getInstance(){
		instance= (instance==null)?instance=new SHAHexCrypto():instance;
		return  instance;
	}

	
	private byte[] buffer(byte[] encoded){
		StringBuffer buf  = new StringBuffer();
        for (int i = 0; i < encoded.length; i++) {
            if ((encoded[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encoded[i] & 0xff, 16));
        }
        return StringUtils.getBytesUtf8(buf.toString());
	}

	@Override
	public Object encode(Object source) throws EncoderException{
		if (source == null) {
            return null;
        } else if (source instanceof String) {
            return encode((String) source);
        } else {
            throw new EncoderException("Objects of type " + source.getClass().getName() + " cannot be encoded using SHAHexCodec");
        }
	}

	@Override
	public String encode(String source){
		return Hex.encodeHexString(buffer(DigestUtils.sha(source)) );
	}
	
	/**
	 * 提供一个MD5多次加密方法
	 */
	public String encode(String source, int times){
		//第一次加密
		byte[] binaryData = buffer(DigestUtils.sha(source));
		for (int i = 0; i < times - 1; i++) {
			//多次加密
			binaryData = buffer(DigestUtils.sha(binaryData));
		}
		return Hex.encodeHexString(binaryData);
	}
	
	/**
	 * 
	 *  SHA-256消息摘要
	 * @param source
	 * @return
	 * @throws EncoderException
	 */
	public String encodeSHA256(String source){
        return Hex.encodeHexString(buffer(DigestUtils.sha256(source)) );
	}
	
	public String encodeSHA384(String source){
        return Hex.encodeHexString(buffer(DigestUtils.sha384(source)) );
	}
	
	public String encodeSHA512(String source){
        return Hex.encodeHexString(buffer(DigestUtils.sha512(source)) );
	}
	
	@Override
	public byte[] encode(byte[] source){
		 return Hex.encodeHexString(buffer(DigestUtils.sha(source)) ).getBytes();
	}
	
	public byte[] encode(byte[] bytes, int times){
		//第一次加密
		byte[] binaryData = buffer(DigestUtils.sha(bytes));
		for (int i = 0; i < times - 1; i++) {
			//多次加密
			binaryData = buffer(DigestUtils.sha(binaryData));
		}
		return Hex.encodeHexString(binaryData).getBytes();
	}
	
	public byte[] encodeSHA256(byte[] source) throws EncoderException{
		return Hex.encodeHexString(buffer(DigestUtils.sha256(source)) ).getBytes();
	}
	
	public byte[] encodeSHA384(byte[] source) throws EncoderException{
		return Hex.encodeHexString(buffer(DigestUtils.sha384(source)) ).getBytes();
	}

	public byte[] encodeSHA512(byte[] source) throws EncoderException{
		return Hex.encodeHexString(buffer(DigestUtils.sha512(source)) ).getBytes();
	}
	
	public String encode(InputStream source) throws IOException {
		return Hex.encodeHexString(buffer(DigestUtils.sha(source)) );
	}
	
	public String encodeSHA256(InputStream source) throws IOException{
		return Hex.encodeHexString(buffer(DigestUtils.sha256(source)) );
	}
	
	public String encodeSHA384(InputStream source) throws IOException{
		return Hex.encodeHexString(buffer(DigestUtils.sha384(source)) );
	}
	
	public String encodeSHA512(InputStream source) throws IOException{
		return Hex.encodeHexString(buffer(DigestUtils.sha512(source)) );
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
