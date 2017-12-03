package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;

public class SingleRunTableRow {
	private final String lane;
	private final String libraryName;
	private final SampleDTO sample;
	public SingleRunTableRow(String lane, String libraryName, SampleDTO sample) {
		super();
		this.lane = lane;
		this.libraryName = libraryName;
		this.sample = sample;
	}
	public String getLane() {
		return lane;
	}
	public String getLibraryName() {
		return libraryName;
	}
	public SampleDTO getSample() {
		return sample;
	}

	
}
