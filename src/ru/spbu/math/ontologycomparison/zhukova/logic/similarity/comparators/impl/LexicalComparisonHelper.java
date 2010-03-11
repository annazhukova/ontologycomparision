package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.ILabeledOntologyEntity;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class LexicalComparisonHelper {

    public static Set<IOntologyConcept> getConceptSetByConceptAndProperty(IOntologyConcept concept, WordNetRelation property) {
        Set<IOntologyConcept> result = new LinkedHashSet<IOntologyConcept>();
        Set<IOntologyRelation> subjectRelations = concept.getSubjectRelations(property.getRelatedOntologyConcept());
        if (subjectRelations != null) {
            for (IOntologyRelation relation : subjectRelations) {
                result.add(relation.getObject());
            }
        }
        return result;
    }

    public static String normalizeString(String source) {
        // todo: manage CamelCase
        return source.toLowerCase().replace("_", " ").replace("-", " ").replace("\\", "/").trim();
    }

    public static boolean areSimilar(ILabeledOntologyEntity first, ILabeledOntologyEntity second) {
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
