/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.NewsDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import java.util.Date;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;

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
    public LibraryDTO getLibraryDTO(String libraryName) {
        return new LibraryDTOImpl(libraryName);
    }
    
    @Override
    public RequestDTO getRequestDTO(String requestor){
        return new RequestDTOImpl(requestor);
    }
    
    @Override
    public SampleDTO getSampleDTO(Integer id){
        return new SampleDTOImpl(id);
    }
    
    @Override
    public UserDTO getUserDTO(Integer id, String userName, String login, String phone, String mailAddress, Integer pi, String userRole){
        return new UserDTOImpl(id, userName, login, phone, mailAddress, pi, userRole);
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
    public NewsDTO createEmptyNews() {
        return new NewsDTOImpl(null,"","",new Date());
    }
}
