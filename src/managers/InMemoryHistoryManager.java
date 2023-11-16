package managers;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> historyList = new LinkedList<>();
    private final static int historySize = 10;

    @Override
    public void add(Task task) {
        if (historyList.size() >= historySize) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyListCopy = new LinkedList<>(historyList);
        return historyListCopy;
    }
}