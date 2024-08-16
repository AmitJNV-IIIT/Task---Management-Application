package com.example.taskmanager.serviceimpl;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.TaskNotFoundException;
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
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

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
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + task.getAssignedTo().getId()));
            task.setAssignedTo(assignedUser);
        }

        // Save task
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        // Optionally, ensure assignedTo is fully loaded if needed
        if (task.getAssignedTo() != null) {
            User assignedUser = userRepository.findById(task.getAssignedTo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + task.getAssignedTo().getId()));
            task.setAssignedTo(assignedUser);
        }

        return task;
    }

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
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + taskDetails.getAssignedTo().getId()));
            task.setAssignedTo(assignedUser);
        }

        // Save updated task
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
}
