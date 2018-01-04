/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryToRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.NewsDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import java.util.Date;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;
import at.ac.oeaw.cemm.lims.api.dto.lims.RunDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class DTOFactoryImpl implements DTOFactory {
    
    @Override
    public ApplicationDTO getApplicationDTO(String applicationName) {
        return new ApplicationDTOImpl(applicationName,null);
    }
    
    @Override
    public ApplicationDTO getApplicationDTO(String applicationName,String instrument) {
        return new ApplicationDTOImpl(applicationName,null);
    }
    
    @Override
    public ApplicationDTO getApplicationDTO(Integer readLength, String readMode, String instrument, String applicationName, Integer depth) {
        return new ApplicationDTOImpl(readLength,readMode,instrument,applicationName,depth);
    }
    
    
    @Override
    public IndexDTO getIndexDTO(String index){
        return new IndexDTOImpl(index);
    }
    
    @Override
    public LibraryDTO getLibraryDTO(String libraryName,Integer id) {
        return new LibraryDTOImpl(libraryName,id);
    }
    
    @Override
    public RequestDTO getRequestDTO(UserDTO requestor,Integer requestId){
        return new RequestDTOImpl(requestor,requestId);
    }
    
    @Override
    public SampleDTO getSampleDTO(Integer id){
        return new SampleDTOImpl(id);
    }
    
    @Override
    public UserDTO getUserDTO(Integer id, String userName, String login, String phone, String mailAddress, Integer pi, String userRole, AffiliationDTO affiliation){
        return new UserDTOImpl(id, userName, login, phone, mailAddress, pi, userRole, affiliation);
    }
    
    @Override
    public SampleDTO getSampleDTO(
            Integer id,
            ApplicationDTO application,
            String organism,
            String type, 
            String antibody,
            Boolean syntehsisNeeded, 
            Double concentration, 
            Double totalAmount,
            Double bulkFragmentSize,
            String costcenter,
            String status, 
            String name, 
            String comment,
            String description,
            Date requestDate, 
            Date bioanalyzerDate, 
            Double bioAnalyzerMolarity,
            Integer submissionId, 
            String experimentName,
            IndexDTO index,
            UserDTO user){
        
        return new SampleDTOImpl(id,
                application,
                organism,
                type,
                antibody,
                syntehsisNeeded,
                concentration,
                totalAmount, 
                bulkFragmentSize, 
                costcenter, 
                status, 
                name, 
                comment, 
                description, 
                requestDate, 
                bioanalyzerDate, 
                bioAnalyzerMolarity, 
                submissionId, 
                experimentName, 
                index,
                user);
        
    }
    
    @Override
    public SampleRunDTO getSampleRunDTO(Integer id, SampleDTO sample, UserDTO operator, String flowCell, Set<String> lanes, String runFolder,Boolean isControl){     
        return new SampleRunDTOImpl(id, sample,operator, flowCell, lanes, runFolder,isControl);
    }
    
    @Override
    public NewsDTO getNewsDTO(Integer id, String header, String body, Date date){
        return new NewsDTOImpl(id,header,body,date);
    }
    
     @Override
    public RunDTO getRunDTO(Integer id, String flowcell, UserDTO operator, String runFolder){
        return new MinimalRunDTOImpl(id,flowcell,operator, runFolder);
    }

    @Override
    public NewsDTO createEmptyNews() {
        return new NewsDTOImpl(null,"","",new Date());
    }

    @Override
    public RequestDTO getRequestDTO(RequestFormDTO requestForm) {
        RequestDTO requestToLims = getRequestDTO(requestForm.getRequestor().getUser(), requestForm.getRequestId());
        for (RequestLibraryDTO requestLibrary : requestForm.getLibraries()) {
            LibraryDTO library = getLibraryDTO(requestLibrary.getName(), null);
            ApplicationDTO application = getApplicationDTO(
                    requestLibrary.getReadLength(), 
                    requestLibrary.getReadMode(), 
                    "HiSeq2000", 
                    requestLibrary.getApplicationName(),30);
            for (RequestSampleDTO requestSample : requestLibrary.getSamples()) {
                SampleDTO sample =   getSampleDTO(null);
                sample.setSubmissionId(requestForm.getRequestId());
                sample.setLibraryName(library.getName());
                
                sample.setName(requestSample.getName());
                sample.setDescription(requestSample.getSampleDescription());
                sample.setComment("");
                
                sample.setStatus(SampleDTO.status_requested);
                
                sample.setUser(requestToLims.getRequestorUser());
                sample.setCostcenter(requestForm.getRequestor().getPi().getUserName().replace(",", ""));

                
                sample.setApplication(application);                
                sample.setIndex(getIndexDTO(requestSample.getCompoundIndex()));
                            sample.setName(requestSample.getName());
                            
                sample.setOrganism(requestSample.getOrganism());
                sample.setConcentration(requestLibrary.getDnaConcentration());
                sample.setTotalAmount(0.0);
                sample.setBulkFragmentSize(requestLibrary.getTotalSize());
                
                sample.setRequestDate(requestForm.getDate());
                sample.setBioanalyzerDate(requestForm.getDate());
                
                sample.setAntibody("");
                sample.setType("");
                sample.setSyntehsisNeeded(true);
                sample.setBioAnalyzerMolarity(0.0);
                
                library.addSample(sample);
            }
            
            requestToLims.addOrGetLibrary(library);
        }
        
        return requestToLims;
    }
    
    @Override
    public AffiliationDTO getAffiliationDTO(OrganizationDTO organization, DepartmentDTO department) {
        return new AffiliationDTOImpl(organization,department);
    }

    @Override
    public OrganizationDTO getOrganizationDTO(String name) {
        return new OrganizationDTOImpl(name, "", "");
    }

    @Override
    public DepartmentDTO getDepartmentDTO(String departmentName) {
        return new DepartmentDTOImpl(departmentName); 
    }

    @Override
    public AffiliationDTO getAffiliationDTO() {
        return new AffiliationDTOImpl(new OrganizationDTOImpl(),new DepartmentDTOImpl());
    }
    
    @Override
    public LibraryToRunDTO getLibraryToRun(LibraryDTO library, UserDTO requestor, Integer requestId, String readMode, Integer readLength ){
        return new LibraryToRunDTOImpl(library, requestor, requestId, readMode, readLength);
    }
}
