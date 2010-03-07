package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;

import java.util.Collection;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class PropertyComparator extends SameClassComparator<IOntologyProperty, Object> {
    private final Collection<IOntologyConcept> mappedConcepts;

    public PropertyComparator(Collection<IOntologyConcept> mappedConcepts) {
        this.mappedConcepts = mappedConcepts;
    }

    public Set<IOntologyProperty> getByProperty(IOntologyProperty concept, Object property) {
        return null;
    }

    public boolean areSimilar(IOntologyProperty first, IOntologyProperty second) {
        if (first.getUri().equals(second.getUri())) {
            return true;
        }
        if (!LexicalComparisonHelper.areSimilar(first, second)) {
            return false;
        }
        if (!domainsAreSimilar(first, second)) {
            return false;
        }
        if (!rangesAreSimilar(first, second)) {
            return false;
        }
        return (first.isFunctional() == second.isFunctional());
    }

    private boolean rangesAreSimilar(IOntologyProperty first, IOntologyProperty second) {
        for (IOntologyConcept range : first.getRanges()) {
            if (!second.getRanges().containsAll(range.getSimilarConcepts())) {
                return false;
            }
        }
        return true;
    }

    private boolean domainsAreSimilar(IOntologyProperty first, IOntologyProperty second) {
        for (IOntologyConcept domain : first.getDomains()) {
            if (!second.getDomains().containsAll(domain.getSimilarConcepts())) {
                return false;
            }
        }
        return true;
    }
}
