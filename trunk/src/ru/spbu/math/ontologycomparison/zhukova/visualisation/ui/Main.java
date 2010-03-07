package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

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
public class Main {
    private boolean isChanged;
    private JFrame frame;

    //panels
    //graph panel
    private final GraphPane graphPane = new GraphPane();
    private final JScrollPane graphScrollPane = new JScrollPane(this.graphPane,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final JPanel descriptionPanel = new JPanel();
    private final JLabel descriptionLabel = new JLabel();
    private JFrame progressFrame;
    private JCheckBox showSingleSynsetVertexCheckBox;
    private JCheckBox showUnmappedConceptsCheckBox;

    public Main() {
        FileChoosers.setMain(this);
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Main().getFrame();
    }

    public JFrame getFrame() {
        if (this.frame == null) {
            this.frame = new JFrame("Ontology Comparision");
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
            this.frame.getContentPane().add(this.getCheckBoxPanel(), BorderLayout.EAST);
            this.descriptionPanel.add(this.descriptionLabel);
            this.frame.getContentPane().add(this.descriptionPanel, BorderLayout.SOUTH);
            initSizes();
            this.frame.setVisible(true);
        }
        return this.frame;
    }

    private void initSizes() {
        this.graphPane.setSize(this.frame.getSize());
        this.graphPane.setPreferredSize(this.frame.getPreferredSize());
        this.graphScrollPane.setSize(this.frame.getSize());
        this.graphScrollPane.setPreferredSize(this.frame.getPreferredSize());
    }

    public void setGraphModel(GraphModel graphModel) {
        //this.progressFrame.setVisible(false);
        this.graphPane.setGraphModel(graphModel);
    }

    public void showProgressBar() {
        if (progressFrame == null) {
            progressFrame = new JFrame();
            progressFrame.setUndecorated(true);
            progressFrame.getContentPane().setLayout(new BorderLayout());
            JProgressBar progressBar = new JProgressBar();
            Dimension d = new Dimension(200, 20);
            progressBar.setPreferredSize(d);
            progressBar.setSize(d);
            progressBar.setBorderPainted(true);
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setValue(0);

            progressBar.setStringPainted(true);
            progressBar.setString("Loading Ontologies...");
            progressFrame.getContentPane().add(progressBar, BorderLayout.CENTER);
            progressBar.setVisible(true);
            progressFrame.pack();
            progressFrame.setResizable(false);
            progressFrame.setLocation((int) getFrame().getLocation().getX() + getFrame().getWidth() / 2,
                    (int) getFrame().getLocation().getY() + getFrame().getHeight() / 2);
            //progressFrame.setLocationRelativeTo(this.getFrame());
            progressFrame.setAlwaysOnTop(true);
            progressFrame.setVisible(true);
            progressBar.setIndeterminate(true);
        }
        progressFrame.setVisible(true);
        progressFrame.requestFocus();
    }

    public void hideProgressBar() {
        if (this.progressFrame != null) {
            this.progressFrame.setVisible(false);
        }
    }

    public void updateDescriptionPanel(String descrintion) {
        this.descriptionLabel.setText(descrintion);
        this.descriptionPanel.repaint();
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
                    for (SuperVertex vertex : graphModel.getSuperVertices()) {
                        if (vertex.getName().equals(SimilarityReason.NO.name())) {
                            //vertex.setHidden(!showUnmapped);
                            for (IVertex subVertex : vertex.getSimpleVertices()) {
                                subVertex.setHidden(!showUnmapped);
                            }
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
