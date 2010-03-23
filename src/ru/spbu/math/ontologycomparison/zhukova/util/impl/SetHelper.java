package ru.spbu.math.ontologycomparison.zhukova.util.impl;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class SetHelper {

    public static final SetHelper INSTANCE = new SetHelper();

    private SetHelper() {}

    public <T> Set<T> setSubtraction(Set<T> first, Set<T> second) {
        if (first == null) {
            return Collections.EMPTY_SET;
        }
        Set<T> result = new LinkedHashSet<T>(first);
        if (second == null) {
            return result;
        }
        result.removeAll(second);
        return result;
    }

    public <T> Set<T> setUnion(Set<T> first, Set<T> second) {
        Set<T> result = first != null ? new LinkedHashSet<T>(first) : new LinkedHashSet<T>();
        if (second != null) {
            result.addAll(second);
        }
        return result;
    }

    public <T> Set<T> setIntersection(Set<T> first, Collection<T> second) {
        if (first == null || second == null) {
            return Collections.EMPTY_SET;
        }
        Set<T> result = new LinkedHashSet<T>(first);
        result.retainAll(second);
        return result;
    }
}
