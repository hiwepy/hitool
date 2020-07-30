package net.jeebiz.crypto;

import java.io.IOException;
import java.security.GeneralSecurityException;
/**
 * 
 * 
 */
public interface FileDecryptor {

	public void decode(String key, String encryptedFilePath,String destFilePath) throws GeneralSecurityException,IOException;
	
	public void decode(byte[] key, String encryptedFilePath,String destFilePath) throws GeneralSecurityException,IOException;
	
}
