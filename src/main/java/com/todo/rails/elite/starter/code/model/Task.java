package com.todo.rails.elite.starter.code.model;

import jakarta.persistence.*;
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

	@Column(name = "title", unique = true, nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "completed", nullable = false)
	private boolean completed;

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
