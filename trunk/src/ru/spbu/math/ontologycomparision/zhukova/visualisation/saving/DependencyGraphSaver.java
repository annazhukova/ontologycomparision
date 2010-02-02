package ru.spbu.math.ontologycomparision.zhukova.visualisation.saving;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.Arc;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SimpleVertex;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.util.*;

import com.sun.xml.internal.stream.writers.XMLStreamWriterImpl;

/**
 * @author Anna R. Zhukova
 */
public class DependencyGraphSaver {

    public static void save(IGraphModel graphmodel, File f) throws XMLStreamException, IOException {
        XMLStreamWriter out = null;
        try {
            out = new XMLStreamWriterImpl(new FileWriter(f), null);
            out.writeStartDocument();
            out.writeStartElement("GraphModel");
            saveVertices(graphmodel, out);
            saveArcFilter(out);
            saveArcs(graphmodel, out);
            out.writeEndElement();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void saveArcFilter(XMLStreamWriter out) throws XMLStreamException {
        out.writeStartElement("ArcFilter");
        for (String barrier : Arc.getArcFilter().getBarriers()) {
            out.writeStartElement("barrier");
            out.writeAttribute("name", barrier);
            out.writeEndElement();
        }
        out.writeEndElement();
    }

    private static void saveVertices(IGraphModel graphmodel, XMLStreamWriter out) throws  XMLStreamException {
        out.writeStartElement("Vertices");
        for (SuperVertex v : graphmodel.getSuperVertices()) {
            out.writeStartElement("superVertex");
            saveVertex(v, out);
            for (SimpleVertex sv : v.getSimpleVertices()) {
                out.writeStartElement("simpleVertex");
                saveVertex(sv, out);
                out.writeEndElement();
            }
            out.writeEndElement();
        }
        out.writeEndElement();
    }

    private static void saveVertex(IVertex v, XMLStreamWriter out) throws XMLStreamException {
        Point location = v.getAbsoluteLocation();
        out.writeAttribute("x", String.valueOf(location.x));
        out.writeAttribute("y", String.valueOf(location.y));
        out.writeAttribute("name", v.getName());
        out.writeAttribute("width", String.valueOf(v.getWidth()));
        out.writeAttribute("height", String.valueOf(v.getHeight()));
        out.writeAttribute("letterWidth", String.valueOf(v.getLetterWidth()));
        out.writeAttribute("letterHeight", String.valueOf(v.getLetterHeight()));
        out.writeAttribute("isHidden", String.valueOf(v.isHidden()));
        saveFont(out, v.getFont());
    }

    private static void saveFont(XMLStreamWriter out, Font font) throws XMLStreamException {
        out.writeStartElement("Font");
        out.writeAttribute("name", font.getName());
        out.writeAttribute("style", String.valueOf(font.getStyle()));
        out.writeAttribute("size", String.valueOf(font.getSize()));
        out.writeEndElement();
    }

    private static void saveArcs(IGraphModel graphmodel, XMLStreamWriter out) throws IOException, XMLStreamException {
        out.writeStartElement("Arcs");
        Set<IArc> arcs = graphmodel.getArcs();
        for (IArc arc : arcs) {
            out.writeStartElement("ar");
            out.writeStartElement("from");
            out.writeAttribute("name", arc.getFromVertex().getName());
            out.writeAttribute("surname", arc.getFromVertex().getSurname());
            out.writeEndElement();
            out.writeStartElement("to");
            out.writeAttribute("name", arc.getToVertex().getName());
            out.writeAttribute("surname", arc.getToVertex().getSurname());
            out.writeEndElement();
            saveArcLabel(out, arc.getLabels());
            out.writeEndElement();
        }
        out.writeEndElement();
    }

    private static void saveArcLabel(XMLStreamWriter out, java.util.List<String> label) throws  XMLStreamException {
        out.writeStartElement("Label");
        for (String s : label) {
            out.writeStartElement("pr");
            out.writeAttribute("name", s);
            out.writeEndElement();
        }
        out.writeEndElement();
    }
}
