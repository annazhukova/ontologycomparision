package ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Anna R. Zhukova
 */
public class SuperVertex extends Vertex {
    private boolean isCollapsed;
    private final Set<SimpleVertex> simpleVertices = new HashSet<SimpleVertex>();
    private static final Color COLOR = Color.BLACK;
    private static final Color SELECTED_COLOR = new Color(192, 192, 192, 50);

    public SuperVertex(Point location, String name) {
        super(name);
        setLocation(location);
    }

    public void addSimpleVertex(SimpleVertex vertex) {
        this.simpleVertices.add(vertex);
    }

    public Set<SimpleVertex> getSimpleVertices() {
        return new HashSet<SimpleVertex>(this.simpleVertices);
    }

    public boolean isCollapsed() {
        return this.isCollapsed;
    }

    public void collapse(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    public Point getAbsoluteLocation() {
        return getLocation();
    }

    public void paint(Graphics g, GraphPane graphPane, boolean isSelected) {
        int width = getWidth();
        int height = getHeight();
        Point location = getAbsoluteLocation();
        if (isCollapsed()) {
            width = getLetterWidth() * getName().length();
            height = getLetterHeight() + 4;
        }
        g.setFont(getFont());
        g.setColor(SuperVertex.COLOR);
        g.drawRect(location.x, location.y,
                width, height);
        if (isSelected) {
            g.setColor(SuperVertex.SELECTED_COLOR);
            g.fillRect(location.x + 1, location.y + 1,
                    width - 1, height - 1);
        }
        g.setColor(SuperVertex.COLOR);
        if (isCollapsed()) {
            g.drawString(getName(), location.x + 5, location.y + 5);
        } else {
            int lettersNumber = width / g.getFontMetrics().getWidths()['w'];
            String title = (getName().length() <= lettersNumber) ? getName() : getName().substring(0, lettersNumber - 3) + "...";
            g.drawString(title, location.x + 5, location.y + 10);
        }
    }

    public String getToolTipText() {
        return getName();
    }

    /*public boolean equals(Object obj) {
        if (!(obj instanceof SuperVertex)) {
            return false;
        }
        SuperVertex v = (SuperVertex) obj;
        return this == v || getLabel().equals(v.getLabel());
    }

    public int hashCode() {
        return getLabel().hashCode();
    }*/

    public boolean leftBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int height = getHeight();
        return (isNear(p.x, x) && isBetween(p.y, y, y + height));
    }

    public boolean rightBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int width = getWidth();
        int height = getHeight();
        return (isNear(p.x, x + width) && isBetween(p.y, y, y + height));
    }

    public boolean topBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int width = getWidth();
        return (isNear(p.y, y) && isBetween(p.x, x, x + width));
    }

    public boolean bottomBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int width = getWidth();
        int height = getHeight();
        return (isNear(p.y, y + height) && isBetween(p.x, x, x + width));
    }

    private static boolean isNear(int a, int b) {
        return Math.abs(a - b) <= 5;
    }

    private static boolean isBetween(int a, int left, int right) {
        return (a - left) >= -5 && (right - a) >= -5;
    }

    private int getMinWidth() {
        int minWidth = 3 * getLetterWidth() + 6;
        for (SimpleVertex vertex : this.simpleVertices) {
            if (!vertex.isHidden()) {
                minWidth = Math.max(minWidth, vertex.getWidth() + 4);
            }
        }
        return minWidth;
    }

    private int getMinHeight() {
        int minHeight = getLetterHeight();
        for (SimpleVertex vertex : this.simpleVertices) {
            if (!vertex.isHidden()) {
                minHeight = Math.max(minHeight, vertex.getHeight() + 4);
            }
        }
        return minHeight;
    }

    public void moveLeftBorder(int dx) {
        int width = getWidth();
        if (dx > 0) {
            dx = Math.min(Math.abs(width - getMinWidth()), dx);
        }
        setWidth(width - dx);
        setLocation(new Point(getAbsoluteLocation().x + dx, getAbsoluteLocation().y));
    }

    public void moveRightBorder(int dx) {
        int width = getWidth();
        if (dx < 0) {
            dx = -Math.min(Math.abs(width - getMinWidth()), -dx);
        }
        setWidth(width + dx);
    }

    public void moveTopBorder(int dy) {
        int height = getHeight();
        if (dy > 0) {
            dy = Math.min(Math.abs(height - getMinHeight()), dy);
        }
        setHeight(height - dy);
        setLocation(new Point(getAbsoluteLocation().x, getAbsoluteLocation().y + dy));
    }

    public void moveBottomBorder(int dy) {
        int height = getHeight();
        if (dy < 0) {
            dy = -Math.min(Math.abs(height - getMinHeight()), -dy);
        }
        setHeight(height + dy);
    }
}
