package org.kzk.data;

import org.kzk.data.entity.FileE;
import org.kzk.data.entity.status.FileStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileE, Integer> {
     Mono<FileE> findByName(String name);

     @Query("UPDATE files SET status = :status WHERE id = :id")
     Mono<Integer> updateStatusById(Integer id, FileStatus status);
}
