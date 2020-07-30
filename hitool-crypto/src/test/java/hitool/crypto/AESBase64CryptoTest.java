/**
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

import org.apache.commons.codec.binary.Base64;

import hitool.crypto.algorithm.AESBase64Crypto;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.SecretKeyUtils;

public class AESBase64CryptoTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AESBase64Crypto codec = new AESBase64Crypto();
		String str = "AES";
		System.out.println("原文：" + str);
		//初始化密钥生成器，AES要求密钥长度为128位、192位、256位
		byte[] base64Key = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_AES, 256);
		
		
		System.out.println(base64Key.length);
		System.out.println(Base64.encodeBase64(base64Key).length);
		System.out.println("密钥：" + Base64.encodeBase64String(Base64.encodeBase64(base64Key)));
		// 加密数据
		byte[] data = codec.encode(str.getBytes(), Base64.encodeBase64(base64Key));
		System.out.println("加密后：" + Base64.encodeBase64String(data));
		// 解密数据
		data = codec.decode(data, base64Key);
		System.out.println("解密后：" + new String(data));
	}

}
