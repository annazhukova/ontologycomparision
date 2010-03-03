package ru.spbu.math.ontologycomparison.zhukova.logic.wordnet;

/**
 * @author Anna Zhukova
 */
public enum WordNetRelation {

    HYPERNYM("superClassOf", true) {
        public WordNetRelation inversed() {
            return HYPONYM;
        }},
    HYPONYM("rdfs:subClassOf", true) {
        public WordNetRelation inversed() {
            return HYPERNYM;
        }},
    MERONYM("part_of", true) {
        public WordNetRelation inversed() {
            return HOLONYM;
        }},
    HOLONYM("hasPart", true) {
        public WordNetRelation inversed() {
            return MERONYM;
        }};

    private final String relatedOntologyConcept;
    private final boolean isTransitive;

    private WordNetRelation(String relatedOntologyConcept, boolean isTransitive) {
        this.relatedOntologyConcept = relatedOntologyConcept;
        this.isTransitive = isTransitive;
    }

    public String toString() {
        return String.format("%s[%s]", this.name(), this.getRelatedOntologyConcept());
    }

    public boolean isTransitive() {
        return this.isTransitive;
    }

    public abstract WordNetRelation inversed();

    public String getRelatedOntologyConcept() {
        return this.relatedOntologyConcept;
    }
}
