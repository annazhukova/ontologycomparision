package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader;

import org.semanticweb.owl.model.OWLOntology;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;

/**
 * @author Anna Zhukova
 */
public interface IOntologyManager {
    
    IOntologyGraph load(IClassAnnotationVisitor<IOntologyConcept> annotationVisitor, IPropertyVisitor<IOntologyConcept>... propertyVisitors);

    OWLOntology getOntology();
}
