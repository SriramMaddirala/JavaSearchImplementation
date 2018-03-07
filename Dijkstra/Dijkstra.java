import java.io.*;
import java.util.*;

/**
 * Created by Ram on 12/1/17.
 */
public class Dijkstra {
    //initializes instance variables to track source, graph and weight
    public Vertex [] graph;
    public int [] [] weight;
    public String source;
    public String dest;
    public String path;
    public static void main(String[] args) {
        String fileInput = args[0];
        String line;
        //create Arraylist to read in the input
        ArrayList<String> readin= new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            try{
                while((line=bufferedReader.readLine())!=null){
                    //add to arraylist
                    readin.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Dijkstra shortst = new Dijkstra();
        //call initVertex to initialize instance variables of Classes
        shortst.initVertex(readin);
        //stores set of finished Vertices in finishSet
        Set<Vertex> finishSet = shortst.DijkstraAlg();
        //gets destination vertex
        Vertex finished = shortst.finishfromSet(finishSet);
        //sets path value
        shortst.getPath(finished);
        String path = shortst.path;
        int time =finished.getUpperbound();
        //sets toPrint to the time: path
        String toPrint = time + ": "+ path;
        try {
            PrintWriter writer = new PrintWriter("output.txt");
            //prints path
            writer.println(toPrint);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //initializes Vertices and stores them in a Array of vertices given an ArrayList of string that is parsed through
    public void initVertex(ArrayList<String> lst){
        int len = lst.size();
        int i =0;
        this.graph = new Vertex[(len-1)];
        while(i<len){
            if(i!=0){
                String[] splitString = lst.get(i).trim().split(" ");
                int j=0;
                if(i==1){
                    this.weight = new int[(len - 1)][splitString.length];
                }
                while(j<splitString.length){
                    this.weight[i-1][j]= Integer.parseInt(splitString[j]);
                    j++;
                }
                this.graph[i-1]= new Vertex(Integer.toString((i-1)));
            }
            else{
                String[] splitString = lst.get(i).trim().split(" ");
                this.source = splitString[0];
                this.dest=splitString[1];
            }
            i++;
        }
    }
    //initializes upperbound for a given Vertex if called with the string name
    public void InitSingSour(String s){
        int len = this.graph.length;
        int i =0;
        while(i < len){
            if(this.graph[i].getName().equals(s)) {
                this.graph[i].setUpperbound(0);
            }
            else{
                this.graph[i].setUpperbound(2000000);
            }
            i++;
        }
    }
    //tightens upperbounds and sets predeccesor given vertices u, v
    public void Relax( Vertex u, Vertex v){
        if(v.getUpperbound()> (u.getUpperbound() + this.weight[getMatrixindex(u)][getMatrixindex(v)])){
            v.setUpperbound((u.getUpperbound() + this.weight[getMatrixindex(u)][getMatrixindex(v)]));
            v.setPred(u);
        }
    }
    //gets index of given vertex
    public int getMatrixindex(Vertex u){
        int i =0;
        int len = graph.length;
        while(i<len){
            if(!(this.graph[i].getName().equals(u.getName()))){
                i++;
            }
            else{
                return i;
            }
        }
        return -1;
    }
    public Set<Vertex> DijkstraAlg(){
        //initializes initial source
        InitSingSour(this.source);
        // declares new Set
        Set<Vertex> S= new HashSet<>();
        //initializes new comparator to order priority queue
        Comparator<Vertex> VertComp=new VertexComparator();
        //initializes priority queue
        PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>(this.graph.length,VertComp);
        int i =0;
        while(i<this.graph.length){
            //adds vertices to priority queue
            Q.add(this.graph[i]);
            i++;
        }
        while(!(Q.isEmpty())){
            //removes top u
            Vertex u = Q.poll();
            //adds it to S
            S.add(u);
            i =0;
            int len = this.graph.length;
            while(i<len){
                Vertex v= this.graph[i];
                //calls relax with every vertex in graph and u
                Relax(u,v);
                i++;
            }
        }
        //returns set
        return S;
    }
    //gets Path in a string given a vertex
    public void getPath(Vertex done){
        if(done.getName().equals(this.dest)) {
            this.path = this.dest;
        }
        if(!(done.getPred().getName().equals(this.source))){
            this.path= done.getPred().getName() + " " + this.path;
            getPath(done.getPred());
        }
        if(done.getPred().getName().equals(this.source)){
            this.path= done.getPred().getName() + " " + this.path;
        }
    }
    //gets destination vertex
    public Vertex finishfromSet(Set<Vertex> finished){
        for(Vertex s: finished){
            if (s.getName().equals(this.dest)){
                return s;
            }
        }
        return null;
    }
}
