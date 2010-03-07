package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;

import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public interface IOntologyComparator {
    double getSimilarity();

    double getSimilarity(double conceptSimilarityWeight, double propertySimilarityWeight);

    double getConceptSimilarity();

    double getPropertySimilarity();

    IPair<Collection<IOntologyConcept>, Collection<IOntologyProperty>> mapOntologies();

    Collection<IOntologyProperty> mapProperties(Collection<IOntologyConcept> mappedConcepts);
}
