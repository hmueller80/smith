package at.ac.oeaw.cemm.lims.util;

import java.util.HashSet;

/**
 * @(#)SampleNameFilter.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Tests sample names for presence of illegal characters and replaces them with underscore.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class NameFilter {
    
    //illegal characters
    //? ( ) [ ] / \ = + < > : ; " ' , * ^ | &    
    static char[] legal = new char[64];
    static HashSet<Character> characters = new HashSet<Character>();
    static HashSet<Character> digits = new HashSet<Character>();

    //legal characters
    static {
        legal[0] = '0';
        legal[1] = '1';
        legal[2] = '2';
        legal[3] = '3';
        legal[4] = '4';
        legal[5] = '5';
        legal[6] = '6';
        legal[7] = '7';
        legal[8] = '8';
        legal[9] = '9';
        legal[10] = 'A';
        legal[11] = 'B';
        legal[12] = 'C';
        legal[13] = 'D';
        legal[14] = 'E';
        legal[15] = 'F';
        legal[16] = 'G';
        legal[17] = 'H';
        legal[18] = 'I';
        legal[19] = 'J';
        legal[20] = 'K';
        legal[21] = 'L';
        legal[22] = 'M';
        legal[23] = 'N';
        legal[24] = 'O';
        legal[25] = 'P';
        legal[26] = 'Q';
        legal[27] = 'R';
        legal[28] = 'S';
        legal[29] = 'T';
        legal[30] = 'U';
        legal[31] = 'V';
        legal[32] = 'W';
        legal[33] = 'X';
        legal[34] = 'Y';
        legal[35] = 'Z';
        legal[36] = 'a';
        legal[37] = 'b';
        legal[38] = 'c';
        legal[39] = 'd';
        legal[40] = 'e';
        legal[41] = 'f';
        legal[42] = 'g';
        legal[43] = 'h';
        legal[44] = 'i';
        legal[45] = 'j';
        legal[46] = 'k';
        legal[47] = 'l';
        legal[48] = 'm';
        legal[49] = 'n';
        legal[50] = 'o';
        legal[51] = 'p';
        legal[52] = 'q';
        legal[53] = 'r';
        legal[54] = 's';
        legal[55] = 't';
        legal[56] = 'u';
        legal[57] = 'v';
        legal[58] = 'w';
        legal[59] = 'x';
        legal[60] = 'y';
        legal[61] = 'z';
        //legal[62] = '-';
        legal[62] = '_';
        for (int i = 0; i < legal.length; i++) {
            characters.add(legal[i]);
        }
        for (int i = 0; i < 10; i++) {
            digits.add(legal[i]);
        }

    }

    /**
    * Removes illegal characters in name and appends "S_" if name starts with digit.
    *
    * @author Heiko Muller
    * @param name
    * @return String
    * @since 1.0
    */
    public static String legalize(String name) {
        String n = name.trim();
        if(startsWithDigit(n)){
            n = "S_" + n;
        }
        if(hasIllegalCharacters(n)){
            n = replaceIllegalCharacters(n);
        }
        return n;
    }
    
    /**
    * Removes illegal characters in name and appends "S_" if name starts with digit.
    *
    * @author Heiko Muller
    * @param name
    * @return String
    * @since 1.0
    */
    public static String legalizeLibrary(String name) {
        String n = name.trim();
        if(startsWithDigit(n)){
            n = "L_" + n;
        }
        if(hasIllegalCharacters(n)){
            n = replaceIllegalCharacters(n);
        }
        return n;
    }
    
    /**
    * Tests if first character in name is a digit.
    *
    * @author Heiko Muller
    * @param name
    * @return boolean
    * @since 1.0
    */
    private static boolean startsWithDigit(String name){
        if (name.length() > 0 && digits.contains(name.charAt(0))) {
            return true;
        }
        return false;
    }

    /**
    * Tests if name contains illegal characters.
    *
    * @author Heiko Muller
    * @param name
    * @return boolean
    * @since 1.0
    */
    public static boolean hasIllegalCharacters(String name) {
        if(name == null){
            return false;
        }
        char[] nameca = name.toCharArray();
        for (int i = 0; i < nameca.length; i++) {
            if (!characters.contains(nameca[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
    * Replaces illegal characters with underscore.
    *
    * @author Heiko Muller
    * @param name
    * @return String
    * @since 1.0
    */
    private static String replaceIllegalCharacters(String name){
        char[] nameca = name.toCharArray();
        for (int i = 0; i < nameca.length; i++) {
            if (!characters.contains(nameca[i])) {
                if(nameca[i] != '+'){
                    nameca[i] = '_';
                }else{
                    nameca[i] = '!';
                }
            }
        }
        String temp = new String(nameca);
        temp = temp.replaceAll("-", "_minus_");
        temp = temp.replaceAll("!", "_plus_");
        temp = temp.replaceAll("__", "_");
        temp = temp.replaceAll("__", "_");
        temp = temp.replaceAll("__", "_");
        temp = temp.replaceAll("__", "_");
        temp = temp.replaceAll("__", "_");
        return temp;
    }
    
}

