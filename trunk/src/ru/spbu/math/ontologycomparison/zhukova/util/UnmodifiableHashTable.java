package ru.spbu.math.ontologycomparison.zhukova.util;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class UnmodifiableHashTable<K, V> extends HashMap<K, List<V>> implements IHashTable<K, V> {
    private final IHashTable<K, V> hashTable;

    public UnmodifiableHashTable(IHashTable<K, V> table) {
        super(table);
        this.hashTable = table;
    }

    public boolean has(Object value) {
        return this.hashTable.has(value);
    }

    public void insert(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public void insertAll(K key, Collection<V> values) {
        throw new UnsupportedOperationException();
    }

    public void insertAll(IHashTable<K, V> kviHashTable) {
        throw new UnsupportedOperationException();
    }

    public void insertAll(Map<K, V> kvMap) {
        throw new UnsupportedOperationException();
    }

    public boolean deleteValue(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Collection<V> allValues() {
        return this.hashTable.allValues();
    }

    public List<V> put(K key, List<V> value) {
        throw new UnsupportedOperationException();
    }

    public List<V> remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends List<V>> m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }
}
