package hitool.crypto;

import java.security.cert.Certificate;
import java.util.Date;

/**
 * 
 * 证书验证接口
 */
public interface CertificateVerifier {

	/**
	 * 
	 *  判断证书是否过期
	 * @param date
	 * @param certificate
	 * @return
	 */
	public boolean expire(Date date, Certificate certificate);
	public boolean expire(Certificate certificate);
	public boolean expire(String certificatePath);
	public boolean expire(Date date, String certificatePath);
	
	public boolean verify(String keyStorePath, String alias,String password);
	
	public boolean verify(Date date, String keyStorePath,String alias, String password);
	
	public boolean verify(byte[] data, String sign,String certificatePath) throws Exception;
	
}
