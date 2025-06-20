package com.task.service;

import com.task.domain.Task;
import com.task.dto.TaskDTO;
import com.task.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        task = taskRepository.save(task);
        // log? id task la gi, user created by, ...
        // log.info("TaskService.createTask_28: Task created with id: {}", task.getId());
        return convertToDTO(task);
    }

    public TaskDTO updateTask(String id, TaskDTO taskDTO) {
        Task task = taskRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        
        BeanUtils.copyProperties(taskDTO, task, "id", "createdAt", "createdBy");
        task = taskRepository.save(task);
        return convertToDTO(task);
    }

    public void deleteTask(String id) {
        Task task = taskRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        
        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public TaskDTO getTask(String id) {
        Task task = taskRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return convertToDTO(task);
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findByDeletedAtIsNull()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        return dto;
    }

    private Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        return task;
    }
}
