/*
LCS_Holder object in order to have a more modular design
This object will hold the data and organize for use in the main routine

This also contains all the methods required for output
 */


import java.io.BufferedWriter;
import java.io.IOException;


public class LCS_Holder
{
    int matches[][];
    String path[][];
    public LCS_Holder(int matches[][], String path[][]){
        this.matches = matches;
        this.path = path;
    }
    
    
    public void printMatches(BufferedWriter bufferedWriter){
        /*
        Prints the matches matrix
        */
        for (int i = 0; i < matches.length; i++){
            for (int j = 0; j < matches[i].length;j++){
                print(matches[i][j] + "\t", bufferedWriter);
            }
            println("",bufferedWriter);
        }
        println("",bufferedWriter);
    }
    
    public void printPath(BufferedWriter bufferedWriter){
        /*
        Prints the path matrix
        */
        for (int i = 0; i < path.length; i++){
            for (int j = 0; j < path[i].length;j++){
                print(path[i][j] + "\t", bufferedWriter);
            }
            println("",bufferedWriter);
        }
    }
    
    public void printLCS(int mode, String x, int i, int j, BufferedWriter bufferedWriter){
        /*
        Prints to console and output the longest common substring
        Takes a mode, to indicate which string it's showing
        A String x, to follow through to the end
        Int i and j, which represent the size of the matrix
        And a bufferedwriter to write to output
        
        Prints out the longest common substring through recursive calls
        
        ENHANCEMENT
        In addition to printing out the longest common substring through
        recursive calls, this function can also be used
        to show the gaps between the LCS, and the original string
        
        For example,
        ATTCG
        ATCG
        has an LCS of,
        ATCG
        but output will be shown as
        ATTCG
        A-TCG
        */
        
        //if at j is not 0, and on the second substring, then add a - for gap
        if (i == 0 && j > 0){
            printLCS(mode, x, i, j-1, bufferedWriter);
            if (mode == 2){
                print("-",bufferedWriter);
            }
        }
        // if i is not 0, and on first substring, then add a - for gap
        else if (i > 0 && j == 0){
            printLCS(mode, x, i-1, j, bufferedWriter);
            if (mode == 1){
                print("-",bufferedWriter);
            }
        }
        //if at the corner, stop
        else if (i == 0 && j == 0){
            println("",bufferedWriter);
            return;
        }
        //follows the path, from bottom right, to top left
        if ("DIA".equals(this.path[i][j])){
            printLCS(mode, x, i-1, j-1, bufferedWriter);
            print(String.valueOf(x.charAt(i-1)), bufferedWriter);
        }
        
        if ("TOP".equals(this.path[i][j])){
            printLCS(mode, x, i-1, j, bufferedWriter);
            if (mode==1){
                print("-",bufferedWriter);
            }
        }
        
        if ("LFT".equals(this.path[i][j])){
            printLCS(mode, x, i, j-1, bufferedWriter);
            if (mode==2){
                print("-",bufferedWriter);
            }
        }
    }
    
    /*
    The following are print functions written to handle both
    printing to console and printing to output at the same time
    for convenience purposes
    */
    
    public void print(String text, BufferedWriter bufferedWriter){
        System.out.print(text);
        try {
            bufferedWriter.write(text);
        } catch (IOException ex) {
            System.out.println("IOException with output file");
        }
    }
    
    public void println(String text, BufferedWriter bufferedWriter){
        System.out.println(text);
        try {
            bufferedWriter.write(text + "\n");
        } catch (IOException ex) {
            System.out.println("IOException with output file");
        }
    }
}
