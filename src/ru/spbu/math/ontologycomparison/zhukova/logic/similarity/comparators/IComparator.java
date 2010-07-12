package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import ru.spbu.math.ontologycomparison.zhukova.util.IPair;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IComparator<C1, C2, P> {
    IPair<C1, C2> areSimilar(C1 first, C2 second, P property);

    Set<C1> getFirstByProperty(C1 concept, P property);

    Set<C2> getSecondByProperty(C2 concept, P property);

    boolean areSimilar(C1 first, C2 second);
}
