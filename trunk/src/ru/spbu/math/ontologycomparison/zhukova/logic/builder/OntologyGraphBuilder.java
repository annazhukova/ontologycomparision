package ru.spbu.math.ontologycomparison.zhukova.logic.builder;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.owl.OntologyLoader;
import ru.spbu.math.ontologycomparison.zhukova.logic.owl.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.owl.impl.PropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyGraphBuilder {

    public static IOntologyGraph build(String ontologyPath)
            throws FileNotFoundException {
        OntologyLoader<OntologyConcept, OntologyRelation> ontologyLoader =
                new OntologyLoader<OntologyConcept, OntologyRelation>(new FileInputStream(ontologyPath));
        ITriple<Map<URI,OntologyConcept>,Map<URI,OntologyProperty>, IHashTable<String, OntologyConcept, Set<OntologyConcept>>>
                triple = ontologyLoader.load(new ClassAnnotationVisitor(), new PropertyVisitor());
        return new OntologyGraph(triple.getFirst(), triple.getSecond(), triple.getThird());
    }

    public static IOntologyGraph build(File ontologyFile)
            throws FileNotFoundException {
        OntologyLoader<OntologyConcept, OntologyRelation> ontologyLoader =
                new OntologyLoader<OntologyConcept, OntologyRelation>(ontologyFile);
        ITriple<Map<URI,OntologyConcept>,Map<URI,OntologyProperty>, IHashTable<String, OntologyConcept, Set<OntologyConcept>>>
                triple = ontologyLoader.load(new ClassAnnotationVisitor(), new PropertyVisitor());
        return new OntologyGraph(triple.getFirst(), triple.getSecond(), triple.getThird());
    }

}
