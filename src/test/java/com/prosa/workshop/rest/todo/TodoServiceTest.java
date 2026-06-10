package com.prosa.workshop.rest.todo;

import com.prosa.workshop.rest.todo.dto.CreateTodoRequest;
import com.prosa.workshop.rest.todo.dto.TodoDto;
import com.prosa.workshop.rest.todo.exception.ResourceNotFoundException;
import com.prosa.workshop.rest.todo.model.Todo;
import com.prosa.workshop.rest.todo.model.TodoStatus;
import com.prosa.workshop.rest.todo.repository.TodoRepository;
import com.prosa.workshop.rest.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoService.
 * <p>
 * These run WITHOUT Spring context — fast, isolated, using Mockito to stub the repository.
 * <p>
 * Run with: ./gradlew test
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        sampleTodo = Todo.builder()
                .id(1L)
                .title("Buy milk")
                .status(TodoStatus.OPEN)
                .build();
    }

    // -------------------------------------------------------------------------
    // BONUS TEST 1 — findAll returns all todos when no status filter
    // -------------------------------------------------------------------------
    @Test
    void findAll_noFilter_returnsAllTodos() {
        when(todoRepository.findAll()).thenReturn(List.of(sampleTodo));

		var result = todoService.findAll(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Buy milk");
        verify(todoRepository).findAll();
    }

    // -------------------------------------------------------------------------
    // BONUS TEST 2 — findAll with status filter calls findByStatus
    // -------------------------------------------------------------------------
    @Test
    void findAll_withStatusFilter_returnFilteredTodos() {
        when(todoRepository.findByStatus(TodoStatus.OPEN)).thenReturn(List.of(sampleTodo));

		var result = todoService.findAll("open");

        assertThat(result).hasSize(1);
        verify(todoRepository).findByStatus(TodoStatus.OPEN);
        verify(todoRepository, never()).findAll();
    }

    // -------------------------------------------------------------------------
    // BONUS TEST 3 — findById throws when todo does not exist
    // -------------------------------------------------------------------------
    @Test
    void findById_notFound_throwsResourceNotFoundException() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // -------------------------------------------------------------------------
    // BONUS TEST 4 — create saves and returns the new todo
    // -------------------------------------------------------------------------
    @Test
    void create_validRequest_returnsSavedDto() {
		var request = new CreateTodoRequest();
        request.setTitle("New task");

		var saved = Todo.builder().id(42L).title("New task").status(TodoStatus.OPEN).build();
        when(todoRepository.save(any(Todo.class))).thenReturn(saved);

		var result = todoService.create(request);

        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getTitle()).isEqualTo("New task");
        assertThat(result.getStatus()).isEqualTo("OPEN");
    }
}
