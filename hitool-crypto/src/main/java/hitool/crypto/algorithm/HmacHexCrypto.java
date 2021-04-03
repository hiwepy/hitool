package hitool.crypto.algorithm;

import java.security.GeneralSecurityException;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import hitool.crypto.StringVerifier;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.HmacUtils;
import hitool.crypto.utils.SecretKeyUtils;
import hitool.crypto.utils.StringUtils;
/*
 *  MAC消息摘要组件
 */
public class HmacHexCrypto implements StringEncoder,BinaryEncoder,StringVerifier {
	
	private byte[] base64Key = null;
	private String algorithm = Algorithm.KEY_HMAC_MD5;
	private static HmacHexCrypto instance = null;
	
	private HmacHexCrypto(){};
	
	private HmacHexCrypto(byte[] base64Key){
		this.base64Key = base64Key;
	};
	
	private HmacHexCrypto(byte[] base64Key,String algorithm){
		this.base64Key = base64Key;
		this.algorithm = algorithm;
	};
	
	public static HmacHexCrypto getInstance(byte[] base64Key){
		instance= (instance==null)?instance=new HmacHexCrypto(base64Key):instance;
		instance.base64Key = base64Key;
		return  instance;
	}
	
	public static HmacHexCrypto getInstance(byte[] base64Key,String algorithm){
		instance= (instance==null)?instance=new HmacHexCrypto(base64Key):instance;
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
			return new String(Hex.encodeHex(HmacUtils.hmac(plainBytes, base64Key, algorithm), false)).getBytes();
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
			return new String(Hex.encodeHex(binaryData, false)).getBytes();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encode(String plainText) throws EncoderException {
		try {
			//获取Hmac消息摘要信息
			return Hex.encodeHexString(HmacUtils.hmac(plainText.getBytes(), base64Key, algorithm)).toUpperCase();
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
			return Hex.encodeHexString(binaryData).toUpperCase();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] encodeHmacMD2(byte[] plainBytes) throws EncoderException {
		//获取HmacMD2消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD2).encode(plainBytes);
	}
	
	public String encodeHmacMD2(String plainText) throws EncoderException {
		//获取HmacMD2消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD2).encode(plainText);
	}
	
	public byte[] encodeHmacMD4(byte[] plainBytes) throws EncoderException {
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD4).encode(plainBytes);
	}
	
	public String encodeHmacMD4(String plainText) throws EncoderException {
		//获取HmacMD4消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_MD4).encode(plainText);
	}
	
	public byte[] encodeHmacSHA1(byte[] plainBytes) throws EncoderException {
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA1).encode(plainBytes);
	}
	
	public String encodeHmacSHA1(String plainText) throws EncoderException {
		//获取HmacSHA1消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA1).encode(plainText);
	}
	
	public byte[] encodeHmacSHA224(byte[] plainBytes) throws EncoderException {
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA224).encode(plainBytes);
	}
	
	public String encodeHmacSHA224(String plainText) throws EncoderException {
		//获取HmacSHA224消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA224).encode(plainText);
	}
	
	public byte[] encodeHmacSHA256(byte[] plainBytes) throws EncoderException {
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA256).encode(plainBytes);
	}
	
	public String encodeHmacSHA256(String plainText) throws EncoderException {
		//获取HmacSHA256消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA256).encode(plainText);
	}
	
	public byte[] encodeHmacSHA384(byte[] plainBytes) throws EncoderException {
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA384).encode(plainBytes);
	}
	
	public String encodeHmacSHA384(String plainText) throws EncoderException {
		//获取HmacSHA384消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA384).encode(plainText);
	}
	
	public byte[] encodeHmacSHA512(byte[] plainBytes) throws EncoderException {
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA512).encode(plainBytes);
	}
	
	public String encodeHmacSHA512(String plainText) throws EncoderException {
		//获取HmacSHA512消息摘要信息
		return HmacHexCrypto.getInstance(base64Key, Algorithm.KEY_HMAC_SHA512).encode(plainText);
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
	
}
