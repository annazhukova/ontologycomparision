package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.HashMapTable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyConcept extends LabeledOntologyEntity implements IOntologyConcept {
    private HashMapTable<Synset, String> synsetToReason = new HashMapTable<Synset, String>();
    private HashMapTable<IOntologyConcept, String> conceptToReason = new HashMapTable<IOntologyConcept, String>();
    /*
    private final List<IOntologyConcept> children = new ArrayList<IOntologyConcept>();*/
    //private final Set<IOntologyConcept> parents = new LinkedHashSet<IOntologyConcept>();/*
    //private final List<OntologyRelation> objectRelations = new ArrayList<OntologyRelation>();*/
    private final Set<IOntologyRelation> subjectRelations = new LinkedHashSet<IOntologyRelation>();
    private static final URI SUBCLASS_URI;
    private boolean isRoot = true;

    static {
        URI uri = null;
        try {
            uri = new URI("http://www.w3.org/TR/rdf-schema/#ch_subclassof");
        } catch (URISyntaxException e) {
            // never happens
        }
        SUBCLASS_URI = uri;
    }

    public OntologyConcept(URI uri, String label) {
        super(uri, label);
    }

    /*public List<OntologyRelation> getObjectRelations() {
        return this.objectRelations;
    }*/

    public Set<IOntologyRelation> getSubjectRelations() {
        return this.subjectRelations;
    }

    /*public List<OntologyRelation> getRelations() {
        List<OntologyRelation> result = new ArrayList<OntologyRelation>(getSubjectRelations());
        result.addAll(getObjectRelations());
        return result;
    }*/

    /*public List<OntologyRelation> getObjectRelations(String relationName) {
        List<OntologyRelation> result = new ArrayList<OntologyRelation>();
        for (OntologyRelation relation : getObjectRelations()) {
            if (relationName.equalsIgnoreCase(relation.getRelationName())) {
                result.add(relation);
            }
        }
        return result;
    }*/

    public Set<IOntologyRelation> getSubjectRelations(String relationName) {
        Set<IOntologyRelation> result = new LinkedHashSet<IOntologyRelation>();
        for (IOntologyRelation relation : getSubjectRelations()) {
            if (relationName.equalsIgnoreCase(relation.getRelationName())) {
                result.add(relation);
                if (relation.isTransitive()) {
                    result.addAll(relation.getObject().getSubjectRelations(relationName));
                }
            }
        }
        return result;
    }

    /*public List<OntologyRelation> getRelations(String relationName) {
        List<OntologyRelation> result = new ArrayList<OntologyRelation>(
                getSubjectRelations(relationName));
        result.addAll(getObjectRelations(relationName));
        return result;
    }*/

    /*public List<IOntologyConcept> getChildren() {
        return this.children;
    }*/

    /*public void addChild(IOntologyConcept child) {
        getChildren().add(child);
       *//* addSubjectRelation(
                new OntologyRelation(WordNetRelation.HYPERNYM.getRelatedOntologyConcept(), this, child));
        *//**//*addObjectRelation(
                new OntologyRelation(WordNetRelation.HYPONYM.getRelatedOntologyConcept(), child, this));*//*
    }*/

    public Set<IOntologyConcept> getAllParents() {
        Set<IOntologyConcept> result = new LinkedHashSet<IOntologyConcept>(getParents());
        addParentsRecursively(result, getParents());
        return result;
    }

    private static void addParentsRecursively(Set<IOntologyConcept> whereToAdd, Set<IOntologyConcept> whoseParentsToAdd) {
        if (whoseParentsToAdd == null || whoseParentsToAdd.isEmpty()) {
            return;
        }
        Set<IOntologyConcept> parents = new LinkedHashSet<IOntologyConcept>();
        for (IOntologyConcept child : whoseParentsToAdd) {
            parents.addAll(child.getParents());
        }
        whereToAdd.addAll(parents);
        addParentsRecursively(whereToAdd, parents);
    }

    public Set<IOntologyConcept> getParents() {
        Set<IOntologyConcept> result = new LinkedHashSet<IOntologyConcept>();
        for (IOntologyRelation relation : getSubjectRelations(WordNetRelation.HYPONYM.getRelatedOntologyConcept())) {
             result.add(relation.getObject());
        }
        return result;
        //return this.parents;
    }

    public void addParent(IOntologyConcept parent) {
        isRoot = false;
        //getParents().add(parent);
        this.subjectRelations.add(new OntologyRelation(SUBCLASS_URI, WordNetRelation.HYPONYM.getRelatedOntologyConcept(), true, this, parent));
        /*addObjectRelation(
                new OntologyRelation(WordNetRelation.HYPERNYM.getRelatedOntologyConcept(), parent, this));
        *//*addSubjectRelation(
                new OntologyRelation(WordNetRelation.HYPONYM.getRelatedOntologyConcept(), this, parent));*/
    }

    /*public void addObjectRelation(OntologyRelation relation) {
        *//*getObjectRelations().add(relation);*//*
    }*/

    public boolean isRoot() {
        return this.isRoot;
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

    public HashMapTable<Synset, String> getSynsetToReason() {
        return synsetToReason;
    }

    public void addSynset(Synset synset, String reason) {
        this.synsetToReason.insert(synset, reason);
    }

    public HashMapTable<IOntologyConcept, String> getConceptToReason() {
        return conceptToReason;
    }

    public void addConcept(IOntologyConcept concept, String reason) {
        this.conceptToReason.insert(concept, reason);
    }

    public void setIsRoot(boolean root) {
        isRoot = root;
    }

    public boolean hasMappedConcepts() {
        return !this.conceptToReason.isEmpty();
    }
}
