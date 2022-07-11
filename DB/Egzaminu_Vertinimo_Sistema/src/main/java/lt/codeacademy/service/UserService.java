package lt.codeacademy.service;

import lt.codeacademy.entity.User;
import lt.codeacademy.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public void createNewUser(User user) {
        userRepository.createUser(user);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public void showAllUsers() {
        List<User> users = userRepository.getUsers();
        users.forEach(System.out::println);
    }
}
