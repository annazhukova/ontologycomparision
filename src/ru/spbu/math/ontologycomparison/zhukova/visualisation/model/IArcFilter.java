package ru.spbu.math.ontologycomparison.zhukova.visualisation.model;

import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public interface IArcFilter {

    void init();

    boolean accept(String s);

    boolean addBarrier(String s);

    boolean removeBarrier(String s);

    void removeAllBarriers();

    Set<String> getBarriers();

    boolean isABarrier(String s);
}
