package server.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class EpicsHandler extends TasksHandler {
    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS: {
                if (manager.getAllEpics().isEmpty()) {
                    writeResponse(exchange, "Список эпиков пуст", 200);
                } else {
                    writeResponse(exchange, gson.toJson(manager.getAllEpics()), 200);
                }
                break;
            }
            case GET_TASK_BY_ID: {
                Optional<Integer> epicId = getTaskId(exchange);
                if (epicId.isPresent()) {
                    int id = epicId.get();
                    if (manager.getEpicById(id) != null) {
                        writeResponse(exchange, gson.toJson(manager.getEpicById(id)), 200);
                    } else {
                        writeResponse(exchange, "Такого эпика нет!", 400);
                    }
                } else {
                    writeResponse(exchange, "Пустой запрос", 400);
                }
                break;
            }
            case POST_TASK: {
                InputStream is = exchange.getRequestBody();
                String epicBody = new String(is.readAllBytes(), DEFAULT_CHARSET);
                try {
                    Epic epic = gson.fromJson(epicBody, Epic.class);
                    if (manager.getEpicById(epic.getId()) == null) {
                        manager.addEpic(epic);
                        writeResponse(exchange, "Эпик создан! " + gson.toJson(epic), 200);
                    } else {
                        manager.updateEpic(epic);
                        writeResponse(exchange, "Эпик обновлен!", 200);
                    }
                } catch (JsonSyntaxException exception) {
                    writeResponse(exchange, "Неправильный формат", 400);
                    break;
                }
                break;
            }
            case DELETE_TASK_BY_ID: {
                Optional<Integer> epicId = getTaskId(exchange);
                if (epicId.isPresent()) {
                    int id = epicId.get();
                    if (manager.getEpicById(id) != null) {
                        manager.deleteEpicById(id);
                        writeResponse(exchange, "Эпик был удален!", 200);
                    } else {
                        writeResponse(exchange, "Такого эпика нет!", 400);
                    }
                } else {
                    writeResponse(exchange, "Пустой запрос", 400);
                }
                break;
            }
            case DELETE_ALL_TASKS: {
                if (manager.getAllEpics().isEmpty()) {
                    writeResponse(exchange, "Список эпиков пуст", 400);
                } else {
                    manager.deleteAllEpics();
                    writeResponse(exchange, "Список эпиков был очищен!", 200);
                }
                break;
            }
            default: {
                writeResponse(exchange, "Такого запроса нет", 400);
            }
        }
    }
}