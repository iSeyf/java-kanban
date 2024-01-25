import managers.Managers;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.now().plus(Duration.ofMinutes(150)), 30);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.now().plus(Duration.ofMinutes(115)), 30);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId(), LocalDateTime.now(), 30);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId(), LocalDateTime.now().plus(Duration.ofMinutes(45)), 30);
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic1.getId(), LocalDateTime.now().plus(Duration.ofMinutes(80)), 30);
        taskManager.addSubtask(subtask3);

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);


        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getEpicById(3));
    }
}