package org.kzk.view;

import org.kzk.repository.impl.LabelRepositoryJdbcImpl;
import org.kzk.repository.impl.PostRepositoryJdbcImpl;

import java.util.Scanner;

public class MainMenuView {

    private final Scanner scanner;
    private final WriterMenuView writerView;
    private final PostMenuView postView;


    public MainMenuView(Scanner scanner) {
        this.scanner = scanner;
        this.writerView = new WriterMenuView(scanner);
        this.postView = new PostMenuView(new PostRepositoryJdbcImpl(), new LabelRepositoryJdbcImpl(), scanner);
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== Главное меню ===");
            System.out.println("1. Авторы");
            System.out.println("2. Посты");
            System.out.println("0. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Очистка буфера

            switch (choice) {
                case 1 -> writerView.showWriterMenu();  // Переход в меню авторов
                case 2 -> postView.showPostMenu();      // Переход в меню постов
                case 0 -> {
                    System.out.println("Завершение работы");
                    return;  // Выход из программы
                }
                default -> System.out.println("Неверный ввод!");
            }
        }
    }
}
