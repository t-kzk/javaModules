package org.kzk;

import org.kzk.env.db.InitDb;
import org.kzk.env.db.InitDbLiquibaseImpl;
import org.kzk.view.MainMenuView;

import java.util.Scanner;

public class Main {

    private static final InitDb INIT_DB = new InitDbLiquibaseImpl();

    public static void main(String[] args) {
        INIT_DB.initDb();
        try (Scanner scanner = new Scanner(System.in)){
            MainMenuView mainMenuView = new MainMenuView(scanner);
            mainMenuView.showMainMenu();
        }
        System.out.println("Hello world!");
    }

}