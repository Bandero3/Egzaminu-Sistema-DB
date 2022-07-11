package lt.codeacademy.repository;

import lt.codeacademy.entity.User;

import java.util.List;

public class UserRepository extends AbstractRepository {

    public void createUser(User user) {
        modifyEntity(session -> session.persist(user));
    }

    public List<User> getUsers() {
        return getResult(session -> session.createQuery("FROM User", User.class).list());
    }
}
