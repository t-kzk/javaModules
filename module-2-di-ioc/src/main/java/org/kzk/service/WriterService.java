package org.kzk.service;


import org.kzk.data.WriterRepository;
import org.kzk.data.entity.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class WriterService {
    private final WriterRepository writersRepository;

    @Autowired
    public WriterService(WriterRepository writersRepository) {
        this.writersRepository = writersRepository;
    }


    Mono<Writer> findByName(String name) {
        Mono<Writer> byName = writersRepository.findByUsername(name);
        return byName;
    }
}
