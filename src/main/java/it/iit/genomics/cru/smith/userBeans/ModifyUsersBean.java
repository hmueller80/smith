/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author hmuller
 */
@ManagedBean(name = "modifyUsersBean")
@SessionScoped
public class ModifyUsersBean implements Serializable {
    
    private DualListModel<String> communications;
    List<User> users;
  
    private ResourceBundle bundle;
    private String formId;
    
    private String userName;
    private String userLogin;
    private String piLogin;
    private String userEmail;
    private String userPhone;
    private String userRole;
    private Integer userPI;
    private String userPIName;
    private Integer userid;
    
    private boolean hasModifyPermission = true;
    private List<String> userRoles;

    /**
     * Creates a new instance of ModifyUsersBean
     */
    public ModifyUsersBean() {
        if(Preferences.getVerbose()){
            System.out.println("init modifyUsersBean");
        }
        System.out.println("init modifyUsersBean");

        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        formId = "userDetailsForm";  
        
        users = UserHelper.getUsersList("username");//order by username

    }
    
    
    public String loadid(){
        System.out.println("load user id called");
        userRoles = Preferences.getROLES();
        FacesContext context = FacesContext.getCurrentInstance();
        //sampleSearchBean = (SampleSearchBean) context.getApplication().evaluateExpressionGet(context, "#{sampleSearchBean}", SampleSearchBean.class);
        String uid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        if (uid != null) {
            userid = Integer.parseInt(uid);
            // this should never happen and will return an error
        } else {
            userid = new Integer(-1);
        }
        //System.out.println(sid);
        System.out.println(userid);
        // get the user of the sample
        User user = UserHelper.getUserByID(userid);

        // retrieve all the user's informations
        userLogin = user.getLogin();
        userName = user.getUserName();
        System.out.println(userName);
        userEmail = user.getMailAddress();
        userPhone = user.getPhone();
        userPI = user.getPi();
        piLogin = UserHelper.getUserByID(userPI.intValue()).getLogin();
        System.out.println(piLogin);
        userPIName = UserHelper.getUserByID(userPI.intValue()).getUserSurname();
        System.out.println(userPIName);
        userRole = user.getUserRole(); 
        
         //users
        List<String> userSource = new ArrayList<String>();
        List<String> userTarget = new ArrayList<String>();
        
        
        
        //move collaborators from source to target list
        List<User> coll = UserHelper.getUserCommunications(user);
        if(coll.size() == 0){
            for(User u : users){
                if(!userSource.contains(u.getUserName())){
                        userSource.add(u.getUserName());
                    }
            }
        }else{
            for(User u : users){            
                for(User v : coll){
                    if(u.getUserName().equals(v.getUserName())){
                        if(!userTarget.contains(u.getUserName())){
                            userTarget.add(u.getUserName());
                        }
                    }else{
                        if(!userSource.contains(u.getUserName())){
                            userSource.add(u.getUserName());
                        }
                    }
                }
            }
        }
        
        communications = new DualListModel<String>(userSource, userTarget);
        
        
        String logInName = context.getExternalContext().getRemoteUser();        
        User loggedUser = UserHelper.getUserByLoginName(logInName);
        hasModifyPermission = loggedUser.getUserRole().equals(Preferences.ROLE_ADMIN);
        
        return "userDetails?faces-redirect=true";
        
      
        
        
    }
    
    public void modify(){
        User u = new User();
        u.setLogin(userLogin);
        u.setUserName(userName);
        u.setMailAddress(userEmail);
        User pi = UserHelper.getUserByLoginName(piLogin);
        u.setPi(pi.getId());
        u.setPhone(userPhone);
        u.setUserRole(userRole);
        UserHelper.updateUser(u);
        
        //find users to receive emails in cc
        List<String> c = communications.getTarget();
        List<User> al = new ArrayList<User>();
        for(User x : users){
            for(String s : c){
                if(s.equals(x.getUserName())){
                    al.add(x);
                    //System.out.println("added user " + x.getUserName());
                }
            }
        }
        UserHelper.updateUserCommunications(u, al);
    
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPiLogin() {
        return piLogin;
    }

    public void setPiLogin(String piLogin) {
        this.piLogin = piLogin;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getUserPI() {
        return userPI;
    }

    public void setUserPI(Integer userPI) {
        this.userPI = userPI;
    }

    public String getUserPIName() {
        return userPIName;
    }

    public void setUserPIName(String userPIName) {
        this.userPIName = userPIName;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public boolean isHasModifyPermission() {
        return hasModifyPermission;
    }

    public void setHasModifyPermission(boolean hasModifyPermission) {
        this.hasModifyPermission = hasModifyPermission;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public DualListModel<String> getCommunications() {
        if(communications == null){
            List<String> userSource = new ArrayList<String>();
            List<String> userTarget = new ArrayList<String>();
            communications = new DualListModel<String>(userSource, userTarget);
        }
        return communications;
    }

    public void setCommunications(DualListModel<String> communications) {
        this.communications = communications;
    }
    
    
    
    
}
