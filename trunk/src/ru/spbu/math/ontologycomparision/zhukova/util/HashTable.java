package ru.spbu.math.ontologycomparision.zhukova.util;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class HashTable<K, V> extends HashMap<K, Set<V>> implements IHashTable<K,V> {

    public HashTable() {};

    public HashTable(Map<K, Set<V>> map) {
        super(map);
    }

    public boolean has(Object value) {
        for (Iterable<V> valueCollection : this.values()) {
            for (V aValue : valueCollection) {
                if (aValue.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void insert(K key, V value) {
        Set<V> oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = new HashSet<V>();
            super.put(key, oldValue);
        }
        oldValue.add(value);
    }

    public void insertAll(K key, Collection<V> values) {
        Set<V> oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = new HashSet<V>();
            super.put(key, oldValue);
        }
        oldValue.addAll(values);
    }

    public void insertAll(IHashTable<K, V> table) {
        for (Map.Entry<K, Set<V>> entry : table.entrySet()) {
            this.insertAll(entry.getKey(), entry.getValue());
        }
    }

    public void insertAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.insert(entry.getKey(), entry.getValue());
        }
    }

    public boolean deleteValue(K key, V value) {
        Set<V> valueList = get(key);
        if (valueList == null) {
            return false;
        }
        boolean  result = valueList.remove(value);
        if (valueList.size() == 0) {
            super.remove(key);
        }
        return result;
    }

    public Collection<V> allValues() {
        Collection<V> result = new ArrayList<V>();
        for (Set<V> valueList : this.values()) {
            result.addAll(valueList);
        }
        return result;
    }
}
