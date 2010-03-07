package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.HashMapTable;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public class OntologyProperty extends LabeledOntologyEntity implements IOntologyProperty {
    private final IOntologyConcept[] domains;
    private final IOntologyConcept[] ranges;
    private final boolean isFunctional;
    private HashMapTable<IOntologyProperty, String> propertyToReason = new HashMapTable<IOntologyProperty, String>();

    public OntologyProperty(URI uri, String label, IOntologyConcept[] domains, IOntologyConcept[] ranges, boolean functional) {
        super(uri, label);
        isFunctional = functional;
        this.domains = domains;
        this.ranges = ranges;
    }

    public int hashCode() {
        return getUri().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ! (o instanceof IOntologyProperty)) {
            return false;
        }
        return this.getUri().equals(((IOntologyProperty) o).getUri());
    }

    public Collection<IOntologyConcept> getDomains() {
        return Arrays.asList(domains);
    }

    public Collection<IOntologyConcept> getRanges() {
        return Arrays.asList(ranges);
    }

    public boolean isFunctional() {
        return isFunctional;
    }

    public void addProperty(IOntologyProperty property, String reason) {
        propertyToReason.insert(property, reason);
    }
}
