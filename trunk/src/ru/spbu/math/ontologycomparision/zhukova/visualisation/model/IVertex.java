package ru.spbu.math.ontologycomparision.zhukova.visualisation.model;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;

/**
 * @author Anna R. Zhukova
 */
public interface IVertex {

    public Point getAbsoluteLocation() ;

    public Point getLocation();
    
    public void setLocation(Point loc);
    
    public int getWidth();

    public void setWidth(int width);

    public int getHeight();

    public void setHeight(int height);

    public void paintIfNeeded(Graphics g, GraphPane graphPane, boolean isSelected);

    public abstract void paint(Graphics g, GraphPane graphPane, boolean isSelected);

    public void setHidden(boolean need);

    public boolean isHidden();

    public boolean hitTest(Point p);

    public String getName();

    public boolean isInRectangleTest(Point left, Point right);

    public Point getMaxPoint();

    public String getToolTipText();

    int getLetterWidth();

    int getLetterHeight();

    Font getFont();

    void setFont(Font font);
}
