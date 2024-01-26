package tasks;

import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private int duration;
    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.status = Status.NEW;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return Type.TASK;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == 0) {
            return null;
        }
        return startTime.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return getType() + "{ " + "Id= " + getId() + ", Name= " + getName() + ", Description= "
                + getDescription() + ", Status= " + getStatus() + ", Start time= " + getStartTime() + ", Duration= "
                + getDuration() + " }";
    }
}