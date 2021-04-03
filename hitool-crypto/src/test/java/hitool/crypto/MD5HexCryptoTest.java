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

import org.apache.commons.codec.EncoderException;

import hitool.crypto.algorithm.MD5HexCrypto;

public class MD5HexCryptoTest {

	/*
	 * 提供一个测试的主函数
	 * @throws EncoderException 
	 */
	public static void main(String[] args){
		
		MD5HexCrypto md5hex = MD5HexCrypto.getInstance();
		/*try {
			System.out.println(getMD5DigestHex("").toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println("test:" + md5hex.encode("").toUpperCase());
		System.out.println("123:" + md5hex.encode("123").toUpperCase());
		System.out.println("123456789:" + md5hex.encode("123456789").toUpperCase().length());
		System.out.println("sarin:" + md5hex.encode("sarin").toUpperCase().length());
		System.out.println("123:" + md5hex.encode("123", 4).toUpperCase().length());
	}
	
}
