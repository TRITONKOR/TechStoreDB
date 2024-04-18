package com.tritonkor.domain;

import com.tritonkor.persistence.entity.Entity;
import java.util.List;

public interface Service <E extends Entity>{

    /**
     * Retrieves an entity by its unique identifier (id).
     *
     * @param id The unique identifier of the entity.
     * @return The entity with the specified ID, or null if not found.
     */
    E findById(int id);

    /**
     * Retrieves all entities of the specified type.
     *
     * @return A set containing all entities of the specified type.
     */
    List<E> findAll();

    /**
     * Saves a new entity to the service.
     *
     * @param entity The entity to be saved.
     * @return The saved entity.
     */
    E save(E entity);
}
