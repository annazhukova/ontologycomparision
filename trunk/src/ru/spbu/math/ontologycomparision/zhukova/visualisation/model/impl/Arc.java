package ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IArc;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IArcFilter;

import java.awt.*;
import static java.lang.Math.*;
import java.util.List;

/**
 * @author Anna R. Zhukova
 */
public class Arc implements IArc {
    private static IArcFilter arcFilter;

    public static void setArcFilter(IArcFilter arcFilter) {
        Arc.arcFilter = arcFilter;
    }

    public static IArcFilter getArcFilter() {
        return arcFilter;
    }

    private final SimpleVertex fromVertex;
    private final SimpleVertex toVertex;
    private final List<String> labels;

    public Arc(SimpleVertex from, SimpleVertex to, List<String> labels) {
        this.fromVertex = from;
        this.toVertex = to;
        this.labels = labels;
    }

    public SimpleVertex getFromVertex() {
        return this.fromVertex;
    }

    public SimpleVertex getToVertex() {
        return this.toVertex;
    }


    public List<String> getLabels() {
        return this.labels;
    }

    public String getToolTipHTML() {
        StringBuilder result = new StringBuilder("<html>");
        for (String s : this.labels) {
            if (Arc.arcFilter == null || Arc.arcFilter.accept(s)) {
                result.append(s).append("<br>");
            }
        }
        return result.toString();
    }

    public void paintIfNeeded(Graphics g) {
        if (!isHidden()) {
            paint(g);
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        Point loc = this.fromVertex.getAbsoluteLocation();
        int x1 = loc.x + this.fromVertex.getWidth() / 2;
        int y1 = loc.y + this.fromVertex.getHeight() / 2;
        loc = toVertex.getAbsoluteLocation();
        int x2 = loc.x + this.toVertex.getWidth() / 2;
        int y2 = loc.y + this.toVertex.getHeight() / 2;
        g.drawLine(x1, y1, x2, y2);
        paintArrow(atan2(y2 - y1, x2 - x1), x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2, PI / 8, 10, g);
        int labelsCount = getLabels().size();
        for (String label : getLabels()) {
            g.drawString(label, x1 + (x2 - x1) / 2 + 5, y1 + (y2 - y1) / 2 - labelsCount-- * 5);
        }
    }

    private static void paintArrow(double direction, int x0, int y0, double beta, int arrowLength, Graphics g) {
        g.setColor(Color.BLACK);
        int xr = x0 + (int) round((arrowLength * cos(PI + direction - beta)));
        int yr = y0 + (int) round((arrowLength * sin(PI + direction - beta)));
        int xl = x0 + (int) round((arrowLength * cos(PI + direction + beta)));
        int yl = y0 + (int) round((arrowLength * sin(PI + direction + beta)));

        g.drawLine(x0, y0, xr, yr);
        g.drawLine(x0, y0, xl, yl);
    }

    public boolean hitTest(Point p) {
        Point fromLocation = getFromVertex().getAbsoluteLocation();
        Point toLocation = getToVertex().getAbsoluteLocation();
        int r0 = (int) (pow(pow(fromLocation.x - p.x, 2) + pow(fromLocation.y - p.y, 2), 0.5));
        double beta = atan2(fromLocation.y - p.y, p.x - fromLocation.x) - atan2(fromLocation.y - toLocation.y, toLocation.x - fromLocation.x);
        int x0 = (int) (r0 * cos(beta));
        int y0 = (int) (r0 * sin(beta));
        int r = (int) (pow(pow(fromLocation.x - toLocation.x, 2) + pow(fromLocation.y - toLocation.y, 2), 0.5));
        return x0 >= 10 && x0 <= r - 10 && y0 >= -5 && y0 <= 5;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Arc)) {
            return false;
        }
        Arc arc = (Arc) obj;
        return this == arc || this.fromVertex.equals(arc.fromVertex) && this.toVertex.equals(arc.toVertex);
    }

    public int hashCode() {
        return this.fromVertex.hashCode() + 7 * this.toVertex.hashCode();
    }

    public boolean isHidden() {
        return this.fromVertex.isHidden() || this.toVertex.isHidden() || "<html>".equals(getToolTipHTML());
    }
}
