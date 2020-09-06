package hitool.web.servlet.filter.chain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;

import hitool.core.lang3.StringUtils;
import hitool.web.servlet.filter.NamedFilterList;
import hitool.web.servlet.filter.chain.ProxiedFilterChain;

public class DefaultNamedFilterList implements NamedFilterList {

	private String name;
	
	private List<Filter> backingList;
	
	public DefaultNamedFilterList(String name) {
		 this(name, new ArrayList<Filter>());
	}

	public DefaultNamedFilterList(String name, List<Filter> backingList) {
		 if (backingList == null) {
	            throw new NullPointerException("backingList constructor argument cannot be null.");
        }
        this.backingList = backingList;
        setName(name);
	}

	public void setName(String name) {
		 if (StringUtils.isBlank(name)) {
	         throw new IllegalArgumentException("Cannot specify a null or empty name.");
        }
        this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FilterChain proxy(FilterChain filterChain) {
		return new ProxiedFilterChain(filterChain, this);
	}
	
	@Override
	public int size() {
		return this.backingList.size();
	}

	@Override
	public boolean isEmpty() {
		return this.backingList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.backingList.contains(o);
	}

	@Override
	public Iterator<Filter> iterator() {
		return this.backingList.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.backingList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.backingList.toArray(a);
	}

	@Override
	public boolean add(Filter e) {
		return this.backingList.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.backingList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.backingList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Filter> c) {
		return this.backingList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Filter> c) {
		return this.backingList.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.backingList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.backingList.retainAll(c);
	}

	@Override
	public void clear() {
		this.backingList.clear();
	}

	@Override
	public Filter get(int index) {
		return this.backingList.get(index);
	}

	@Override
	public Filter set(int index, Filter element) {
		return this.backingList.set(index, element);
	}

	@Override
	public void add(int index, Filter element) {
		this.backingList.add(index, element);
	}

	@Override
	public Filter remove(int index) {
		return this.backingList.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.backingList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.backingList.lastIndexOf(o);
	}

	@Override
	public ListIterator<Filter> listIterator() {
		return this.backingList.listIterator();
	}

	@Override
	public ListIterator<Filter> listIterator(int index) {
		return this.backingList.listIterator(index);
	}

	@Override
	public List<Filter> subList(int fromIndex, int toIndex) {
		return this.backingList.subList(fromIndex, toIndex);
	}

}
