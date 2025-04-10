package com.projects.tennisscoreboard.mapper;

import java.util.List;

public interface Mapper<F, T> {

    T mapFrom(F object);

    default List<T> mapFrom(List<F> objects) {
        return objects.stream()
                .map(this::mapFrom)
                .toList();
    }
}
