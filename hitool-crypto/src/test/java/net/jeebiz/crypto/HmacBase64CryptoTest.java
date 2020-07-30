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

import net.jeebiz.crypto.algorithm.DESBase64Crypto;
import net.jeebiz.crypto.algorithm.HmacBase64Crypto;
import net.jeebiz.crypto.enums.Algorithm;
import net.jeebiz.crypto.utils.SecretKeyUtils;
import net.jeebiz.crypto.utils.StringUtils;

public class HmacBase64CryptoTest {

	/**
	 * 进行相关的摘要算法的处理展示
	 * @throws Exception 
	 * **/
	public static void main(String[] args) throws Exception {
		

		String base64Key1 = StringUtils.newStringUtf8(DESBase64Crypto.getInstance().initkey());
		System.out.println("base64Key："+base64Key1);
		
		System.out.println("学校随机秘钥："+ HmacBase64Crypto.getEncryptKey("011111454",base64Key1));
		//成绩加密密钥：学号、课程号、成绩、学校随机密钥（存二维表实例下的表）
		String keyText = "3000611031-72120170-不及格-"+ HmacBase64Crypto.getDecryptKey(HmacBase64Crypto.getEncryptKey("011111454",base64Key1),base64Key1) ;
		System.out.println("keyText:"+keyText);
		
		HmacBase64Crypto codec1 = HmacBase64Crypto.getInstance(Base64.encodeBase64(keyText.getBytes()));
		
		System.out.println("================================================================:" );
		System.out.println("test:" +codec1.encode("不及格"));
		System.out.println("123:" + codec1.encode("123"));
		System.out.println("123456789:" + codec1.encode("123456789"));
		System.out.println("sarin:" + codec1.encode("sarin").length());
		
		
		System.out.println("================================================================:" );
		
		String plainText="HmacMD5消息摘要";
		//初始化HmacMD5的密钥
		byte[] base64Key = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_HMAC_MD5);
		System.out.println("Hmac的密钥:"+ new String(base64Key));
		System.out.println("原文："+plainText);
		
		HmacBase64Crypto codec = HmacBase64Crypto.getInstance(base64Key);
		//获取HmacMD5消息摘要信息
		System.out.println("HmacMD5算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA256);
		//获取摘要信息
		System.out.println("HmacSHA256算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA1);
		//获取摘要信息
		System.out.println("HmacSHA1算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA384);
		//获取摘要信息
		System.out.println("HmacSHA384算法摘要："+codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA512);
		//获取摘要信息
		System.out.println("HmacSHA512算法摘要："+ codec.encode(plainText));
		
		System.out.println("================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================");
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_MD2);
		//获取摘要信息
		System.out.println("Bouncycastle HmacMD2算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_MD4);
		//获取摘要信息
		System.out.println("Bouncycastle HmacMD4算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacBase64Crypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA224);
		//获取摘要信息
		System.out.println("Bouncycastle HmacSHA224算法摘要："+ codec.encode(plainText));
		
		/*base64Key：UkC58t+bfPc=
		学校随机秘钥：huNw6LTXyLY1hUbU2YMYOQ==
		keyText:3000611031-72120170-不及格-011111454
		================================================================:
		test:3mE2dFHRQHxY7ZXptopAow==

		123:o4IwTP5APYnMpFQQQkBnQw==

		123456789:jrHX0YdhCmzJ7qPUe+3lJQ==

		sarin:26
		================================================================:
		Hmac的密钥:,�B�oN]촧��(&�]<'�8\��j���)��/2�GT9Q2�7o1C�O�2jQ��"�v�T
		原文：HmacMD5消息摘要
		HmacMD5算法摘要：8gBN6cKizw83l0Zbacq7cw==

		HmacSHA256算法摘要：1y2b6Sm42Ouomm/5TPX0wCTTM7vzGr0vOEG6bzaoR28=

		HmacSHA1算法摘要：y1yhPN74Lla06uT6MPlKFN2+7oo=

		HmacSHA384算法摘要：5VcKQFB1MVhusqzjp7uoobPnGK/ZiGb5E6ETdN1ZVbiKl3QcWvrpq8HIuwVmW3wz

		HmacSHA512算法摘要：69mkMxBDmCKwHwKnpcbIRR0zJDrwZaWu/waXFE8J/QpNCMgPQ38ZtXRyZhSbNCULZ9rK6+1WKBru
		/ZjAgxCOcA==

		================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================
		Bouncycastle HmacMD2算法摘要：hnpnB1Ry65Af68T5Ngz0/A==

		Bouncycastle HmacMD4算法摘要：68U8Z7ulMICJNgcToGJyjw==

		Bouncycastle HmacSHA224算法摘要：+/R0v8A7bEAaAEPrIGJCoRj8yDSU7AzIWPa4fg==*/

	}
	
}
