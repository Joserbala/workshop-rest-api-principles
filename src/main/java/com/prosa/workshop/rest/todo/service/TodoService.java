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

    // -------------------------------------------------------------------------
    // TODO 3 — create
    // -------------------------------------------------------------------------
    // Build a new Todo entity from the request, save it, and return the DTO.
    // -------------------------------------------------------------------------
    @Transactional
    public TodoDto create(CreateTodoRequest request) {
        throw new UnsupportedOperationException("TODO 3: implement create");
    }

    // -------------------------------------------------------------------------
    // TODO 4 — update
    // -------------------------------------------------------------------------
    // Find the todo by id (throw 404 if missing). Apply non-null fields from
    // the request. Save and return the updated DTO.
    // -------------------------------------------------------------------------
    @Transactional
    public TodoDto update(Long id, UpdateTodoRequest request) {
        throw new UnsupportedOperationException("TODO 4: implement update");
    }

    // -------------------------------------------------------------------------
    // TODO 5 — updateStatus
    // -------------------------------------------------------------------------
    // Find the todo by id (throw 404 if missing). Set its status. Return DTO.
    // -------------------------------------------------------------------------
    @Transactional
    public TodoDto updateStatus(Long id, TodoStatus newStatus) {
        throw new UnsupportedOperationException("TODO 5: implement updateStatus");
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
