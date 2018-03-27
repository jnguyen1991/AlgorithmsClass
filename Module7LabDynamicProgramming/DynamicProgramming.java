/*

 */

import java.util.*;
import java.io.*;

public class DynamicProgramming {
    /*
    Main Driver
    */
    public static void main(String[] args) {
        String fileInput;
        String fileOutput;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        //matrices to hold path and matches for DP
        int[][] matches;
        String[][] path;
        //longest common substring object
        LCS_Holder lcs;
        //holders for the strings for pairwise testing
        String s1, s2;
        //holder for all strings from input file
        ArrayList<String> seq;
        //code used to determine matching behavior
        String tempIupac;
        IUPAC iupac;
        //check for three arguments
        if (args.length == 3){
                fileInput = args[0];
                fileOutput = args[1];
                tempIupac = args[2].toUpperCase();
            }
            else
            //if only two, default to ACGT only for IUPAC matching
            if (args.length == 2){
                Scanner scan = new Scanner(System.in);
                String in;
                System.out.println("Defaulting to ACGT only for matches");
                fileInput = args[0];
                fileOutput = args[1];
                tempIupac = "AGCT";
            }
            //otherwise just ask user for input
            else{
                Scanner scan = new Scanner(System.in);
                String in;
                System.out.println("Enter an input file:");
                in = scan.next();
                fileInput = in;
                System.out.println("Enter an output file:");
                in = scan.next();
                fileOutput = in;
                System.out.println("Enter a string of valid IUPAC codes:");
                in = scan.next();
                tempIupac = in.toUpperCase();
            }
        
        //constructing IUPAC, and reading file input
        iupac = new IUPAC(tempIupac);
        seq = readFile(fileInput);
        
        //main portion
        try{
            fileWriter = new FileWriter(fileOutput);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < seq.size(); i++){
                for (int j = i+1; j < seq.size(); j++){
                    if (true){
                        //force uppercase, and feed into s1 and s2
                        s1 = seq.get(i).toUpperCase();
                        s2 = seq.get(j).toUpperCase();
                        
                        //set path and matches object
                        path = new String[s1.length()+1][s2.length()+1];
                        matches = new int[s1.length()+1][s2.length()+1];
                        //feed both matrices in LCS holder for display
                        lcs = new LCS_Holder(matches, path);
                        

                        lcs.println("Comparing:\n" + s1 
                                + "\n" + s2, bufferedWriter);
                        lcs.println("IUPAC code used is: " 
                                + iupac.choices + "\n", bufferedWriter);
                        
                        //work being done by align function
                        align(lcs, iupac, s1, s2);
                        //output
                        lcs.println("Matches matrix:", bufferedWriter);
                        lcs.printMatches(bufferedWriter);
                        lcs.println("Path matrix:", bufferedWriter);
                        lcs.printPath(bufferedWriter);
                        
                        lcs.println("\nFirst string:", bufferedWriter);
                        lcs.print(s1, bufferedWriter);
                        lcs.printLCS(1, s1, s1.length(), s2.length(),bufferedWriter);
                        lcs.println("\nSecond string:", bufferedWriter);
                        lcs.print(s2, bufferedWriter);
                        lcs.printLCS(2, s1, s1.length(), s2.length(),bufferedWriter);
                        lcs.print("\nNo gaps:", bufferedWriter);
                        lcs.printLCS(3, s1, s1.length(), s2.length(),bufferedWriter);
                        
                        lcs.println("",bufferedWriter);
                        lcs.println("",bufferedWriter);
                        
                    }
                }
            }
        }
        catch (IOException ex){
            System.out.println("IOException, issue with output file");
        }
    }       
    
    public static ArrayList<String> readFile(String filePath){
        /*
        Returns an ArrayList of Strings, that will represent all sequences given
        Takes a filePath in the form of a string
        
        filePath should contain data in the form of 
        "S1 = [SEQUENCE]"
        And have one sequence per line
        
        */
        ArrayList<String> holder = new ArrayList<>();
        String line = "";
        String temp = "";
        boolean invalid = false;
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine())!= null){
                //taking everything after = sign
                if (line.trim().isEmpty()){
                    System.out.println("Empty line found, skipping");
                    continue;
                }
                temp = line;
                if (!(temp.contains("="))){
                    System.out.println("No = sign found, skipping: " 
                    + line);
                    continue;
                }
                temp = line.substring(line.indexOf("=")+1).trim();
                //check for empty string
                if (temp.trim().isEmpty()){
                    System.out.println("Empty string found, skipping: "
                    + line);
                    continue;
                }
                //check for valid characters, ATCG
                invalid = false;
                for (int i = 0; i<temp.length();i++){
                    if ("ATCGactg".indexOf(temp.charAt(i)) == -1){
                        System.out.println("Invalid char: " + temp.charAt(i)
                            + " found, skipping: " + line);
                        invalid = true;
                        break;
                    };
                }
                if (invalid){
                    continue;
                }
                holder.add(temp);
            }
            bufferedReader.close();
            
        }
        catch(FileNotFoundException ex){
            System.out.println("File not found exception. "
                    + "Check if file exists, or if name is correct");
            }
        catch(IOException ex){
            System.out.println("IO Exception found");
            }
        
        return holder;
    }
    
    public static void align(LCS_Holder lcs, IUPAC choices, String x, String y){
        /*
        Does not return anything
        Takes an LCS_Holder object, lcs, that holds the matrices for the end
        result
        Take an IUPAC object, choices, that contains information on how to match
        sequences correctly
        Takes a String x, for one sequence
        Takes a String y, to compare against x
        
        The lcs holds empty matrics of size x by y, that will hold both
        the number of matches and the path taken for the matches
        The end of this routine will fill both matrices up.
        */
        
        //setting top row, and first column
        for (int i = 0; i <= x.length(); i++) lcs.matches[i][0] = 0;
        for (int i = 0; i <= y.length(); i++) lcs.matches[0][i] = 0;
        //Sequence header information that can be stored in the matrix
        for (int i = 1; i <= y.length(); i++) lcs.path[0][i] = 
                String.valueOf(y.charAt(i-1));
        for (int i = 1; i <= x.length(); i++) lcs.path[i][0] = 
                String.valueOf(x.charAt(i-1));
        //corner
        lcs.path[0][0] = "0";
        
        //dynamic programming portion
        //based off of CLRS 15.4
        for (int i=1; i<=x.length(); i++) {
            for (int j=1; j<=y.length(); j++) {
                if (choices.matches(x.charAt(i-1), y.charAt(j-1))){
                    lcs.matches[i][j] = lcs.matches[i-1][j-1] + 1;
                    lcs.path[i][j] = "DIA";
                }
                else if (lcs.matches[i-1][j] >= lcs.matches[i][j-1]){
                    lcs.matches[i][j] = lcs.matches[i-1][j];
                    lcs.path[i][j] = "TOP";
                }
                else{
                    lcs.matches[i][j] = lcs.matches[i][j-1];
                    lcs.path[i][j] = "LFT";
                }
            }
        }
    }  
}