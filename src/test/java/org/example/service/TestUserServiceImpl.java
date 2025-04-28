package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestUserServiceImpl {

    @Mock
    private UserDAO userDao;

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



}
