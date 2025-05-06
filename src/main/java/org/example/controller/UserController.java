package org.example.controller;

import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return UserMapper.toDto(userService.getById(id.intValue()));
    }

    @PostMapping
    public void create(@RequestBody UserDto dto) {
        userService.create(UserMapper.toEntity(dto));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody UserDto dto) {
        User user = UserMapper.toEntity(dto);
        user.setId(id);
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id.intValue());
    }
}
