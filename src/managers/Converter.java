package managers;

import interfaces.HistoryManager;
import tasks.Task;
import tasks.Epic;
import tasks.Subtask;
import tasks.Status;
import tasks.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static List<Integer> historyFromString(String value) {
        if (value.isEmpty()) {
            return null;
        }
        String[] history = value.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String taskId : history) {
            historyList.add(Integer.parseInt(taskId));
        }
        return historyList;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> historyList = manager.getHistory();
        StringBuilder history = new StringBuilder();
        for (Task task : historyList) {
            history.append(task.getId());
            history.append(",");
        }
        return history.toString();
    }

    public static String TaskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,", task.getId(), task.getType(), task.getName(), task.getStatus(),
                task.getDescription(), task.getStartTime(), task.getDuration());
    }

    public static Task TaskFromString(String value) {
        String[] taskData = value.split(",");
        int taskId = Integer.parseInt(taskData[0]);
        Type taskType = Type.valueOf(taskData[1]);
        String taskName = taskData[2];
        Status taskStatus = Status.valueOf(taskData[3]);
        String taskDescription = taskData[4];
        LocalDateTime taskStartTime = null;
        int taskDuration = 0;
        if (!taskData[5].equals("null")) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            taskStartTime = LocalDateTime.parse(taskData[5], formatter);
        }
        if (!taskData[6].equals("null")) {
            taskDuration = Integer.parseInt(taskData[6]);
        }


        if (taskType.equals(Type.TASK)) {
            Task task = new Task(taskName, taskDescription);
            task.setId(taskId);
            task.setStatus(taskStatus);
            if (taskStartTime != null) {
                task.setStartTime(taskStartTime);
            }
            if (taskDuration != 0) {
                task.setDuration(taskDuration);
            }
            return task;
        }
        if (taskType.equals(Type.SUBTASK)) {
            int epicId = Integer.parseInt(taskData[7]);
            Subtask subtask = new Subtask(taskName, taskDescription, epicId);
            subtask.setId(taskId);
            subtask.setStatus(taskStatus);
            if (taskStartTime != null) {
                subtask.setStartTime(taskStartTime);
            }
            if (taskDuration != 0) {
                subtask.setDuration(taskDuration);
            }
            return subtask;
        }
        Epic epic = new Epic(taskName, taskDescription);
        epic.setId(taskId);
        epic.setStatus(taskStatus);
        return epic;
    }
}