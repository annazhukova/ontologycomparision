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
    private final ILogger logger;
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
        this.logger = logger;
        IOntologyComparator ontologyComparator = new OntologyComparator(this.firstOntologyGraph, this.secondOntologyGraph, logger);
        this.mergedConcepts = ontologyComparator.mapOntologies().getFirst();
        this.similarity = (int) (ontologyComparator.getSimilarity() * 100);
    }

    public GraphModel buildGraphModel(GraphPane graphPane, boolean showUnmapped, boolean showUnmappedWithSynsets) {
        GraphModel graphModel = new GraphModel(graphPane);
        Map<String, SuperVertex> keyToSyperVertex = new HashMap<String, SuperVertex>();
        Map<String, SimpleVertex> conceptNameToVertex = new HashMap<String, SimpleVertex>();
        buildVertices(graphPane, graphModel, keyToSyperVertex, conceptNameToVertex, showUnmapped, showUnmappedWithSynsets);
        buildArcs(conceptNameToVertex, graphModel);
        graphModel.setKeyToSuperVertexMap(keyToSyperVertex);
        graphModel.setIntToSimpleVertexMap(conceptNameToVertex);
        return graphModel;
    }

    private void buildVertices(GraphPane graphPane, IGraphModel graphModel, Map<String, SuperVertex> keyToSuperVertex,
                               Map<String, SimpleVertex> conceptNameToVertices, boolean showUnmapped, boolean showUnmappedWithSynsets) {
        Graphics g = graphPane.getGraphics();
        Font font = new Font(Font.MONOSPACED, Font.ITALIC, 15);
        g.setFont(font);
        int letterWidth = g.getFontMetrics().getWidths()['w'];
        int letterHeight = g.getFontMetrics().getHeight();
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
                superVertex = keyToSuperVertex.get(superLabel + conceptSet);
                if (superVertex != null) {
                    continue;
                }
                superVertex = createSuperVertex(graphModel, font, letterWidth, letterHeight, currentX,
                        currentY, superLabel, superVertexWidth + X_GAP, superVertexHeight, mainConcept, synset);
                if (hidden) {
                    superVertex.setHidden(true);
                }
                keyToSuperVertex.put(superLabel + conceptSet, superVertex);
            }
            int conceptX = X_GAP + currentX;
            int conceptY = LABEL_GAP + letterHeight + currentY;
            int newChildren = 0;
            for (IOntologyConcept concept : conceptSet) {
                SimpleVertex simpleVertex = conceptNameToVertices.get(concept.getUri().toString());
                if (simpleVertex == null) {
                    String simpleLabel = concept.getLabelCollection().toString();
                    int simpleVertexWidth = letterWidth * simpleLabel.length() + 2 * LABEL_GAP;
                    if (conceptX + simpleVertexWidth > currentX + superVertexWidth) {
                        conceptX = currentX + X_GAP;
                        conceptY += simpleVertexHeight + Y_GAP;
                    }
                    simpleVertex = createSimpleVertex(graphModel, font, letterWidth, letterHeight, simpleVertexHeight, superVertex, conceptX, conceptY, concept, simpleLabel, simpleVertexWidth);
                    simpleVertex.setHidden(hidden);
                    conceptNameToVertices.put(concept.getUri().toString(), simpleVertex);
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

    private SimpleVertex createSimpleVertex(IGraphModel graphModel, Font font, int letterWidth, int letterHeight, int simpleVertexHeight,
                                            SuperVertex superVertex, int conceptX, int conceptY, IOntologyConcept concept, String simpleLabel, int simpleVertexWidth) {
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

    private SuperVertex createSuperVertex(IGraphModel graphModel, Font font, int letterWidth, int letterHeight,
                                          int currentX, int currentY, String superLabel, int superVertexWidth, int superVertexHeight,
                                          IOntologyConcept mainConcept, Synset synset) {
        String toolTip = createToolTip(mainConcept, synset);
        SuperVertex superVertex = new SuperVertex(new Point(currentX, currentY), superLabel, toolTip);
        initVertex(graphModel, font, letterWidth, letterHeight, superVertexHeight, superVertexWidth, superVertex);
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





