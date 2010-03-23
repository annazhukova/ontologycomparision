package ru.spbu.math.ontologycomparison.zhukova.visualisation.model;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;

import java.util.Map;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public interface IGraphModel {

    void addVertex(IVertex v);

    void removeVertex(IVertex v);

    void moveVertex(IVertex v, int dx, int dy);

    void addArc(IArc arc);

    void removeArc(IArc arc);

    Set<SimpleVertex> getSimpleVertices();

    Set<SuperVertex> getSuperVertices();

    Set<IArc> getArcs();

    void clear();

    void setKeyToSuperVertexMap(Map<?, SuperVertex> nameToVertex);

    Map<?, SuperVertex> getKeyToSuperVertexMap();

    void setKeyToSimpleVertexMap(Map<?, SimpleVertex> nameToVertex);

    Map<?, SimpleVertex> getNameToSimpleVertexMap();

    void setConceptToVertexMap(Map<IOntologyConcept, SimpleVertex> conceptToVertexMap);

    Map<IOntologyConcept, SimpleVertex> getConceptToVertexMap();

    void update();

    SimpleVertex getVertexByConcept(IOntologyConcept concept);

    void showNoParentVertices(boolean show);

    void showSingleVerticesWithSuchNamedParent(boolean show, String name);
}
