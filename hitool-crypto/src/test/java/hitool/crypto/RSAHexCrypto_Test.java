/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import hitool.crypto.keypair.KeyPairCrypto.KeyPairEntry;
import hitool.crypto.keypair.algorithm.RSACrypto;
import hitool.crypto.keypair.algorithm.RSAHexCrypto;

public class RSAHexCrypto_Test {

	private RSACrypto crypto = RSAHexCrypto.instance();
	private String plainText = "这是一段加密测试内容.";
	private String encryptedText = null;
	private RSAPublicKey pubKey;
	private RSAPrivateKey priKey;

	@Before
	public void setup() throws Exception {

		KeyPairEntry<RSAPublicKey, RSAPrivateKey> keyEntry = crypto.genKeyEntry(512);
		pubKey = keyEntry.getPublicKey();
		priKey = keyEntry.getPrivateKey();

	}

	@Test
	public void test1() throws Exception {

		System.out.println("公钥: \\n\\r:" + Base64.encodeBase64String(pubKey.getEncoded()));
		System.out.println("私钥： \\n\\r:" + Base64.encodeBase64String(priKey.getEncoded()));

	}

	@Test
	public void test2() throws Exception {

		encryptedText = crypto.encrypt(plainText, pubKey);
		System.out.println("encryptedText: " + encryptedText);

	}

	@Test
	public void test3() throws Exception {

		String dString = crypto.decrypt(encryptedText, priKey);
		assertEquals(dString, plainText);
		System.out.println("dString: " + dString);

	}

	@Test
	public void test4() throws Exception {
		/*
		 * String s = crypto.encrypt(plainText, pubKey); System.out.println("密文: " +
		 * Base64.encodeBase64String(s)); System.out.println("明文: " + crypto.decrypt(s,
		 * priKey));
		 */

	}

	// @Test
	public void atestSign() throws Exception {

		System.out.println("--------------------------------私钥签名——公钥验证签名-------------------------------- ");
		// 产生签名
		String sign = crypto.sign(encryptedText.getBytes(), priKey);
		System.out.println("签名:\r" + sign);

		// 验证签名
		boolean status = crypto.verify(encryptedText.getBytes(), pubKey, sign);
		System.out.println("状态:\r" + status);
		assertTrue(status);

	}
}
