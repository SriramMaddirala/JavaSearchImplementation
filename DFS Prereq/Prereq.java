import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ram on 12/1/17.
 */
public class Prereq {
    //initialize instance variables for graph and time
    public Vertex [] graph;
    public int time;

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
        //create instance of Prereq
        Prereq Classes = new Prereq();
        //call initVertex to initialize .graph of Classes
        Classes.graph=Classes.initVertex(readin);
        //call Depth First Search
        Classes.DFS();
        // calls FinishtimeSorted to output vertices sorted by finish time and store in sorted graph
        Vertex [] sortedGraph = Classes.FinishtimeSorted();
        int len = sortedGraph.length-1;
        try {
            PrintWriter writer = new PrintWriter("output.txt");
            //prints vertices by finish time with shortest finish time first until last element with space after
            while(len>0) {
                String toPrint = sortedGraph[len].getName() + " ";
                writer.print(toPrint);
                len--;
            }
            //ensures last element doesn't have trailing space
            String toPrint = sortedGraph[len].getName();
            writer.print(toPrint);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //gets index of vertex in graph for a given name
    public int getVertexIndex(String name){
        int glen =this.graph.length;
        int i =0;
        while(i<glen){
            if(this.graph[i].getName().equals(name)){
                return i;
            }
            i++;
        }
        return -1;
    }
    public void DFS(){
        // sets time
        this.time=0;
        int glen = this.graph.length;
        int i=0;
        //for each vertex in this.graph set color to white and call DFS-visit
        while(i<glen){
            if(this.graph[i].getColor()=="White"){
                DFSVisit(i);
            }
            i++;
        }
    }
    //initializes Vertices and stores them in a Array of vertices given an ArrayList of string that is parsed through
    public Vertex[] initVertex(ArrayList<String> lst){
        int len = lst.size();
        int i =0;
        Vertex[] graph = new Vertex[len];
        while(i<len){
            String[] splitString = lst.get(i).trim().split(":");
            String clas = splitString[0];
            if(splitString.length==2){
                String prereq=splitString[1];
                String[] splitPreq = prereq.split(" ");
                graph[i]= new Vertex(clas,splitPreq);
            }
            else{
                graph[i] = new Vertex(clas,null);
            }
            i++;
        }
        return graph;
    }
    public void DFSVisit(int index){
        //sets color as gray for given vertex
        this.graph[index].setColor("Gray");
        // increments time
        this.time = this.time +1;
        //set discovery time for the given index
        this.graph[index].setDiscovery(this.time);
        // if the vertex has adjacent list
        if(this.graph[index].getAdj()!=null){
            int adjlen = this.graph[index].getAdj().length;
            int i = 0;
            int[] adjVertIndex = new int[adjlen];
            while (i < adjlen) {
                adjVertIndex[i] = getVertexIndex(this.graph[index].getAdj()[i]);
                i++;
            }
            int j = 0;
            //explore each edge of Vertex
            while (j < adjlen) {
                //if color is white
                if (this.graph[adjVertIndex[j]].getColor() == "White") {
                    //set pred of j to u
                    this.graph[adjVertIndex[j]].setPred(this.graph[index]);
                    //recursive call
                    DFSVisit(adjVertIndex[j]);
                }
                j++;
            }
        }
        //change color to black
        this.graph[index].setColor("Black");
        // increment time
        this.time = this.time + 1;
        //set finishtime
        this.graph[index].setFinish(this.time);
    }
    //return a sorted list of vertices for finishtime
    public Vertex [] FinishtimeSorted(){
        int len =this.graph.length;
        int i =0;
        int [] finishTime= new int[len];
        while(i<len){
            finishTime[i]=this.graph[i].getFinish();
            i++;
        }
        i=0;
        int biggest =-1;
        int biggestIndex=-1;
        Vertex [] sorted= new Vertex[len];
        //finds biggest index and puts vertex that corresponds to that at top of new list then repeats process after setting biggest to -1
        while(i<len){
            int j =0;
            while(j<len){
                if(finishTime[j]>biggest){
                    biggest=finishTime[j];
                    biggestIndex=j;
                }
                j++;
            }
            sorted[i]=this.graph[biggestIndex];
            finishTime[biggestIndex]=-1;
            biggest=-1;
            i++;
        }
        return sorted;
    }
}