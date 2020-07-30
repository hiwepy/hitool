package net.jeebiz.crypto.keypair;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public interface KeyPairEncryptor<Pub extends PublicKey, Pri extends PrivateKey> {
	 
	 public byte[] encrypt(byte[] plainBytes, Pub pubKey) throws GeneralSecurityException;
	 
	 public byte[] encrypt(byte[] plainBytes, byte[] keyBytes) throws GeneralSecurityException;
	 
	 public String encrypt(String plainText, Pub pubKey) throws GeneralSecurityException;
	 
	 public String encrypt(String plainText, byte[] keyBytes) throws GeneralSecurityException;
	 
	 public byte[] segmentEncrypt(byte[] plainBytes, RSAPublicKey pubKey) throws GeneralSecurityException;
	 
}

 

