package lt.codeacademy.controller;

import lt.codeacademy.data.UserType;
import lt.codeacademy.entity.User;
import lt.codeacademy.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Login {
    public static boolean loggedIn = false;
    private static final int MAX_RETRY = 3;
    private final UserService userService = new UserService();
    public static List<User> users = new ArrayList<>();

    public void selectAction(Scanner scanner, Login login) throws IOException {
        String action;
        do {
            menu();
            action = scanner.nextLine();
            switch (action) {
                case "1" -> userRegistration(scanner);
                case "2" -> login(scanner, login);
                case "3" -> System.out.println("Programa baige darba");
                default -> System.out.println("Tokio veiksmo nera");
            }
        } while (!action.equals("3") && !loggedIn);
    }

    private void menu() {
        System.out.println("""
                [1]. Registracija
                [2]. Prisijungimas
                [3]. Uždaryti Programa
                """);
    }

    public void userRegistration(Scanner scanner) {
        String userName = getUniqueUserName(scanner);
        if (userName == null) {
            return;
        }

        System.out.println("Iveskite slaptažodi");
        String password = scanner.nextLine();

        if (!isRepeatPasswordCorrect(scanner, password)) {
            System.out.println("Vartotojo sukurti nepavyko");
            return;
        }
        String[] name = userName.split(" ");

        String userType = getUserType(scanner);

        User newUser = new User(name[0], name[1], userType, DigestUtils.sha512Hex(password));
        users.add(newUser);
        userService.createNewUser(newUser);
        System.out.println("Registracija sekminga");

    }

    public void login(Scanner scanner, Login login) throws IOException {
        Student student = new Student();
        Teacher teacher = new Teacher();

        User existingUser = getUser(scanner);

        if (existingUser == null) {
            System.out.println("Tokio vartotojo nera");
            return;
        }
        System.out.println("Iveskite slaptažodi");
        String password = scanner.nextLine();

        if (!existingUser.getPassword().equals(DigestUtils.sha512Hex(password))) {
            System.out.println("Neteisingas slaptažodis");
            return;
        }
        System.out.printf("Sveikiname prisijungus %s %s\n", existingUser.getName(), existingUser.getSurname());

        if (existingUser.getType().equals(UserType.TEACHER.getRoleName())) {
            teacher.teacherAction(login);
            loggedIn = true;
        } else {
            student.studentAction(existingUser, login);
            loggedIn = true;
        }

    }

    private User getUser(Scanner scanner) {
        while (true) {
            System.out.println("Iveskita savo Varda ir Pavarde");
            String userName = scanner.nextLine();

            String[] name = userName.split(" ");

            if (name.length != 2) {
                System.out.println("Blogai ivestas vardas ir pavarde");
                continue;
            }

            users = userService.getUsers();

            return users.stream()
                    .filter(n -> n.getName().equals(name[0]) && n.getSurname().equals(name[1]))
                    .findFirst()
                    .orElse(null);
        }
    }

    public boolean isRepeatPasswordCorrect(Scanner scanner, String password) {
        for (int i = 0; i < MAX_RETRY; i++) {
            System.out.println("Pakartotinai iveskite savo slaptažodi");
            String repeatPassword = scanner.nextLine();

            if (!repeatPassword.equals(password)) {
                System.out.println("Slaptazodziai nesutampa");
                continue;
            }
            return true;
        }
        return false;
    }

    public String getUniqueUserName(Scanner scanner) {
        while (true) {
            System.out.println("Iveskite savo Varda ir Pavarde:");
            String newUserName = scanner.nextLine();
            String[] name = newUserName.split(" ");
            if (name.length != 2) {
                System.out.println("Blogai ivestas vardas ir pavarde");
                continue;
            }

            users = userService.getUsers();

            User existingUser = users.stream()
                    .filter(n -> n.getName().equals(name[0]) && n.getSurname().equals(name[1]))
                    .findFirst()
                    .orElse(null);

            if (existingUser != null) {
                System.out.printf("Vartotojas %s jau existuoja\n", newUserName);
                return null;
            }
            return newUserName;
        }
    }

    public String getUserType(Scanner scanner) {
        String userType;
        do {
            userTypeMenu();
            userType = scanner.nextLine();
            switch (userType) {
                case "1" -> {
                    return UserType.TEACHER.getRoleName();
                }
                case "2" -> {
                    return UserType.STUDENT.getRoleName();
                }
                default -> System.out.println("Tokios roles nera");
            }
        } while (true);
    }

    private void userTypeMenu() {
        System.out.println("""
                Pasirinkite savo role:
                [1]. Destytojas
                [2]. Studentas""");
    }

}
