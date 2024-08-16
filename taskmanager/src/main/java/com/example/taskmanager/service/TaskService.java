package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;

import java.util.List;
import java.util.TimeZone;

public interface TaskService {

    Task createTask(Task task, TimeZone timezone);

    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task updateTask(Long id, Task taskDetails, TimeZone timezone);

    void deleteTask(Long id);
}

