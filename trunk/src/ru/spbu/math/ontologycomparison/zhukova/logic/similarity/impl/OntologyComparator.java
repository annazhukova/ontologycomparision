package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.IOntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl.OntologyConceptMapper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl.OntologyPropertyMapper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl.SynsetMapper;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Pair;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator implements IOntologyComparator {
    private IOntologyGraph firstGraph;
    private IOntologyGraph secondGraph;
    private final ILogger logger;
    private Integer conceptIntersectionSize;
    private Integer conceptUnionSize;
    private Integer propertyIntersectionSize;
    private Integer propertyUnionSize;

    public OntologyComparator(IOntologyGraph firstGraph, IOntologyGraph secondGraph, ILogger logger) {
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
        this.logger = logger;
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
        if (this.propertyIntersectionSize == null) {
            this.mapOntologies();
        }
        if (propertyUnionSize == 0) {
            return 1;
        }
        return propertyIntersectionSize / (double) propertyUnionSize;
    }

    public IPair<Collection<IOntologyConcept>, Collection<IOntologyProperty>> mapOntologies() {
        Collection<IOntologyConcept> mappedConcepts = mapConcepts();
        return new Pair<Collection<IOntologyConcept>, Collection<IOntologyProperty>>(mappedConcepts, mapProperties(mappedConcepts));
    }

    private Collection<IOntologyConcept> mapConcepts() {
        SynsetMapper firstSynsetMapper = new SynsetMapper(new HashSet<IOntologyConcept>(this.firstGraph.getConcepts()), logger);
        Collection<IOntologyConcept> firstConcepts = firstSynsetMapper.map();
        this.firstGraph.setSynsetToConcept(firstSynsetMapper.getSynsetToConceptTable());
        SynsetMapper secondSynsetMapper = new SynsetMapper(new HashSet<IOntologyConcept>(this.secondGraph.getConcepts()), logger);
        Collection<IOntologyConcept> secondConcepts = secondSynsetMapper.map();
        this.secondGraph.setSynsetToConcept(secondSynsetMapper.getSynsetToConceptTable());
        int secondConceptsSize = secondConcepts.size();
        Collection<IOntologyConcept> result = (new OntologyConceptMapper(firstConcepts, secondConcepts, this.firstGraph, this.secondGraph, logger)).map();
        this.conceptIntersectionSize = secondConceptsSize - secondConcepts.size();
        this.conceptUnionSize = result.size();
        return result;
    }

    public Collection<IOntologyProperty> mapProperties(Collection<IOntologyConcept> mappedConcepts) {
        Collection<IOntologyProperty> firstProperties = new HashSet<IOntologyProperty>(this.firstGraph.getProperties());
        Collection<IOntologyProperty> secondProperties = new HashSet<IOntologyProperty>(this.secondGraph.getProperties());
        int secondPropertiesSize = secondProperties.size();
        Collection<IOntologyProperty> result = (new OntologyPropertyMapper(firstProperties, secondProperties,
                this.firstGraph, this.secondGraph, mappedConcepts, logger)).map();
        this.propertyIntersectionSize = secondPropertiesSize - secondProperties.size();
        this.propertyUnionSize = result.size();
        return result;
    }
}
