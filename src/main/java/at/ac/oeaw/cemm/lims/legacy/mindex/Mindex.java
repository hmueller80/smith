package at.ac.oeaw.cemm.lims.legacy.mindex;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @(#)Mindex.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class tests number of permitted mismatches during de-multiplexing to
 * unequivocally assign index sequences to samples.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class Mindex implements Serializable {

    //List<SequencingIndex> indexset;
    HashSet<String> current;
    String proposal = "";
    
    /**
     * Class constructor
     *
     * @author Heiko Muller
     * @version 1.0
     * @since 1.0
     */
    public Mindex(){
        if(Preferences.getVerbose()){
            System.out.println("init Mindex");
        }
    }

    /**
     * Calculates a HashSet of oligos that are 1 mismatch away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 1 if the set of indices can be de-multiplexed with 1
     * mismatch allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet1(List<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 1;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            String idx = indices.get(j);
            if(idx != null){
                String s = idx.trim().toUpperCase();
                if (s.length() >= 1) {
                    test = getDistance1Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            }
        }
        return result;
    }

    /**
     * Calculates a HashSet of oligos that are 2 mismatches away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 2 if the set of indices can be de-multiplexed with 2
     * mismatches allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet2(List<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 2;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            String idx = indices.get(j);
            if(idx != null){
                String s = idx.trim().toUpperCase();
                if (s.length() >= 2) {
                    test = getDistance2Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates a HashSet of oligos that are 2 mismatches away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 2 if the set of indices can be de-multiplexed with 2
     * mismatches allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet3(ArrayList<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 3;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            String idx = indices.get(j);
            if(idx != null){
                String s = idx.trim().toUpperCase();
                if (s.length() >= 3) {
                    test = getDistance3Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates a HashSet of oligos that are 2 mismatches away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 2 if the set of indices can be de-multiplexed with 2
     * mismatches allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet4(ArrayList<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 4;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            String idx = indices.get(j);
            if(idx != null){
                String s = idx.trim().toUpperCase();
                if (s.length() >= 4) {
                    test = getDistance4Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates a HashSet of oligos that are 2 mismatches away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 2 if the set of indices can be de-multiplexed with 2
     * mismatches allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet5(ArrayList<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 5;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            String idx = indices.get(j);
            if(idx != null){
                String s = idx.trim().toUpperCase();
                if (s.length() >= 5) {
                    test = getDistance5Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates a HashSet of oligos that are 2 mismatches away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 2 if the set of indices can be de-multiplexed with 2
     * mismatches allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet6(ArrayList<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 6;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            String idx = indices.get(j);
            if(idx != null){
                String s = idx.trim().toUpperCase();
                if (s.length() >= 6) {
                    test = getDistance6Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates a HashSet of oligos that are 1 mismatch away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 1 if the set of indices can be de-multiplexed with 1
     * mismatch allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet1FromStringList(ArrayList<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 1;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            
            
                String s = indices.get(j).trim().toUpperCase();
                if (s.length() >= 1) {
                    test = getDistance1Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            
        }
        return result;
    }

    /**
     * Calculates a HashSet of oligos that are 2 mismatches away from the input
     * sequences.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param indices - a selected set of indices
     * @return int - 2 if the set of indices can be de-multiplexed with 2
     * mismatches allowed, -1 otherwise
     * @since 1.0
     */
    public int calculateHashSet2FromStringList(ArrayList<String> indices) {
        //exclude "none" indexed samples from HashSet calculation

        int result = 2;
        HashSet<String> hs = new HashSet<String>();
        for (int j = 0; j < indices.size(); j++) {
            ArrayList<String> test = null;
            
            
                String s = indices.get(j).trim().toUpperCase();
                if (s.length() >= 2) {
                    test = getDistance2Oligos(s);
                    for (int i = 0; i < test.size(); i++) {
                        String key = test.get(i);
                        //System.out.println(indices[j] + " " + key);
                        if (!hs.contains(key)) {
                            hs.add(key);
                        } else {
                            result = -1;
                            break;
                        }
                    }
                }else{
                    result = -1;
                }
            
        }
        return result;
    }

    /**
     * Calculates all sequences that are 1 mismatch away from the input sequence.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param index - the input sequences, an oligo of length 6
     * @return ArrayList<String> - a list of all sequences that are 1 mismatch away from index
     * @since 1.0
     */
    private ArrayList<String> getDistance1Oligos(String index) {
        String[] Nreplace = new String[index.length()];
        char[] ca = index.toCharArray();
        char[] temp = null;
        for (int i = 0; i < Nreplace.length; i++) {
            temp = index.toCharArray();
            temp[i] = 'N';
            Nreplace[i] = new String(temp);
        }
        ArrayList<String> al = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        al.add(index);
        hs.add(index);
        for (int i = 0; i < Nreplace.length; i++) {
            ConsensusMotif cm = new ConsensusMotif(Nreplace[i]);
            String[] matches = cm.getMatchingOligos();
            for (int j = 0; j < matches.length; j++) {
                if (cm.matchDirect(matches[j])) {
                    if (!hs.contains(matches[j])) {
                        hs.add(matches[j]);
                        al.add(matches[j]);
                        //System.out.println(index + "\t" + matches[j]);
                    }
                    //System.out.println(matches[j]);
                }
            }
            //al.add(Nreplace[i]);
        }
        return al;
    }

    /**
     * Calculates all sequences that are 2 mismatches away from the input sequence.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param index - the input sequences, an oligo of length 6
     * @return ArrayList<String> - a list of all sequences that are 2 mismatches away from index
     * @since 1.0
     */
    private ArrayList<String> getDistance2Oligos(String index) {
        String[] Nreplace = new String[(index.length() * (index.length() - 1)) / 2];
        char[] ca = index.toCharArray();
        char[] temp = null;
        int count = 0;
        for (int i = 0; i < index.length() - 1; i++) {
            for (int j = i + 1; j < index.length(); j++) {
                temp = index.toCharArray();
                temp[i] = 'N';
                temp[j] = 'N';
                Nreplace[count] = new String(temp);
                count++;
            }
        }
        ArrayList<String> al = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        al.add(index);
        hs.add(index);
        for (int i = 0; i < Nreplace.length; i++) {
            ConsensusMotif cm = new ConsensusMotif(Nreplace[i]);
            String[] matches = cm.getMatchingOligos();
            for (int j = 0; j < matches.length; j++) {
                if (cm.matchDirect(matches[j])) {
                    if (!hs.contains(matches[j])) {
                        hs.add(matches[j]);
                        al.add(matches[j]);
                        //System.out.println(index + "\t" + matches[j]);
                    }
                    //System.out.println(matches[j]);
                }
            }
            //al.add(Nreplace[i]);
        }
        return al;
    }
    
    /**
     * Calculates all sequences that are 2 mismatches away from the input sequence.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param index - the input sequences, an oligo of length 6
     * @return ArrayList<String> - a list of all sequences that are 2 mismatches away from index
     * @since 1.0
     */
    private ArrayList<String> getDistance3Oligos(String index) {
        String[] Nreplace = new String[(index.length() * (index.length() - 1) * (index.length() - 2)) / 6];
        char[] ca = index.toCharArray();
        char[] temp = null;
        int count = 0;
        for (int i = 0; i < index.length() - 1; i++) {
            for (int j = i + 1; j < index.length(); j++) {
                for (int k = j + 1; k < index.length(); k++) {
                    temp = index.toCharArray();
                    temp[i] = 'N';
                    temp[j] = 'N';
                    temp[k] = 'N';
                    Nreplace[count] = new String(temp);
                    count++;
                }
            }
        }
        ArrayList<String> al = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        al.add(index);
        hs.add(index);
        for (int i = 0; i < Nreplace.length; i++) {
            ConsensusMotif cm = new ConsensusMotif(Nreplace[i]);
            String[] matches = cm.getMatchingOligos();
            for (int j = 0; j < matches.length; j++) {
                if (cm.matchDirect(matches[j])) {
                    if (!hs.contains(matches[j])) {
                        hs.add(matches[j]);
                        al.add(matches[j]);
                        //System.out.println(index + "\t" + matches[j]);
                    }
                    //System.out.println(matches[j]);
                }
            }
            //al.add(Nreplace[i]);
        }
        return al;
    }
    
    /**
     * Calculates all sequences that are 2 mismatches away from the input sequence.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param index - the input sequences, an oligo of length 6
     * @return ArrayList<String> - a list of all sequences that are 2 mismatches away from index
     * @since 1.0
     */
    private ArrayList<String> getDistance4Oligos(String index) {
        String[] Nreplace = new String[(index.length() * (index.length() - 1) * (index.length() - 2)*(index.length() - 3)) / 24];
        char[] ca = index.toCharArray();
        char[] temp = null;
        int count = 0;
        for (int i = 0; i < index.length() - 1; i++) {
            for (int j = i + 1; j < index.length(); j++) {
                for (int k = j + 1; k < index.length(); k++) {
                    for (int l = k+ 1; l < index.length(); l++) {
                        temp = index.toCharArray();
                        temp[i] = 'N';
                        temp[j] = 'N';
                        temp[k] = 'N';
                        temp[l] = 'N';
                        Nreplace[count] = new String(temp);
                        count++;
                    }
                }
            }
        }
        ArrayList<String> al = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        al.add(index);
        hs.add(index);
        for (int i = 0; i < Nreplace.length; i++) {
            ConsensusMotif cm = new ConsensusMotif(Nreplace[i]);
            String[] matches = cm.getMatchingOligos();
            for (int j = 0; j < matches.length; j++) {
                if (cm.matchDirect(matches[j])) {
                    if (!hs.contains(matches[j])) {
                        hs.add(matches[j]);
                        al.add(matches[j]);
                        //System.out.println(index + "\t" + matches[j]);
                    }
                    //System.out.println(matches[j]);
                }
            }
            //al.add(Nreplace[i]);
        }
        return al;
    }
    
    /**
     * Calculates all sequences that are 2 mismatches away from the input sequence.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param index - the input sequences, an oligo of length 6
     * @return ArrayList<String> - a list of all sequences that are 2 mismatches away from index
     * @since 1.0
     */
    private ArrayList<String> getDistance5Oligos(String index) {
        String[] Nreplace = new String[(index.length() * (index.length() - 1) * (index.length() - 2)*(index.length() - 3)*(index.length() - 4)) / 120];
        char[] ca = index.toCharArray();
        char[] temp = null;
        int count = 0;
        for (int i = 0; i < index.length() - 1; i++) {
            for (int j = i + 1; j < index.length(); j++) {
                for (int k = j + 1; k < index.length(); k++) {
                    for (int l = k+ 1; l < index.length(); l++) {
                        for (int m = l+ 1; m < index.length(); m++) {
                            temp = index.toCharArray();
                            temp[i] = 'N';
                            temp[j] = 'N';
                            temp[k] = 'N';
                            temp[l] = 'N';
                            temp[m] = 'N';
                            Nreplace[count] = new String(temp);
                            count++;
                        }
                    }
                }
            }
        }
        ArrayList<String> al = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        al.add(index);
        hs.add(index);
        for (int i = 0; i < Nreplace.length; i++) {
            ConsensusMotif cm = new ConsensusMotif(Nreplace[i]);
            String[] matches = cm.getMatchingOligos();
            for (int j = 0; j < matches.length; j++) {
                if (cm.matchDirect(matches[j])) {
                    if (!hs.contains(matches[j])) {
                        hs.add(matches[j]);
                        al.add(matches[j]);
                        //System.out.println(index + "\t" + matches[j]);
                    }
                    //System.out.println(matches[j]);
                }
            }
            //al.add(Nreplace[i]);
        }
        return al;
    }
    
    /**
     * Calculates all sequences that are 2 mismatches away from the input sequence.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param index - the input sequences, an oligo of length 6
     * @return ArrayList<String> - a list of all sequences that are 2 mismatches away from index
     * @since 1.0
     */
    private ArrayList<String> getDistance6Oligos(String index) {
        String[] Nreplace = new String[(index.length() * (index.length() - 1) * (index.length() - 2)*(index.length() - 3)*(index.length() - 4)*(index.length() - 5)) / 720];
        char[] ca = index.toCharArray();
        char[] temp = null;
        int count = 0;
        for (int i = 0; i < index.length() - 1; i++) {
            for (int j = i + 1; j < index.length(); j++) {
                for (int k = j + 1; k < index.length(); k++) {
                    for (int l = k+ 1; l < index.length(); l++) {
                        for (int m = l+ 1; m < index.length(); m++) {
                            for (int n = m+ 1; n < index.length(); n++) {
                                temp = index.toCharArray();
                                temp[i] = 'N';
                                temp[j] = 'N';
                                temp[k] = 'N';
                                temp[l] = 'N';
                                temp[m] = 'N';
                                temp[n] = 'N';
                                Nreplace[count] = new String(temp);
                                count++;
                            }
                        }
                    }
                }
            }
        }
        ArrayList<String> al = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        al.add(index);
        hs.add(index);
        for (int i = 0; i < Nreplace.length; i++) {
            ConsensusMotif cm = new ConsensusMotif(Nreplace[i]);
            String[] matches = cm.getMatchingOligos();
            for (int j = 0; j < matches.length; j++) {
                if (cm.matchDirect(matches[j])) {
                    if (!hs.contains(matches[j])) {
                        hs.add(matches[j]);
                        al.add(matches[j]);
                        //System.out.println(index + "\t" + matches[j]);
                    }
                    //System.out.println(matches[j]);
                }
            }
            //al.add(Nreplace[i]);
        }
        return al;
    }

   /**
     * Returns the laser balance for a set of indices.
     * Perfect laser balance means that at each position of the index read there is an equal proportion of A/C versus G/T nucleotides.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param list - the set of index sequences of a lane
     * @return String - a String indicating the proportion of A/C versus G/T nucleotides for each of the 6 positions
     * @since 1.0
     */
    public String getLaserBalance(List<String> list) {
        int count = 0;
        //exclude "none" indexed samples from laser balance calculation
        for (String idx : list) {
            if (idx != null && idx.trim().length() == 6) {
                count++;
            }
        }
        if (list.size() > 0 && count > 0) {
            char[][] ca = new char[count][6];
            for (int i = 0; i < count; i++) {
                String idx = list.get(i);
                if(idx != null){
                    String s = idx.trim();
                    if (s.length() == 6) {
                        ca[i] = s.toUpperCase().toCharArray();
                    }
                }
            }
            return getLaserBalance(ca);
        } else {
            return "";
        }

    }

    /**
     * Returns the laser balance for a set of indices.
     * Perfect laser balance means that at each position of the index read there is an equal proportion of A/C versus G/T nucleotides.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param ca - the set of index sequences of a lane as character array
     * @return String - a String indicating the proportion of A/C versus G/T nucleotides for each of the 6 positions
     * @since 1.0
     */
    private String getLaserBalance(char[][] ca) {
        int[] ac = new int[ca[0].length];
        int[] gt = new int[ca[0].length];
        for (int i = 0; i < ca[0].length; i++) {
            for (int j = 0; j < ca.length; j++) {
                char c = ca[j][i];
                if (c == 'A' || c == 'C') {
                    ac[i]++;
                } else if (c == 'G' || c == 'T') {
                    gt[i]++;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ca[0].length; i++) {
            if (i < ca[0].length - 1) {
                sb.append(ac[i] + "/" + gt[i] + " // ");
            } else {
                sb.append(ac[i] + "/" + gt[i]);
            }
        }
        sb.append("\r\n");
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < ca[0].length; i++) {
            char c = 'N';
            if (ac[i] < gt[i]) {
                c = 'M';
            } else if (ac[i] > gt[i]) {
                c = 'K';
            }
            sb1.append(c);
        }
        proposal = sb1.toString();

        return sb.toString();
    }

    /**
     * Performs sorting of a list of indices such that at the top of the list there are indices that improve the laser balance when added to the lane.
     *
     * @author Heiko Muller
     * @version 1.0
     * @param list - the set of index sequences of samples not yet added to a lane
     * @return List<Sample> - the list of index sequences with index sequences improving laser balance at the top.
     * @since 1.0
     */
    public List<SampleDTO> bringProposalsToTopOfList(List<SampleDTO> list) {
        //System.out.println(proposal);
        ConsensusMotif cm = new ConsensusMotif(proposal);
        if (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                SampleDTO s = list.get(i);
                //System.out.println(s.getName());
                String sidx = s.getIndex().getIndex();
                if (sidx != null && sidx.trim().length() == 6) {
                    String idx = sidx.trim().toUpperCase();
                    //System.out.println(idx);

                    if (cm.matchDirect(idx)) {
                        //System.out.println("match " + s1 + " proposal " + proposal);
                        list.remove(s);
                        list.add(0, s);
                    } else {
                        list.remove(s);
                        list.add(list.size() - 1, s);
                    }
                } else {
                    list.remove(s);
                    list.add(list.size() - 1, s);
                    //list.add(0, s);
                }
            }
        }
        return list;
    }

    /**
     * Getter for the proposal consensus motif. The proposal is a IUPAC style DNA motif consisting of N, M, or K letters.
     * M indicates that there are less A/C than G/T nucleotides in a given index position considering the current index sequences of a lane.
     * K indicates that there are more A/C than G/T nucleotides in a given index position considering the current index sequences of a lane.
     * N indicates equal proportions of A/C and G/T.
     * Index sequences matching the proposal are most advantageous to be added to the lane for improving laser balance.
     *
     * @author Heiko Muller
     * @version 1.0
     * @return String - the proposal IUPAC motif
     * @since 1.0
     */
    public String getProposal() {
        return proposal;
    }
    
    public static void main(String[] a){
        Mindex m = new Mindex();
        //List<String> l = m.getDistance1Oligos("ACGTGGAC");
        //for(String s : l){
        //    System.out.println(s);
        //}
        
        String i1 = new String ("ACGAC");
        String  i2 = new String ("CGTCG");
        String  i3 = new String ("GTAGT");
        String  i4 = new String ("TACTA");
        ArrayList<String > al = new ArrayList<String >();
        al.add(i1);
        al.add(i2);
        al.add(i3);
        al.add(i4);
        int m1 = m.calculateHashSet1(al);
        int m2 = -1;
        int m3 = -1;
        int m4 = -1;
        int m5 = -1;
        int m6 = -1;
        if(m1 > -1){
            m2 = m.calculateHashSet2(al);
        }
        if(m2 > -1){
            m3 = m.calculateHashSet3(al);
        }
        if(m3 > -1){
            m4 = m.calculateHashSet4(al);
        }
        if(m4 > -1){
            m5 = m.calculateHashSet5(al);
        }
        if(m5 > -1){
            m6 = m.calculateHashSet6(al);
        }
        System.out.println(m1 + "\t" + m2+ "\t" + m3+ "\t" + m4+ "\t" + m5+ "\t" + m6);
        
    }
}
