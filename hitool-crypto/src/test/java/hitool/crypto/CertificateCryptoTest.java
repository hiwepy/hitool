package hitool.crypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hitool.crypto.algorithm.CertificateCrypto;
import org.junit.Test;

/*
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class CertificateCryptoTest {
	private String password = "123456";
	private String alias = "www.zlex.org";
	private String certificatePath = "d:/zlex.cer";
	private String keyStorePath = "d:/zlex.keystore";


	public static void main(String[] args) {
		/*
		步骤一：生成keyStroe文件 
		keytool -genkey -validity 36000 -alias www.zlex.org -keyalg RSA -keystore d:\zlex.keystore 
		
		
		
		**/
		
		
	}
	
	@Test
	public void test0() throws Exception {

	}

	// @Test
	public void atest() throws Exception {
		System.out.println("公钥加密——私钥解密");
		String inputStr = "Ceritifcate";
		byte[] data = inputStr.getBytes();

		byte[] encrypt = CertificateCrypto.encryptByPublicKey(data,
				certificatePath);

		byte[] decrypt = CertificateCrypto.decryptByPrivateKey(encrypt,
				keyStorePath, alias, password);
		String outputStr = new String(decrypt);

		System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

		// 验证数据一致
		assertArrayEquals(data, decrypt);

		// 验证证书有效
		assertTrue(CertificateCrypto.verifyCertificate(certificatePath));

	}

	// @Test
	public void atestSign() throws Exception {
		System.out.println("私钥加密——公钥解密");

		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = CertificateCrypto.encryptByPrivateKey(data,
				keyStorePath, alias, password);

		byte[] decodedData = CertificateCrypto.decryptByPublicKey(encodedData,
				certificatePath);

		String outputStr = new String(decodedData);
		System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		assertEquals(inputStr, outputStr);

		System.out.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = CertificateCrypto.sign(encodedData, keyStorePath, alias,
				password);
		System.out.println("签名:\r" + sign);

		// 验证签名
		boolean status = CertificateCrypto.verify(encodedData, sign,
				certificatePath);
		System.out.println("状态:\r" + status);
		assertTrue(status);

	}
}
