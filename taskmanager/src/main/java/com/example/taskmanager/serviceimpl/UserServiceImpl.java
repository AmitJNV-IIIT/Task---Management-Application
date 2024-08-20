package com.example.taskmanager.serviceimpl;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user and saves it to the database.
     *
     * @param user The user entity to be created.
     * @return The saved user entity.
     */
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user entity with the given ID.
     * @throws UserNotFoundException If no user is found with the given ID.
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    /**
     * Updates an existing user with new details.
     *
     * @param id The ID of the user to update.
     * @param userDetails The new user details.
     * @return The updated user entity.
     * @throws UserNotFoundException If no user is found with the given ID.
     */
    @Override
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Update user details
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setTimezone(userDetails.getTimezone());
        user.setIsActive(userDetails.getIsActive());

        // Save and return the updated user
        return userRepository.save(user);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id The ID of the user to delete.
     * @throws UserNotFoundException If no user is found with the given ID.
     */
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
