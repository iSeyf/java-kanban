package historyManagerTest;

import interfaces.HistoryManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void emptyHistoryList() {
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void removeDuplicates() {
        Task task1 = new Task("task1", "task1Description");
        task1.setId(1);
        Task task2 = new Task("task2", "task2Description");
        task2.setId(2);
        manager.add(task1);
        manager.add(task2);
        manager.add(task1);
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeTask() {
        List<Task> historyList = new ArrayList<>();
        Task task1 = new Task("task1", "task1Description");
        task1.setId(1);
        Task task2 = new Task("task2", "task2Description");
        task2.setId(2);
        Task task3 = new Task("task3", "task3Description");
        task3.setId(3);
        Task task4 = new Task("task4", "task4Description");
        task4.setId(4);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task4);
        manager.remove(task1.getId());
        historyList.add(task2);
        historyList.add(task3);
        historyList.add(task4);
        assertEquals(historyList, manager.getHistory());
        manager.remove(task3.getId());
        historyList.remove(task3);
        assertEquals(historyList,manager.getHistory());
        manager.remove(task4.getId());
        historyList.remove(task4);
        assertEquals(historyList,manager.getHistory());
    }
}