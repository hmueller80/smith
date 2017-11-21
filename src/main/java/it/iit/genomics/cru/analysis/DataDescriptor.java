package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;

/**
 * @(#)DataDescriptor.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * DataDescriptor class
 * 
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
public class DataDescriptor {
    
    private String descriptorName;
    private String applicationName;
    private String type; //aligned,annotation
    private String format;
    private String folder;
    private String algorithm;
    private String parameters;
    
    /**
     *
     * Class constructor
     * 
     * @author Yuriy Vaskin
     * @param descriptorName
     * @param applicationName
     * @param type
     * @param format
     * @param folder
     * @param algorithm
     * @param parameters
     * @since 1.0
     */
    public DataDescriptor(String descriptorName,
    String applicationName,
    String type,
    String format,
    String folder,
    String algorithm,
    String parameters){
        if(Preferences.getVerbose()){
            System.out.println("init DataDescriptor");
        }
        this.descriptorName = descriptorName;
        this.applicationName = applicationName;
        this.type = type;
        this.format = format;
        this.folder = folder;
        this.algorithm = algorithm;
        this.parameters = parameters;
    
    }

    /**
     *
     * Getter for descriptorName
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String descriptorName 
     * @since 1.0
     */
    public String getDescriptorName() {
        return descriptorName;
    }

    /**
     *
     * Getter for applicationName
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String applicationName
     * @since 1.0
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     *
     * Getter for type
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String type
     * @since 1.0
     */
    public String getType() {
        return type;
    }

    /**
     *
     * Getter for format
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String format
     * @since 1.0
     */
    public String getFormat() {
        return format;
    }

    /**
     *
     * Getter for folder
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String folder
     * @since 1.0
     */
    public String getFolder() {
        return folder;
    }

    /**
     *
     * Getter for algorithm
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String algorithm
     * @since 1.0
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     *
     * Getter for parameters
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return String parameters
     * @since 1.0
     */
    public String getParameters() {
        return parameters;
    }
}
