/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.queue;

public class QueueArray implements Queue{
	
	private final static int CAP = 8;//队列默认大小
	private Object[] elements;//数据元素数组
	private int capacity;//数组的大小elements.length
	private int front;//队首指针指向队首
	private int rear;//队尾指针指向队列最后一个
	
	public QueueArray(){
		this(CAP);
	}
	
	public QueueArray(int cap){
		capacity = cap+1;
		elements = new Object[capacity];
		front = rear = 0;
	}

	public int getSize() {
		return (rear-front+capacity)%capacity;
	}

	public boolean isEmpty() {
		return rear==front;
	}

	public Object peek() throws QueueEmptyException {
		if(isEmpty()){
			throw new QueueEmptyException("Queue is empty ");
		}
		return elements[front];
	}

	public Object dequeue() throws QueueEmptyException {
		if(isEmpty()){
			throw new QueueEmptyException("Queue is empty ");
		}
		Object obj = elements[front];
		elements[front] = null;
		front = (front+1)%capacity;
		return obj;
	}

	public void enqueue(Object e) {
		if(getSize()==(capacity-1)){
			expandSpace();
		}
		elements[rear] = e;
		rear = (rear+1)%capacity;
	}
	
	private void expandSpace(){
		Object[] a = new Object[elements.length*2];
		int i=front,j=0;
		while (i!=rear) {
			a[j++] = elements[i];
			i=(rear+1)%capacity;
		}
		elements = a;
		capacity = elements.length;
		front = 0;
		rear = j;
	}
	

}
