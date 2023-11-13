package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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
        for (Epic epic : epicMap.values()) {
            epic.getSubtasks().clear();
        }
        subtaskMap.clear();
    }

    public Task getTaskById(Integer id) {
        return taskMap.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epicMap.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
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

    public void deleteTaskById(Integer id) {
        if (taskMap.get(id) != null) {
            taskMap.remove(id);
        }
    }

    public void deleteEpicById(Integer id) {
        Epic deletedEpic = epicMap.get(id);
        if (deletedEpic != null) {
            List<Integer> deleteSubtasks = deletedEpic.getSubtasks();
            for (Integer subtask : deleteSubtasks) {
                subtaskMap.remove(subtask);
            }
            epicMap.remove(id);
        }
    }

    public void deleteSubtaskById(Integer id) {
        Subtask deletedSubtask = subtaskMap.get(id);
        if (deletedSubtask != null) {
            Epic inEpic = epicMap.get(deletedSubtask.getEpicId());
            inEpic.getSubtasks().remove(id);
            subtaskMap.remove(id);
            updateEpicStatus(deletedSubtask.getEpicId());
        }
    }

    public List<Subtask> getSubtasksInEpic(Integer epicId) {
        List<Subtask> subtasks = new ArrayList<>();
        Epic epic = epicMap.get(epicId);
        if (epic != null) {
            List<Integer> subtaskList = epic.getSubtasks();
            for (Integer subtask : subtaskList) {
                subtasks.add(subtaskMap.get(subtask));
            }
        }
        return subtasks;
    }

    private int getNewId() {
        return ++id;
    }

    private void updateEpicStatus(Integer epicId) {
        String newStatus = "NEW";
        Epic epic = epicMap.get(epicId);
        List<Integer> subtaskList = epic.getSubtasks();
        if (!subtaskList.isEmpty()) {
            int quantityForDone = 0;
            int quantityForNew = 0;
            List<Integer> keys = new ArrayList<>(subtaskList);
            for (Integer key : keys) {
                String status = subtaskMap.get(key).getStatus();
                if (status.equals("DONE")) {
                    quantityForDone++;
                } else if (status.equals("NEW")) {
                    quantityForNew++;
                }
            }
            if (quantityForDone == subtaskList.size()) {
                newStatus = "DONE";
            } else if (quantityForNew == subtaskList.size()) {
                newStatus = "NEW";
            } else {
                newStatus = "IN_PROGRESS";
            }
        } else {
            newStatus = "NEW";
        }
        epic.setStatus(newStatus);
    }
}