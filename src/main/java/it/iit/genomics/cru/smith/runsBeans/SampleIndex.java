package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;

/**
 * @(#)SampleIndex.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Utility class used to visualize different samples ids and indexes when making
 * a new run.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class SampleIndex {

    private int id;
    private String sequencingindex;

    /**
    * Constructor
    *
    * @author Francesco Venco
    * @param id
    * @param sequencingindex
    * @since 1.0
    */
    public SampleIndex(int id, String sequencingindex) {
        if(Preferences.getVerbose()){
            System.out.println("init SampleIndex");
        }
        this.id = id;
        this.sequencingindex = sequencingindex;
    }

    /**
    * Getter for id
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getId() {
        return id;
    }

    /**
    * Getter for sequencingindex
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSequencingIndex() {
        return this.sequencingindex;
    }

    /**
    * Setter for sequencingindex
    *
    * @author Francesco Venco
    * @param index
    * @since 1.0
    */
    public void setSequencingIndex(String index) {
        this.sequencingindex = index;
    }

}
