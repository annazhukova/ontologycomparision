package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyRelation;

import java.net.URI;

/**
 * @author Anna Zhukova
 */
public class OntologyRelation implements IOntologyRelation {
    private final URI uri;
    private final String name;
    private final IOntologyConcept subject;
    private final IOntologyConcept object;
    private final boolean isTransitive;

    public OntologyRelation(URI uri, String name, boolean isTransitive, IOntologyConcept subject, IOntologyConcept object) {
        this.uri = uri;
        this.name = name;
        this.subject = subject;
        this.object = object;
        this.isTransitive = isTransitive;
    }


    public IOntologyConcept getSubject() {
        return subject;
    }

    public IOntologyConcept getObject() {
        return object;
    }

    public String getRelationName() {
        return name;
    }

    public String toString() {
        return String.format("%s :%s: %s", getSubject().getLabels(), getRelationName(), getObject().getLabels());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ! (o instanceof OntologyRelation)) {
            return false;
        }
        OntologyRelation anotherRelation = (OntologyRelation) o;
        if (this.getRelationName() == null ?
                anotherRelation.getRelationName() != null :
                !this.getRelationName().equalsIgnoreCase(anotherRelation.getRelationName())){
            return false;
        }
        return this.getSubject().equals(anotherRelation.getSubject()) &&
                this.getObject().equals(anotherRelation.getObject());
    }

    public int hashCode() {
        int result = this.getRelationName() != null ? this.getRelationName().hashCode() : 0;
        result += 13 * this.getSubject().hashCode();
        result += 13 * this.getObject().hashCode();
        return result;
    }

    public boolean isTransitive() {
        return isTransitive;
    }

    public URI getUri() {
        return uri;
    }
}
