package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService {
    void saveUser(User user);

    void updateUser(User user, Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean deleteUserById(Long id);

}
