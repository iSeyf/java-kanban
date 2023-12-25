package tasks;

public class Node<T extends Task> {
    private T node;
    private Node<T> next = null;
    private Node<T> prev = null;

    public T getNode() {
        return node;
    }

    public Node<T> getNext() {
        return next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setData(T data) {
        this.node = data;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}
