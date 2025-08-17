package com.example.demo.tests;

import com.example.demo.api.endpoints.TaskEndpoints;
import com.example.demo.api.utils.TaskDataUtils;
import com.example.demo.model.Task;
import com.example.demo.tests.utils.TestUtils;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskApiTests extends BaseTest {

    @AfterAll
    void afterAll() {
        TestUtils.cleanupAllTestTasks();
        List<Task> allTasks = TaskEndpoints.getAllTasks();
        Assertions.assertThat(allTasks)
                .describedAs("No tasks should remain after tests")
                .isEmpty();
    }

    @Test
    void createTask_ShouldReturn201() {
        String taskName = TestUtils.uniqueName("NewTask");
        Response response = TaskEndpoints.createTaskRaw(TaskDataUtils.defaultTask(taskName));

        Assertions.assertThat(response.statusCode())
                .describedAs("Status code when creating task")
                .isEqualTo(201);
    }

    @Test
    void createTask_ShouldReturn400_WhenDuplicateName() {
        String taskName = TestUtils.uniqueName("Duplicate");
        TaskEndpoints.createTask(TaskDataUtils.defaultTask(taskName));

        var duplicateTask = TaskDataUtils.defaultTask(taskName);
        var response = TaskEndpoints.createTaskExpectingError(duplicateTask);

        Assertions.assertThat(response.statusCode())
                .describedAs("Status code when creating duplicate task")
                .isEqualTo(400);
    }

    @Test
    void getTask_ShouldReturn200_WhenExists() {
        String taskName = TestUtils.uniqueName("Fetch");
        Task createdTask = TaskEndpoints.createTask(TaskDataUtils.defaultTask(taskName));

        Task fetchedTask = TaskEndpoints.getTaskById(createdTask.getId());

        Assertions.assertThat(fetchedTask.getId()).isEqualTo(createdTask.getId());
        Assertions.assertThat(fetchedTask.getName()).isEqualTo(taskName);
    }

    @Test
    void getTask_ShouldReturn404_WhenNotExists() {
        var response = TaskEndpoints.getTaskExpectingError(9999);
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    void updateTask_ShouldReturn200_WhenExists() {
        String taskName = TestUtils.uniqueName("Old");
        Task createdTask = TaskEndpoints.createTask(TaskDataUtils.defaultTask(taskName));
        createdTask.setName("Updated_" + taskName);
        createdTask.setCompleted(true);

        Task updatedTask = TaskEndpoints.updateTask(createdTask.getId(), createdTask);

        Assertions.assertThat(updatedTask.getName()).contains("Updated_");
        Assertions.assertThat(updatedTask.isCompleted()).isTrue();
    }

    @Test
    void updateTask_ShouldReturn404_WhenNotExists() {
        String taskName = TestUtils.uniqueName("NonExisting");
        Task dummyTask = new Task(9999, taskName, false);

        var response = TaskEndpoints.updateTaskExpectingError(dummyTask.getId(), dummyTask);
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    void getAllTasks_ShouldReturnListIncludingCreatedOnes() {
        Task first = TaskEndpoints.createTask(TaskDataUtils.defaultTask(TestUtils.uniqueName("Task1")));
        Task second = TaskEndpoints.createTask(TaskDataUtils.completedTask(TestUtils.uniqueName("Task2")));

        List<Task> allTasks = TaskEndpoints.getAllTasks();

        Assertions.assertThat(allTasks)
                .extracting(Task::getId)
                .contains(first.getId(), second.getId());
    }

    @Test
    void deleteTask_ShouldReturn204_WhenExists() {
        Task task = TaskEndpoints.createTask(TaskDataUtils.defaultTask(TestUtils.uniqueName("DeleteMe")));

        var response = TaskEndpoints.deleteTask(task.getId());
        Assertions.assertThat(response.statusCode()).isEqualTo(204);

        var getResponse = TaskEndpoints.getTaskExpectingError(task.getId());
        Assertions.assertThat(getResponse.statusCode()).isEqualTo(404);
    }

    @Test
    void deleteTask_ShouldReturn404_WhenNotExists() {
        var response = TaskEndpoints.deleteTask(9999);
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
    }
}