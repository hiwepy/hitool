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

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.regexp.PathMatcher;
import hitool.core.regexp.matcher.AntPathMatcher;
import hitool.web.WebUtils;

public abstract class AbstractPathMatchFilter extends AbstractAdviceFilter implements PathProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPathMatchFilter.class);

	protected PathMatcher pathMatcher = new AntPathMatcher();

	// 需要过滤的路径
	protected List<String> appliedPaths = new ArrayList<String>();

	@Override
	public Filter processPath(String path) {
		this.appliedPaths.add(path);
		return this;
	}

	protected String getPathWithinApplication(ServletRequest request) {
		return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
	}

	protected boolean pathsMatch(String path, ServletRequest request) {
		String requestURI = getPathWithinApplication(request);
		LOG.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
		return pathsMatch(path, requestURI);
	}

	protected boolean pathsMatch(String pattern, String path) {
		return pathMatcher.match(pattern, path);
	}

	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

		if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("appliedPaths property is null or empty.  This Filter will passthrough immediately.");
			}
			return true;
		}

		for (String path : this.appliedPaths) {
			// If the path does match, then pass on to the subclass
			// implementation for specific checks
			// (first match 'wins'):
			if (pathsMatch(path, request)) {
				LOG.trace("Current requestURI matches pattern '{}'.  Determining filter chain execution...", path);
				return isFilterChainContinued(request, response, path);
			}
		}

		// no path matched, allow the request to go through:
		return true;
	}

	private boolean isFilterChainContinued(ServletRequest request, ServletResponse response, String path) throws Exception {

		if (isEnabled(request, response, path)) { // isEnabled check

			if (LOG.isTraceEnabled()) {
				LOG.trace("Filter '{}' is enabled for the current request under path '{}'.  "
								+ "Delegating to subclass implementation for 'onPreHandle' check.",
						new Object[] { getName(), path });
			}
			// The filter is enabled for this specific request, so delegate to
			// subclass implementations
			// so they can decide if the request should continue through the
			// chain or not:
			return onPreHandle(request, response);
		}

		if (LOG.isTraceEnabled()) {
			LOG.trace("Filter '{}' is disabled for the current request under path '{}'.  "
							+ "The next element in the FilterChain will be called immediately.",
					new Object[] { getName(), path });
		}
		// This filter is disabled for this specific request,
		// return 'true' immediately to indicate that the filter will not
		// process the request
		// and let the request/response to continue through the filter chain:
		return true;
	}

	protected boolean onPreHandle(ServletRequest request, ServletResponse response) throws Exception {
		return true;
	}

	protected boolean isEnabled(ServletRequest request, ServletResponse response, String path)
			throws Exception {
		return isEnabled(request, response);
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public List<String> getAppliedPaths() {
		return appliedPaths;
	}
}
