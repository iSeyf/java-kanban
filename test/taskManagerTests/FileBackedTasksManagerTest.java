package taskManagerTests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File("src/data/data2.csv");

    @Override
    public void setTaskManager() {
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    public void saveAndLoadEmptyListTest() {
        taskManager.addTask(null);
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

    @Test
    public void loadListsFromFile() {
        Task task = new Task("task", "taskDescription", LocalDateTime.now(), 30);
        taskManager.addTask(task);
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.now().plusMinutes(35), 30);
        taskManager.addSubtask(subtask);

        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        List<Task> tasks = taskManager.getAllTasks();
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> history = taskManager.getHistory();
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(file);
        assertEquals(tasks.toString(), manager2.getAllTasks().toString(), "Список задач не совпадает.");
        assertEquals(epics.toString(), manager2.getAllEpics().toString(), "Список эпиков не совпадает.");
        assertEquals(subtasks.toString(), manager2.getAllSubtasks().toString(), "Список подзадач не совпадает.");
        assertEquals(prioritizedTasks, manager2.getPrioritizedTasks(), "Отсортированные списки не совпадают.");
        assertEquals(history.toString(), manager2.getHistory().toString(), "Списки истории не совпадают.");
    }
}