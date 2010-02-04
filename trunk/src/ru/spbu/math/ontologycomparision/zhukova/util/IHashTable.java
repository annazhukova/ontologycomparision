package ru.spbu.math.ontologycomparision.zhukova.util;

import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.io.Serializable;

/**
 * @author Anna Zhukova
 */
public interface IHashTable<K, V> extends Map<K, List<V>>, Cloneable, Serializable {

    boolean has(Object value);

    void insert(K key, V value);

    void insertAll(K key, Collection<V> values);

    void insertAll(IHashTable<K, V> table);
    
    void insertAll(Map<K, V> map);
}
