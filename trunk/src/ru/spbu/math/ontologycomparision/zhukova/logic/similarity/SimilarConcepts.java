package ru.spbu.math.ontologycomparision.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class SimilarConcepts<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>>
        implements ISimilarConcepts<C, R> {
    private final Set<C> concepts = new HashSet<C>();
    private final Set<C> unmodifyableConcepts = Collections.unmodifiableSet(concepts);
    private final Set<Object> similarityReasons = new HashSet<Object>();
    private int similarity;

    public SimilarConcepts(Object similarityReason, C... concepts) {
        this(similarityReason, Arrays.asList(concepts));
    }

    public SimilarConcepts(Object similarityReason, Collection<C> concepts) {
        this.similarityReasons.add(similarityReason);
        this.concepts.addAll(concepts);
    }

    public void addConcept(C concept) {
        this.concepts.add(concept);
    }

    public void addConcepts(C... concepts) {
        this.addConcepts(Arrays.asList(concepts));
    }

    public void addConcepts(Collection<C> concepts) {
        this.concepts.addAll(concepts);
    }

    public Set<C> getConcepts() {
        return this.unmodifyableConcepts;
    }

    public Set<Object> getSimilarityReasons() {
        return similarityReasons;
    }

    public void addSimilarityReason(Object similarityReason) {
        this.similarityReasons.add(similarityReason);
    }

    public int hashCode() {
        int result = 0;
        if (this.similarityReasons != null) {
            result = this.similarityReasons.hashCode();
        }
        result += 13 * this.concepts.hashCode();
        return  result;
    }

     public boolean equals(Object o) {
         if (this == o) {
             return true;
         }
         if (!(o instanceof SimilarConcepts)) {
             return false;
         }
         SimilarConcepts that = (SimilarConcepts) o;
         if (this.similarityReasons == null ? that.similarityReasons != null : !this.similarityReasons.equals(that.similarityReasons)) {
             return false;
         }
         return this.concepts.equals(that.concepts);
    }

    public int getSimilarity() {
        return similarity;
    }
}
