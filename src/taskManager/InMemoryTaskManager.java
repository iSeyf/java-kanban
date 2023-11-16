package taskManager;

import historyManager.HistoryManager;
import managers.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epicMap.values()) {
            epic.getSubtasks().clear();
        }
        subtaskMap.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtaskMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            task.setId(getNewId());
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(getNewId());
            epicMap.put(epic.getId(), epic);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(getNewId());
            int epicId = subtask.getEpicId();
            subtaskMap.put(subtask.getId(), subtask);
            epicMap.get(epicId).addSubtaskId(subtask.getId());
            updateEpicStatus(epicId);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtaskMap.containsKey(subtask.getId())) {
            subtaskMap.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (taskMap.get(id) != null) {
            taskMap.remove(id);
        }
    }

    @Override
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

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask deletedSubtask = subtaskMap.get(id);
        if (deletedSubtask != null) {
            Epic inEpic = epicMap.get(deletedSubtask.getEpicId());
            inEpic.getSubtasks().remove(id);
            subtaskMap.remove(id);
            updateEpicStatus(deletedSubtask.getEpicId());
        }
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int getNewId() {
        return ++id;
    }

    private void updateEpicStatus(Integer epicId) {
        Status newStatus;
        Epic epic = epicMap.get(epicId);
        List<Integer> subtaskList = epic.getSubtasks();
        if (!subtaskList.isEmpty()) {
            int quantityForDone = 0;
            int quantityForNew = 0;
            List<Integer> keys = new ArrayList<>(subtaskList);
            for (Integer key : keys) {
                Status status = subtaskMap.get(key).getStatus();
                if (status.equals(Status.DONE)) {
                    quantityForDone++;
                } else if (status.equals(Status.NEW)) {
                    quantityForNew++;
                }
            }
            if (quantityForDone == subtaskList.size()) {
                newStatus = Status.DONE;
            } else if (quantityForNew == subtaskList.size()) {
                newStatus = Status.NEW;
            } else {
                newStatus = Status.IN_PROGRESS;
            }
        } else {
            newStatus = Status.NEW;
        }
        epic.setStatus(newStatus);
    }
}