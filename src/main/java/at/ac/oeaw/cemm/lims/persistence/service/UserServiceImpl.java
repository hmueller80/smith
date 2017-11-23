/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject  private UserDAO userDAO;

    /*public UserServiceImpl(UserDAO userDAO) {
        System.out.println("Initializing User Service");
        this.userDAO = userDAO;
    }*/

    @Override
    public UserDTO getUserByLogin(final String userLogin) {
        UserDTO user = null;

        try {
            UserEntity userEntity = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<UserEntity>() {
                @Override
                public UserEntity execute() throws Exception {
                    return userDAO.getUserByLogin(userLogin);
                }
            });
            
            if (userEntity!=null) {
                user = DTOMapper.getUserDTOFromEntity(userEntity);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public UserDTO getUserByID(final Integer Id) {
       UserDTO user = null;

        try {
            UserEntity userEntity = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<UserEntity>() {
                @Override
                public UserEntity execute() throws Exception {
                    return userDAO.getUserByID(Id);
                }
            });
            
            if (userEntity!=null) {
                user = DTOMapper.getUserDTOFromEntity(userEntity);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
