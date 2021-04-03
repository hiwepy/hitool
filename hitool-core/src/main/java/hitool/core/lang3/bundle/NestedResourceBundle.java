/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.bundle;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NestedResourceBundle extends ResourceBundle {

	protected ResourceBundle[] bundles;
	protected static Logger LOG = LoggerFactory.getLogger(NestedResourceBundle.class);
	
	public NestedResourceBundle() {
    }
	
	public NestedResourceBundle(ResourceBundle ...bundles){
		this.bundles = bundles;
	}
	
	@Override
	protected Object handleGetObject(String key) {
		if (key == null) {
            throw new NullPointerException("key is null ");
        }
		for (ResourceBundle bundle : bundles) {
			try {
				Object value = bundle.getObject(key);
				if(value != null){
					return value;
				}
			} catch (Exception e) {
				// ingrone e
				LOG.warn(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public Enumeration<String> getKeys() {
        return new ResourceBundleEnumeration( this.parent, this.bundles);
	}
	
};
