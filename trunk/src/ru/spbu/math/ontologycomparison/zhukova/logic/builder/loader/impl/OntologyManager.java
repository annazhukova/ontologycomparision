package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl;

import net.sourceforge.fluxion.utils.OWLTransformationException;
import net.sourceforge.fluxion.utils.OWLUtils;
import net.sourceforge.fluxion.utils.ReasonerSession;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.io.DefaultOntologyFormat;
import org.semanticweb.owl.io.StreamInputSource;
import org.semanticweb.owl.model.*;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IOntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IPropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import uk.ac.manchester.cs.owl.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.OWLEquivalentClassesAxiomImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Anna Zhukova
 *         Processes OWL file and creates internal ontology map (node id to node).
 */
public class OntologyManager implements IOntologyManager {
    private OWLOntology ontology;
    private OWLReasoner reasoner;

    /**
     * Create an instance with ontology read from the given InputStream.
     *
     * @param ontologyStream InputStream with ontology
     */
    public OntologyManager(InputStream ontologyStream) {
        this(new StreamInputSource(ontologyStream));
    }

    /**
     * Create an instance with ontology read from the given StreamInputSource.
     *
     * @param ontologyInput StreamInputSource with ontology
     */
    public OntologyManager(StreamInputSource ontologyInput) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            this.ontology = manager.loadOntology(ontologyInput);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Can't load ontology", e);
        }

    }

    public OntologyManager(File ontologyFile) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            this.ontology = manager.loadOntologyFromPhysicalURI(ontologyFile.toURI());
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Can't load ontology", e);
        }

    }

    public static synchronized OWLOntology saveOntologies(OWLOntologyManager manager, OWLOntology... ontologies) throws OWLOntologyChangeException, OWLOntologyCreationException, OWLOntologyStorageException, IOException, URISyntaxException {
        Set<OWLOntology> ontologySet = new HashSet<OWLOntology>((Arrays.asList(ontologies)));
        File temp = File.createTempFile("ontology", ".owl");
        System.out.println(temp.getAbsolutePath());
        return manager.createOntology(getURIForFile(temp), ontologySet);
    }

    private static URI getURIForFile(File file) throws URISyntaxException {
        return new URI(String.format("file:///%s", file.getAbsolutePath().replace("\\", "/")));
    }


    /**
     * Loads ontology into Map id -> internal node implementation.
     *
     * @param annotationVisitor To visit annotations.
     * @param propertyVisitors  To visit properties.
     * @return Map ontology's been loaded into.
     */
    public IOntologyGraph load(IClassAnnotationVisitor<IOntologyConcept> annotationVisitor, IPropertyVisitor<IOntologyConcept>... propertyVisitors) {
        Map<URI, IOntologyConcept> uriToConcept = new HashMap<URI, IOntologyConcept>();
        Map<URI, IOntologyProperty> uriToProperty = new HashMap<URI, IOntologyProperty>();
        IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept = new SetHashTable<String, IOntologyConcept>();
        IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty = new SetHashTable<String, IOntologyProperty>();
        Set<IOntologyConcept> roots = new HashSet<IOntologyConcept>();
        ReasonerSession session = OWLUtils.getReasonerSession(this.getOntology());
        try {
            this.reasoner = session.getReasoner();
            for (OWLClass cls : this.getOntology().getReferencedClasses()) {
                loadClass(cls, annotationVisitor, uriToConcept, labelToConcept, roots);
            }
            for (OWLProperty property : this.getOntology().getReferencedObjectProperties()) {
                loadProperty(property, uriToProperty, labelToProperty, uriToConcept);
            }
            for (IPropertyVisitor<IOntologyConcept> visitor : propertyVisitors) {
                loadProperties(visitor, uriToConcept);
            }
        } catch (OWLReasonerException e) {
            throw new RuntimeException(e);
        } finally {
            session.releaseSession();
        }
        return new OntologyGraph(roots, uriToConcept, labelToConcept, uriToProperty, labelToProperty);
    }

    private void loadProperty(OWLProperty property, Map<URI, IOntologyProperty> uriToProperty,
                              IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty,
                              Map<URI, IOntologyConcept> uriToConcept) {
        if (property.isAnonymous()) {
            return;
        }
        String label = null;
        for (OWLAnnotation annotation : property.getAnnotations(getOntology())) {
            if (annotation instanceof OWLConstantAnnotation) {
                OWLConstantAnnotation mayBeLabel = (OWLConstantAnnotation) annotation;
                if (mayBeLabel.isLabel()) {
                    label = mayBeLabel.getAnnotationValue().getLiteral();
                }
            }
        }
        Set<IOntologyConcept> domains = new HashSet<IOntologyConcept>();
        for (Object domain : property.getDomains(getOntology())) {
            OWLDescription domainDescription = (OWLDescription) domain;
            for (OWLClass clazz : domainDescription.getClassesInSignature()) {
                IOntologyConcept concept = uriToConcept.get(clazz.getURI());
                if (concept != null) {
                    domains.add(concept);
                }
            }
        }
        Set<IOntologyConcept> ranges = new HashSet<IOntologyConcept>();
        for (Object domain : property.getRanges(getOntology())) {
            OWLDescription rangeDescription = (OWLDescription) domain;
            for (OWLClass clazz : rangeDescription.getClassesInSignature()) {
                IOntologyConcept concept = uriToConcept.get(clazz.getURI());
                if (concept != null) {
                    ranges.add(concept);
                }
            }
        }
        IOntologyProperty ontologyProperty = new OntologyProperty(property.getURI(), label,
                domains.toArray(new IOntologyConcept[domains.size()]), ranges.toArray(new IOntologyConcept[ranges.size()]),
                property.isFunctional(getOntology()));
        uriToProperty.put(property.getURI(), ontologyProperty);
        labelToProperty.insert(ontologyProperty.getNormalizedMainLabel(), ontologyProperty);
    }

    /*
     * Finds all classes with the relationship induced by the property the given visitor is interested in
     * and gives them to the visitor
     */

    private void loadProperties(IPropertyVisitor<IOntologyConcept> visitor, Map<URI, IOntologyConcept> concepts) {

        for (OWLClass clazz : this.getOntology().getReferencedClasses()) {
            URI uri = clazz.getURI();
            IOntologyConcept concept = concepts.get(uri);
            Set<OWLRestriction> owlRestrictions;
            try {
                owlRestrictions = OWLUtils.keep(this.getOntology(), clazz);
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

    private IOntologyConcept loadClass(OWLClass clazz, IClassAnnotationVisitor<IOntologyConcept> annotationVisitor,
                                       Map<URI, IOntologyConcept> concepts, IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept,
                                       Set<IOntologyConcept> roots)
            throws OWLReasonerException {
        if (this.reasoner.isSatisfiable(clazz)) {
            URI uri = clazz.getURI();
            IOntologyConcept concept = concepts.get(uri);
            if (concept == null) {
                annotationVisitor.start();
                for (OWLAnnotation annotation : clazz.getAnnotations(this.getOntology())) {
                    annotation.accept(annotationVisitor);
                }
                concept = annotationVisitor.getOntologyConcept(uri);
                concept.setOWLClass(clazz);
                Set<Set<OWLClass>> children = this.reasoner.getSubClasses(clazz);
                for (Set<OWLClass> setOfClasses : children) {
                    for (OWLClass child : setOfClasses) {
                        if (!child.equals(clazz)) {
                            IOntologyConcept childConcept = loadClass(child, annotationVisitor, concepts, labelToConcept, roots);
                            if (childConcept != null) {
                                childConcept.addParent(concept);
                                concept.addChild(childConcept);
                                roots.remove(childConcept);
                            }
                        }
                    }
                }
                concepts.put(uri, concept);
                roots.add(concept);
                labelToConcept.insert(concept.getNormalizedMainLabel(), concept);
            }
            return concept;
        }
        return null;
    }

    public static void addEquivalentClasses(OWLOntologyManager manager, OWLOntology ontology, Set<OWLClass> clazzes) throws OWLOntologyChangeException {
        manager.addAxiom(ontology, new OWLEquivalentClassesAxiomImpl(new OWLDataFactoryImpl(), clazzes));
    }

    public static void saveResult(OWLOntologyManager manager, OWLOntology ontology, File file) throws OWLOntologyStorageException, URISyntaxException {
        manager.saveOntology(ontology, getOntologyFormatByFile(file), getURIForFile(file));
    }

    private static OWLOntologyFormat getOntologyFormatByFile(File file) {
        return new DefaultOntologyFormat();
    }

    public OWLOntology getOntology() {
        return ontology;
    }
}

