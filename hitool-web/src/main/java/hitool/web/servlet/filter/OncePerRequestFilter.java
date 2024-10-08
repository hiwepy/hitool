package hitool.web.servlet.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/*
 *  增加过滤标记，实现一次请求仅会过滤一次
 */
public abstract class OncePerRequestFilter extends AbstractNameableFilter {
	
	/*
	 * Suffix that gets appended to the filter name for the "already filtered" request attribute.
	 * @see #getAlreadyFilteredAttributeName
	 */
	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

	protected boolean enabled = true;
	
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		
	}
	
	/*
	 * This <code>doFilter</code> implementation stores a request attribute for
	 * "already filtered", proceeding without filtering again if the attribute
	 * is already there.
	 * 
	 * @see #getAlreadyFilteredAttributeName
	 * @see #isEnabled
	 * @see #doFilterInternal
	 */
	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
		if (request.getAttribute(alreadyFilteredAttributeName) != null) {
			// Proceed without invoking this filter...
			LOG.trace("Filter '{}' already executed.  Proceeding without invoking this filter.", getName());
			filterChain.doFilter(request, response);
		 } else if (!isEnabled(request, response)) {
			LOG.debug("Filter '{}' should not enabled for the current request.  Proceeding without invoking this filter.", getName());
            filterChain.doFilter(request, response);
		} else {
			LOG.trace("Filter '{}' not yet executed.  Executing now.", getName());
			// Do invoke this filter...
			request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
			try {
				doFilterInternal(request , response, filterChain);
			} finally {
				// Remove the "already filtered" request attribute for this
				// request.
				request.removeAttribute(alreadyFilteredAttributeName);
			}
		}
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}
	
	/*
	 * Return the name of the request attribute that identifies that a request
	 * is already filtered.
	 * <p>
	 * Default implementation takes the configured name of the concrete filter
	 * instance and appends ".FILTERED". If the filter is not fully initialized,
	 * it falls back to its class name.
	 * 
	 * @see #getFilterName
	 * @see #ALREADY_FILTERED_SUFFIX
	 */
	protected String getAlreadyFilteredAttributeName() {
		String name = getName();
        if (name == null) {
            name = getClass().getName();
        }
		return name + ALREADY_FILTERED_SUFFIX;
	}

	protected boolean isEnabled(ServletRequest request, ServletResponse response){
		return isEnabled();
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/*
     * Same contract as for
     * {@link #doFilter(jakarta.servlet.ServletRequest, jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)},
     * but guaranteed to be invoked only once per request.
     *
     * @param request  incoming {@code ServletRequest}
     * @param response outgoing {@code ServletResponse}
     * @param chain    the {@code FilterChain} to execute
     * @throws ServletException if there is a problem processing the request
     * @throws IOException      if there is an I/O problem processing the request
     */
	protected abstract void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException;
	
	
}