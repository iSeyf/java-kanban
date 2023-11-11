import java.util.*;

public class TaskManager {
    private int id = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private int getNewId() {
        return ++id;
    }       //генерируется в ADD

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
        List<Subtask> subtasks = getAllSubtasks();
        for (Subtask subtask : subtasks) {
            deleteSubtaskById(subtask.getId());
        }
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
            epicMap.get(subtask.getEpicId()).addSubtask(subtask.getId(), subtask);
            epicMap.get(epicId).updateEpicStatus();
        }
    }

    public void updateTask(Task task, String status) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskMap.get(task.getId()).setStatus(status);
        }
    }

    public void updateSubtask(Subtask subtask, String status) {    //надо исправить
        if (subtask != null && subtaskMap.containsKey(subtask.getId())) {
            subtaskMap.get(subtask.getId()).setStatus(status);
            epicMap.get(subtask.getEpicId()).updateEpicStatus();
        }
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    public void deleteEpicById(int id) {
        if (epicMap.get(id) != null) {
            HashMap<Integer, Subtask> deleteSubtasks = epicMap.get(id).getSubtasks();
            for (Integer subtask : deleteSubtasks.keySet()) {
                subtaskMap.remove(subtask);
            }
            epicMap.remove(id);
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask deletedSubtask = subtaskMap.get(id);
        Epic inEpic = epicMap.get(deletedSubtask.getEpicId());
        inEpic.getSubtasks().remove(deletedSubtask.getId());
        subtaskMap.remove(id);
        inEpic.updateEpicStatus();
    }

    public HashMap<Integer, Subtask> getSubtasksInEpic(int epicId) {
        return epicMap.get(epicId).getSubtasks();
    }
}