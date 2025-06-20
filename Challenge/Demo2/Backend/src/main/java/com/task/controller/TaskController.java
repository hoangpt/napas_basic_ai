package com.task.controller;

import com.task.dto.TaskDTO;
import com.task.exception.TaskValidationException;
import com.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        validateTask(taskDTO);
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable String id,
            @RequestBody TaskDTO taskDTO) {
        validateTask(taskDTO);
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskDTO> getTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    private void validateTask(TaskDTO taskDTO) {
        StringBuilder errors = new StringBuilder();

        if (taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            errors.append("Title is required. ");
        } else if (taskDTO.getTitle().length() < 3 || taskDTO.getTitle().length() > 100) {
            errors.append("Title must be between 3 and 100 characters. ");
        }

        if (taskDTO.getDescription() != null && taskDTO.getDescription().length() > 500) {
            errors.append("Description cannot exceed 500 characters. ");
        }

        if (taskDTO.getAssignee() == null || taskDTO.getAssignee().trim().isEmpty()) {
            errors.append("Assignee is required. ");
        }

        if (errors.length() > 0) {
            throw new TaskValidationException(errors.toString().trim());
        }
    }

}
