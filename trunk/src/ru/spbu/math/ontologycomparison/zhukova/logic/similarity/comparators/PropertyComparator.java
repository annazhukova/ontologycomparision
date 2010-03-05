package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;

import java.util.Collection;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class PropertyComparator extends SameClassComparator<OntologyProperty, Object> {
    private final Collection<OntologyConcept> mappedConcepts;

    public PropertyComparator(Collection<OntologyConcept> mappedConcepts) {
        this.mappedConcepts = mappedConcepts;
    }

    @Override
    public Set<OntologyProperty> getByProperty(OntologyProperty concept, Object property) {
        return null;
    }

    public boolean areSimilar(OntologyProperty first, OntologyProperty second) {
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
        return true;
    }

    private boolean rangesAreSimilar(OntologyProperty first, OntologyProperty second) {
        for (IOntologyConcept range : first.getRanges()) {
            if (!second.getRanges().containsAll(range.getSimilarConcepts())) {
                return false;
            }
        }
        return true;
    }

    private boolean domainsAreSimilar(OntologyProperty first, OntologyProperty second) {
        for (IOntologyConcept domain : first.getDomains()) {
            if (!second.getDomains().containsAll(domain.getSimilarConcepts())) {
                return false;
            }
        }
        return true;
    }
}
