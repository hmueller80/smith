/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.service.LazySampleService;
import at.ac.oeaw.cemm.lims.service.UserService;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.sampleBeans.SampleSpecDesc;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@SessionScoped
public class SingleSampleBean implements Serializable {

    @ManagedProperty(value = "#{lazySampleService}")
    private LazySampleService sampleService;
    
    @ManagedProperty(value = "#{userService}")
    private UserService userService;
    
    private Sample currentSample = null;
    private User principalInvestigator = null;
    private List<SampleSpecDesc> possibleIndexes = new LinkedList<>();
    
    public String loadId() {
        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        Integer sampleID = Integer.parseInt(sid);
        
        currentSample = sampleService.getFullSampleById(sampleID);
        principalInvestigator = userService.getUserByID(currentSample.getUser().getPi());
        for (String index: sampleService.getAllIndexes()){
            possibleIndexes.add(new SampleSpecDesc(index,"none","none"));
        }
        
        return "/Sample/sampleDetails_1?faces-redirect=true";
    }
    
    public String getSampleName() { return currentSample.getName();}
    public Integer getSampleID() { return currentSample.getId();}
    public String getUserLogin() { return currentSample.getUser().getLogin();}
    public String getUserName() { return currentSample.getUser().getUserName();}
    public String getUserEmail() { return currentSample.getUser().getMailAddress();}
    public String getUserTel() { return currentSample.getUser().getPhone();}
    public String getPiLogin() { return principalInvestigator.getLogin();}
    public String getApplicationName() { return emptyIfNull(currentSample.getApplication().getApplicationname());}
    public String getReadMode() { return currentSample.getApplication().getReadmode();}
    public Integer getReadLength() { return currentSample.getApplication().getReadlength();}
    public Integer getDepth() { return currentSample.getApplication().getDepth();}
    public Boolean getLibrarySynthesis() { return currentSample.getLibrarySynthesisNeeded();}
    public String getSequencingIndex() { return emptyIfNull(currentSample.getSequencingIndexes().getIndex());}
    public List<SampleSpecDesc> getPossibleIndexes() { return possibleIndexes;}
    public String getCostCenter() { return currentSample.getCostCenter();}
    public String getInstrument() { return currentSample.getApplication().getInstrument();}
    public Date getBioDate() { return currentSample.getBioanalyzerDate();}
    public Double getBioMolarity() { return currentSample.getBionalyzerBiomolarity();}
    public String getSampleType() { return currentSample.getType();}
    public String getOrganism() { return currentSample.getOrganism();}
    public Double getSampleConcentration() { return currentSample.getConcentration();}
    public Double getTotalAmount() { return currentSample.getTotalAmount();}
    public Double getBulkFragmentSize() { return currentSample.getBulkFragmentSize();}
    public String getAntibody() { return currentSample.getAntibody();}
    public String getSampleDescription() { return currentSample.getDescription();}
    public String getComments() { return currentSample.getComment();}
    public String getStatus() { return currentSample.getStatus();}

    public LazySampleService getSampleService() {
        return sampleService;
    }

    public void setSampleService(LazySampleService sampleService) {
        this.sampleService = sampleService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    private String emptyIfNull(String toCheck){
        return toCheck == null ? "" : toCheck;
    }

}
