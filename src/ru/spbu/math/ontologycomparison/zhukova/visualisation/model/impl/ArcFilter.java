package ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArcFilter;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * @author Anna R. Zhukova
 */
public class ArcFilter implements IArcFilter {
    private final Set<String> barriers = new HashSet<String>();

    public void init() {
        this.barriers.add("Type feature dependency");
        this.barriers.add("Element feature dependency");
        this.barriers.add("Type feature dependency: EXTENDING_DEPENDENCY");
        this.barriers.add("Type feature dependency: FIELD_DEPENDENCY");
        this.barriers.add("Type feature dependency: METHOD_ARGUMENT_DEPENDENCY");
        this.barriers.add("Type feature dependency: METHOD_RETURN_TYPE_DEPENDENCY");
        this.barriers.add("Type feature dependency: LOCAL_VARIABLE_DEPENDENCY");

    }

    public boolean accept(String s) {
        /*for (String barrier : this.barriers) {
            if (s.startsWith(barrier)) {
                return false;
            }
        }*/
        return true;
    }

    public boolean addBarrier(String s) {
        return this.barriers.add(s);
    }

    public boolean removeBarrier(String s) {
        return this.barriers.remove(s);
    }

    public void removeAllBarriers() {
        this.barriers.clear();
    }

    public Set<String> getBarriers() {
        return Collections.unmodifiableSet(this.barriers);
    }

    public boolean isABarrier(String s) {
        return this.barriers.contains(s);
    }
}
