/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.stack;

@SuppressWarnings("serial")
public class StackEmptyException extends RuntimeException {
	public StackEmptyException(String err){
		super(err);
	}
}
