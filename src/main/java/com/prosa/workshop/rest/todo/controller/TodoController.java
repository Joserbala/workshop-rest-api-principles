package com.prosa.workshop.rest.todo.controller;

import com.prosa.workshop.rest.todo.dto.CreateTodoRequest;
import com.prosa.workshop.rest.todo.dto.TodoDto;
import com.prosa.workshop.rest.todo.dto.UpdateTodoRequest;
import com.prosa.workshop.rest.todo.model.TodoStatus;
import com.prosa.workshop.rest.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(todoService.findAll(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@Valid @RequestBody CreateTodoRequest request, UriComponentsBuilder uriBuilder) {
        var todoDto = todoService.create(request);
        var location = uriBuilder.path("/api/v1/todos/{id}").buildAndExpand(todoDto.getId()).toUri();

        return ResponseEntity.created(location).body(todoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @Valid @RequestBody UpdateTodoRequest request) {
        return ResponseEntity.ok(todoService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TodoDto> updateStatus(@PathVariable Long id, @RequestParam TodoStatus status) {
        return ResponseEntity.ok(todoService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
