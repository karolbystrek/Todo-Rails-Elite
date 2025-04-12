package com.karolbystrek.todo.rails.elite.service;

import com.karolbystrek.todo.rails.elite.exceptions.ResourceAlreadyExistsException;
import com.karolbystrek.todo.rails.elite.exceptions.ResourceNotFoundException;
import com.karolbystrek.todo.rails.elite.model.Task;
import com.karolbystrek.todo.rails.elite.repository.TaskRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    // TODO 16: Log Exceptions. Use SLF4J to log exceptions in the service and controller layers.

    /**
     * Constructs a TaskService with the required repository.
     *
     * @param taskRepository The repository for task operations
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates a new task if it doesn't already exist.
     *
     * @param task The task to be added
     * @return The saved task with generated ID
     * @throws RuntimeException if a task with the same title already exists
     */
    public Task addTask(@NotNull(message = "Task cannot be null") Task task) throws ResourceAlreadyExistsException {
        if (taskRepository.findByTitle(task.getTitle()).isPresent()) {
            throw new ResourceAlreadyExistsException("Task with title '" + task.getTitle() + "' already exists");
        }
        return taskRepository.save(task);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve
     * @return The found task
     * @throws RuntimeException if no task is found with the given ID
     */
    public Task getTaskById(@NotNull(message = "Id cannot be null") Long id) throws ResourceNotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Task not found with id: " + id)
                );
    }

    /**
     * Retrieves a task by its title.
     *
     * @param title The title of the task to retrieve
     * @return The found task
     * @throws RuntimeException if no task is found with the given title
     */
    public Task getTaskByTitle(
            @NotNull(message = "Title cannot be null")
            @NotBlank(message = "Title cannot be blank")
            String title
    ) throws ResourceNotFoundException {
        return taskRepository.findByTitle(title)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Task not found with title: " + title)
                );
    }


    /**
     * Retrieves all tasks in the system.
     *
     * @return List of all tasks or empty list if no tasks exist
     */
    public List<Task> getAllTasks() {
        if (taskRepository.findAll().isEmpty()) {
            return List.of();
        }
        return taskRepository.findAll();
    }

    /**
     * Updates an existing task's details.
     *
     * @param task The task with updated information
     * @return The updated task
     * @throws RuntimeException if the task to update is not found
     */
    public Task updateTask(@NotNull(message = "Task cannot be null") Task task) throws ResourceNotFoundException {
        Optional<Task> existingTask = taskRepository.findByTitle(task.getTitle());
        if (existingTask.isEmpty()) {
            throw new ResourceNotFoundException("Task not found with title: " + task.getTitle());
        }
        Task taskToUpdate = existingTask.get();
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setCompleted(task.isCompleted());
        taskToUpdate.setDueDate(task.getDueDate());
        return taskRepository.save(taskToUpdate);
    }

    /**
     * Deletes a task from the system.
     *
     * @param task The task to be deleted
     * @throws RuntimeException if the task to delete is not found
     */
    public void deleteTask(@NotNull(message = "Task cannot be null") Task task) throws ResourceNotFoundException {
        Optional<Task> taskByTitle = taskRepository.findByTitle(task.getTitle());
        if (taskByTitle.isEmpty()) {
            throw new ResourceNotFoundException("Task not found with title: " + task.getTitle());
        }
        taskRepository.delete(task);
    }

    /**
     * Retrieves all incomplete tasks.
     *
     * @return List of pending tasks or empty list if none exist
     */
    public List<Task> getPendingTasks() {
        List<Task> allTasks = getAllTasks();
        if (allTasks.isEmpty()) {
            return List.of();
        }
        return allTasks.stream()
                .filter(task -> !task.isCompleted())
                .toList();
    }

    /**
     * Retrieves all completed tasks.
     *
     * @return List of completed tasks or empty list if none exist
     */
    public List<Task> getCompletedTasks() {
        List<Task> allTasks = getAllTasks();
        if (allTasks.isEmpty()) {
            return List.of();
        }
        return allTasks.stream()
                .filter(Task::isCompleted)
                .toList();
    }

    /**
     * Retrieves all incomplete tasks due today.
     *
     * @return List of today's incomplete tasks or empty list if none exist
     */
    public List<Task> getTodayTasks() {
        List<Task> allTasks = getAllTasks();
        if (allTasks.isEmpty()) {
            return List.of();
        }
        return allTasks.stream()
                .filter(
                        task -> !task.isCompleted()
                )
                .filter(
                        task -> task.getDueDate()
                                .isEqual(LocalDate.now())
                )
                .toList();
    }
}
