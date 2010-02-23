package ru.spbu.math.ontologycomparision.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;

import java.util.Collection;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface ISimilarConcepts<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {

    Set<C> getConcepts();

    Set<Object> getSimilarityReasons();

    void addSimilarityReason(Object similarityReason);

    void addConcept(C concept);

    void addConcepts(C... concepts);

    void addConcepts(Collection<C> concepts);

    int getSimilarity();
}
