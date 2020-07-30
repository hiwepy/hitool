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
package hitool.core.web.servlet3;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@HandlesTypes({ AsyncMessagePushServlet.class })
public class CustomServletContainerInitializer implements ServletContainerInitializer {
	
  private static final Logger log = LoggerFactory.getLogger(CustomServletContainerInitializer.class);

  private static final String JAR_HELLO_URL = "/jarhello";

  public void onStartup(Set<Class<?>> c, ServletContext servletContext)
      throws ServletException {
    log.info("CustomServletContainerInitializer is loaded here...");
   
    log.info("now ready to add servlet : " + AsyncMessagePushServlet.class.getName());
   /*
    ServletRegistration.Dynamic servlet = servletContext.addServlet(
        AsyncMessagePushServlet.class.getSimpleName(),
        AsyncMessagePushServlet.class);
    servlet.addMapping(JAR_HELLO_URL);*/

    /*log.info("now ready to add filter : " + JarWelcomeFilter.class.getName());
    FilterRegistration.Dynamic filter = servletContext.addFilter(
        JarWelcomeFilter.class.getSimpleName(), JarWelcomeFilter.class);

    EnumSet<DispatcherType> dispatcherTypes = EnumSet
        .allOf(DispatcherType.class);
    dispatcherTypes.add(DispatcherType.REQUEST);
    dispatcherTypes.add(DispatcherType.FORWARD);

    filter.addMappingForUrlPatterns(dispatcherTypes, true, JAR_HELLO_URL);

    log.info("now ready to add listener : " + JarWelcomeListener.class.getName());
    servletContext.addListener(JarWelcomeListener.class);*/
  }
}