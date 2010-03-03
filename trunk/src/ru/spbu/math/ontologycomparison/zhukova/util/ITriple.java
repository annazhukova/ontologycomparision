package ru.spbu.math.ontologycomparison.zhukova.util;

/**
 * @author Anna Zhukova
 */
public interface ITriple<F, S, T> extends IPair<F, S> {
    
    T getThird();
}
