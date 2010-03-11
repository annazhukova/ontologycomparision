package ru.spbu.math.ontologycomparison.zhukova.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class RecursiveAddHelper<C> {

    public void addRecursively(Collection<C> whereToAdd, Collection<C> whoseElementsToAdd, ElementsToAddExtractor<C> extractor) {
        this.addRecursively(whereToAdd, whoseElementsToAdd, extractor, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void addRecursively(Collection<C> whereToAdd, Collection<C> whoseElementsToAdd, ElementsToAddExtractor<C> extractor,
                               int maxLevel, int maxSize) {
        if (whoseElementsToAdd == null || whoseElementsToAdd.isEmpty()) {
            return;
        }
        if (maxLevel <= 1 || whereToAdd.size() > maxSize) {
            return;
        }
        Set<C> result = new LinkedHashSet<C>();
        for (C element : whoseElementsToAdd) {
            result.addAll(extractor.extract(element));
        }
        whereToAdd.addAll(result);
        addRecursively(whereToAdd, result, extractor, maxLevel - 1, maxSize);
    }

    public static interface ElementsToAddExtractor<C> {

        Collection<C> extract(C element);
    }
}
