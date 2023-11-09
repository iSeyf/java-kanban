import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class Epic extends Task{
    private String status;
    private String type="Epic";
    private HashMap<Integer,Subtask> subtaskList = new HashMap<>();

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public void addSubtask(int id,Subtask subtask){
        subtaskList.put(id, subtask);
    }

    public HashMap<Integer,Subtask> getSubtasks() {
        return subtaskList;
    }

    public void updateEpicStatus() {
        String newStatus="NEW";
        if(!subtaskList.isEmpty()){
            int quantityForDone=0;
            int quantityForNew=0;
            List<Integer>keys=new ArrayList<>(subtaskList.keySet());{
                for(Integer key:keys){
                    String status=subtaskList.get(key).getStatus();
                    if (status.equals("DONE")){
                    quantityForDone++;
                    } else if (status.equals("NEW")){
                    quantityForNew++;
                    }
                }
            }

            if(quantityForDone==subtaskList.size()){
                newStatus="DONE";
            }else if(quantityForNew==subtaskList.size()){
                newStatus="NEW";
            }else{
                newStatus="IN_PROGRESS";
            }
        }else{
            newStatus="NEW";
        }
        this.setStatus(newStatus);
    }

    @Override
    public String toString(){
        return type+ "{ "+"Id= "+getId()+", Name= "+getName()+", Description= "+getDescription()+", Status= "+getStatus()+" }";
    }
}