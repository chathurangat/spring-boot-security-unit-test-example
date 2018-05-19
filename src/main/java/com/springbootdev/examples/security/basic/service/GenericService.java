package com.springbootdev.examples.security.basic.service;

import com.springbootdev.examples.security.basic.exception.PersistentException;

public interface GenericService<Q, S> {

    S create(Q rq) throws PersistentException;
}
