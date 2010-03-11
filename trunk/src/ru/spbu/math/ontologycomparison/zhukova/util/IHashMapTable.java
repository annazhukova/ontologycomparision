package ru.spbu.math.ontologycomparison.zhukova.util;

import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Anna Zhukova
 */
public interface IHashMapTable<K, V> extends Map<K, Map<V, Integer>>, Cloneable, Serializable {
    boolean has(Object value);

    void insert(K key, V value, int count);

    void insert(K key, V value);

    void insertAll(K key, Collection<V> values);

    void insertAll(IHashTable<K, V, Map<V, Integer>> table);

    void insertAll(Map<K, V> map);

    boolean deleteValue(K key, V value);
}
