package taskManagerTests;

import managers.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @Override
    public void setTaskManager() {
        taskManager = new HttpTaskManager("http://localhost:8078", false);
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        setTaskManager();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void saveAndLoadEmptyListTest() {
        Task task = new Task("task", "tD1");
        taskManager.addTask(task);
        taskManager.deleteTaskById(1);
        HttpTaskManager manager2 = new HttpTaskManager("http://localhost:8078", true);
        assertTrue(manager2.getAllTasks().isEmpty(), "Список не пустой.");
        assertTrue(manager2.getAllEpics().isEmpty(), "Список не пустой.");
        assertTrue(manager2.getAllSubtasks().isEmpty(), "Список не пустой.");
    }

    @Test
    public void saveAndLoadTheEpicWithoutSubtasks() {
        Epic epic = new Epic("epic", "description");
        taskManager.addEpic(epic);
        HttpTaskManager manager2 = new HttpTaskManager("http://localhost:8078", true);
        assertEquals(taskManager.getAllEpics().size(), manager2.getAllEpics().size(),
                "Размеры списков не совпадают.");
    }

    @Test
    public void saveAndLoadEmptyHistoryList() {
        Task task = new Task("task", "tD");
        taskManager.addTask(task);
        HttpTaskManager manager2 = new HttpTaskManager("http://localhost:8078", true);
        assertTrue(taskManager.getHistory().isEmpty(), "Список не пуст.");
        assertTrue(manager2.getHistory().isEmpty(), "Список не пуст.");
    }

    @Test
    public void loadListsFromFile() {
        Task task = new Task("task", "tD");
        taskManager.addTask(task);
        Epic epic = new Epic("epic", "eD");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "sD", epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getEpicById(2);

        List<Task> tasks = taskManager.getAllTasks();
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        HttpTaskManager manager2 = new HttpTaskManager("http://localhost:8078", true);
        assertEquals(tasks.size(), manager2.getAllTasks().size(), "Список задач не совпадает.");
        assertEquals(epics.size(), manager2.getAllEpics().size(), "Список эпиков не совпадает.");
        assertEquals(subtasks.size(), manager2.getAllSubtasks().size(), "Список подзадач не совпадает.");
        assertEquals(taskManager.getHistory().toString(), manager2.getHistory().toString(),
                "Истории не совпадают");
    }
}