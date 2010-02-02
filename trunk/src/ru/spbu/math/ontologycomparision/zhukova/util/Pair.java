package ru.spbu.math.ontologycomparision.zhukova.util;

public class Pair<F,S> implements IPair<F, S> {
    private final F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public String toString() {
        return this.first + ", " + this.second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}