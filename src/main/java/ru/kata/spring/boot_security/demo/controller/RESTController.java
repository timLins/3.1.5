package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.Util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exception_handling.RestExceptionHandler;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class RESTController {

    private final UserService userService;


    @Autowired
    public RESTController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(value = "id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid User user) {
        userService.updateUser(user, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
