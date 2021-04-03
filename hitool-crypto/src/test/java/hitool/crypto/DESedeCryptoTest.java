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

import org.apache.commons.codec.binary.Base64;

import hitool.crypto.algorithm.DESedeCrypto;
import hitool.crypto.enums.Algorithm;
import hitool.crypto.utils.SecretKeyUtils;

public class DESedeCryptoTest {

	/*
	 * 进行加解密的测试
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String str="DESede";
		System.out.println("原文：/t"+str);
		//获得128位密钥
		byte[] base64Key = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_DESEDE, 168);
		System.out.println("密钥：/t"+Base64.encodeBase64String(base64Key));
		//加密数据
		byte[] data= DESedeCrypto.getInstance().encode(str.getBytes(), Base64.encodeBase64String(base64Key));
		System.out.println("加密后：/t"+Base64.encodeBase64String(data));
		//解密数据
		data= DESedeCrypto.getInstance().decode(data, Base64.encodeBase64String(base64Key));
		System.out.println("解密后：/t"+new String(data));
	}
}
