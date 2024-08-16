package com.example.taskmanager.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        // Given
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.save(user)).thenReturn(user);

        // When
        User createdUser = userService.createUser(user);

        // Then
        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetAllUsers() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getUserById(1L);

        // Then
        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserByIdNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateUser() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("John");

        User updatedUserDetails = new User();
        updatedUserDetails.setFirstName("Johnny");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // When
        User updatedUser = userService.updateUser(1L, updatedUserDetails);

        // Then
        assertNotNull(updatedUser);
        assertEquals("Johnny", updatedUser.getFirstName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testDeleteUser() {
        // Given
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }
}
