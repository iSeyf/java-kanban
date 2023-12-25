package managers;

import interfaces.HistoryManager;
import tasks.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private static final int HISTORY_SIZE = 10;

    private Node<Task> lastNode = null;
    private Node<Task> firstNode = null;

    @Override
    public void add(Task task) {
        if (task != null) {
            int addedTaskId = task.getId();
            if (historyMap.size() >= HISTORY_SIZE) {
                int firstId = firstNode.getNode().getId();
                historyMap.remove(firstId);
            }
            if (historyMap.containsKey(addedTaskId)) {
                removeNode(historyMap.get(addedTaskId));
            }
            Node<Task> addedTask = linkLast(task);
            historyMap.put(addedTaskId, addedTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> task = historyMap.get(id);
        removeNode(task);
    }

    private void removeNode(Node<Task> node) {
        Node<Task> prevNode = node.getPrev();
        Node<Task> nextNode = node.getNext();
        if (prevNode == null && nextNode == null) {
            lastNode = null;
            firstNode = null;
        } else if (prevNode == null) {
            nextNode.setPrev(null);
            firstNode = nextNode;
        } else if (nextNode == null) {
            prevNode.setNext(null);
            lastNode = prevNode;
        } else {
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }
        historyMap.remove(node.getNode().getId());
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> newNode = new Node<>();
        newNode.setData(task);
        if (lastNode == null) {
            firstNode = newNode;
            lastNode = newNode;
        } else if (historyMap.size() == 1) {
            firstNode.setNext(newNode);
            newNode.setPrev(firstNode);
            lastNode = newNode;
        } else {
            lastNode.setNext(newNode);
            newNode.setPrev(lastNode);
            lastNode = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> currentNode = firstNode;
        while (currentNode != null) {
            historyList.add(currentNode.getNode());
            currentNode = currentNode.getNext();
        }
        return historyList;
    }
}