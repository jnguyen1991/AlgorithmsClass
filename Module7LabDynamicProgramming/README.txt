Java version 1.8.0_05
Javac 1.8.0_05
IDE: Netbeans

File takes two arguments, the first is an input file, the second is an output file.
Optional third parameter for IUPAC notation matching, for example Strong bonds code is S, and will match C against G.
i.e. SW will match for Strong Bonds and Weak Bonds

File output needs to be opened with other software besides notepad. 
For some reason notepad is not reading display the newline characters correctly,
but output appears normal in console and in notepad++. 
This is probably due to only LF being present without the CR.

e.g. java DynamicProgramming <input.txt> <output.txt> <[IUPAC String]>