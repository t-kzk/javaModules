package org.kzk.repository;

import java.util.List;
import java.util.Optional;

public interface ReadRepository<T, ID> {

    Optional<T> findById(ID id);
    List<T> findAll();
}
