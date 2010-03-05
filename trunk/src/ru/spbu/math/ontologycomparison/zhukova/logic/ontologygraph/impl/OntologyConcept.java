package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.HashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Anna Zhukova
 */
public class OntologyConcept implements IOntologyConcept<OntologyConcept, OntologyRelation> {
    private final URI uri;
    private final String[] labels;
    private IHashTable<Synset, String, List<String>> synsetToReason = new HashTable<Synset, String, List<String>>() {
        @Override
        public List<String> newCollection() {
            return new ArrayList<String>();
        }
    };
    private IHashTable<OntologyConcept, String, List<String>> conceptToReason = new HashTable<OntologyConcept, String, List<String>>() {
        @Override
        public List<String> newCollection() {
            return new ArrayList<String>();
        }
    };
    /*
    private final List<OntologyConcept> children = new ArrayList<OntologyConcept>();*/
    //private final Set<OntologyConcept> parents = new LinkedHashSet<OntologyConcept>();/*
    //private final List<OntologyRelation> objectRelations = new ArrayList<OntologyRelation>();*/
    private final Set<OntologyRelation> subjectRelations = new LinkedHashSet<OntologyRelation>();
    private static final URI SUBCLASS_URI;
    static {
        URI uri = null;
        try {
            uri = new URI("http://www.w3.org/TR/rdf-schema/#ch_subclassof");
        } catch (URISyntaxException e) {
            // never happens
        }
        SUBCLASS_URI = uri;
    }

    public OntologyConcept(URI uri, String label, String comment) {
        this.uri = uri;
        Set<String> labels = new LinkedHashSet<String>();
        if (label != null && !label.isEmpty()) {
            labels.add(label);
        }
        if (uri != null && uri.getFragment() != null) {
            labels.add(this.uri.getFragment().replace("_", " "));
        }
        /*if (comment != null && !comment.isEmpty()) {
            labels.add(comment);
        }*/
        this.labels = labels.toArray(new String[labels.size()]);
    }

    public URI getUri() {
        return this.uri;
    }

    public String[] getLabels() {
        return this.labels;
    }

    public Collection<String> getLabelCollection() {
        return Arrays.asList(this.labels);
    }

    /*public List<OntologyRelation> getObjectRelations() {
        return this.objectRelations;
    }*/

    public Set<OntologyRelation> getSubjectRelations() {
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

    public Set<OntologyRelation> getSubjectRelations(String relationName) {
        Set<OntologyRelation> result = new LinkedHashSet<OntologyRelation>();
        for (OntologyRelation relation : getSubjectRelations()) {
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

    /*public List<OntologyConcept> getChildren() {
        return this.children;
    }*/

    /*public void addChild(OntologyConcept child) {
        getChildren().add(child);
       *//* addSubjectRelation(
                new OntologyRelation(WordNetRelation.HYPERNYM.getRelatedOntologyConcept(), this, child));
        *//**//*addObjectRelation(
                new OntologyRelation(WordNetRelation.HYPONYM.getRelatedOntologyConcept(), child, this));*//*
    }*/

    public Set<OntologyConcept> getAllParents() {
        Set<OntologyConcept> result = new LinkedHashSet<OntologyConcept>(getParents());
        addParentsRecursively(result, getParents());
        return result;
    }

    private void addParentsRecursively(Set<OntologyConcept> whereToAdd, Set<OntologyConcept> whoseParentsToAdd) {
        if (whoseParentsToAdd == null || whoseParentsToAdd.isEmpty()) {
            return;
        }
        Set<OntologyConcept> parents = new LinkedHashSet<OntologyConcept>();
        for (OntologyConcept child : whoseParentsToAdd) {
            parents.addAll(child.getParents());
        }
        whereToAdd.addAll(parents);
        addParentsRecursively(whereToAdd, parents);
    }

    public Set<OntologyConcept> getParents() {
        Set<OntologyConcept> result = new LinkedHashSet<OntologyConcept>();
        for (OntologyRelation relation : getSubjectRelations(WordNetRelation.HYPONYM.getRelatedOntologyConcept())) {
             result.add(relation.getObject());
        }
        return result;
        //return this.parents;
    }

    public void addParent(OntologyConcept parent) {
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

    public String getMainLabel() {
        return normalizeString(getLabels()[0]);
    }

    public static String normalizeString(String source) {
        // todo: manage CamelCase
        return source.toLowerCase().replace("_", " ").replace("-", " ").replace("\\", "/").trim();
    }

    public Collection<OntologyConcept> getSimilarConcepts() {
        return this.conceptToReason.keySet();
    }

    public void addSubjectRelation(OntologyRelation relation) {
        getSubjectRelations().add(relation);
    }

    public String toString() {
        /*StringBuilder builder = new StringBuilder(getLabels());
        builder.append("\t children [");
        for (OntologyConcept child : getChildren()) {
            builder.append(child.getLabels()).append(", ");
        }
        builder.append("]\n");
        for (int i = 0; i < getLabels().length(); i++) {
            builder.append(" ");
        }
        builder.append("\t relations [");
        for (OntologyRelation relation : getRelations()) {
            builder.append(relation).append(", ");
        }
        builder.append("]\n");
        return builder.toString();*/
        return getLabelCollection().toString();
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

    public IHashTable<Synset, String, List<String>> getSynsetToReason() {
        return synsetToReason;
    }

    public void addSynset(Synset synset, String reason) {
        this.synsetToReason.insert(synset, reason);
    }

    public IHashTable<OntologyConcept, String, List<String>> getConceptToReason() {
        return conceptToReason;
    }

    public void addConcept(OntologyConcept concept, String reason) {
        this.conceptToReason.insert(concept, reason);
    }
}
