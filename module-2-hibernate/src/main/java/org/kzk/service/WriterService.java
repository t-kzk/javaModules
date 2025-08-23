package org.kzk.service;

import org.kzk.model.Writer;
import org.kzk.repository.WriterRepository;
import org.kzk.repository.imp.WriterRepositoryHibernateImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WriterService {
    private final WriterRepository writerRepository;

    public WriterService() {
        this.writerRepository = new WriterRepositoryHibernateImpl();
    }
    public WriterService(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public Writer createWriter(String firstName, String lastName) {
        return writerRepository.save(new Writer(null, firstName, lastName, null));

    }

    public Writer updateWriter(Integer writerId, String firstName, String lastName) {
        Optional<Writer> currenOpt = writerRepository.findById(writerId);
        if (currenOpt.isPresent()) {
            Writer currentOldWriter = currenOpt.get();
            String finalFirstName = checkFirstNameForUpdate(currentOldWriter, firstName);
            String finalLastName = checkLastName(currentOldWriter, lastName);
            return writerRepository.update(new Writer(writerId, finalFirstName, finalLastName, null));
        } else {
            throw new RuntimeException("The user [%d] is not exist".formatted(writerId));
        }

    }

    public List<Writer> showAllWriters() {
        return writerRepository.findAll();
    }

    public boolean deleteWriter(Integer writerId) {
        Optional<Writer> currenOpt = writerRepository.findById(writerId);
        if (currenOpt.isPresent()) {
            Writer writer = currenOpt.get();
            return writerRepository.delete(writer);
        } else {
            throw new RuntimeException("The user [%d] is not exist".formatted(writerId));
        }
    }

    private String checkFirstNameForUpdate(Writer currentOldWriter, String firstName) {
        if (!currentOldWriter.getFirstName().equals(firstName)) {
            List<Writer> all = writerRepository.findAll();
            if (all.stream().anyMatch(w -> w.getFirstName().equals(firstName))) {
                System.out.println("К сожалению, это имя уже занято - выберете другое");
                throw new RuntimeException("К сожалению, это имя уже занято - выберете другое");
            } else {
                return firstName;
            }
        } else {
            return currentOldWriter.getFirstName();
        }
    }

    private String checkLastName(Writer currentOldWriter, String lastName) {
        if (Objects.nonNull(lastName)) {
            return lastName;
        } else {
            return currentOldWriter.getLastName();
        }
    }

    public Writer writerInfo(Integer writerId) {
        Optional<Writer> writer = writerRepository.findById(writerId);
        if(writer.isPresent()) {
            return writer.get();
        } else {
            throw new RuntimeException("Writer is not exist!");
        }
    }
}
