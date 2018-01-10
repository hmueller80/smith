/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.indexes;

import at.ac.oeaw.cemm.lims.api.dto.lims.BarcodeDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.dto.lims.BarcodeDTOImpl;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class IndexEditBean {
    private final String validIndexRegex = "[ATCG]+";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Pattern pattern = Pattern.compile("(.*)([0-9]+)$");

    @Inject ServiceFactory services;
    @Inject DTOFactory myDTOFactory;

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private List<BarcodeDTO> barcodes = new LinkedList<>();
    private String kitName = "";
    private boolean isNew;
    private boolean barcodesValid = true;
    private boolean nameValid = true;
    
    @PostConstruct
    public void init() {
        
        kitName = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("kitName");

        if (kitName != null) {
            barcodes= services.getIndexService().getBarcodesForKit(kitName);
            sortIndexes();
            isNew = false;
            barcodesValid = true;
            nameValid = true;
        }else{
            isNew = true;
            barcodesValid = false;
            nameValid = false;
        }
    }
    
    //PERMISSIONS
    public void hasViewPermission() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!roleManager.hasIndexPermission()) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error401.xhtml");
        }
    }
    
    //JSON HANDLING
     public String getBarcodes() {
        try {
            String output = objectMapper.writeValueAsString(barcodes);
            return output;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public void setBarcodes(String json) {
                Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("barcodesMessages");
        while (messageIterator.hasNext()) {
            messageIterator.remove();
        }
        barcodes = new LinkedList<>();
        
        try {
            barcodes = objectMapper.readValue(json, new TypeReference<List<BarcodeDTOImpl>>() {});
            validateBarcodes();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        if (kitName!=null && !kitName.isEmpty()){
            this.kitName = NameFilter.legalize(kitName);
        }else{
            this.kitName = "";
        }
        
        validateName();
    }

    public boolean isNew() {
        return isNew;
    }
    
    public String submit() {
        validateName();
        validateBarcodes();
        if (!roleManager.hasIndexPermission()){
            return null;
        }
        if (nameValid && barcodesValid){
            try {
                services.getIndexService().updateOrInsertKit(kitName,barcodes,isNew);
                NgsLimsUtility.setSuccessMessage(null, null, "Success", "Kit "+kitName+" saved correctly");
                return "indexView?faces-redirect=true";
            }catch(Exception e){
                e.printStackTrace();
                NgsLimsUtility.setFailMessage("nameMessages", null, "Server Error", e.getMessage());
            }
        }
        
        return null;
    }
    
    public String delete() {
      
        if (!roleManager.hasIndexPermission()){
            return null;
        }
        try {
            services.getIndexService().deleteKit(kitName);
            NgsLimsUtility.setSuccessMessage(null, null, "Success", "Kit " + kitName + " saved correctly");
            return "indexView?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            NgsLimsUtility.setFailMessage("nameMessages", null, "Server Error", e.getMessage());
        }
        
        return null;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    private void validateName(){
        Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("nameMessages");
        while (messageIterator.hasNext()) {
            messageIterator.remove();
        }
        
        if (kitName == null || kitName.isEmpty()){
            nameValid = false;    
            NgsLimsUtility.setFailMessage("nameMessages", null, "Kit Name", "Kit Name cannot be empty");
        }else if (isNew && services.getIndexService().kitExists(kitName)){
                nameValid = false;            
                NgsLimsUtility.setFailMessage("nameMessages", null, "Kit Name", "A kit with this name already exists");
        }else{
                nameValid = true;
        }
    }
    
    private void validateBarcodes() {
        if (barcodes.isEmpty()) {
            barcodesValid = false;
            if (!isNew) {
                setInvalidMessage("Empty list", "To delete the kit, please go to the barcode menu");
            }
        } else {
            Set<String> names = new HashSet<>();
            Map<IndexType, Set<String>> indexes = new HashMap<>();
            barcodesValid = true;
            for (BarcodeDTO barcode : barcodes) {

                if (barcode.getIndexName() == null || barcode.getIndexName().isEmpty()) {
                    setInvalidMessage("Index Name", "There is at least one index with empty name");
                } else if (names.contains(barcode.getIndexName())) {
                    setInvalidMessage("Index Name", "Duplicate index name " + barcode.getIndexName());
                } else {
                    names.add(barcode.getIndexName());
                }

                if (barcode.getIndex() == null) {
                    barcode.setIndex(myDTOFactory.getIndexDTO("NONE"));
                }

                if (barcode.getIndex().getType() == null) {
                    barcode.getIndex().setType(IndexType.i7);
                }

                Set<String> indexesSet = indexes.get(barcode.getIndex().getType());
                if (indexesSet == null) {
                    indexesSet = new HashSet<>();
                    indexes.put(barcode.getIndex().getType(), indexesSet);
                }

                if (!barcode.getIndex().getIndex().matches(validIndexRegex)) {
                    setInvalidMessage("Index Sequence", "Invalid index sequence " + barcode.getIndex().getIndex());
                } else if (indexesSet.contains(barcode.getIndex().getIndex())) {
                    setInvalidMessage("Index Sequence", "Duplicate index sequence " + barcode.getIndex().getIndex());
                } else {
                    indexesSet.add(barcode.getIndex().getIndex());
                }

            }
        }
    }

        
    private void setInvalidMessage(String summary, String message){
        barcodesValid = false;
        NgsLimsUtility.setFailMessage("barcodesMessages", null, summary, message);

    }
    
    private void sortIndexes() {
        Collections.sort(barcodes, new Comparator<BarcodeDTO>() {
            @Override
            public int compare(BarcodeDTO o1, BarcodeDTO o2) {
                if (o1.getKitName().equals(o2.getKitName())) {
                    if (o1.getIndex().getType().equals(o2.getIndex().getType())) {
                        Matcher matcher1 = pattern.matcher(o1.getIndexName());
                        Matcher matcher2 = pattern.matcher(o2.getIndexName());
                        if (matcher1.matches() && matcher2.matches()) {
                            String baseName1 = matcher1.group(1);
                            String baseName2 = matcher2.group(1);
                            if (baseName1.equals(baseName2)) {
                                Integer code1 = Integer.parseInt(matcher1.group(2));
                                Integer code2 = Integer.parseInt(matcher2.group(2));

                                return code1.compareTo(code2);
                            } else {
                                return baseName1.compareTo(baseName2);
                            }
                        } else {
                            return o1.getIndexName().compareTo(o2.getIndexName());
                        }
                    } else {
                        return o1.getIndex().getType().compareTo(o2.getIndex().getType());
                    }

                } else {
                    return o1.getKitName().compareTo(o2.getKitName());
                }
            }
        });
    }
}
