package managers;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> taskSet = new TreeSet<>((task1, task2) -> {
        LocalDateTime startTime1 = task1.getStartTime();
        LocalDateTime startTime2 = task2.getStartTime();

        if (startTime1 == null && startTime2 == null) {
            return 0;
        } else if (startTime1 == null) {
            return 1;
        } else if (startTime2 == null) {
            return -1;
        } else {
            return startTime1.compareTo(startTime2);
        }
    });


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
        List<Task> tasks = getAllTasks();
        for (Task task : tasks) {
            deleteTaskById(task.getId());
        }
    }

    @Override
    public void deleteAllEpics() {
        List<Epic> epics = getAllEpics();
        for (Epic epic : epics) {
            deleteEpicById(epic.getId());
        }
    }

    @Override
    public void deleteAllSubtasks() {
        List<Subtask> subtasks = getAllSubtasks();
        for (Subtask subtask : subtasks) {
            deleteSubtaskById(subtask.getId());
        }
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
            if (!hasTimeOverlap(task)) {
                taskSet.add(task);
            }
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
            epicMap.get(epicId).addSubtask(subtask);
            updateEpicStatus(epicId);
            calculateTimeForEpic(epicId);
            if (!hasTimeOverlap(subtask)) {
                taskSet.add(subtask);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskSet.remove(taskMap.get(task.getId()));
            taskMap.put(task.getId(), task);
            if (!hasTimeOverlap(task)) {
                taskSet.add(task);
            }
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
            taskSet.remove(subtaskMap.get(subtask.getId()));
            subtaskMap.put(subtask.getId(), subtask);
            List<Subtask> subtaskList = epicMap.get(subtask.getEpicId()).getSubtasks();
            for (Subtask updatedSubtask : subtaskList) {
                if (updatedSubtask.getId() == subtask.getId()) {
                    subtaskList.remove(updatedSubtask);
                    subtaskList.add(subtask);
                    break;
                }
            }
            updateEpicStatus(subtask.getEpicId());
            calculateTimeForEpic(subtask.getEpicId());
            if (!hasTimeOverlap(subtask)) {
                taskSet.add(subtask);
            }
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        Task task = taskMap.get(id);
        if (task != null) {
            taskMap.remove(id);
            historyManager.remove(id);
            taskSet.remove(task);
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic deletedEpic = epicMap.get(id);
        if (deletedEpic != null) {
            List<Subtask> deleteSubtasks = deletedEpic.getSubtasks();
            for (Subtask subtask : deleteSubtasks) {
                subtaskMap.remove(subtask.getId());
                historyManager.remove(subtask.getId());
                taskSet.remove(subtask);
            }
            epicMap.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask deletedSubtask = subtaskMap.get(id);
        if (deletedSubtask != null) {
            Epic inEpic = epicMap.get(deletedSubtask.getEpicId());
            inEpic.getSubtasks().remove(deletedSubtask);
            subtaskMap.remove(id);
            historyManager.remove(id);
            taskSet.remove(deletedSubtask);
            updateEpicStatus(deletedSubtask.getEpicId());
            calculateTimeForEpic(deletedSubtask.getEpicId());
        }
    }

    @Override
    public List<Subtask> getSubtasksInEpic(Integer epicId) {
        List<Subtask> subtasks = new ArrayList<>();
        Epic epic = epicMap.get(epicId);
        if (epic != null) {
            List<Subtask> subtaskList = epic.getSubtasks();
            for (Subtask subtask : subtaskList) {
                subtasks.add(subtaskMap.get(subtask.getId()));
            }
        }
        return subtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return taskSet;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void calculateTimeForEpic(Integer epicId) {
        Epic epic = epicMap.get(epicId);
        if (!epic.getSubtasks().isEmpty()) {
            LocalDateTime earliestStartTime = null;
            int duration = 0;
            LocalDateTime latestEndTime = null;
            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStartTime() != null) {
                    if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                        earliestStartTime = subtask.getStartTime();
                    }
                }
                int subtaskDuration = subtask.getDuration();
                if (subtaskDuration != 0) {
                    duration = duration + subtaskDuration;
                }
                if (subtask.getEndTime() != null) {
                    if (latestEndTime == null || subtask.getEndTime().isAfter(latestEndTime)) {
                        latestEndTime = subtask.getEndTime();
                    }
                }
            }
            epic.setStartTime(earliestStartTime);
            epic.setDuration(duration);
            epic.setEndTime(latestEndTime);
        } else {
            epic.setStartTime(null);
            epic.setDuration(0);
            epic.setEndTime(null);
        }
    }

    private boolean hasTimeOverlap(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        if (startTime == null) {
            return false;
        }
        for (Task task2 : taskSet) {
            LocalDateTime startTime2 = task2.getStartTime();
            LocalDateTime endTime2 = task2.getEndTime();
            if (startTime2 != null && endTime2 != null) {
                if (!(endTime.isBefore(startTime2) || endTime2.isBefore(startTime))) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getNewId() {
        return ++id;
    }

    private void updateEpicStatus(Integer epicId) {
        Status newStatus;
        Epic epic = epicMap.get(epicId);
        List<Subtask> subtaskList = epic.getSubtasks();
        if (!subtaskList.isEmpty()) {
            int quantityForDone = 0;
            int quantityForNew = 0;
            List<Subtask> subtasks = new ArrayList<>(subtaskList);
            for (Subtask subtask : subtasks) {
                Status status = subtaskMap.get(subtask.getId()).getStatus();
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