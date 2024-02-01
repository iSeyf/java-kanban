package managers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import client.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    protected KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String url, boolean isLoad) {
        client = new KVTaskClient(url);
        gson = new Gson();
        if (isLoad) {
            load(url);
        }
    }

    protected void save() {
        client.put("tasks", gson.toJson(getAllTasks()));
        client.put("epics", gson.toJson(getAllEpics()));
        client.put("subtasks", gson.toJson(getAllSubtasks()));
        String history = Converter.historyToString(historyManager);
        client.put("history", gson.toJson(history));
    }

    private void load(String url) {
        if (!client.load("tasks").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("tasks"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Task task = gson.fromJson(json, Task.class);
                taskMap.put(task.getId(), task);
                taskSet.add(task);
            }
        }
        if (!client.load("epics").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("epics"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Epic epic = gson.fromJson(json, Epic.class);
                epicMap.put(epic.getId(), epic);
            }
        }
        if (!client.load("subtasks").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("subtasks"));
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                Subtask subtask = gson.fromJson(json, Subtask.class);
                subtaskMap.put(subtask.getId(), subtask);
                epicMap.get(subtask.getEpicId()).addSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
                calculateTimeForEpic(subtask.getEpicId());
                taskSet.add(subtask);
            }
        }
        if (!client.load("history").isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(client.load("history"));
            String value = jsonElement.getAsString();
            List<Integer> historyList = Converter.historyFromString(value);
            if (historyList != null) {
                for (int taskId : historyList) {
                    if (taskMap.containsKey(taskId)) {
                        historyManager.add(taskMap.get(taskId));
                    } else if (epicMap.containsKey(taskId)) {
                        historyManager.add(epicMap.get(taskId));
                    } else if (subtaskMap.containsKey(taskId)) {
                        historyManager.add(subtaskMap.get(taskId));
                    }
                }
            }
        }
    }
}
