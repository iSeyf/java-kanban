package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    public void deleteAllTasks() {
        taskMap.clear();
    }

    public void deleteAllEpics() {
        epicMap.clear();
        subtaskMap.clear();
    }

    public void deleteAllSubtasks() {
        List<Integer> keys = new ArrayList<>(epicMap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            int key = keys.get(i);
            epicMap.get(key).getSubtasks().clear();
            updateEpicStatus(key);
        }
        subtaskMap.clear();
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    public void addTask(Task task) {
        if (task != null) {
            task.setId(getNewId());
            taskMap.put(task.getId(), task);
        }
    }

    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(getNewId());
            epicMap.put(epic.getId(), epic);
        }
    }

    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(getNewId());
            int epicId = subtask.getEpicId();
            subtaskMap.put(subtask.getId(), subtask);
            epicMap.get(epicId).addSubtaskId(subtask.getId());
            updateEpicStatus(epicId);
        }
    }

    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtaskMap.containsKey(subtask.getId())) {
            subtaskMap.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    public void deleteTaskById(int id) {
        if (taskMap.get(id) != null) {
            taskMap.remove(id);
        }
    }

    public void deleteEpicById(int id) {
        if (epicMap.get(id) != null) {
            List<Integer> deleteSubtasks = epicMap.get(id).getSubtasks();
            for (Integer subtask : deleteSubtasks) {
                subtaskMap.remove(subtask);
            }
            epicMap.remove(id);
        }
    }

    public void deleteSubtaskById(int deletedId) {
        if (subtaskMap.get(deletedId) != null) {
            Subtask deletedSubtask = subtaskMap.get(deletedId);
            Epic inEpic = epicMap.get(deletedSubtask.getEpicId());
            inEpic.getSubtasks().removeIf(n -> n == deletedId);
            subtaskMap.remove(deletedId);
            updateEpicStatus(deletedSubtask.getEpicId());
        }
    }

    public List<Subtask> getSubtasksInEpic(int epicId) {
        List<Subtask> subtasks = new ArrayList<>();
        if (epicMap.get(epicId) != null) {
            List<Integer> subtaskList = epicMap.get(epicId).getSubtasks();
            for (Integer subtask : subtaskList) {
                subtasks.add(subtaskMap.get(subtask));
            }
        }
        return subtasks;
    }

    private int getNewId() {
        return ++id;
    }

    private void updateEpicStatus(int epicId) {
        String newStatus = "NEW";
        if (!epicMap.get(epicId).getSubtasks().isEmpty()) {
            int quantityForDone = 0;
            int quantityForNew = 0;
            List<Integer> keys = new ArrayList<>(epicMap.get(epicId).getSubtasks());
            for (Integer key : keys) {
                String status = subtaskMap.get(key).getStatus();
                if (status.equals("DONE")) {
                    quantityForDone++;
                } else if (status.equals("NEW")) {
                    quantityForNew++;
                }
            }
            if (quantityForDone == epicMap.get(epicId).getSubtasks().size()) {
                newStatus = "DONE";
            } else if (quantityForNew == epicMap.get(epicId).getSubtasks().size()) {
                newStatus = "NEW";
            } else {
                newStatus = "IN_PROGRESS";
            }
        } else {
            newStatus = "NEW";
        }
        epicMap.get(epicId).setStatus(newStatus);
    }
}