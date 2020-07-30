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
package hitool.freemarker.template;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import hitool.core.lang3.StringUtils;

public class TemplateMethod implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
			String message = null;
			String code = arguments.get(0).toString();
			if (arguments.size() > 1) {
				Object[] args = arguments.subList(1, arguments.size()).toArray();
				//message = MessageUtil.getText(code, args);
			} else {
				//message = MessageUtil.getText(code);
			}
			return new SimpleScalar(message);
		}
		return null;
	}
	
}
