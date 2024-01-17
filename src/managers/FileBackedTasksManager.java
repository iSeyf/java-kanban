package managers;

import exceptions.ManagerSaveException;
import tasks.Type;
import tasks.Task;
import tasks.Status;
import tasks.Subtask;
import tasks.Epic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File fileName;

    public FileBackedTasksManager(File file) {
        fileName = file;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            if (!taskMap.isEmpty()) {
                fileWriter.write("id,type,name,status,description,epic,\n");
                for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
                    fileWriter.write(Converter.TaskToString(entry.getValue()) + "\n");
                }
            }
            if (!epicMap.isEmpty()) {
                for (Map.Entry<Integer, Epic> entry : epicMap.entrySet()) {
                    fileWriter.write(Converter.TaskToString(entry.getValue()) + "\n");
                }
            }
            if (!subtaskMap.isEmpty()) {
                for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
                    fileWriter.write(Converter.TaskToString(entry.getValue()) + entry.getValue().getEpicId() + ",\n");
                }
            }
            if (!getHistory().isEmpty()) {
                fileWriter.write("\n");
                fileWriter.write(Converter.historyToString(historyManager));
            }
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File fromFile) {
        FileBackedTasksManager manager = new FileBackedTasksManager(fromFile);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fromFile))) {
            fileReader.readLine();
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (!line.isEmpty()) {
                    Task task = Converter.TaskFromString(line);
                    if (task.getType().equals(Type.TASK)) {
                        manager.taskMap.put(task.getId(), task);
                    } else if (task.getType().equals(Type.EPIC)) {
                        Epic epic = (Epic) task;
                        manager.epicMap.put(task.getId(), epic);
                    } else if (task.getType().equals(Type.SUBTASK)) {
                        Subtask subtask = (Subtask) task;
                        manager.subtaskMap.put(task.getId(), subtask);
                    }
                } else {
                    List<Integer> historyList = Converter.historyFromString(fileReader.readLine());
                    for (int taskId : historyList) {
                        if (manager.taskMap.containsKey(taskId)) {
                            manager.historyManager.add(manager.taskMap.get(taskId));
                        } else if (manager.epicMap.containsKey(taskId)) {
                            manager.historyManager.add(manager.epicMap.get(taskId));
                        } else if (manager.subtaskMap.containsKey(taskId)) {
                            manager.historyManager.add(manager.subtaskMap.get(taskId));
                        }
                    }
                    break;
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
        return manager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    public static void main(String[] args) {
        File file = new File("src/data/data.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        Task task1 = new Task("task1", "taskDescription1");
        manager.addTask(task1);
        Task task2 = new Task("task2", "taskDescription2");
        manager.addTask(task2);
        Epic epic1 = new Epic("epic1", "epicDescription1");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("epic2", "epicDescription2");
        manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", epic1.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", epic1.getId());
        manager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "subtaskDescription3", epic2.getId());
        manager.addSubtask(subtask3);

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        manager.updateSubtask(subtask3);

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getEpicById(4));
        System.out.println(manager.getHistory());
        System.out.println();

        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(file);
        System.out.println(manager2.getHistory());
    }
}