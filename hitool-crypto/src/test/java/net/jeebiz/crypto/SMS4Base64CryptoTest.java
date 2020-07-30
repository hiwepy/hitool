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

import net.jeebiz.crypto.algorithm.SMS4Base64Crypto;
import net.jeebiz.crypto.utils.StringUtils;

public class SMS4Base64CryptoTest {

public static void main(String[] args) throws Exception {
		
		
		byte[] in =  "4G USIM卡备卡是为您原卡制作的一张备用卡，相比原卡，安全性更高、容量更大、建议您尽快激活、更换使用USIM卡，并剪毁原卡，确保用卡安全。".getBytes();
		
		byte[] key = "300061103172120170".getBytes();
		
		System.out.println("Key：");
		for (int i = 0; i < key.length; i++){
			System.out.print(Integer.toHexString(key[i] & 0xff) + "");
		}
		
		byte[] out = null;
		long starttime;
		System.out.println();
		System.out.println("加密前：" + StringUtils.newStringUtf8(in));
		
		// 加密 128bit
		starttime = System.nanoTime();
		out = SMS4Base64Crypto.getInstance(key).encode(in);
		System.out.println();
		System.out.println("加密1个分组执行时间： " + (System.nanoTime() - starttime) + "ns");
		System.out.println("加密结果：");
		for (int i = 0; i < out.length; i++){
			System.out.print(Integer.toHexString(out[i] & 0xff) + "");
		}
		
		// 解密 128bit
		System.out.println();
		in = SMS4Base64Crypto.getInstance(key).decode(out);
		System.out.println("解密结果："+ StringUtils.newStringUtf8(in));
		
	}

	
}
