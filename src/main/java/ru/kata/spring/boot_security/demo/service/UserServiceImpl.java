package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.Util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exception_handling.RestExceptionHandler;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RestExceptionHandler restExceptionHandler;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder bCryptPasswordEncoder, RestExceptionHandler restExceptionHandler) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restExceptionHandler = restExceptionHandler;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            restExceptionHandler.handleCreatedException(new UserNotCreatedException("ERROR"));
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User user, Long id) {
        User userFromDb = userRepository.findById(id).get();
        if (userFromDb.getPassword().equals(user.getPassword())) {
            userRepository.save(user);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            restExceptionHandler.handleFoundException(new UserNotFoundException());
        }
        return user.get();
                //.orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public boolean deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public User getUserByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("This user not found");
        }
        return userRepository.findByUsername(username).get();
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
}

