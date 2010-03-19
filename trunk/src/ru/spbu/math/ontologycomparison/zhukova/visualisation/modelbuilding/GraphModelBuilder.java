package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding;


import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.IOntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.SimilarityReason;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.impl.OntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArcFilter;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.*;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;
import java.util.*;


public class GraphModelBuilder implements IGraphModelBuilder {
    private final IOntologyGraph firstOntologyGraph;
    private final IOntologyGraph secondOntologyGraph;
    private final Collection<IOntologyConcept> mergedConcepts;
    private final int similarity;
    private static final Color firstOntologyColor = Color.BLUE;
    private static final Color secondOntologyColor = Color.GREEN;
    private static final Color bothOntologyColor = Color.ORANGE;
    private static final int X_GAP = 4;
    private static final int Y_GAP = 2;
    private static final int FRAME_WIDTH = 800;
    private static final int LABEL_GAP = 4;

    public GraphModelBuilder(IOntologyGraph firstOntologyGraph, IOntologyGraph secondOntologyGraph, ILogger logger) {
        this.firstOntologyGraph = firstOntologyGraph;
        this.secondOntologyGraph = secondOntologyGraph;
        IOntologyComparator ontologyComparator = new OntologyComparator(this.firstOntologyGraph, this.secondOntologyGraph, logger);
        this.mergedConcepts = ontologyComparator.mapOntologies().getFirst();
        this.similarity = (int) (ontologyComparator.getSimilarity() * 100);
    }

    public GraphModel buildGraphModel(GraphPane graphPane, boolean showUnmapped, boolean showUnmappedWithSynsets) {
        GraphModel graphModel = new GraphModel(graphPane);
        Map<String, SuperVertex> keyToSuperVertexMap = new HashMap<String, SuperVertex>();
        Map<String, SimpleVertex> conceptNameToVertexMap = new HashMap<String, SimpleVertex>();
        Map<IOntologyConcept, SimpleVertex> conceptToVertexMap = new HashMap<IOntologyConcept, SimpleVertex>();
        buildVertices(graphPane, graphModel, keyToSuperVertexMap, conceptNameToVertexMap, conceptToVertexMap, showUnmapped, showUnmappedWithSynsets);
        buildArcs(conceptNameToVertexMap, graphModel);
        graphModel.setKeyToSuperVertexMap(keyToSuperVertexMap);
        graphModel.setIntToSimpleVertexMap(conceptNameToVertexMap);
        graphModel.setConceptToVertexMap(conceptToVertexMap);
        return graphModel;
    }

