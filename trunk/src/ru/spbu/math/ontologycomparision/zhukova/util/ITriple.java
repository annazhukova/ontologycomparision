package ru.spbu.math.ontologycomparision.zhukova.util;

/**
 * @author Anna Zhukova
 */
public interface ITriple<F, S, T> extends IPair<F, S> {
    
    T getThird();
}
