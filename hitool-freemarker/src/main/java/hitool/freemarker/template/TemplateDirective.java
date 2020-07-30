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

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TemplateDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		TemplateModel paramValue = (TemplateModel) params.get("gnmkdm");
		String gnmkdm = ((SimpleScalar)paramValue).getAsString();
		
		/*User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		List<AncdModel> ancdList = ancdService.cxButtons(user, gnmkdm);
		*/
		TemplateModel template = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build().wrap(null);
		
		env.setVariable("ancdList",template);
		
		if (body != null) {  
            body.render(env.getOut());  
        } 
	}

}
