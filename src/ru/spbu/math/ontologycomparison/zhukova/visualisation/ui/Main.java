package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.SimilarityReason;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.SelectingTool;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions.Open;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions.Save;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.TreeComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

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
    private final JPanel infoPanel = new JPanel(new BorderLayout());
    private final JScrollPane infoScrollPane = new JScrollPane(this.infoPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final JPanel logPanel = new JPanel(new BorderLayout());
    private final JSplitPane logSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logPanel, infoScrollPane);
    private JLabel logLabel;
    private JCheckBox showSingleSynsetVertexCheckBox;
    private JCheckBox showUnmappedConceptsCheckBox;
    private JFrame progressFrame;
    private JProgressBar progressBar = new JProgressBar();
    private final TreeComponent trees = new TreeComponent();
    private final JPanel checkBoxPanel = new JPanel();
    private final JSplitPane visibilitySettingsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, trees, checkBoxPanel);
    private final JSplitPane componentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, visibilitySettingsSplitPane, graphScrollPane);
    private final JSplitPane general = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentSplitPane, logSplitPane);


    public void setTrees(IPair<JTree, SetHashTable<IOntologyConcept, CheckNode>> firstTree, IPair<JTree, SetHashTable<IOntologyConcept, CheckNode>> secondTree) {
        trees.setTrees(firstTree, secondTree);
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
            this.frame.setSize(1200, 750);
            this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            AbstractAction[] actions = getActions();
            this.frame.setJMenuBar(Menu.getMenuBar(actions));
            this.frame.getContentPane().setLayout(new BorderLayout());
            this.frame.getContentPane().add(ToolBar.getToolBar(actions), BorderLayout.NORTH);

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
        infoPanel.add(mergedEntityStatisticsLabel, BorderLayout.CENTER);
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
        this.logPanel.add(this.logLabel, BorderLayout.CENTER);
        this.logPanel.setMaximumSize(size);
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

        componentSplitPane.setSize(frame.getSize());
        componentSplitPane.setPreferredSize(frame.getPreferredSize());
        componentSplitPane.setDividerLocation(0.7);
        visibilitySettingsSplitPane.setDividerLocation(0.5);
        logSplitPane.setDividerLocation(0.5);
        logPanel.setMaximumSize(logPanel.getPreferredSize());
    }

    public void setGraphModel(final GraphModel graphModel) {
        //this.progressFrame.setVisible(false);
        graphPane.setGraphModel(graphModel);
        trees.setGraphPane(graphPane);
        componentSplitPane.setDividerLocation(0.7);
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
        this.checkBoxPanel.setLayout(new GridLayout(3, 1));
        this.showUnmappedConceptsCheckBox = new JCheckBox("Show unmapped concepts");
        showUnmappedConceptsCheckBox.setSelected(false);
        showUnmappedConceptsCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final boolean showUnmapped = showUnmappedConceptsCheckBox.isSelected();
                IGraphModel graphModel = Main.this.getGraphModel();
                if (graphModel != null) {
                    graphModel.showNoParentVertices(showUnmapped);
                }
            }
        });
        checkBoxPanel.add(showUnmappedConceptsCheckBox);

        this.showSingleSynsetVertexCheckBox = new JCheckBox("Show concepts mapped to WordNet only");
        showSingleSynsetVertexCheckBox.setSelected(false);
        showSingleSynsetVertexCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final boolean showSingleSynsetVertex = showSingleSynsetVertexCheckBox.isSelected();
                IGraphModel graphModel = Main.this.getGraphModel();
                if (graphModel != null) {
                    graphModel.showSingleVerticesWithSuchNamedParent(showSingleSynsetVertex, SimilarityReason.WORDNET.name());
                }
            }
        });
        checkBoxPanel.add(showSingleSynsetVertexCheckBox);
        final JCheckBox onlyOuterNodesSelection = new JCheckBox("Select only outer nodes", false);
        onlyOuterNodesSelection.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SelectingTool.setOnlySuperVerticesSelection(onlyOuterNodesSelection.isSelected());
                graphPane.deselectVertices();
            }
        });
        checkBoxPanel.add(onlyOuterNodesSelection);
    }

    public boolean areUnmappedConceptsVisible() {
        return showUnmappedConceptsCheckBox.isSelected();
    }

    public boolean areUnmappedConceptsWithSynsetsVisible() {
        return showSingleSynsetVertexCheckBox.isSelected();
    }

    public AbstractAction[] getActions() {
        Open open = new Open();
        open.setMain(this);
        Save save = new Save();
        save.setMain(this);
        open.addListener(save);
        return new AbstractAction[]{open, save};
    }
}
