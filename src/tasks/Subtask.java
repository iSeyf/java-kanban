package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return getType() + "{ " + "Id= " + getId() + ", Name= " + getName() + ", Description= " + getDescription()
                + ", Status= " + getStatus() + ", Start time= " + getStartTime() + ", Duration= " + getDuration()
                + ", Epic ID=" + epicId + " }";
    }
}