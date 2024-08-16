package com.example.taskmanager.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.serviceimpl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Transactional
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTaskWithValidUser() {
        Task task = new Task();
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation for the task management project");
        task.setStatus("Pending");
        User assignedUser = new User();
        assignedUser.setId(2L);
        task.setAssignedTo(assignedUser);

        User mockUser = new User();
        mockUser.setId(2L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task createdTask = taskService.createTask(task, TimeZone.getTimeZone("UTC"));

        assertNotNull(createdTask);
        assertEquals("Complete project documentation", createdTask.getTitle());
        assertEquals("John", createdTask.getAssignedTo().getFirstName());
        verify(userRepository, times(1)).findById(2L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testCreateTaskWithInvalidUser() {
        Task task = new Task();
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation for the task management project");
        task.setStatus("Pending");
        User assignedUser = new User();
        assignedUser.setId(2L);
        task.setAssignedTo(assignedUser);

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task, TimeZone.getTimeZone("UTC"));
        });

        assertEquals("User not found with id: 2", exception.getMessage());
        verify(userRepository, times(1)).findById(2L);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation for the task management project");
        task.setStatus("Pending");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals("Complete project documentation", foundTask.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTaskByIdWithAssignedUser() {
        User assignedUser = new User();
        assignedUser.setId(2L);
        assignedUser.setFirstName("John");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation for the task management project");
        task.setStatus("Pending");
        task.setAssignedTo(assignedUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignedUser));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals("Complete project documentation", foundTask.getTitle());
        assertEquals("John", foundTask.getAssignedTo().getFirstName());
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    public void testGetTaskByIdWithInvalidAssignedUser() {
        User assignedUser = new User();
        assignedUser.setId(2L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation for the task management project");
        task.setStatus("Pending");
        task.setAssignedTo(assignedUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("User not found with id: 2", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus("Pending");

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("New Title");
        updatedTaskDetails.setDescription("New Description");
        updatedTaskDetails.setStatus("Completed");
        User assignedUser = new User();
        assignedUser.setId(2L);
        updatedTaskDetails.setAssignedTo(assignedUser);

        User mockUser = new User();
        mockUser.setId(2L);
        mockUser.setFirstName("John");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = taskService.updateTask(1L, updatedTaskDetails, TimeZone.getTimeZone("UTC"));

        assertNotNull(updatedTask);
        assertEquals("New Title", updatedTask.getTitle());
        assertEquals("New Description", updatedTask.getDescription());
        assertEquals("Completed", updatedTask.getStatus());
        assertEquals("John", updatedTask.getAssignedTo().getFirstName());
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    public void testUpdateTaskWithInvalidUser() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus("Pending");

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("New Title");
        updatedTaskDetails.setDescription("New Description");
        updatedTaskDetails.setStatus("Completed");
        User assignedUser = new User();
        assignedUser.setId(2L);
        updatedTaskDetails.setAssignedTo(assignedUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTask(1L, updatedTaskDetails, TimeZone.getTimeZone("UTC"));
        });

        assertEquals("User not found with id: 2", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void testDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).delete(any(Task.class));
    }
}
