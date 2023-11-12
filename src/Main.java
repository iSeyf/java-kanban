import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic2.getId());
        taskManager.addSubtask(subtask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        task1.setStatus("DONE");
        taskManager.updateTask(task1);
        subtask1.setStatus("DONE");
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(subtask2);
        subtask3.setStatus("DONE");
        taskManager.updateSubtask(subtask3);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(4);
        taskManager.deleteSubtaskById(5);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();


    }
}
