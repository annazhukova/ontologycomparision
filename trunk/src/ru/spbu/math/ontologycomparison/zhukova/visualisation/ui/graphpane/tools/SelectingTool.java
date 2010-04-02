package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.IGraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;

import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public class SelectingTool extends Tool {
    private static boolean isOnlySuperVerticesSelection;
    private static Point left;
    private static int rectangleX;
    private static int rectangleY;
    private static int rectangleWidth;
    private static int rectangleHeight;
    private static final SelectingTool INSTANCE = new SelectingTool();
    private PopUpMenu popUpMenu = new PopUpMenu();

    private SelectingTool() {
        super();
    }

    public static boolean isOnlySuperVerticesSelection() {
        return SelectingTool.isOnlySuperVerticesSelection;
    }

    public static void setOnlySuperVerticesSelection(boolean b) {
        SelectingTool.isOnlySuperVerticesSelection = b;
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            IGraphPane graphPane = Tool.getGraphPane();
            Point mouseLocation = e.getPoint();
            Set<IVertex> vertices = graphPane.getSelectedVertices();
            for (IVertex v : vertices) {
                if (v.hitTest(mouseLocation)) {
                    moveOn(mouseLocation);
                    return;
                }
            }
            graphPane.deselectVertices();
            if (!SelectingTool.isOnlySuperVerticesSelection) {
                Set<SimpleVertex> simpleVertices = graphPane.getGraphModel().getSimpleVertices();
                for (SimpleVertex v : simpleVertices) {
                    if (v.hitTest(mouseLocation)) {
                        graphPane.selectVertex(v);
                        moveOn(mouseLocation);
                        return;
                    }
                }
            }
            Set<SuperVertex> superVertices = graphPane.getGraphModel().getSuperVertices();
            for (SuperVertex v : superVertices) {
                /*boolean leftX = v.leftBorderTest(mouseLocation);
                boolean rightX = v.rightBorderTest(mouseLocation);
                boolean topY = v.topBorderTest(mouseLocation);
                boolean bottomY = v.bottomBorderTest(mouseLocation);
                if (leftX || rightX || topY || bottomY) {
                    resizeOn(mouseLocation, v, leftX, rightX, topY, bottomY);
                } else*/
                if (v.hitTest(mouseLocation)) {
                    graphPane.selectVertex(v);
                    moveOn(mouseLocation);
                    return;
                }
            }
            SelectingTool.left = mouseLocation;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            IGraphPane graphPane = Tool.getGraphPane();
            Point mouseLocation = e.getPoint();
            Set<IVertex> vertices = graphPane.getSelectedVertices();
            boolean show = false;
            Set<SuperVertex> forPopUp = new HashSet<SuperVertex>();
            for (IVertex vertex : vertices) {
                if (vertex instanceof SuperVertex) {
                    forPopUp.add((SuperVertex) vertex);
                } else if (vertex instanceof SimpleVertex) {
                    SimpleVertex simple = (SimpleVertex) vertex;
                    if (simple.getSuperVertex() != null) {
                        forPopUp.add(simple.getSuperVertex());
                    }
                }
                if (vertex.hitTest(mouseLocation)) {
                    show = true;
                }
            }
            if (show) {
                showPopUp(mouseLocation, forPopUp.toArray(new SuperVertex[forPopUp.size()]));
                return;
            }
            if (!SelectingTool.isOnlySuperVerticesSelection) {
                Set<SimpleVertex> simpleVertices = graphPane.getGraphModel().getSimpleVertices();
                for (SimpleVertex v : simpleVertices) {
                    if (v.hitTest(mouseLocation) && v.getSuperVertex() != null) {
                        showPopUp(mouseLocation, v.getSuperVertex());
                        return;
                    }
                }
            }
            Set<SuperVertex> superVertices = graphPane.getGraphModel().getSuperVertices();
            for (SuperVertex v : superVertices) {
                /*boolean leftX = v.leftBorderTest(mouseLocation);
                boolean rightX = v.rightBorderTest(mouseLocation);
                boolean topY = v.topBorderTest(mouseLocation);
                boolean bottomY = v.bottomBorderTest(mouseLocation);
                if (leftX || rightX || topY || bottomY) {
                    resizeOn(mouseLocation, v, leftX, rightX, topY, bottomY);
                } else*/
                if (v.hitTest(mouseLocation)) {
                    showPopUp(mouseLocation, v);
                    return;
                }
            }
        }
    }

    private void showPopUp(Point mouseLocation, SuperVertex... vertices) {
        popUpMenu.setGraphPane(getGraphPane());
        popUpMenu.setVertices(vertices);
        popUpMenu.show(getGraphPane(), mouseLocation.x, mouseLocation.y);
    }

    private void resizeOn(Point mouseLocation, SuperVertex v,
                          boolean left, boolean right, boolean top, boolean bottom) {
        Tool.getGraphPane().setTool(ResizingTool.getInstance(mouseLocation, v,
                left, right, top, bottom));
    }

    private void moveOn(Point mouseLocation) {
        Tool.getGraphPane().setTool(MovingTool.getInstance(mouseLocation));
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mouseLocation = e.getPoint();
            GraphPane graphPane = Tool.getGraphPane();
            if (mouseLocation.equals(SelectingTool.left)) {
                if (!SelectingTool.isOnlySuperVerticesSelection) {
                    Set<SimpleVertex> simpleVertices = graphPane.getGraphModel().getSimpleVertices();
                    for (IVertex v : simpleVertices) {
                        if (v.hitTest(mouseLocation)) {
                            graphPane.selectVertex(v);
                            break;
                        }
                    }
                }
                Set<SuperVertex> vertices = graphPane.getGraphModel().getSuperVertices();
                for (IVertex v : vertices) {
                    if (v.hitTest(mouseLocation)) {
                        graphPane.selectVertex(v);
                        break;
                    }
                }

            } else {
                Set<SuperVertex> superVertices = graphPane.getGraphModel().getSuperVertices();
                for (SuperVertex v : superVertices) {
                    if (v.isInRectangleTest(SelectingTool.left, mouseLocation)) {
                        graphPane.selectVertex(v);
                    }
                }
                if (!SelectingTool.isOnlySuperVerticesSelection) {
                    Set<SimpleVertex> simpleVertices = graphPane.getGraphModel().getSimpleVertices();
                    for (SimpleVertex v : simpleVertices) {
                        if (v.isInRectangleTest(SelectingTool.left, mouseLocation) && !graphPane.getSelectedVertices().contains(v.getSuperVertex())) {
                            graphPane.selectVertex(v);
                        }
                    }
                }
            }
            graphPane.repaint();
        }
        SelectingTool.rectangleWidth = 0;
        SelectingTool.rectangleHeight = 0;
    }

    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        SelectingTool.rectangleX = Math.min(SelectingTool.left.x, p.x);
        SelectingTool.rectangleY = Math.min(SelectingTool.left.y, p.y);
        SelectingTool.rectangleWidth = Math.abs(SelectingTool.left.x - p.x);
        SelectingTool.rectangleHeight = Math.abs(SelectingTool.left.y - p.y);
        Tool.getGraphPane().repaint();
    }

    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(SelectingTool.rectangleX, SelectingTool.rectangleY, SelectingTool.rectangleWidth, SelectingTool.rectangleHeight);
    }

    public static SelectingTool getInstance() {
        return SelectingTool.INSTANCE;
    }
}
