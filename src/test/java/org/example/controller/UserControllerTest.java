package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsers_ReturnsList() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Тест");
        user.setEmail("test@example.com");
        user.setAge(25);

        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Тест"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService).getAll();
    }

    @Test
    void createUser_ReturnsOk() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Новый");
        dto.setEmail("new@example.com");
        dto.setAge(22);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userService).create(Mockito.any(User.class));
    }

    @Test
    void updateUser_ReturnsOk() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Обновлённый");
        dto.setEmail("updated@example.com");
        dto.setAge(30);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userService).update(Mockito.any(User.class));
    }

    @Test
    void deleteUser_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).delete(1);
    }
}