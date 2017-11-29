package at.ac.oeaw.cemm.lims.legacy.mindex;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * @(#)DNAMotif.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class representing a IUPAC style degenerate DNA motif
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class DNAMotif implements Motif{
    
    protected String NAME = "";
    protected static final char[] LETTERS = {'A', 'C', 'G', 'T', 'R', 'Y', 'M', 'K', 'W', 'S', 'B', 'D', 'H', 'V', 'N'};
    protected static final String[] ALPHABET = {"A", "C", "G", "T"};
    protected static final int[] A = {0};//0
    protected static final int[] C = {1};//1
    protected static final int[] G = {2};//2
    protected static final int[] T = {3};//3
    protected static final int[] R = {0, 2};//4 purine A G
    protected static final int[] Y = {1, 3};//5 pyrimidine C T
    protected static final int[] M = {0, 1};//6 A C 
    protected static final int[] K = {2, 3};//7 G T
    protected static final int[] W = {0, 3};//8 A T
    protected static final int[] S = {1, 2};//9 C G
    protected static final int[] B = {1, 2, 3};//10 C G T
    protected static final int[] D = {0, 2, 3};//11 A G T
    protected static final int[] H = {0, 1, 3};//12 A C T
    protected static final int[] V = {0, 1, 2};//13 A C G
    protected static final int[] N = {0, 1, 2, 3};//14 A C G T
    protected static final int[][] CODEINDEX = {A, C, G, T, R, Y, M, K, W, S, B, D, H, V, N};
    protected static char[][] rev_compl_map = {{'A', 'T'}, {'C', 'G'},{'G', 'C'},{'T', 'A'},{'R', 'Y'},{'Y', 'R'},{'M', 'K'},{'K', 'M'}, {'W', 'W'},{'S', 'S'},{'B', 'V'},{'D', 'H'},{'H', 'D'},{'V', 'B'},{'N', 'N'}};
    protected static final double[] EVEN_SINGLE_NT_PROBS = {0.25, 0.25, 0.25, 0.25};
    
    protected static final Hashtable LETTERMAP;
    protected static final Hashtable LETTERINDEX;
    //protected static PermutationLooper PL;
    static{        
        LETTERMAP = new Hashtable();
        LETTERMAP.put("A", "T");
        LETTERMAP.put("C", "G");
        LETTERMAP.put("G", "C");
        LETTERMAP.put("T", "A");
        LETTERMAP.put("R", "Y");
        LETTERMAP.put("Y", "R");
        LETTERMAP.put("M", "K");
        LETTERMAP.put("K", "M");
        LETTERMAP.put("W", "W");
        LETTERMAP.put("S", "S");
        LETTERMAP.put("B", "V");
        LETTERMAP.put("D", "H");
        LETTERMAP.put("H", "D");
        LETTERMAP.put("V", "B");
        LETTERMAP.put("N", "N");  
        
        LETTERINDEX = new Hashtable();
        LETTERINDEX.put("A", new Integer(0));
        LETTERINDEX.put("C", new Integer(1));
        LETTERINDEX.put("G", new Integer(2));
        LETTERINDEX.put("T", new Integer(3));
        LETTERINDEX.put("R", new Integer(4));
        LETTERINDEX.put("Y", new Integer(5));
        LETTERINDEX.put("M", new Integer(6));
        LETTERINDEX.put("K", new Integer(7));
        LETTERINDEX.put("W", new Integer(8));
        LETTERINDEX.put("S", new Integer(9));
        LETTERINDEX.put("B", new Integer(10));
        LETTERINDEX.put("D", new Integer(11));
        LETTERINDEX.put("H", new Integer(12));
        LETTERINDEX.put("V", new Integer(13));
        LETTERINDEX.put("N", new Integer(14));        
       
    }
    
    protected HashSet OLIGOS;
    protected HashSet REVCOMPLOLIGOS;
    protected String[] OLIGOSTRINGARRAY;
    protected String[] REVCOMPLOLIGOSTRINGARRAY;
    
    
    
    /**
     *
     * Matches a sequence to the motif.
     *
     * @author Heiko Muller
     * @param sequence - a DNA sequence
     * @return boolean - true if match is found, false otherwise
     * @since 1.0
     */
    public boolean matchSequence(String sequence){
        boolean result = false;
        if(sequence.length() < getMotifLength()){
            result = false;
        }else if(sequence.length() == getMotifLength()){
            result = match(sequence);
        }else{
         
            for(int i = 0; i < sequence.length() - getMotifLength(); i++){
                if(match(sequence.substring(i, i + getMotifLength()))){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     *
     * Matches a DNA sequence of the same length as the motif to the motif.
     *
     * @author Heiko Muller
     * @param oligo - a DNA sequence
     * @return boolean - true if match is found in oligo or its reverse complement, false otherwise
     * @since 1.0
     */
    public boolean match(String oligo){ 
        return oligoIsMatching(oligo) || reverseComplementOligoIsMatching(oligo);
    }
    
    /**
     *
     * Matches a DNA sequence of the same length as the motif to the motif.
     *
     * @author Heiko Muller
     * @param oligo - a DNA sequence
     * @return boolean - true if match is found in oligo, false otherwise
     * @since 1.0
     */
    public boolean matchDirect(String oligo){
        return oligoIsMatching(oligo);
    }
    
    /**
     *
     * Matches a DNA sequence of the same length as the motif to the motif.
     *
     * @author Heiko Muller
     * @param oligo - a DNA sequence
     * @return boolean - true if match is found in reverse complement of oligo, false otherwise
     * @since 1.0
     */
    public boolean matchReverseComplement(String oligo){
        return reverseComplementOligoIsMatching(oligo);
    }
    
    /**
     *
     * Returns the length of the motif.
     *
     * @author Heiko Muller
     * @return int - the length of the motif
     * @since 1.0
     */
    public int getMotifLength(){
        if(OLIGOSTRINGARRAY != null){
            return OLIGOSTRINGARRAY[0].length();
        }else{
            return -1;
        }
    }
    
    /**
     *
     * Matches a DNA sequence of the same length as the motif to the motif.
     *
     * @author Heiko Muller
     * @param oligo - a DNA sequence
     * @return boolean - true if match is found in reverse complement of oligo, false otherwise
     * @since 1.0
     */
    public boolean oligoIsMatching(String oligo){
         return OLIGOS.contains(oligo);
    }
    
    /**
     *
     * Tests if a DNA sequence is present in the HashSet of reverse complemented oligos matching the motif.
     *
     * @author Heiko Muller
     * @param oligo - a DNA sequence
     * @return boolean - true if match is found, false otherwise
     * @since 1.0
     */
    public boolean reverseComplementOligoIsMatching(String oligo){
         return REVCOMPLOLIGOS.contains(oligo);
    }
    
    /**
     *
     * Returns the array of oligos matching the motif.
     *
     * @author Heiko Muller
     * @return String[] - the matching oligos
     * @since 1.0
     */
    public String[] getMatchingOligos(){
        return OLIGOSTRINGARRAY;
    }
    
    /**
     *
     * Returns the array of oligos matching the motif.
     *
     * @author Heiko Muller
     * @return String[] - the matching oligos
     * @since 1.0
     */
    public String[] getReverseComplementMatchingOligos(){
        return REVCOMPLOLIGOSTRINGARRAY;
    }
    
    /**
     *
     * Returns the index of a character c in the LetterIndex Hashtable
     *
     * @author Heiko Muller
     * @param c - a character
     * @return int - the index of c
     * @since 1.0
     */
    protected static int getLetterIndex(char c){        
        return ((Integer)LETTERINDEX.get("" + c)).intValue();
    }
    
    /**
     *
     * Returns the index of a character c in the LetterIndex Hashtable.
     *
     * @author Heiko Muller
     * @param letter - a character
     * @return int - the index of letter
     * @since 1.0
     */
    protected static int getLetterIndex(String letter){        
        return ((Integer)LETTERINDEX.get(letter)).intValue();
    }
    
    /**
     *
     * Setter for motif name.
     *
     * @author Heiko Muller
     * @param name - a character
     * @since 1.0
     */
    public void setName(String name){
        NAME = name;
    }
    
    /**
     *
     * Getter for motif name.
     *
     * @author Heiko Muller
     * @return String - the motif name
     * @since 1.0
     */
    public String getName(){ 
        return NAME;
    }
    
    /**
     *
     * Tests if motif is composed of admitted letters.
     *
     * @author Heiko Muller
     * @param motif - the motif being tested
     * @return boolean - true if motif is legal, false otherwise
     * @since 1.0
     */
    protected static boolean motifIsLegal(String motif){
        //System.out.println("testing motif");
        boolean result = true;
        //char[] c = motif.toCharArray();
        for(int i = 0; i < motif.length(); i++){
            //System.out.println(motif.substring(i, i + 1));
            if(!letterIsLegal(motif.substring(i, i + 1))){
                result = false;
                break;
            }
        }
        return result;
    }
    
    /**
     *
     * Tests if a letter is admitted.
     *
     * @author Heiko Muller
     * @param letter - the letter being tested
     * @return boolean - true if letter is legal, false otherwise
     * @since 1.0
     */
    protected static boolean letterIsLegal(String letter){
        return LETTERMAP.containsKey(letter);
    }
    
    /**
     *
     * Tests if a letter is admitted.
     *
     * @author Heiko Muller
     * @param c - the character being tested
     * @return boolean - true if letter is legal, false otherwise
     * @since 1.0
     */
    protected static boolean letterIsLegal(char c){
        return letterIsLegal("" + c);
    }
    
    /**
     *
     * Returns reverse complement of a letter.
     *
     * @author Heiko Muller
     * @param c - the character being reverse complemented
     * @return char - the reverse complement of the letter
     * @since 1.0
     */
    protected static char getRevComplLetter(char c){
        return rev_compl_map[getLetterIndex(c)][1];
    }
    
    /**
     *
     * Returns reverse complement of a letter.
     *
     * @author Heiko Muller
     * @param letter - the letter being reverse complemented
     * @return String - the reverse complement of the letter
     * @since 1.0
     */
    private static String getReverseComplementLetter(String letter){        
        return (String)LETTERMAP.get(letter);
    }
    
    
    /**
     *
     * Reverse complements a DNA sequence.
     *
     * @author Heiko Muller
     * @param sequence - the sequence being reverse complemented
     * @return String - the reverse complement of the sequence
     * @since 1.0
     */
    public static String getReverseComplement(String sequence){        
        StringBuffer sb = new StringBuffer();    
        String temp = "";
        for(int i = sequence.length() - 1; i >= 0; i--){
            temp = sequence.substring(i, i + 1);
            if(letterIsLegal(temp)){
                sb.append(getReverseComplementLetter(temp));
            }else{
                return null;
            }
        }
         return sb.toString();
    }
        
    }