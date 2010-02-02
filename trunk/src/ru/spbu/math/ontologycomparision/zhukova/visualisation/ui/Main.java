package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.UndoManager;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.IUndoManager;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions.Exit;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions.Undo;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions.Redo;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions.Open;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

/**
 * @author Anna R. Zhukova
 */
public class Main {
    private boolean isChanged;
    private JFrame frame;

    //panels
    //graph panel
    private final GraphPane graphPane = new GraphPane();
    private final JScrollPane graphScrollPane = new JScrollPane(this.graphPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final JPanel descriptionPanel = new JPanel();
    private final JLabel descriptionLabel = new JLabel();

    public Main() {
        FileChoosers.setMain(this);
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Main().getFrame();
    }

    public JFrame getFrame() {
        if (this.frame == null) {
            this.frame = new JFrame("Ontology Comparision");
            Exit.setFrame(this.frame);
            Open.setMain(this);
            this.frame.setSize(1200, 750);
            this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    /*if (isChanged) {
                        Exit.confirmExit();
                    } else {*/
                        System.exit(0);
                    /*}*/
                }
            });
            this.frame.setJMenuBar(Menu.getMenuBar());
            this.frame.getContentPane().setLayout(new BorderLayout());
            this.frame.getContentPane().add(ToolBar.getToolBar(), BorderLayout.NORTH);
            this.frame.getContentPane().add(this.graphScrollPane, BorderLayout.CENTER);

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
        IUndoManager undoManager = new UndoManager();
        Undo.setUndoManager(undoManager);
        Redo.setUndoManager(undoManager);
        this.graphPane.setGraphModel(graphModel, undoManager);
    }

    public void updateDescriptionPanel(String descrintion) {
        this.descriptionLabel.setText(descrintion);
        this.descriptionLabel.repaint();
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
}
