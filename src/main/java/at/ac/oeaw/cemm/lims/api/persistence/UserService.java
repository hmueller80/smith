/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface UserService {

    UserDTO getUserByID(final Integer Id);

    UserDTO getUserByLogin(final String userLogin);
    
    List<UserDTO> getAllUsersByPI(Integer PIid);

    public List<UserDTO> getAllUsers();

    public List<UserDTO> getUsersByRole(String role);

    public List<UserDTO> getCollaborators(UserDTO user);

    public boolean userExists(String login) throws Exception;

    public PersistedEntityReceipt persistOrUpdateUser(UserDTO validatedUser, List<UserDTO> collaborators, boolean isNew) throws Exception;

    public UserDTO getUserByMail(String mailAddress);

    public OrganizationDTO getOrganizationByName(String name);

    public List<OrganizationDTO> getAllOrganizations();

    public void saveOrganization(OrganizationDTO orga) throws Exception;

    public void deleteOrgaByName(String name) throws Exception;

}
