package ru.spbu.math.ontologycomparision.zhukova.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class UnmodifiableHashTable<K, V> implements IHashTable<K, V> {
    private final IHashTable<K, V> hashTable;

    public UnmodifiableHashTable(IHashTable<K, V> table) {
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

    public int size() {
        return this.hashTable.size();
    }

    public boolean isEmpty() {
        return this.hashTable.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.hashTable.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.hashTable.containsValue(value);
    }

    public Set<V> get(Object key) {
        return this.hashTable.get(key);
    }

    public Set<V> put(K key, Set<V> value) {
        throw new UnsupportedOperationException();
    }

    public Set<V> remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends Set<V>> m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Set<K> keySet() {
        return this.hashTable.keySet();
    }

    public Collection<Set<V>> values() {
        return this.hashTable.values();
    }

    public Set<Entry<K, Set<V>>> entrySet() {
        return this.hashTable.entrySet();
    }

    public boolean equals(Object o) {
        return this.hashTable.equals(o);
    }

    public int hashCode() {
        return this.hashTable.hashCode();
    }
}
