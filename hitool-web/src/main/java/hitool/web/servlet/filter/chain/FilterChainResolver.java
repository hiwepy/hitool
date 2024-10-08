/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package hitool.web.servlet.filter.chain;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/*
 * 定义如何获取对应请求的过滤器链
 */
public interface FilterChainResolver {

	/*
	 * 获取FilterChian
	 * @param request
	 * @param response
	 * @param originalChain
	 * @return
	 */
	FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain);
	
}
