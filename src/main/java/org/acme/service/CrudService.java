package org.acme.service;

import java.util.List;

public interface CrudService<R, S> {

    List<S> findAll();

    S findById(Long id);

    S create(R dto);

    S update(Long id, R dto);

    boolean delete(Long id);
}
