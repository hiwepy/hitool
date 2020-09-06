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
package hitool.web.servlet.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFilter implements Filter {
	
	protected final Logger LOG = LoggerFactory.getLogger(AbstractFilter.class);
	
	protected FilterConfig filterConfig = null;
	protected ServletContext sc;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化参数取值对象
		this.setFilterConfig(filterConfig);
		this.setServletContext(filterConfig.getServletContext());
		try {
            onFilterConfigSet(filterConfig);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException) e;
            } else {
                if (LOG.isErrorEnabled()) {
                	LOG.error("Unable to start Filter: [" + e.getMessage() + "].", e);
                }
                throw new ServletException(e);
            }
        }
	}
	
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
    }
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * Make the name of this filter available to subclasses.
	 * Analogous to GenericServlet's <code>getServletName()</code>.
	 * <p>Takes the FilterConfig's filter name by default.
	 * If initialized as bean in a Spring application context,
	 * it falls back to the bean name as defined in the bean factory.
	 * @return the filter name, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.FilterConfig#getFilterName()
	 * @see #setBeanName
	 */
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName() : this.getClass().getSimpleName());
	}
	
	public final void setServletContext(ServletContext servletContext) {
		this.sc = servletContext;
	}
	
	public final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig.getServletContext() : this.sc);
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
	

}
