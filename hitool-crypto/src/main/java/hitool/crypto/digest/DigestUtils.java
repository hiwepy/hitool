package hitool.crypto.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import hitool.crypto.enums.Algorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

/*
 * 
 */
@SuppressWarnings("deprecation")
public class DigestUtils extends org.apache.commons.codec.digest.DigestUtils {

	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			// 加入bouncyCastle支持
			Security.addProvider(new BouncyCastleProvider());
		}
		if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
			// 加入bouncyCastleJsse支持
			Security.addProvider(new BouncyCastlePQCProvider());
		}
	}

	private static final int STREAM_BUFFER_LENGTH = 1024;

	public static byte[] md4(String source) {
		return md4(source.getBytes());
	}

	public static byte[] md4(byte[] source) {
		return DigestUtils.getDigest(Algorithm.KEY_MD4).digest(source);
	}

	public static byte[] md4(InputStream source) throws IOException {
		return digest(DigestUtils.getDigest(Algorithm.KEY_MD4), source);
	}

	public static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
		byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
		int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

		while (read > -1) {
			digest.update(buffer, 0, read);
			read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
		}

		return digest.digest();
	}

	public static MessageDigest getDigest(Algorithm algorithm) {
		return DigestUtils.getDigest(algorithm.toString());
	}

	public static MessageDigest getDigest(Algorithm algorithm, String provider) {
		return DigestUtils.getDigest(algorithm.toString(), provider);
	}

	public static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static MessageDigest getDigest(String algorithm, String provider) {
		try {
			return MessageDigest.getInstance(algorithm, provider);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static void main(String[] args) {

		for (Provider p : Security.getProviders()) {
			System.out.println(p);
			/*for (Map.Entry<Object, Object> entry : p.entrySet()) {
				System.out.println("\t" + entry.getKey());
			}*/
		}
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_MD4));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_SM3));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD128));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD160));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD256));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD320));
		// System.out.println(DigestUtils.getDigest(Algorithm.KEY_HMAC_MD5).getProvider());
		// System.out.println(DigestUtils.getDigest(Algorithm.KEY_HMAC_MD5).digest().length);

	}
}
