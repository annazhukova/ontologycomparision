package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IMapStore;
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
    private IMapStore firstMapStore;
    private IMapStore secondMapStore;
    private Integer intersectionSize;
    private Integer unionSize;

    public OntologyComparator(IMapStore firstMapStore, IMapStore secondMapStore) {
        this.firstMapStore = firstMapStore;
        this.secondMapStore = secondMapStore;
    }

    public double getSimilarity() {
        if (this.intersectionSize == null) {
            this.mapOntologies();
        }
        return intersectionSize / (double) unionSize;
    }

    public Collection<OntologyConcept> mapOntologies() {
        SynsetMapper firstSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.firstMapStore.getConcepts()));
        Collection<OntologyConcept> firstConcepts = firstSynsetMapper.map();
        this.firstMapStore.setSynsetToConcept(firstSynsetMapper.getSynsetToConceptTable());
        SynsetMapper secondSynsetMapper = new SynsetMapper(new HashSet<OntologyConcept>(this.secondMapStore.getConcepts()));
        Collection<OntologyConcept> secondConcepts = secondSynsetMapper.map();
        this.secondMapStore.setSynsetToConcept(secondSynsetMapper.getSynsetToConceptTable());
        int secondConceptsSize = secondConcepts.size();
        Collection<OntologyConcept> result = (new OntologyMapper(firstConcepts, secondConcepts, this.firstMapStore, this.secondMapStore)).map();
        this.intersectionSize = secondConceptsSize - secondConcepts.size();
        this.unionSize = result.size();
        PropertyComparator propertyComparator = new PropertyComparator(result);
        //todo property compare!!!
        /*for (OntologyProperty first : this.firstMapStore.getProperties()) {
            for (OntologyProperty second : this.secondMapStore.getProperties()) {
                //System.out.println(first + " " + second);
                if (propertyComparator.areSimilar(first, second)) {
                    //System.out.printf("SIMILAR PROPERTIES: %s, %s\n", first, second);
                }
            }
        }*/
        return result;
    }
}
