package at.ac.oeaw.cemm.lims.model.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.oeaw.cemm.lims.api.dto.LaneDTO;
import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;

public class LaneDTOImpl  implements LaneDTO{
	private final String name;
	private Map<String,LibraryDTO> libraries = new HashMap<>();
	
	public LaneDTOImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public void addSample(SampleDTO sample) {
		String libraryName = sample.getLibraryName();
		if (libraryName == null || libraryName.isEmpty()) libraryName = "Undefined";
		
		LibraryDTO library = libraries.get(sample.getLibraryName());
		
		if (library==null) {
			library = new LibraryDTOImpl(libraryName);
			libraries.put(library.getName(), library);
		}
		library.addSample(sample);
		
	}

	@Override	
	public Set<LibraryDTO> getLibraries(){
		return new HashSet<>(libraries.values());
	}

	@Override
	public String getName() {
		return name;
	}
	
	
}
