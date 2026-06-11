package com.prosa.workshop.rest.todo.service;

import com.prosa.workshop.rest.todo.dto.CreateTodoRequest;
import com.prosa.workshop.rest.todo.dto.TodoDto;
import com.prosa.workshop.rest.todo.dto.UpdateTodoRequest;
import com.prosa.workshop.rest.todo.exception.ResourceNotFoundException;
import com.prosa.workshop.rest.todo.model.Todo;
import com.prosa.workshop.rest.todo.model.TodoStatus;
import com.prosa.workshop.rest.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> findAll(String status) {
        if (status != null) {
            return todoRepository.findByStatus(TodoStatus.valueOf(status.toUpperCase()))
                    .stream().map(this::toDto).toList();
        }

        return todoRepository.findAll().stream().map(this::toDto).toList();
    }

    public TodoDto findById(Long id) {
        return todoRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> ResourceNotFoundException.forTodo(id));
    }

    @Transactional
    public TodoDto create(CreateTodoRequest request) {
        var todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .build();

        return toDto(todoRepository.save(todo));
    }

    @Transactional
    public TodoDto update(Long id, UpdateTodoRequest request) {
        var todo = todoRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.forTodo(id));

        if (request.getTitle() != null) {
            todo.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            todo.setDescription(request.getDescription());
        }

        if (request.getDueDate() != null) {
            todo.setDueDate(request.getDueDate());
        }

        if (request.getStatus() != null) {
            todo.setStatus(request.getStatus());
        }

        return toDto(todoRepository.save(todo));
    }

    @Transactional
    public TodoDto updateStatus(Long id, TodoStatus newStatus) {
        var todo = todoRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.forTodo(id));

        todo.setStatus(newStatus);

        return toDto(todoRepository.save(todo));
    }

    // -------------------------------------------------------------------------
    // TODO 6 — delete
    // -------------------------------------------------------------------------
    // Find the todo by id (throw 404 if missing). Then delete it.
    // -------------------------------------------------------------------------
    @Transactional
    public void delete(Long id) {
        throw new UnsupportedOperationException("TODO 6: implement delete");
    }

    // -------------------------------------------------------------------------
    // Mapping helper — converts a Todo entity to a TodoDto.
    // You can use this in all methods above with: .map(this::toDto)
    // -------------------------------------------------------------------------
    private TodoDto toDto(Todo todo) {
        return TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .status(todo.getStatus().name())
                .createdAt(todo.getCreatedAt())
                .dueDate(todo.getDueDate())
                .build();
    }
}
