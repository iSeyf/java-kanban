package managers;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new LinkedList<>();
    private static final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyList.size() >= HISTORY_SIZE) {
                historyList.remove(0);
            }
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}