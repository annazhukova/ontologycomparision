package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl.PropertyComparator;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHelper;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Triple;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.SAME_URI;

/**
 * @author Anna Zhukova
 */
public class OntologyPropertyMapper extends Mapper<IOntologyProperty, IOntologyProperty, Object> {
    private Collection<IOntologyProperty> firstProperties;
    private Collection<IOntologyProperty> secondProperties;
    private final IOntologyGraph firstGraph;
    private final IOntologyGraph secondGraph;
    private final Collection<IOntologyConcept> mappedConcepts;

    public OntologyPropertyMapper(Collection<IOntologyProperty> firstProperties, Collection<IOntologyProperty> secondProperties,
                                  IOntologyGraph firstGraph, IOntologyGraph secondGraph, Collection<IOntologyConcept> mappedConcepts) {
        this.firstProperties = firstProperties;
        this.secondProperties = secondProperties;
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
        this.mappedConcepts = mappedConcepts;
    }

    public Collection<IOntologyProperty> map() {
        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(firstGraph.getPropertyUris(),
                secondGraph.getPropertyUris());
        for (URI uri : commonUriSet) {
            IOntologyProperty first = firstGraph.getUriToProperty().get(uri);
            IOntologyProperty second = secondGraph.getUriToProperty().get(uri);
            bind(first, second, SAME_URI);
            secondProperties.remove(second);
        }

        PropertyComparator propertyComparator = new PropertyComparator(mappedConcepts);
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(firstGraph.getPropertyLabels(),
                secondGraph.getPropertyLabels());
        for (String label : commonLabelSet) {
            for (IOntologyProperty first : firstGraph.getLabelToProperty().get(label)) {
                for (IOntologyProperty second : secondGraph.getLabelToProperty().get(label)) {
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

    public void bind(IOntologyProperty first, IOntologyProperty second, String reason) {
        first.addProperty(second, reason);
    }
}
