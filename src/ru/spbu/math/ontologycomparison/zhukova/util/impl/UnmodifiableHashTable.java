package ru.spbu.math.ontologycomparison.zhukova.util.impl;

import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class UnmodifiableHashTable<K, V, C extends Collection<V>> extends HashMap<K, C> implements IHashTable<K, V, C> {
    private final IHashTable<K, V, C> hashTable;

    public UnmodifiableHashTable(IHashTable<K, V, C> table) {
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

    public void insertAll(IHashTable<K, V, C> kviHashTable) {
        throw new UnsupportedOperationException();
    }

    public void insertAll(Map<K, V> kvMap) {
        throw new UnsupportedOperationException();
    }

    public boolean deleteValue(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public C allValues() {
        return this.hashTable.allValues();
    }

    public C put(K key, C value) {
        throw new UnsupportedOperationException();
    }

    public C remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends C> m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }
}
