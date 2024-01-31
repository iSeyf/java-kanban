package managers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import client.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Type;

public class HttpTaskManager extends FileBackedTasksManager {
    static KVTaskClient client;
    String url;
    static Gson gson = new Gson();

    public HttpTaskManager(String url) {
        this.url = url;
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(getAllTasks()));
        client.put("epics", gson.toJson(getAllEpics()));
        client.put("subtasks", gson.toJson(getAllSubtasks()));
        client.put("history", gson.toJson(getHistory()));
        client.put("prioritized", gson.toJson(getPrioritizedTasks()));
    }

    public static HttpTaskManager load(String url) {
        HttpTaskManager manager = new HttpTaskManager(url);
        if (!client.load("tasks").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("tasks"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Task task = gson.fromJson(json, Task.class);
                manager.taskMap.put(task.getId(), task);
                manager.taskSet.add(task);
            }
        }
        if (!client.load("epics").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("epics"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Epic epic = gson.fromJson(json, Epic.class);
                manager.epicMap.put(epic.getId(), epic);
            }
        }
        if (!client.load("subtasks").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("subtasks"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Subtask subtask = gson.fromJson(json, Subtask.class);
                manager.subtaskMap.put(subtask.getId(), subtask);
                manager.epicMap.get(subtask.getEpicId()).addSubtask(subtask);
                manager.updateSubtask(subtask);
                manager.taskSet.add(subtask);
            }
        }
        if (!client.load("history").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("history"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Task task = gson.fromJson(json, Task.class);
                if (task.getType().equals(Type.EPIC)) {
                    Epic epic = (Epic) task;
                    int id = epic.getId();
                    if (manager.epicMap.containsKey(id)) {
                        manager.historyManager.add(manager.epicMap.get(id));
                    }
                } else if (task.getType().equals(Type.SUBTASK)) {
                    Subtask subtask = (Subtask) task;
                    int id = subtask.getId();
                    if (manager.subtaskMap.containsKey(id)) {
                        manager.historyManager.add(manager.subtaskMap.get(id));
                    }
                } else {
                    int id = task.getId();
                    if (manager.taskMap.containsKey(id)) {
                        manager.historyManager.add(manager.taskMap.get(id));
                    }
                }
            }
        }
        return manager;
    }
}
