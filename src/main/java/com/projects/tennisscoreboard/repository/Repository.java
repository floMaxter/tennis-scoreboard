package com.projects.tennisscoreboard.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<K extends Serializable, E> {

    E save(E entity);

    Optional<E> findById(K id);

    List<E> findAll();
}
