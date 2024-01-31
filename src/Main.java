import managers.Managers;
import interfaces.TaskManager;
import server.KVServer;
import tasks.Task;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("name", "description");
        taskManager.addTask(task);
    }
}