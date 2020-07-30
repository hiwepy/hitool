/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.bundle;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

public class ResourceBundleEnumeration implements Enumeration<String> {

	private Iterator<String> ite;
	
	public ResourceBundleEnumeration(ResourceBundle ...bundles){
		this(null, bundles);
	}
	
	public ResourceBundleEnumeration(ResourceBundle parent,ResourceBundle ...bundles) {
		Set<String> keys = new HashSet<String>();
		if(parent != null){
			keys.addAll(parent.keySet());
		}
		for (ResourceBundle bundle : bundles) {
			if(bundle == null){
				continue;
			}
			keys.addAll(bundle.keySet());
		}
		this.ite = keys.iterator();
	}

	@Override
	public boolean hasMoreElements() {
		return ite.hasNext();
	}

	@Override
	public String nextElement() {
		return ite.next();
	}

}