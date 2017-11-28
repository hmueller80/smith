package at.ac.oeaw.cemm.lims.view.upload;

import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.model.parser.runCSV.SampleRunsBuilder;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "uploadRunFormNewBean")
@SessionScoped
public class UploadRunFormNewBean implements Serializable {

    private final static String FORM_ID = "sampleTableUploadProcessForm";
    private final static String COMPONENT = "RequestUploadProcessButton";


    @ManagedProperty("#{newRoleManager}")
    protected NewRoleManager roleManager;

    @Inject private ServiceFactory services;
    @Inject private SampleRunsBuilder runBuilder;
    
    private String destination;
    private String filename;

    public UploadRunFormNewBean() {
        System.out.println("Initializing UploadRunFormNewBean");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("UploadRunFormNewBean post construct");

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            String applicationPath = context.getExternalContext().getRealPath("/");
            destination = applicationPath + "upload" + File.separator;          
        } catch (UnsupportedOperationException uoe) {
            uoe.printStackTrace();
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("uploading");
        UploadedFile file = event.getFile();
        if (file != null) {
            FacesMessage msg = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        System.out.println("getting data");
        transferFile(file);
    }
    
    private void transferFile(UploadedFile file) {
        //writes uploaded file to upload directory
        //String fileName = file.getFileName();
        String fileName = (new File(file.getFileName())).getName();
        filename = fileName;
        try {
            InputStream in = file.getInputstream();
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int reader = 0;
            byte[] bytes = new byte[(int) file.getSize()];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void submitSampleRun() {
        if (roleManager.getHasRunAddPermission()) {
            ValidatedCSV<Set<SampleRunDTO>> parsedCSV = runBuilder.buildSampleRunsFromCSV(new File(destination + filename), services, roleManager.getCurrentUser());

            System.out.println("---------Parsed file " + filename + "----------");
            System.out.println("Is Valid: " + !parsedCSV.getValidationStatus().isFailed());
            System.out.println("Errors");
            for (ParsingMessage message : parsedCSV.getValidationStatus().getFailMessages()) {
                System.out.println(message.getSummary() + ":" + message.getMessage());
            }
            System.out.println("Warnings");
            for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
                System.out.println(message.getSummary() + ":" + message.getMessage());
            }

            if (parsedCSV.getValidationStatus().isFailed()) {
                for (ParsingMessage message : parsedCSV.getValidationStatus().getFailMessages()) {
                    NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, message.getSummary(), message.getMessage());
                    System.out.println("Failed validation");
                }
            } else {
                Set<SampleRunDTO> sampleRuns = parsedCSV.getRequestObj();

                try {
                    Set<PersistedEntityReceipt> receipts = services.getRunService().bulkUploadRuns(sampleRuns);

                    for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
                        NgsLimsUtility.setWarningMessage(FORM_ID, COMPONENT, message.getSummary(), message.getMessage());
                    }
                   NgsLimsUtility.setSuccessMessage(FORM_ID, COMPONENT, "Success", "Run form uploaded correctly");

                } catch (Exception e) {
                    NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "Error while persisting request", e.getMessage());
                    System.out.println("Failed upload to DB");
                    e.printStackTrace();
                }
            }
        } else {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "User role error", "You do not have permission to submit runs.");
        }
    }
    

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public ServiceFactory getServices() {
        return services;
    }

    public void setServices(ServiceFactory services) {
        this.services = services;
    }

    public String getFilename() {
        return filename;
    }


}
