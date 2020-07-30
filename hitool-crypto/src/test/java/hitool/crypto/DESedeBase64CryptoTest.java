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

import hitool.crypto.algorithm.DESedeBase64Crypto;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.SecretKeyUtils;

public class DESedeBase64CryptoTest {

	/**
	 * 进行加解密的测试
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String str="DESede";
		String encryptKey = Base64.encodeBase64String(SecretKeyUtils.genSecretKey(Algorithm.KEY_DESEDE, 168).getEncoded());
		
		System.out.println("原文：/t"+str);
		System.out.println("密钥：/t"+encryptKey);
		//加密数据
		String encryptedText = DESedeBase64Crypto.getInstance().encode(str, encryptKey);
		System.out.println("加密后：/t"+encryptedText);
		//解密数据
		System.out.println("解密后：/t"+DESedeBase64Crypto.getInstance().decode(encryptedText, encryptKey));
		
	}
}
