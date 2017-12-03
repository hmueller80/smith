package at.ac.oeaw.cemm.lims.api.dto;

import java.util.Set;


public interface LaneDTO {
	
	public void addSample(SampleDTO sample);
	
	public Set<LibraryDTO> getLibraries();
	
	public String getName();
	
}
