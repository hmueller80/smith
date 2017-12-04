package at.ac.oeaw.cemm.lims.view.upload;

import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.model.parser.runCSV.SampleRunsBuilder;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "uploadRunFormNewBean")
@ViewScoped
public class UploadRunFormNewBean implements Serializable {

    @ManagedProperty("#{newRoleManager}")
    protected NewRoleManager roleManager;

    @Inject private ServiceFactory services;
    @Inject private SampleRunsBuilder runBuilder;
    
    private String destination;
    private String filename;
    private ValidatedCSV<Set<SampleRunDTO>> parsedCSV=null;

    public ValidatedCSV<Set<SampleRunDTO>> getParsedCSV() {
        return parsedCSV;
    }
    
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
        String messageBoxComponent = "uploadDialogMsg";

        if (roleManager.getHasRunAddPermission()) {

            System.out.println("uploading");
            UploadedFile file = event.getFile();
            if (file == null) {
                NgsLimsUtility.setFailMessage(messageBoxComponent, null, "File upload", "the File is null");
                return;
            }
            System.out.println("getting data");
            try {
                transferFile(file);
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage(messageBoxComponent, null, "File transfer", "Error while transfering the file: " + ex.getMessage());
                ex.printStackTrace();
                return;
            }
            System.out.println("Parsing data");
            parsedCSV = runBuilder.buildSampleRunsFromCSV(new File(destination + filename), services, roleManager.getCurrentUser());
            //--------------LOG PARSING STATUS ------------------------------------
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
            //----------------------------------------------------------------------

            if (parsedCSV.getValidationStatus().isFailed()) {
                for (ParsingMessage message : parsedCSV.getValidationStatus().getFailMessages()) {
                    NgsLimsUtility.setFailMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                    System.out.println("Failed validation");
                }
            } else {
                NgsLimsUtility.setSuccessMessage(messageBoxComponent, null, "Parsing Success!", "");
                for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
                    NgsLimsUtility.setWarningMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                }
            }
        } else {
            NgsLimsUtility.setFailMessage(messageBoxComponent, null, "User role error", "You do not have permission to submit runs.");
        }

    }
    
    private void transferFile(UploadedFile file) throws Exception {
        //writes uploaded file to upload directory
        //String fileName = file.getFileName();
        String fileName = (new File(file.getFileName())).getName();
        filename = fileName;

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

    }

    public void submitSampleRun() {
        if (roleManager.getHasRunAddPermission()) {
         
            if (!parsedCSV.getValidationStatus().isFailed()) {

                Set<SampleRunDTO> sampleRuns = parsedCSV.getRequestObj();

                try {
                    Set<PersistedEntityReceipt> receipts = services.getRunService().bulkUploadRuns(sampleRuns,true);

                   NgsLimsUtility.setSuccessMessage(null, null, "Success", "Run form uploaded correctly");
                           
                } catch (Exception e) {
                    NgsLimsUtility.setFailMessage(null, null, "Error while persisting request", e.getMessage());
                    System.out.println("Failed upload to DB");
                    e.printStackTrace();
                }
            }else{
                NgsLimsUtility.setFailMessage(null, null, "Error while parsing the request", "Malformed CSV");
            }
        } else {
            NgsLimsUtility.setFailMessage(null, null, "User role error", "You do not have permission to submit runs.");
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
