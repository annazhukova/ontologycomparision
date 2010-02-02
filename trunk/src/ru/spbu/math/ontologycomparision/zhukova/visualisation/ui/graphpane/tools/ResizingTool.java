package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SuperVertex;

import java.awt.*;
import java.awt.event.MouseEvent;
import static java.lang.Math.round;
import static java.lang.Math.cos;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

/**
 * @author Anna R. Zhukova
 */
public class ResizingTool extends Tool {
    private static boolean moveLeftBorder;
    private static boolean moveTopBorder;
    private static boolean moveRightBorder;
    private static boolean moveBottomBorder;
    private static Point from;
    private static SuperVertex superVertex;

    private static final ResizingTool INSTANCE = new ResizingTool();

    private ResizingTool() {
        super();
    }

    public static ResizingTool getInstance(Point from, SuperVertex v,
                                                    boolean moveLeft, boolean moveRight,
                                                    boolean moveTop, boolean moveBottom) {
        ResizingTool.moveLeftBorder = moveLeft;
        ResizingTool.moveRightBorder = moveRight;
        ResizingTool.moveTopBorder = moveTop;
        ResizingTool.moveBottomBorder = moveBottom;
        ResizingTool.from = from;
        ResizingTool.superVertex = v;
        return INSTANCE;
    }

    public void mouseDragged(MouseEvent e) {
        Point mouseLocation = e.getPoint();
        int dx = mouseLocation.x - ResizingTool.from.x;
        int dy = mouseLocation.y - ResizingTool.from.y;
        GraphPane graphPane = Tool.getGraphPane();
        if (ResizingTool.moveLeftBorder) {
            ResizingTool.superVertex.moveLeftBorder(dx);
        }
        if (ResizingTool.moveRightBorder) {
            ResizingTool.superVertex.moveRightBorder(dx);
        }
        if (ResizingTool.moveBottomBorder) {
            ResizingTool.superVertex.moveBottomBorder(dy);
        }
        if (ResizingTool.moveTopBorder) {
            ResizingTool.superVertex.moveTopBorder(dy);
        }
        ResizingTool.from = mouseLocation;
        graphPane.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        Tool.getGraphPane().setTool(SelectingTool.getInstance());
    }

    public void paint(Graphics g) {
        double direction = getArrowDirection();
        int x1 = (int) (ResizingTool.from.x + 10 * Math.cos(direction));
        int y1 = (int) (ResizingTool.from.y + 10 * Math.sin(direction));
        paintArrow(direction, x1, y1, Math.PI / 8, 5, g);
        direction += Math.PI;
        int x2 = (int) (ResizingTool.from.x + 10 * Math.cos(direction));
        int y2 = (int) (ResizingTool.from.y + 10 * Math.sin(direction));
        paintArrow(direction, x2, y2, Math.PI / 8, 5, g);
        g.drawLine(x1, y1, x2, y2);
    }

    private double getArrowDirection() {
        if (ResizingTool.moveLeftBorder) {
            if (ResizingTool.moveTopBorder) {
                return Math.PI / 4;
            }
            if (ResizingTool.moveBottomBorder) {
                return Math.PI * 3 / 4;
            }
            return 0;
        }
        if (ResizingTool.moveRightBorder) {
            if (ResizingTool.moveTopBorder) {
                return Math.PI * 3 / 4;
            }
            if (ResizingTool.moveBottomBorder) {
                return Math.PI / 4;
            }
            return 0;
        }
        return Math.PI / 2;
    }

    private static void paintArrow(double direction, int x0, int y0, double beta, int arrowLength, Graphics g) {
        g.setColor(Color.RED);
        int xr = x0 + (int) round((arrowLength * cos(PI + direction - beta)));
        int yr = y0 + (int) round((arrowLength * sin(PI + direction - beta)));
        int xl = x0 + (int) round((arrowLength * cos(PI + direction + beta)));
        int yl = y0 + (int) round((arrowLength * sin(PI + direction + beta)));

        g.drawLine(x0, y0, xr, yr);
        g.drawLine(x0, y0, xl, yl);
    }
}
