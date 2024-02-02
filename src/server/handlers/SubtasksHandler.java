package server.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class SubtasksHandler extends TasksHandler {
    public SubtasksHandler(TaskManager manager) {
        super(manager);
    }

    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange, exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS: {
                writeResponse(exchange, gson.toJson(manager.getAllSubtasks()), 200);
                break;
            }
            case GET_TASK_BY_ID: {
                Optional<Integer> subtaskId = getTaskId(exchange);
                if (subtaskId.isPresent()) {
                    int id = subtaskId.get();
                    if (manager.getSubtaskById(id) != null) {
                        writeResponse(exchange, gson.toJson(manager.getSubtaskById(id)), 200);
                    } else {
                        writeResponse(exchange, "Такой подзадачи нет!", 400);
                    }
                } else {
                    writeResponse(exchange, "Пустой запрос", 400);
                }
                break;
            }
            case POST_TASK: {
                InputStream is = exchange.getRequestBody();
                String subtaskBody = new String(is.readAllBytes(), DEFAULT_CHARSET);
                try {
                    Subtask subtask = gson.fromJson(subtaskBody, Subtask.class);
                    if (manager.getSubtaskById(subtask.getId()) == null) {
                        manager.addSubtask(subtask);
                        writeResponse(exchange, "Подзадача создана! " + gson.toJson(subtask), 200);
                    } else {
                        manager.updateSubtask(subtask);
                        writeResponse(exchange, "Подзадача обновлена!", 200);
                    }
                } catch (JsonSyntaxException exception) {
                    writeResponse(exchange, "Неправильный формат", 400);
                    break;
                }
                break;
            }
            case DELETE_TASK_BY_ID: {
                Optional<Integer> subtaskId = getTaskId(exchange);
                if (subtaskId.isPresent()) {
                    int id = subtaskId.get();
                    if (manager.getSubtaskById(id) != null) {
                        manager.deleteSubtaskById(id);
                        writeResponse(exchange, "Подзадача была удалена!", 200);
                    } else {
                        writeResponse(exchange, "Такой подзадачи нет!", 200);
                    }
                } else {
                    writeResponse(exchange, "Пустой запрос", 400);
                }
                break;
            }
            case DELETE_ALL_TASKS: {
                if (manager.getAllSubtasks().isEmpty()) {
                    writeResponse(exchange, "Список подзадач пуст", 200);
                } else {
                    manager.deleteAllSubtasks();
                    writeResponse(exchange, "Список подзадач был очищен!", 200);
                }
                break;
            }
            case GET_EPIC_SUBTASKS: {
                Optional<Integer> epicId = getTaskId(exchange);
                if (epicId.isPresent()) {
                    int id = epicId.get();
                    List<Subtask> subtasks = manager.getSubtasksInEpic(id);
                    writeResponse(exchange, gson.toJson(subtasks), 200);
                }
                break;
            }
            default: {
                writeResponse(exchange, "Метод " + exchange.getRequestMethod()
                        + " здесь некорректен! Ожидается другой метод! ", 405);
            }
        }
    }
}