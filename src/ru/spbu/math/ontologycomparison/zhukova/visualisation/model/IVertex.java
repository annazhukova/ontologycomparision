package ru.spbu.math.ontologycomparison.zhukova.visualisation.model;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;

/**
 * @author Anna R. Zhukova
 */
public interface IVertex {       
    static final int LETTER_HEIGHT = 17;
    static final int LETTER_WIDTH = 7;
    static final Font FONT = new Font(Font.MONOSPACED, Font.ITALIC, 12);
    static final int LABEL_GAP = 3;
    static final int Y_GAP = 1;

    Point getAbsoluteLocation() ;

    Point getLocation();
    
    void setLocation(int x, int y);

    void setLocation(Point p);
    
    int getWidth();

    int getHeight();

    void paintIfNeeded(Graphics g, GraphPane graphPane, boolean isSelected);

    abstract void paint(Graphics g, GraphPane graphPane, boolean isSelected);

    void setHidden(boolean need);

    boolean isHidden();

    boolean hitTest(Point p);

    String getName();

    boolean isInRectangleTest(Point left, Point right);

    Point getMaxPoint();

    Point getMinPoint();

    String getToolTipText();

    void setY(int y);

    int getY();

    void setX(int x);

    int getX();
}
