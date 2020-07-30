package hitool.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;

public interface Crypto {
	
	/**
	 * 
	 *  初始化key
	 * @return
	 * @throws Exception
	 */
	 public byte[] initkey() throws GeneralSecurityException;
	 
	 /**
	  * 
	  *  还原key
	  * @param key
	  * @return
	  * @throws Exception
	  */
	 public Key toKey(byte[] key) throws GeneralSecurityException;
	 
	 public Key toKey(String key) throws GeneralSecurityException;
	 
}
