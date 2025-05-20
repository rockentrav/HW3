package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAll() {
        List<EntityModel<UserDto>> users = userService.getAll()
                .stream()
                .map(UserMapper::toDto)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(UserController.class).getById(dto.getId().intValue())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAll()).withRel("all-users")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "Get user by ID", description = "Returns a single user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getById(
            @Parameter(description = "User ID", required = true) @PathVariable int id) {
        User user = userService.getById(id);
        UserDto dto = UserMapper.toDto(user);
        EntityModel<UserDto> resource = EntityModel.of(dto,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Create a new user", description = "Adds a new user to the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> create(
            @Parameter(description = "User to create", required = true) @RequestBody @Valid UserDto dto) {
        User saved = userService.create(UserMapper.toEntity(dto));
        UserDto savedDto = UserMapper.toDto(saved);
        EntityModel<UserDto> resource = EntityModel.of(savedDto,
                linkTo(methodOn(UserController.class).getById(savedDto.getId().intValue())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("all-users"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @Operation(summary = "Update a user", description = "Updates an existing user by ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> update(
            @Parameter(description = "User ID", required = true) @PathVariable int id,
            @Parameter(description = "Updated user object", required = true) @RequestBody @Valid UserDto dto) {
        User entity = UserMapper.toEntity(dto);
        entity.setId((long) id);
        User updated = userService.update(entity);
        UserDto updatedDto = UserMapper.toDto(updated);
        EntityModel<UserDto> resource = EntityModel.of(updatedDto,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        userService.delete(id.intValue());
        return ResponseEntity.noContent().build();
    }
}