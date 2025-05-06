package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        if (user.getName() == null || user.getEmail() == null || user.getAge() == 0) {
            throw new IllegalArgumentException("Имя, Email и Age обязательны!");
        }
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public User getById(int id) {
        return userRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("Пользователь не найден");
        }
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById((long) id);
    }
}