/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;
import at.ac.oeaw.cemm.lims.persistence.dao.CommunicationsDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.CommunicationsEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.dao.OrganizationDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.DepartmentEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.DepartmentPK;
import at.ac.oeaw.cemm.lims.persistence.entity.OrganizationEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
    @Inject private OrganizationDAO organizationDAO;
    @Inject DTOMapper myDTOMapper;

    @Override
    public UserDTO getUserByLogin(final String userLogin) {
        UserDTO user = null;

        try {
            user = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<UserDTO>() {
                @Override
                public UserDTO execute() throws Exception {
                    UserEntity userEntity = userDAO.getUserByLogin(userLogin);
                    if (userEntity != null) {
                        return myDTOMapper.getUserDTOFromEntity(userEntity);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public UserDTO getUserByID(final Integer Id) {
        UserDTO user = null;

        try {
            user = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<UserDTO>() {
                @Override
                public UserDTO execute() throws Exception {
                    
                    UserEntity userEntity = userDAO.getUserByID(Id);
                    
                    if (userEntity != null) {
                        return myDTOMapper.getUserDTOFromEntity(userEntity);
                    }
                    return null;
                }
            });

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

    @Override
    public UserDTO getUserByMail(final String mailAddress) {
        UserDTO user = null;
        try {
            user = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<UserDTO>() {
                @Override
                public UserDTO execute() throws Exception {
                    UserEntity userEntity = userDAO.getUserByMail(mailAddress);

                    if (userEntity != null) {
                        return myDTOMapper.getUserDTOFromEntity(userEntity);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    @Override
    public OrganizationDTO getOrganizationByName(final String name) {
        OrganizationDTO orga = null;

        try {
            orga = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<OrganizationDTO>() {
                @Override
                public OrganizationDTO execute() throws Exception {
                    OrganizationEntity orgaEntity = organizationDAO.getOrganizationByName(name);
                    return myDTOMapper.getOrganizationFromEntity(orgaEntity);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orga;
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations() {
        final List<OrganizationDTO> organizations = new LinkedList<>();
        
         try {
            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<OrganizationEntity> orgaEntities = organizationDAO.getAllOrganizations();
                    if (orgaEntities!=null){
                        for (OrganizationEntity orga: orgaEntities){
                            organizations.add(myDTOMapper.getOrganizationFromEntity(orga));
                        }
                    }
                   
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return organizations;
    }

    @Override
    public void saveOrganization(final OrganizationDTO orga) throws Exception {
        TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    OrganizationEntity orgaToPersist = organizationDAO.getOrganizationByName(orga.getName());
                    if (orgaToPersist == null) {                    
                        orgaToPersist = new OrganizationEntity();
                        orgaToPersist.setOrganizationName(orga.getName());
                    }
                    
                    orgaToPersist.setAddress(orga.getAddress());
                    orgaToPersist.setUrl(orga.getWebPage());
                                      
                    Set<DepartmentEntity> toKeep = new HashSet<>();
                    for (DepartmentDTO dept: orga.getDepartments()){
                        DepartmentEntity departmentToPersist = null;
                        DepartmentPK departmentKey = new DepartmentPK(dept.getName(),orga.getName());                        
                        for (DepartmentEntity existingDept: orgaToPersist.getDepartmentSet()){
                            if(existingDept.getDepartmentPK().equals(departmentKey)){
                                departmentToPersist = existingDept;
                                break;
                            }
                        }
                        if (departmentToPersist==null){
                            departmentToPersist = new DepartmentEntity(departmentKey);
                            orgaToPersist.getDepartmentSet().add(departmentToPersist);
                        }
                        
                        departmentToPersist.setAddress(dept.getAddress());
                        departmentToPersist.setUrl(dept.getWebPage());
                        toKeep.add(departmentToPersist);
                    }
                    
                    orgaToPersist.getDepartmentSet().retainAll(toKeep);
                    
                    organizationDAO.saveOrUpdate(orgaToPersist);
                    return null;
                }
            });
    }

    @Override
    public void deleteOrgaByName(final String name) throws Exception {
         TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    OrganizationEntity orgaToDelete = organizationDAO.getOrganizationByName(name);
                    if (orgaToDelete == null) {                    
                        throw new Exception("Organization with name "+name+" not found in DB");
                    }
                    
                    organizationDAO.deleteOrga(orgaToDelete);
                  
     
                    return null;
                }
            });
    }
}
