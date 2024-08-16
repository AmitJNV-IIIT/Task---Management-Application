package com.example.taskmanager.contoller;

import com.example.taskmanager.controller.TaskController;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testCreateTaskWithValidDataAndTimezone() throws Exception {
        Task task = new Task();
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation");
        task.setStatus("Pending");

        when(taskService.createTask(any(Task.class), any(TimeZone.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(task))
                        .param("timezone", "UTC"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Complete project documentation"));
    }

    @Test
    public void testCreateTaskWithValidDataWithoutTimezone() throws Exception {
        Task task = new Task();
        task.setTitle("Complete project documentation");
        task.setDescription("Finish the documentation");
        task.setStatus("Pending");

        when(taskService.createTask(any(Task.class), isNull())).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Complete project documentation"));
    }


    @Test
    public void testCreateTaskWithInvalidData() throws Exception {
        Task task = new Task();
        task.setTitle(""); // Assuming this is invalid

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(task)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    public void testGetTaskById_Success() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }


    @Test
    public void testUpdateTaskWithValidDataAndTimezone() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        when(taskService.updateTask(eq(1L), any(Task.class), any(TimeZone.class))).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTask))
                        .param("timezone", "UTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    public void testUpdateTaskWithValidDataWithoutTimezone() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        when(taskService.updateTask(eq(1L), any(Task.class), isNull())).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    public void testUpdateTaskWithInvalidData() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle(""); // Assuming this is invalid

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }


}
