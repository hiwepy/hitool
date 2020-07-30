/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.queue;

@SuppressWarnings("serial")
public class QueueEmptyException extends RuntimeException {
	public QueueEmptyException(String err){
		super(err);
	}
}
