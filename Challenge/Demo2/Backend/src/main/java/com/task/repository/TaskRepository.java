package com.task.repository;

import com.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByAssignee(String assignee);
    List<Task> findByCreatedBy(String createdBy);
    List<Task> findByProject(String project);
    Optional<Task> findByIdAndDeletedAtIsNull(String id);
    List<Task> findByDeletedAtIsNull();
}
