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

import java.security.GeneralSecurityException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.CipherUtils;
import hitool.crypto.utils.SecretKeyUtils;

public class CipherUtils_Test {
	
	public static void main(String[] args) throws Exception {
		// 加入bouncyCastle支持
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
			//Security.insertProviderAt(new BouncyCastleProvider(), 1);
		}
		if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastlePQCProvider());
		}
		try {

			SecretKeyUtils.genSecretKey(Algorithm.KEY_AES, 56);

			byte[] key = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_AES, 56);

			CipherUtils.getDecryptCipher(Algorithm.KEY_CIPHER_AES, SecretKeyUtils.genSecretKey(key, Algorithm.KEY_AES));

		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

	}

}
