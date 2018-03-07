import java.io.*;
import java.util.*;

/**
 * Created by Ram on 12/3/17.
 */
public class Johnson {
    //initializes instance variables
    public Vertex[] graph;
    public int[][] weight;
    public int[][] weightNew;
    public Vertex[] graphNew;
    public boolean Negativenew;
    public boolean Negative;
    public boolean Empty;
    public static void main(String[] args) {
        String fileInput = args[0];
        String line;
        //create Arraylist to read in the input
        ArrayList<String> readin = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    //add to arraylist
                    readin.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        Johnson shortst = new Johnson();
        //run johnsons on shortst
        //initializes boolean instance variables
        shortst.Negativenew=false;
        shortst.Empty=false;
        shortst.Negative=false;
        //call initVertex to initialize instance variables of shortst
        shortst.initVertexManage(readin);
        //if it's not negative cycle then run johnson's
        if(shortst.Negative==false) {
            shortst.JohnsonAlg();
        }
        //if it's not negative cycle and not empty run New Johnsons
        if((shortst.Negativenew==false)&&(shortst.Empty==false)){
            shortst.JohnsonNew();
        }
        try {
            PrintWriter writer = new PrintWriter("output.txt");
            int i = 0;
            int j = 0;
            if(shortst.Negative){
                writer.println("Negative cycle");
            }
            else {
                //print out initial matrix
                while (i < shortst.weight.length) {
                    j = 0;
                    while (j < shortst.weight[0].length) {
                        if (j == (shortst.weight[0].length - 1)) {
                            writer.print(shortst.weight[i][j] + "\n");
                        } else {
                            writer.print(shortst.weight[i][j] + " ");
                        }
                        j++;
                    }
                    i++;
                }
            }
            if(shortst.Empty){
                writer.close();
                System.exit(0);
            }
            if(shortst.Negativenew) {
                writer.println("Negative cycle");
                writer.close();
                System.exit(0);
            }
            i = 0;
            j = 0;
            //print combined matrix
            while (i < shortst.weightNew.length) {
                j = 0;
                while (j < shortst.weightNew[0].length) {
                    if (j == (shortst.weightNew[0].length - 1)) {
                        writer.print(shortst.weightNew[i][j] + "\n");
                    } else {
                        writer.print(shortst.weightNew[i][j] + " ");
                    }
                    j++;
                }
                i++;
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //get this.graph and this.weight after parsing
    public void initVertex(ArrayList<String> lst) {
        int len = lst.size();
        int i = 0;
        this.graph = new Vertex[len];
        while (i < len) {
            String[] splitString = lst.get(i).trim().split(" ");
            int j = 0;
            if (i == 0) {
                this.weight = new int[len][splitString.length];
            }
            while (j < splitString.length) {
                if((Integer.parseInt(splitString[j]))<0){
                    this.Negative=true;
                }
                this.weight[i][j] = Integer.parseInt(splitString[j]);
                this.weightNew[i][j]=this.weight[i][j];
                j++;
            }
            this.graph[i] = new Vertex(Integer.toString((i)));
            this.graphNew[i]= this.graph[i];
            i++;
        }
    }
    //manages parsing for initVertex 
    public void initVertexManage(ArrayList<String> lst){
        int i =0;
        int len =lst.size();
        String[] splitString = lst.get(i).trim().split(" ");
        int initMatLength=splitString.length;
        ArrayList<String> fstMat = new ArrayList<String>();
        ArrayList<String> SndMat = new ArrayList<String>();
        while(i<initMatLength){
            fstMat.add(lst.get(i));
            i++;
        }
        if(i==len){
            this.Empty=true;
            this.weightNew = new int[i][i];
            this.graphNew = new Vertex[i];
            initVertex(fstMat);
        }
        else {
            while (i < len) {
                SndMat.add(lst.get(i));
                i++;
            }
            this.weightNew = new int[i][i];
            this.graphNew = new Vertex[i];
            initVertex(fstMat);
            initVertexSnd(SndMat);
            initTranspose();
        }
    }
    //get this.graphNew and this.weightNew after parsing
    public void initVertexSnd(ArrayList<String> lst){
        int len = lst.size();
        int i = 0;
        int donelen = this.graph.length;
        while (i < len) {
            String[] splitString = lst.get(i).trim().split(" ");
            int j = 0;
            while (j < splitString.length) {
                if((Integer.parseInt(splitString[j]))<0){
                    this.Negativenew=true;
                }
                this.weightNew[i+donelen][j] = Integer.parseInt(splitString[j]);
                j++;
            }
            this.graphNew[i+donelen] = new Vertex(Integer.toString((i+donelen)));
            i++;
        }
    }
    //calculates transpose to make combined matrix
    public void initTranspose(){
        int i =0;
        int j;
        while(i<this.weightNew.length){
            j=this.weight.length;
            while(j<this.weightNew.length){
                this.weightNew[i][j]= this.weightNew[j][i];
                j++;
            }
            i++;
        }
    }
    //initializes upperbound for a given Vertex if called with the string name
    public void InitSingSour(String s) {
        int len = this.graph.length;
        int i = 0;
        while (i < len) {
            if (this.graph[i].getName().equals(s)) {
                this.graph[i].setUpperbound(0);
            } else {
                this.graph[i].setUpperbound(2000000);
            }
            i++;
        }
    }
    //initializes upperbound for a given Vertex if called with the string name for graphnew
    public void InitSingSourNew(String s) {
        int len = this.graphNew.length;
        int i = 0;
        while (i < len) {
            if (this.graphNew[i].getName().equals(s)) {
                this.graphNew[i].setUpperbound(0);
            } else {
                this.graphNew[i].setUpperbound(2000000);
            }
            i++;
        }
    }
    //tightens upperbounds and sets predeccesor given vertices u, v for new
    public void RelaxNew(Vertex u, Vertex v) {
        if (v.getUpperbound() > (u.getUpperbound() + this.weightNew[getMatrixindexNew(u)][getMatrixindexNew(v)])) {
            if(u.getUpperbound()!=2000000) {
                v.setUpperbound((u.getUpperbound() + this.weightNew[getMatrixindexNew(u)][getMatrixindexNew(v)]));
                v.setPred(u);
            }
        }
    }
    //tightens upperbounds and sets predeccesor given vertices u, v
    public void Relax(Vertex u, Vertex v) {
        if (v.getUpperbound() > (u.getUpperbound() + this.weight[getMatrixindex(u)][getMatrixindex(v)])) {
            if(u.getUpperbound()!=2000000) {
                v.setUpperbound((u.getUpperbound() + this.weight[getMatrixindex(u)][getMatrixindex(v)]));
                v.setPred(u);
            }
        }
    }
    //gets index of given vertex
    public int getMatrixindex(Vertex u) {
        int i = 0;
        int len = graph.length;
        while (i < len) {
            if (!(this.graph[i].getName().equals(u.getName()))) {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }
    //gets index of given vertex for new
    public int getMatrixindexNew(Vertex u) {
        int i = 0;
        int len = graphNew.length;
        while (i < len) {
            if (!(this.graphNew[i].getName().equals(u.getName()))) {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }
    public Set<Vertex> DijkstraAlg(String s) {
        //initializes initial source
        InitSingSour(s);
        Set<Vertex> S = new HashSet<>();
        //initializes new comparator to order priority queue
        Comparator<Vertex> VertComp = new VertexComparator();
        //initializes priority queue
        PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>(this.graph.length, VertComp);
        int i = 0;
        while (i < this.graph.length) {
            //adds vertices to priority queue
            Q.add(this.graph[i]);
            i++;
        }
        while (!(Q.isEmpty())) {
            //removes top u
            Vertex u = Q.poll();
            //adds it to S
            S.add(u);
            i = 0;
            int len = this.graph.length;
            while (i < len) {
                Vertex v = this.graph[i];
                //calls relax with every vertex in graph and u
                Relax(u, v);
                i++;
            }
        }
        //returns Set
        return S;
    }
    //runs Dijkstra for new
    public Set<Vertex> DijkstraAlgNew(String s) {
        InitSingSourNew(s);
        Set<Vertex> S = new HashSet<>();
        Comparator<Vertex> VertComp = new VertexComparator();
        PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>(this.graphNew.length, VertComp);
        int i = 0;
        while (i < this.graphNew.length) {
            Q.add(this.graphNew[i]);
            i++;
        }
        while (!(Q.isEmpty())) {
            Vertex u = Q.poll();
            S.add(u);
            i = 0;
            int len = this.graphNew.length;
            while (i < len) {
                Vertex v = this.graphNew[i];
                RelaxNew(u, v);
                i++;
            }
        }
        return S;
    }
    //Johnson Alg without the checking for negative as undirected graph
    public void JohnsonAlg() {
        int[][] matList = new int[this.graph.length][this.graph.length];
        int u = 0;
        while (u < this.graph.length) {
            Set<Vertex> donerow = DijkstraAlg(Integer.toString(u));
            int i1 = 0;
            while (i1 < donerow.size()) {
                matList[u][i1] = finishfromSet(donerow, Integer.toString(i1)).getUpperbound();
                i1++;
            }
            int v = 0;
            while (v < this.graph.length) {
                this.weight[u][v] = matList[u][v];
                v++;
            }
            u++;
        }
    }
    //Johnson Alg for new without the checking for negative as undirected graph but only runs for only the new vertices
    public void JohnsonNew(){
        int i=0;
        int j;
        while(i<this.weight.length){
            j=0;
            while (j<this.weight.length){
                this.weightNew[i][j]=this.weight[i][j];
                j++;
            }
            i++;
        }
        i=0;
        while(i<this.graph.length){
            this.graphNew[i]=this.graph[i];
            i++;
        }
        int[][] matList = new int[this.graphNew.length][this.graphNew.length];
        int u = this.graph.length;
        while (u < this.graphNew.length) {
            Set<Vertex> donerow = DijkstraAlgNew(Integer.toString(u));
            int i1 = 0;
            while (i1 < donerow.size()) {
                matList[u][i1] = finishfromSet(donerow, Integer.toString(i1)).getUpperbound();
                i1++;
            }
            int v = 0;
            while (v < this.graphNew.length) {
                this.weightNew[u][v] = matList[u][v];
                v++;
            }
            u++;
        }
        //transpose again
        initTranspose();
        //check if A->D->B is faster than A->B and update accordingly for given vertices
        optimizeEverything();
    }
    public Vertex finishfromSet(Set<Vertex> finished, String u) {
        for (Vertex s : finished) {
            if (s.getName().equals(u)) {
                return s;
            }
        }
        return null;
    }
    //check if shorter path exists and update
    public void optimizeJohnson(int rowindex,int colindex){
        int rowlen = this.weightNew.length;
        int collen = rowlen;
        int row =this.weight.length;
        while(row<rowlen){
            int col=0;
            while(col<collen){
                if(this.weightNew[rowindex][colindex]> this.weightNew[rowindex][row] + this.weightNew[row][colindex]){
                    this.weightNew[rowindex][colindex] = this.weightNew[rowindex][row] + this.weightNew[row][colindex];
                    this.weightNew[rowindex][colindex]= this.weightNew[rowindex][row] + this.weightNew[row][colindex];
                }
                col++;
            }
            row++;
        }
    } 
    public void optimizeEverything(){
        int row =0;
        int col;
        int rowlen = this.weight.length;
        while(row<rowlen){
            col =0;
            while (col<rowlen){
                optimizeJohnson(row,col);
                col++;
            }
            row++;
        }
    }
}