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
import java.net.URI;
import java.util.*;


public class GraphModelBuilder implements IGraphModelBuilder {
    private final IOntologyGraph firstOntologyGraph;
    private final IOntologyGraph secondOntologyGraph;
    private final IOntologyGraph ontologyGraph;
    private final int similarity;
    private static final Color firstOntologyColor = Color.BLUE;
    private static final Color secondOntologyColor = Color.GREEN;
    private static final Color bothOntologyColor = Color.ORANGE;
    private static final int X_GAP = 3;
    private static final int Y_GAP = 1;
    private static final int FRAME_WIDTH = 800;
    private static final int LABEL_GAP = 3;

    public GraphModelBuilder(IOntologyGraph firstOntologyGraph, IOntologyGraph secondOntologyGraph, ILogger logger) {
        this.firstOntologyGraph = firstOntologyGraph;
        this.secondOntologyGraph = secondOntologyGraph;
        IOntologyComparator ontologyComparator = new OntologyComparator(this.firstOntologyGraph, this.secondOntologyGraph, logger);
        this.ontologyGraph = ontologyComparator.mapOntologies().getFirst();
        this.similarity = (int) (ontologyComparator.getSimilarity() * 100);
    }

    public GraphModel buildGraphModel(GraphPane graphPane, boolean showUnmapped, boolean showUnmappedWithSynsets) {
        GraphModel graphModel = new GraphModel(graphPane);
        Map<Set<IOntologyConcept>, SuperVertex> keyToSuperVertexMap = new HashMap<Set<IOntologyConcept>, SuperVertex>();
        Map<URI, SimpleVertex> keyToSimpleVertexMap = new HashMap<URI, SimpleVertex>();
        Map<IOntologyConcept, SimpleVertex> conceptToVertexMap = new HashMap<IOntologyConcept, SimpleVertex>();
        buildVertices(graphPane, graphModel, keyToSuperVertexMap, keyToSimpleVertexMap, conceptToVertexMap, showUnmapped, showUnmappedWithSynsets);
        buildArcs(keyToSimpleVertexMap, graphModel);
        graphModel.setKeyToSuperVertexMap(keyToSuperVertexMap);
        graphModel.setKeyToSimpleVertexMap(keyToSimpleVertexMap);
        graphModel.setConceptToVertexMap(conceptToVertexMap);
        return graphModel;
    }

    private void buildVertices(GraphPane graphPane, IGraphModel graphModel, Map<Set<IOntologyConcept>, SuperVertex> keyToSuperVertexMap,
                                Map<URI, SimpleVertex> keyToSimpleVertexMap, Map<IOntologyConcept, SimpleVertex> conceptToVertexMap, boolean showUnmapped, boolean showUnmappedWithSynsets) {
        Graphics g = graphPane.getGraphics();
        Font font = Vertex.FONT;
        g.setFont(font);
        buildLayers(this.ontologyGraph.getRoots(), Y_GAP, graphModel, keyToSuperVertexMap, keyToSimpleVertexMap, conceptToVertexMap, showUnmapped, showUnmappedWithSynsets);
    }

    private void buildLayers(Set<IOntologyConcept> concepts, int currentY, IGraphModel graphModel, Map<Set<IOntologyConcept>, SuperVertex> keyToSuperVertexMap, Map<URI, SimpleVertex> keyToSimpleVertexMap, Map<IOntologyConcept, SimpleVertex> conceptToVertexMap, boolean showUnmapped, boolean showUnmappedWithSynsets) {
        int nextY = buildLayer(concepts, graphModel, keyToSuperVertexMap, keyToSimpleVertexMap, conceptToVertexMap, showUnmapped, showUnmappedWithSynsets, currentY);
        Set<IOntologyConcept> nextLayer = new LinkedHashSet<IOntologyConcept>();
        for (IOntologyConcept current : concepts) {
            nextLayer.addAll(Arrays.asList(current.getChildren()));
            for (IOntologyConcept similar : current.getSimilarConcepts()) {
                nextLayer.addAll(Arrays.asList(similar.getChildren()));
            }
        }
        if (!nextLayer.isEmpty()) {
            buildLayers(nextLayer, nextY, graphModel, keyToSuperVertexMap, keyToSimpleVertexMap, conceptToVertexMap, showUnmapped, showUnmappedWithSynsets);
        }
    }

