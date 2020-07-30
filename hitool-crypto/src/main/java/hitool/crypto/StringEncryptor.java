package hitool.crypto;

import org.apache.commons.codec.EncoderException;

public interface StringEncryptor extends org.apache.commons.codec.StringEncoder {
	
	public String encode(String plainText, int times) throws EncoderException;
	
}
