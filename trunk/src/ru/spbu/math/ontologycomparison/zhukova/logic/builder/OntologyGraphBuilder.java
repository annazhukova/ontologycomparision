package ru.spbu.math.ontologycomparison.zhukova.logic.builder;

import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.OntologyLoader;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.PropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IMapStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class OntologyGraphBuilder {

    public static IMapStore build(String ontologyPath)
            throws FileNotFoundException {
        OntologyLoader ontologyLoader = new OntologyLoader(new FileInputStream(ontologyPath));
        return ontologyLoader.load(new ClassAnnotationVisitor(), new PropertyVisitor());
    }

    public static IMapStore build(File ontologyFile)
            throws FileNotFoundException {
        OntologyLoader ontologyLoader = new OntologyLoader(ontologyFile);
        return ontologyLoader.load(new ClassAnnotationVisitor(), new PropertyVisitor());
    }

}
