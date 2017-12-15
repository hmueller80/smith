/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SequencingRequestSubmission implements Serializable {
    private String libraryName = "";
    private String sequencingType = "";
    private String readLength = "";
    private String numberofLanes = "";
    private String specialRequirements = "";
    private String additionalComment = "";
    private String receivingPerson = "";
    private Date receivingDate;
    private String receivingComment = "";
    private String qualityControlPerson = "";
    private Date qualityControlDate;
    private String qualityControlSummary = "";
    private String qualityControlFiles = "";
    private String qualityControlStatus = "";
    private Integer sequencingRequestSubmissionId;
    private String sequencer = "";

    public SequencingRequestSubmission(ArrayList<String> row, ArrayList<Integer> indices) {
        if(indices.size() >= 16){
            if(!row.get(indices.get(0)).equals("null")){
                if(indices.get(0) > -1 && row.size() > indices.get(0)){
                    libraryName = row.get(indices.get(0));
                }
                if(indices.get(1) > -1 && row.size() > indices.get(1)){
                    sequencingType  = row.get(indices.get(1));
                }
                if(indices.get(2) > -1 && row.size() > indices.get(2)){
                    readLength = removeUnits(row.get(indices.get(2)));
                }
                if(indices.get(4) > -1 && row.size() > indices.get(4)){
                    numberofLanes = removeUnits(row.get(indices.get(4)));
                }
                if(indices.get(5) > -1 && row.size() > indices.get(5)){
                    specialRequirements = row.get(indices.get(5));
                }
                if(indices.get(6) > -1 && row.size() > indices.get(6)){
                    additionalComment = row.get(indices.get(6));
                }
                if(indices.get(7) > -1 && row.size() > indices.get(7)){
                    receivingPerson = row.get(indices.get(7));
                }
                if(indices.get(8) > -1 && row.size() > indices.get(8)){
                    receivingDate = new Date(System.currentTimeMillis());
                    //receivingDate = row.get(indices.get(8));
                }
                if(indices.get(9) > -1 && row.size() > indices.get(9)){
                    receivingComment = row.get(indices.get(9));
                }
                if(indices.get(10) > -1 && row.size() > indices.get(10)){
                    qualityControlPerson = row.get(indices.get(10));
                }
                if(indices.get(11) > -1 && row.size() > indices.get(11)){ 
                    qualityControlDate = new Date(System.currentTimeMillis());
                    //qualityControlDate = row.get(indices.get(11));
                }
                if(indices.get(12) > -1 && row.size() > indices.get(12)){
                    qualityControlSummary = row.get(indices.get(12));
                }
                if(indices.get(13) > -1 && row.size() > indices.get(13)){
                    qualityControlFiles = row.get(indices.get(13));
                }
                if(indices.get(14) > -1 && row.size() > indices.get(14)){
                    qualityControlStatus = row.get(indices.get(14));
                }
                if(indices.get(15) > -1 && row.size() > indices.get(15)){
                    sequencer = row.get(indices.get(15));
                }
            }
        }
    }
    
    private String removeUnits(String s){
        StringBuilder sb = new StringBuilder();
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++){
            if(Character.isDigit(c[i]) || c[i] == ',' || c[i] == '.'){
                sb.append(c[i]);
            }
        }
        return sb.toString();
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getSequencingType() {
        return sequencingType;
    }

    public String getReadLength() {
        return readLength;
    }

    public String getNumberofLanes() {
        return numberofLanes;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public String getReceivingPerson() {
        return receivingPerson;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public String getReceivingComment() {
        return receivingComment;
    }

    public String getQualityControlPerson() {
        return qualityControlPerson;
    }

    public Date getQualityControlDate() {
        return qualityControlDate;
    }

    public String getQualityControlSummary() {
        return qualityControlSummary;
    }

    public String getQualityControlFiles() {
        return qualityControlFiles;
    }

    public String getQualityControlStatus() {
        return qualityControlStatus;
    }

    public Integer getSequencingRequestSubmissionId() {
        return sequencingRequestSubmissionId;
    }

    public String getSequencer() {
        return sequencer;
    }
    
    
    
    public boolean siValid(){
        if( libraryName == null){
            //System.out.println("1");
            return false;
        }
        if( libraryName.equals("")){
            //System.out.println("2");
            return false;
        }
        if( libraryName.equals("null")){
            //System.out.println("3");
            return false;
        }
        if( sequencingType == null){
            //System.out.println("4");
            return false;
        }
        if( sequencingType.equals("")){
            //System.out.println("5");
            return false;
        }
        if( sequencingType.equals("null")){
            //System.out.println("6");
            return false;
        }
        if( readLength == null){
            //System.out.println("7");
            return false;
        }
        if( readLength.equals("null")){
            //System.out.println("8");
            return false;
        }
        if( readLength.equals("")){
            //System.out.println("9");
            return false;
        }
        if( numberofLanes == null){
            //System.out.println("10");
            return false;
        }
        if( numberofLanes.equals("null")){
            //System.out.println("11");
            return false;
        }
        if( numberofLanes.equals("")){
            //System.out.println("12");
            return false;
        }
        //System.out.println("valid");
        return true;
    }
    
}
