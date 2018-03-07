/**
 * Created by Ram on 12/1/17.
 */
public class Vertex {
    private Vertex pred;
    private String ident;
    private int upperbound;
    public Vertex(String identi){
        this.pred = null;
        this.ident=identi;
    }
    public Vertex getPred(){
        return pred;
    }
    public String getName(){
        return this.ident;
    }

    public void setPred(Vertex prede){
        this.pred=prede;
    }
    public int getUpperbound() {
        return this.upperbound;
    }

    public void setUpperbound(int upper) {
        this.upperbound = upper;
    }

}
