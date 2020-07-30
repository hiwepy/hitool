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

import hitool.crypto.algorithm.Base64Crypto;
import hitool.crypto.utils.StringUtils;

public class Base64CryptoTest {


	public static void main(String[] args) throws Exception{
		
		String str2 = "test/xxx.jsp";
		String str = Base64Crypto.getInstance().encode(str2,2);
		System.out.println(str);
		System.out.println(Base64Crypto.getInstance().decode(str,2));
		System.out.println();
		System.out.println(Base64.encodeBase64String(str2.getBytes()));
		System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64(Base64.encodeBase64String(str2.getBytes()))));
	
		System.out.println(Base64Crypto.getInstance().toString(str2));
	}
	
}
