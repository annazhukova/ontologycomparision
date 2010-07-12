package ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArc;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.IGraphPane;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphModel implements IGraphModel {
    private final Set<SimpleVertex> simpleVertices = new HashSet<SimpleVertex>();
    private final Set<SuperVertex> superVertices = new HashSet<SuperVertex>();
    private final Set<IArc> arcs = new HashSet<IArc>();
    private Map<?, SuperVertex> nameToSuperVertex;
    private Map<?, SimpleVertex> nameToSimpleVertex;
    private final IGraphPane graphPane;
    private final Set<IVertexListener> listeners = new LinkedHashSet<IVertexListener>();
    private Map<IOntologyConcept, SimpleVertex> conceptToVertexMap;

    private int height;
    private int width;

    public GraphModel(IGraphPane graphPane) {
        this.graphPane = graphPane;
    }

    public void addListener(IVertexListener listener) {
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
        for (IVertexListener listener : this.listeners) {
            listener.vertexAdded(v);
        }
    }

    public void removeVertex(IVertex vertex) {
        vertex.setHidden(true);
        List<IVertex> toRemove = new ArrayList<IVertex>();
        if (vertex instanceof SimpleVertex) {
            this.simpleVertices.remove(vertex);
            toRemove.add(vertex);
        } else if (vertex instanceof SuperVertex) {
            this.superVertices.remove(vertex);
            toRemove.add(vertex);
            toRemove.addAll(((SuperVertex) vertex).getSimpleVertices());
        }
        for (IVertexListener listener : this.listeners) {
            listener.vertexRemoved(toRemove.toArray(new IVertex[toRemove.size()]));
        }
    }

    public void moveVertex(IVertex vertex, int dx, int dy) {
        Point location = vertex.getAbsoluteLocation();
        Point topLeft = new Point(location.x + dx, location.y + dy);
        topLeft = this.graphPane.checkPoint(topLeft);
        vertex.setLocation(topLeft);
        Point bottomRight = vertex.getMaxPoint();
        Point realBottomRight = this.graphPane.checkPoint(bottomRight);
        if (!bottomRight.equals(realBottomRight)) {
            vertex.setLocation(new Point(topLeft.x + realBottomRight.x - bottomRight.x, topLeft.y + realBottomRight.y - bottomRight.y));
        }
        update();
    }

    public SimpleVertex getVertexByConcept(IOntologyConcept concept) {
        return this.conceptToVertexMap.get(concept);
    }

    public void update() {
        for (IVertexListener listener : this.listeners) {
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

    public void setKeyToSuperVertexMap(Map<?, SuperVertex> nameToVertex) {
        this.nameToSuperVertex = nameToVertex;
    }

    public Map<?, SuperVertex> getKeyToSuperVertexMap() {
        return Collections.unmodifiableMap(this.nameToSuperVertex);
    }

    public void setKeyToSimpleVertexMap(Map<?, SimpleVertex> nameToVertex) {
        this.nameToSimpleVertex = nameToVertex;
    }

    public Map<?, SimpleVertex> getNameToSimpleVertexMap() {
        return Collections.unmodifiableMap(this.nameToSimpleVertex);
    }

    public void setConceptToVertexMap(Map<IOntologyConcept, SimpleVertex> conceptToVertexMap) {
        this.conceptToVertexMap = conceptToVertexMap;
    }

    public Map<IOntologyConcept, SimpleVertex> getConceptToVertexMap() {
        return conceptToVertexMap;
    }

    public void showNoParentVertices(boolean show) {
        for (SimpleVertex vertex : getSimpleVertices()) {
            if (vertex.getSuperVertex() == null) {
                vertex.setHidden(!show);
                if (!show) {
                    for (IVertexListener listener : this.listeners) {
                        listener.vertexRemoved(vertex);
                    }
                } else {
                    this.graphPane.checkPoint(vertex.getMaxPoint());
                    for (IVertexListener listener : this.listeners) {
                        listener.vertexAdded(vertex);
                    }
                }
            }
        }
    }

    public void showSingleVerticesWithSuchNamedParent(boolean show, String name) {
        for (SuperVertex vertex : getSuperVertices()) {
            if (vertex.getName().equals(name)) {
                Set<SimpleVertex> vertexSet = vertex.getSimpleVertices();
                if (vertexSet != null && vertexSet.size() <= 1) {
                    vertex.setHidden(!show);
                    if (!show) {
                        for (IVertexListener listener : this.listeners) {
                            List<IVertex> removed = new ArrayList<IVertex>(vertex.getSimpleVertices());
                            removed.add(vertex);
                            listener.vertexRemoved(removed.toArray(new IVertex[removed.size()]));
                        }
                    } else {
                        this.graphPane.checkPoint(vertex.getMaxPoint());
                        for (IVertexListener listener : this.listeners) {
                            List<IVertex> added = new ArrayList<IVertex>(vertex.getSimpleVertices());
                            added.add(vertex);
                            listener.vertexAdded(added.toArray(new IVertex[added.size()]));
                        }
                    }
                }
            }
        }
    }

    public void removeSuperVertex(SuperVertex vertex) {
        for (SimpleVertex simple : vertex.getSimpleVertices()) {
            simple.removeSuperVertex();
        }
        this.superVertices.remove(vertex);
        update();
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}