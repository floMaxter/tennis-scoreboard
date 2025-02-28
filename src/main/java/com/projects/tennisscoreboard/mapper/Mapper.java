package com.projects.tennisscoreboard.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
