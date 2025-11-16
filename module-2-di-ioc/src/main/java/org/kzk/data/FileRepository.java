package org.kzk.data;

import org.kzk.data.entity.FileEntity;
import org.kzk.data.entity.status.FileStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileEntity, Integer> {
    Mono<FileEntity> findByName(String name);

    @Query("UPDATE files SET status = :status WHERE id = :id")
    Mono<Integer> updateStatusById(Integer id, FileStatus status);

    @Query("""
            SELECT f.*
                FROM files f
                JOIN events e ON e.file_id = f.id
                WHERE e.user_id = :userId
            """)
    Flux<FileEntity> findAllByUserId(Integer userId);
}
