package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class LexicalConceptComparator extends SameClassComparator<IOntologyConcept, WordNetRelation> {

    public boolean areSimilar(IOntologyConcept first, IOntologyConcept second) {
        return LexicalComparisonHelper.areSimilar(first, second);
    }

    public Set<IOntologyConcept> getByProperty(IOntologyConcept concept, WordNetRelation property) {
        if (property.equals(WordNetRelation.HYPONYM)) {
            return concept.getAllParents();
        }
        return LexicalComparisonHelper.getConceptSetByConceptAndProperty(concept, property);
    }
}
