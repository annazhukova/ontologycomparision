package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
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
public class OntologyPropertyMapper extends Mapper<IOntologyProperty, IOntologyProperty, Object, Collection<IOntologyProperty>> {
    private Collection<IOntologyProperty> firstProperties;
    private Collection<IOntologyProperty> secondProperties;
    private final IOntologyGraph firstGraph;
    private final IOntologyGraph secondGraph;
    private final Collection<IOntologyConcept> mappedConcepts;
    private final ILogger logger;

    public OntologyPropertyMapper(Collection<IOntologyProperty> firstProperties, Collection<IOntologyProperty> secondProperties,
                                  IOntologyGraph firstGraph, IOntologyGraph secondGraph, Collection<IOntologyConcept> mappedConcepts,
                                  ILogger logger) {
        this.firstProperties = firstProperties;
        this.secondProperties = secondProperties;
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
        this.mappedConcepts = mappedConcepts;
        this.logger = logger;
    }

    public Collection<IOntologyProperty> map() {
        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(firstGraph.getPropertyUris(),
                secondGraph.getPropertyUris());
        for (URI uri : commonUriSet) {
            IOntologyProperty first = firstGraph.getUriToProperty().get(uri);
            IOntologyProperty second = secondGraph.getUriToProperty().get(uri);
            bind(first, second, SAME_URI);
        }

        PropertyComparator propertyComparator = new PropertyComparator(mappedConcepts);
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(firstGraph.getPropertyLabels(),
                secondGraph.getPropertyLabels());
        for (String label : commonLabelSet) {
            for (IOntologyProperty first : firstGraph.getLabelToProperty().get(label)) {
                for (IOntologyProperty second : secondGraph.getLabelToProperty().get(label)) {
                    if (tryToBind(propertyComparator, first, second, getBindFactors())) {
                        break;
                    }
                }
            }
        }
        firstProperties.addAll(secondProperties);
        logger.log("binded ontologies");
        return firstProperties;
    }

    public ITriple<Object, String, String>[] getBindFactors() {
        return new Triple[0];
    }

    public void bind(IOntologyProperty first, IOntologyProperty second, String reason, int count) {
        first.addProperty(second, reason, count);
        second.addProperty(first, reason, count);
        secondProperties.remove(second);
    }

    public void bind(IOntologyProperty first, IOntologyProperty second, String reason) {
        bind(first, second, reason, 1);
    }
}
