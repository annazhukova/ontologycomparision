package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.PropertyComparator;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.SetHelper;
import ru.spbu.math.ontologycomparison.zhukova.util.Triple;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.SAME_URI;

/**
 * @author Anna Zhukova
 */
public class OntologyPropertyMapper extends Mapper<OntologyProperty, OntologyProperty, Object> {
    private Collection<OntologyProperty> firstProperties;
    private Collection<OntologyProperty> secondProperties;
    private final IOntologyGraph firstGraph;
    private final IOntologyGraph secondGraph;
    private final Collection<OntologyConcept> mappedConcepts;

    public OntologyPropertyMapper(Collection<OntologyProperty> firstProperties, Collection<OntologyProperty> secondProperties,
                                  IOntologyGraph firstGraph, IOntologyGraph secondGraph, Collection<OntologyConcept> mappedConcepts) {
        this.firstProperties = firstProperties;
        this.secondProperties = secondProperties;
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
        this.mappedConcepts = mappedConcepts;
    }

    public Collection<OntologyProperty> map() {
        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(firstGraph.getPropertyUris(),
                secondGraph.getPropertyUris());
        for (URI uri : commonUriSet) {
            OntologyProperty first = firstGraph.getUriToProperty().get(uri);
            OntologyProperty second = secondGraph.getUriToProperty().get(uri);
            bind(first, second, SAME_URI);
            secondProperties.remove(second);
        }

        PropertyComparator propertyComparator = new PropertyComparator(mappedConcepts);
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(firstGraph.getPropertyLabels(),
                secondGraph.getPropertyLabels());
        for (String label : commonLabelSet) {
            for (OntologyProperty first : firstGraph.getLabelToProperty().get(label)) {
                for (OntologyProperty second : secondGraph.getLabelToProperty().get(label)) {
                    if (tryToBind(propertyComparator, first, second, getBindFactors())) {
                        secondProperties.remove(second);
                        break;
                    }
                }
            }
        }
        firstProperties.addAll(secondProperties);
        System.out.println("binded ontologies");
        return firstProperties;
    }

    public ITriple<Object, String, String>[] getBindFactors() {
        return new Triple[0];
    }

    public void bind(OntologyProperty first, OntologyProperty second, String reason) {
        first.addProperty(second, reason);
    }
}
