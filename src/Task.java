public class Task {
    private String name;
    private String description;
    private int id;
    private String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return "TASK";
    }

    @Override
    public String toString() {
        return getType() + "{ " + "Id= " + getId() + ", Name= " + getName() + ", Description= "
                + getDescription() + ", Status= " + getStatus() + " }";
    }
}