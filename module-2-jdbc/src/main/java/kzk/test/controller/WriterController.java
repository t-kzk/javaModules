package kzk.test.controller;

import kzk.test.model.Writer;
import kzk.test.service.WriterService;

import java.util.List;


public class WriterController {
    private WriterService writerService = new WriterService();

    public Integer createWriter(String firstName, String lastName) {
        return writerService.createWriter(firstName, lastName);
    }

    public List<Writer> showAllWriters() {
       return writerService.showAllWriters();
    }

    public void deleteWriter(Integer id) {
        writerService.deleteWriter(id);
    }
}
