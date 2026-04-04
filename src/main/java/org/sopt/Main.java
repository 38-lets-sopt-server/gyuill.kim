package org.sopt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PostService postService = new PostService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== 에브리타임 게시판 ===");
            System.out.println("1. 게시글 작성");
            System.out.println("2. 전체 조회");
            System.out.println("3. 단건 조회");
            System.out.println("4. 게시글 수정");
            System.out.println("5. 게시글 삭제");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("제목: ");
                    String title = scanner.nextLine();
                    System.out.print("내용: ");
                    String content = scanner.nextLine();
                    System.out.print("작성자: ");
                    String author = scanner.nextLine();
                    postService.createPost(title, content, author);
                    break;
                case 2:
                    postService.readAllPosts();
                    break;
                case 3:
                    System.out.print("조회할 게시글 ID: ");
                    postService.readPost(scanner.nextLong());
                    scanner.nextLine();
                    break;
                case 4:
                    System.out.print("수정할 게시글 ID: ");
                    Long updateId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("새 제목: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("새 내용: ");
                    String newContent = scanner.nextLine();
                    postService.updatePost(updateId, newTitle, newContent);
                    break;
                case 5:
                    System.out.print("삭제할 게시글 ID: ");
                    postService.deletePost(scanner.nextLong());
                    scanner.nextLine();
                    break;
                case 0:
                    running = false;
                    System.out.println("👋 프로그램 종료");
                    break;
                default:
                    System.out.println("❗ 잘못된 입력입니다.");
            }
        }
        scanner.close();
    }
}
