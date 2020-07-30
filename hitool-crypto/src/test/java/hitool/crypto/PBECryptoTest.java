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

import hitool.crypto.algorithm.PBECrypto;

public class PBECryptoTest {

	/**
	 * 使用PBE算法对数据进行加解密
	 * @throws Exception 
	 * 
	 */
	public static void main(String[] args) throws Exception {
		//待加密数据
		String str="PBE";
		//设定的口令密码
		String password="azsxdc";
		
		System.out.println("原文：/t"+str);
		System.out.println("密码：/t"+password);
		
		//初始化盐
		byte[] salt = PBECrypto.getInstance().initkey();
		System.out.println("盐：/t"+Base64.encodeBase64String(salt));
		//加密数据
		byte[] data=PBECrypto.getInstance().encode(str.getBytes(), password, salt);
		System.out.println("加密后：/t"+Base64.encodeBase64String(data));
		//解密数据
		data=PBECrypto.getInstance().decode(data, password, salt);
		System.out.println("解密后："+new String(data));
	}
}
