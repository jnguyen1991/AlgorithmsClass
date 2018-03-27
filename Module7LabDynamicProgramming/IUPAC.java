/*
ENHANCEMENT

IUPAC class used for boolean logic, on how to compare bases
A = A is always a match, but if R is set to true then
A = G is a valid match

If no information is given, then the main program will default all of these
to false, and only exact matches will work.
*/
public class IUPAC {
        boolean R; //purine, GA
        boolean Y; //pyrimidine, TC
        boolean K; //keto, GT
        boolean M; //amino, AC
        boolean S; //strong bonds, GC
        boolean W; //weak bonds, AT
        boolean B; //all except A, GTC
        boolean D; //all except C, GAT
        boolean H; //all except G, ACT
        boolean V; //all except T, GCA
        String choices = "N/A";
    
    public IUPAC(String choices){
        this.choices = choices;
        R = choices.contains("R");
        Y = choices.contains("Y");
        K = choices.contains("K");
        M = choices.contains("M");
        S = choices.contains("S");
        W = choices.contains("W");
        B = choices.contains("B");
        D = choices.contains("D");
        H = choices.contains("H");
        V = choices.contains("V");
    }
    
    public boolean matches(char x, char y){
        if (x == y){
            return true;
        }
        if (R && ('G' + 'A' == x + y)){
            return true;
        }
        if (Y && ('T' + 'C' == x + y)){
            return true;
        }
        if (K && ('G' + 'T' == x + y)){
            return true;
        }
        if (M && ('A' + 'C' == x + y)){
            return true;
        }
        if (S && ('G' + 'C' == x + y)){
            return true;
        }
        if (W && ('A' + 'T' == x + y)){
            return true;
        }
        return false;
    }
}
