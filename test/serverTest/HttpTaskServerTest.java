package serverTest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import interfaces.TaskManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager manager;
    HttpTaskServer server;
    HttpClient client;

    Gson gson = new Gson();

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test
    public void getAllTasksAndDeleteAllTasksEndpointsTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task task1 = new Task("task1", "tD1");
        Task task2 = new Task("task2", "tD2");
        manager.addTask(task1);
        manager.addTask(task2);
        HttpRequest request = getMethodGET(url);
        JsonArray jsonArray = getJsonArray(request);
        assertEquals(2, jsonArray.size(), "Количество задач не совпадает.");

        HttpRequest delete = getMethodDELETE(url);
        client.send(delete, HttpResponse.BodyHandlers.ofString());
        assertTrue(manager.getAllTasks().isEmpty(), "Список задач не был очищен.");
    }

    @Test
    public void getAllEpicssAndDeleteAllTasksEndpointsTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic1 = new Epic("epic1", "eD1");
        Epic epic2 = new Epic("epic2", "eD2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        HttpRequest request = getMethodGET(url);
        JsonArray jsonArray = getJsonArray(request);
        assertEquals(2, jsonArray.size(), "Количество эпиков не совпадает.");

        HttpRequest delete = getMethodDELETE(url);
        client.send(delete, HttpResponse.BodyHandlers.ofString());
        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков не был очищен.");
    }

    @Test
    public void getAllSubtasksAndDeleteAllTasksEndpointsTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("epic1", "eD1");
        Epic epic2 = new Epic("epic2", "eD2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Subtask subtask1 = new Subtask("subtask1", "sD1", epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "sD2", epic2.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        HttpRequest request = getMethodGET(url);
        JsonArray jsonArray = getJsonArray(request);
        assertEquals(2, jsonArray.size(), "Количество подзадач не совпадает.");

        HttpRequest delete = getMethodDELETE(url);
        client.send(delete, HttpResponse.BodyHandlers.ofString());
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список подзадач не был очищен.");
    }

    @Test
    public void postTaskTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task task = new Task("task1", "tD1");
        String json = gson.toJson(task);
        HttpRequest request = getMethodPOST(url, json);
        client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(1, manager.getAllTasks().size(), "Количество задач не совпадает.");
    }

    @Test
    public void postEpicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic = new Epic("epic1", "eD1");
        String json = gson.toJson(epic);
        HttpRequest request = getMethodPOST(url, json);
        client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(1, manager.getAllEpics().size(), "Количество эпиков не совпадает.");
    }

    @Test
    public void postSubtaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "eD1");
        manager.addEpic(epic);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Subtask subtask = new Subtask("subtask1", "sD1", epic.getId());
        String json = gson.toJson(subtask);
        HttpRequest request = getMethodPOST(url, json);
        client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(1, manager.getAllSubtasks().size(), "Количество подзадач не совпадает.");
    }

    @Test
    public void getAndDeleteTaskByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        Task task = new Task("task", "tD1");
        manager.addTask(task);
        HttpRequest request = getMethodGET(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);
        assertEquals(task.toString(), task1.toString(), "Задачи не совпадают.");

        HttpRequest requestDel = getMethodDELETE(url);
        client.send(requestDel, HttpResponse.BodyHandlers.ofString());
        assertTrue(manager.getAllTasks().isEmpty(), "Задача не удалилась.");
    }

    @Test
    public void getAndDeleteEpicByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        Epic epic = new Epic("epic", "tD1");
        manager.addEpic(epic);
        HttpRequest request = getMethodGET(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic.toString(), epic1.toString(), "Эпики не совпадают.");

        HttpRequest requestDel = getMethodDELETE(url);
        client.send(requestDel, HttpResponse.BodyHandlers.ofString());
        assertTrue(manager.getAllEpics().isEmpty(), "Эпик не удалился.");
    }

    @Test
    public void getAndDeleteSubtaskByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        Epic epic = new Epic("epic", "eD1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "sD1", epic.getId());
        manager.addSubtask(subtask);
        HttpRequest request = getMethodGET(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask1 = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.toString(), subtask1.toString(), "Подзадачи не совпадают.");

        HttpRequest requestDel = getMethodDELETE(url);
        client.send(requestDel, HttpResponse.BodyHandlers.ofString());
        assertTrue(manager.getAllSubtasks().isEmpty(), "Подзадача не удалилась.");
    }

    @Test
    public void getEpicSubtasksTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=1");
        Epic epic = new Epic("epic", "eD");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "sD1", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "sD2", epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        HttpRequest request = getMethodGET(url);
        JsonArray jsonArray = getJsonArray(request);
        assertEquals(2, jsonArray.size(), "Количество подзадач не совпадает.");
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history/");
        Task task = new Task("task", "tD");
        manager.addTask(task);
        manager.getTaskById(task.getId());
        HttpRequest request = getMethodGET(url);
        JsonArray jsonArray = getJsonArray(request);
        assertEquals(1, jsonArray.size(), "Количество задач не совпадает.");
    }

    public HttpRequest getMethodGET(URI uri) {
        return HttpRequest.newBuilder().uri(uri).GET().build();
    }

    public HttpRequest getMethodDELETE(URI uri) {
        return HttpRequest.newBuilder().uri(uri).DELETE().build();
    }

    public HttpRequest getMethodPOST(URI uri, String json) {
        return HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
    }

    public JsonArray getJsonArray(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        return jsonElement.getAsJsonArray();
    }
}