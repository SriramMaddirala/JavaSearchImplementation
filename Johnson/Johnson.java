import java.io.*;
import java.util.*;

/**
 * Created by Ram on 12/3/17.
 */
public class Johnson {
    //initializes instance variables
    public Vertex[] graph;
    public int[][] weight;
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
        //call initVertex to initialize instance variables of shortst
        shortst.initVertex(readin);
        //run johnsons on shortst
        shortst.JohnsonAlg();
        try {
            PrintWriter writer = new PrintWriter("output.txt");
            int i = 0;
            int j = 0;
            //print out matrix
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
                this.weight[i][j] = Integer.parseInt(splitString[j]);
                j++;
            }
            this.graph[i] = new Vertex(Integer.toString((i)));
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
    //tightens upperbounds and sets predeccesor given vertices u, v
    public void Relax(Vertex u, Vertex v) {
        if (v.getUpperbound() > (u.getUpperbound() + this.weight[getMatrixindex(u)][getMatrixindex(v)])) {
            //ensures boolean logic holds
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
        //returns set
        return S;
    }
    //checks for a negative cycle given string
    public boolean BellmanFord(String s) {
        InitSingSour(s);
        int i = 0;
        int row;
        int col;
        while (i < this.graph.length) {
            row = 0;
            col = 0;
            while (row < this.weight.length) {
                col = 0;
                while (col < this.weight[0].length) {
                    Relax(this.graph[row], this.graph[col]);
                    col++;
                }
                row++;
            }
            i++;
        }
        row = 0;
        col = 0;
        while (row < this.weight.length) {
            col = 0;
            while (col < this.weight[0].length) {
                if (this.graph[col].getUpperbound() > (this.graph[row].getUpperbound() + this.weight[row][col])) {
                    return false;
                }
                col++;
            }
            row++;
        }
        return true;
    }
    // adds supervertex, runs bellman ford with that and tests if negative cycle then does weighting, reweighting etc.
    public void JohnsonAlg() {
        //essentially adds supervertex
        Vertex[] graphp = new Vertex[this.graph.length + 1];
        int i = 0;
        while (i < this.graph.length) {
            graphp[i] = this.graph[i];
            i++;
        }
        String s = Integer.toString(i);
        graphp[i] = new Vertex(s);
        int[][] matrix = new int[this.graph.length + 1][this.graph.length + 1];
        int row = 0;
        int col;
        while (row < this.weight.length) {
            col = 0;
            while (col < this.weight[0].length) {
                matrix[row][col] = this.weight[row][col];
                col++;
            }
            matrix[row][col] = 2000000;
            row++;
        }
        col = 0;
        while (col < graphp.length) {
            matrix[row][col] = 0;
            col++;
        }
        this.graph = graphp;
        this.weight = matrix;
        //if negative cycle then print that and close writer and exit
        if (BellmanFord(s) == false) {
            try {
                PrintWriter writer = new PrintWriter("output.txt");
                writer.println("Negative cycle");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0);
        } else {
            // for each vertex v set upperbound to value computed by bellman -ford 
            int j = 0;
            while (j < this.graph.length) {
                this.graph[j].setUpperbound(this.weight[this.graph.length - 1][j]);
                j++;
            }
            int matrow = 0;
            //do reweighting
            while (matrow < this.weight.length) {
                int matcol = 0;
                while (matcol < this.weight[0].length) {
                    this.weight[matrow][matcol] = this.weight[matrow][matcol] + this.graph[matrow].getUpperbound() - this.graph[matcol].getUpperbound();
                    matcol++;
                }
                matrow++;
            }
            //take out supervertex
            int[][] Gmatrix = new int[this.graph.length - 1][this.graph.length - 1];
            Vertex[] Ggraph = new Vertex[this.graph.length - 1];
            int rowchange = 0;
            while (rowchange < this.weight.length - 1) {
                int colchange = 0;
                while (colchange < this.weight[0].length - 1) {
                    Gmatrix[rowchange][colchange] = this.weight[rowchange][colchange];
                    colchange++;
                }
                Ggraph[rowchange] = this.graph[rowchange];
                rowchange++;
            }
            this.graph = Ggraph;
            this.weight = Gmatrix;
            int[][] matList = new int[this.graph.length][this.graph.length];
            int u = 0;
            while (u < this.graph.length) {
                //run dijkstra for every Vertex and return that row
                Set<Vertex> donerow = DijkstraAlg(Integer.toString(u));
                int i1 = 0;
                while (i1 < donerow.size()) {
                    matList[u][i1] = finishfromSet(donerow, Integer.toString(i1)).getUpperbound();
                    i1++;
                }
                int v = 0;
                while (v < this.graph.length) {
                    //make this.weight equal to matlist
                    this.weight[u][v] = matList[u][v];
                    v++;
                }
                u++;
            }
        }
    }
    public Vertex finishfromSet(Set<Vertex> finished, String u) {
        for (Vertex s : finished) {
            if (s.getName().equals(u)) {
                return s;
            }
        }
        return null;
    }
}