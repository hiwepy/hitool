package net.jeebiz.crypto;

import org.apache.commons.codec.DecoderException;

public interface BinaryDecryptor extends org.apache.commons.codec.BinaryDecoder {

	public byte[] decode(byte[] encryptedBytes, int times) throws DecoderException;
	
}
