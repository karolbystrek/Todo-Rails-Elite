package com.karolbystrek.todo.rails.elite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    // TODO 19: Add Validation Error Messages

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title is required")
    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @NotNull(message = "Due date is required")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    public Task(String title, String description, boolean completed, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", dueDate=" + dueDate +
                '}';
    }
}
