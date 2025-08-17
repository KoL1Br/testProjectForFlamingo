package com.example.demo.api.endpoints;

import com.example.demo.model.Task;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TaskEndpoints {

    private static final String BASE_PATH = "/tasks";
    public static final List<Integer> createdTasksID = new ArrayList<>();

    public static Task createTask(Task task) {
        Task response = given()
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract()
                .as(Task.class);

        createdTasksID.add(response.getId());

        return response;
    }

    public static Response createTaskRaw(Task task) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .post(BASE_PATH)
                .then()
                .extract()
                .response();

        if (response.statusCode() == 201) {
            int id = response.jsonPath().getInt("id");
            createdTasksID.add(id);
        }
        return response;
    }

    public static Response createTaskExpectingError(Task task) {
        return given()
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .post(BASE_PATH)
                .then()
                .extract()
                .response();
    }

    public static Task getTaskById(int id) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_PATH + "/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .as(Task.class);
    }

    public static Response getTaskExpectingError(int id) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_PATH + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public static Task updateTask(int id, Task task) {
        return given()
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .put(BASE_PATH + "/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .as(Task.class);
    }

    public static Response updateTaskExpectingError(int id, Task task) {
        return given()
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .put(BASE_PATH + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public static List<Task> getAllTasks() {
        return given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", Task.class);
    }

    public static Response deleteTask(int id) {
        return given()
                .when()
                .delete(BASE_PATH + "/{id}", id)
                .then()
                .extract()
                .response();
    }
}