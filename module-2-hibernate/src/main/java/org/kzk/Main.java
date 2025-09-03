package org.kzk;

import org.apache.log4j.BasicConfigurator;
import org.kzk.model.Label;
import org.kzk.model.Post;
import org.kzk.model.Writer;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.PostRepository;
import org.kzk.repository.WriterRepository;
import org.kzk.repository.imp.HibernateLabelRepositoryImpl;
import org.kzk.repository.imp.HibernatePostRepositoryImpl;
import org.kzk.repository.imp.HibernateWriterRepositoryImpl;
import org.kzk.view.MainMenuView;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {

/*        public static void main(String[] args) {
            BasicConfigurator.configure();
            MainMenuView mainMenuView = new MainMenuView();
            mainMenuView.showMainMenu();
            System.out.println("Hello world!");
        }*/
    public static void main(String[] args) {
        BasicConfigurator.configure();
        PostRepository postR = new HibernatePostRepositoryImpl();
        WriterRepository writerR = new HibernateWriterRepositoryImpl();
        LabelRepository labelR = new HibernateLabelRepositoryImpl();

        List<Label> all = labelR.findAll();
        all.forEach(l->l.getPosts().forEach(p->p.getContent()));
        System.out.println(all);
        System.out.println("Hello world!");
    }
}