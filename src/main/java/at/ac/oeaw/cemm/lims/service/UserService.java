/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service;

import at.ac.oeaw.cemm.lims.service.dao.UserDAO;
import it.iit.genomics.cru.smith.entity.User;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class UserService {

    @Inject
    private UserDAO userDAO;

    public User getUserByLogin(final String userLogin) {
        User user = null;

        try {
            user = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<User>() {
                @Override
                public User execute() throws Exception {
                    return userDAO.getUserByLogin(userLogin);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
