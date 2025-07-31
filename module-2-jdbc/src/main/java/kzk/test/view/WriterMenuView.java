package kzk.test.view;

import kzk.test.controller.WriterController;
import kzk.test.model.Writer;

import java.util.List;
import java.util.Scanner;

public class WriterMenuView {
    private final WriterController writerController = new WriterController();
    private final Scanner scanner;


    public WriterMenuView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showWriterMenu() {
        while (true) {
            System.out.println("\n=== Меню авторов ===");
            System.out.println("1. Создать автора");
            System.out.println("2. Список авторов");
            System.out.println("0. Назад");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createWriter();
                case 2 -> showAllWriters();
                case 3 -> deleteWriter();
                case 0 -> {
                    return;
                }  // Возврат в главное меню
                default -> System.out.println("Неверный ввод!");
            }
        }
    }

    private void createWriter() {
        System.out.println("Введите имя автора: ");
        String firstname = scanner.nextLine();
        System.out.println("Введите фамилию автора: ");
        String lastname = scanner.nextLine();

        Integer writerUid = writerController.createWriter(firstname, lastname);
        System.out.println("Автор создан с id = [%d]".formatted(writerUid));
    }

    private void showAllWriters() {
        List<Writer> writers = writerController.showAllWriters();
        System.out.println(writers);
    }

    private void deleteWriter() {
        System.out.println("Введите uuid автора:");
        Integer writerId = scanner.nextInt();
        try {
            writerController.deleteWriter(writerId);
            System.out.println("Автор успешно удален!");
            System.out.println("Список аутальных авторов: ");
            showAllWriters();
        } catch (RuntimeException e) {
            System.out.println("К сожалению, такой uuid не подошел.");
            System.out.println("Предлагаем выбрать uuid автора: ");
            showAllWriters();
        }
    }
}
