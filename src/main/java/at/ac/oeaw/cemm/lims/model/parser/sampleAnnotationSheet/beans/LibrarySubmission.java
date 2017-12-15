/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author hMueller
 */
public class LibrarySubmission implements Serializable {
    private String libraryName = "";
    private String libraryLabel = "";
    private String sampleName = "";
    private String barcodeSequencei7 = "";
    private String libraryAdapteri7 = "";
    private String barcodeSequencei5 = "";
    private String libraryAdapteri5 = "";
    private String sequencingPrimerType = "";
    private String customSequencingPrimerName = "";
    private String customSequencingPrimerSequence = "";
    private String libraryType = "";
    private String libraryKits = "";
    private String libraryDetails = "";
    private String libraryPerson = "";
    private Date libraryDate;
    private String libraryVolume = "";
    private String libraryDNAConcentration = "";
    private String libraryTotalSize = "";
    private String libraryInsertSize = "";
    private String libraryComment = "";
    private String additionalComment = "";
    private String bioinformaticsProtocol = "";
    private String bioinformaticsGenome = "";
    private String bioinformaticsGermlineControl = "";
    private String bioinformaticsComment = "";
    private String libraryDNAAmount = "";
    private Integer librarySubmissionId;

    public LibrarySubmission(ArrayList<String> row, ArrayList<Integer> indices) {
        //System.out.println("init SampleSubmission");
        if(indices.size() >= 27){
            if(!row.get(indices.get(0)).equals("null")){
                if(indices.get(0) > -1 && row.size() > indices.get(0)){
                    libraryName = row.get(indices.get(0));
                    //System.out.println(libraryName);
                }
                if(indices.get(1) > -1 && row.size() > indices.get(1)){
                    libraryLabel  = row.get(indices.get(1));
                    //System.out.println(libraryLabel);
                }
                if(indices.get(2) > -1 && row.size() > indices.get(2)){
                    sampleName = row.get(indices.get(2));
                    //System.out.println(sampleName);
                }
                if(indices.get(3) > -1 && row.size() > indices.get(3)){
                    barcodeSequencei7 = row.get(indices.get(3));
                }
                if(indices.get(4) > -1 && row.size() > indices.get(4)){
                    libraryAdapteri7 = row.get(indices.get(4));
                }
                if(indices.get(5) > -1 && row.size() > indices.get(5)){
                    barcodeSequencei5 = row.get(indices.get(5));
                    if(barcodeSequencei5.equals("null")){
                        barcodeSequencei5 = "";
                    }
                }
                if(indices.get(6) > -1 && row.size() > indices.get(6)){
                    libraryAdapteri5 = row.get(indices.get(6));
                }
                if(indices.get(7) > -1 && row.size() > indices.get(7)){
                    sequencingPrimerType = row.get(indices.get(7));
                }
                if(indices.get(8) > -1 && row.size() > indices.get(8)){
                    customSequencingPrimerName = row.get(indices.get(8));
                }
                if(indices.get(9) > -1 && row.size() > indices.get(9)){
                    customSequencingPrimerSequence = row.get(indices.get(9));
                }
                if(indices.get(10) > -1 && row.size() > indices.get(10)){                    
                    libraryType = row.get(indices.get(10));
                }
                if(indices.get(11) > -1 && row.size() > indices.get(11)){
                    libraryKits = row.get(indices.get(11));
                }
                if(indices.get(12) > -1 && row.size() > indices.get(12)){
                    libraryDetails = row.get(indices.get(12));
                }
                if(indices.get(13) > -1 && row.size() > indices.get(13)){
                    libraryPerson = row.get(indices.get(13));
                }
                if(indices.get(14) > -1 && row.size() > indices.get(14)){
                    //libraryDate = row.get(indices.get(15));
                    libraryDate = new Date(System.currentTimeMillis());
                }
                if(indices.get(15) > -1 && row.size() > indices.get(15)){
                    libraryVolume = removeUnits(row.get(indices.get(15)));
                }  
                if(indices.get(16) > -1 && row.size() > indices.get(16)){
                    libraryDNAConcentration = removeUnits(row.get(indices.get(16)));
                }  
                if(indices.get(17) > -1 && row.size() > indices.get(17)){
                    libraryTotalSize = removeUnits(row.get(indices.get(17)));
                }  
                if(indices.get(18) > -1 && row.size() > indices.get(18)){
                    libraryInsertSize = removeUnits(row.get(indices.get(18)));
                }  
                if(indices.get(19) > -1 && row.size() > indices.get(19)){
                    libraryComment = row.get(indices.get(19));
                }  
                if(indices.get(20) > -1 && row.size() > indices.get(20)){
                    additionalComment = row.get(indices.get(20));
                }  
                if(indices.get(21) > -1 && row.size() > indices.get(21)){
                    //System.out.println(indices.get(22));
                    bioinformaticsProtocol = row.get(indices.get(21));
                } 
                if(indices.get(22) > -1 && row.size() > indices.get(22)){
                    bioinformaticsGenome = row.get(indices.get(22));
                } 
                if(indices.get(23) > -1 && row.size() > indices.get(23)){
                    bioinformaticsGermlineControl = row.get(indices.get(23));
                } 
                if(indices.get(24) > -1 && row.size() > indices.get(24)){
                    bioinformaticsComment = row.get(indices.get(24));
                } 
                if(indices.get(25) > -1 && row.size() > indices.get(25)){
                    libraryDNAAmount = removeUnits(row.get(indices.get(25)));
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

  
    public String getLibraryDateAsString() {
        DateFormat df = new SimpleDateFormat("dd.MM.YYYY");
        String result = df.format(libraryDate);
        return result;
        //return libraryDate;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibraryLabel() {
        return libraryLabel;
    }

    public String getSampleName() {
        return sampleName;
    }

    public String getBarcodeSequencei7() {
        return barcodeSequencei7;
    }

    public String getLibraryAdapteri7() {
        return libraryAdapteri7;
    }

    public String getBarcodeSequencei5() {
        return barcodeSequencei5;
    }

    public String getLibraryAdapteri5() {
        return libraryAdapteri5;
    }

    public String getSequencingPrimerType() {
        return sequencingPrimerType;
    }

    public String getCustomSequencingPrimerName() {
        return customSequencingPrimerName;
    }

    public String getCustomSequencingPrimerSequence() {
        return customSequencingPrimerSequence;
    }

    public String getLibraryType() {
        return libraryType;
    }

    public String getLibraryKits() {
        return libraryKits;
    }

    public String getLibraryDetails() {
        return libraryDetails;
    }

    public String getLibraryPerson() {
        return libraryPerson;
    }

    public Date getLibraryDate() {
        return libraryDate;
    }

    public String getLibraryVolume() {
        return libraryVolume;
    }

    public String getLibraryDNAConcentration() {
        return libraryDNAConcentration;
    }

    public String getLibraryTotalSize() {
        return libraryTotalSize;
    }

    public String getLibraryInsertSize() {
        return libraryInsertSize;
    }

    public String getLibraryComment() {
        return libraryComment;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public String getBioinformaticsProtocol() {
        return bioinformaticsProtocol;
    }

    public String getBioinformaticsGenome() {
        return bioinformaticsGenome;
    }

    public String getBioinformaticsGermlineControl() {
        return bioinformaticsGermlineControl;
    }

    public String getBioinformaticsComment() {
        return bioinformaticsComment;
    }

    public String getLibraryDNAAmount() {
        return libraryDNAAmount;
    }

    public Integer getLibrarySubmissionId() {
        return librarySubmissionId;
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
        if( libraryLabel == null){
            //System.out.println("4");
            return false;
        }
        if( libraryLabel.equals("null")){
            //System.out.println("5");
            return false;
        }
        if( libraryLabel.equals("")){
            //System.out.println("6");
            return false;
        }
        if( sampleName == null){
            //System.out.println("7");
            return false;
        }
        if( sampleName.equals("")){
            //System.out.println("8");
            return false;
        }
        if( sampleName.equals("null")){
            //System.out.println("9");
            return false;
        }
        
        //System.out.println("valid");
        return true;
    }
    
}
