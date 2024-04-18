package com.tritonkor.domain.impl;

import com.tritonkor.domain.Service;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Entity;
import java.util.List;

public class GenericService <E extends Entity> implements Service<E> {

    private final Dao<E> dao;

    public GenericService(Dao<E> dao) {
        this.dao = dao;
    }

    @Override
    public E findById(int id) {
        return dao.findOneById(id).orElseThrow();
    }

    @Override
    public List<E> findAll() {
        return dao.findAll();
    }

    @Override
    public E save(E entity) {
        return dao.save(entity);
    }
}
