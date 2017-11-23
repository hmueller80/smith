/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;

/**
 *
 * @author dbarreca
 */
public interface UserService {

    UserDTO getUserByID(final Integer Id);

    UserDTO getUserByLogin(final String userLogin);
    
}
