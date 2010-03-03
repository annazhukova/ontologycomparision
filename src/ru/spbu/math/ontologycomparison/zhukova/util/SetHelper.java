package ru.spbu.math.ontologycomparison.zhukova.util;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

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
        Set<T> result = new HashSet<T>(first);
        if (second == null) {
            return result;
        }
        result.removeAll(second);
        return result;
    }

    public <T> Set<T> setUnion(Set<T> first, Set<T> second) {
        Set<T> result = first != null ? new HashSet<T>(first) : new HashSet<T>();
        if (second != null) {
            result.addAll(second);
        }
        return result;
    }

    public <T> Set<T> setIntersection(Set<T> first, Set<T> second) {
        if (first == null || second == null) {
            return Collections.EMPTY_SET;
        }
        Set<T> result = new HashSet<T>(first);
        result.retainAll(second);
        return result;
    }
}
