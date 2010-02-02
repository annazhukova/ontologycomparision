package ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IVertex;

import java.awt.*;

/**
 * @author Anna R. Zhukova
 */
public abstract class Vertex implements IVertex {
    private Point location;
    private final String name;
    private int width;
    private int height;
    private boolean isHidden = false;
    private int letterHeight = 21;
    private int letterWidth = 9;
    private Font font = new Font(Font.MONOSPACED, Font.ITALIC, 15);

    public Vertex(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return new Point(this.location);
    }

    public void setLocation(Point p) {
        this.location = new Point(p);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void paintIfNeeded(Graphics g, GraphPane graphPane, boolean isSelected) {           
        if (!isHidden()) {
            paint(g, graphPane, isSelected);
        }
    }

    public abstract void paint(Graphics g, GraphPane graphPane, boolean isSelected);

    public void setHidden(boolean need) {
        this.isHidden = need;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public boolean hitTest(Point p) {
        return (p.x >= (getAbsoluteLocation().x))
                && (p.x <= (getAbsoluteLocation().x + getWidth()))
                && (p.y >= (getAbsoluteLocation().y))
                && (p.y <= (getAbsoluteLocation().y + getHeight()));
    }

    public String getName() {
        return this.name;
    }

    public boolean isInRectangleTest(Point left, Point right) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int inXLeft = Math.min(left.x, right.x);
        int inYLeft = Math.min(left.y, right.y);
        int inXRight = Math.max(left.x, right.x);
        int inYRight = Math.max(left.y, right.y);
        return !((x + getWidth() < inXLeft) || (x > inXRight)
                || (y + getHeight() < inYLeft) || (y > inYRight));
    }

    public Point getMaxPoint() {
        return new Point(getAbsoluteLocation().x + getWidth(),
                getAbsoluteLocation().y + getHeight());
    }

    public Point getMinPoint() {
        return new Point(getAbsoluteLocation());
    }

    public void setLetterWidth(int letterWidth) {
        this.letterWidth = letterWidth;
    }

    public void setLetterHeight(int letterHeight) {
        this.letterHeight = letterHeight;
    }

    public int getLetterWidth() {
        return this.letterWidth;
    }

    public int getLetterHeight() {
        return this.letterHeight;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }
}
