/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.api.persistence.UploadService;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;

/**
 *
 * @author dbarreca
 */
public interface ServiceFactory {

    UploadService getRequestUploadService();

    SampleService getSampleService();

    UserService getUserService();
    
}
