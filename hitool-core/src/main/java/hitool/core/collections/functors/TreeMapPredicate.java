/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.functors;

import java.util.Map;

import org.apache.commons.collections.Predicate;

@SuppressWarnings("rawtypes")
public class TreeMapPredicate implements Predicate {

	protected String key;
	protected String parent;

	public TreeMapPredicate(String key,String parent){
		this.key = key;
		this.parent = parent;
	}
	
	@Override
	public boolean evaluate(Object object) {
		Map navMenu = (Map)object;
		return parent.equalsIgnoreCase(String.valueOf(navMenu.get(key)));
	}

}
