package hitool.crypto.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/*
 * 
 * 
 */
public final class KeyStoreUtils {

	/*
	 * 
	 *  获得证书对象
	 * @param certificatePath 证书路径
	 * @return 
	 * @throws Exception
	 */
	public static Certificate getCertificate(String certificatePath) throws Exception {
		return KeyStoreUtils.getCertificate(new FileInputStream(certificatePath));
	}

	/*
	 * 
	 *  获得证书对象
	 * @param certificatePath 证书路径
	 * @return 
	 * @throws Exception
	 */
	public static Certificate getCertificate(FileInputStream in) throws Exception {
		
/*
		This class defines the functionality of a certificate factory, which is used to generate certificate, certification path (CertPath) and certificate revocation list (CRL) objects from their encodings. 

		For encodings consisting of multiple certificates, use generateCertificates when you want to parse a collection of possibly unrelated certificates. Otherwise, use generateCertPath when you want to generate a CertPath (a certificate chain) and subsequently validate it with a CertPathValidator. 

		A certificate factory for X.509 must return certificates that are an instance of java.security.cert.X509Certificate, and CRLs that are an instance of java.security.cert.X509CRL. 

		The following example reads a file with Base64 encoded certificates, which are each bounded at the beginning by -----BEGIN CERTIFICATE-----, and bounded at the end by -----END CERTIFICATE-----. We convert the FileInputStream (which does not support mark and reset) to a BufferedInputStream (which supports those methods), so that each call to generateCertificate consumes only one certificate, and the read position of the input stream is positioned to the next certificate in the file:
*/

		 /*FileInputStream fis = new FileInputStream(filename);
		 BufferedInputStream bis = new BufferedInputStream(fis);

		 CertificateFactory cf = CertificateFactory.getInstance("X.509");

		 while (bis.available() > 0) {
		    Certificate cert = cf.generateCertificate(bis);
		    System.out.println(cert.toString());
		 }*/
		 
		//The following example parses a PKCS#7-formatted certificate reply stored in a file and extracts all the certificates from it:

/*
		 FileInputStream fis = new FileInputStream(filename);
		 CertificateFactory cf = CertificateFactory.getInstance("X.509");
		 Collection c = cf.generateCertificates(fis);
		 Iterator i = c.iterator();
		 while (i.hasNext()) {
		    Certificate cert = (Certificate)i.next();
		    System.out.println(cert);
		 }
		 */

		 
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		Certificate certificate = certificateFactory.generateCertificate(in);
		in.close();
		return certificate;
	}
	
	
	public static KeyStore getKeyStore(String keyStore){
		return null;
	}
	
}
