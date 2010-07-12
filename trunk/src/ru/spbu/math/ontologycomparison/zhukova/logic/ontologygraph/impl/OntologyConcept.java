package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import edu.smu.tspell.wordnet.Synset;
import org.semanticweb.owl.model.OWLClass;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashMapTable;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.RecursiveAddHelper;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.HashMapTable;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyConcept extends LabeledOntologyEntity implements IOntologyConcept {
    private HashMapTable<Synset, String> synsetToReason = new HashMapTable<Synset, String>();
    private HashMapTable<IOntologyConcept, String> conceptToReason = new HashMapTable<IOntologyConcept, String>();
    private final Set<IOntologyConcept> parents = new HashSet<IOntologyConcept>();
    private final Set<IOntologyConcept> children = new HashSet<IOntologyConcept>();
    private final IHashTable<String, IOntologyRelation, Set<IOntologyRelation>> labelToSubjectRelation = new SetHashTable<String, IOntologyRelation>();
    private boolean isRoot = true;
    private int depth = 0;
    private static final RecursiveAddHelper<IOntologyConcept> RECURSIVE_ADD_HELPER = new RecursiveAddHelper<IOntologyConcept>();
    private static final RecursiveAddHelper.ElementsToAddExtractor<IOntologyConcept> EXTRACTOR = new RecursiveAddHelper.ElementsToAddExtractor<IOntologyConcept>() {
        public Collection<IOntologyConcept> extract(IOntologyConcept element) {
            return element.getParents();
        }
    };
    private static final int MAX_RECURSIVE_LEVEL = 5;
    private static final int MAX_RECURSIVE_SIZE = 20;
    private OWLClass clazz;
    /*private static final URI SUBCLASS_URI;

    static {
        URI uri = null;
        try {
            uri = new URI("http://www.w3.org/TR/rdf-schema/#ch_subclassof");
        } catch (URISyntaxException e) {
            // never happens
        }
        SUBCLASS_URI = uri;
    }*/

    public OntologyConcept(URI uri, String label, int depth) {
        super(uri, label);
        this.depth = depth;
    }

    public OntologyConcept(URI uri, String label) {
        this(uri, label, 0);
    }

    public Set<IOntologyRelation> getSubjectRelations() {
        return this.labelToSubjectRelation.allValues();
    }

    public Set<IOntologyRelation> getSubjectRelations(String label) {
        return this.labelToSubjectRelation.get(label);
    }

    public Set<IOntologyConcept> getParents() {
        /*Set<IOntologyConcept> result = new LinkedHashSet<IOntologyConcept>();
        for (IOntologyRelation relation : this.labelToSubjectRelation.get(WordNetRelation.HYPONYM.getRelatedOntologyConcept())) {
            result.add(relation.getObject());
        }
        return result;*/
        return this.parents;
    }

    public void addParent(IOntologyConcept parent) {
        parents.add(parent);
        isRoot = false;
        /*this.labelToSubjectRelation.insert(WordNetRelation.HYPONYM.getRelatedOntologyConcept(), new OntologyRelation(SUBCLASS_URI, WordNetRelation.HYPONYM.getRelatedOntologyConcept(), true, this, parent));*/
    }

    public Set<IOntologyConcept> getParentsRecursively() {
        Set<IOntologyConcept> result = new LinkedHashSet<IOntologyConcept>(getParents());
        RECURSIVE_ADD_HELPER.addRecursively(result, getParents(), EXTRACTOR, MAX_RECURSIVE_LEVEL, MAX_RECURSIVE_SIZE);
        return result;
    }

    public Collection<IOntologyConcept> getSimilarConcepts() {
        return this.conceptToReason.keySet();
    }

    public void addSubjectRelation(IOntologyRelation relation) {
        getSubjectRelations().add(relation);
    }    

    public int hashCode() {
        return getUri().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ! (o instanceof OntologyConcept)) {
            return false;
        }
        return this.getUri().equals(((OntologyConcept) o).getUri());
    }

    public IHashMapTable<Synset, String> getSynsetToReason() {
        return synsetToReason;
    }

    public void addSynset(Synset synset, String reason, int count) {
        this.synsetToReason.insert(synset, reason, count);
    }

    public IHashMapTable<IOntologyConcept, String> getConceptToReason() {
        return conceptToReason;
    }

    public void addConcept(IOntologyConcept concept, String reason, int count) {
        this.conceptToReason.insert(concept, reason, count);
    }

    public boolean hasMappedConcepts() {
        return !this.conceptToReason.isEmpty();
    }

    public IOntologyConcept[] getChildren() {
        return children.toArray(new IOntologyConcept[children.size()]);
    }

    public void setOWLClass(OWLClass clazz) {
        this.clazz = clazz;
    }

    public boolean hasSynsets() {
        return !this.synsetToReason.isEmpty();
    }

    public void addChild(IOntologyConcept child) {
        this.children.add(child);
    }

    public OWLClass getOWLClass() {
        return clazz;
    }
}
