package hitool.crypto;

import org.apache.commons.codec.DecoderException;

public interface StringDecryptor extends org.apache.commons.codec.StringDecoder{

	public String decode(String encryptedText, int times) throws DecoderException;
	
}
