package ru.spbu.math.ontologycomparision.zhukova.util;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Anna Zhukova
 */
public class SetHelper {

    public static final SetHelper INSTANCE = new SetHelper();

    private SetHelper(){};

    public <T> Set<T> setSubtraction(Set<T> fisrt, Set<T> second) {
        Set<T> result = new HashSet<T>(fisrt);
        result.removeAll(second);
        return result;
    }

    public <T> Set<T> setUnion(Set<T> fisrt, Set<T> second) {
        Set<T> result = new HashSet<T>(fisrt);
        result.addAll(second);
        return result;
    }

    public <T> Set<T> setIntersection(Set<T> fisrt, Set<T> second) {
        Set<T> result = new HashSet<T>(fisrt);
        result.retainAll(second);
        return result;
    }
}
