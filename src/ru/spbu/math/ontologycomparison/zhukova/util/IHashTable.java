package ru.spbu.math.ontologycomparison.zhukova.util;

import java.util.Map;
import java.util.Collection;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IHashTable<K, V> extends Map<K, Set<V>>, Cloneable, Serializable {

    boolean has(Object value);

    void insert(K key, V value);

    void insertAll(K key, Collection<V> values);

    void insertAll(IHashTable<K, V> table);
    
    void insertAll(Map<K, V> map);

    boolean deleteValue(K key, V value);

    Collection<V> allValues();
}
