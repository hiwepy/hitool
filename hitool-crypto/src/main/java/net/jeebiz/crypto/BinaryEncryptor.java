package net.jeebiz.crypto;

import org.apache.commons.codec.EncoderException;

public interface BinaryEncryptor extends org.apache.commons.codec.BinaryEncoder{

	public byte[] encode(byte[] plainBytes, int times) throws EncoderException;
	
}
