package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;
    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtaskList;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtaskList.isEmpty()) {
            return null;
        }
        LocalDateTime earliestStartTime = null;
        ;
        for (Subtask subtask : subtaskList) {
            if (subtask.getStartTime() != null) {
                if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                    earliestStartTime = subtask.getStartTime();
                }
            }
        }
        return earliestStartTime;
    }

    @Override
    public Duration getDuration() {
        if (subtaskList.isEmpty()) {
            return null;
        }
        Duration epicDuration = Duration.ofMinutes(0);
        for (int i = 0; i < subtaskList.size(); i++) {
            Duration subtaskDuration = subtaskList.get(i).getDuration();
            if (subtaskDuration != null) {
                epicDuration = epicDuration.plus(subtaskDuration);
            }
        }
        return epicDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtaskList.isEmpty()) {
            return null;
        }
        LocalDateTime latestEndTime = null;
        for (Subtask subtask : subtaskList) {
            if (subtask.getEndTime() != null) {
                if (latestEndTime == null || subtask.getEndTime().isAfter(latestEndTime)) {
                    latestEndTime = subtask.getEndTime();
                }
            }
        }
        return latestEndTime;
    }
}