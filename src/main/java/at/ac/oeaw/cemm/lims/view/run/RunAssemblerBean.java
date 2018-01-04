/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryToRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.util.Preferences;
import at.ac.oeaw.cemm.lims.util.RunIdBean;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "runAssemblerBean")
@ViewScoped
public class RunAssemblerBean {

    @Inject
    private ServiceFactory services;

    @Inject
    private DTOFactory myDTOFactory;
    
    @Inject private RunIdBean runIdBean;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    private List<LibraryToRunDTO> libraries;
    private List<LibraryToRunDTO> filteredLibraries;
    private List<LibraryToRunDTO> selectedLibraries;
    private List<RunFolder> runFolders;
    private List<LaneSampleRow> samplesToRun = new LinkedList<>();
    private RunFolder selectedFolder;
    
    private String wizardStep = "librarySelect";
    
    Set<String> readModes;
    Set<Integer> readLengths;
    
    @PostConstruct
    public void init(){
        libraries = services.getRequestService().getRunnableLibraries();
        readModes = new HashSet<>();
        readLengths = new HashSet<>();
        for(LibraryToRunDTO library: libraries){
            readModes.add(library.getReadMode());
            readLengths.add(library.getReadLength());
        }
        
        runFolders = new ArrayList<>();
        for (File folder: new File(Preferences.getRunfolderroot()).listFiles()){
            if (folder.isDirectory() && Arrays.asList(folder.list()).contains("RunInfo.xml")){
                runFolders.add(new RunFolder(folder.getName()));
            }
        }
        
        Collections.sort(runFolders,new Comparator<RunFolder>() {
            @Override
            public int compare(RunFolder o1, RunFolder o2) {
                return -1*o1.getRunDate().compareTo(o2.getRunDate());
            }
        });
        
        selectedFolder = runFolders.get(0);
    }

    public void hasViewPermission() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!roleManager.getHasRunAddPermission()) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error401.xhtml");
        }
    }
    

    public List<LibraryToRunDTO> getLibraries() {
        return libraries;
    }


    public List<LibraryToRunDTO> getFilteredLibraries() {
        return filteredLibraries;
    }

    public Set<String> getReadModes() {
        return readModes;
    }

    public Set<Integer> getReadLengths() {
        return readLengths;
    }

    public void setFilteredLibraries(List<LibraryToRunDTO> filteredLibraries) {
        this.filteredLibraries = filteredLibraries;
    }
    
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public List<LibraryToRunDTO> getSelectedLibraries() {
        return selectedLibraries;
    }

    public void setSelectedLibraries(List<LibraryToRunDTO> selectedLibraries) {
        this.selectedLibraries = selectedLibraries;
    }
    
    public String onFlowProcess(FlowEvent event) {

        wizardStep = event.getNewStep();

        return wizardStep;
    }

    public String getWizStep() {
        return wizardStep;
    }
    
    public List<RunFolder> getRunFolders(){
        return runFolders;
    }

    public RunFolder getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(RunFolder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public List<LaneSampleRow> getSamplesToRun() {
        return samplesToRun;
    }
    
    
    public void prepareSubmission() {
        samplesToRun = new LinkedList<>();
        
        boolean failed = false;
        
        if (selectedFolder == null) {
            NgsLimsUtility.setFailMessage("uploadDialogMsg", null, "Flowcell", "No run folder selected");
            failed =true;
        } else if (selectedLibraries == null || selectedLibraries.isEmpty()) {
            NgsLimsUtility.setFailMessage("uploadDialogMsg", null, "Library", "No library selected");
            failed = true;
        } else {
            Map<String, Set<String>> indexesInLanes = new HashMap<>();
            for (LibraryToRunDTO library: selectedLibraries){
                if (library.getLanesSet()==null || library.getLanesSet().isEmpty() ){
                    NgsLimsUtility.setFailMessage("uploadDialogMsg", null, "Lanes", "No lanes associated to selected library "+library.getLibrary().getName());
                    failed = true;
                    break;
                }
                Set<String> lanes = library.getLanesSet();
                for (SampleDTO sample : library.getLibrary().getSamples()){

                    for (String lane: lanes){
                        Set<String> indexes = indexesInLanes.get(lane);
                        if (indexes == null) {
                            indexes = new HashSet<>();
                            indexesInLanes.put(lane, indexes);
                        }
                        if (indexes.contains(sample.getCompoundIndex())){
                            NgsLimsUtility.setFailMessage("uploadDialogMsg", null, "Index", "Index collision in lane "+lane+" for index "+sample.getCompoundIndex());
                            failed = true;
                            break;
                        }else{
                            indexes.add(sample.getCompoundIndex());
                        }
                    }
                    
                    if (failed) break;

                    SampleRunDTO newSampleRun = myDTOFactory.getSampleRunDTO(
                                null, 
                                sample, 
                                roleManager.getCurrentUser(), 
                                selectedFolder.getFlowCell(),
                                lanes, 
                                selectedFolder.getRunFolderName(),
                                false);

                    for (String lane: newSampleRun.getLanes()){
                        samplesToRun.add(new LaneSampleRow(newSampleRun,lane));
                    }
                }
            }
        }
        
        if (failed) {
            samplesToRun = new LinkedList<>();
        }
        
    }
    
    public String submitRequest(){
        if (roleManager.getHasRunAddPermission()){
            if (!samplesToRun.isEmpty()){
                try {
                    Integer runId = runIdBean.getNextId();
                    if (runId == null) {
                        throw new Exception("Could not get the next run Id");
                    }
                    
                    Set<SampleRunDTO> sampleRuns = new HashSet<>();
                    for (LaneSampleRow lane : samplesToRun) {
                        sampleRuns.add(lane.getSample());
                    }
                    for (SampleRunDTO sampleRun: sampleRuns){
                        sampleRun.setRunId(runId);
                    }
                    
                    Set<PersistedEntityReceipt> receipts = services.getRunService().bulkUploadRuns(sampleRuns, true);
                    return "runDetails.jsg?faces-redirect=true&rid=" + receipts.iterator().next().getId();
                } catch (Exception ex) {
                    NgsLimsUtility.setFailMessage(null, null, "Server Error", ex.getMessage());
                } finally{
                    runIdBean.unlock();
                }
            }else{
                NgsLimsUtility.setFailMessage(null, null, "Samples", "No Samples to upload");
            }
        }else{
            NgsLimsUtility.setFailMessage(null, null, "User permission", "You do not have permission to upload runs");
        }
        return null;
    }

}
