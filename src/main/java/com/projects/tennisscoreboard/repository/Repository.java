package com.projects.tennisscoreboard.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<K extends Serializable, E> {

    E save(E entity);

    List<E> findAll(Integer page);
}
