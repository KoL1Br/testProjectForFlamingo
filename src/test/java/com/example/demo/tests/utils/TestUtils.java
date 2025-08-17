package com.example.demo.tests.utils;

import com.example.demo.api.endpoints.TaskEndpoints;
import com.example.demo.model.Task;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestUtils {

    private static final String PREFIX = "TestTask_";

    public static String uniqueName(String base) {
        return PREFIX + base + "_" + UUID.randomUUID();
    }

    public static void cleanupAllTestTasks() {
        List<Task> allTasks = TaskEndpoints.getAllTasks();

        List<Task> testTasks = allTasks.stream()
                .filter(task -> TaskEndpoints.createdTasksID.contains(task.getId()))
                .toList();

        testTasks.forEach(task -> TaskEndpoints.deleteTask(task.getId()));

        TaskEndpoints.createdTasksID.clear();
    }
}