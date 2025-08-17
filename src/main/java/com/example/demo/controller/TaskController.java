package com.example.demo.controller;

import com.example.demo.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private int idCounter = 1;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        boolean exists = tasks.values().stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(task.getName()));

        if (exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(task); // 200
    }

    @GetMapping
    public ResponseEntity<Collection<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks.values()); // 200 завжди
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        if (!tasks.containsKey(id)) {
            return ResponseEntity.notFound().build(); // 404 якщо нема
        }
        updatedTask.setId(id);
        tasks.put(id, updatedTask);
        return ResponseEntity.ok(updatedTask); // 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        if (!tasks.containsKey(id)) {
            return ResponseEntity.notFound().build(); // 404 якщо нема
        }
        tasks.remove(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}