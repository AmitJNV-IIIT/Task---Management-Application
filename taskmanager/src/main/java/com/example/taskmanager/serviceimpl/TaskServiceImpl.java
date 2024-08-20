package com.example.taskmanager.serviceimpl;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional // Maintain data integrity and consistency, especially when dealing with multiple operations that interact with the database.
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new task with the specified details and timezone.
     *
     * @param task     The task to be created.
     * @param timezone The timezone to be used for setting task timestamps. If null, the system default timezone will be used.
     * @return The created task.
     * @throws UserNotFoundException If the user assigned to the task is not found.
     */
    @Override
    public Task createTask(Task task, TimeZone timezone) {
        ZoneId zoneId = timezone != null ? timezone.toZoneId() : ZoneId.systemDefault();

        // Set createdAt and updatedAt in UTC
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of("UTC"));
        task.setCreatedAt(nowUtc.toLocalDateTime());
        task.setUpdatedAt(nowUtc.toLocalDateTime());

        // Handle assignedTo user if provided
        if (task.getAssignedTo() != null) {
            User assignedUser = userRepository.findById(task.getAssignedTo().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + task.getAssignedTo().getId()));
            task.setAssignedTo(assignedUser);
        }

        // Save task
        return taskRepository.save(task);
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return A list of all tasks.
     */
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task with the specified ID.
     * @throws TaskNotFoundException If no task is found with the given ID.
     * @throws UserNotFoundException If the user assigned to the task is not found.
     */
    @Override
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        // Optionally, ensure assignedTo is fully loaded if needed
        if (task.getAssignedTo() != null) {
            User assignedUser = userRepository.findById(task.getAssignedTo().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + task.getAssignedTo().getId()));
            task.setAssignedTo(assignedUser);
        }

        return task;
    }

    /**
     * Updates an existing task with new details and timezone.
     *
     * @param id          The ID of the task to update.
     * @param taskDetails The new task details.
     * @param timezone    The timezone to be used for setting the updated timestamp. If null, the system default timezone will be used.
     * @return The updated task.
     * @throws TaskNotFoundException If no task is found with the given ID.
     * @throws UserNotFoundException If the user assigned to the task is not found.
     */
    @Override
    public Task updateTask(Long id, Task taskDetails, TimeZone timezone) {
        Task task = getTaskById(id);

        ZoneId zoneId = timezone != null ? timezone.toZoneId() : ZoneId.systemDefault();

        // Update fields
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setUpdatedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());

        // Handle assignedTo user if provided
        if (taskDetails.getAssignedTo() != null) {
            User assignedUser = userRepository.findById(taskDetails.getAssignedTo().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + taskDetails.getAssignedTo().getId()));
            task.setAssignedTo(assignedUser);
        }

        // Save updated task
        return taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @throws TaskNotFoundException If no task is found with the given ID.
     */
    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
}
