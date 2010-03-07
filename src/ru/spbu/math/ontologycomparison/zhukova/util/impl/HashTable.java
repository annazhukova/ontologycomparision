package ru.spbu.math.ontologycomparison.zhukova.util.impl;

import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public abstract class HashTable<K, V, C extends Collection<V>> extends HashMap<K, C> implements IHashTable<K, V, C> {

    public HashTable() {}

    public HashTable(Map<K, C> map) {
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
        C oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = newCollection();
            super.put(key, oldValue);
        }
        oldValue.add(value);
    }

    public void insertAll(K key, Collection<V> values) {
        C oldValue = this.get(key);
        if (oldValue == null) {
            oldValue = newCollection();
            super.put(key, oldValue);
        }
        oldValue.addAll(values);
    }

    public void insertAll(IHashTable<K, V, C> table) {
        for (Map.Entry<K, C> entry : table.entrySet()) {
            this.insertAll(entry.getKey(), entry.getValue());
        }
    }

    public void insertAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.insert(entry.getKey(), entry.getValue());
        }
    }

    public boolean deleteValue(K key, V value) {
        /*System.out.printf("DELETIND VALUE %s FOR KEY %s\n", value, key);*/
        C values = get(key);
        /*System.out.printf("VALUE SET %s\n", values);*/
        if (values == null) {
            return false;
        }
        boolean  result = values.remove(value);
        /*System.out.printf("REMOVED: %s\n", result);*/
        if (values.size() == 0) {
            super.remove(key);
            /*System.out.printf("REMOVED TOTALLY\n");*/
        }
        return result;
    }

    public C allValues() {
        C result = newCollection();
        for (C valueList : this.values()) {
            result.addAll(valueList);
        }
        return result;
    }

    public abstract C newCollection();
}
