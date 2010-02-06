package ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;
import java.util.*;


public class SimpleVertex extends Vertex {
    private final Set<Arc> arcs = new HashSet<Arc>();
    private final SuperVertex superVertex;
    private final Color color;

    public SimpleVertex(Point location, String name, SuperVertex superVertex, Color color) {
        super(name);
        this.superVertex = superVertex;
        setLocation(location);
        this.color = color;
    }

    public Set<Arc> getArcs() {
        return this.arcs;
    }

    public void paint(Graphics g, GraphPane graphPane, boolean isSelected) {
        g.setFont(getFont());
        int width = getWidth();
        int height = getHeight();
        g.setColor(this.color);
        Point location = getAbsoluteLocation();
        g.drawRect(location.x, location.y,
                width, height);
        if (!isSelected) {
            g.setColor(graphPane.getBackground());
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillRect(location.x + 1, location.y + 1,
                width - 1, height - 1);
        g.setColor(this.color);
        g.drawString(getName(), location.x + 5, location.y + getHeight() - 5);
    }

    public String getToolTipText() {
        return getSurname();
    }

    /*public boolean equals(Object obj) {
        if (!(obj instanceof SimpleVertex)) {
            return false;
        }
        SimpleVertex v = (SimpleVertex) obj;
        return this == v || getLabel().equals(v.getLabel()) && getSurname().equals(v.getSurname());
    }

    public int hashCode() {
        return getLabel().hashCode() + 7 * getSurname().hashCode();
    }  */

    public String getSurname() {
        return this.superVertex.getName();
    }

    public void addArc(Arc arc) {
        this.arcs.add(arc);
    }

    public void setLocation(Point p) {
        Point location = new Point(p);
        if (this.superVertex != null) {
            Point pmin = this.superVertex.getMinPoint();
            Point pmax = this.superVertex.getMaxPoint();
            if (!this.superVertex.isHidden()) {
                checkLocation(location, pmin, pmax);
            }
            location.x -= pmin.x;
            location.y -= pmin.y;
        }
        super.setLocation(location);
    }

    private void checkLocation(Point location, Point pmin, Point pmax) {
        location.x = Math.min(location.x, pmax.x - getWidth());
        location.x = Math.max(location.x, pmin.x);
        location.y = Math.min(location.y, pmax.y - getHeight());
        location.y = Math.max(location.y, pmin.y);
    }

    public Point getAbsoluteLocation() {
        Point location = new Point(getLocation());
        if (this.superVertex != null) {
            Point pmin = this.superVertex.getMinPoint();
            Point pmax = this.superVertex.getMaxPoint();
            location.x += pmin.x;
            location.y += pmin.y;
            if (!this.superVertex.isHidden()) {
                checkLocation(location, pmin, pmax);
            }
        }
        return location;
    }

    public SuperVertex getSuperVertex() {
        return this.superVertex;
    }

    public String toString() {
        return this.getName();
    }
}
