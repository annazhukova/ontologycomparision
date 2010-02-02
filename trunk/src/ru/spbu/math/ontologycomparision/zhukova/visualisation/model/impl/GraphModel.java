package ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.IGraphPane;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IArc;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class GraphModel extends Observable implements IGraphModel {
    private final Set<IVertex> vertices = new HashSet<IVertex>();
    private final Set<SimpleVertex> simpleVertices = new HashSet<SimpleVertex>();
    private final Set<SuperVertex> superVertices = new HashSet<SuperVertex>();
    private final Set<IArc> arcs = new HashSet<IArc>();
    private Map<String, SuperVertex> nameToSuperVertex = new HashMap<String, SuperVertex>();
    private Map<String, SimpleVertex> nameToSimpleVertex = new HashMap<String, SimpleVertex>();
    private final IGraphPane graphPane;

    public GraphModel(IGraphPane graphPane) {
        this.graphPane = graphPane;
    }

    public void addVertex(IVertex v) {
        this.vertices.add(v);
        v.setHidden(false);
        if (v instanceof SimpleVertex) {
            this.simpleVertices.add((SimpleVertex) v);
        } else if (v instanceof SuperVertex) {
            this.superVertices.add((SuperVertex) v);
        }
        this.graphPane.checkPoint(v.getMaxPoint());
        setChanged();
        notifyObservers();
    }

    public LinkedList<IArc> removeVertex(IVertex vertex) {
        LinkedList<IArc> arcs = new LinkedList<IArc>();
        this.vertices.remove(vertex);
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
        setChanged();
        notifyObservers(vertex);
        return arcs;
    }

    public void moveVertex(IVertex vertex, int dx, int dy) {
        Point location = vertex.getAbsoluteLocation();
        vertex.setLocation(new Point(location.x + dx, location.y + dy));
        this.graphPane.checkPoint(vertex.getMaxPoint());
        setChanged();
        notifyObservers();
    }

    public void addArc(IArc arc) {
        this.arcs.add(arc);
        setChanged();
        notifyObservers();
    }

    public void removeArc(IArc arc) {
        this.arcs.remove(arc);
        setChanged();
        notifyObservers();
    }

    public Set<IVertex> getVertices() {
        return Collections.unmodifiableSet(this.vertices);
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
        this.vertices.clear();
        this.simpleVertices.clear();
        this.superVertices.clear();
        this.arcs.clear();
    }

    public void setIntToSuperVertexMap(Map<String, SuperVertex> nameToVertex) {
        this.nameToSuperVertex = nameToVertex;
    }

    public Map<String, SuperVertex> getNameToSuperVertexMap() {
        return Collections.unmodifiableMap(this.nameToSuperVertex);
    }

    public void setIntToSimpleVertexMap(Map<String, SimpleVertex> nameToVertex) {
        this.nameToSimpleVertex = nameToVertex;
    }

    public Map<String, SimpleVertex> getNameToSimpleVertexMap() {
        return Collections.unmodifiableMap(this.nameToSimpleVertex);
    }
}