package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestUserServiceImpl {

    @Mock
    private UserDAO userDao;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("John");
        testUser.setEmail("john@example.com");
        testUser.setAge(30);
    }

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_validInput_callsDao() {
        User user = new User();
        user.setName("Name");
        user.setEmail("name@mail.com");
        user.setAge(25);

        userService.create(user);

        verify(userDao).create(any(User.class));
    }

    @Test
    void createUser_missingName_throwsException() {
        User user = new User();
        user.setEmail("name@mail.com");
        user.setAge(30);

        assertThrows(IllegalArgumentException.class, () -> userService.create(user));

        verify(userDao, never()).create(any(User.class));
    }

    @Test
    void createUser_missingEmail_throwsException() {
        User user = new User();
        user.setName("Name");
        user.setAge(25);

        assertThrows(IllegalArgumentException.class, () -> userService.create(user));

        verify(userDao, never()).create(any());
    }

    @Test
    void createUser_ageZero_throwsException() {
        User user = new User();
        user.setName("Name");
        user.setEmail("mail@mail.com");
        user.setAge(0);

        assertThrows(IllegalArgumentException.class, () -> userService.create(user));

        verify(userDao, never()).create(any());
    }

    @Test
    void testGetById_ReturnsUser() {
        when(userDao.getById(1)).thenReturn(testUser);

        User result = userService.getById(1);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(userDao, times(1)).getById(1);
    }

    @Test
    void testGetAll_ReturnsUsers() {
        List<User> mockUsers = List.of(
                new User(1, "Alice", "alice@example.com", 25),
                new User(2, "Bob", "bob@example.com", 30)
        );

        when(userDao.getAll()).thenReturn(mockUsers);

        List<User> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userDao, times(1)).getAll();
    }

    @Test
    void testGetAll_ReturnsEmptyList() {
        when(userDao.getAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdate_Success() {
        userService.update(new User(1, "John Updated", "john.updated@example.com", 35));

        verify(userDao, times(1)).update(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userDao).delete(1);

        userService.delete(1);

        verify(userDao, times(1)).delete(1);
    }

    @Test
    void testDelete_Success() {
        userService.delete(1);

        verify(userDao, times(1)).delete(1);
    }
}
