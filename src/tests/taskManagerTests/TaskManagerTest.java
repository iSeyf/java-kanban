package tests.taskManagerTests;

import interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    abstract public void setTaskManager();

    @BeforeEach
    public void beforeEach() {
        setTaskManager();
    }

    @Test
    public void addTaskTest() {
        taskManager.addTask(null);
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач не пуст.");

        Task task = new Task("task", "descriptionTask");
        taskManager.addTask(task);
        Task savedTask = taskManager.getTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> taskList = taskManager.getAllTasks();

        assertNotNull(taskList, "Задачи не возвращаются.");
        assertEquals(1, taskList.size(), "Неверное количество задач.");
        assertEquals(task, taskList.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addEpicTest() {
        taskManager.addEpic(null);
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список эпиков не пуст.");

        Epic epic = new Epic("epic", "descriptionEpic");
        taskManager.addEpic(epic);
        Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        List<Epic> epicList = taskManager.getAllEpics();

        assertNotNull(epicList, "Эпики не возвращаются.");
        assertEquals(1, epicList.size(), "Неверное количество эпиков.");
        assertEquals(epic, epicList.get(0), "Эпики не совпадают.");
    }

    @Test
    public void addSubtaskTest() {
        taskManager.addSubtask(null);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пуст.");

        Epic epic = new Epic("epic", "descriptionEpic");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "description", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        List<Subtask> subtaskList = taskManager.getAllSubtasks();
        assertNotNull(subtaskList, "Подзадачи не возвращаются.");
        assertEquals(1, subtaskList.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtaskList.get(0), "Подзадачи не совпадают.");

        List<Subtask> subtasksInEpic = taskManager.getSubtasksInEpic(subtask.getEpicId());
        assertNotNull(subtasksInEpic, "Подзадачи эпиков не возвращаются.");
        assertEquals(1, subtasksInEpic.size(), "Неверное количество подзадач в эпике.");
        assertEquals(subtask, subtasksInEpic.get(0), "Подзадача в эпике не совпадает с подзадачей в листе.");
    }

    @Test
    public void deleteAllTasksTest() {
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач не пуст.");

        Task task = new Task("task", "descriptionTask");
        Task task2 = new Task("task2", "descriptionTask2");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи не были удалены.");
    }

    @Test
    public void deleteAllEpicsTest() {
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список эпиков не пуст.");

        Epic epic = new Epic("epic", "descriptionEpic");
        Epic epic2 = new Epic("epic2", "descriptionEpic2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        Subtask subtask = new Subtask("subtask", "description", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "description2", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Эпики не были удалены.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Подзадачи не были удалены.");
    }

    @Test
    public void deleteAllSubtasksTest() {
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пуст.");

        Epic epic = new Epic("epic", "descriptionEpic");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "description", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "description2", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Подзадачи не были удалены.");
        assertTrue(taskManager.getEpicById(epic.getId()).getSubtasks().isEmpty(),
                "Подзадачи не были удалены из эпика.");
    }

    @Test
    public void getTaskByIdTest() {
        Task task = taskManager.getTaskById(null);
        assertNull(task, "Должен вернуть Null.");
        Task task1 = new Task("task", "descriptionTask");
        taskManager.addTask(task1);
        Task task2 = taskManager.getTaskById(task1.getId());
        assertEquals(task2, task1, "Задачи не совпадают.");
    }

    @Test
    public void getEpicByIdTest() {
        Epic epic = taskManager.getEpicById(null);
        assertNull(epic, "Должен вернуть Null.");
        Epic epic1 = new Epic("epic", "descriptionEpic");
        taskManager.addEpic(epic1);
        Epic epic2 = taskManager.getEpicById(epic1.getId());
        assertEquals(epic2, epic1, "Эпики не совпадают.");
    }

    @Test
    public void getSubtaskByIdTest() {
        Subtask subtask = taskManager.getSubtaskById(null);
        assertNull(subtask, "Должен вернуть Null.");
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask", "subtaskDescription", epic.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(subtask2, subtask1, "Подзадачи не совпадают.");
    }

    @Test
    public void updateTaskTest() {
        Task nullTask = null;
        taskManager.updateTask(nullTask);
        assertNull(nullTask, "Должен быть Null.");

        Task task = new Task("task", "taskDescription");
        taskManager.addTask(task);
        Task task1 = new Task("task1", "updatedTaskDescription");
        task1.setId(task.getId());
        taskManager.updateTask(task1);
        assertEquals(task1, taskManager.getTaskById(task.getId()), "Задача не обновлена.");
    }

    @Test
    public void updateEpicTest() {
        Epic nullEpic = null;
        taskManager.updateEpic(nullEpic);
        assertNull(nullEpic, "Должен быть Null.");
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Epic updatedEpic = new Epic("UpdatedEpic", "updatedEpicDescription");
        updatedEpic.setId(epic.getId());
        taskManager.updateEpic(updatedEpic);
        assertEquals(updatedEpic, taskManager.getEpicById(epic.getId()), "Эпик не обновлен.");
    }

    @Test
    public void updateSubtaskTest() {
        Subtask nullSubtask = null;
        taskManager.updateSubtask(nullSubtask);
        assertNull(nullSubtask, "Должен быть Null.");
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskDescription", epic.getId());
        updatedSubtask.setId(subtask.getId());
        taskManager.updateSubtask(updatedSubtask);
        assertEquals(updatedSubtask, taskManager.getSubtaskById(subtask.getId()), "Подзадача не обновлена.");
    }

    @Test
    public void deleteTaskByIdTest() {
        taskManager.deleteTaskById(null);
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список не пуст.");

        Task task = new Task("task", "description");
        taskManager.addTask(task);
        taskManager.deleteTaskById(task.getId());
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задача не была удалена.");
    }

    @Test
    public void deleteEpicByIdTest() {
        taskManager.deleteEpicById(null);
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список не пуст.");

        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "description", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.deleteEpicById(epic.getId());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Эпик не был удален.");
    }

    @Test
    public void deleteSubtaskByIdTest() {
        taskManager.deleteSubtaskById(null);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список не пуст.");
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "description", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Подзадача не была удалена.");
        assertTrue(taskManager.getEpicById(epic.getId()).getSubtasks().isEmpty(),
                "Подзадача не была удалена из эпика.");
    }

    @Test
    public void getSubtasksInEpicTest() {
        List<Subtask> nullSubtaskList = taskManager.getSubtasksInEpic(null);
        assertTrue(nullSubtaskList.isEmpty(), "Список должен быть пуст.");
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "description", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "description2", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(subtask);
        subtaskList.add(subtask2);
        assertNotNull(taskManager.getSubtasksInEpic(epic.getId()), "Список пуст.");
        assertEquals(subtaskList, taskManager.getSubtasksInEpic(epic.getId()), "Списки не совпадают.");
    }

    @Test
    public void getHistoryTest() {
        assertTrue(taskManager.getHistory().isEmpty(), "Список должен быть пуст.");
        Task task1 = new Task("task1", "taskDescription1");
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "taskDescription2");
        taskManager.addTask(task2);
        Epic epic1 = new Epic("epic1", "epicDescription1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", epic1.getId());
        taskManager.addSubtask(subtask2);
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task2.getId());
        List<Task> historyList = new ArrayList<>();
        historyList.add(subtask2);
        historyList.add(epic1);
        historyList.add(task2);
        assertEquals(historyList, taskManager.getHistory(), "История выводится некорректно.");
    }

    @Test
    public void getPrioritizedTasksTest() {
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список должен быть пуст.");
        Task task1 = new Task("task1", "taskDescription1", LocalDateTime.now(), 30);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "taskDescription2");
        taskManager.addTask(task2);
        Epic epic1 = new Epic("epic1", "epicDescription1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", epic1.getId(),
                LocalDateTime.now().plus(Duration.ofMinutes(70)), 30);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", epic1.getId(),
                LocalDateTime.now().plus(Duration.ofMinutes(35)), 30);
        taskManager.addSubtask(subtask2);
        TreeSet<Task> taskTreeSet = taskManager.getPrioritizedTasks();

        assertNotNull(taskTreeSet, "Список пуст.");
        assertEquals(task1, taskTreeSet.first(), "Задачи не совпадают.");
        assertEquals(task2, taskTreeSet.last(), "Задачи не совпадают.");
    }
}