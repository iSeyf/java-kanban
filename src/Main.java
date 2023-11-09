public class Main {

    public static void main(String[] args) {
        TaskManager taskManager=new TaskManager();
        Task task1=new Task("Задача 1","Описание 1",
                taskManager.getNewId());
        Task task2=new Task("Задача 2","Описание 2",
                taskManager.getNewId());
        Epic epic1=new Epic("Эпик 1","Описание эпика 1",
                taskManager.getNewId());
        Epic epic2=new Epic("Эпик 2","Описание эпика 2",
                taskManager.getNewId());
        Subtask subtask1= new Subtask("Подзадача 1","Описание подзадачи 1",
                taskManager.getNewId(), epic1.getId());
        Subtask subtask2= new Subtask("Подзадача 2","Описание подзадачи 2",
                taskManager.getNewId(), epic1.getId());
        Subtask subtask3=new Subtask("Подзадача 1","Описание подзадачи 1",
                taskManager.getNewId(), epic2.getId());
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask2);
        System.out.println(taskManager.getList());
    }
}
