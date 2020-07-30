package net.jeebiz.crypto.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSAPublicPrivateKeyAnalysis {
	
	public static char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static void main(String[] args) throws Exception {
		byte[] content = getSourceFromFile("D:\\pubpri");
		printByteArray(content, 16);

		byte[] m, pubE, priE, p, q, ep, eq, cce;
		int start = 3;
		int end = 3 + 0x81;
		m = Arrays.copyOfRange(content, start, end);
		start = end + 2;
		end = start + 3;
		pubE = Arrays.copyOfRange(content, start, end);
		start = end + 3;
		end = start + 0x80;
		priE = Arrays.copyOfRange(content, start, end);

		start = end + 2;
		end = start + 0x41;
		p = Arrays.copyOfRange(content, start, end);

		start = end + 2;
		end = start + 0x41;
		q = Arrays.copyOfRange(content, start, end);

		start = end + 2;
		end = start + 0x40;
		ep = Arrays.copyOfRange(content, start, end);

		start = end + 2;
		end = start + 0x40;
		eq = Arrays.copyOfRange(content, start, end);

		start = end + 2;
		end = start + 0x40;
		cce = Arrays.copyOfRange(content, start, end);

		printByteArray(m, 16);
		printByteArray(pubE, 16);
		printByteArray(priE, 16);
		printByteArray(p, 16);
		printByteArray(q, 16);
		printByteArray(ep, 16);
		printByteArray(eq, 16);
		printByteArray(cce, 16);

		//���㼸����֮��Ĺ�ϵ
		BigInteger primeP = new BigInteger(p);
		BigInteger primeQ = new BigInteger(q);
		BigInteger modulus = new BigInteger(m);

		System.out.println("modulus = primeQ * primeP ? "
				+ modulus.equals(primeQ.multiply(primeP)));

		BigInteger dp = new BigInteger(ep);
		BigInteger d = new BigInteger(priE);
		System.out.println("dp = d mod(p - 1) ? "
				+ dp.equals(d.mod(primeP.subtract(new BigInteger("1")))));
		BigInteger dq = new BigInteger(eq);
		System.out.println("dq = d mod(q - 1) ? "
				+ dq.equals(d.mod(primeQ.subtract(new BigInteger("1")))));

		RSAPublicKey pubKey = constructRSAPublicKey(m, pubE);
		/*RSAPrivateKey priKey = constructRSAPrivateKey(m, pubE, priE, p, q, ep,
				eq, cce);*/

		RSAPrivateKey priKey = constructRSAPrivateKey(m, priE);
		String source = "hello world";
		Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initSign(priKey);
		sign.update(source.getBytes());
		byte[] signature = sign.sign();
		System.out.println();
		printByteArray(signature, 16);

		Signature verifier = Signature.getInstance("SHA1withRSA");
		X509Certificate cert = ummarshalCertificate("D:\\cert.cer");
		//verifier.initVerify(pubKey);
		//verifier.initVerify(cert);
		verifier.initVerify(cert.getPublicKey());
		verifier.update(source.getBytes());
		System.out.println("verify signature : " + verifier.verify(signature));

		Cipher encryptor = Cipher.getInstance("RSA");
		//encryptor.init(Cipher.ENCRYPT_MODE, pubKey);
		//encryptor.init(Cipher.ENCRYPT_MODE, cert);
		encryptor.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());

		Cipher decryptor = Cipher.getInstance("RSA");
		decryptor.init(Cipher.DECRYPT_MODE, priKey);

		encryptor.update(source.getBytes());
		byte[] cipherText = encryptor.doFinal();

		decryptor.update(cipherText);
		byte[] plainText = decryptor.doFinal();
		System.out.println(new String(plainText));

	}

	public static RSAPublicKey constructRSAPublicKey(byte[] m, byte[] pubE)
			throws Exception {
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(m),new BigInteger(pubE));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
	}

	public static RSAPrivateKey constructRSAPrivateKey(byte[] m, byte[] priE)
			throws Exception {
		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(m),
				new BigInteger(priE));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(priKeySpec);
	}

	public static RSAPrivateKey constructRSAPrivateKey(byte[] m, byte[] pubE,
			byte[] priE, byte[] p, byte[] q, byte[] ep, byte[] eq, byte[] cce)
			throws Exception {
		RSAPrivateCrtKeySpec priKeySpec = new RSAPrivateCrtKeySpec(
				new BigInteger(m), new BigInteger(pubE), new BigInteger(priE),
				new BigInteger(p), new BigInteger(q), new BigInteger(ep),
				new BigInteger(eq), new BigInteger(cce));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(priKeySpec);
	}

	public static byte[] getSourceFromFile(String fileName) throws Exception {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (fis.available() > 0) {
			baos.write(fis.read());
		}
		fis.close();
		return baos.toByteArray();
	}

	public static void printByteArray(byte[] source, int lineLength) {
		System.out.println("start printByteArray()...");
		int i = 0;
		while (i < source.length) {
			System.out.print(hexChars[(source[i] & 0xFF)>> 4]);
			System.out.print(hexChars[source[i] & 0xF]);
			if (i % lineLength == 15) {
				System.out.println("");
			} else {
				System.out.print("-");
			}
			i++;
		}
		System.out.println("\nprintByteArray() end");
	}

	public static X509Certificate ummarshalCertificate(String certFileName) throws Exception {
		File certFile = new File(certFileName);
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		FileInputStream fis = new FileInputStream(certFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (fis.available() > 0) {
			baos.write(fis.read());
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(baos.toByteArray()));
		fis.close();
		return (X509Certificate) certFactory.generateCertificate(bais);
	}
	
}
