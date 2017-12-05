/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;


import java.util.Date;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public interface DTOFactory {

    ApplicationDTO getApplicationDTO(String applicationName);

    ApplicationDTO getApplicationDTO(String applicationName, String instrument);

    ApplicationDTO getApplicationDTO(Integer readLength, String readMode, String instrument, String applicationName, Integer depth);

    IndexDTO getIndexDTO(String index);

    LibraryDTO getLibraryDTO(String libraryName, Integer Id);

    NewsDTO getNewsDTO(Integer id, String header, String body, Date date);

    RequestDTO getRequestDTO(UserDTO requestor, Integer requestId);

    SampleDTO getSampleDTO(Integer id);

    SampleDTO getSampleDTO(Integer id, ApplicationDTO application, String organism, String type, String antibody, Boolean syntehsisNeeded, Double concentration, Double totalAmount, Double bulkFragmentSize, String costcenter, String status, String name, String comment, String description, Date requestDate, Date bioanalyzerDate, Double bioAnalyzerMolarity, Integer submissionId, String experimentName, IndexDTO index, UserDTO user);

    SampleRunDTO getSampleRunDTO(Integer id, SampleDTO sample, UserDTO operator, String flowCell, Set<String> lanes, String runFolder, Boolean isControl);

    UserDTO getUserDTO(Integer id, String userName, String login, String phone, String mailAddress, Integer pi, String userRole);
    
    RunDTO getRunDTO(Integer id, String flowcell, UserDTO operator, String runFolder);

    public NewsDTO createEmptyNews();
    
}
