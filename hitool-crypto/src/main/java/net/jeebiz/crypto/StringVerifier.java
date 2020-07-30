package net.jeebiz.crypto;

import org.apache.commons.codec.EncoderException;


public interface StringVerifier {
	
	public boolean verify(String plainText,String key) throws EncoderException;
	
	public boolean verify(String plainText,String key, int times) throws EncoderException;
	
}
