package org.kzk;

import org.apache.log4j.BasicConfigurator;
import org.kzk.view.MainMenuView;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        MainMenuView mainMenuView = new MainMenuView();
        mainMenuView.showMainMenu();
        System.out.println("Hello world!");
    }
}