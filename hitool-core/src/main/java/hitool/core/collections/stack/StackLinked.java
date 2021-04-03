/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.stack;

public class StackLinked implements Stack {

	private class SLNode{
		public SLNode next;
		public Object data;
		public SLNode(Object data,SLNode next){
			this.next = next;
			this.data = data;
		}
		public SLNode getNext() {
			return next;
		}
		public Object getData() {
			return data;
		}
		
	}
	
	private SLNode top;
	private int size;//栈大小
	
	public StackLinked(){
		size = 0;
		top = null;
	}
	
	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size==0;
	}

	public Object peek() throws StackEmptyException {
		if(1>size){
			throw new StackEmptyException("stack is empty ");
		}
		return top.getData();
	}

	public Object pop() throws StackEmptyException {
		if(1>size){
			throw new StackEmptyException("stack is empty ");
		}
		Object obj = top.getData();
		top = top.getNext();
		size--;
		return obj;
	}

	public void push(Object e) {
		SLNode q = new SLNode(e, top);
		top = q;
		size ++;
	}

}
