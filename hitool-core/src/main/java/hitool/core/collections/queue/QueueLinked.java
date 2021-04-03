/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.queue;

public class QueueLinked implements Queue{

	private class SLNode{
		public SLNode next;
		public Object data;
		public SLNode(){
		}
		public SLNode(Object data,SLNode next){
			this.next = next;
			this.data = data;
		}
		public SLNode getNext() {
			return next;
		}
		public void setNext(SLNode next){
			this.next = next;
		}
		public Object getData() {
			return data;
		}
	}
	
	private SLNode front;
	private SLNode rear;
	private int size;
	
	public QueueLinked(){
		size = 0;
		front = new SLNode();
		rear = front;
	}
	
	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size==0;
	}

	public Object peek() throws QueueEmptyException {
		if(1>size){
			throw new QueueEmptyException("stack is empty ");
		}
		return front.getNext().getData();
	}

	public Object dequeue() throws QueueEmptyException {
		if(1>size){
			throw new QueueEmptyException("stack is empty ");
		}
		SLNode q  = front.getNext();
		front.setNext(q.getNext());
		size--;
		if(size<1){
			rear = front;
		}
		return q.getData();
	}

	public void enqueue(Object e) {
		SLNode q = new SLNode(e, null);
		rear.setNext(q);
		rear = q;
		size ++;
	}

}
