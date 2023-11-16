package managers;

import taskManager.TaskManager;
import taskManager.InMemoryTaskManager;
import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}