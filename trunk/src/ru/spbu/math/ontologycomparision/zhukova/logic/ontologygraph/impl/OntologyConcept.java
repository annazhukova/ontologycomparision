package ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyConcept implements IOntologyConcept<OntologyConcept, OntologyRelation> {
    private final URI uri;
    private final String label;/*
    private final List<OntologyConcept> children = new ArrayList<OntologyConcept>();*/
    private final Set<OntologyConcept> parents = new LinkedHashSet<OntologyConcept>();/*
    private final List<OntologyRelation> objectRelations = new ArrayList<OntologyRelation>();*/
    private final Set<OntologyRelation> subjectRelations = new LinkedHashSet<OntologyRelation>();

    public OntologyConcept(URI uri, String label) {
        this.uri = uri;
        this.label = (label == null || label.length() == 0) ? this.uri.getFragment().replace("_", " ") : label;
    }

    public URI getUri() {
        return this.uri;
    }

    public String getLabel() {
        return this.label;
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
        return this.parents;
    }

    public void addParent(OntologyConcept parent) {
        getParents().add(parent);
        /*addObjectRelation(
                new OntologyRelation(WordNetRelation.HYPERNYM.getRelatedOntologyConcept(), parent, this));
        *//*addSubjectRelation(
                new OntologyRelation(WordNetRelation.HYPONYM.getRelatedOntologyConcept(), this, parent));*/
    }

    /*public void addObjectRelation(OntologyRelation relation) {
        *//*getObjectRelations().add(relation);*//*
    }*/

    public void addSubjectRelation(OntologyRelation relation) {
        getSubjectRelations().add(relation);
    }

    public String toString() {
        /*StringBuilder builder = new StringBuilder(getLabel());
        builder.append("\t children [");
        for (OntologyConcept child : getChildren()) {
            builder.append(child.getLabel()).append(", ");
        }
        builder.append("]\n");
        for (int i = 0; i < getLabel().length(); i++) {
            builder.append(" ");
        }
        builder.append("\t relations [");
        for (OntologyRelation relation : getRelations()) {
            builder.append(relation).append(", ");
        }
        builder.append("]\n");
        return builder.toString();*/
        return getLabel();
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
}
