package com.karolbystrek.todo.rails.elite.service;

import com.karolbystrek.todo.rails.elite.model.Task;
import com.karolbystrek.todo.rails.elite.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Test Task", "Test Description", false, LocalDate.now());
        task.setId(1L);
    }

    @Test
    void givenNewTask_whenAddTask_thenReturnSavedTask() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.addTask(task);

        assertNotNull(savedTask);
        assertEquals(task.getTitle(), savedTask.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void givenExistingTaskTitle_whenAddTask_thenThrowException() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.of(task));

        assertThrows(RuntimeException.class, () -> taskService.addTask(task));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void givenValidId_whenGetTaskById_thenReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
    }

    @Test
    void givenInvalidId_whenGetTaskById_thenThrowException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void givenTasksExist_whenGetAllTasks_thenReturnTasksList() {
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> foundTasks = taskService.getAllTasks();

        assertFalse(foundTasks.isEmpty());
        assertEquals(1, foundTasks.size());
    }

    @Test
    void givenNoTasks_whenGetAllTasks_thenReturnEmptyList() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<Task> foundTasks = taskService.getAllTasks();

        assertTrue(foundTasks.isEmpty());
    }

    @Test
    void givenExistingTask_whenUpdateTask_thenReturnUpdatedTask() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(task);

        assertNotNull(updatedTask);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void givenNonExistingTask_whenUpdateTask_thenThrowException() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.updateTask(task));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void givenExistingTask_whenDeleteTask_thenTaskDeleted() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.of(task));

        taskService.deleteTask(task);

        verify(taskRepository).delete(task);
    }

    @Test
    void givenNonExistingTask_whenDeleteTask_thenThrowException() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(task));
        verify(taskRepository, never()).delete(any(Task.class));
    }

    @Test
    void givenTasksDueToday_whenGetTodayTasks_thenReturnTodayTasksList() {
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> todayTasks = taskService.getTodayTasks();

        assertFalse(todayTasks.isEmpty());
        assertEquals(LocalDate.now(), todayTasks.get(0).getDueDate());
    }

    @Test
    void givenMixedTasks_whenGetPendingTasks_thenReturnPendingTasksList() {
        Task completedTask = new Task("Completed", "Done", true, LocalDate.now());
        List<Task> tasks = List.of(task, completedTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> pendingTasks = taskService.getPendingTasks();

        assertEquals(1, pendingTasks.size());
        assertFalse(pendingTasks.get(0).isCompleted());
    }

    @Test
    void givenMixedTasks_whenGetCompletedTasks_thenReturnCompletedTasksList() {
        Task completedTask = new Task("Completed", "Done", true, LocalDate.now());
        List<Task> tasks = List.of(task, completedTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> completedTasks = taskService.getCompletedTasks();

        assertEquals(1, completedTasks.size());
        assertTrue(completedTasks.get(0).isCompleted());
    }
}
