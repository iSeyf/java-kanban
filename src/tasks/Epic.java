package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Integer> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String getType() {
        return "EPIC";
    }

    public void addSubtaskId(int id) {
        subtaskList.add(id);
    }

    public List<Integer> getSubtasks() {
        return subtaskList;
    }
}