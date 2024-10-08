package hitool.web.servlet.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.web.servlet.filter.chain.FilterChainResolver;

public abstract class AbstractRouteableFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractRouteableFilter.class);
	
	/*
	 * 用来判定使用那个FilterChian
	 */
	protected FilterChainResolver filterChainResolver;
	
	public AbstractRouteableFilter() {
		super();
	}

	public AbstractRouteableFilter(FilterChainResolver filterChainResolver) {
		super();
		this.filterChainResolver = filterChainResolver;
	}

	protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, final FilterChain chain) throws ServletException, IOException {
        Throwable t = null;
        try {
             executeChain(servletRequest, servletResponse, chain);
        }catch (Throwable throwable) {
            t = throwable;
        }
        if (t != null) {
            if (t instanceof ServletException) {
                throw (ServletException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            String msg = "Filtered request failed.";
            throw new ServletException(msg, t);
        }
    }

    protected FilterChain getExecutionChain(ServletRequest request, ServletResponse response, FilterChain origChain) {
        FilterChain chain = origChain;

        FilterChainResolver resolver = getFilterChainResolver();
        if (resolver == null) {
            LOG.debug("No FilterChainResolver configured.  Returning original FilterChain.");
            return origChain;
        }

        FilterChain resolved = resolver.getChain(request, response, origChain);
        if (resolved != null) {
            LOG.trace("Resolved a configured FilterChain for the current request.");
            chain = resolved;
        } else {
            LOG.trace("No FilterChain configured for the current request.  Using the default.");
        }

        return chain;
    }


    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain)
            throws IOException, ServletException {
        FilterChain chain = getExecutionChain(request, response, origChain);
        chain.doFilter(request, response);
    }


	public FilterChainResolver getFilterChainResolver() {
		return filterChainResolver;
	}


	public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
		this.filterChainResolver = filterChainResolver;
	}

}
