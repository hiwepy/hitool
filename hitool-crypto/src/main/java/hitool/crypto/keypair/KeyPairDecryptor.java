package hitool.crypto.keypair;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairDecryptor<Pub extends PublicKey, Pri extends PrivateKey> {
	 
	 public byte[] decrypt(byte[] encryptedBytes, Pri priKey) throws GeneralSecurityException;
	 
	 public byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes) throws GeneralSecurityException;
	 
	 public String decrypt(String encryptedText, Pri priKey) throws GeneralSecurityException;
	 
	 public String decrypt(String encryptedText, byte[] keyBytes) throws GeneralSecurityException;
	 
	 public byte[] segmentDecrypt(byte[] encryptedBytes, Pri priKey) throws GeneralSecurityException;
	 
}

 

