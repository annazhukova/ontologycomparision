package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Anna R. Zhukova
 */
public interface ITool extends MouseListener, MouseMotionListener {

    void paint(Graphics g);
}
