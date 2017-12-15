/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParserConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * to do: date parsing for different locales
 * @author hMueller
 * 
 */
public class SubmissionSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    private String submissionName = "";
    private String submissionTitle = "";
    private String submissionContext = "";
    private String submissionDescription = "";
    private String submissionComment = "";
    private Date submissionDate;// = new Date(System.currentTimeMillis());
    private String submissionTemplate = "";
    private String relatedPublications = "";
    private String relatedDatasets = "";
    private String submittingOrganizationName = "";
    private String submittingOrganizationAddress = "";
    private String submittingOrganizationURL = "";
    private String submittingOrganizationUID = "";
    private String contactPersonName = "";
    private String contactPersonEmail = "";
    private String contactPersonPhone = "";
    private String labURL = "";
    private String labHeadContact = "";
    private String labAdministrativeContact = "";
    private String labExperimentalContact = "";
    private String labBioinformaticsContact = "";
    private String submittingOrganizationDepartment = "";
    private String billingContact = "";
    private String billingAddress = "";
    private String billingCode = "";
    private Integer submissionSummaryId = -1;
    private String submissionDateString = "";

     public SubmissionSummary(ArrayList<ArrayList<String>> rows) {
        submissionName = findValueForKey(rows, ExcelParserConstants.SubmissionName);
        submissionTitle = findValueForKey(rows, ExcelParserConstants.SubmissionTitle);
        submissionContext = findValueForKey(rows, ExcelParserConstants.SubmissionContext);
        submissionDescription = findValueForKey(rows, ExcelParserConstants.SubmissionDescription);
       
        submissionComment = findValueForKey(rows, ExcelParserConstants.SubmissionComment);
        submissionDateString = findValueForKey(rows, ExcelParserConstants.SubmissionDate);
        submissionTemplate = findValueForKey(rows, ExcelParserConstants.SubmissionTemplate);

        relatedPublications = findValueForKey(rows, ExcelParserConstants.RelatedPublications);
        relatedDatasets = findValueForKey(rows, ExcelParserConstants.RelatedDatasets);

        contactPersonName = findValueForKey(rows, ExcelParserConstants.ContactPersonName);
        contactPersonEmail = findValueForKey(rows, ExcelParserConstants.ContactPersonEmail);
        contactPersonPhone = findValueForKey(rows, ExcelParserConstants.ContactPersonPhone);

        submittingOrganizationDepartment = findValueForKey(rows, ExcelParserConstants.SubmittingOrganizationDepartment);
        submittingOrganizationName = findValueForKey(rows, ExcelParserConstants.SubmittingOrganizationName);
        submittingOrganizationName = submittingOrganizationName.replaceAll(",", " ");
        submittingOrganizationAddress = findValueForKey(rows, ExcelParserConstants.SubmittingOrganizationAddress);
        submittingOrganizationURL = findValueForKey(rows, ExcelParserConstants.SubmittingOrganizationURL);
        submittingOrganizationUID = findValueForKey(rows, ExcelParserConstants.SubmittingOrganizationUID);
        billingContact = findValueForKey(rows, ExcelParserConstants.BillingContact);   
        billingAddress = findValueForKey(rows, ExcelParserConstants.BillingAddress);                    
        billingCode = findValueForKey(rows, ExcelParserConstants.BillingCode);                     

        labHeadContact = findValueForKey(rows, ExcelParserConstants.LabHeadContact);                     
        labAdministrativeContact = findValueForKey(rows, ExcelParserConstants.LabAdministrativeContact);         
        labExperimentalContact = findValueForKey(rows, ExcelParserConstants.LabExperimentalContact);            
        labBioinformaticsContact = findValueForKey(rows, ExcelParserConstants.LabBioinformaticsContact);          
        labURL = findValueForKey(rows, ExcelParserConstants.LabURL); 
               
    }

    
    private String findValueForKey(ArrayList<ArrayList<String>> rows, String key){
        for (ArrayList<String> row : rows) {
            if (row.contains(key)) {//use startsWith to handle trailung null values (cells that appear empty but have had some value in it)
                //System.out.println(row);
                
                if(row.size() > 1 && row.get(1) != null && !row.get(1).equals("null")){
                    return row.get(1);
                }
            }
        }
        return "";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSubmissionName() {
        return submissionName;
    }

    public String getSubmissionTitle() {
        return submissionTitle;
    }

    public String getSubmissionContext() {
        return submissionContext;
    }

    public String getSubmissionDescription() {
        return submissionDescription;
    }

    public String getSubmissionComment() {
        return submissionComment;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public String getSubmissionTemplate() {
        return submissionTemplate;
    }

    public String getRelatedPublications() {
        return relatedPublications;
    }

    public String getRelatedDatasets() {
        return relatedDatasets;
    }

    public String getSubmittingOrganizationName() {
        return submittingOrganizationName;
    }

    public String getSubmittingOrganizationAddress() {
        return submittingOrganizationAddress;
    }

    public String getSubmittingOrganizationURL() {
        return submittingOrganizationURL;
    }

    public String getSubmittingOrganizationUID() {
        return submittingOrganizationUID;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public String getLabURL() {
        return labURL;
    }

    public String getLabHeadContact() {
        return labHeadContact;
    }

    public String getLabAdministrativeContact() {
        return labAdministrativeContact;
    }

    public String getLabExperimentalContact() {
        return labExperimentalContact;
    }

    public String getLabBioinformaticsContact() {
        return labBioinformaticsContact;
    }

    public String getSubmittingOrganizationDepartment() {
        return submittingOrganizationDepartment;
    }

    public String getBillingContact() {
        return billingContact;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getBillingCode() {
        return billingCode;
    }

    public Integer getSubmissionSummaryId() {
        return submissionSummaryId;
    }

    public String getSubmissionDateString() {
        return submissionDateString;
    }
    
    
}
