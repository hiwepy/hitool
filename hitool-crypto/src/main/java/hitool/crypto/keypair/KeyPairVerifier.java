package hitool.crypto.keypair;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

/*
 */
public interface KeyPairVerifier<Pub extends PublicKey, Pri extends PrivateKey> {

	public boolean verify(byte[] encryptedBytes, Pub pubKey, String signText) throws GeneralSecurityException;

	public boolean verify(byte[] encryptedBytes, byte[] keyBytes, String signText) throws GeneralSecurityException;

	public boolean verify(byte[] encryptedBytes, Pub pubKey, byte[] signBytes) throws GeneralSecurityException;

	public boolean verify(byte[] encryptedBytes, byte[] keyBytes, byte[] signBytes) throws GeneralSecurityException;

}
