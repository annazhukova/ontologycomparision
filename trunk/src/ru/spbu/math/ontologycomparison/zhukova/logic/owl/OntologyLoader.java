package ru.spbu.math.ontologycomparison.zhukova.logic.owl;

import net.sourceforge.fluxion.utils.OWLTransformationException;
import net.sourceforge.fluxion.utils.OWLUtils;
import net.sourceforge.fluxion.utils.ReasonerSession;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.io.StreamInputSource;
import org.semanticweb.owl.model.*;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyRelation;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 *         Processes OWL file and creates internal ontology map (node id to node).
 */
public class OntologyLoader<C extends IOntologyConcept, R extends IOntologyRelation<C>> {
    private OWLOntology ontology;
    private OWLReasoner reasoner;

    /**
     * Create an instance with ontology read from the given InputStream.
     *
     * @param ontologyStream InputStream with ontology
     */
    public OntologyLoader(InputStream ontologyStream) {
        this(new StreamInputSource(ontologyStream));
    }

    /**
     * Create an instance with ontology read from the given StreamInputSource.
     *
     * @param ontologyInput StreamInputSource with ontology
     */
    public OntologyLoader(StreamInputSource ontologyInput) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            this.ontology = manager.loadOntology(ontologyInput);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Can't load ontology", e);
        }

    }

    public OntologyLoader(File ontologyFile) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            this.ontology = manager.loadOntologyFromPhysicalURI(ontologyFile.toURI());
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Can't load ontology", e);
        }

    }


    /**
     * Loads ontology into Map id -> internal node implementation.
     *
     * @param annotationVisitor To visit annotations.
     * @param propertyVisitors  To visit properties.
     * @return Map ontology's been loaded into.
     */
    public Map<URI, C> load(IClassAnnotationVisitor<C> annotationVisitor,
                                                         IPropertyVisitor<C>... propertyVisitors) {
        Map<URI, C> concepts = new HashMap<URI, C>();
        ReasonerSession session = OWLUtils.getReasonerSession(this.ontology);
        try {
            this.reasoner = session.getReasoner();
            for (OWLClass cls : this.ontology.getReferencedClasses()) {
                loadClass(cls, annotationVisitor, concepts);
            }
        } catch (OWLReasonerException e) {
            throw new RuntimeException(e);
        } finally {
            session.releaseSession();
        }
        for (IPropertyVisitor<C> visitor : propertyVisitors) {
            loadProperties(visitor, concepts);
        }
        return concepts;
    }

    /*
     * Finds all classes with the relationship induced by the property the given visitor is interested in
     * and gives them to the visitor
     */
    private void loadProperties(IPropertyVisitor<C> visitor, Map<URI, C> concepts) {

        for (OWLClass clazz : this.ontology.getReferencedClasses()) {
            URI uri = clazz.getURI();
            C concept = concepts.get(uri);
            Set<OWLRestriction> owlRestrictions = null;
            try {
                owlRestrictions = OWLUtils.keep(this.ontology, clazz);
                for (OWLRestriction restriction : owlRestrictions) {
                    OWLPropertyExpression property = restriction.getProperty();
                    for (OWLClass friend : restriction.getClassesInSignature()) {
                        URI friendId = friend.getURI();
                        visitor.inRelationship(concept, concepts.get(friendId), property);
                    }
                }
            } catch (OWLTransformationException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Finds OWLObjectProperty by name.
     *
     * @param propertyName Name of the property.
     * @return OWLObjectProperty with the specified name.
     */
    public OWLObjectProperty getProperty(String propertyName) {
        for (OWLObjectProperty prpt : this.ontology.getReferencedObjectProperties()) {
            if (prpt.toString().equals(propertyName)) {
                return prpt;
            }
        }
        return null;
    }

    /*
     * Loads class and its children and
     * returns respective node if it's not organizational and nodes for its children otherwise.
     * @param clazz Class to load.
     * @param annotationVisitor Visitor for annotations.
     * @return Collection containing the node respective to the specified class if it's not organizational
     * and nodes for its children otherwise.
     * @param ontologyMap Map id -> internal node implementation.
     * @throws OWLReasonerException  If operations with the reasoner fail.
     */
    private C loadClass(OWLClass clazz, IClassAnnotationVisitor<C> annotationVisitor,
                                                     Map<URI, C> concepts)
            throws OWLReasonerException {
        if (this.reasoner.isSatisfiable(clazz)) {
            URI uri = clazz.getURI();
            C concept = concepts.get(uri);
            if (concept == null) {
                for (OWLAnnotation annotation : clazz.getAnnotations(this.ontology)) {
                    annotation.accept(annotationVisitor);
                }
                concept = annotationVisitor.getOntologyConcept(uri);
                Set<Set<OWLClass>> children = this.reasoner.getSubClasses(clazz);
                for (Set<OWLClass> setOfClasses : children) {
                    for (OWLClass child : setOfClasses) {
                        if (!child.equals(clazz)) {
                            C childConcept = loadClass(child, annotationVisitor, concepts);
                            if (childConcept != null) {
                                /*concept.addChild(childConcept);*/
                                childConcept.addParent(concept);
                            }
                        }
                    }
                }
                concepts.put(uri, concept);
            }
            return concept;
        }
        return null;
    }

}

