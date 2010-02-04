package ru.spbu.math.ontologycomparision.zhukova.util;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class HashTable<K, V> extends HashMap<K, List<V>> implements IHashTable<K,V> {

    public HashTable() {};

    public HashTable(Map<K, List<V>> map) {
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
        List<V> oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = new LinkedList<V>();
            super.put(key, oldValue);
        }
        oldValue.add(value);
    }

    public void insertAll(K key, Collection<V> values) {
        List<V> oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = new LinkedList<V>();
            super.put(key, oldValue);
        }
        oldValue.addAll(values);
    }

    public void insertAll(IHashTable<K, V> table) {
        for (Map.Entry<K, List<V>> entry : table.entrySet()) {
            this.insertAll(entry.getKey(), entry.getValue());
        }
    }

    public void insertAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.insert(entry.getKey(), entry.getValue());
        }
    }
}
