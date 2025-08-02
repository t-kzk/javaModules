package org.kzk.service;

import org.kzk.model.Writer;
import org.kzk.repository.WriterRepository;
import org.kzk.repository.impl.WriterRepositoryJdbcImpl;

import java.util.List;

public class WriterService {
    WriterRepository writerRepository = new WriterRepositoryJdbcImpl();

    public Integer createWriter(String firstName, String lastName) {
        return writerRepository.save(new Writer(null, firstName, lastName, null));

    }

    public List<Writer> showAllWriters() {
       return writerRepository.findAll();
    }

    public void deleteWriter(Integer id) {
        writerRepository.deleteById(id);
    }
}
