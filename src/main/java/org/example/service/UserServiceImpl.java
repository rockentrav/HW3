package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.UserNotFoundException;
import org.example.kafka.UserEventProducer;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.kafka.UserEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventProducer eventProducer;

    @Override
    public User create(User user) {
        user.setCreatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        //в кафку
        eventProducer.sendEvent(new UserEvent("CREATE", saved.getEmail()));

        return saved;
    }

    @Override
    public User getById(int id) {
        return userRepository.findById((long) id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        User user = userRepository.findById((long) id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        userRepository.deleteById((long) id);

        //в кафку
        eventProducer.sendEvent(new UserEvent("DELETE", user.getEmail()));
    }
}