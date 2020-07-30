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
package hitool.core.web.servlet.filter;


public abstract class AbstractNameableFilter extends AbstractFilter implements Nameable{

	/**
	 * 过滤器名称
	 */
	protected String name;

	protected String getName() {
		if (this.name == null) {
			this.name = this.getFilterName();
		}
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
