package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Attribute;
import it.iit.genomics.cru.smith.entity.AttributeValue;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)EditAttributeBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for editing attribute value pairs.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
public class EditAttributeBean implements Serializable {

    private int sampleID;
    private String formId;
    private int selectedAttributeId;
    private String loadedAttributeName;
    private String loadedAttributeValue;
    private List<String> allAttributeNames;
    private List<Attribute> attributesList;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public EditAttributeBean() {
        if(Preferences.getVerbose()){
            System.out.println("init EditAttributeBean");
        }

        formId = "EditAttributeForm";

        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext()
                .getRequestParameterMap().get("sid");
        if (sid != null) {
            sampleID = Integer.parseInt(sid);
        } // this should never happen and will return an error
        else {
            sampleID = -1;
        }

        String selAtId = (String) context.getExternalContext()
                .getRequestParameterMap().get("selAtId");
        if (selAtId != null) {
            selectedAttributeId = Integer.parseInt(selAtId);

        } else {
            selectedAttributeId = -1;
        }
        loadSelectedAttribute();
        loadAllAttributeNames();
    }

    /**
    * Loads selected attribute.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void loadSelectedAttribute() {
        if (selectedAttributeId < 0) {
            loadedAttributeName = "";
            loadedAttributeValue = "";
            return;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // load the attribute-value in this session
            AttributeValue loadedAttribute = (AttributeValue) session.load(
                    AttributeValue.class, new Integer(selectedAttributeId));
            loadedAttributeName = loadedAttribute.getAttribute().getName();
            loadedAttributeValue = getValue(loadedAttribute);
        } catch (RuntimeException e) {
            tx.rollback();
        } finally {
            session.close();
        }

    }

    /**
    * Loads all attribute names.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    @SuppressWarnings("unchecked")
    public void loadAllAttributeNames() {
        allAttributeNames = new ArrayList<String>();
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            attributesList = session.createQuery("from Attribute").list();
            for (Attribute a : attributesList) {
                allAttributeNames.add(a.getName());
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    /**
    * Completes attribute names.
    *
    * @author Francesco Venco
    * @param query
    * @return List<String>
    * @since 1.0
    */
    public List<String> completeName(String query) {
        List<String> toRet = new ArrayList<String>();
        for (int i = 0; i < allAttributeNames.size(); i++) {
            if (allAttributeNames.get(i).startsWith(query)) {
                toRet.add(allAttributeNames.get(i));
            }
        }
        return toRet;
    }

    /**
    * Saves attribute values.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void saveAttributeValue() {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            /*
             * load the attribue-value in this session or create new
             */
            AttributeValue attributeValuePair;
            if (selectedAttributeId > 0) {
                attributeValuePair = (AttributeValue) session.load(
                        AttributeValue.class, new Integer(selectedAttributeId));
            } else {
                attributeValuePair = new AttributeValue();
                Sample sample = (Sample) session.load(Sample.class, new Integer(sampleID));
                attributeValuePair.setSample(sample);
            }

            // Set the value, choosing between numeri or string
            try {
                float num = Float.parseFloat(loadedAttributeValue);
                attributeValuePair.setNumericValue(new Float(num));
                attributeValuePair.setValue(null);
            } catch (Exception e) {
                attributeValuePair.setNumericValue(null);
                attributeValuePair.setValue(loadedAttributeValue);
            }
            /*
             * load the attribute entity or create new searching for the new
             * name string
             */
            int attId = getAttributeIdFromName(loadedAttributeName);
            Attribute attribute;
            if (attId != -1) {
                attribute = (Attribute) session.load(Attribute.class, new Integer(attId));
            } else {
                attribute = new Attribute();
                attribute.setName(loadedAttributeName);
                session.save(attribute);
            }
            // set the attribute in the pair
            attributeValuePair.setAttribute(attribute);
            session.saveOrUpdate(attributeValuePair);
            tx.commit();
            selectedAttributeId = attributeValuePair.getAttrValid().intValue();
            NgsLimsUtility.setSuccessMessage(formId, "",
                    "Attribute-value saved", sampleID + " saved correctly");
        } catch (RuntimeException e) {
            tx.rollback();
            NgsLimsUtility.setFailMessage(formId, "saveButton",
                    "Saving Failed " + e.getMessage(), e.getMessage());
        } finally {
            session.close();
        }

        this.loadAllAttributeNames();
    }

    /**
    * Deletes an attribute value pair.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void delete() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // load attribute-value in this session and delete
            AttributeValue attributeValuePair = (AttributeValue) session.load(
                    AttributeValue.class, new Integer(selectedAttributeId));
            session.delete(attributeValuePair);
            tx.commit();
            NgsLimsUtility.setWarningMessage(formId, "",
                    "Atrribute-Value deleted", "At-val has been deleted");
            loadedAttributeName = "";
            loadedAttributeValue = "";
            selectedAttributeId = -1;
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "saveButton",
                    "Error in deleting ", fail);
        } finally {
            session.close();
        }

    }

    /**
    * Returns attribute id from attribute name.
    *
    * @author Francesco Venco
    * @param name
    * @return int
    * @since 1.0
    */
    public int getAttributeIdFromName(String name) {
        for (Attribute a : attributesList) {
            if (a.getName().equals(name)) {
                return a.getId().intValue();
            }
        }
        return -1;
    }

    /**
    * Getter for selectedAttributeId.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getSelectedAttributeId() {
        return this.selectedAttributeId;

    }

    /**
    * Getter for loadedAttributeName.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getLoadedAttributeName() {
        return this.loadedAttributeName;
    }

    /**
    * Setter for loadedAttributeName.
    *
    * @author Francesco Venco
    * @param loadedAttributeName
    * @since 1.0
    */
    public void setLoadedAttributeName(String loadedAttributeName) {
        this.loadedAttributeName = loadedAttributeName;
    }

    /**
    * Getter for loadedAttributeValue.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getLoadedAttributeValue() {
        return this.loadedAttributeValue;
    }

    /**
    * Setter for loadedAttributeValue.
    *
    * @author Francesco Venco
    * @param loadedAttributeValue
    * @since 1.0
    */
    public void setLoadedAttributeValue(String loadedAttributeValue) {
        this.loadedAttributeValue = loadedAttributeValue;
    }

    /**
    * Tests if attribute is selected.
    *
    * @author Francesco Venco
    * @param id
    * @return boolean
    * @since 1.0
    */
    public boolean attributeIsSelected(int id) {
        return (id == selectedAttributeId || selectedAttributeId < 0);
    }

    /**
    * Tests if attribute is edited.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean editAttributValue() {
        return selectedAttributeId > 0;
    }

    /**
    * Tests if attribute value is new.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean newAttributeValue() {
        return selectedAttributeId == 0;
    }

    /**
    * Returns attribute value.
    *
    * @author Francesco Venco
    * @param av
    * @return String
    * @since 1.0
    */
    public String getValue(AttributeValue av) {
        String toRet = "";
        if (av.getValue() != null && !av.getValue().equals("")) {
            toRet = av.getValue();
        } else {
            toRet = "" + av.getNumericValue();
        }
        return toRet;
    }

    /**
    * Getter for sampleID.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getSampleID() {
        return this.sampleID;
    }

    /**
    * Setter for sampleID.
    *
    * @author Francesco Venco
    * @param sampleID
    * @since 1.0
    */
    public void setSampleID(int sampleID) {
        this.sampleID = sampleID;
    }

}
