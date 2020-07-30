package hitool.crypto.keypair;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 */
public interface KeyPairSigner<Pub extends PublicKey, Pri extends PrivateKey> {

	public String sign(byte[] encryptedBytes, Pri priKey) throws GeneralSecurityException;
	
	public String sign(byte[] encryptedBytes, byte[] keyBytes) throws GeneralSecurityException;
	
}
