package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl;

/*import net.sourceforge.fluxion.utils.OWLTransformationException;
import net.sourceforge.fluxion.utils.OWLUtils;
import net.sourceforge.fluxion.utils.ReasonerSession;*/

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.DefaultOntologyFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
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
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Anna Zhukova
 *         Processes OWL file and creates internal ontology map (node id to node).
 */
public class OntologyManager implements IOntologyManager {
    private OWLOntology ontology;
    private OWLReasoner reasoner;
    private OWLOntologyManager manager;
    private ILogger logger = new ILogger() {
        public void log(String log) {
            // do nothing
        }

        public void info(String log) {
            // do nothing
        }
    };

    /**
     * Create an instance with ontology read from the given InputStream.
     *
     * @param ontologyInput InputStream with ontology
     */
    public OntologyManager(InputStream ontologyInput) {
        this.manager = OWLManager.createOWLOntologyManager(OWLDataFactoryImpl.getInstance());
        try {
            this.ontology = manager.loadOntologyFromOntologyDocument(ontologyInput);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Can't load ontology", e);
        }

    }

    public OntologyManager(File ontologyFile, ILogger logger) {
        this(ontologyFile);
        this.logger = logger;
    }

    public OntologyManager(File ontologyFile) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager(OWLDataFactoryImpl.getInstance());
        manager.setSilentMissingImportsHandling(true);
        try {
            this.ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Can't load ontology", e);
        }
    }

    public static synchronized OWLOntology saveOntologies(OWLOntologyManager manager, OWLOntology... ontologies) throws OWLOntologyChangeException, OWLOntologyCreationException, OWLOntologyStorageException, IOException, URISyntaxException {
        Set<OWLOntology> ontologySet = new HashSet<OWLOntology>((Arrays.asList(ontologies)));
        File temp = File.createTempFile("ontology", ".owl");
        return manager.createOntology(getIRIForFile(temp), ontologySet);
    }

    private static IRI getIRIForFile(File file) throws URISyntaxException {
        return IRI.create(file);
    }


    /**
     * Loads ontology into Map id -> internal node implementation.
     *
     * @param annotationVisitor To visit annotations.
     * @param propertyVisitors  To visit properties.
     * @return Map ontology's been loaded into.
     */
    public IOntologyGraph load(IClassAnnotationVisitor<IOntologyConcept> annotationVisitor, IPropertyVisitor<IOntologyConcept>... propertyVisitors) throws OWLReasonerException/*, OWLTransformationException*/ {
        Map<IRI, IOntologyConcept> uriToConcept = new HashMap<IRI, IOntologyConcept>();
        Map<IRI, IOntologyProperty> uriToProperty = new HashMap<IRI, IOntologyProperty>();
        IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept = new SetHashTable<String, IOntologyConcept>();
        IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty = new SetHashTable<String, IOntologyProperty>();
        Set<IOntologyConcept> roots = new HashSet<IOntologyConcept>();
        //ReasonerSession session = OWLUtils.getReasonerSession(this.getOntology());
        try {
            OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
            // We'll now create an instance of an OWLReasoner (the implementation being provided by HermiT as
            // we're using the HermiT reasoner factory). The are two categories of reasoner, Buffering and
            // NonBuffering. In our case, we'll create the buffering reasoner, which is the default kind of reasoner.
            // We'll also attach a progress monitor to the reasoner. To do this we set up a configuration that
            // knows about a progress monitor.

            // Create a console progress monitor. This will print the reasoner progress out to the console.
            ReasonerProgressMonitor progressMonitor = new ReasonerProgressMonitor() {
                public void reasonerTaskStarted(String s) {
                    OntologyManager.this.logger.log(s);
                }

                public void reasonerTaskStopped() {
                    OntologyManager.this.logger.log("Stopped");
                }

                public void reasonerTaskProgressChanged(int i, int i1) {
                    OntologyManager.this.logger.log("Reasoner task progress " + i + " " + i1);
                }

                public void reasonerTaskBusy() {
                    OntologyManager.this.logger.log("Busy");
                }
            };
            // Specify the progress monitor via a configuration. We could also specify other setup parameters in
            // the configuration, and different reasoners may accept their own defined parameters this way.
            OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
            // Create a reasoner that will reason over our ontology and its imports closure. Pass in the configuration.
            this.reasoner = reasonerFactory.createReasoner(this.getOntology(), config);

            // Ask the reasoner to do all the necessary work now
            reasoner.prepareReasoner();


            for (OWLClass cls : reasoner.getTopClassNode()) {
                loadClass(cls, annotationVisitor, uriToConcept, labelToConcept, roots, true);
            }
            for (OWLProperty property : reasoner.getTopObjectPropertyNode()) {
                loadProperty(property, uriToProperty, labelToProperty, uriToConcept);
            }
            for (IPropertyVisitor<IOntologyConcept> visitor : propertyVisitors) {
                loadProperties(visitor, uriToConcept);
            }
        } finally {
            if (reasoner != null) {
                reasoner.dispose();
            }
        }
        return new OntologyGraph(roots, uriToConcept, labelToConcept, uriToProperty, labelToProperty);
    }

    /**
     * Loads ontology into Map id -> internal node implementation.
     *
     * @return Map ontology's been loaded into.
     */
    public IOntologyGraph load() throws OWLReasonerException {
        return this.load(new ClassAnnotationVisitor(), new PropertyVisitor());
    }

    private void loadProperty(OWLProperty property, Map<IRI, IOntologyProperty> uriToProperty,
                              IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty,
                              Map<IRI, IOntologyConcept> uriToConcept) {
        if (property.isAnonymous()) {
            return;
        }
        String label = null;
        for (OWLAnnotation annotation : property.getAnnotations(getOntology())) {
            if (annotation instanceof OWLAnnotationImpl) {
                OWLAnnotationImpl mayBeLabel = (OWLAnnotationImpl) annotation;
                if (mayBeLabel.isLabel()) {
                    OWLAnnotationValue value = mayBeLabel.getValue();
                    if (value instanceof OWLLiteral) {
                        label = ((OWLLiteral) value).getLiteral();
                    }
                }
            }
        }
        Set<IOntologyConcept> domains = new HashSet<IOntologyConcept>();
        for (Object domain : property.getDomains(getOntology())) {
            OWLClassExpression domainDescription = (OWLClassExpression) domain;
            for (OWLClass clazz : domainDescription.getClassesInSignature()) {
                IOntologyConcept concept = uriToConcept.get(clazz.getIRI());
                if (concept != null) {
                    domains.add(concept);
                }
            }
        }
        Set<IOntologyConcept> ranges = new HashSet<IOntologyConcept>();
        for (Object domain : property.getRanges(getOntology())) {
            OWLClassExpression rangeDescription = (OWLClassExpression) domain;
            for (OWLClass clazz : rangeDescription.getClassesInSignature()) {
                IOntologyConcept concept = uriToConcept.get(clazz.getIRI());
                if (concept != null) {
                    ranges.add(concept);
                }
            }
        }
        IOntologyProperty ontologyProperty = new OntologyProperty(property.getIRI(), label,
                domains.toArray(new IOntologyConcept[domains.size()]), ranges.toArray(new IOntologyConcept[ranges.size()]),
                property.isFunctional(getOntology()));
        uriToProperty.put(property.getIRI(), ontologyProperty);
        labelToProperty.insert(ontologyProperty.getNormalizedMainLabel(), ontologyProperty);
    }

    /*
     * Finds all classes with the relationship induced by the property the given visitor is interested in
     * and gives them to the visitor
     */

    private void loadProperties(IPropertyVisitor<IOntologyConcept> visitor, Map<IRI, IOntologyConcept> concepts) /*throws OWLTransformationException*/ {

        for (OWLClass clazz : this.getOntology().getClassesInSignature()) {
            IRI uri = clazz.getIRI();
            IOntologyConcept concept = concepts.get(uri);
            /*for (OWLClassAxiom classAxiom : ontology.getAxioms(clazz)) {
                for (OWLClass friend : classAxiom.getClassesInSignature()) {
                    IRI friendIri = friend.getIRI();
                    System.out.println(concept + " + " +  concepts.get(friendIri) + " = " + classAxiom);
                    *//*visitor.inRelationship(concept, concepts.get(friendIri), classAxiom.getNNF());*//*
                }
            }*/
            /*Set<OWLRestriction> owlRestrictions = OWLUtils.keep(this.getOntology(), clazz);
            for (OWLRestriction restriction : owlRestrictions) {
                OWLPropertyExpression property = restriction.getProperty();
                for (OWLClass friend : restriction.getClassesInSignature()) {
                    IRI friendId = friend.getIRI();
                    visitor.inRe lationship(concept, concepts.get(friendId), property);
                }
            }*/
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
                                       Map<IRI, IOntologyConcept> concepts, IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept,
                                       Set<IOntologyConcept> roots, boolean root)
            throws OWLReasonerException {
        if (!this.reasoner.isSatisfiable(clazz) || clazz.equals(OWLDataFactoryImpl.getInstance().getOWLNothing())) {
            return null;
        }
        IRI uri = clazz.getIRI();
        IOntologyConcept concept = concepts.get(uri);
        if (concept == null) {
            if (!clazz.equals(OWLDataFactoryImpl.getInstance().getOWLThing())) {
                annotationVisitor.start();
                for (OWLAnnotation annotation : clazz.getAnnotations(this.getOntology())) {
                    annotation.accept(annotationVisitor);
                }
                concept = annotationVisitor.getOntologyConcept(uri);
                concept.setOWLClass(clazz);
                concepts.put(uri, concept);
                if (root) {
                    roots.add(concept);
                }
                labelToConcept.insert(concept.getNormalizedMainLabel(), concept);
            }
            NodeSet<OWLClass> children = this.reasoner.getSubClasses(clazz, true);
            for (OWLClass child : children.getFlattened()) {
                if (!child.equals(clazz)) {
                    IOntologyConcept childConcept = loadClass(child, annotationVisitor, concepts, labelToConcept, roots, root && concept == null);
                    if (childConcept != null && concept != null) {
                        childConcept.addParent(concept);
                        concept.addChild(childConcept);
                    }
                }
            }
        }
        return concept;
        /*}
        return null;*/
    }

    public static void addEquivalentClasses(OWLOntologyManager manager, OWLOntology ontology, Set<OWLClass> clazzes) throws OWLOntologyChangeException {
        manager.addAxiom(ontology, new OWLEquivalentClassesAxiomImpl(new OWLDataFactoryImpl(), clazzes, Collections.<OWLAnnotation>emptySet()));
    }

    public static void saveResult(OWLOntologyManager manager, OWLOntology ontology, File file) throws OWLOntologyStorageException, URISyntaxException {
        File temp = new File(ontology.getOntologyID().getOntologyIRI().toURI().getPath());
        temp.delete();
        manager.saveOntology(ontology, getOntologyFormatByFile(file), getIRIForFile(file));
    }

    private static OWLOntologyFormat getOntologyFormatByFile(File file) {
        return new DefaultOntologyFormat();
    }

    public OWLOntology getOntology() {
        return ontology;
    }
}

