package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;

/**
 * @(#)SampleSpecDesc.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class holds information about sample index, description, and comments.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class SampleSpecDesc implements Serializable {
    
    private String index;
    private String comment;
    private String description;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @param index
    * @param comment
    * @param description
    * @since 1.0
    */
    public SampleSpecDesc(String index, String comment, String description) {
        if(Preferences.getVerbose()){
            System.out.println("init SampleSpecDesc");
        }
        this.index = index;
        this.comment = comment;
        this.description = description;
        //System.out.println("c: " + index);
    }

    

    /**
    * Getter for sequencing index.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getIndex() {
        return index;
    }

    /**
    * Setter for sequencing index.
    *
    * @author Francesco Venco
    * @param index
    * @since 1.0
    */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
    * Getter for comment.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getComment() {
        return comment;
    }

    /**
    * Setter for comment.
    *
    * @author Francesco Venco
    * @param comment
    * @since 1.0
    */
    public void setComment(String comment) {
        System.out.println("SETTING COMMENT " + comment);
        this.comment = comment;
    }

    /**
    * Getter for description.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getDescription() {
        return description;
    }

    /**
    * Setter for description.
    *
    * @author Francesco Venco
    * @param description
    * @since 1.0
    */
    public void setDescription(String description) {
        System.out.println("SETTING DESCRIPTION " + description);
        this.description = description;
    }

    /**
    * Getter for sequencing index used as tab title.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getTitle() {
        if (index.equals("none")) {
            return "none";
        } else {
            return index;
        }
    }

    /**
    * Returns sequencing index.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String toString() {
        return index;
    }

}
