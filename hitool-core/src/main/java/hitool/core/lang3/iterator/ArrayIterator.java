/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
@SuppressWarnings("unchecked")
public class ArrayIterator<T> implements Iterator<T> {
    
    private T[] array;
    private int i;

    public ArrayIterator(T[] array) {
        this.array = array;
        i = 0;
    }

    public boolean hasNext() {
        return array != null ? i < array.length : false;
    }

    public T next() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return array[i++];
    }

	public void remove(T obj) {
    	List<T> temp = arrayToList(array);
    	temp.remove(obj);
    	this.array = ((T[]) temp.toArray());
    }
    
    public void remove(int index) {
    	List<T> temp = arrayToList(array);
    	temp.remove(index);
    	this.array = (T[]) temp.toArray();
    }
    
    public void remove() {
        throw new UnsupportedOperationException(this.getClass().getName() + " doesn't support remove");
    }
    
    private List<T> arrayToList(T[] array){
    	List<T> temp = new ArrayList<T>(array.length);
    	for (int i = 0; i < array.length; i++) {
    		temp.add(array[i]);
		}
    	return temp;
    }
}