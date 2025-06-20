package com.task.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReminderDTO {
    private String id;
    private LocalDateTime reminderTime;
    private String message;
    private boolean isCompleted;
}
