package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface ISameClassComparator<C, P> extends IComparator<C, C, P> {
    Set<C> getByProperty(C concept, P property);
}
