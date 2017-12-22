/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.user;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.UserDTOValidator;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="singleUserBean")
@ViewScoped
public class SingleUserBean {
    private final static String FORM_ID = "userPersistMessages";
    
    @Inject private ServiceFactory services;
    @Inject private DTOFactory myDTOFactory;
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
    
    @ManagedProperty(value="#{orgaBean}")
    OrgaBean orgaBean;
    
    @ManagedProperty(value="#{externalUserBean}")
    ExternalUserBean externalUser;
    
    private boolean isNew = false;
    private UserDTO currentUser;
    private UserDTO currentUserPI;
    private DualListModel<UserDTO> communications;
    private boolean isEditable;
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String uid = (String) context.getExternalContext().getRequestParameterMap().get("uid");
        Integer userId = null;
        if(uid!=null){
            userId = Integer.parseInt(uid);
        }
        
        initInternal(userId);
    }
    
    private void initInternal(Integer uid){

        if (uid != null) {
            isNew = false;
            currentUser = services.getUserService().getUserByID(uid);
            currentUserPI = currentUser;
            if (!Objects.equals(currentUser.getPi(), currentUser.getId())){
                currentUserPI = services.getUserService().getUserByID(currentUser.getPi());
            }
            if (Preferences.ROLE_GUEST.equals(currentUser.getUserRole())){
                isEditable = false;
            }else{
                isEditable = 
                        roleManager.getHasUserAddPermission()|| 
                        roleManager.getCurrentUser().getId().equals(currentUserPI.getId()) ||
                        roleManager.getCurrentUser().getId().equals(currentUser.getId());
            }
            externalUser.set(currentUser.getLogin());

        }else{
            isNew = true;
            currentUser = myDTOFactory.getUserDTO(null, "User, New", "newUser", null, null, null, null,myDTOFactory.getAffiliationDTO());
            currentUserPI = services.getUserService().getUsersByRole(Preferences.ROLE_GROUPLEADER).get(0);
            isEditable = roleManager.getHasUserAddPermission();
            
        } 
        orgaBean.set(currentUser.getAffiliation().getOrganization(),currentUser.getAffiliation().getDepartment());
        
        List<UserDTO> coll = services.getUserService().getCollaborators(currentUser);
        List<UserDTO> possibleColl = services.getUserService().getAllUsers();
        possibleColl.remove(currentUser);
        possibleColl.removeAll(coll);
        
        communications = new DualListModel<>();
        communications.setSource(possibleColl);
        communications.setTarget(coll);
        
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public UserDTO getCurrentUserPI() {
        return currentUserPI;
    }
    
    public void setCurrentUserPI(UserDTO pi) {
        currentUserPI=pi;
        currentUser.setPi(pi.getId());
    }
    
    public String getCurrentUserRole() {
        return currentUser.getUserRole();
    }
    
    public void setCurrentUserRole(String role) {
        currentUser.setUserRole(role);
        if (Preferences.ROLE_GROUPLEADER.equalsIgnoreCase(role)){
            setCurrentUserPI(currentUser);
        }
    }
    
    public List<UserDTO> getUserPIs() {
        List<UserDTO> possiblePIs;
        if (Preferences.ROLE_GROUPLEADER.equals(currentUser.getUserRole())) {
            possiblePIs = new LinkedList<>();
            possiblePIs.add(currentUser);
        } else {
            possiblePIs = services.getUserService().getUsersByRole(Preferences.ROLE_GROUPLEADER);
            possiblePIs.addAll(services.getUserService().getUsersByRole(Preferences.ROLE_ADMIN));

            Collections.sort(possiblePIs, new Comparator<UserDTO>() {
                @Override
                public int compare(UserDTO lhs, UserDTO rhs) {
                    return lhs.getLogin().compareTo(rhs.getLogin());
                }
            });
        }
        return possiblePIs;
    }
    
    public boolean isUserEditable(){
        return isEditable;
    }
    
    
    public List<String> getUserRoles() {
        List<String> possibleRoles = Preferences.ROLES;
        possibleRoles.remove(Preferences.ROLE_GUEST);
        return possibleRoles;
    }   

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public DualListModel<UserDTO> getCommunications() {
        return communications;
    }

    public void setCommunications(DualListModel<UserDTO> communications) {
        this.communications = communications;
    }

    public boolean isIsNew() {
        return isNew;
    }
    
    
    public void persist(){
        AffiliationDTO affiliation = myDTOFactory.getAffiliationDTO(orgaBean.getOrga(), orgaBean.getDept());
        currentUser.setAffiliation(affiliation);
        UserDTOValidator userValidator = new UserDTOValidator(services);
        try{
            ValidationStatus validation = userValidator.isValid(currentUser);
             for (ValidatorMessage message:validation.getValidationMessages()){
                if (ValidatorSeverity.WARNING.equals(message.getType())){
                    NgsLimsUtility.setWarningMessage(FORM_ID, null, message.getSummary(), message.getDescription());
                }
                if (ValidatorSeverity.FAIL.equals(message.getType())){
                    NgsLimsUtility.setFailMessage(FORM_ID, null, message.getSummary(), message.getDescription());
                }
            }
            
            if (validation.isValid()) {
                PersistedEntityReceipt receipt = services.getUserService().persistOrUpdateUser(currentUser, communications.getTarget(), isNew);

                NgsLimsUtility.setSuccessMessage(FORM_ID, null, "Success", "User saved");
                initInternal(receipt.getId());
            }

        }catch(Exception e){
            e.printStackTrace();
            NgsLimsUtility.setFailMessage(FORM_ID, null, "DB error", e.getMessage());
        }
    }
    
   
    public OrgaBean getOrgaBean() {
        return orgaBean;
    }

    public void setOrgaBean(OrgaBean orgaBean) {
        this.orgaBean = orgaBean;
    }

    public ExternalUserBean getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(ExternalUserBean externalUser) {
        this.externalUser = externalUser;
    }
    
    
 
}
