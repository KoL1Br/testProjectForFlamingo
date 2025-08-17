package com.example.demo.api.utils;

import com.example.demo.model.Task;

public class TaskDataUtils {

    public static Task defaultTask(String name) {
        return new Task(0, name, false);
    }

    public static Task completedTask(String name) {
        return new Task(0, name, true);
    }
}