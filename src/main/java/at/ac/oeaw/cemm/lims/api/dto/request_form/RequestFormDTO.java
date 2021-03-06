/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.generic.Request;
import at.ac.oeaw.cemm.lims.api.dto.generic.User;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author dbarreca
 */
public interface RequestFormDTO  extends Request {
    public final static String STATUS_NEW = "new";
    public final static String STATUS_ACCEPTED = "accepted";
    
    String DEFAULT_INDEX = "NONE";
    String NO_DEMUX_INDEX = "NO_DEMUX";
    String DEFAULT_LIBRARY = "DEFAULT_LIB";
    String DEFAULT_NAME = "DEFAULT_NAME";
    String DEFAULT_SUFFIX = "_LIMS";
    String INDEX_REGEXP = "[ATGCN]+|" + DEFAULT_INDEX+"|"+NO_DEMUX_INDEX;
    Pattern NAME_PATTERN = Pattern.compile("(.*)" + DEFAULT_SUFFIX + "[0-9]+");

    void addLibrary(RequestLibraryDTO library);

    Date getDate();
    
    String getDateAsString();
    
    BillingInfoDTO getBillingInfo();
    
    String getAuthorizationFileName();

    String getSampleAnnotationFileName();
    
    void setAuthorizationFileName(String fileName);
    
    void setSampleAnnotationFileName(String fileName);
    
    @Override
    public Integer getRequestId();
    
    @Override
    public User getRequestorUser();
    
    @Override
    public List<RequestLibraryDTO> getLibraries();
    
    RequestLibraryDTO getLibraryByUUID(String uuid);

    RequestorDTO getRequestor();

    void resetLibraries();
    
    public void removeEmptyLibraries();
    
    public String getStatus();
    
    public void setRequestId(Integer id);
    
}
