package com.task.service;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import com.task.dto.TaskDTO;
import com.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId("1");
        task.setTitle("Test Task");
        task.setStatus(TaskStatus.TODO);
        task.setAssignee("user1");
        task.setCreatedBy("user2");

        taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setStatus(TaskStatus.TODO);
        taskDTO.setAssignee("user1");
        taskDTO.setCreatedBy("user2");
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        // mock repository behavior
        // mockito create mocktask
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_ShouldThrowException_WhenTitleIsEmpty() {
        taskDTO.setTitle("");
        taskDTO.setDescription("Valid description");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO);
        });

        assertEquals("Task title cannot be empty", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        when(taskRepository.findByDeletedAtIsNull()).thenReturn(Arrays.asList(task));

        List<TaskDTO> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        verify(taskRepository).findByDeletedAtIsNull();
    }

    @Test
    void getTask_ShouldReturnTask() {
        when(taskRepository.findByIdAndDeletedAtIsNull("1")).thenReturn(Optional.of(task));

        TaskDTO result = taskService.getTask("1");

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).findByIdAndDeletedAtIsNull("1");
    }

    @Test
    void updateTask_ShouldUpdateAndReturnTask() {
        when(taskRepository.findByIdAndDeletedAtIsNull("1")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.updateTask("1", taskDTO);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).findByIdAndDeletedAtIsNull("1");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldMarkTaskAsDeleted() {
        when(taskRepository.findByIdAndDeletedAtIsNull("1")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.deleteTask("1");

        verify(taskRepository).findByIdAndDeletedAtIsNull("1");
        verify(taskRepository).save(any(Task.class));
    }
}
