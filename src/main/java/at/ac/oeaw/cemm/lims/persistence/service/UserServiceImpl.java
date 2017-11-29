/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;
import at.ac.oeaw.cemm.lims.persistence.dao.CommunicationsDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.CommunicationsEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject  private UserDAO userDAO;
    @Inject  private CommunicationsDAO commDAO;
    @Inject DTOMapper myDTOMapper;

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
                user = myDTOMapper.getUserDTOFromEntity(userEntity);
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
                user = myDTOMapper.getUserDTOFromEntity(userEntity);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<UserDTO> getAllUsersByPI(final Integer PIid) {
        final List<UserDTO> result = new ArrayList<>();

        try {

            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<UserEntity> userEntities = userDAO.getUsersByPI(PIid);
                    if (userEntities != null) {
                        for (UserEntity entity : userEntities) {
                            result.add(myDTOMapper.getUserDTOFromEntity(entity));
                        }
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        final List<UserDTO> result = new LinkedList<>();

        try {

            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<UserEntity> userEntities = userDAO.getAllUsers();
                    if (userEntities != null) {
                        for (UserEntity entity : userEntities) {
                            result.add(myDTOMapper.getUserDTOFromEntity(entity));
                        }
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<UserDTO> getUsersByRole(final String role) {
        final List<UserDTO> result = new LinkedList<>();

        try {

            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<UserEntity> userEntities = userDAO.getAllUsersByRole(role);
                    if (userEntities != null) {
                        for (UserEntity entity : userEntities) {
                            result.add(myDTOMapper.getUserDTOFromEntity(entity));
                        }
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<UserDTO> getCollaborators(final UserDTO user) {
        final List<UserDTO> result = new LinkedList<>();

        try {
            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {                    
                    if (user!=null && user.getId()!=null){
                        List<Integer> collaborators = commDAO.getAllCommunicationByUser(user.getId());
                        if (collaborators != null && !collaborators.isEmpty()) {                         
                            List<UserEntity> userEntities = userDAO.getUsersByID(collaborators);
                            if (userEntities != null) {
                                for (UserEntity entity : userEntities) {
                                    result.add(myDTOMapper.getUserDTOFromEntity(entity));
                                }
                            }
                        }
                    }

                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean userExists(final String login) throws Exception {
        
        return TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Boolean>() {
                @Override
                public Boolean execute() throws Exception {                    
                    return userDAO.userExists(login);
                }
            });
    }

    @Override
    public PersistedEntityReceipt persistOrUpdateUser(final UserDTO validatedUser,final List<UserDTO> collaborators, final boolean isNew) throws Exception { 
        return TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<PersistedEntityReceipt>() {
                @Override
                public PersistedEntityReceipt execute() throws Exception {                    
                   UserEntity userEntity = persistOrUpdateUserOnly(validatedUser,isNew);
                   List<Integer> toBeRemoved = commDAO.getAllCommunicationByUser(userEntity.getId());
                   for (Integer collaborator: toBeRemoved){
                       commDAO.delete(userEntity.getId(),collaborator);
                   }
                   for (UserDTO collaborator: collaborators){
                       commDAO.save(new CommunicationsEntity(userEntity.getId(),collaborator.getId()));
                   }
                   return new PersistedEntityReceipt(userEntity.getId(),userEntity.getLogin());
                }
            });
    }
    
    protected UserEntity persistOrUpdateUserOnly(UserDTO user, boolean isNew) throws Exception {
        UserEntity userEntity;
        if (!isNew) {
            userEntity = userDAO.getUserByLogin(user.getLogin());
            if (userEntity == null) {
                throw new Exception("Could not find user with login " + user.getLogin());
            }
        } else {
            userEntity = new UserEntity();
            if (userDAO.userExists(user.getLogin())) {
                throw new Exception("User with the same login already exists. Login " + user.getLogin());
            }
            userEntity.setLogin(user.getLogin());
        }
        userEntity.setUserName(user.getUserName());
        userEntity.setPhone(user.getPhone());
        userEntity.setMailAddress(user.getMailAddress());
        if (user.getPi()==null){
            userEntity.setPi(-1);
        }else{
            userEntity.setPi(user.getPi());
        }
        userEntity.setUserRole(user.getUserRole());

        userDAO.updateOrPersistUser(userEntity);
        HibernateUtil.getSessionFactory().getCurrentSession().flush();
        
        if (userEntity.getPi() == -1) {
            userEntity.setPi(userEntity.getId());
            userDAO.updateOrPersistUser(userEntity);
        }
        return userEntity;
    }
}
