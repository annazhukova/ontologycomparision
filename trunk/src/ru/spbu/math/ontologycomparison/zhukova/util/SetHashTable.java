package ru.spbu.math.ontologycomparison.zhukova.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class SetHashTable<K, V> extends HashTable<K, V, Set<V>> {

    @Override
    public Set<V> newCollection() {
        return new HashSet<V>();
    }
}
