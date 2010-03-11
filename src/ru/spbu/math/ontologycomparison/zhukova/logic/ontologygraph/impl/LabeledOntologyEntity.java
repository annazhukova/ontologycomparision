package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.ILabeledOntologyEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public abstract class LabeledOntologyEntity implements ILabeledOntologyEntity {
    private static final String EMPTY_STRING = "";
    private final URI uri;
    private final String[] labels;

    public LabeledOntologyEntity(URI uri, String label) {
        this.uri = uri;
        Set<String> labels = new LinkedHashSet<String>();
        if (label != null && !label.isEmpty()) {
            labels.add(label);
        }
        if (uri != null && uri.getFragment() != null) {
            labels.add(this.uri.getFragment());
        }
        this.labels = labels.toArray(new String[labels.size()]);
    }

    public URI getUri() {
        return this.uri;
    }

    public String[] getLabels() {
        return this.labels;
    }

    public Collection<String> getLabelCollection() {
        return Arrays.asList(getLabels());
    }

    public String getNormalizedMainLabel() {
        return normalizeString(getMainLabel());
    }

    public String getMainLabel() {
        return getLabels().length > 0 ? getLabels()[0] : EMPTY_STRING;
    }

    public static String normalizeString(String source) {
        // todo: manage CamelCase
        return source.toLowerCase().replace("_", " ").replace("-", " ").replace("\\", "/").trim();
    }

    public String toString() {
        return getLabelCollection().toString();
    }
}
