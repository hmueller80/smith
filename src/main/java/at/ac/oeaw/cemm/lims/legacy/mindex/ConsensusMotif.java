package at.ac.oeaw.cemm.lims.legacy.mindex;

import java.util.Vector;
import java.util.HashSet;

/**
 * @(#)ConsensusMotif.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class finds matching 4 letter DNA motifs for a IUPAC-style degenerate DNA
 * motif such as SMCACGTGC
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class ConsensusMotif extends DNAMotif {

    private String MOTIF;

    /**
     *
     * Class constructor
     *
     * @author Heiko Muller
     * @param motif
     * @since 1.0
     */
    public ConsensusMotif(String motif) {
        super();
    
        if (motifIsLegal(motif)) {
            MOTIF = motif;
            OLIGOS = new HashSet();
            REVCOMPLOLIGOS = new HashSet();
            OLIGOSTRINGARRAY = initMatchingOligos(MOTIF);
            REVCOMPLOLIGOSTRINGARRAY = initRevComplMatchingOligos(MOTIF);
            for (int i = 0; i < OLIGOSTRINGARRAY.length; i++) {
                OLIGOS.add(OLIGOSTRINGARRAY[i]);
            }
            for (int i = 0; i < REVCOMPLOLIGOSTRINGARRAY.length; i++) {
                REVCOMPLOLIGOS.add(REVCOMPLOLIGOSTRINGARRAY[i]);
            }
        } else {
            MOTIF = null;
        }
    }

    /**
     *
     * Generates 4 letter DNA motifs matching IUPAC motif "motif"
     *
     * @author Heiko Muller
     * @param motif - a IUPAC style DNA motif, e.g. SMCACGTGC
     * @return String[] - an array of matching 4-letter DNA motifs
     * @since 1.0
     */
    private String[] initMatchingOligos(String motif) {
        int counter = 0;
        int letterindex = -1;
        char[] ca = motif.toCharArray();
        Vector v = new Vector();
        Vector temp = new Vector();
        v.addElement("");
        while (counter < ca.length) {
            letterindex = getLetterIndex(ca[counter]);
            for (int i = 0; i < v.size(); i++) {
                for (int j = 0; j < CODEINDEX[letterindex].length; j++) {
                    temp.addElement((String) v.elementAt(i) + ALPHABET[CODEINDEX[letterindex][j]]);
                }
            }
            v = temp;
            temp = new Vector();
            counter++;
        }
        String[] result = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            result[i] = (String) v.elementAt(i);
        }
        return result;
    }

    /**
     *
     * Generates 4 letter DNA motifs matching reverse complemented IUPAC motif
     * "motif"
     *
     * @author Heiko Muller
     * @param motif - a IUPAC style DNA motif, e.g. SMCACGTGC
     * @return String[] - an array of matching 4-letter DNA motifs
     * @since 1.0
     */
    private String[] initRevComplMatchingOligos(String motif) {
        String temp = getReverseComplement(motif);
        return initMatchingOligos(temp);
    }

    /**
     *
     * Getter for the IUPAC motif
     *
     * @author Heiko Muller
     * @return String - the IUPAC motif
     * @since 1.0
     */
    public String getMotif() {
        return MOTIF;
    }
}