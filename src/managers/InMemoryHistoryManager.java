package managers;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyMap = new HashMap<>();

    private Node lastNode;
    private Node firstNode;

    @Override
    public void add(Task task) {
        if (task != null) {
            int addedTaskId = task.getId();
            if (historyMap.containsKey(addedTaskId)) {
                removeNode(historyMap.get(addedTaskId));
            }
            Node addedTask = linkLast(task);
            historyMap.put(addedTaskId, addedTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node task = historyMap.get(id);
        if (task != null) {
            removeNode(task);
        }
    }

    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (prevNode == null && nextNode == null) {
            lastNode = null;
            firstNode = null;
        } else if (prevNode == null) {
            nextNode.prev = null;
            firstNode = nextNode;
        } else if (nextNode == null) {
            prevNode.next = null;
            lastNode = prevNode;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        historyMap.remove(node.data.getId());
    }

    private Node linkLast(Task task) {
        Node newNode = new Node(lastNode, task, null);
        if (lastNode == null) {
            firstNode = newNode;
        } else {
            lastNode.next = newNode;
        }
        lastNode = newNode;
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node currentNode = firstNode;
        while (currentNode != null) {
            historyList.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return historyList;
    }

    private static class Node {
        public Node prev;
        public Node next;
        public Task data;

        public Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }
}