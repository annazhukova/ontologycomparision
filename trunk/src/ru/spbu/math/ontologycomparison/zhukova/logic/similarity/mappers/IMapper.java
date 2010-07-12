package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;

/**
 * @author Anna Zhukova
 */
public interface IMapper<C1, C2, P, R> {

    /**
     * Bind factors are reasons of mapping.
     * Example of bind factor: new Triple<WordNetRelation, String, String>(WordNetRelation.HYPONYM, SAME_PARENTS, SAME_CHILDREN)
     * SAME_PARENTS is reason of mapping for C1, SAME_CHILDREN -- for C2.
     * @return Bind factors.
     */
    ITriple<P, String, String>[] getBindFactors();

    void bind(C1 first, C2 second, String reason, int count);

    void bind(C1 first, C2 second, String reason);

    R map();
}
