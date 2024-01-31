package server;

import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import server.handlers.EpicsHandler;
import server.handlers.HistoryHandler;
import server.handlers.PrioritizedHandler;
import server.handlers.SubtasksHandler;
import server.handlers.TasksHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    TaskManager taskManager;
    HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task/", new TasksHandler(taskManager));
        server.createContext("/tasks/epic/", new EpicsHandler(taskManager));
        server.createContext("/tasks/subtask/", new SubtasksHandler(taskManager));
        server.createContext("/tasks/", new PrioritizedHandler(taskManager));
        server.createContext("/tasks/history/", new HistoryHandler(taskManager));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
