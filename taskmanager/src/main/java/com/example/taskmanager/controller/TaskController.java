package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TimeZone;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Creates a new task.
     *
     * @param task The task entity to be created.
     * @param timezone The timezone for the task's timestamps. If null, the system default timezone will be used.
     * @return ResponseEntity containing the created task and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task, @RequestParam(required = false) String timezone) {
        TimeZone tz = timezone != null ? TimeZone.getTimeZone(timezone) : null;
        Task createdTask = taskService.createTask(task, tz);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Retrieves all tasks.
     *
     * @return ResponseEntity containing a list of all tasks and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return ResponseEntity containing the task with the given ID and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Updates an existing task.
     *
     * @param id The ID of the task to update.
     * @param taskDetails The new task details.
     * @param timezone The timezone for the task's updated timestamp. If null, the system default timezone will be used.
     * @return ResponseEntity containing the updated task and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails, @RequestParam(required = false) String timezone) {
        TimeZone tz = timezone != null ? TimeZone.getTimeZone(timezone) : null;
        Task updatedTask = taskService.updateTask(id, taskDetails, tz);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) if the deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
