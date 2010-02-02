package ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;

/**
 * @author Anna Zhukova
 */
public class OntologyRelation implements IOntologyRelation<OntologyConcept> {
    private final String name;
    private final OntologyConcept subject;
    private final OntologyConcept object;

    public OntologyRelation(String name, OntologyConcept subject, OntologyConcept object) {
        this.name = name;
        this.subject = subject;
        this.object = object;
    }


    public OntologyConcept getSubject() {
        return subject;
    }

    public OntologyConcept getObject() {
        return object;
    }

    public String getRelationName() {
        return name;
    }

    public String toString() {
        return String.format("%s :%s: %s", getSubject().getLabel(), getRelationName(), getObject().getLabel());
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
}
