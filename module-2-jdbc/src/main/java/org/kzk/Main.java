package org.kzk;

import org.kzk.view.MainMenuView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            MainMenuView mainMenuView = new MainMenuView(scanner);
            mainMenuView.showMainMenu();
        }
        System.out.println("Hello world!");
    }

}