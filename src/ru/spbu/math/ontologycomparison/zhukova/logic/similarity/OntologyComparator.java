package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.PropertyComparator;
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
        return intersectionSize / (double) unionSize;
    }

    public Collection<OntologyConcept> mapOntologies() {
        SynsetMapper firstSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.firstGraph.getConcepts()));
        Collection<OntologyConcept> firstConcepts = firstSynsetMapper.map();
        this.firstGraph.setSynsetToConcept(firstSynsetMapper.getSynsetToConceptTable());
        SynsetMapper secondSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.secondGraph.getConcepts()));
        Collection<OntologyConcept> secondConcepts = secondSynsetMapper.map();
        this.secondGraph.setSynsetToConcept(secondSynsetMapper.getSynsetToConceptTable());
        int secondConceptsSize = secondConcepts.size();
        Collection<OntologyConcept> result = (new OntologyMapper(firstConcepts, secondConcepts, this.firstGraph, this.secondGraph)).map();
        this.intersectionSize = secondConceptsSize - secondConcepts.size();
        this.unionSize = result.size();
        PropertyComparator propertyComparator = new PropertyComparator(result);
        //todo property compare!!!
        /*for (OntologyProperty first : this.firstGraph.getProperties()) {
            for (OntologyProperty second : this.secondGraph.getProperties()) {
                //System.out.println(first + " " + second);
                if (propertyComparator.areSimilar(first, second)) {
                    //System.out.printf("SIMILAR PROPERTIES: %s, %s\n", first, second);
                }
            }
        }*/
        return result;
    }
}
