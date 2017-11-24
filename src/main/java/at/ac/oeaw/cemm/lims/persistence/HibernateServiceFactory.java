/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence;


import at.ac.oeaw.cemm.lims.api.persistence.RunService;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.api.persistence.UploadService;
import at.ac.oeaw.cemm.lims.persistence.service.LazySampleService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class HibernateServiceFactory implements ServiceFactory {

    @Inject LazySampleService sampleService;
    @Inject UserService userService;
    @Inject UploadService requestUploadService;
    @Inject RunService runService;

    @Override
    public SampleService getSampleService() {
        return sampleService;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public UploadService getRequestUploadService() {
        return requestUploadService;
    }
    
    @Override
    public RunService getRunService() {
        return runService;
    }

}
