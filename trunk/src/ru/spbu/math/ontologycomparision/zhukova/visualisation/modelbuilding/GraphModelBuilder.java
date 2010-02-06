package ru.spbu.math.ontologycomparision.zhukova.visualisation.modelbuilding;


import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.OntologyComparator;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparision.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IArcFilter;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;
import java.util.*;


public class GraphModelBuilder implements IGraphModelBuilder {
    private final IOntologyGraph<OntologyConcept, OntologyRelation> firstOntologyGraph;
    private final IOntologyGraph<OntologyConcept, OntologyRelation> secondOntologyGraph;
    private final IHashTable<Synset, OntologyConcept> merged;
    private static final Color firstOntologyColor = Color.BLUE;
    private static final Color secondOntologyColor = Color.GREEN;
    private static final Color bothOntologyColor = Color.ORANGE;
    private static final int X_GAP = 6;
    private static final int Y_GAP = 6;
    private static final int FRAME_WIDTH = 800;
    private static final int LABEL_GAP = 2;

    public GraphModelBuilder(IOntologyGraph<OntologyConcept, OntologyRelation> firstOntologyGraph,
                             IOntologyGraph<OntologyConcept, OntologyRelation> secondOntologyGraph) {
        this.firstOntologyGraph = firstOntologyGraph;
        this.secondOntologyGraph = secondOntologyGraph;
        this.merged = (new OntologyComparator<OntologyConcept, OntologyRelation>(
                this.firstOntologyGraph, this.secondOntologyGraph)).merge();
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
        Graphics g = graphPane.getGraphics();
        Font font = new Font(Font.MONOSPACED, Font.ITALIC, 15);
        g.setFont(font);
        int letterWidth = g.getFontMetrics().getWidths()['w'];
        int letterHeight = g.getFontMetrics().getHeight();
        int currentX= X_GAP;
        int currentY = Y_GAP;
        int maxHeight = letterHeight + LABEL_GAP + 2 * Y_GAP;
        int simpleVertexHeight = letterHeight + 2 * LABEL_GAP;
        for (Map.Entry<Synset, Set<OntologyConcept>> mergedEntry : this.merged.entrySet()) {
            int maxSimpleVertexWidth = 0;
            for (OntologyConcept concept : mergedEntry.getValue()) {
                String simpleLabel = concept.getLabel();
                maxSimpleVertexWidth =
                        Math.max(letterWidth * simpleLabel.length() + 2 * LABEL_GAP, maxSimpleVertexWidth);
            }
            String superLabel = mergedEntry.getKey().getDefinition();
            int simpleVertexNumber = mergedEntry.getValue().size();
            int superVertexWidth = (X_GAP + maxSimpleVertexWidth) * simpleVertexNumber + X_GAP;
            int superVertexHeight = (Y_GAP + simpleVertexHeight) * simpleVertexNumber + letterHeight + LABEL_GAP + Y_GAP;

            if (currentX > FRAME_WIDTH - superVertexWidth) {
                currentX = X_GAP;
                currentY += maxHeight + Y_GAP;
            }

            SuperVertex superVertex = createSuperVertex(graphModel, font, letterWidth, letterHeight, currentX,
                    currentY, superLabel, superVertexWidth, superVertexHeight);
            synsetNameToVertex.put(superLabel, superVertex);

            int conceptX = X_GAP + currentX;
            int conceptY = LABEL_GAP + letterHeight + currentY;

            for (OntologyConcept concept : mergedEntry.getValue()) {
                if (conceptNameToVertices.containsKey(concept.getUri().toString())) {
                    continue;
                }
                String simpleLabel = concept.getLabel();
                int simpleVertexWidth = letterWidth * simpleLabel.length() + 2 * LABEL_GAP;
                if (conceptX  + simpleVertexWidth > currentX + superVertexWidth) {
                    conceptX = currentX + X_GAP;
                    conceptY += simpleVertexHeight + Y_GAP;
                }
                SimpleVertex simpleVertex = createSimpleVertex(graphModel, font, letterWidth, letterHeight, simpleVertexHeight, superVertex, conceptX, conceptY, concept, simpleLabel, simpleVertexWidth);
                conceptNameToVertices.put(concept.getUri().toString(), simpleVertex);
                superVertex.addSimpleVertex(simpleVertex);
                conceptX += simpleVertexWidth + X_GAP;
            }
            if (conceptY + simpleVertexHeight + Y_GAP < currentY + superVertexHeight) {
                int newHeight = conceptY - currentY + simpleVertexHeight + Y_GAP;
                superVertex.setHeight(newHeight);
                superVertexHeight = newHeight;
            }
            maxHeight = Math.max(maxHeight, superVertexHeight);
            currentX += superVertexWidth + X_GAP;
        }
    }

    private SimpleVertex createSimpleVertex(IGraphModel graphModel, Font font, int letterWidth, int letterHeight, int simpleVertexHeight, SuperVertex superVertex, int conceptX, int conceptY, OntologyConcept concept, String simpleLabel, int simpleVertexWidth) {
        SimpleVertex simpleVertex =
                new SimpleVertex(new Point(conceptX, conceptY), simpleLabel, superVertex,
                        getColorForConcept(concept));
        initVertex(graphModel, font, letterWidth, letterHeight, simpleVertexHeight, simpleVertexWidth, simpleVertex);
        return simpleVertex;
    }

    private void initVertex(IGraphModel graphModel, Font font, int letterWidth, int letterHeight,
                            int vertexHeight, int vertexWidth, Vertex vertex) {
        vertex.setLetterWidth(letterWidth);
        vertex.setLetterHeight(letterHeight);
        vertex.setFont(font);
        vertex.setWidth(vertexWidth);
        vertex.setHeight(vertexHeight);
        graphModel.addVertex(vertex);
    }

    private SuperVertex createSuperVertex(IGraphModel graphModel, Font font, int letterWidth, int letterHeight, int currentX, int currentY, String superLabel, int superVertexWidth, int superVertexHeight) {
        SuperVertex superVertex = new SuperVertex(new Point(currentX, currentY), superLabel);
        initVertex(graphModel, font, letterWidth, letterHeight, superVertexHeight, superVertexWidth, superVertex);
        return superVertex;
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
        for (OntologyConcept child : allConcepts) {
            String childName = child.getUri().toString();
            SimpleVertex childVertex = nameToVertex.get(childName);
            if (childVertex  == null) {
                continue;
            }
            /*for (OntologyRelation relation :
                    child.getSubjectRelations(WordNetRelation.HYPERNYM.getRelatedOntologyConcept())) {
               */
            for (OntologyConcept parent : child.getParents()) {
                /*String parentName = relation.getObject().getUri().toString();*/
                String parentName = parent.getUri().toString();
                if (nameToVertex.containsKey(parentName)) {
                    SimpleVertex parentVertex = nameToVertex.get(parentName);
                    graphModel.addArc(new Arc(childVertex, parentVertex,
                            Arrays.asList(WordNetRelation.HYPONYM.getRelatedOntologyConcept())));
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





