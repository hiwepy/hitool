package hitool.web.servlet.filter.chain.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.collections.CollectionUtils;
import hitool.core.lang3.StringUtils;
import hitool.web.exception.FilterException;
import hitool.web.servlet.filter.Nameable;
import hitool.web.servlet.filter.NamedFilterList;
import hitool.web.servlet.filter.PathProcessor;
import hitool.web.servlet.filter.chain.FilterChainManager;

/**
 * 
 */
public class DefaultFilterChainManager implements FilterChainManager {
	
	private static transient final Logger log = LoggerFactory.getLogger(DefaultFilterChainManager.class);

    private FilterConfig filterConfig;

    private Map<String, Filter> filters; 

    private Map<String, NamedFilterList> filterChains;

    private final static String DEFAULT_CHAIN_DEFINATION_DELIMITER_CHAR = ",";
    
    public DefaultFilterChainManager() {
        this.filters = new LinkedHashMap<String, Filter>();
        this.filterChains = new LinkedHashMap<String, NamedFilterList>();
    }

    public DefaultFilterChainManager(FilterConfig filterConfig) {
        this.filters = new LinkedHashMap<String, Filter>();
        this.filterChains = new LinkedHashMap<String, NamedFilterList>();
        setFilterConfig(filterConfig);
    }

    /**
     * @return the {@code FilterConfig} provided by the Servlet container at webapp startup.
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Sets the {@code FilterConfig} provided by the Servlet container at webapp startup.
     *
     * @param filterConfig the {@code FilterConfig} provided by the Servlet container at webapp startup.
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Filter> filters) {
        this.filters = filters;
    }

    public Map<String, NamedFilterList> getFilterChains() {
        return filterChains;
    }
    
    public void setFilterChains(Map<String, NamedFilterList> filterChains) {
        this.filterChains = filterChains;
    }

    public Filter getFilter(String name) {
        return this.filters.get(name);
    }

    public void addFilter(String name, Filter filter) {
        addFilter(name, filter, false);
    }

    public void addFilter(String name, Filter filter, boolean init) {
        addFilter(name, filter, init, true);
    }

    public void createChain(String chainName, String chainDefinition) {
        if (StringUtils.isBlank(chainName)) {
            throw new NullPointerException("chainName cannot be null or empty.");
        }
        if (StringUtils.isBlank(chainDefinition)) {
            throw new NullPointerException("chainDefinition cannot be null or empty.");
        }
        if (log.isDebugEnabled()) {
            log.debug("Creating chain [" + chainName + "] from String definition [" + chainDefinition + "]");
        }
        String[] filterTokens = splitChainDefinition(chainDefinition);
        for (String token : filterTokens) {
            addToChain(chainName, token);
        }
    }

    /**
     * Splits the comma-delimited filter chain definition line into individual filter definition tokens.
     */
    protected String[] splitChainDefinition(String chainDefinition) {
    	String trimToNull = StringUtils.trimToNull(chainDefinition);
    	if(trimToNull == null){
    		return null;
    	}
    	String[] split = StringUtils.splits(trimToNull, DEFAULT_CHAIN_DEFINATION_DELIMITER_CHAR);
    	for (int i = 0; i < split.length; i++) {
    		split[i] = StringUtils.trimToNull(split[i]);
		}
        return split;
    }

    public static void main(String[] args) {
		
	}
    protected void addFilter(String name, Filter filter, boolean init, boolean overwrite) {
        Filter existing = getFilter(name);
        if (existing == null || overwrite) {
            if (filter instanceof Nameable) {
                ((Nameable) filter).setName(name);
            }
            if (init) {
                initFilter(filter);
            }
            this.filters.put(name, filter);
        }
    }

    public void addToChain(String chainName, String filterName) {
        if (StringUtils.isBlank(chainName)) {
            throw new IllegalArgumentException("chainName cannot be null or empty.");
        }
        Filter filter = getFilter(filterName);
        if (filter == null) {
            throw new IllegalArgumentException("There is no filter with name '" + filterName +
                    "' to apply to chain [" + chainName + "] in the pool of available Filters.  Ensure a " +
                    "filter with that name/path has first been registered with the addFilter method(s).");
        }
        applyChainConfig(chainName, filter);
        NamedFilterList chain = ensureChain(chainName);
        chain.add(filter);
    }


    protected void applyChainConfig(String chainName, Filter filter) {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to apply path [" + chainName + "] to filter [" + filter + "] ");
        }
        if (filter instanceof PathProcessor) {
            ((PathProcessor) filter).processPath(chainName);
        } else {
            String msg = "the underlying " + "Filter instance is not an 'instanceof' " +
                    PathProcessor.class.getName() + ".  This is required if the filter is to accept " +
                    "chain-specific configuration.";
            throw new FilterException(msg);
        }
    }
    
    protected NamedFilterList ensureChain(String chainName) {
        NamedFilterList chain = getChain(chainName);
        if (chain == null) {
            chain = new DefaultNamedFilterList(chainName);
            this.filterChains.put(chainName, chain);
        }
        return chain;
    }

    public NamedFilterList getChain(String chainName) {
        return this.filterChains.get(chainName);
    }

    public boolean hasChains() {
        return !CollectionUtils.isEmpty(this.filterChains);
    }

    @SuppressWarnings("unchecked")
	public Set<String> getChainNames() {
        return this.filterChains != null ? this.filterChains.keySet() : Collections.EMPTY_SET;
    }

    public FilterChain proxy(FilterChain original, String chainName) {
        NamedFilterList configured = getChain(chainName);
        if (configured == null) {
            String msg = "There is no configured chain under the name/key [" + chainName + "].";
            throw new IllegalArgumentException(msg);
        }
        return configured.proxy(original);
    }

    /**
     * Initializes the filter by calling <code>filter.init( {@link #getFilterConfig() getFilterConfig()} );</code>.
     *
     * @param filter the filter to initialize with the {@code FilterConfig}.
     */
    protected void initFilter(Filter filter) {
        FilterConfig filterConfig = getFilterConfig();
        if (filterConfig == null) {
            throw new IllegalStateException("FilterConfig attribute has not been set.  This must occur before filter " +
                    "initialization can occur.");
        }
        try {
            filter.init(filterConfig);
        } catch (ServletException e) {
            throw new FilterException(e);
        }
    }

}
