import java.util.*;

public class TaskManager {
    private int id=0;
    protected HashMap <Integer, Task> taskMap = new HashMap<>();
    protected HashMap <Integer, Epic> epicMap = new HashMap<>();
    protected HashMap <Integer, Subtask> subtaskMap  = new HashMap<>();
    protected HashMap <Integer, Object> allTasks=new HashMap<>();


    public int getNewId(){
        return ++id;
    }

    public List<Task> getAllTasks() {
        List<Task> result = new ArrayList<>();
        if (!taskMap.isEmpty()) {
            List<Integer> keys = new ArrayList<>(taskMap.keySet());
            for(int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                Task value = taskMap.get(key);
                result.add(value);
            }
        }
        return result;
    }

    public List<Epic> getAllEpics() {
        List<Epic> result = new ArrayList<>();
        if (!epicMap.isEmpty()) {
            List<Integer> keys = new ArrayList<>(epicMap.keySet());
            for(int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                Epic value = epicMap.get(key);
                result.add(value);
            }
        }
        return result;
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> result = new ArrayList<>();
        if (!subtaskMap.isEmpty()) {
            List<Integer> keys = new ArrayList<>(subtaskMap.keySet());
            for(int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                Subtask value = subtaskMap.get(key);
                result.add(value);
            }
        }
        return result;
    }

    public String getList(){
        String list="";
        if(!allTasks.isEmpty()){
            List<Integer>keys=new ArrayList<>(allTasks.keySet());
            for(int key:keys){
                list= list+allTasks.get(key)+System.lineSeparator();
            }
        }
        return list;
    }

    public void deleteAllTasks() {
        List<Task> tasks = getAllTasks();
        for (Task task : tasks) {
            deleteTaskById(task.getId());
        }
    }

    public void deleteAllEpics() {
        List<Epic> epics = getAllEpics();
        for (Epic epic : epics) {
            deleteEpicById(epic.getId());
        }
    }

    public void deleteAllSubtasks() {
        List<Subtask> subtasks = getAllSubtasks();
        for (Subtask subtask : subtasks) {
            deleteSubtaskById(subtask.getId());
        }


    }

    public void clearList() {
        taskMap.clear();
        subtaskMap.clear();
        epicMap.clear();
        allTasks.clear();
        id=0;
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Epic getEpicById(int id){
        return epicMap.get(id);
    }

    public Subtask getSubtaskById(int id){
        return subtaskMap.get(id);
    }


    public void addTask(Task task) {
        if(task != null) {
            taskMap.put(task.getId(), task);
            allTasks.put(task.getId(), task);
        }
    }

    public void addEpic(Epic epic) {
        if(epic != null) {
            epicMap.put(epic.getId(), epic);
            allTasks.put(epic.getId(),epic);
        }
    }

    public void addSubtask(Subtask subtask) {
        if(subtask != null) {
            subtaskMap.put(subtask.getId(), subtask);
            allTasks.put(subtask.getId(), subtask);
            epicMap.get(subtask.getEpicId()).addSubtask(subtask.getId(),subtask);
            epicMap.get(subtask.getEpicId()).updateEpicStatus();
        }
    }

    public void updateTask(Task task) {
        if(task != null && taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
            allTasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if(subtask != null && subtaskMap.containsKey(subtask.getId())) {
            int subtaskId=subtask.getId();
            subtaskMap.put(subtaskId, subtask);
            allTasks.put(subtaskId, subtask);
            epicMap.get(subtask.getEpicId()).getSubtasks().put(subtaskId, subtask);
            epicMap.get(subtask.getEpicId()).updateEpicStatus();
        }
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
        allTasks.remove(id);
    }

    public void deleteEpicById(int id) {
        if(epicMap.get(id) != null) {
            HashMap<Integer,Subtask> deleteSubtasks = epicMap.get(id).getSubtasks();
            if(deleteSubtasks != null){
                for(Integer subtask : deleteSubtasks.keySet()){
                    subtaskMap.remove(subtask);
                    allTasks.remove(subtask);
                }
            }
            epicMap.remove(id);
            allTasks.remove(id);
        }
    }

    public void deleteSubtaskById(int id){
        Subtask deletedSubtask = subtaskMap.get(id);
        Epic inEpic = epicMap.get(deletedSubtask.getEpicId());
        inEpic.getSubtasks().remove(deletedSubtask.getId());

        subtaskMap.remove(id);
        allTasks.remove(id);

        inEpic.updateEpicStatus();
    }

}