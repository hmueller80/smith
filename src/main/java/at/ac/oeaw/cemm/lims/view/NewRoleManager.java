/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
//import static it.iit.genomics.cru.smith.sampleBeans.SampleHelper.incrementNextSampleId;
import java.io.Serializable;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author hmuller
 */
@ManagedBean(name = "newRoleManager")
@SessionScoped
public class NewRoleManager implements Serializable {
    
    User currentUser;
    String loginName = "";
    User pi;
    boolean Admin;
    boolean Technician;
    boolean User;
    boolean GroupLeader;
    boolean Guest;
    LoggedUser loggedUser;

    /**
     * Creates a new instance of RoleManager
     */
    public NewRoleManager() {
        if(Preferences.getVerbose()){
            System.out.println("init newRoleManager");
        }
        FacesContext context = FacesContext.getCurrentInstance();     
        loggedUser = context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class);         
        
        init();
    }
    
    public void init(){
        System.out.println("init newRoleManager");
        currentUser = loggedUser.getLoggedUser();
        if(currentUser == null){
            Admin = false;
            Technician = false;
            User = false;
            GroupLeader = false;
            Guest = true;
            loginName = Preferences.ROLE_GUEST;
        }else{
            Admin = currentUser.getUserRole().equals(Preferences.ROLE_ADMIN);
            Technician = currentUser.getUserRole().equals(Preferences.ROLE_TECHNICIAN);
            User = currentUser.getUserRole().equals(Preferences.ROLE_USER);
            GroupLeader = currentUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER);
            Guest = currentUser.getUserRole().equals(Preferences.ROLE_GUEST);
            pi = UserHelper.getPi(currentUser);
            loginName = currentUser.getLogin();
        }
        //dump();
    }
    
    public void dump(){
        System.out.println("Admin " + Admin);
        System.out.println("Technician " + Technician);
        System.out.println("User " + User);
        System.out.println("GroupLeader " + GroupLeader);
        System.out.println("Guest " + Guest);
    }

    public User getLoggedUser() {
        return currentUser;
    }

    public void setLoggedUser(User loggeduser) {
        this.currentUser = loggeduser;
    }

    public User getPi() {
        return pi;
    }

    public void setPi(User pi) {
        this.pi = pi;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public boolean isTechnician() {
        return Technician;
    }

    public boolean isUser() {
        return User;
    }

    public boolean isGroupLeader() {
        return GroupLeader;
    }

    public boolean isGuest() {
        return Guest;
    }

    public String getLoginName() {
        return loginName;
    }

    public boolean hasLoadPermission(Sample sample){ 
        //dump();
        if(Admin || Technician){
            return true;
        }else if(Guest){
            return false;
        }else if(User){
            //case 1: sample belongs to user, return true;
            if(sample.getUser().getId().intValue() == this.currentUser.getId().intValue()){
                return true;
            }
            //case 2: user is a collaborator on a project containing this sample
            //we need a hibernate session because projects are loaded lazily
            Set<Project> projects = null;
            Session session = HibernateUtil.getSessionFactory().openSession();  
            boolean outcome = false;
            Transaction tx = null;
            try {
                tx = session.beginTransaction();  
                Sample s = (Sample)session.load(Sample.class, sample.getId());
                projects = s.getProjects();
                for(Project p : projects){
                Set<Collaboration> collaborations = p.getCollaborations();
                for(Collaboration c : collaborations){
                    if(c.getUser().getId().intValue() == this.currentUser.getId().intValue()){
                        outcome = true;
                        break;
                    }
                }
            }   
                tx.commit();
            } catch (Exception e) {
                outcome = false;
                e.printStackTrace();
            }finally{
                if(session.isOpen()){
                    session.close();
                }
            }
            return outcome;                     
        }else if(GroupLeader){
            if(sample.getUser().getPi().intValue() == this.currentUser.getId().intValue()){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasModifyPermission(Sample sample){
        if(Admin || Technician){
            return true;
        }else if(Guest){
            return false;
        }else if(User){
            if(sample.getStatus().equals(Sample.status_requested)){
                return true;
            }                       
        }else if(GroupLeader){
            if(sample.getStatus().equals(Sample.status_requested)){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasDeletePermission(Sample sample){
        if(Admin || Technician){
            return true;
        }else if(Guest){
            return false;
        }else if(User){
            if(sample.getStatus().equals(Sample.status_requested)){
                return true;
            }                       
        }else if(GroupLeader){
            if(sample.getStatus().equals(Sample.status_requested)){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasLoadPermission(SampleRun samplerun){ 
        //dump();
        if(Admin || Technician){
            return true;
        }else if(Guest){
            return false;
        }else if(User){
            //case 1: samplerun belongs to user, return true;
            if(samplerun.getUser().getId().intValue() == this.currentUser.getId().intValue()){
                return true;
            }
            //case 2: user is a collaborator on a project containing this sample
            //we need a hibernate session because projects are loaded lazily
            Set<Project> projects = null;
            Session session = HibernateUtil.getSessionFactory().openSession();  
            boolean outcome = false;
            Transaction tx = null;
            try {
                tx = session.beginTransaction();  
                SampleRun sr = (SampleRun)session.load(Sample.class, samplerun.getId());
                Sample s = sr.getsample();
                projects = s.getProjects();
                for(Project p : projects){
                Set<Collaboration> collaborations = p.getCollaborations();
                for(Collaboration c : collaborations){
                    if(c.getUser().getId().intValue() == this.currentUser.getId().intValue()){
                        outcome = true;
                        break;
                    }
                }
            }   
                tx.commit();
            } catch (Exception e) {
                outcome = false;
                e.printStackTrace();
            }finally{
                if(session.isOpen()){
                    session.close();
                }
            }
            return outcome;                     
        }else if(GroupLeader){
            if(samplerun.getUser().getPi().intValue() == this.currentUser.getId().intValue()){
                return true;
            }
        }
        return false;
    }

    
    
}
