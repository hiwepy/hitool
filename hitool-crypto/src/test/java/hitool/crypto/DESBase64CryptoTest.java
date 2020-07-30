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

import hitool.crypto.algorithm.DESBase64Crypto;
import hitool.crypto.utils.StringUtils;

public class DESBase64CryptoTest {
	
public static void main(String[] args) throws Exception {
		
		String str="DES";
		
		String encryptKey = StringUtils.newStringUtf8(DESBase64Crypto.getInstance().initkey());
		
		System.out.println("原文："+str); 
		System.out.println("密钥："+ encryptKey);
		//加密数据
		String encryptedText = DESBase64Crypto.getInstance().encode(str, encryptKey);
		System.out.println("加密后："+encryptedText);
		//解密数据
		System.out.println("解密后："+ DESBase64Crypto.getInstance().decode(encryptedText, encryptKey));

		/*<property name="jdbcUrl" value=></property> 
        <property name="user" value=></property>
        <property name="password" value=></property>
        */
		String encryptKeyText = "7EV/Zzutjzg=";
		
		//"jdbc:oracle:thin:@10.71.32.37:1521:DevDB";  / szzyjwa
		String jdbcUrlText = "jdbc:oracle:thin:@10.71.32.37:1521:DevDB";
		String userText = "admin";
		String passwordText = "admin";
		
		String jdbcUrl = DESBase64Crypto.getInstance().encode(jdbcUrlText,encryptKeyText);
		String user = DESBase64Crypto.getInstance().encode(userText,encryptKeyText);
		String password =  DESBase64Crypto.getInstance().encode(passwordText,encryptKeyText);
		
		System.out.println("jdbcUrlText加密后：" + jdbcUrl);
		System.out.println("userText加密后：" + user);
		System.out.println("passwordText加密后：" +password);
		
    	System.out.println("jdbcUrl解密后：" + DESBase64Crypto.getInstance().decode(jdbcUrl,encryptKeyText));
    	System.out.println("user解密后：" + DESBase64Crypto.getInstance().decode(user,encryptKeyText));
    	System.out.println("password解密后：" + DESBase64Crypto.getInstance().decode(password,encryptKeyText));
    	
    }

}
