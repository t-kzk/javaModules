package org.kzk.view;

import org.kzk.controller.PostController;
import org.kzk.model.Post;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.PostRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PostMenuView {
    private final PostController postController;
    private final Scanner scanner;

    public PostMenuView(PostRepository postRepository, LabelRepository labelRepository, Scanner scanner) {
        this.postController = new PostController(postRepository, labelRepository);
        this.scanner = scanner;
    }

    public void showPostMenu() {
        while (true) {
            System.out.println("\n=== Меню постов ===");
            System.out.println("1. Создать пост");
            System.out.println("2. Изменить пост");
            System.out.println("3. Удаление поста для пользователя");
            System.out.println("0. Назад");


            int choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1 -> createPost();
                case 2 -> updatePost();
                case 3 -> deletePost();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный ввод!");
            }

        }
    }

    private void createPost() {
        System.out.println("Введите uuid автора: ");
        Integer writerId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Расскажите нам, что-нибудь:");
        String content = scanner.nextLine();

        System.out.println("Выберете лейблы");
        // todo вывести доступные лейблы
        System.out.println("Введите номер лейбла (если их несколько - через пробел)");
        String rowLabels = scanner.nextLine();
        List<Integer> labelsIds = new ArrayList<>();

        if(!StringUtils.isEmpty(rowLabels)) {
            String[] rowLabelsArr = rowLabels.split(" ");
            Arrays.stream(rowLabelsArr).forEach(id -> labelsIds.add(Integer.valueOf(id)));
        }

        Post post = postController.createPost(writerId, content, labelsIds);
        System.out.printf("Создан новый пост [%s]%n", post);

    }

    private void updatePost() {
        System.out.println("Введите uuid поста для редактирования:");
        Integer postId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите измененный пост:");
        String content = scanner.nextLine();
        Post updatedPost = postController.updatePostContent(postId, content);
        System.out.println("Пост изменен! %s".formatted(updatedPost));

    }

    private void deletePost() {
        System.out.println("Введите uuid поста для удаления:");
        Integer postId = scanner.nextInt();

        postController.userDeletesPost(postId);
    }
}
