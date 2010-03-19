package ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArc;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.IGraphPane;

import java.awt.*;
import java.util.*;

public class GraphModel implements IGraphModel {
    private final Set<SimpleVertex> simpleVertices = new HashSet<SimpleVertex>();
    private final Set<SuperVertex> superVertices = new HashSet<SuperVertex>();
    private final Set<IArc> arcs = new HashSet<IArc>();
    private Map<String, SuperVertex> nameToSuperVertex = new HashMap<String, SuperVertex>();
    private Map<String, SimpleVertex> nameToSimpleVertex = new HashMap<String, SimpleVertex>();
    private final IGraphPane graphPane;
    private final Set<Listener> listeners = new LinkedHashSet<Listener>();
    private Map<IOntologyConcept, SimpleVertex> conceptToVertexMap;

    public GraphModel(IGraphPane graphPane) {
        this.graphPane = graphPane;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void addVertex(IVertex v) {
        v.setHidden(false);
        if (v instanceof SimpleVertex) {
            this.simpleVertices.add((SimpleVertex) v);
        } else if (v instanceof SuperVertex) {
            this.superVertices.add((SuperVertex) v);
        }
        this.graphPane.checkPoint(v.getMaxPoint());
        update();
    }

    public LinkedList<IArc> removeVertex(IVertex vertex) {
        LinkedList<IArc> arcs = new LinkedList<IArc>();
        vertex.setHidden(true);
        if (vertex instanceof SimpleVertex) {
            this.simpleVertices.remove(vertex);
        } else if (vertex instanceof SuperVertex) {
            this.superVertices.remove(vertex);
        }
        for (IArc arc : this.arcs) {
            if (vertex.equals(arc.getFromVertex()) || vertex.equals(arc.getToVertex())) {
                arcs.add(arc);
            }
        }
        for (Listener listener : this.listeners) {
            listener.update(vertex);
        }
        return arcs;
    }

    public void moveVertex(IVertex vertex, int dx, int dy) {
        Point location = vertex.getAbsoluteLocation();
        vertex.setLocation(new Point(location.x + dx, location.y + dy));
        this.graphPane.checkPoint(vertex.getMaxPoint());
        update();
    }

    public SimpleVertex getVertexByConcept(IOntologyConcept concept) {
        return this.conceptToVertexMap.get(concept);
    }

    public void update() {
        for (Listener listener : this.listeners) {
            listener.update();
        }
    }

    public void addArc(IArc arc) {
        this.arcs.add(arc);
        update();
    }

    public void removeArc(IArc arc) {
        this.arcs.remove(arc);
        update();
    }

    public Set<SimpleVertex> getSimpleVertices() {
        return Collections.unmodifiableSet(this.simpleVertices);
    }

    public Set<SuperVertex> getSuperVertices() {
        return Collections.unmodifiableSet(this.superVertices);
    }

    public Set<IArc> getArcs() {
        return Collections.unmodifiableSet(this.arcs);
    }

    public void clear() {
        this.simpleVertices.clear();
        this.superVertices.clear();
        this.arcs.clear();
        update();
    }

    public void setKeyToSuperVertexMap(Map<String, SuperVertex> nameToVertex) {
        this.nameToSuperVertex = nameToVertex;
    }

    public Map<String, SuperVertex> getKeyToSuperVertexMap() {
        return Collections.unmodifiableMap(this.nameToSuperVertex);
    }

    public void setIntToSimpleVertexMap(Map<String, SimpleVertex> nameToVertex) {
        this.nameToSimpleVertex = nameToVertex;
    }

    public Map<String, SimpleVertex> getNameToSimpleVertexMap() {
        return Collections.unmodifiableMap(this.nameToSimpleVertex);
    }

    public void setConceptToVertexMap(Map<IOntologyConcept, SimpleVertex> conceptToVertexMap) {
        this.conceptToVertexMap = conceptToVertexMap;
    }

    public Map<IOntologyConcept, SimpleVertex> getConceptToVertexMap() {
        return conceptToVertexMap;
    }

    public static interface Listener {

        void update();

        void update(IVertex vertex);
    }
}