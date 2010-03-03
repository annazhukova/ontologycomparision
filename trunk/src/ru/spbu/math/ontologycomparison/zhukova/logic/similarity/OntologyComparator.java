package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.OntologyMapper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.SynsetMapper;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator {
    private IOntologyGraph firstGraph;
    private IOntologyGraph secondGraph;
    private Integer intersectionSize;
    private Integer unionSize;

    public OntologyComparator(IOntologyGraph firstGraph, IOntologyGraph secondGraph) {
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
    }

    public double getSimilarity() {
        if (this.intersectionSize == null) {
            this.mapOntologies();
        }
        return intersectionSize / (double)unionSize;
    }

    public Collection<OntologyConcept> mapOntologies() {
        Collection<OntologyConcept> firstConcepts =
                (new SynsetMapper(new HashSet<OntologyConcept>(this.firstGraph.getConcepts()))).map();
        Collection<OntologyConcept> secondConcepts =
                (new SynsetMapper(new HashSet<OntologyConcept>(this.secondGraph.getConcepts()))).map();
        int secondConceptsSize = secondConcepts.size();
        Collection<OntologyConcept> result = (new OntologyMapper(firstConcepts, secondConcepts)).map();
        this.intersectionSize = secondConceptsSize - secondConcepts.size();
        this.unionSize = result.size();
        return result;
    }
}
