package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.IComparator;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Pair;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public abstract class Comparator<C1, C2, P> implements IComparator<C1,C2,P> {

    public IPair<C1, C2> areSimilar(C1 first, C2 second, P property) {
        Set<C1> firstSet =  getFirstByProperty(first, property);
        Set<C2> secondSet = getSecondByProperty(second, property);
        return areSimilar(firstSet, secondSet);
    }

    protected IPair<C1, C2> areSimilar(Set<C1> firstSet, Set<C2> secondSet) {
        for (C1 first : firstSet){
            for (C2 second : secondSet) {
                if (areSimilar(first, second)) {
                    return new Pair<C1, C2>(first, second);
                }
            }
        }
        return null;
    }
}
