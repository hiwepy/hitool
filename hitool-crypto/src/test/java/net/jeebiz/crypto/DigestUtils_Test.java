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
package net.jeebiz.crypto;

import java.security.Provider;
import java.security.Security;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import net.jeebiz.crypto.enums.Algorithm;
import net.jeebiz.crypto.utils.DigestUtils;

public class DigestUtils_Test {

	public static void main(String[] args) {
		// 加入bouncyCastle支持
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
			//Security.insertProviderAt(new BouncyCastleProvider(), 1);
		}
		if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastlePQCProvider());
		}
		for (Provider p : Security.getProviders()) {
			if(p.getName().startsWith("BC")) {
				System.out.println(p);
				for (Map.Entry<Object, Object> entry : p.entrySet()) {
					System.out.println("\t" + entry.getKey());
				}
				break;
			}
		}

		System.out.println(DigestUtils.getDigest(Algorithm.KEY_SM3));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD128));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD160));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD256));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD320));
		// System.out.println(DigestUtils.getDigest(Algorithm.KEY_HMAC_MD5).getProvider());
		// System.out.println(DigestUtils.getDigest(Algorithm.KEY_HMAC_MD5).digest().length);

	}

}
