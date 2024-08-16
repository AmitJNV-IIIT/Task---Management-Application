package com.example.taskmanager.service;

import com.example.taskmanager.entity.User;
import java.util.List;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);
}

