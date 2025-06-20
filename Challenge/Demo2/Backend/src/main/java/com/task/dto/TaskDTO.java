package com.task.dto;

import com.task.domain.Priority;
import com.task.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDTO {
    private String id;    private String title;
    
    private String description;
    
    private TaskStatus status;
    
    private LocalDateTime deadline;
    
    private String assignee;
    
    private String createdBy;
    
    private String project;
    
    private List<String> labels;
    
    private Priority priority;
    
    private List<ReminderDTO> reminders;
    
    private List<CommentDTO> comments;
    
    private LocalDateTime deletedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
