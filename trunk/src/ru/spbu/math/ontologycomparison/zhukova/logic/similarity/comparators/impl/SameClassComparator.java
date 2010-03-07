package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.ISameClassComparator;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public abstract class SameClassComparator<C, P> extends Comparator<C, C, P> implements ISameClassComparator<C, P> {

    public Set<C> getByFirstProperty(C concept, P property) {
        return getByProperty(concept, property);
    }

    public Set<C> getBySecondProperty(C concept, P property) {
        return getByProperty(concept, property);
    }
}
