package ru.spbu.math.ontologycomparision.zhukova.visualisation.model;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SuperVertex;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;

/**
 * @author Anna R. Zhukova
 */
public interface IGraphModel {

    void addVertex(IVertex v);

    LinkedList<IArc> removeVertex(IVertex v);

    void moveVertex(IVertex v, int dx, int dy);

    void addArc(IArc arc);

    void removeArc(IArc arc);

    Set<IVertex> getVertices();

    Set<SimpleVertex> getSimpleVertices();

    Set<SuperVertex> getSuperVertices();

    Set<IArc> getArcs();

    void clear();

    void setIntToSuperVertexMap(Map<String, SuperVertex> nameToVertex);

    Map<String, SuperVertex> getNameToSuperVertexMap();

    void setIntToSimpleVertexMap(Map<String, SimpleVertex> nameToVertex);

    Map<String, SimpleVertex> getNameToSimpleVertexMap();
}
