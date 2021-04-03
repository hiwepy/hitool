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

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.regexp.PathMatcher;
import hitool.core.regexp.matcher.AntPathMatcher;
import hitool.web.WebUtils;
import hitool.web.servlet.filter.chain.impl.DefaultFilterChainManager;

public class PathMatchingFilterChainResolver implements FilterChainResolver {

	private static final Logger LOG = LoggerFactory.getLogger(PathMatchingFilterChainResolver.class);
	
	/*
	 * filterChain管理器
	 */
	private FilterChainManager filterChainManager;
	
	/*
	 * 路径匹配器
	 */
	private PathMatcher pathMatcher;
	
	 public PathMatchingFilterChainResolver() {
        this.pathMatcher = new AntPathMatcher();
        this.filterChainManager = new DefaultFilterChainManager();
    }

    public PathMatchingFilterChainResolver(FilterConfig filterConfig) {
        this.pathMatcher = new AntPathMatcher();
        this.filterChainManager = new DefaultFilterChainManager(filterConfig);
    }

	public FilterChainManager getFilterChainManager() {
		return filterChainManager;
	}

	public void setFilterChainManager(FilterChainManager filterChainManager) {
		this.filterChainManager = filterChainManager;
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}
	
	
	public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);
        for (String pathPattern : filterChainManager.getChainNames()) {
            if (pathMatches(pathPattern, requestURI)) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }
        return null;
    }

    protected boolean pathMatches(String pattern, String path) {
        PathMatcher pathMatcher = getPathMatcher();
        return pathMatcher.match(pattern, path);
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }
	
}
