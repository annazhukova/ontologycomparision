package ru.spbu.math.ontologycomparision.zhukova.visualisation.modelbuilding;


import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.merged.MergedOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.OntologyComparator;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetRelation;

import java.util.*;
import java.awt.*;

import edu.smu.tspell.wordnet.Synset;


public class GraphModelBuilder implements IGraphModelBuilder {
    private final IOntologyGraph<OntologyConcept, OntologyRelation> firstOntologyGraph;
    private final IOntologyGraph<OntologyConcept, OntologyRelation> secondOntologyGraph;
    private final Map<Synset, MergedOntologyConcept<OntologyConcept, OntologyRelation>> mergedMap;
    private static final Color firstOntologyColor = Color.BLUE;
    private static final Color secondOntologyColor = Color.GREEN;
    private static final Color bothOntologyColor = Color.ORANGE;

    public GraphModelBuilder(IOntologyGraph<OntologyConcept, OntologyRelation> firstOntologyGraph,
                             IOntologyGraph<OntologyConcept, OntologyRelation> secondOntologyGraph) {
        this.firstOntologyGraph = firstOntologyGraph;
        this.secondOntologyGraph = secondOntologyGraph;
        this.mergedMap =
                (new OntologyComparator<OntologyConcept, OntologyRelation>()).merge(
                        this.firstOntologyGraph, this.secondOntologyGraph);
    }

    public GraphModel buildGraphModel(GraphPane graphPane) {
        GraphModel graphModel = new GraphModel(graphPane);
        Map<String, SuperVertex> synsetNameToVertex = new HashMap<String, SuperVertex>();
        Map<String, SimpleVertex> conceptNameToVertex = new HashMap<String, SimpleVertex>();
        buildVertices(graphPane, graphModel, synsetNameToVertex, conceptNameToVertex);
        buildArcs(conceptNameToVertex, graphModel);
        graphModel.setIntToSuperVertexMap(synsetNameToVertex);
        graphModel.setIntToSimpleVertexMap(conceptNameToVertex);
        return graphModel;
    }

    private void buildVertices(GraphPane graphPane, IGraphModel graphModel,
                               Map<String, SuperVertex> synsetNameToVertex,
                               Map<String, SimpleVertex> conceptNameToVertices) {
        Collection<MergedOntologyConcept<OntologyConcept, OntologyRelation>> mergedConcepts =
                this.mergedMap.values();
        Graphics g = graphPane.getGraphics();
        Font font = new Font(Font.MONOSPACED, Font.ITALIC, 15);
        g.setFont(font);
        int letterWidth = g.getFontMetrics().getWidths()['w'];
        int letterHeight = g.getFontMetrics().getHeight();
        int x = 10;
        int y = 10;
        int conceptVertexHeight = letterHeight + 4;
        int maxHeight = 10;
        for (MergedOntologyConcept<OntologyConcept, OntologyRelation> mergedConcept : mergedConcepts) {
            int maxConceptVertexWidth = 0;
            for (OntologyConcept concept : mergedConcept.getConcepts()) {
                String conceptName = concept.getLabel();
                maxConceptVertexWidth =
                        Math.max(letterWidth * conceptName.length() + 10, maxConceptVertexWidth);
            }
            String definition = mergedConcept.getSynset().getDefinition();
            int number = mergedConcept.getConcepts().size();
            int width = 10 + Math.min(maxConceptVertexWidth * number,
                    definition.length() * letterWidth) + 10;
            if (x > 800 - width) {
                x = 10;
                y += maxHeight + 10;
                maxHeight = 10;
            }
            int height = (conceptVertexHeight + 10) * number + letterHeight + 4;
            SuperVertex synsetVertex = new SuperVertex(new Point(x, y), definition);
            synsetVertex.setLetterWidth(letterWidth);
            synsetVertex.setLetterHeight(letterHeight);
            synsetVertex.setFont(font);
            synsetVertex.setHeight(height);
            synsetVertex.setWidth(width);
            graphModel.addVertex(synsetVertex);
            synsetNameToVertex.put(definition, synsetVertex);
            int conceptX = 10 + x;
            int conceptY = letterHeight + y;
            for (OntologyConcept concept : mergedConcept.getConcepts()) {
                if (conceptNameToVertices.containsKey(concept.getUri().toString())) {
                    continue;
                }
                String conceptName = concept.getLabel();
                int conceptWidth = letterWidth * conceptName.length() + 10;
                if (conceptX > x + width - conceptWidth) {
                    conceptX = x + 10;
                    conceptY += conceptVertexHeight + 10;
                }
                SimpleVertex conceptVertex =
                        new SimpleVertex(new Point(conceptX, conceptY), conceptName, synsetVertex,
                                getColorForConcept(concept));
                conceptVertex.setLetterWidth(letterWidth);
                conceptVertex.setLetterHeight(letterHeight);
                conceptVertex.setFont(font);

                conceptNameToVertices.put(concept.getUri().toString(), conceptVertex);
                graphModel.addVertex(conceptVertex);
                synsetVertex.addSimpleVertex(conceptVertex);
                conceptX += conceptWidth + 10;

                conceptVertex.setWidth(conceptWidth);
                conceptVertex.setHeight(conceptVertexHeight);

            }
            if (conceptY + conceptVertexHeight + 10 - y < height) {
                int newHeight = conceptY + conceptVertexHeight + 10 - conceptY;
                synsetVertex.setHeight(newHeight);
                height = newHeight;
            }
            maxHeight = Math.max(maxHeight, height);
            x += width + 10;
        }
    }

    private void buildArcs(Map<String, SimpleVertex> nameToVertex, IGraphModel graphModel) {
        IArcFilter filter = Arc.getArcFilter();
        if (filter == null) {
            Arc.setArcFilter(new ArcFilter());
        }
        Collection<OntologyConcept> firstConcepts = this.firstOntologyGraph.getConcepts();
        Collection<OntologyConcept> secondConcepts = this.secondOntologyGraph.getConcepts();
        Collection<OntologyConcept> allConcepts = new ArrayList<OntologyConcept>(firstConcepts);
        allConcepts.addAll(secondConcepts);
        for (OntologyConcept concept : allConcepts) {
            String name = concept.getUri().toString();
            SimpleVertex subjectVertex = nameToVertex.get(name);
            for (OntologyRelation relation :
                    concept.getSubjectRelations(WordNetRelation.HYPERNYM.getRelatedOntologyConcept())) {
                String objectName = relation.getObject().getUri().toString();
                if (nameToVertex.containsKey(objectName)) {
                    SimpleVertex objectVertex = nameToVertex.get(objectName);
                    graphModel.addArc(new Arc(subjectVertex, objectVertex,
                            Arrays.asList(relation.getRelationName())));
                }

            }
        }
    }

    private Color getColorForConcept(OntologyConcept concept) {
        return (this.firstOntologyGraph.getConceptByURI(concept.getUri()) != null) ?
                ((this.secondOntologyGraph.getConceptByURI(concept.getUri()) != null) ?
                        GraphModelBuilder.bothOntologyColor : GraphModelBuilder.firstOntologyColor) :
                GraphModelBuilder.secondOntologyColor;

    }
}





