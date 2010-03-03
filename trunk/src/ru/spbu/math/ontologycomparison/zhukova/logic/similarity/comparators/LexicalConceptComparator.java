package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class LexicalConceptComparator extends SameClassComparator<OntologyConcept, WordNetRelation> {

    public boolean areSimilar(OntologyConcept first, OntologyConcept second) {
        return LexicalComparisonHelper.areSimilar(first, second);
    }

    @Override
    public Set<OntologyConcept> getByProperty(OntologyConcept concept, WordNetRelation property) {
        return LexicalComparisonHelper.getConceptSetByConceptAndProperty(concept, property);
    }


}
