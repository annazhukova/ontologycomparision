package ru.spbu.math.ontologycomparision.zhukova.visualisation.reading;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;
import java.util.List;
import java.awt.*;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.Arc;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.ArcFilter;

/**
 * @author Anna R. Zhukova
 */
class ParserHandler extends DefaultHandler {
    private final IGraphModel graphModel;
    private final Map<String, SimpleVertex> nameToSimpleVertex = new HashMap<String, SimpleVertex>();
    private final Map<String, SuperVertex> nameToSuperVertex = new HashMap<String, SuperVertex>();
    private SimpleVertex fromSimpleVertex;
    private SimpleVertex toSimpleVertex;
    private List<String> label;
    private SuperVertex superVertex;
    private IVertex vertex;
    private IArcFilter arcFilter;

    public ParserHandler(IGraphModel graphModel) {
        this.graphModel = graphModel;
        this.arcFilter = Arc.getArcFilter();
        if (this.arcFilter == null) {
            this.arcFilter = new ArcFilter();
            Arc.setArcFilter(this.arcFilter);
        }
        arcFilter.removeAllBarriers();
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        if ("superVertex".equals(qName)) {
            String name = attributes.getValue("name");
            SuperVertex v = new SuperVertex(new Point(Integer.parseInt(attributes.getValue("x")),
                    Integer.parseInt(attributes.getValue("y"))), name);
            v.setWidth(Integer.parseInt(attributes.getValue("width")));
            v.setHeight(Integer.parseInt(attributes.getValue("height")));
            v.setHidden(Boolean.parseBoolean(attributes.getValue("isHidden")));
            v.setLetterWidth(Integer.parseInt(attributes.getValue("letterWidth")));
            v.setLetterHeight(Integer.parseInt(attributes.getValue("letterHeight")));
            this.nameToSuperVertex.put(name, v);
            this.graphModel.addVertex(v);
            this.superVertex = v;
            this.vertex = v;
        } else if ("simpleVertex".equals(qName)) {
            String name = attributes.getValue("name");
            SimpleVertex v = new SimpleVertex(new Point(Integer.parseInt(attributes.getValue("x")),
                    Integer.parseInt(attributes.getValue("y"))), name, this.superVertex, Color.BLACK);
            v.setWidth(Integer.parseInt(attributes.getValue("width")));
            v.setHeight(Integer.parseInt(attributes.getValue("height")));
            v.setHidden(Boolean.parseBoolean(attributes.getValue("isHidden")));
            v.setLetterWidth(Integer.parseInt(attributes.getValue("letterWidth")));
            v.setLetterHeight(Integer.parseInt(attributes.getValue("letterHeight")));
            this.nameToSimpleVertex.put(this.superVertex.getName() + "." + name, v);
            this.graphModel.addVertex(v);
            this.superVertex.addSimpleVertex(v);
            this.vertex = v;
        } else if ("from".equals(qName)) {
            this.fromSimpleVertex = this.nameToSimpleVertex.get(attributes.getValue("surname") + "." + attributes.getValue("name"));
        } else if ("to".equals(qName)) {
            this.toSimpleVertex = this.nameToSimpleVertex.get(attributes.getValue("surname") + "." + attributes.getValue("name"));
        } else if ("Label".equals(qName)) {
            this.label = new ArrayList<String>();
        } else if ("Entry".equals(qName)) {
            //Arc.myTypeToShow.put(attributes.getValue("key"), Boolean.valueOf(attributes.getValue("value")));
        } else if ("Font".equals(qName)) {
            this.vertex.setFont(new Font(attributes.getValue("name"), Integer.valueOf(attributes.getValue("style")),
                    Integer.valueOf(attributes.getValue("size"))));
        } else if ("barrier".equals(qName)) {
            this.arcFilter.addBarrier(attributes.getValue("name"));
        } else if ("pr".equals(qName)) {
            this.label.add(attributes.getValue("name"));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("ar".equals(qName)) {
            Arc arc = new Arc(this.fromSimpleVertex, this.toSimpleVertex, this.label);
            this.graphModel.addArc(arc);
        }
    }

    public void endDocument ()	throws SAXException {
        this.graphModel.setIntToSimpleVertexMap(this.nameToSimpleVertex);
        this.graphModel.setIntToSuperVertexMap(this.nameToSuperVertex);
    }
}
