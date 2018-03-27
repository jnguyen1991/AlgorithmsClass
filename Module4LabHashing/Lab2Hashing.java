/*
jnguye36
Lab 2, Hashing Schemes Comparisons
EN.605.420.82.FA17

The goal is to compare 4 different hashing schemes, 
3 of which will use modulo, and one of which will be done 
using the multiplication method given in CLRS.

Each set of data will run through 11 different variations, changing either
the hashing scheme, bucket size, or collision handling scheme

The program will handle one set of data at a time, interpreting a set as
continuous data inputted for each line, ending at an empty line as the end point
for that set. At which time it will run on 11 subroutines 
to handle each hashing scheme. In between each subroutine, the initial variables
will be reset.

Two functions will be needed to handle input and output
    ReadFile
    PrintOutput
For simplicity, each collision handling scheme will be split into 3 functions.
    LinearProbe
    QuadProbe
    ChainProbe
Hashing will be split into 2 functions. 
    ModuloHash
    MultiHash
One function for ease of use
    Reset

Finally, values will wrap around on overflow, this should only happen on cases 
7 and 8, where only buckets 0 - 39 exist. 40%41, will resolve to 40, 
and will be treated as 0.

 */

import java.io.*;
import java.util.*;


public class Lab2Hashing {

    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileName;
        String outputName;
        if (args.length != 2){
            Scanner scan = new Scanner(System.in);
            String in;
            System.out.println("Enter an input file:");
            in = scan.next();
            fileName = in;
            
            System.out.println("Enter an output file:");
            in = scan.next();
            outputName = in;
        }
        else{
            fileName = args[0];
            outputName = args[1];
        }
        ArrayList<Integer> currentSet = new ArrayList<>();
        ArrayList<Integer> freeStack = new ArrayList<>();
        int Size = 120;
        int[] table = new int[Size];
        //for chaining
        int[] refTable = new int[Size];
        //each index for different stats, see print output
        int[] status = new int[5]; status[0] = 0; 
        //tracking
        int caseNum = 0;
        int setNum = 0;
        //defaulting, also same values in Reset()
        Arrays.fill(table, -99999);
        Arrays.fill(refTable, -99999);
        Arrays.fill(status, 0);
        for (int i = 0; i<Size; i++){
            freeStack.add(i);
        }
        try{
            FileReader filereader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(filereader);
            FileWriter filewriter = new FileWriter(outputName);
            BufferedWriter writer = new BufferedWriter(filewriter);
            //skip first three lines
            if (!reader.ready()){
                throw new Exception();
            }
            for (int i = 0; i < 4; i++){
                reader.readLine();
            }
            //main driver
            /*
            Read in one set at a time,
            And run through all test cases,
            Print output after each case, and reset all needed parameters
            */
            while (reader.ready()){
                currentSet = ReadFile(currentSet, reader);
                setNum++;
                caseNum = 0;
                //case 1
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    LinearProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),120,1,120),table, 1, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 2
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    QuadProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),120,1,120),table, 1, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 3
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    ChainProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),120,1,120),table, refTable,
                    freeStack, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 4
                caseNum++;
                Reset(table, refTable, freeStack, status);
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    LinearProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),113,1,120),table, 1, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 5
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    QuadProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),113,1,120),table, 1, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 6
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    ChainProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),113,1,120),table, refTable,
                    freeStack, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 7
                caseNum++;
                Reset(table, refTable, freeStack, status);
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    LinearProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),41,3,120),table, 3, status);
                }
                PrintOutput(table,3, status, writer, currentSet, caseNum, setNum);
                
                //case 8
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    QuadProbe(currentSet.get(i),
                    ModuloHash(currentSet.get(i),41,3,120),table, 3, status);
                }
                PrintOutput(table,3, status, writer, currentSet, caseNum, setNum);
                
                //random double for hashing
                double A = 0.6443488538;
                
                //case 9
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    LinearProbe(currentSet.get(i),
                    MultiHash(currentSet.get(i),A,1,120),table, 1, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 10
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    QuadProbe(currentSet.get(i),
                    MultiHash(currentSet.get(i),A,1,120),table, 1, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);
                
                //case 11
                caseNum++;
                Reset(table, refTable, freeStack, status);
                for (int i = 0; i < currentSet.size(); i++){
                    ChainProbe(currentSet.get(i),
                    MultiHash(currentSet.get(i),A,1,120),table, refTable,
                    freeStack, status);
                }
                PrintOutput(table,5, status, writer, currentSet, caseNum, setNum);

                currentSet.clear();
            }
            writer.close();
        }
        //file not found
        catch (FileNotFoundException e){
            
            System.out.println("File not found.");
            System.out.println(fileName);
            System.exit(-1);
        }
        //input errors
        catch (IOException e){
            System.out.println(e);
            System.out.println("File input error");
            System.exit(-1);
        //EMPTY input file given
        } catch (Exception ex) {
            System.out.println("\nEmpty file given."
                    + "\nPlease check contents of file.");
            System.exit(-1);
        }
    }
    /*
    Read File
    currentSet = place to add items into as the file is read
    reader = buffered reader pointing at input file
    
    Takes an empty arrayList and adds integers to it based on what it reads
    from the given file. Read file only used for this program, 
    setting to private
    
    */
    private static ArrayList<Integer> ReadFile(ArrayList<Integer> currentSet, 
            BufferedReader reader){
        String line = null;
        try{
            while ((line = reader.readLine())!= null){
                if (line.trim().isEmpty()){
                    if (currentSet.isEmpty()){
                        throw new Exception();
                    }
                    return currentSet;
                }
                currentSet.add(Integer.parseInt(line));
            }
        }
        //read error
        catch (IOException e){
            System.out.println(e);
            System.out.println("Error reading file.");
            System.exit(-1);
        }
        //incorrect format in file
        catch (NumberFormatException e){
            System.out.println("\nCould not parse item: " + line + 
                    "\nPlease ensure all data is readable by Integer.parseInt");
            System.exit(-1);
        } 
        //empty set given, skipping
        catch (Exception ex) {
            System.out.println("\nEmpty set given. Moving on to next set.");
        }
        return currentSet;
    }
    
    /*
    ModuloHash
    k = value to hash
    divisor = used to perform modulo operation
    bucket = integer to know how large buckets are
    tableSize = integer to know size of table
    
    Uses bucket and tableSize to figure out valid locations. Simple modulo hash,
    following k % divisor.
    
    */    
    public static int ModuloHash(int k, int divisor, int bucket, int tableSize){
        int out = k % divisor;
        out /= bucket;
        //wrap around if outside of table after hash
        while (out > (tableSize/bucket)){
            out-=tableSize/bucket;
        }
        return out;
    }

    /*
    MultiHash
    k = value to hash
    A = double given to create a fractional portion of k
    bucket = integer to know how large buckets are
    tableSize = integer to know size of table
    
    Uses CLRS, 3rd Edition Multiplication Hash method found in section 11.3.2
    Multiplies k and A to form a number with a fractional portion,
    and extracts that fractional portion to find a slot.
    
    */        
    public static int MultiHash(int k, double A, int bucket, int tableSize){
        int m = tableSize / bucket;
        int out = (int) Math.floor(m * ((k * A) % 1));
        //wrap around if outside of table after hash
        return out;
    }    

    
    /*
    PrintOutput
    table = table of values to print
    across = how many columns to print
    status = status of given data, each array index gives certain stats data
    writer = writer pointing at output file
    currentSet = used to check for empty set
    caseNum = Case that this table is printing for
    setNum = Set of data that this table is printing for
    
    Prints output, only used for this program and setting to private.
    
    */    
    private static void PrintOutput(int[] table, int across, int[] status, 
            BufferedWriter writer, ArrayList<Integer> currentSet,
            int caseNum, int setNum){
        //0 = collisions
        //1 = unable to add
        //2 = items added
        //3 = buckets/slots
        //4 = load factor
        if (currentSet.isEmpty()){
            System.out.println("Empty set, moving to next");
            return;
        }
        int upper;
        double lf = 0;
        
        try{
            System.out.print("\nSet number: " + setNum);
            writer.append("\nSet number: " + setNum);
            System.out.print("\nCase number: " + caseNum);
            writer.append("\nCase number: " + caseNum);
            for (int i = 0; i < table.length; i++){
                if (i%across == 0){
                    upper = i + across - 1;
                    System.out.print("\n"+i+"-"+upper+"\t");
                    writer.append("\n"+i+"-"+upper+"\t");
                }
                if (table[i] == -99999){
                    System.out.print("NIL\t");
                    writer.append("NIL\t");
                }
                else{
                    System.out.print(table[i]+"\t");
                    writer.append(table[i]+"\t");
                }
            }
            
            lf = (double) status[2] /status[3];
            
            System.out.print("\nCollisions:" + status[0]);
            writer.append("\nCollisions:" + status[0]);
            System.out.print("\nUnadded items:" + status[1]);
            writer.append("\nUnadded items:" + status[1]);
            System.out.print("\nEntries:" + status[2]);
            writer.append("\nEntries:" + status[2]);
            System.out.print("\nSlots:" + status[3]);
            writer.append("\nSlots:" + status[3]);
            System.out.println("\nLoad Factor:" + String.format("%.02f",lf));
            writer.append("\nLoad Factor:" + String.format("%.02f",lf));


            writer.append("\n");
        }
        catch (IOException e){
            System.out.println("Could not write to file. "
                    + "Check if file is locked, or if it exists.");
            System.exit(-1);
        }
    }    

    
    /*
    LinearProbe
    value = value to be hashed and stored
    key = location value to be sent to
    table = array to contain all values
    bucket = bucket size
    status = statistics as program is run
    
    Linear probe that will check if given key is valid, and if not will move
    by 1 to the next slot. Continues until not slots left.
    */    
    public static void LinearProbe(int value, int key, 
            int[] table, int bucket, int[] status){
        int i = 0;
        int probe = key * bucket;
        status[2] += 1;
        status[3] = table.length;
        while (i<table.length){
            //progression through loop
            if (table[probe] != -99999){
                i++;
                probe = (key+(i))%(table.length/bucket)*bucket;
            }
            //slot found
            else{
                for (int j = bucket - 1; j>=0; j--){
                    if (table[probe+j] == -99999){
                        table[probe+j] = value;
                        status[0] += i;
                        return;
                    }
                }
            }
        }
        //no slots
        status[1] += 1;
        return;
    }
    
    /*
    QuadProbe
    value = value to be hashed and stored
    key = location value to be sent to
    table = array to contain all values
    bucket = bucket size
    status = statistics as program is run
    
    Quadratic probe is similar to linear probe, but will use the formula to
    move down the table in a quadratic manner instead of 1 by 1.
    e.g. 1,3,6,etc.
    */    
    public static void QuadProbe(int value, int key, 
            int[] table, int bucket, int[] status){
        int i = 0;
        int probe = key * bucket;
        status[2] += 1;
        status[3] = table.length;
        while (i<table.length){
            //progression through loop
            if (table[probe] != -99999){
                i++;
                probe = (int) (Math.floor((key + (0.5 * i) + (0.5 * i * i ) ))
                        %(table.length/bucket)) * bucket;
            }
            //slot found
            else{
                for (int j = bucket - 1; j>=0; j--){
                    if (table[probe+j] == -99999){
                        table[probe+j] = value;
                        status[0] += i;
                        return;
                    }
                }
            }
        }
        //no slots
        status[1] += 1;
        return;
    }
    
    /*
    ChainProbe
    value = value to be hashed and stored
    key = location value to be sent to
    table = array to contain all values
    refTable = separate table to keep track of where the chains lead to
    freeStack = stack that keeps track of free available slots
    status = statistics as program is run
    
    Chain probe will use freeStack to find a free slot upon collision, and then
    use refTable to maintain links. These chains are on the same table itself
    freeStack gives space in descending order, and will offer up the highest
    slot first.
    */ 
    public static void ChainProbe(int value, int key, 
            int[] table, int[] refTable,
            ArrayList<Integer> freeStack, int[] status){
        int probe = key;
        int prev = key;
        int newKey;
        int collision = 0;
        status[2] += 1;
        status[3] = table.length;
        //first case
        //no collision, we insert on the given key
        if (table[probe] == -99999){
            table[probe] = value;
            status[0] += collision;
            return;
        }
        
        //collision, chaining logic begins
        else{
            //always only one collision
            collision = 1;
            //find new slot to place, or fail if empty
            if (freeStack.isEmpty()){
                status[0] += collision;
                status[1] += 1;
                return;
            }
            newKey = freeStack.get(freeStack.size()-1);
            while (table[newKey] > 0){
                //if slot already taken, discard it
                freeStack.remove(newKey);
                //try the next one
                newKey = freeStack.get(freeStack.size()-1);
            }
            //find the end of the chain
            prev = probe;
            while (refTable[prev] >= 0){ //check for end
                prev = refTable[prev];
            }
            //set the previous link to point to the the new key
            refTable[prev] = newKey;
            table[newKey] = value;
            status[0] += collision;
            return;
        }
    }
    
    /*
    Reset
    table = table containing inserted values
    refTable = table containing reference values
    freeStack = stack of available space
    status = statistics
    
    Reset simply resets the variables used in this program to make setting up
    each case and set easier. Reset is only used in this program, so private.
    */ 
    private static void Reset(int[] table, int[] refTable, 
            ArrayList<Integer> freeStack, int[] status){
        Arrays.fill(table,-99999);
        Arrays.fill(refTable,-99999);
        Arrays.fill(status,0);
        freeStack.clear();
        for (int i = 0; i < table.length;i++){
            freeStack.add(i);
        }
    }
}


