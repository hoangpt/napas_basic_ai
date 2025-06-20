package com.task.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.auth.dto.AuthRequest;
import com.task.user.domain.User;
import com.task.user.dto.UserDTO;
import com.task.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@example.com");
        admin.setFullName("Admin User");
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");
        admin.setRoles(adminRoles);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        userRepository.save(admin);

        // Create regular user
        User user = new User();
        user.setUsername("user");
        user.setPassword("user123");
        user.setEmail("user@example.com");
        user.setFullName("Regular User");
        Set<String> userRoles = new HashSet<>();
        userRoles.add("USER");
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // Get admin token
        AuthRequest adminAuth = new AuthRequest();
        adminAuth.setUsername("admin");
        adminAuth.setPassword("admin123");
        MvcResult adminResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminAuth)))
                .andReturn();
        adminToken = objectMapper.readTree(adminResult.getResponse().getContentAsString())
                .get("token").asText();

        // Get user token
        AuthRequest userAuth = new AuthRequest();
        userAuth.setUsername("user");
        userAuth.setPassword("user123");
        MvcResult userResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAuth)))
                .andReturn();
        userToken = objectMapper.readTree(userResult.getResponse().getContentAsString())
                .get("token").asText();
    }

    @Test
    void getAllUsers_AsAdmin_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("admin", "user")));
    }

    @Test
    void getAllUsers_AsUser_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void createUser_AsAdmin_ShouldSucceed() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password123");
        newUser.setEmail("newuser@example.com");
        newUser.setFullName("New User");
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        newUser.setRoles(roles);

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.fullName").value("New User"));
    }

    @Test
    void updateUser_AsOwner_ShouldSucceed() throws Exception {
        User user = userRepository.findByUsername("user").orElseThrow();
        user.setFullName("Updated User Name");

        mockMvc.perform(put("/api/users/" + user.getId())
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated User Name"));
    }

    @Test
    void deleteUser_AsAdmin_ShouldSucceed() throws Exception {
        User user = userRepository.findByUsername("user").orElseThrow();

        mockMvc.perform(delete("/api/users/" + user.getId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + user.getId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
