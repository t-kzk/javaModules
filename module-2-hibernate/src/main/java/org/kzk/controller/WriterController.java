package org.kzk.controller;

import org.kzk.model.Writer;
import org.kzk.service.WriterService;

import java.util.List;


public class WriterController {
    private WriterService writerService = new WriterService();

    public Writer createWriter(String firstName, String lastName) {
        return writerService.createWriter(firstName, lastName);
    }

    public List<Writer> showAllWriters() {
        return writerService.showAllWriters();
    }

    public void deleteWriter(Integer id) {
        writerService.deleteWriter(id);
    }
}
