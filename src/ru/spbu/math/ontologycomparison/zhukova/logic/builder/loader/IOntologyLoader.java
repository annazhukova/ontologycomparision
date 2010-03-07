package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader;

import org.semanticweb.owl.model.OWLObjectProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;

/**
 * @author Anna Zhukova
 */
public interface IOntologyLoader {
    
    IOntologyGraph load(IClassAnnotationVisitor<IOntologyConcept> annotationVisitor, IPropertyVisitor<IOntologyConcept>... propertyVisitors);

    OWLObjectProperty getProperty(String propertyName);
}
