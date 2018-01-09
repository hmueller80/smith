/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence;


import at.ac.oeaw.cemm.lims.api.persistence.NewsService;
import at.ac.oeaw.cemm.lims.api.persistence.RunService;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.persistence.service.LazySampleService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import at.ac.oeaw.cemm.lims.api.persistence.RequestService;
import at.ac.oeaw.cemm.lims.persistence.service.external_users.ExternalUsersService;
import at.ac.oeaw.cemm.lims.persistence.service.request_form.RequestFormService;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class HibernateServiceFactory implements ServiceFactory {

    @Inject LazySampleService sampleService;
    @Inject UserService userService;
    @Inject RequestService requestUploadService;
    @Inject RunService runService;
    @Inject NewsService newsService;
    @Inject RequestFormService requestFormService;
    @Inject ExternalUsersService externalUserService;

    @Override
    public SampleService getSampleService() {
        return sampleService;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public RequestService getRequestService() {
        return requestUploadService;
    }
    
    @Override
    public RunService getRunService() {
        return runService;
    }

    @Override
    public NewsService getNewsService() {
        return newsService;
    }
    
    @Override
    public RequestFormService getRequestFormService() {
        return requestFormService;
    }
    
    @Override
    public ExternalUsersService getExternalUsersService() {
        return externalUserService;
    }
  
}
