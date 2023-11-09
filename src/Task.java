public class Task {
    private String name;
    private String description;
    private int id;
    private String status;
    private String type="Task";

    public Task(String name, String description, int id){
        this.name = name;
        this.description = description;
        this.id = id;
        this.status="NEW";
    }
    public Task(String name, String description, int id,String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status=status;
    }

    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString(){
        return type+"{ "+"Id= "+id+", Name= "+name+", Description= "+description+", Status= "+status+" }";
    }
}