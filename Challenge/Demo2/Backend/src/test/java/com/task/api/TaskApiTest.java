import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.api.model.TaskDTO;
import com.task.api.model.TaskStatus;
import com.task.api.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

package com.task.api;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createTask_WithValidData_ShouldReturnCreatedTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(TaskStatus.TODO);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @Order(2)
    void createTask_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        // Missing required fields

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void getTask_WithValidId_ShouldReturnTask() throws Exception {
        // Create a task first
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Get Task Test");
        taskDTO.setStatus(TaskStatus.TODO);
        
        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andReturn().getResponse().getContentAsString();
        
        TaskDTO createdTask = objectMapper.readValue(response, TaskDTO.class);

        mockMvc.perform(get("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Get Task Test"));
    }

    @Test
    @Order(4)
    void getTask_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void getAllTasks_ShouldReturnTasksList() throws Exception {
        // Create multiple tasks
        for (int i = 0; i < 3; i++) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTitle("Task " + i);
            taskDTO.setStatus(TaskStatus.TODO);
            
            mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDTO)));
        }

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @Order(6)
    void updateTask_WithValidData_ShouldReturnUpdatedTask() throws Exception {
        // Create a task first
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Original Title");
        taskDTO.setStatus(TaskStatus.TODO);
        
        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andReturn().getResponse().getContentAsString();
        
        TaskDTO createdTask = objectMapper.readValue(response, TaskDTO.class);

        // Update the task
        taskDTO.setTitle("Updated Title");
        taskDTO.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/api/tasks/" + createdTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @Order(7)
    void updateTask_WithInvalidId_ShouldReturnNotFound() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated Title");
        taskDTO.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/api/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void deleteTask_WithValidId_ShouldReturnNoContent() throws Exception {
        // Create a task first
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Task to Delete");
        taskDTO.setStatus(TaskStatus.TODO);
        
        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andReturn().getResponse().getContentAsString();
        
        TaskDTO createdTask = objectMapper.readValue(response, TaskDTO.class);

        mockMvc.perform(delete("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isNoContent());

        // Verify task is deleted
        mockMvc.perform(get("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    void deleteTask_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }
}