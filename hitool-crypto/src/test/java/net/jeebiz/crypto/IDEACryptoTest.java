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
package net.jeebiz.crypto;

import org.apache.commons.codec.binary.Base64;

import net.jeebiz.crypto.algorithm.IDEACrypto;

public class IDEACryptoTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String str="IDEA";
		System.out.println("原文："+str);
		//初始化密钥
		byte[] key = IDEACrypto.initkey();
		System.out.println("密钥："+Base64.encodeBase64String(key));
		//加密数据
		byte[] data=IDEACrypto.encrypt(str.getBytes(), key);
		System.out.println("加密后："+Base64.encodeBase64String(data));
		//解密数据
		data= IDEACrypto.decrypt(data, key);
		System.out.println("解密后："+new String(data));
	}
	
}
