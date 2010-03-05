package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.ILabeledEntity;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyProperty implements ILabeledEntity {
    private final URI uri;
    private final String[] labels;
    private final IOntologyConcept[] domains;
    private final IOntologyConcept[] ranges;

    public OntologyProperty(URI uri, String label, IOntologyConcept[] domains, IOntologyConcept[] ranges) {
        this.uri = uri;
        Set<String> labels = new LinkedHashSet<String>();
        if (label != null && !label.isEmpty()) {
            labels.add(label);
        }
        if (uri != null && uri.getFragment() != null) {
            labels.add(this.uri.getFragment().replace("_", " "));
        }
        this.labels = labels.toArray(new String[labels.size()]);
        this.domains = domains;
        this.ranges = ranges;
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
        if (o == null || ! (o instanceof OntologyProperty)) {
            return false;
        }
        return this.getUri().equals(((OntologyProperty) o).getUri());
    }

    public Collection<IOntologyConcept> getDomains() {
        return Arrays.asList(domains);
    }

    public Collection<IOntologyConcept> getRanges() {
        return Arrays.asList(ranges);
    }
}
