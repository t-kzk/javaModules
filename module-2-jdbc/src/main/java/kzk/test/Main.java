package kzk.test;

import kzk.test.env.db.InitDb;
import kzk.test.env.db.InitDbLiquibaseImpl;
import kzk.test.view.MainMenuView;

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