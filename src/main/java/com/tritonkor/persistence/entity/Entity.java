package com.tritonkor.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This abstract class represents the base entity in the system with a unique identifier (UUID).
 * Subclasses of this entity are expected to extend from this class to inherit the common properties.
 */
public abstract class Entity {

    /**
     * The unique identifier for the entity.
     */
    protected int id;

    /**
     * Hold error messages.
     */
    private List<String>  validationMessages;

    /**
     * Constructs an entity with the specified identifier.
     *
     * @param id The unique identifier for the entity.
     */
    public Entity(int id) {
        this.id = id;
        this.validationMessages = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the entity.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the entity.
     *
     * @return The unique identifier.
     */
    public Entity setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Gets the error messag list of the entity.
     *
     * @return The unique identifier.
     */
    public List<String> getValidationMessages() {
        return validationMessages;
    }

    /**
     * Compares this entity with the specified object for equality.
     *
     * @param o The object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id);
    }
}
