import java.io.*;
import java.util.*;

public class Strassen {
    //recursively multiplies two arrays
    /*
    The main function is the main driver,
    it sets up all the parameters, and handles the file input output portion
    of the code. It then runs strassen to multiply the two arrays together.
    
    */
    
    //counter for time tracking comparisons
    public static int strassen_counter = 0;
    
    //main file
    /*
    Handles file input, and initializes variables
    Also performs the initial set up of converting the file input into two
    matrices of the correct size needed for the algorithm. As well as general
    output handling
    */
    public static void main(String[] args) {
        String fileName = "";
        if (args.length != 1){
            System.out.println("Not exactly one argument given for filepath");
            System.exit(-1);
        }
        else{
            fileName = args[0];
        }
        String line = null;
        int order = 0; 
        boolean found = false; //booleans to handle output flow
        int size = 32; //initial size set to allocate space, can be larger
        //variables for use
        int x = 0; int y = 0;
        int[][] container = new int [size][size];
        int[][] output = new int [size][size];
        int[][] A = new int [size][size];
        int[][] B = new int [size][size];
        StringTokenizer tokenizer;
        
        
        try {
            //file input
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                if (line.length()== 0){
                    if (found == true){
                        //echo input
                        System.out.println("Input:");
                        System.out.println(order);
                        print_array(container, x, order);
                        
                        A = new int [order][order];
                        B = new int [order][order];
                        //splitting input
                        ABlize(container, A, B, x, order);
                        //regular multiplication
                        ord_multi(A,B);
                        
                        //strassen multiplication
                        output = strassen(A,B);
                        
                        //strassen output
                        System.out.println("Strassen:");
                        for (int i = 0; i < output.length; i++){
                            for (int j = 0; j < output.length; j++){
                                System.out.print(output[i][j] + "\t");
                            }
                            System.out.println();
                        }
                        System.out.println("Counter: " + strassen_counter);
                        strassen_counter = 0;

                    }
                    found = false;
                }
                if (line.length() == 1){
                    //this is a new matrix
                    order = Integer.parseInt(line);
                    found = true;
                    x = 0;
                    y = 0;
                    continue;
                }
                if (found){
                    //pulling in values into matrix
                    tokenizer = new StringTokenizer(line," ");
                    while (tokenizer.hasMoreTokens()){
                        container[x][y] = Integer.parseInt(tokenizer.nextToken());
                        y++;
                    }
                    y = 0;
                    x++;
                }
            }   
        } catch (FileNotFoundException ex) {
            System.out.println("File error");
        } catch (IOException ex) {
            System.out.println("Read error");
        }
    }
    
    /*
    Prints out a matrix, given the sizes of that matrix, and its address
    Write to console
    */
    public static void print_array(int[][] container, int x, int y){
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                System.out.print(container[i][j] + "\t");
            }
            System.out.println();
        }
    }
    /*
    Takes two matrices and
    Performs regular matrix multiplication
    Using the algorithm from the book
    */
    public static void ord_multi(int[][] A, int[][] B){
        int counter = 0;
        int n = A.length;
        int[][] C = new int [n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                C[i][j] = 0;
                for (int k = 0; k < n; k++){
                    C[i][j] = C[i][j] 
                            + A[i][k] 
                            * B[k][j];
                    counter++;
                }
            }
        }
        System.out.println("Normal Multiplication:");
        print_array(C, n, n);
        System.out.println("Counter: " + counter);
    }
    
    /*
    Strassen's algorithm
    */
    public static int[][] strassen(int[][] A, int[][] B){
        int n = A.length;
        int[][] C = new int[n][n];
        //each call will add to the counter
        //for comparison purposes
        strassen_counter++;
        //base case
        if (n == 1){
            C[0][0] = A[0][0] * B[0][0];
        }
        //recursive case
        else{
            //setting up space to subdivide
            int[][] A11 = new int[n/2][n/2]; 
            int[][] A12 = new int[n/2][n/2];
            int[][] A21 = new int[n/2][n/2];
            int[][] A22 = new int[n/2][n/2];
            int[][] B12 = new int[n/2][n/2];
            int[][] B21 = new int[n/2][n/2];
            int[][] B11 = new int[n/2][n/2];
            int[][] B22 = new int[n/2][n/2];
            
            //step1
            //values are partitioned
            partition(A,A11,0,n/2,0, n/2);
            partition(A,A12,0,n/2,n/2, n);
            partition(A,A21,n/2,n,0, n/2);
            partition(A,A22,n/2,n,n/2, n);
            partition(B,B11,0,n/2,0, n/2);
            partition(B,B12,0,n/2,n/2, n);
            partition(B,B21,n/2,n,0, n/2);
            partition(B,B22,n/2,n,n/2, n);
            
            //step 2
            //helper matrices are formed
            int[][] S1 = sub(B12,B22);
            int[][] S2 = add(A11,A12);
            int[][] S3 = add(A21,A22);
            int[][] S4 = sub(B21,B11);
            int[][] S5 = add(A11,A22);
            int[][] S6 = add(B11,B22);
            int[][] S7 = sub(A12,A22);
            int[][] S8 = add(B21,B22);
            int[][] S9 = sub(A11,A21);
            int[][] S10 = add(B11,B12);
            
            //step 3
            //second set of matrices
            //for strassen calculations
            //recursion here
            int[][] P1 = strassen(A11,S1);
            int[][] P2 = strassen(S2,B22);
            int[][] P3 = strassen(S3, B11);
            int[][] P4 = strassen(A22,S4);
            int[][] P5 = strassen(S5,S6);
            int[][] P6 = strassen(S7,S8);
            int[][] P7 = strassen(S9,S10);
            
            //step 4
            int[][] C11 = add(sub(add(P5,P4),P2),P6);
            int[][] C12 = add(P1,P2);
            int[][] C21 = add(P3,P4);
            int[][] C22 = sub(sub(add(P5,P1),P3),P7);
            
            //combining all components
            combine(C,C11,0,n/2,0, n/2);
            combine(C,C12,0,n/2,n/2, n);
            combine(C,C21,n/2,n,0, n/2);
            combine(C,C22,n/2,n,n/2, n);

        }
         
        return C;
    }
    
    /*
    Because input is given as one large block, 
    ABlize is used to separate out the two matrices
    Takes an initial long matrix
    and breaks it into two
    */
    public static void ABlize(int[][] container, int[][] A, int[][] B, 
            int x, int y){
        
        for (int i = 0; i < x/2; i++){
            for (int j = 0; j < y; j++){
                A[i][j] = container[i][j];
            }
        }
        
        for (int i = x/2; i < x; i++){
            for (int j = 0; j < y; j++){
                B[i-x/2][j] = container[i][j];
            }
        }
        
    }
    
    /*
    Performs partitioning step in order to break down an array into 4 subarrays
    This is essentially a copy of one portion of an array
    */
    public static void partition(int[][] P, int[][] sub, 
            int x1, int x2,
            int y1, int y2){
        
        for (int i = 0, x = x1; i < x2 - x1; i++, x++){
            for (int j = 0, y = y1; j < y2- y1; j++, y++){
                sub[i][j] = P[x][y];
            }
        }
    }
    
    /*
    Adds two matrices together
    */
    public static int[][] add(int[][] A, int[][] B){
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }
    
    /*
    Subtacts two matrices together
    */
    public static int[][] sub(int[][] A, int[][] B){
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }
    
    /*
    Puts one submatrix into a larger matrix
    */
    public static void combine(int[][] C, int[][] sub,
            int x1, int x2,
            int y1, int y2){

        
        for (int i = 0, x = x1; x < x2; x++, i++){
            for (int j = 0, y = y1; y < y2; y++, j++){
                C[x][y] = sub[i][j];
            }
        }
    }
    
}
