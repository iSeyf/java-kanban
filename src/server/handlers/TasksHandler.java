package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler implements HttpHandler {
    protected TaskManager manager;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected Gson gson = new Gson();

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS: {
                if (manager.getAllTasks().isEmpty()) {
                    writeResponse(exchange, "Список задач пуст", 200);
                } else {
                    writeResponse(exchange, gson.toJson(manager.getAllTasks()), 200);
                }
                break;
            }
            case GET_TASK_BY_ID: {
                Optional<Integer> taskId = getTaskId(exchange);
                if (taskId.isPresent()) {
                    int id = taskId.get();
                    if (manager.getTaskById(id) != null) {
                        writeResponse(exchange, gson.toJson(manager.getTaskById(id)), 200);
                    }else {
                        writeResponse(exchange, "Такой задачи нет!", 400);
                    }
                } else {
                    writeResponse(exchange, "Пустой запрос", 400);
                }
                break;
            }
            case POST_TASK: {
                InputStream is = exchange.getRequestBody();
                String taskBody = new String(is.readAllBytes(), DEFAULT_CHARSET);
                try {
                    Task task = gson.fromJson(taskBody, Task.class);
                    if (manager.getTaskById(task.getId()) == null) {
                        manager.addTask(task);
                        writeResponse(exchange, "Задача создана! " + gson.toJson(task), 200);
                    } else {
                        manager.updateTask(task);
                        writeResponse(exchange, "Задача обновлена!", 200);
                    }
                } catch (JsonSyntaxException exception) {
                    writeResponse(exchange, "Неправильный формат", 400);
                    break;
                }
                break;
            }
            case DELETE_TASK_BY_ID: {
                Optional<Integer> taskId = getTaskId(exchange);
                if (taskId.isPresent()) {
                    int id = taskId.get();
                    if (manager.getTaskById(id) != null) {
                        manager.deleteTaskById(id);
                        writeResponse(exchange, "Задача была удалена!", 200);
                    } else {
                        writeResponse(exchange, "Такой задачи нет!", 200);
                    }
                } else {
                    writeResponse(exchange, "Пустой запрос", 400);
                }
                break;
            }
            case DELETE_ALL_TASKS: {
                if (manager.getAllTasks().isEmpty()) {
                    writeResponse(exchange, "Список задач пуст", 200);
                } else {
                    manager.deleteAllTasks();
                    writeResponse(exchange, "Список задач был очищен!", 200);
                }
                break;
            }
            default: {
                writeResponse(exchange, "Такого запроса нет", 400);
            }
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        switch (requestMethod) {
            case "GET": {
                if (pathParts.length == 3) {
                    return Endpoint.GET_TASKS;
                } else if (pathParts.length == 4) {
                    return Endpoint.GET_TASK_BY_ID;
                } else if (pathParts.length == 5 && pathParts[3].equals("epic")) {
                    return Endpoint.GET_EPIC_SUBTASKS;
                }
                break;
            }
            case "POST": {
                return Endpoint.POST_TASK;
            }
            case "DELETE": {
                if (pathParts.length == 3) {
                    return Endpoint.DELETE_ALL_TASKS;
                } else if (pathParts.length == 4) {
                    return Endpoint.DELETE_TASK_BY_ID;
                }
            }
            default: {
                return Endpoint.UNKNOWN;
            }
        }
        return Endpoint.UNKNOWN;
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getRawQuery();
        String[] pathParts = path.split("=");
        try {
            int postId = Integer.parseInt(pathParts[1]);
            return Optional.of(postId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}