package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.ILabeledEntity;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class LexicalComparisonHelper {

    public static Set<OntologyConcept> getConceptSetByConceptAndProperty(OntologyConcept concept, WordNetRelation property) {
        Set<OntologyConcept> result = new LinkedHashSet<OntologyConcept>();
        for (OntologyRelation relation : concept.getSubjectRelations(property.getRelatedOntologyConcept())) {
            result.add(relation.getObject());
        }
        return result;
    }

    public static String normalizeString(String source) {
        // todo: manage CamelCase
        return source.toLowerCase().replace("_", " ").replace("-", " ").replace("\\", "/").trim();
    }

    public static boolean areSimilar(ILabeledEntity first, ILabeledEntity second) {
        for (String firstLabel : first.getLabels()) {
            for (String secondLabel : second.getLabels()) {
                if (areSimilar(firstLabel, secondLabel)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean areSimilar(String first, String second) {
        first = LexicalComparisonHelper.normalizeString(first);
        second = LexicalComparisonHelper.normalizeString(second);
        return first.equals(second);
    }
}
