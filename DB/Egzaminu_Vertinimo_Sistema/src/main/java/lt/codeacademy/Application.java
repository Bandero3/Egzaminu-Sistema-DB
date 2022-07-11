package lt.codeacademy;

import lt.codeacademy.controller.Login;


import java.io.IOException;
import java.util.Scanner;


public class Application {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        login.selectAction(scanner, login);
    }
}
