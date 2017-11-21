package it.iit.genomics.cru.smith.reagentsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)ReagentFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for reagent form.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
//@SessionScoped
public class ReagentFormBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String reagentbarcode;
    protected String ownerLogin;
    protected String operatorLogin;
    protected String application;
    protected String cataloguenumber;
    protected int supportedreactions;
    protected Date receptiondate;
    protected Date expirationdate;
    protected double price;
    protected String comments;
    protected String institute;
    protected String costcenter;
    protected List<String> userNames;
    protected SimpleDateFormat df;
    protected String formId;
    protected boolean notUsedYet;
    protected int uses;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public ReagentFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init ReagentFormBean");
        }
        init();
    }

    /**
     * init
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void init() {
        formId = "ReagentDetailsForm";
        FacesContext context = FacesContext.getCurrentInstance();
        reagentbarcode = (String) context.getExternalContext().getRequestParameterMap().get("barcode");
        //reagentbarcode = "aaa";
        df = new SimpleDateFormat("dd-MM-yyyy");
        load();
        loadUsers();

    }

    /**
     * Loads reagent by reagent barcode.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    protected void load() {
        //NgsLimsUtility.setFailMessage("reagentDetailsForm", "ReagentModbutton", "test", "test");
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Reagent r = (Reagent) session.load(Reagent.class, reagentbarcode);
            setCurrentReagentData(r);
            uses = r.getSamplerunsForClustergenerationReagentCode().size()
                    + r.getSamplerunsForSampleprepReagentCode().size()
                    + r.getSamplerunsForSequencingReagentCode().size();
            notUsedYet = r.getSamplerunsForClustergenerationReagentCode().isEmpty();
            notUsedYet = notUsedYet && r.getSamplerunsForSampleprepReagentCode().isEmpty();
            notUsedYet = notUsedYet && r.getSamplerunsForSequencingReagentCode().isEmpty();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Loads reagents for a given groupleader.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    protected void loadUsers() {

        List<User> temp = UserHelper.getUsersListByRole("groupleader");
        userNames = new ArrayList<String>();
        userNames.add("undefined");
        for (User u : temp) {
            userNames.add(u.getLogin());
        }

    }

    /**
     * Setter for reagent details.
     *
     * @author Francesco Venco
     * @param reagent
     * @since 1.0
     */
    protected void setCurrentReagentData(Reagent reagent) {
        ownerLogin = reagent.getUserByOwnerId().getLogin();
        operatorLogin = reagent.getUserByOperatorUserId().getLogin();
        application = reagent.getApplication();
        cataloguenumber = reagent.getCatalogueNumber();
        supportedreactions = reagent.getSupportedReactions();
        receptiondate = reagent.getReceptionDate();
        expirationdate = reagent.getExpirationDate();
        price = reagent.getPrice();
        comments = reagent.getComments();
        institute = reagent.getInstitute();
        costcenter = reagent.getCostCenter();
    }

    /**
     * Deletes a reagent with a given barcode.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void delete() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        //NgsLimsUtility.setFailMessage(formId, "ReagentModbutton", "test", "test");
        try {
            tx = session.beginTransaction();
            Reagent r = (Reagent) session.load(Reagent.class, reagentbarcode);
            session.delete(r);
            tx.commit();
            NgsLimsUtility.setWarningMessage(formId, "ReagentModbutton", "Reagent Deleted", "Deletion succesfull");
        } catch (RuntimeException e) {
            NgsLimsUtility.setFailMessage(formId, "ReagentModbutton", "Error", e.getMessage());
            tx.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Saves modifications of reagent details.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void save() {
        User owner = UserHelper.getUserByLoginName(this.ownerLogin);
        User operator = UserHelper.getUserByLoginName(this.operatorLogin);
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Reagent r = (Reagent) session.get(Reagent.class, reagentbarcode);
            if (!checkFormAndSetReagent(r, operator, owner)) {
                throw new RuntimeException("The form is not correct");
            }
            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "ReagentModbutton", "Reagent Updated", "Update succesful");
        } catch (RuntimeException e) {
            NgsLimsUtility.setFailMessage(formId, "ReagentModbutton", "Error in the form", e.getMessage());
            tx.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Saves modifications of reagent details.
     *
     * @author Francesco Venco
     * @param r - reagent
     * @param operator
     * @param owner
     * @return boolean
     * @since 1.0
     */
    protected boolean checkFormAndSetReagent(Reagent r, User operator, User owner) {
        boolean toRet = true;
        if (reagentbarcode.equals("")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "barCode", "Barcode is missing", "Missing");
        } else {
            r.setReagentBarCode(reagentbarcode);
        }
        if (application.equals("undefined")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "application", "Application is undefined", "Undefined");
        } else {
            r.setApplication(application);
        }
        if (cataloguenumber.equals("")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "cataloguenumber", "Catalogue number is missing", "Missing");
        } else {
            r.setCatalogueNumber(cataloguenumber);
        }
        r.setComments(comments);
        if (costcenter.equals("")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "costcenter", "Cost center is missing", "Missing");
        } else {
            r.setCostCenter(costcenter);
        }
        if (institute.equals("")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "institute", "Institute is missing", "Missing");
        } else {
            r.setInstitute(institute);
        }
        try {
            r.setExpirationDate(expirationdate);
        } catch (Exception e) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "expirationdate", "Expiration date is not correct", "Not Correct");
        }
        try {
            r.setReceptionDate(receptiondate);
        } catch (Exception e) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "receptiondate", "Reception date is not correct", "Not Correct");
        }
        try {
            if (supportedreactions == 0) {
                NgsLimsUtility.setWarningMessage(formId, "supportedreactions", "No reactions remaining", "No reactions remaining");
            } else if (supportedreactions < 0) {
                NgsLimsUtility.setFailMessage(formId, "supportedreactions", "Reactions is not correct", "Negative Number!");
                toRet = false;
            } else {
                r.setSupportedReactions(supportedreactions);
            }
        } catch (Exception e) {
            NgsLimsUtility.setFailMessage(formId, "supportedreactions", "Number of reactions is not correct", "Number of reactions is not correct");
            toRet = false;
        }
        try {
            if (price < 0) {
                throw new Exception();
            }
            r.setPrice(price);
        } catch (Exception e) {
            NgsLimsUtility.setFailMessage(formId, "price", "Price is not correct", "Price is not correct");
            toRet = false;
        }
        r.setUserByOperatorUserId(operator);
        if (ownerLogin.equals("undefined")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "owner", "Owner is undefined", "Undefined");
        } else {
            r.setUserByOwnerId(owner);
        }
        return toRet;
    }

    /**
     * Tests if user has reagent modify permission.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean hasModifyPermission() {
        FacesContext context = FacesContext.getCurrentInstance();        
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //LoggedUser lu = new LoggedUser();
        //admin always can
        if (lu.getIsAdmin()) {
            return true;
        }
        if (lu.getIsTech() && notUsedYet) {
            return true;
        }

        return false;
    }

    /**
     * Returns the remaining reactions for a given reagent.
     *
     * @author Francesco Venco
     * @return int - the number of remaining reactions.
     * @since 1.0
     */
    public int getRemainingReactions() {
        return supportedreactions - uses;
    }

    /**
     * Tests if user has reagent delete permission.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean hasDeletePermission() {
        //just in case we need to change something....
        return hasModifyPermission();
    }

    /**
     * Getter for userNames.
     *
     * @author Francesco Venco
     * @return List<String>
     * @since 1.0
     */
    public List<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Getter for reagentbarcode.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getReagentbarcode() {
        return this.reagentbarcode;
    }

    /**
     * Setter for reagentbarcode.
     *
     * @author Francesco Venco
     * @param reagentbarcode
     * @since 1.0
     */
    public void setReagentbarcode(String reagentbarcode) {
        this.reagentbarcode = reagentbarcode;
    }

    /**
     * Getter for ownerLogin.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getOwnerLogin() {
        return this.ownerLogin;
    }

    /**
     * Setter for ownerLogin.
     *
     * @author Francesco Venco
     * @param ownerLogin
     * @since 1.0
     */
    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    /**
     * Getter for operatorLogin.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getOperatorLogin() {
        return this.operatorLogin;
    }

    /**
     * Setter for operatorLogin.
     *
     * @author Francesco Venco
     * @param operatorLogin
     * @since 1.0
     */
    public void setUserByOperatorUserId(String operatorLogin) {
        this.operatorLogin = operatorLogin;
    }

    /**
     * Getter for application.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getApplication() {
        return this.application;
    }

    /**
     * Setter for application.
     *
     * @author Francesco Venco
     * @param application
     * @since 1.0
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * Getter for cataloguenumber.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getCataloguenumber() {
        return this.cataloguenumber;
    }

    /**
     * Setter for cataloguenumber.
     *
     * @author Francesco Venco
     * @param cataloguenumber
     * @since 1.0
     */
    public void setCataloguenumber(String cataloguenumber) {
        this.cataloguenumber = cataloguenumber;
    }

    /**
     * Getter for supportedreactions.
     *
     * @author Francesco Venco
     * @return Integer
     * @since 1.0
     */
    public Integer getSupportedreactions() {
        return this.supportedreactions;
    }

    /**
     * Setter for supportedreactions.
     *
     * @author Francesco Venco
     * @param supportedreactions
     * @since 1.0
     */
    public void setSupportedreactions(Integer supportedreactions) {
        this.supportedreactions = supportedreactions;
    }

    /**
     * Getter for receptiondate.
     *
     * @author Francesco Venco
     * @return Date
     * @since 1.0
     */
    public Date getReceptiondate() {
        return this.receptiondate;
    }

    /**
     * Setter for receptiondate.
     *
     * @author Francesco Venco
     * @param receptiondate
     * @since 1.0
     */
    public void setReceptiondate(Date receptiondate) {
        this.receptiondate = receptiondate;
    }

    /**
     * Getter for expirationdate.
     *
     * @author Francesco Venco
     * @return Date
     * @since 1.0
     */
    public Date getExpirationdate() {
        return this.expirationdate;
    }

    /**
     * Setter for expirationdate.
     *
     * @author Francesco Venco
     * @param expirationdate
     * @since 1.0
     */
    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    /**
     * Getter for price.
     *
     * @author Francesco Venco
     * @return Double
     * @since 1.0
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * Setter for price.
     *
     * @author Francesco Venco
     * @param price
     * @since 1.0
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Getter for comments.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * Setter for comments.
     *
     * @author Francesco Venco
     * @param comments
     * @since 1.0
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Getter for institute.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getInstitute() {
        return this.institute;
    }

    /**
     * Setter for institute.
     *
     * @author Francesco Venco
     * @param institute
     * @since 1.0
     */
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    /**
     * Getter for costcenter.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getCostcenter() {
        return this.costcenter;
    }

    /**
     * Setter for costcenter.
     *
     * @author Francesco Venco
     * @param costcenter
     * @since 1.0
     */
    public void setCostcenter(String costcenter) {
        this.costcenter = costcenter;
    }
}
