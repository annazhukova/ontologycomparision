package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.SimilarityReason;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions.Open;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public class Main implements ILogger {
    private boolean isChanged;
    private JFrame frame;

    //panels
    //graph panel
    private final GraphPane graphPane;
    private final JScrollPane graphScrollPane;
    private JPanel logPanel;
    private JLabel logLabel;
    private JCheckBox showSingleSynsetVertexCheckBox;
    private JCheckBox showUnmappedConceptsCheckBox;
    private JFrame progressFrame;
    private JProgressBar progressBar = new JProgressBar();

    public Main() {
        this.graphPane = new GraphPane();
        this.graphScrollPane = new JScrollPane(this.graphPane,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
            this.frame.getContentPane().add(this.graphScrollPane, BorderLayout.CENTER);
            JPanel panel = new JPanel(new GridLayout(3, 1));
            Dimension dimension = new Dimension(300, -1);
            panel.add(getStatusPanel(dimension));
            panel.add(this.getCheckBoxPanel());
            panel.add(getLogPanel(dimension));
            panel.setMaximumSize(dimension);
            panel.setPreferredSize(dimension);
            this.frame.getContentPane().add(panel, BorderLayout.EAST);
            initSizes();
            this.frame.setVisible(true);
        }
        return this.frame;
    }

    private JPanel getStatusPanel(Dimension size) {
        final JPanel descriptionPanel = new JPanel();
        final JLabel mergedEntityStatisticsLabel = new JLabel("");
        descriptionPanel.add(mergedEntityStatisticsLabel);
        this.graphPane.addListener(new GraphPane.SuperVertexSelectionListener() {
            public void selectionCleared() {
                updateLabel("<html>", mergedEntityStatisticsLabel, descriptionPanel, true);
            }

            public void vertexSelected(String message) {
                updateLabel(message, mergedEntityStatisticsLabel, descriptionPanel, false);
            }
        });
        descriptionPanel.setMaximumSize(size);
        descriptionPanel.setPreferredSize(size);
        return descriptionPanel;
    }

    private JPanel getLogPanel(Dimension size) {
        if (this.logPanel == null) {
            this.logPanel = new JPanel();
            this.logLabel = new JLabel("<html><p>Press \"Open\" to select ontologies to compare<p>");
            this.logPanel.add(this.logLabel);
            this.logPanel.setMaximumSize(size);
            this.logPanel.setPreferredSize(size);
        }
        return this.logPanel;
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
    }

    public void setGraphModel(final GraphModel graphModel) {
        //this.progressFrame.setVisible(false);
        graphPane.setGraphModel(graphModel);

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

    private JPanel getCheckBoxPanel() {
        JPanel result = new JPanel(new GridLayout(2, 1));
        this.showUnmappedConceptsCheckBox = new JCheckBox("Show unmapped concepts with no synsets");
        showUnmappedConceptsCheckBox.setSelected(true);
        showUnmappedConceptsCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        result.add(showUnmappedConceptsCheckBox);

        this.showSingleSynsetVertexCheckBox = new JCheckBox("Show unmapped concepts with synsets");
        showSingleSynsetVertexCheckBox.setSelected(true);
        showSingleSynsetVertexCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final boolean showSingleSynsetVertex = showSingleSynsetVertexCheckBox.isSelected();
                IGraphModel graphModel = Main.this.getGraphModel();
                if (graphModel != null) {
                    for (SuperVertex vertex : graphModel.getSuperVertices()) {
                        if (vertex.getName().equals(SimilarityReason.WORDNET.name())) {
                            Set<SimpleVertex> vertexSet = vertex.getSimpleVertices();
                            if (vertexSet != null && vertexSet.size() <= 1) {
                                vertex.setHidden(!showSingleSynsetVertex);
                                for (IVertex subVertex : vertexSet) {
                                    subVertex.setHidden(!showSingleSynsetVertex);
                                }
                            }
                        }
                    }
                    graphModel.update();
                }
            }
        });
        result.add(showSingleSynsetVertexCheckBox);
        return result;
    }

    public boolean areUnmappedConceptsVisible() {
        return showUnmappedConceptsCheckBox.isSelected();
    }

    public boolean areUnmappedConceptsWithSynsetsVisible() {
        return showSingleSynsetVertexCheckBox.isSelected();
    }
}
