package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding;


import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.OntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArcFilter;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.*;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;
import java.util.*;


public class GraphModelBuilder implements IGraphModelBuilder {
    private final IOntologyGraph firstOntologyGraph;
    private final IOntologyGraph secondOntologyGraph;
    private final Collection<OntologyConcept> mergedConcepts;
    private final int similarity;
    private static final Color firstOntologyColor = Color.BLUE;
    private static final Color secondOntologyColor = Color.GREEN;
    private static final Color bothOntologyColor = Color.ORANGE;
    private static final int X_GAP = 6;
    private static final int Y_GAP = 6;
    private static final int FRAME_WIDTH = 800;
    private static final int LABEL_GAP = 2;

    public GraphModelBuilder(IOntologyGraph firstOntologyGraph,
                             IOntologyGraph secondOntologyGraph) {
        this.firstOntologyGraph = firstOntologyGraph;
        this.secondOntologyGraph = secondOntologyGraph;
        OntologyComparator ontologyComparator = new OntologyComparator(
                this.firstOntologyGraph, this.secondOntologyGraph);
        this.mergedConcepts = ontologyComparator.mapOntologies();
        this.similarity = (int) (ontologyComparator.getSimilarity() * 100);
    }

    public GraphModel buildGraphModel(GraphPane graphPane) {
        GraphModel graphModel = new GraphModel(graphPane);
        Map<String, SuperVertex> keyToSyperVertex = new HashMap<String, SuperVertex>();
        Map<String, SimpleVertex> conceptNameToVertex = new HashMap<String, SimpleVertex>();
        buildVertices(graphPane, graphModel, keyToSyperVertex, conceptNameToVertex, true);
        buildArcs(conceptNameToVertex, graphModel);
        graphModel.setKeyToSuperVertexMap(keyToSyperVertex);
        graphModel.setIntToSimpleVertexMap(conceptNameToVertex);
        return graphModel;
    }

    private void buildVertices(GraphPane graphPane, IGraphModel graphModel,
                               Map<String, SuperVertex> keyToSuperVertex,
                               Map<String, SimpleVertex> conceptNameToVertices, boolean showUnmappedUnmergedOnes) {
        Graphics g = graphPane.getGraphics();
        Font font = new Font(Font.MONOSPACED, Font.ITALIC, 15);
        g.setFont(font);
        int letterWidth = g.getFontMetrics().getWidths()['w'];
        int letterHeight = g.getFontMetrics().getHeight();
        int currentX = X_GAP;
        int currentY = Y_GAP;
        int maxHeight = letterHeight + LABEL_GAP + 2 * Y_GAP;
        int simpleVertexHeight = letterHeight + 2 * LABEL_GAP;
        for (OntologyConcept mainConcept : this.mergedConcepts) {
            int maxSimpleVertexWidth = 0;
            Set<OntologyConcept> conceptSet = new LinkedHashSet<OntologyConcept>(mainConcept.getConceptToReason().keySet());
            conceptSet.add(mainConcept);
            if (conceptSet.isEmpty()) {
                continue;
            }
            for (OntologyConcept concept : conceptSet) {
                String simpleLabel = concept.getLabelCollection().toString();
                maxSimpleVertexWidth =
                        Math.max(letterWidth * simpleLabel.length() + 2 * LABEL_GAP, maxSimpleVertexWidth);
            }
            String lbl = conceptSet.size() > 1 ? "LEXICAL" : "UNMAPPED";
            for (OntologyConcept c : conceptSet) {
                if (!c.getSynsetToReason().isEmpty()) {
                    StringBuilder labelBuilder = new StringBuilder("SYNSET {");
                    for (Synset synset: c.getSynsetToReason().keySet()) {
                          labelBuilder.append(synset.getDefinition()).append(c.getSynsetToReason().get(synset)).append(", ");
                    }
                    labelBuilder.append("}");
                    lbl = labelBuilder.toString();
                    break;
                }
            }
            String superLabel = String.format("%s (%s)", lbl, mainConcept.getConceptToReason().values());
            int simpleVertexNumber = conceptSet.size();
            int superVertexWidth = (X_GAP + maxSimpleVertexWidth) * simpleVertexNumber + X_GAP;
            int superVertexHeight = (Y_GAP + simpleVertexHeight) * simpleVertexNumber + letterHeight + LABEL_GAP + Y_GAP;

            if (currentX > FRAME_WIDTH - superVertexWidth) {
                currentX = X_GAP;
                currentY += maxHeight + Y_GAP;
            }

            SuperVertex superVertex = keyToSuperVertex.get(superLabel + conceptSet);
            if (superVertex != null) {
                continue;
            }
            superVertex = createSuperVertex(graphModel, font, letterWidth, letterHeight, currentX,
                    currentY, superLabel, superVertexWidth, superVertexHeight);
            int conceptX = X_GAP + currentX;
            int conceptY = LABEL_GAP + letterHeight + currentY;
            int newChildren = 0;
            for (OntologyConcept concept : conceptSet) {
                SimpleVertex simpleVertex = conceptNameToVertices.get(concept.getUri().toString());
                if (simpleVertex == null) {
                    String simpleLabel = concept.getLabelCollection().toString();
                    int simpleVertexWidth = letterWidth * simpleLabel.length() + 2 * LABEL_GAP;
                    if (conceptX + simpleVertexWidth > currentX + superVertexWidth) {
                        conceptX = currentX + X_GAP;
                        conceptY += simpleVertexHeight + Y_GAP;
                    }
                    simpleVertex = createSimpleVertex(graphModel, font, letterWidth, letterHeight, simpleVertexHeight, superVertex, conceptX, conceptY, concept, simpleLabel, simpleVertexWidth);
                    conceptNameToVertices.put(concept.getUri().toString(), simpleVertex);
                    conceptX += simpleVertex.getWidth() + X_GAP;
                    newChildren++;
                    superVertex.addSimpleVertex(simpleVertex);
                }
            }
            if (newChildren == 0) {
                continue;
            }
            if (conceptY + simpleVertexHeight + Y_GAP < currentY + superVertexHeight) {
                int newHeight = conceptY - currentY + simpleVertexHeight + Y_GAP;
                superVertex.setHeight(newHeight);
                superVertexHeight = newHeight;
            }
            maxHeight = Math.max(maxHeight, superVertexHeight);
            currentX += superVertexWidth + X_GAP;
            keyToSuperVertex.put(superLabel + conceptSet, superVertex);
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
            if (childVertex == null) {
                continue;
            }
            /*for (OntologyRelation relation :
                    child.getSubjectRelations(WordNetRelation.HYPERNYM.getRelatedOntologyConcept())) {
               */
            /*for (OntologyConcept parent : child.getParents()) {
                *//*String parentName = relation.getObject().getUri().toString();*//*
                String parentName = parent.getUri().toString();
                SimpleVertex parentVertex = nameToVertex.get(parentName);
                if (parentVertex != null) {
                    graphModel.addArc(new Arc(childVertex, parentVertex,
                            Arrays.asList(WordNetRelation.HYPONYM.getRelatedOntologyConcept())));
                }

            }*/

            for (OntologyRelation relation : child.getSubjectRelations()) {
                OntologyConcept objectConcept = relation.getObject();
                if (objectConcept != null) {
                    String objectName = objectConcept.getUri().toString();
                    SimpleVertex objectVertex = nameToVertex.get(objectName);
                    if (objectVertex != null) {
                        graphModel.addArc(new Arc(childVertex, objectVertex,
                                Arrays.asList(relation.getRelationName())));
                    }
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

    public int getSimilarity() {
        return similarity;
    }
}





