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
import net.jeebiz.crypto.algorithm.HmacHexCrypto;
import net.jeebiz.crypto.enums.Algorithm;
import net.jeebiz.crypto.utils.SecretKeyUtils;
import net.jeebiz.crypto.utils.StringUtils;

public class HmacHexCryptoTest {

	/**
	 * 进行相关的摘要算法的处理展示
	 * @throws Exception 
	 * **/
	public static void main(String[] args) throws Exception {
		
		String base64Key1 = StringUtils.newStringUtf8(DESBase64Crypto.getInstance().initkey());
		System.out.println("base64Key："+base64Key1);
		
		System.out.println("随机秘钥："+ HmacHexCrypto.getEncryptKey("011111454",base64Key1));
		//成绩加密密钥：学号、课程号、成绩、学校随机密钥（存二维表实例下的表）
		String keyText = "3000611031-72120170-不及格-"+ HmacHexCrypto.getDecryptKey(HmacHexCrypto.getEncryptKey("011111454",base64Key1),base64Key1) ;
		System.out.println("keyText:"+keyText);
		
		HmacHexCrypto codec1 = HmacHexCrypto.getInstance(Base64.encodeBase64(keyText.getBytes()));
		
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
		
		HmacHexCrypto codec = HmacHexCrypto.getInstance(base64Key);
		//获取HmacMD5消息摘要信息
		System.out.println("HmacMD5算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA256);
		//获取摘要信息
		System.out.println("HmacSHA256算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA1);
		//获取摘要信息
		System.out.println("HmacSHA1算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA384);
		//获取摘要信息
		System.out.println("HmacSHA384算法摘要："+codec.encode(plainText));
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA512);
		//获取摘要信息
		System.out.println("HmacSHA512算法摘要："+ codec.encode(plainText));
		
		System.out.println("================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================");
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_MD2);
		//获取摘要信息
		System.out.println("Bouncycastle HmacMD2算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_MD4);
		//获取摘要信息
		System.out.println("Bouncycastle HmacMD4算法摘要："+ codec.encode(plainText));
		
		//初始化密钥
		codec = HmacHexCrypto.getInstance(base64Key,Algorithm.KEY_HMAC_SHA224);
		//获取摘要信息
		System.out.println("Bouncycastle HmacSHA224算法摘要："+ codec.encode(plainText));
		
		/*base64Key：uoMHT55MO5E=
		学校随机秘钥：5mAnUwZA8QDjP0OEVdp3Kw==
		keyText:3000611031-72120170-不及格-011111454
		================================================================:
		test:DE61367451D1407C58ED95E9B68A40A3
		123:A382304CFE403D89CCA4541042406743
		123456789:8EB1D7D187610A6CC9EEA3D47BEDE525
		sarin:32
		================================================================:
		Hmac的密钥:9F��k*RcQ�0όq7�&��pb�m%�5��2��g3�c'��_��uC�i/��D�d����
		�z�A
		原文：HmacMD5消息摘要
		HmacMD5算法摘要：B8C2DFC7AFE9ABC55ED2C45FB7DE7F3B
		HmacSHA256算法摘要：2834EB747E9BE0B63DB89EA09DEB07F751E4634176C3E1F5E2DCC913BE19CC85
		HmacSHA1算法摘要：86900B93B6B7498751A4959703754600AF197472
		HmacSHA384算法摘要：0E937B9803540FF80F7F80A852B33780426634EED29AF7A56170F7C90954F4D1D28F4F849CF9068E2B242F452EB3429F
		HmacSHA512算法摘要：39364A0E4122828E9D939662AF9016DAE30F4CFF64A368B3EEA9AB47E48F2E569420BC30747FB6154B4A7B10CB19669EB96F91DCDE7B19DE964CDF23B31807EA
		================以下的算法支持是bouncycastle支持的算法，sun java6不支持=======================
		Bouncycastle HmacMD2算法摘要：2758AD209FABC12CD51EAD8431042BB5
		Bouncycastle HmacMD4算法摘要：5A12D7287EC27F7A8B5313241319E9AE
		Bouncycastle HmacSHA224算法摘要：3C293374D37BBC448A328BADC330A89B2CDD58E6F5AE83EB5F8B144C
*/
	}
	
}
