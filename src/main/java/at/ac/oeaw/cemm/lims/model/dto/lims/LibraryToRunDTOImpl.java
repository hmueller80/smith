/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryToRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class LibraryToRunDTOImpl implements LibraryToRunDTO{
    private final LibraryDTO library;
    private final UserDTO requestor;
    private final Integer requestId;
    private final String readMode;
    private final Integer readLength;
    private Set<Integer> lanes = new HashSet<>();
    
    protected LibraryToRunDTOImpl(LibraryDTO library, UserDTO requestor, Integer requestId, String readMode, Integer readLength) {
        this.library = library;
        this.requestor = requestor;
        this.requestId = requestId;
        this.readMode = readMode;
        this.readLength = readLength;
    }

    @Override
    public LibraryDTO getLibrary() {
        return library;
    }

    @Override
    public UserDTO getRequestor() {
        return requestor;
    }

    @Override
    public Integer getRequestId() {
        return requestId;
    }

    @Override
    public String getReadMode() {
        return readMode;
    }

    @Override
    public Integer getReadLength() {
        return readLength;
    }

    @Override
    public String getLanes() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Integer lane: lanes){
            if (!isFirst) {
                sb.append(" ");
            }else{
                isFirst = false;
            }
            sb.append(lane);
        }
        
        return sb.toString();
    }

    @Override
    public void setLanes(String lanes) {
        this.lanes = new HashSet<>();
        splitRecursively(new String[]{lanes});          
    }
    
    private void splitRecursively(String[] toSplit){
        for (String subString: toSplit){
            if (subString.contains(",")){
                splitRecursively(subString.split(","));
            }else if (subString.contains(";")){
                splitRecursively(subString.split(";"));
            }else if (subString.contains(" ")){
                splitRecursively(subString.split(" "));
            }else {
                try{
                    lanes.add(Integer.parseInt(subString));
                }catch(NumberFormatException e) {}
            }
        }
    }

    @Override
    public Set<String> getLanesSet() {
        Set<String> toReturn = new HashSet<>();
        for (Integer lane : lanes){
            toReturn.add(String.valueOf(lane));
        }
        return toReturn;
    }

}
