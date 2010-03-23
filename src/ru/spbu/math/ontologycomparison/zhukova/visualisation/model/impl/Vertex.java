package ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;

import java.awt.*;

/**
 * @author Anna R. Zhukova
 */
public abstract class Vertex implements IVertex {
    private int x;
    private int y;
    private boolean isHidden = false;

    public Point getLocation() {
        return new Point(x, y);
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void setY(int y) {
        this.y = y;
    }    

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }
}