    private int buildLayer(Collection<IOntologyConcept> layer, IGraphModel graphModel, Map<Set<IOntologyConcept>, SuperVertex> keyToSuperVertexMap,
                           Map<URI, SimpleVertex> conceptNameToVertexMap, Map<IOntologyConcept, SimpleVertex> conceptToVertexMap, boolean showUnmapped, boolean showUnmappedWithSynsets, int y) {
        int currentX = 0;
        for (IOntologyConcept mainConcept : layer) {
            if (currentX > FRAME_WIDTH) {
                y += SuperVertex.getVertexHeight() + 4 * Y_GAP;
                currentX = 0;
            }
            Set<IOntologyConcept> conceptSet = new HashSet<IOntologyConcept>(mainConcept.getConceptToReason().keySet());
            conceptSet.add(mainConcept);
            SuperVertex superVertex = keyToSuperVertexMap.get(conceptSet);
            if (superVertex != null) {
                superVertex.setY(y);
                superVertex.setX(currentX);
                currentX += superVertex.getWidth() + X_GAP;
                continue;
            }
            int superVertexWidth = getSuperVertexWidth(conceptSet);
            String superLabel = mainConcept.hasMappedConcepts() ? SimilarityReason.LEXICAL.name() : SimilarityReason.NO.name();
            Synset synset = null;
            for (IOntologyConcept c : conceptSet) {
                if (!c.getSynsetToReason().isEmpty()) {
                    superLabel = SimilarityReason.WORDNET.name();
                    synset = c.getSynsetToReason().keySet().iterator().next();
                    break;
                }
            }
            superVertexWidth = Math.max(superVertexWidth, superLabel.length() * Vertex.LETTER_WIDTH + 2 * X_GAP);
            boolean hidden = isHidden(showUnmapped, showUnmappedWithSynsets, mainConcept, superLabel);
            if (!superLabel.equals(SimilarityReason.NO.name())) {
                superVertex = createSuperVertex(graphModel, currentX, y, superLabel, superVertexWidth + X_GAP, mainConcept, conceptSet, synset);
                if (hidden) {
                    superVertex.setHidden(true);
                }
                keyToSuperVertexMap.put(conceptSet, superVertex);
            }
            int conceptX = X_GAP + currentX;
            int conceptY = LABEL_GAP + Vertex.LETTER_HEIGHT + y;
            for (IOntologyConcept concept : conceptSet) {
                SimpleVertex simpleVertex = conceptNameToVertexMap.get(concept.getUri());
                if (simpleVertex == null) {
                    simpleVertex = createSimpleVertex(graphModel, superVertex, conceptX, conceptY, concept);
                    conceptToVertexMap.put(concept, simpleVertex);
                    simpleVertex.setHidden(hidden);
                    conceptNameToVertexMap.put(concept.getUri(), simpleVertex);
                    conceptX += simpleVertex.getWidth() + X_GAP;
                    if (superVertex != null) {
                        superVertex.addSimpleVertex(simpleVertex);
                    }
                }
            }
            currentX += superVertexWidth + X_GAP;
        }
        return y + SuperVertex.getVertexHeight() + 4 * Y_GAP;
    }

    private int getSuperVertexWidth(Set<IOntologyConcept> conceptSet) {
        int superVertexWidth = 0;
        for (IOntologyConcept concept : conceptSet) {
            String simpleLabel = concept.getMainLabel();
            superVertexWidth += Vertex.LETTER_WIDTH * simpleLabel.length() + 2 * LABEL_GAP + X_GAP;
        }
        return superVertexWidth;
    }