    private void buildVertices(GraphPane graphPane, IGraphModel graphModel, Map<String, SuperVertex> keyToSuperVertexMap,
                               Map<String, SimpleVertex> conceptNameToVertexMap, Map<IOntologyConcept, SimpleVertex> conceptToVertexMap,  boolean showUnmapped, boolean showUnmappedWithSynsets) {
        Graphics g = graphPane.getGraphics();
        Font font = Vertex.FONT;
        g.setFont(font);
        int letterWidth = Vertex.LETTER_WIDTH;
        int letterHeight = Vertex.LETTER_HEIGHT;
        int currentX = X_GAP;
        int currentY = Y_GAP;
        int maxHeight = letterHeight + LABEL_GAP + 2 * Y_GAP;
        int simpleVertexHeight = letterHeight + 2 * LABEL_GAP;
        for (IOntologyConcept mainConcept : this.mergedConcepts) {
            int superVertexWidth = 0;
            Set<IOntologyConcept> conceptSet = new LinkedHashSet<IOntologyConcept>(mainConcept.getConceptToReason().keySet());
            conceptSet.add(mainConcept);
            for (IOntologyConcept concept : conceptSet) {
                String simpleLabel = concept.getLabelCollection().toString();
                superVertexWidth += letterWidth * simpleLabel.length() + 2 * LABEL_GAP + X_GAP;
            }
            String superLabel = mainConcept.hasMappedConcepts() ? SimilarityReason.LEXICAL.name() : SimilarityReason.NO.name();
            Synset synset = null;
            for (IOntologyConcept c : conceptSet) {
                if (!c.getSynsetToReason().isEmpty()) {
                    superLabel = SimilarityReason.WORDNET.name();
                    synset = c.getSynsetToReason().keySet().iterator().next();
                    break;
                }
            }
            int simpleVertexNumber = conceptSet.size();
            if (currentX > FRAME_WIDTH - superVertexWidth) {
                currentX = X_GAP;
                currentY += maxHeight + Y_GAP;
            }

            boolean hidden = isHidden(showUnmapped, showUnmappedWithSynsets, mainConcept, superLabel);
            SuperVertex superVertex = null;
            int superVertexHeight = 0;
            if (!superLabel.equals(SimilarityReason.NO.name())) {
                superVertexHeight = (Y_GAP + simpleVertexHeight) * simpleVertexNumber + letterHeight + LABEL_GAP + Y_GAP;
                superVertex = keyToSuperVertexMap.get(superLabel + conceptSet);
                if (superVertex != null) {
                    continue;
                }
                superVertex = createSuperVertex(graphModel, currentX,
                        currentY, superLabel, superVertexWidth + X_GAP, superVertexHeight, mainConcept, synset);
                if (hidden) {
                    superVertex.setHidden(true);
                }
                keyToSuperVertexMap.put(superLabel + conceptSet, superVertex);
            }
            int conceptX = X_GAP + currentX;
            int conceptY = LABEL_GAP + letterHeight + currentY;
            int newChildren = 0;
            for (IOntologyConcept concept : conceptSet) {
                SimpleVertex simpleVertex = conceptNameToVertexMap.get(concept.getUri().toString());
                if (simpleVertex == null) {
                    String simpleLabel = concept.getLabelCollection().toString();
                    int simpleVertexWidth = letterWidth * simpleLabel.length() + 2 * LABEL_GAP;
                    if (conceptX + simpleVertexWidth > currentX + superVertexWidth) {
                        conceptX = currentX + X_GAP;
                        conceptY += simpleVertexHeight + Y_GAP;
                    }
                    simpleVertex = createSimpleVertex(graphModel, simpleVertexHeight, superVertex, conceptX, conceptY, concept, simpleLabel, simpleVertexWidth);
                    conceptToVertexMap.put(concept, simpleVertex);
                    simpleVertex.setHidden(hidden);
                    conceptNameToVertexMap.put(concept.getUri().toString(), simpleVertex);
                    conceptX += simpleVertex.getWidth() + X_GAP;
                    newChildren++;
                    if (superVertex != null) {
                        superVertex.addSimpleVertex(simpleVertex);
                    }
                }
            }
            if (newChildren == 0) {
                continue;
            }
            if (superVertexHeight > 0) {
                if (conceptY + simpleVertexHeight + Y_GAP < currentY + superVertexHeight) {
                    int newHeight = conceptY - currentY + simpleVertexHeight + Y_GAP;
                    superVertex.setHeight(newHeight);
                    superVertexHeight = newHeight;
                }
                maxHeight = Math.max(maxHeight, superVertexHeight);
            }
            currentX += superVertexWidth + X_GAP;
        }
    }

    private boolean isHidden(boolean showUnmapped, boolean showUnmappedWithSynsets, IOntologyConcept mainConcept, String superLabel) {
        return !showUnmapped && superLabel.equals(SimilarityReason.NO.name()) ||
                !showUnmappedWithSynsets && superLabel.equals(SimilarityReason.WORDNET.name()) && !mainConcept.hasMappedConcepts();
    }

