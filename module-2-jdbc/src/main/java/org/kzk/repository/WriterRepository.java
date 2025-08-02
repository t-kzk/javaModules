package org.kzk.repository;

import org.kzk.model.Writer;

import java.util.Optional;

public interface WriterRepository extends CrudRepository<Writer, Integer> {
    Optional<Integer> findIdByFirstname(String firstName);

    int deleteById(Integer id);
}
