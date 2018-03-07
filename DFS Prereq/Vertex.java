/**
 * Created by Ram on 12/1/17.
 */
public class Vertex {
    private int finish;
    private String [] adjList;
    private String color;
    private Vertex pred;
    private String ident;
    private int discovery;
    public Vertex(String identi, String [] lst){
        this.pred = null;
        this.color= "White";
        this.adjList=lst;
        this.ident=identi;
    }
    public String getColor(){
        return this.color;
    }
    public int getFinish(){
        return this.finish;
    }
    public String [] getAdj(){
        return adjList;
    }
    public Vertex getPred(){
        return pred;
    }
    public String getName(){
        return this.ident;
    }
    public void setFinish(int time){
        this.finish=time;
    }
    public void setColor(String colour){
        this.color=colour;
    }
    public void setPred(Vertex prede){
        this.pred=prede;
    }
    public int getDiscovery(){
        return this.discovery;
    }
    public void setDiscovery(int time){
        this.discovery=time;
    }
}
