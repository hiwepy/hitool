package hitool.crypto;

import org.apache.commons.codec.EncoderException;

/**
 * 
 * 二级制验证
 */
public interface BinaryVerifier {
	
	public boolean verify(byte[] plantBytes,byte[] encrypt) throws EncoderException;
	
	public boolean verify(byte[] plantBytes,byte[] encrypt, int times) throws EncoderException;
	
}
