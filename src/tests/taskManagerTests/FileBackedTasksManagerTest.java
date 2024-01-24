package tests.taskManagerTests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;
import tests.taskManagerTests.TaskManagerTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File("src/data/data2.csv");

    @Override
    public void setTaskManager() {
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    public void saveAndLoadEmptyListTest() {
        taskManager.save();
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(file);
        assertTrue(manager2.getAllTasks().isEmpty(), "Список не пустой.");
        assertTrue(manager2.getAllEpics().isEmpty(), "Список не пустой.");
        assertTrue(manager2.getAllSubtasks().isEmpty(), "Список не пустой.");
    }

    @Test
    public void saveAndLoadTheEpicWithoutSubtasks() {
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(file);
        assertEquals(1, manager2.getAllEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    public void saveAndLoadEmptyHistoryList() {
        Task task = new Task("task", "description");
        taskManager.addTask(task);
        List<Task> history = taskManager.getHistory();
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(file);
        assertTrue(manager2.getHistory().isEmpty(), "Список не пустой.");
        assertEquals(history, manager2.getHistory(), "Истории не совпадают.");
    }
}