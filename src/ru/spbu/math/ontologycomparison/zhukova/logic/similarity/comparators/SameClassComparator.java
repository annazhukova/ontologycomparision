package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public abstract class SameClassComparator<C, P> extends Comparator<C, C, P> {

    public abstract Set<C> getByProperty(C concept, P property);

    @Override
    public Set<C> getByFirstProperty(C concept, P property) {
        return getByProperty(concept, property);
    }

    @Override
    public Set<C> getBySecondProperty(C concept, P property) {
        return getByProperty(concept, property);
    }
}
