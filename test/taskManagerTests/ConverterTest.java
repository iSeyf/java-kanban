package taskManagerTests;

import interfaces.TaskManager;
import managers.Converter;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void toStringWithoutStartTimeAndDurationTest() {
        Task task = new Task("Task1", "taskDescription1");
        taskManager.addTask(task);

        String taskString = Converter.TaskToString(task);
        String strForTaskTest = String.format("%d,%s,%s,%s,%s,%s,%s,", task.getId(), task.getType(), task.getName(),
                task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration());
        assertEquals(strForTaskTest, taskString, "Результаты не совпадают.");

        Epic epic = new Epic("Epic1", "epicDescription1");
        taskManager.addEpic(epic);
        String epicString = Converter.TaskToString(epic);
        String strForEpicTest = String.format("%d,%s,%s,%s,%s,%s,%s,", epic.getId(), epic.getType(), epic.getName(),
                epic.getStatus(), epic.getDescription(), epic.getStartTime(), epic.getDuration());
        assertEquals(strForEpicTest, epicString, "Результаты не совпадают.");

        Subtask subtask = new Subtask("Subtask1", "subtaskDescription1", epic.getId());
        taskManager.addSubtask(subtask);
        String subtaskString = Converter.TaskToString(subtask);
        String strForSubtaskTest = String.format("%d,%s,%s,%s,%s,%s,%s,", subtask.getId(), subtask.getType(), subtask.getName(),
                subtask.getStatus(), subtask.getDescription(), subtask.getStartTime(), subtask.getDuration());
        assertEquals(strForSubtaskTest, subtaskString, "Результаты не совпадают.");
    }

    @Test
    public void toStringWithStartTimeAndDurationTest() {
        Task task = new Task("Task1", "taskDescription1", LocalDateTime.now(), 30);
        taskManager.addTask(task);

        String taskString = Converter.TaskToString(task);
        String strForTaskTest = String.format("%d,%s,%s,%s,%s,%s,%s,", task.getId(), task.getType(), task.getName(),
                task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration());
        assertEquals(strForTaskTest, taskString, "Результаты не совпадают.");

        Epic epic = new Epic("Epic1", "epicDescription1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "subtaskDescription1", epic.getId(),
                LocalDateTime.now().plus(Duration.ofMinutes(35)), 30);
        taskManager.addSubtask(subtask);

        String epicString = Converter.TaskToString(epic);
        String strForEpicTest = String.format("%d,%s,%s,%s,%s,%s,%s,", epic.getId(), epic.getType(), epic.getName(),
                epic.getStatus(), epic.getDescription(), epic.getStartTime(), epic.getDuration());
        assertEquals(strForEpicTest, epicString, "Результаты не совпадают.");

        String subtaskString = Converter.TaskToString(subtask);
        String strForSubtaskTest = String.format("%d,%s,%s,%s,%s,%s,%s,", subtask.getId(), subtask.getType(), subtask.getName(),
                subtask.getStatus(), subtask.getDescription(), subtask.getStartTime(), subtask.getDuration());
        assertEquals(strForSubtaskTest, subtaskString, "Результаты не совпадают.");
    }

    @Test
    public void fromStringTest() {
        Task task = new Task("Task1", "taskDescription1");
        taskManager.addTask(task);
        String taskString = Converter.TaskToString(task);
        Task task2 = Converter.TaskFromString(taskString);
        assertEquals(task.toString(), task2.toString(), "Задачи не совпадают.");
        task.setStartTime(LocalDateTime.now());
        task2.setStartTime(LocalDateTime.now());
        task.setDuration(30);
        task2.setDuration(30);
        assertEquals(task.toString(), task2.toString(), "Задачи не совпадают.");

        Epic epic = new Epic("Epic1", "epicDescription1");
        taskManager.addEpic(epic);
        String epicString = Converter.TaskToString(epic);
        Task epic2 = Converter.TaskFromString(epicString);
        assertEquals(epic.toString(), epic2.toString(), "Задачи не совпадают.");

        Subtask subtask = new Subtask("Subtask1", "subtaskDescription1", epic.getId());
        taskManager.addSubtask(subtask);
        String subtaskString = Converter.TaskToString(subtask) + subtask.getEpicId() + ",";
        Task subtask2 = Converter.TaskFromString(subtaskString);
        assertEquals(subtask.toString(), subtask2.toString(), "Задачи не совпадают.");
        subtask.setStartTime(LocalDateTime.MIN);
        subtask2.setStartTime(LocalDateTime.MIN);
        subtask.setDuration(30);
        subtask2.setDuration(30);
        assertEquals(subtask.toString(), subtask2.toString(), "Задачи не совпадают.");
    }

    @Test
    public void historyToStringTest() {
        Task task1 = new Task("task1", "taskDescription1");
        taskManager.addTask(task1);
        Epic epic1 = new Epic("epic1", "epicDescription1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", epic1.getId());
        String historyString = Converter.historyToString(taskManager.getHistoryManager());
        assertTrue(historyString.isEmpty(), "Строка не пуста.");

        taskManager.addSubtask(subtask1);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        historyString = Converter.historyToString(taskManager.getHistoryManager());
        String history = "1,2,3,";
        assertEquals(history, historyString, "Строки не совпадают.");
    }

    @Test
    public void historyFromStringTest() {
        String history1 = "";
        List<Integer> history1List = Converter.historyFromString(history1);
        assertNull(history1List);
        String history = "1,2,3,";
        List<Integer> historyList = Converter.historyFromString(history);
        List<Integer> listForTest = new ArrayList<>();
        listForTest.add(1);
        listForTest.add(2);
        listForTest.add(3);
        assertEquals(listForTest, historyList, "Списки не совпадают.");
    }
}