/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author hMueller
 */
public class SampleSubmission implements Serializable {
    private String sampleName = "undefined";
    private String sampleDescription = "";
    private String organism = "";
    private String sex = "";
    private String age = "";
    private String tissue = "";
    private String cellType = "";
    private String genotype = "";
    private String familyRelations = "";
    private String phenotype = "";
    private String disease = "";
    private String materialType = "";
    private String source = "";
    private String acquisitionDate = "";
    private String sampleGroup = "";
    private String originalSampleID = "";
    private Integer sampleSubmissionId;

    
    public SampleSubmission(ArrayList<String> row, ArrayList<Integer> indices) {
        if(indices.size() >= 17){
            if(!row.get(indices.get(0)).equals("null")){
                
                if(indices.get(0) > -1 && row.size() > indices.get(0)){
                    sampleName = row.get(indices.get(0));
                }
                if(indices.get(1) > -1 && row.size() > indices.get(1)){
                    sampleDescription  = row.get(indices.get(1));
                }
                if(indices.get(2) > -1 && row.size() > indices.get(2)){
                    organism = row.get(indices.get(2));
                }
                if(indices.get(3) > -1 && row.size() > indices.get(3)){
                    sex = row.get(indices.get(3));
                }
                if(indices.get(4) > -1 && row.size() > indices.get(4)){
                    age = row.get(indices.get(4));
                }
                if(indices.get(5) > -1 && row.size() > indices.get(5)){
                    tissue = row.get(indices.get(5));
                }
                if(indices.get(6) > -1 && row.size() > indices.get(6)){
                    cellType = row.get(indices.get(6));
                }
                if(indices.get(7) > -1 && row.size() > indices.get(7)){
                    genotype = row.get(indices.get(7));
                }
                if(indices.get(8) > -1 && row.size() > indices.get(8)){
                    familyRelations = row.get(indices.get(8));
                }
                if(indices.get(9) > -1 && row.size() > indices.get(9)){
                    phenotype = row.get(indices.get(9));
                }
                if(indices.get(10) > -1 && row.size() > indices.get(10)){                    
                    disease = row.get(indices.get(10));
                }
                if(indices.get(11) > -1 && row.size() > indices.get(11)){
                    materialType = row.get(indices.get(11));
                }
                if(indices.get(12) > -1 && row.size() > indices.get(12)){
                    source = row.get(indices.get(12));
                }
                if(indices.get(13) > -1 && row.size() > indices.get(13)){
                    acquisitionDate = row.get(indices.get(13));
                }
                if(indices.get(14) > -1 && row.size() > indices.get(14)){
                    sampleGroup = row.get(indices.get(14));
                }
                if(indices.get(15) > -1 && row.size() > indices.get(15)){
                    originalSampleID = row.get(indices.get(15));
                }               

            }else{
                System.out.println("null 0");
            }
        }
    }

    public String getSampleName() {
        return sampleName;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public String getOrganism() {
        return organism;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getTissue() {
        return tissue;
    }

    public String getCellType() {
        return cellType;
    }

    public String getGenotype() {
        return genotype;
    }

    public String getFamilyRelations() {
        return familyRelations;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public String getDisease() {
        return disease;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getSource() {
        return source;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public String getSampleGroup() {
        return sampleGroup;
    }

    public String getOriginalSampleID() {
        return originalSampleID;
    }

    public Integer getSampleSubmissionId() {
        return sampleSubmissionId;
    }

    
    public boolean siValid(){
        if( sampleName == null){
            System.out.println("1");
            return false;
        }
        if( sampleName.equals("")){
            System.out.println("2");
            return false;
        }
        if( sampleName.equals("null")){
            System.out.println("3");
            return false;
        }
        if( organism == null){
            System.out.println("4");
            return false;
        }
        if( organism.equals("null")){
            System.out.println("5");
            return false;
        }
        if( organism.equals("")){
            System.out.println("6");
            return false;
        }        

        return true;
    }
    
}
