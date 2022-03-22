/*
Assignment 3 modifications to LZW.java code 
Julia Kenny
Due Date: 11/15
*/

import java.io.*;

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^w
    private static int W = 9;         // codeword is starting at 9
    private static final int MaxLength = 16; // want to be going from 9-16
    private static boolean reset = false; // reset from user r = true anything else is false

    public static void compress() { 
    	BinaryStdOut.write(reset); // reset bit needed for expand to work correctly
    	
      	StringBuilder sb = new StringBuilder(); // need a new stringbuilder to keep track
        TSTmod<Integer> st = new TSTmod<Integer>();
        
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);
        int code = R+1;  // R is codeword for EOF		
		
        //initialize the current string
        StringBuilder current = new StringBuilder();
        //read and append the first char
        char c = BinaryStdIn.readChar();
        current.append(c);
        Integer codeword = st.get(current);
        while (!BinaryStdIn.isEmpty()) {
            codeword = st.get(current);
            //TODO: read and append the next char to current
        	c = BinaryStdIn.readChar();
        	current.append(c);
            if(!st.contains(current)){
              BinaryStdOut.write(codeword, W);
                      
               // ADDED CODE FOR RESET BELOW 
              if(W == 16 && code == L && reset == true){ 
                W = 9; // reset both L and W and code 
              	L = (int)(Math.pow(2,W));
              	code = R+1;
              	st = new TSTmod<Integer>(); // reset 
              	
              	for(int i = 0; i < R; i++){ // reset 
              		st.put(new StringBuilder("" + (char) i), i);
              	}
              }
              
              // added code below
              if(code == L && W<16){ // WHEN ALL CODES ARE USED THEN INCREMENT W THUS INCREMENTING L 
              	W++; // increment W
              	L = (int) (Math.pow(2,W)); // therefore incrementing L
              }     
              
              if(code < L) st.put(current, code++); // Add to symbol table if not full
			
             StringBuilder l = new StringBuilder();
             l.append(c);
             current = l;
            }
        }

        //TODO: Write the codeword of whatever remains
        //in current
		BinaryStdOut.write(st.get(current), W);
        BinaryStdOut.write(R, W); //Write EOF
        BinaryStdOut.close();
    }
    
    
     public static void expand() {
     	reset = BinaryStdIn.readBoolean();
        String[] st = new String[(int)(Math.pow(2, MaxLength))]; //might have to change to 2^16
        int i; // next available codeword value
		
		//add in reset stuff here potentially
		
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
            
        	//2 sections of added code below
            //ADDED FOR RESET -- same idea as in compress -- 
            if( W == 16 && i == L && reset == true){ // if using reset feature -- when maxed out then reset ONLY IF RESET IS TRUE 
              	W = 9; // RESET ALL THINGS LIKE IN COMPRESS RESET
              	L = (int)(Math.pow(2,W));
              	st = new String[(int)(Math.pow(2, MaxLength))]; 
        			for (i = 0; i < R; i++) // utilized above code
           				st[i] = "" + (char) i;
        			st[i++] = "";                       
            }
            
         if(i==L && W<16){ // WHEN ALL CODES ARE USE THEN INCREMENT W THUS INCREMENTING L  // SAME INCREMENTATION AS COMPRESS
            	W++;
            	L = (int)Math.pow(2,W);
            }	
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
    	if(args.length >=2){ // allows for user to input if they want to utilize reset
    		if(args[1].equals("r")) reset = true;
    		else reset = false;
    	}
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
        //}
    }
}