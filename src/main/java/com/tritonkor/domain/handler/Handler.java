package com.tritonkor.domain.handler;

import com.tritonkor.persistence.entity.Entity;

public interface Handler<E extends Entity> {
    public void validate(E entity);
    public void setNextHandler(Handler handler);
}
