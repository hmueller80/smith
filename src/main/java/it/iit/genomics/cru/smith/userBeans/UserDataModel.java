package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.User;
import java.io.Serializable;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

/**
 * @(#)UserDataModel.java 20 JUN 2014 Copyright 2014 Computational Research Unit
 * of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * DataModel for User objects.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class UserDataModel extends ListDataModel<User> implements SelectableDataModel<User>, Serializable {

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public UserDataModel() {
        if(Preferences.getVerbose()){
            System.out.println("init UserDataModel");
        }
    }

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @param data - a list of users
     * @since 1.0
     */
    public UserDataModel(List<User> data) {
        super(data);
        if(Preferences.getVerbose()){
            System.out.println("init UserDataModel");
        }
    }

    /**
     * Getter for row data.
     *
     * @author Francesco Venco
     * @param rowKey
     * @return User
     * @since 1.0
     */
    @Override
    public User getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

        List<User> us = (List<User>) getWrappedData();

        for (User u : us) {
            if (u.getId().toString().equals(rowKey)) {
                return u;
            }
        }

        return null;
    }

    /**
     * Getter for row key.
     *
     * @author Francesco Venco
     * @param u - a user
     * @return Object
     * @since 1.0
     */
    @Override
    public Object getRowKey(User u) {
        return u.getId();
    }
}
