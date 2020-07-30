/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.queue;

public interface Queue {
	//返回队列的大小
	public int getSize();
	//判断队列是否为空
	public boolean isEmpty();
	//元素e 入对
	public void enqueue(Object e);
	//队首元素出栈
	public Object dequeue() throws QueueEmptyException;
	//取队首元素
	public Object peek()  throws QueueEmptyException;
	
}
