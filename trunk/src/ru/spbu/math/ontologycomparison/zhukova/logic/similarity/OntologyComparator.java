package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
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
        MapStore mapStore = new MapStore();
        mapStore.setFirstUriToConcept(this.firstGraph.getUriToConceptMap());
        mapStore.setSecondUriToConcept(this.secondGraph.getUriToConceptMap());
        mapStore.setFirstLabelToConcept(this.firstGraph.getLabelToConceptTable());
        mapStore.setSecondLabelToConcept(this.secondGraph.getLabelToConceptTable());
        SynsetMapper firstSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.firstGraph.getConcepts()));
        Collection<OntologyConcept> firstConcepts = firstSynsetMapper.map();
        mapStore.setFirstSynsetlToConcept(firstSynsetMapper.getSynsetToConceptTable());
        SynsetMapper secondSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.secondGraph.getConcepts()));
        Collection<OntologyConcept> secondConcepts = secondSynsetMapper.map();
        mapStore.setSecondSynsetToConcept(secondSynsetMapper.getSynsetToConceptTable());
        int secondConceptsSize = secondConcepts.size();
        Collection<OntologyConcept> result = (new OntologyMapper(firstConcepts, secondConcepts, mapStore)).map();
        this.intersectionSize = secondConceptsSize - secondConcepts.size();
        this.unionSize = result.size();
        PropertyComparator propertyComparator = new PropertyComparator(result);
        for (OntologyProperty first : this.firstGraph.getProperties()) {
            for (OntologyProperty second : this.secondGraph.getProperties()) {
                //System.out.println(first + " " + second);
                if (propertyComparator.areSimilar(first, second)) {
                    //System.out.printf("SIMILAR PROPERTIES: %s, %s\n", first, second);
                }
            }
        }
        return result;
    }
}
