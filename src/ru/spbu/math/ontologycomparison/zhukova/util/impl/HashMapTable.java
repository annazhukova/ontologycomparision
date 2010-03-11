package ru.spbu.math.ontologycomparison.zhukova.util.impl;

import ru.spbu.math.ontologycomparison.zhukova.util.IHashMapTable;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anna Zhukova
 */
public class HashMapTable<K, V> extends HashMap<K, Map<V, Integer>> implements IHashMapTable<K,V> {

    public boolean has(Object value) {
        for (Map<V, Integer> valueCollection : this.values()) {
            for (V aValue : valueCollection.keySet()) {
                if (aValue.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void insert(K key, V value, int count) {
        Map<V, Integer> oldValue = getOldValueOrNew(key);
        addToInnerMap(value, count, oldValue);
    }

    public void insert(K key, V value) {
        this.insert(key, value, 1);
    }

    private Map<V, Integer> getOldValueOrNew(K key) {
        Map<V, Integer> oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = new HashMap<V, Integer>();
            super.put(key, oldValue);
        }
        return oldValue;
    }

    private void addToInnerMap(V innerKey, int innerValue, Map<V, Integer> oldValue) {
        Integer count = oldValue.get(innerKey);
        if (count == null) {
            oldValue.put(innerKey, innerValue);
        } else {
            oldValue.put(innerKey, count + innerValue);
        }
    }

    private void addToInnerMap(V innerKey, Map<V, Integer> oldValue) {
        this.addToInnerMap(innerKey, 1, oldValue);
    }

    public void insertAll(K key, Collection<V> values) {
        Map<V, Integer> oldValue = getOldValueOrNew(key);
        for (V value : values) {
            addToInnerMap(value, oldValue);
        }
    }

    public void insertAll(IHashTable<K, V, Map<V, Integer>> table) {
        for (Map.Entry<K, Map<V, Integer>> entry : table.entrySet()) {
            Map<V, Integer> oldValue = getOldValueOrNew(entry.getKey());
            Map<V, Integer> value = entry.getValue();
            for (V innerKey : value.keySet()) {
                addToInnerMap(innerKey, value.get(innerKey), oldValue);
            }
        }
    }

    public void insertAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.insert(entry.getKey(), entry.getValue());
        }
    }

    public boolean deleteValue(K key, V value) {
        Map<V, Integer> values = get(key);
        if (values == null) {
            return false;
        }
        Integer innerValue = values.get(value);
        if (innerValue == null) {
            return false;
        }
        if (innerValue == 1) {
            values.remove(value);
        } else {
            values.put(value, innerValue - 1);
        }
        return true;
    }
}
