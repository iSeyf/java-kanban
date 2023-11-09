class Subtask extends Task{

    private int epicId;
    private String type="Subtask";
    private String status;

    public Subtask(String name, String description, int id, int epicId){
        super(name, description, id);
        this.epicId=epicId;
        this.status="NEW";
    }
    public Subtask(String name, String description, int id, int epicId,String status) {
        super(name, description, id,status);
        this.epicId = epicId;
        this.status=status;
    }
    public int getEpicId() {
        return epicId;
    }
    public String getStatus(){
        return status;
    }
    @Override
    public String toString(){
        return type+ "{ "+"Id= "+getId()+", Name= "+getName()+", Description= "+getDescription()+", Status= "+getStatus()
                +", Epic ID="+epicId+" }";
    }
}