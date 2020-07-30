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
package hitool.core.resources.property;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.ResourceUtils;
import hitool.core.resources.PropertiesLoaderUtils;
import hitool.core.resources.property.holder.PropertiesHolder;


public class ReloadablePropertySource {

	protected static Logger LOG = LoggerFactory.getLogger(ReloadablePropertySource.class);

	protected static final String MERGED_FILENAME = "Merged";
	
	protected static final String PROPERTIES_SUFFIX = ".properties";

	protected static final String XML_SUFFIX = ".xml";
	
	protected String defaultEncoding;
	
	protected long cacheMillis = -1;
	
	protected boolean concurrentRefresh = true;

	/** Cache to hold already loaded properties per filename */
	protected final ConcurrentMap<String, PropertiesHolder> cachedProperties = new ConcurrentHashMap<String, PropertiesHolder>();
	
	/** Cache to hold merged loaded properties per filename */
	protected final ConcurrentMap<String, PropertiesHolder> cachedMergedProperties = new ConcurrentHashMap<String, PropertiesHolder>();

	protected final ConcurrentMap<String, Long> cachedModifyTimes = new ConcurrentHashMap<String, Long>();

	
	/**
	 * Get a PropertiesHolder for the given filename, either from the
	 * cache or freshly loaded.
	 * @param filename the bundle filename (basename + Locale)
	 * @return the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder getProperties(String filename) {
		PropertiesHolder propHolder = this.cachedProperties.get(filename);
		long originalTimestamp = -2;
	
		if (propHolder != null) {
			originalTimestamp = propHolder.getRefreshTimestamp();
			if (originalTimestamp == -1 || originalTimestamp > System.currentTimeMillis() - this.cacheMillis) {
				// Up to date
				return propHolder;
			}
		}
		else {
			propHolder = new PropertiesHolder();
			PropertiesHolder existingHolder = this.cachedProperties.putIfAbsent(filename, propHolder);
			if (existingHolder != null) {
				propHolder = existingHolder;
			}
		}
		cachedModifyTimes.put(filename, originalTimestamp);
		// At this point, we need to refresh...
		if (this.concurrentRefresh && propHolder.getRefreshTimestamp() >= 0) {
			// A populated but stale holder -> could keep using it.
			if (!propHolder.refreshLock.tryLock()) {
				// Getting refreshed by another thread already ->
				// let's return the existing properties for the time being.
				return propHolder;
			}
		}
		else {
			propHolder.refreshLock.lock();
		}
		try {
			PropertiesHolder existingHolder = this.cachedProperties.get(filename);
			if (existingHolder != null && existingHolder.getRefreshTimestamp() > originalTimestamp) {
				return existingHolder;
			}
			return refreshProperties(filename, propHolder);
		}
		finally {
			propHolder.refreshLock.unlock();
		}
	}
	
	/**
	 * Refresh the PropertiesHolder for the given bundle filename.
	 * The holder can be {@code null} if not cached before, or a timed-out cache entry
	 * (potentially getting re-validated against the current last-modified timestamp).
	 * @param filename the bundle filename (basename + Locale)
	 * @param propHolder the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
		long refreshTimestamp = (this.cacheMillis < 0 ? -1 : System.currentTimeMillis());
		String resourceName = filename + PROPERTIES_SUFFIX;
		URL resource = ResourceUtils.getResourceAsURL(resourceName ,this.getClass());
		if (resource == null) {
			resourceName = filename + XML_SUFFIX;
			resource = ResourceUtils.getResourceAsURL(resourceName ,this.getClass());
		}
		if (resource != null) {
			long fileTimestamp = -1;
			if (this.cacheMillis >= 0) {
				// Last-modified timestamp of file will just be read if caching with timeout.
				try {
					fileTimestamp = cachedModifyTimes.get(filename);
					if (propHolder != null && propHolder.getFileTimestamp() == fileTimestamp) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
						}
						propHolder.setRefreshTimestamp(refreshTimestamp);
						return propHolder;
					}
				}
				catch (Exception ex) {
					// Probably a class path resource: cache it forever.
					if (LOG.isDebugEnabled()) {
						LOG.debug(resource + " could not be resolved in the file system - assuming that it hasn't changed", ex);
					}
					fileTimestamp = -1;
				}
			}
			try {
				Properties props = new Properties();
				PropertiesLoaderUtils.fillProperties(props, resourceName, defaultEncoding);
				propHolder = new PropertiesHolder(props, fileTimestamp);
			}
			catch (IOException ex) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Could not parse properties file [" + resource.getFile() + "]", ex);
				}
				// Empty holder representing "not valid".
				propHolder = new PropertiesHolder();
			}
		}
	
		else {
			// Resource does not exist.
			if (LOG.isDebugEnabled()) {
				LOG.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
			}
			// Empty holder representing "not found".
			propHolder = new PropertiesHolder();
		}
	
		propHolder.setRefreshTimestamp(refreshTimestamp);
		this.cachedProperties.put(filename, propHolder);
		return propHolder;
	}
	
	/**
	 * Set the number of seconds to cache loaded properties files.
	 * <ul>
	 * <li>Default is "-1", indicating to cache forever (just like
	 * {@code java.util.ResourceBundle}).
	 * <li>A positive number will cache loaded properties files for the given
	 * number of seconds. This is essentially the interval between refresh checks.
	 * Note that a refresh attempt will first check the last-modified timestamp
	 * of the file before actually reloading it; so if files don't change, this
	 * interval can be set rather low, as refresh attempts will not actually reload.
	 * <li>A value of "0" will check the last-modified timestamp of the file on
	 * every message access. <b>Do not use this in a production environment!</b>
	 * </ul>
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheMillis = (cacheSeconds * 1000);
	}
	
	/**
	 * Set the default charset to use for parsing resource bundle files.
	 * <p>Default is none, using the {@code java.util.ResourceBundle}
	 * default encoding: ISO-8859-1.
	 * @since 3.1.3
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

}
