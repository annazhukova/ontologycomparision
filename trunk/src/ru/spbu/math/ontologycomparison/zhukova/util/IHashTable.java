package ru.spbu.math.ontologycomparison.zhukova.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Anna Zhukova
 */
public interface IHashTable<K, V, C extends Collection<V>> extends Map<K, C>, Cloneable, Serializable {

    boolean has(Object value);

    void insert(K key, V value);

    void insertAll(K key, Collection<V> values);

    void insertAll(IHashTable<K, V, C> table);
    
    void insertAll(Map<K, V> map);

    boolean deleteValue(K key, V value);

    C allValues();
}
