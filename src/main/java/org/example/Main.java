package org.example;

import org.example.dao.UserDAOImpl;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl(new UserDAOImpl());
        Scanner scanner = new Scanner(System.in);
        //testSergeyR
        while (true) {
            System.out.println("\n1. Создать пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Найти по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход");

            int choice = readInt(scanner, "Введите номер действия: ");

            switch (choice) {
                case 1 -> {
                    String name = readString(scanner, "Имя: ");
                    String email = readEmail(scanner, "Email: ");
                    int age = readInt(scanner, "Возраст: ");

                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);

                    try {
                        userService.create(user);
                        System.out.println("Пользователь создан.");
                    } catch (Exception e) {
                        System.out.println("Ошибка создания: " + e.getMessage());
                    }
                }

                case 2 -> {
                    List<User> users = userService.getAll();
                    users.sort(Comparator.comparing(User::getId));
                    if (users.isEmpty()) {
                        System.out.println("Пользователей нет.");
                    } else {
                        users.forEach(System.out::println);
                    }
                }

                case 3 -> {
                    int id = readInt(scanner, "ID: ");
                    User user = userService.getById(id);
                    if (user != null) {
                        System.out.println(user);
                    } else {
                        System.out.println("Пользователь не найден.");
                    }
                }

                case 4 -> {
                    int id = readInt(scanner, "ID пользователя: ");
                    User user = userService.getById(id);
                    if (user != null) {
                        String name = readString(scanner, "Новое имя: ");
                        String email = readString(scanner, "Новый email: ");
                        int age = readInt(scanner, "Новый возраст: ");

                        user.setName(name);
                        user.setEmail(email);
                        user.setAge(age);

                        userService.update(user);
                        System.out.println("Пользователь обновлён.");
                    } else {
                        System.out.println("Пользователь не найден.");
                    }
                }

                case 5 -> {
                    int id = readInt(scanner, "ID для удаления: ");
                    userService.delete(id);
                    System.out.println("Пользователь удалён.");
                }

                case 0 -> {
                    System.out.println("До свидания 👋");
                    return;
                }

                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    // Метод для безопасного ввода int
    private static int readInt(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = scanner.nextInt();
                scanner.nextLine(); // очистка \n
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Введите корректное число!");
                scanner.nextLine(); // очистка ввода
            }
        }
    }

    // Метод для безопасного ввода строки
    private static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Метод для проверки email с помощью регулярного выражения
    private static String readEmail(Scanner scanner, String prompt) {
        String emailRegex = "^[a-zA-Z\\d._%+-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$"; // Регулярное выражение для email
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();
            if (email.matches(emailRegex)) {
                return email;
            } else {
                System.out.println("Некорректный email. Попробуйте снова.");
            }
        }
    }
}