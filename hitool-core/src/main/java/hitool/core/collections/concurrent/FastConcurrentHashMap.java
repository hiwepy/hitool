/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.collections.concurrent;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class FastConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    /*
     * The default initial capacity for this table,
     * used when not otherwise specified in a constructor.
     */
	protected static final int DEFAULT_INITIAL_CAPACITY = 16;

    /*
     * The default load factor for this table, used when not
     * otherwise specified in a constructor.
     */
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /*
     * The default concurrency level for this table, used when not
     * otherwise specified in a constructor.
     */
    protected static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    
	protected ConcurrentMap<K, V> COMPLIED_MAP = null;
	
	public FastConcurrentHashMap(int initialCapacity,float loadFactor, int concurrencyLevel){
		COMPLIED_MAP = new ConcurrentHashMap<K, V>(initialCapacity,loadFactor,concurrencyLevel);
	}
	
    public FastConcurrentHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_CONCURRENCY_LEVEL);
    }

    public FastConcurrentHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
    }

    public FastConcurrentHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
    }
	
	protected abstract V initialValue(Object key) ;

	@Override
	public void clear() {
		COMPLIED_MAP.clear();
	}
	
	@Override
	public boolean containsKey(Object key) {
		return COMPLIED_MAP.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return COMPLIED_MAP.containsValue(value);
	}

	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return COMPLIED_MAP.entrySet();
	}

	
	@Override
	public V get(Object key) {
		if (key != null ) {
 			V ret = COMPLIED_MAP.get(key);
 			if (ret != null) {
 				return ret;
 			}
 			ret = initialValue(key);
 			V existing = putIfAbsent((K)key, ret);
 			if (existing != null) {
 				ret = existing;
 			}
 			return ret;
 		}
 		return null;
	}

	@Override
	public boolean isEmpty() {
		return COMPLIED_MAP.isEmpty();
	}
	
	@Override
	public Set<K> keySet() {
		return COMPLIED_MAP.keySet();
	}

	@Override
	public V put(K key, V value) {
		return COMPLIED_MAP.put(key, value);
	}

	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		COMPLIED_MAP.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return COMPLIED_MAP.remove(key);
	}

	@Override
	public int size() {
		return COMPLIED_MAP.size();
	}

	@Override
	public Collection<V> values() {
		return COMPLIED_MAP.values();
	}

	@Override
	public V putIfAbsent(K key, V value) {
		return COMPLIED_MAP.putIfAbsent(key, null);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return COMPLIED_MAP.remove(key, value);
	}
	
	@Override
	public V replace(K key, V value) {
		return COMPLIED_MAP.replace(key, value);
	}
	
	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		return COMPLIED_MAP.replace(key, oldValue, newValue);
	}
	
}
