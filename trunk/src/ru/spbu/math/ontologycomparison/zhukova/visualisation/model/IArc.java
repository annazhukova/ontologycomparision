package ru.spbu.math.ontologycomparison.zhukova.visualisation.model;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;

import java.util.List;
import java.awt.*;

/**
 * @author Anna R. Zhukova
 */
public interface IArc {

    public SimpleVertex getFromVertex();

    public SimpleVertex getToVertex();

    public List<String> getLabels();

    public String getToolTipHTML();
    
    public void paintIfNeeded(Graphics g);

    public void paint(Graphics g);

    public boolean hitTest(Point p);

    public boolean isHidden();
}
