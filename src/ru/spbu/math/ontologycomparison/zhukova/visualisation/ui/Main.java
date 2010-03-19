package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.SelectingTool;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions.Open;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.TreeComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author Anna R. Zhukova
 */
public class Main implements ILogger {
    private boolean isChanged;
    private JFrame frame;

    //panels
    //graph panel
    private final GraphPane graphPane = new GraphPane();
    private final JScrollPane graphScrollPane = new JScrollPane(this.graphPane,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final JPanel infoPanel = new JPanel();
    private final JScrollPane infoScrollPane = new JScrollPane(this.infoPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final JPanel logPanel = new JPanel();
    private final JSplitPane logSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logPanel, infoScrollPane);
    private JLabel logLabel;
    /*private JCheckBox showSingleSynsetVertexCheckBox;
    private JCheckBox showUnmappedConceptsCheckBox;*/
    private JFrame progressFrame;
    private JProgressBar progressBar = new JProgressBar();
    private final TreeComponent trees = new TreeComponent();
    private final JPanel checkBoxPanel = new JPanel();
    private final JSplitPane visibilitySettingsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, trees, checkBoxPanel);
    private final JSplitPane mySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, visibilitySettingsSplitPane, graphScrollPane);
    private final JSplitPane general = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mySplitPane, logSplitPane);


    public void setTrees(IPair<JTree, Map<IOntologyConcept, CheckNode>> firstTree, IPair<JTree, Map<IOntologyConcept, CheckNode>> secondTree) {
        trees.setTrees(firstTree, secondTree);
        frame.getContentPane().repaint();
    }


    public Main() {
        FileChoosers.setMain(this);
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Main().getFrame();
    }

    public JFrame getFrame() {
        if (this.frame == null) {
            this.frame = new JFrame("Ontology Comparison");
            Open.setMain(this);
            this.frame.setSize(1200, 750);
            this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            this.frame.setJMenuBar(Menu.getMenuBar());
            this.frame.getContentPane().setLayout(new BorderLayout());
            this.frame.getContentPane().add(ToolBar.getToolBar(), BorderLayout.NORTH);

            this.frame.getContentPane().add(general, BorderLayout.CENTER);
            initLabelsPanel();
            initCheckBoxPanel();
            initSizes();
            general.setDividerLocation(0.7);
            this.frame.setVisible(true);
        }
        return this.frame;
    }

    private void initLabelsPanel() {
        Dimension dimension = new Dimension(300, -1);
        initInfoPanel(dimension);
        initLogPanel(dimension);
    }

    private void initInfoPanel(Dimension size) {
        final JLabel mergedEntityStatisticsLabel = new JLabel("");
        infoPanel.add(mergedEntityStatisticsLabel);
        this.graphPane.addListener(new GraphPane.SuperVertexSelectionListener() {
            public void selectionCleared() {
                updateLabel("<html>", mergedEntityStatisticsLabel, infoPanel, true);
            }

            public void vertexSelected(String message) {
                updateLabel(message, mergedEntityStatisticsLabel, infoPanel, false);
            }
        });
        infoPanel.setMaximumSize(size);
        infoPanel.setPreferredSize(size);
    }

    private void initLogPanel(Dimension size) {
        this.logLabel = new JLabel("<html><p>Press \"Open\" to select ontologies to compare<p>");
        this.logPanel.add(this.logLabel);
        this.logPanel.setMaximumSize(size);
        this.logPanel.setPreferredSize(size);
    }

    private void updateLabel(String message, JLabel label, JPanel panel, boolean clear) {
        if (clear) {
            label.setText(message);
        } else {
            label.setText(String.format("%s<br>%s", label.getText(), message));
        }
        panel.repaint();
    }

    private void initSizes() {
        this.graphPane.setSize(this.frame.getSize());
        this.graphPane.setPreferredSize(this.frame.getPreferredSize());
        this.graphScrollPane.setSize(this.frame.getSize());
        this.graphScrollPane.setPreferredSize(this.frame.getPreferredSize());

        mySplitPane.setSize(frame.getSize());
        mySplitPane.setPreferredSize(frame.getPreferredSize());
        mySplitPane.setDividerLocation(0.7);
        visibilitySettingsSplitPane.setDividerLocation(0.5);
        logSplitPane.setDividerLocation(0.5);
        logPanel.setMaximumSize(logPanel.getPreferredSize());
    }

    public void setGraphModel(final GraphModel graphModel) {
        //this.progressFrame.setVisible(false);
        graphPane.setGraphModel(graphModel);
        trees.setGraphPane(graphPane);
        mySplitPane.setDividerLocation(0.7);
        visibilitySettingsSplitPane.setDividerLocation(0.8);
        logSplitPane.setDividerLocation(0.2);
        general.setDividerLocation(0.7);
    }

    public void showProgressBar() {
        if (progressFrame == null) {
            progressFrame = new JFrame();
            progressFrame.setUndecorated(true);
            progressFrame.getContentPane().setLayout(new BorderLayout());
            Dimension d = new Dimension(200, 20);
            progressBar.setPreferredSize(d);
            progressBar.setSize(d);
            progressBar.setBorderPainted(true);
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setValue(0);
            progressBar.setIndeterminate(true);
            progressFrame.getContentPane().add(progressBar, BorderLayout.CENTER);
            progressFrame.pack();
            progressFrame.setResizable(false);
            progressFrame.setLocation((int) getFrame().getLocation().getX() + getFrame().getWidth() / 2,
                    (int) getFrame().getLocation().getY() + getFrame().getHeight() / 2);
            //progressFrame.setLocationRelativeTo(this.getFrame());
            progressFrame.setAlwaysOnTop(true);
        }
        progressFrame.setVisible(true);
        progressFrame.requestFocus();
    }

    public void hideProgressBar() {
        if (this.progressFrame != null) {
            this.progressFrame.setVisible(false);
        }
    }

    public void log(final String description) {
        updateLabel(String.format("<html><p>%s</p>", description), logLabel, logPanel, true);
    }

    public IGraphModel getGraphModel() {
        return this.graphPane.getGraphModel();
    }

    public GraphPane getGraphPane() {
        return this.graphPane;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    private void initCheckBoxPanel() {
        this.checkBoxPanel.setLayout(new GridLayout(1, 1));
        /*this.showUnmappedConceptsCheckBox = new JCheckBox("Show unmapped concepts with no synsets");
        showUnmappedConceptsCheckBox.setSelected(true);
        showUnmappedConceptsCheckBox.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                final boolean showUnmapped = showUnmappedConceptsCheckBox.isSelected();
                IGraphModel graphModel = Main.this.getGraphModel();
                if (graphModel != null) {
                    for (SimpleVertex vertex : graphModel.getSimpleVertices()) {
                        if (vertex.getSuperVertex() == null) {
                            vertex.setHidden(!showUnmapped);
                        }
                    }
                    graphModel.update();
                }
            }
        });
        checkBoxPanel.add(showUnmappedConceptsCheckBox);

        this.showSingleSynsetVertexCheckBox = new JCheckBox("Show unmapped concepts with synsets");
        showSingleSynsetVertexCheckBox.setSelected(true);
        showSingleSynsetVertexCheckBox.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                final boolean showSingleSynsetVertex = showSingleSynsetVertexCheckBox.isSelected();
                IGraphModel graphModel = Main.this.getGraphModel();
                if (graphModel != null) {
                    for (SuperVertex vertex : graphModel.getSuperVertices()) {
                        if (vertex.getName().equals(SimilarityReason.WORDNET.name())) {
                            Set<SimpleVertex> vertexSet = vertex.getSimpleVertices();
                            if (vertexSet != null && vertexSet.size() <= 1) {
                                vertex.setHidden(!showSingleSynsetVertex);
                            }
                        }
                    }
                    graphModel.update();
                }
            }
        });
        checkBoxPanel.add(showSingleSynsetVertexCheckBox);
        */
        final JCheckBox onlyOuterNodesSelection = new JCheckBox("Select only outer nodes", false);
        onlyOuterNodesSelection.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                SelectingTool.setOnlySuperVerticesSelection(onlyOuterNodesSelection.isSelected());
                graphPane.deselectVertices();
            }
        });
        checkBoxPanel.add(onlyOuterNodesSelection);
    }

    public boolean areUnmappedConceptsVisible() {
        return true;
        //return showUnmappedConceptsCheckBox.isSelected();
    }

    public boolean areUnmappedConceptsWithSynsetsVisible() {
        return true;
        //return showSingleSynsetVertexCheckBox.isSelected();
    }
}
