import java.io.*;
import java.util.Arrays;

/**
 * Created by Ram on 11/30/17.
 */
public class finiteAutomata {
    //main function takes in arguments and reads in first line into pattern String and second line into toMatch
    public static void main(String [] args){
        String fileInput = args[0];
        String line;
        String toMatch="";
        String pattern="";
        try {
            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            try{
                int i =0;
                while(((line=bufferedReader.readLine())!=null)&&(i<=1)){
                    if(i==0){
                        pattern=line;
                    }
                    if(i==1){
                        toMatch= line;
                    }
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //if no pattern just exit
        if(pattern.equals("")){
            System.exit(1);
        }
        //instantiate finite Automata
        finiteAutomata stringMatch = new finiteAutomata();
        //get unique characters
        String unique = stringMatch.giveStringUnique(toMatch);
        //computes Transition function for given pattern and unique charcters in String
        int [] []  tf = stringMatch.computeTransFunc(pattern,unique);
        //set m as pattern length
        int m = pattern.length();
        // get the int array of match indices from the finitematcher
        int [] indices= stringMatch.finiteMatcher(toMatch,tf,m,unique);
        // reverses pattern
        String revp = new StringBuilder(pattern).reverse().toString();
        //computes trans func for reversed pattern
        int [] []  tfrev = stringMatch.computeTransFunc(revp,unique);
        // computes match indices from finitematcher
        int [] indicesRev = stringMatch.finiteMatcher(toMatch,tfrev,m,unique);
        // merges the int arrays and sorts it
        int [] mergedSorted = stringMatch.mergeandSort(indices,indicesRev);
        int j =0;
        String str ="";
        // formats the ints for printing; stores in a string that is to be printed
        while(j<mergedSorted.length){
            //makes sure there is no extra space
            if(j==0){
                str = String.valueOf(mergedSorted[j]);
            }
            else{
                str = str + " " + String.valueOf(mergedSorted[j]);
            }
            j++;
        }
        try {
            PrintWriter writer = new PrintWriter("output.txt");
            //prints the string
            writer.println(str);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public  int [] finiteMatcher (String T, int[] [] TF, int m, String unique){
        //set n to pattern length
        int n= T.length();
        int q =0;
        int i =0;
        //initialize int array
        int [] begin = new int [n];
        int count =0;
        //iterate over string
        while(i<n){
            int x =0;
            int index =0;
            //checks every character in stringand gets index of match in unique
            while(x<unique.length()) {
                if(T.charAt(i)==unique.charAt(x)){
                    index =x;
                    x = unique.length()+1;
                }
                x= x+1;
            }
            //sets q to TF[q] of the index matched in unique
            q = TF[q][index];
            //if state is finished then add the index to count and reset q
            if (q == m) {
                begin[count] = (i - m + 1);
                count++;
                q=TF[q][index];
            }
            i=i+1;
        }
        i=0;
        int [] finalresult = new int[count];
        while(i<count){
            finalresult[i]=begin[i];
            i++;
        }
        //return final result as int array
        return finalresult;
    }
    public int [] mergeandSort(int [] list1, int [] list2){
        //merges the list of indices; sorts it and returns
        int l1=list1.length;
        int l2 = list2.length;
        int i =0;
        int [] finalresult = new int[(l1+l2)];
        while(i<l1){
            finalresult[i]=list1[i];
            i++;
        }
        int j=0;
        while(j<l2){
            finalresult[i+j] = list2[j];
            j++;
        }
        Arrays.sort(finalresult);
        return finalresult;
    }
    public  int[][] computeTransFunc(String p, String sig){
        int m = p.length();
        int siglen = sig.length();
        int q =0;
        int k;
        int [] [] TF= new int[m+1][siglen];
        //while current state q is not finished
        while(q<=m){
            int i =0;
            while(i<siglen){
                //set k to min of m and q+1
                if((m)>(q+1)){
                    k=(q+1);
                }
                else if((m)<(q+1)){
                    k=(m);
                }
                else {
                    k = (m);
                }
                // if Pk is suffix of Pqa then decrement k until you get suffix
                while(!(computeifSuffix(p.substring(0,k),(p.substring(0,q) + sig.charAt(i))))){
                    k=k-1;
                }
                //TF of the state and character index is updated to k
                TF[q][i]=k;
                i=i+1;
            }
            q=q+1;
        }
        return TF;
    }
    //checks if it's a suffix or not
    public  boolean computeifSuffix(String p, String q){
        return (q.endsWith(p));
    }
    //returns all the unique characters in String
    public String giveStringUnique(String sig){
        char [] unique = new char [sig.length()];
        int i =0;
        int j =0;
        while(i<sig.length()){
            //if character isn't in list of unique characters add it
            if(!(checkChar(sig.charAt(i),unique))){
                unique[j]=sig.charAt(i);
                j++;
            }
            //otherwise do nothing
            i++;
        }
        i=0;
        //get an array that has length equivalent to the number of unique characers
        char [] uniquefinal = new char [j];
        while(i<j){
            uniquefinal[i]=unique[i];
            i++;
        }
        //return string of character array
        String str = new String(uniquefinal);
        return str;
    }
    //just check if given character is in given list of characters
    public boolean checkChar(char a, char [] lst){
        int i =0;
        while(i<lst.length){
            if(lst[i]==a){
                return true;
            }
            i++;
        }
        return false;
    }
}