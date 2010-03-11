package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;

import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public interface IMapper<C1, C2, P> {

    ITriple<P, String, String>[] getBindFactors();

    void bind(C1 first, C2 second, String reason, int count);

    void bind(C1 first, C2 second, String reason);

    Collection<C1> map();
}
