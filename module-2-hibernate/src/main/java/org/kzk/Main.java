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
/*    public static void main(String[] args) {
        BasicConfigurator.configure();
        PostRepository postR = new PostRepositoryHibernateImpl();
        WriterRepository writerR = new WriterRepositoryHibernateImpl();
        LabelRepository labelR = new LabelRepositoryHibernateImpl();

        List<Label> labelsByIds = labelR.findLabelsByIds(List.of(1, 2, 5));
        Optional<Post> byId = postR.findById(5);
        List<Writer> all = writerR.findAll();
        all.forEach(e->e.getPosts().forEach(l->l.getLabels().getFirst()));
        System.out.println("Hello world!");
    }*/
}