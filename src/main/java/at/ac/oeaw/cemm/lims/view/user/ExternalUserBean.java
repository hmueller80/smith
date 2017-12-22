/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.user;

import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="externalUserBean")
@ViewScoped
public class ExternalUserBean {
    private String password;
    private String confirmPassword;
    private String userId = null;
    
    @Inject ServiceFactory services;    
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
    
    public void set(String userId){
        this.userId = userId;
    }
    
    public boolean canResetPassword(){
        return userId!=null && roleManager.getHasUserAddPermission();
    }
    
    public boolean canSetPassword(){
        return userId!=null && roleManager.getCurrentUser().getLogin().equals(userId) && userExists();
    }
    
    public boolean userExists() {
        return userId!=null && services.getExternalUsersService().userExists(userId);
    }
    
    public String getFirstPassword() {
        if (roleManager.getHasUserAddPermission()){
            return services.getExternalUsersService().getPasswordForUser(userId);
        }
        
        return "";
    }
    
    public void resetPasswordForUser(){
        if (canResetPassword()) {
            try {
                services.getExternalUsersService().resetPasswordForUser(userId, null);
                NgsLimsUtility.setSuccessMessage("userPersistMessages", null, "Password Reset", null);
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage("userPersistMessages", null, "DB Error", ex.getMessage());
            }
        }else{
            NgsLimsUtility.setFailMessage("userPersistMessages", null, "Permission error", "You don't have permission to reset password");
        }
    }
    
      public void deleteUser(){
        if (canResetPassword()) {
            try {
                services.getExternalUsersService().deleteUser(userId);
                NgsLimsUtility.setSuccessMessage("userPersistMessages", null, "Used Deleted", null);
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage("userPersistMessages", null, "DB Error", ex.getMessage());
            }
        }else{
            NgsLimsUtility.setFailMessage("userPersistMessages", null, "Permission error", "You don't have permission to delete user");
        }
    }
    
    public void setPasswordForUser(){
        if (canSetPassword()) {
            if (isPwdValid()){
                try {
                    services.getExternalUsersService().resetPasswordForUser(userId, password);
                    NgsLimsUtility.setSuccessMessage("userPersistMessages", null, "Password set", null);
                } catch (Exception ex) {
                    NgsLimsUtility.setFailMessage("userPersistMessages", null, "DB Error", ex.getMessage());
                }
            }
        }else{
            NgsLimsUtility.setFailMessage("userPersistMessages", null, "Permission error", "You don't have permission to set password for user "+userId);
        }
    }
    
    private boolean isPwdValid() {
        if (!password.trim().equals(confirmPassword.trim())){
            NgsLimsUtility.setFailMessage("userPersistMessages", null, "Password Error", "The password and confirm password are not equal");
            return false;
        }
        
        if (password.trim().isEmpty()) {
            NgsLimsUtility.setFailMessage("userPersistMessages", null, "Password Error", "The password cannot be empty");
            return false;
        }
        
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword.trim();
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    
}
