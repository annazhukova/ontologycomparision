package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.OntologyConceptMapper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.OntologyPropertyMapper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.SynsetMapper;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.Pair;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator {
    private IOntologyGraph firstGraph;
    private IOntologyGraph secondGraph;
    private Integer conceptIntersectionSize;
    private Integer conceptUnionSize;
    private Integer propertyItersectionSize;
    private Integer propertyUnionSize;

    public OntologyComparator(IOntologyGraph firstGraph, IOntologyGraph secondGraph) {
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
    }

    public double getSimilarity() {
        return getSimilarity(1.0, 0.0);
    }

    public double getSimilarity(double conceptSimilarityWeight, double propertySimilarityWeight) {
        return conceptSimilarityWeight * getConceptSimilarity() + propertySimilarityWeight * getPropertySimilarity();
    }

    public double getConceptSimilarity() {
        if (this.conceptIntersectionSize == null) {
            this.mapOntologies();
        }
        if (conceptUnionSize == 0) {
            return 1;
        }
        return conceptIntersectionSize / (double) conceptUnionSize;
    }

    public double getPropertySimilarity() {
        if (this.propertyItersectionSize == null) {
            this.mapOntologies();
        }
        if (propertyUnionSize == 0) {
            return 1;
        }
        return propertyItersectionSize / (double) propertyUnionSize;
    }

    public IPair<Collection<OntologyConcept>, Collection<OntologyProperty>> mapOntologies() {
        Collection<OntologyConcept> mappedConcepts = mapConcepts();
        return new Pair<Collection<OntologyConcept>, Collection<OntologyProperty>>(mappedConcepts, mapProperties(mappedConcepts));
    }

    private Collection<OntologyConcept> mapConcepts() {
        SynsetMapper firstSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.firstGraph.getConcepts()));
        Collection<OntologyConcept> firstConcepts = firstSynsetMapper.map();
        this.firstGraph.setSynsetToConcept(firstSynsetMapper.getSynsetToConceptTable());
        SynsetMapper secondSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.secondGraph.getConcepts()));
        Collection<OntologyConcept> secondConcepts = secondSynsetMapper.map();
        this.secondGraph.setSynsetToConcept(secondSynsetMapper.getSynsetToConceptTable());
        int secondConceptsSize = secondConcepts.size();
        Collection<OntologyConcept> result = (new OntologyConceptMapper(firstConcepts, secondConcepts, this.firstGraph, this.secondGraph)).map();
        this.conceptIntersectionSize = secondConceptsSize - secondConcepts.size();
        this.conceptUnionSize = result.size();
        return result;
    }

    public Collection<OntologyProperty> mapProperties(Collection<OntologyConcept> mappedConcepts) {
        Collection<OntologyProperty> firstProperties = new HashSet<OntologyProperty>(this.firstGraph.getProperties());
        Collection<OntologyProperty> secondProperties = new HashSet<OntologyProperty>(this.secondGraph.getProperties());
        int secondPropertiesSize = secondProperties.size();
        Collection<OntologyProperty> result = (new OntologyPropertyMapper(firstProperties, secondProperties,
                this.firstGraph, this.secondGraph, mappedConcepts)).map();
        this.propertyItersectionSize = secondPropertiesSize - secondProperties.size();
        this.propertyUnionSize = result.size();
        return result;
    }
}
