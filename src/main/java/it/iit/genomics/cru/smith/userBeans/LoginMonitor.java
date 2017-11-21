/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.User;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author hmuller
 */
@ManagedBean(name = "loginMonitor")
@RequestScoped
public class LoginMonitor  implements Serializable{

    /**
     * Creates a new instance of LoginMonitor
     */
    public LoginMonitor(){
        if(Preferences.getVerbose()){
            System.out.println("init loginMonitor");
        }
        FacesContext context = FacesContext.getCurrentInstance();        
        RoleManager rm = ((RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class)); 
        String login = context.getExternalContext().getRemoteUser();
        System.out.println("loginMonitor: remote user " + login);
        if(rm == null){
            System.out.println("rolemanger is null");
            if(login != null && login.length() > 0){
                if(!rm.getLoginName().equals(login)){
                    User u = UserHelper.getUserByLoginName(login);
                    User pi = UserHelper.getPi(u);
                    if(u != null && pi != null){
                        rm.setLoggedUser(u);
                        rm.setPi(pi);
                        rm.init();
                    }
                }
            }
        }else{
            System.out.println("rolemaner present");
            rm.dump();
        }
    }
}
