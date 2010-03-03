package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import ru.spbu.math.ontologycomparison.zhukova.util.Pair;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IComparator<C1, C2, P> {
    Pair<C1, C2> areSimilar(C1 first, C2 second, P property);

    Set<C1> getByFirstProperty(C1 concept, P property);

    Set<C2> getBySecondProperty(C2 concept, P property);

    boolean areSimilar(C1 first, C2 second);
}
