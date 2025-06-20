package com.task.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private String id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
