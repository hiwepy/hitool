/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.stack;

public class StackArray implements Stack{
	
	private final int LEN = 8;//数组默认大小
	private Object[] elements;//数据元素数组
	private int top;//栈顶指针
	public StackArray(){
		elements = new Object[LEN];
		top = -1;
	}
	
	public StackArray(int size){
		top = -1;
		if(size>LEN)
			elements = new Object[size];
		else
			elements = new Object[LEN];
	}

	public int getSize() {
		return top+1;
	}

	public boolean isEmpty() {
		return top<0;
	}

	public Object peek() throws StackEmptyException {
		if(getSize()<1){
			throw new StackEmptyException("stack is empty ");
		}
		return elements[top];
	}

	public Object pop() throws StackEmptyException {
		if(getSize()<1){
			throw new StackEmptyException("stack is empty ");
		}
		Object obj = elements[top];
		elements[--top] = null;
		return obj;
	}

	public void push(Object e) {
		if(getSize()>elements.length){
			expandSpace();
		}
		elements[++top] = e;
	}
	
	private void expandSpace(){
		Object[] a = new Object[elements.length*2];
		for (int i = 0; i < elements.length; i++) {
			a[i] = elements[i];
		}
		elements = a;
	}
	

}
