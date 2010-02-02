package ru.spbu.math.ontologycomparision.zhukova.visualisation.reading;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.io.File;
import java.io.IOException;

import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.IGraphPane;

/**
 * @author Anna R. Zhukova
 */
public class DependencyGraphReader {

    public static GraphModel read(IGraphPane graphPane, File f) throws ParserConfigurationException, SAXException, IOException {
        GraphModel graphModel = new GraphModel(graphPane);
        SAXParser xmlprsr = SAXParserFactoryImpl.newInstance().newSAXParser();
        ParserHandler parserHandler = new ParserHandler(graphModel);
        xmlprsr.parse(f, parserHandler);
        return graphModel;
    }
}
