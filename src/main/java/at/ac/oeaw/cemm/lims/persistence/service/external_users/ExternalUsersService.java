/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service.external_users;

import at.ac.oeaw.cemm.lims.persistence.dao.external_users.ExternalUserDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.external_users.ExternalGroupEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.external_users.ExternalUserEntity;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class ExternalUsersService {
    @Inject ExternalUserDAO userDAO;
    
    public boolean userExists(final String user){
        boolean result = false;
        
        try{
            result = ExternalUsersTransactionManager.doInTransaction(new ExternalUsersTransactionManager.TransactionCallable<Boolean>() {
                @Override
                public Boolean execute() throws Exception {
                    return userDAO.getUserById(user)!=null;
                }
            });
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return result;
    }
    
    public void resetPasswordForUser(final String login, final String password) throws Exception {   
        final boolean generateNewPassword = password == null || password.trim().isEmpty();
        final String generatedPasswod = RandomStringUtils.random(10, true, true);
        
         ExternalUsersTransactionManager.doInTransaction(new ExternalUsersTransactionManager.TransactionCallable<Void>() {
             @Override
             public Void execute() throws Exception {
                 ExternalUserEntity user = userDAO.getUserById(login);
                 if (user != null) {
                     if (generateNewPassword) {
                         user.setPassword(DigestUtils.sha256Hex(generatedPasswod));
                         user.setFirstPassword(generatedPasswod);
                     } else {
                         user.setFirstPassword("");
                         user.setPassword(DigestUtils.sha256Hex(password));
                     }

                 } else {
                     user = new ExternalUserEntity();
                     user.setUserid(login);
                     user.setPassword(DigestUtils.sha256Hex(generatedPasswod));
                     user.setFirstPassword(generatedPasswod);
                     ExternalGroupEntity group = new ExternalGroupEntity(login, "user");
                     Set<ExternalGroupEntity> externalGroupsSet = new HashSet<>();
                     externalGroupsSet.add(group);
                     user.setExternalGroupsCollection(externalGroupsSet);
                 }
                                
                userDAO.saveUser(user);
                return null;
            }
        });
      
    }

    public void deleteUser(final String userId) throws Exception {
        ExternalUsersTransactionManager.doInTransaction(new ExternalUsersTransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                ExternalUserEntity user = userDAO.getUserById(userId);
                if (user == null) {
                    throw new Exception("User with id "+userId+" not found in DB");
                }
                userDAO.deleteUser(user);
                
                return null;
            }
        });

    }

    public String getPasswordForUser(final String userId) {
        String returnValue = "***";
        
          try{
            returnValue = ExternalUsersTransactionManager.doInTransaction(new ExternalUsersTransactionManager.TransactionCallable<String>() {
                @Override
                public String execute() throws Exception {
                    ExternalUserEntity user = userDAO.getUserById(userId);
                    if (user == null) {
                        return "";
                    }
                    
                    if (DigestUtils.sha256Hex(user.getFirstPassword()).equals(user.getPassword())){
                        return user.getFirstPassword();
                    }
                    
                    return "***";
                }
            });
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return returnValue;
    }
}
