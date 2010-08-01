package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader;

//import net.sourceforge.fluxion.utils.OWLTransformationException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasonerException;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;

/**
 * @author Anna Zhukova
 */
public interface IOntologyManager {
    
    IOntologyGraph load(IClassAnnotationVisitor<IOntologyConcept> annotationVisitor, IPropertyVisitor<IOntologyConcept>... propertyVisitors) throws /*OWLTransformationException,*/ OWLReasonerException;

    OWLOntology getOntology();
}