    private boolean isHidden(boolean showUnmapped, boolean showUnmappedWithSynsets, IOntologyConcept mainConcept, String superLabel) {
        return !showUnmapped && superLabel.equals(SimilarityReason.NO.name()) ||
                !showUnmappedWithSynsets && superLabel.equals(SimilarityReason.WORDNET.name()) && !mainConcept.hasMappedConcepts();
    }

    private String createToolTip(IOntologyConcept mainConcept, Set<IOntologyConcept> conceptSet, Synset synset) {
        StringBuilder result = new StringBuilder("");
        if (synset == null && !mainConcept.hasMappedConcepts()) {
            return null;
        }
        result.append("CONCEPTS: <ul>");
        for (IOntologyConcept concept : conceptSet) {
            result.append("<li>").append(concept.getMainLabel()).append("</li>");
        }
        result.append("</ul>");
        if (synset != null) {
            result.append("<p>SYNSET: ").append(synset.getDefinition()).append("</p>");
        }
        if (mainConcept.hasMappedConcepts()) {
            result.append("MAPPING REASONS: <ul>");
            for (Map.Entry<String, Integer> reason : mainConcept.getConceptToReason().values().iterator().next().entrySet()) {
                result.append("<li>").append(reason.getKey()).append(" (").append(reason.getValue()).append(")");
            }
            result.append("</ul>");
        }
        return result.toString();
    }

    private SimpleVertex createSimpleVertex(IGraphModel graphModel, SuperVertex superVertex, int conceptX, int conceptY, IOntologyConcept concept) {
        SimpleVertex simpleVertex =
                new ConceptVertex(concept, new Point(conceptX, conceptY), superVertex, getColorForConcept(concept));
        initVertex(graphModel, simpleVertex);
        return simpleVertex;
    }

    private void initVertex(IGraphModel graphModel, Vertex vertex) {
        graphModel.addVertex(vertex);
    }

    private SuperVertex createSuperVertex(IGraphModel graphModel,
                                          int currentX, int currentY, String superLabel, int superVertexWidth,
                                          IOntologyConcept mainConcept, Set<IOntologyConcept> conceptSet, Synset synset) {
        String toolTip = createToolTip(mainConcept, conceptSet, synset);
        SuperVertex superVertex = new SuperVertex(new Point(currentX, currentY), superLabel, toolTip);
        initVertex(graphModel, superVertex);
        superVertex.setWidth(superVertexWidth);
        return superVertex;
    }

    private void buildArcs(Map<URI, SimpleVertex> nameToVertex, IGraphModel graphModel) {
        IArcFilter filter = Arc.getArcFilter();
        if (filter == null) {
            Arc.setArcFilter(new ArcFilter());
        }
        Collection<IOntologyConcept> firstConcepts = this.firstOntologyGraph.getConcepts();
        Collection<IOntologyConcept> secondConcepts = this.secondOntologyGraph.getConcepts();
        Collection<IOntologyConcept> allConcepts = new ArrayList<IOntologyConcept>(firstConcepts);
        allConcepts.addAll(secondConcepts);
        for (IOntologyConcept parent : allConcepts) {
            SimpleVertex parentVertex = nameToVertex.get(parent.getUri());
            if (parentVertex == null) {
                continue;
            }
            for (IOntologyConcept child : parent.getParents()) {
                SimpleVertex childVertex = nameToVertex.get(child.getUri());
                if (childVertex != null) {
                    graphModel.addArc(new Arc(parentVertex, childVertex, Collections.EMPTY_LIST, Color.DARK_GRAY));
                }
            }

            for (IOntologyRelation relation : parent.getSubjectRelations()) {
                IOntologyConcept objectConcept = relation.getObject();
                if (objectConcept != null) {
                    SimpleVertex objectVertex = nameToVertex.get(objectConcept.getUri());
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