    private String createToolTip(IOntologyConcept mainConcept, Synset synset) {
        StringBuilder result = new StringBuilder("");
        if (synset == null && !mainConcept.hasMappedConcepts()) {
            return null;
        }
        if (synset != null) {
            result.append("<p>").append(synset.getDefinition()).append("</p>");
        }
        if (mainConcept.hasMappedConcepts()) {
            result.append("<ul>");
            for (Map.Entry<String, Integer> reason :
                    mainConcept.getConceptToReason().values().iterator().next().entrySet()) {
                result.append("<li>").append(reason.getKey()).append(" (").append(reason.getValue()).append(")");
            }
            result.append("</ul>");
        }
        return result.toString();
    }

    private SimpleVertex createSimpleVertex(IGraphModel graphModel, int simpleVertexHeight,
                                            SuperVertex superVertex, int conceptX, int conceptY, IOntologyConcept concept, String simpleLabel, int simpleVertexWidth) {
        SimpleVertex simpleVertex =
                new SimpleVertex(new Point(conceptX, conceptY), simpleLabel, superVertex,
                        getColorForConcept(concept));
        initVertex(graphModel, simpleVertexHeight, simpleVertexWidth, simpleVertex);
        return simpleVertex;
    }

    private void initVertex(IGraphModel graphModel,
                            int vertexHeight, int vertexWidth, Vertex vertex) {
        vertex.setWidth(vertexWidth);
        vertex.setHeight(vertexHeight);
        graphModel.addVertex(vertex);
    }

    private SuperVertex createSuperVertex(IGraphModel graphModel,
                                          int currentX, int currentY, String superLabel, int superVertexWidth, int superVertexHeight,
                                          IOntologyConcept mainConcept, Synset synset) {
        String toolTip = createToolTip(mainConcept, synset);
        SuperVertex superVertex = new SuperVertex(new Point(currentX, currentY), superLabel, toolTip);
        initVertex(graphModel, superVertexHeight, superVertexWidth, superVertex);
        return superVertex;
    }

    private void buildArcs(Map<String, SimpleVertex> nameToVertex, IGraphModel graphModel) {
        IArcFilter filter = Arc.getArcFilter();
        if (filter == null) {
            Arc.setArcFilter(new ArcFilter());
        }
        Collection<IOntologyConcept> firstConcepts = this.firstOntologyGraph.getConcepts();
        Collection<IOntologyConcept> secondConcepts = this.secondOntologyGraph.getConcepts();
        Collection<IOntologyConcept> allConcepts = new ArrayList<IOntologyConcept>(firstConcepts);
        allConcepts.addAll(secondConcepts);
        for (IOntologyConcept parent : allConcepts) {
            String parentName = parent.getUri().toString();
            SimpleVertex parentVertex = nameToVertex.get(parentName);
            if (parentVertex == null) {
                continue;
            }
            for (IOntologyConcept child : parent.getParents()) {
                String childName = child.getUri().toString();
                SimpleVertex childVertex = nameToVertex.get(childName);
                if (childVertex != null) {
                    graphModel.addArc(new Arc(parentVertex, childVertex, Collections.EMPTY_LIST, Color.DARK_GRAY));
                }
            }

            for (IOntologyRelation relation : parent.getSubjectRelations()) {
                IOntologyConcept objectConcept = relation.getObject();
                if (objectConcept != null) {
                    String objectName = objectConcept.getUri().toString();
                    SimpleVertex objectVertex = nameToVertex.get(objectName);
                    if (objectVertex != null) {
                        graphModel.addArc(new Arc(parentVertex, objectVertex,
                                Arrays.asList(relation.getRelationName())));
                    }
                }
            }
        }
    }

    private Color getColorForConcept(IOntologyConcept concept) {
        return (this.firstOntologyGraph.getConceptByURI(concept.getUri()) != null) ?
                ((this.secondOntologyGraph.getConceptByURI(concept.getUri()) != null) ?
                        GraphModelBuilder.bothOntologyColor : GraphModelBuilder.firstOntologyColor) :
                GraphModelBuilder.secondOntologyColor;

    }

    public int getSimilarity() {
        return similarity;
    }
}





