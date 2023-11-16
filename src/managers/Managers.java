package managers;

import interfaces.TaskManager;
import interfaces.HistoryManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}