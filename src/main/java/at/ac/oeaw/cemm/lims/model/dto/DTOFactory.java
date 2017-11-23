/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import java.util.Date;

/**
 *
 * @author dbarreca
 */
public class DTOFactory {
    
    public static ApplicationDTO getApplicationDTO(String applicationName) {
        return new ApplicationDTOImpl(applicationName,null);
    }
    
    public static ApplicationDTO getApplicationDTO(String applicationName,String instrument) {
        return new ApplicationDTOImpl(applicationName,null);
    }
    
    public static ApplicationDTO getApplicationDTO(Integer readLength, String readMode, String instrument, String applicationName, Integer depth) {
        return new ApplicationDTOImpl(readLength,readMode,instrument,applicationName,depth);
    }
    
    
    public static IndexDTO getIndexDTO(String index){
        return new IndexDTOImpl(index);
    }
    
    public static LibraryDTO getLibraryDTO(String libraryName) {
        return new LibraryDTOImpl(libraryName);
    }
    
    public static RequestDTO getRequestDTO(String requestor){
        return new RequestDTOImpl(requestor);
    }
    
    public static SampleDTO getSampleDTO(Integer id){
        return new SampleDTOImpl(id);
    }
    
    public static UserDTO getUserDTO(Integer id, String userName, String login, String phone, String mailAddress, Integer pi, String userRole){
        return new UserDTOImpl(id, userName, login, phone, mailAddress, pi, userRole);
    }
    
    public static SampleDTO getSampleDTO(
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
}
