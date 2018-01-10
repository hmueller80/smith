/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.persistence.service.IndexService;
import at.ac.oeaw.cemm.lims.persistence.service.external_users.ExternalUsersService;
import at.ac.oeaw.cemm.lims.persistence.service.request_form.RequestFormService;

/**
 *
 * @author dbarreca
 */
public interface ServiceFactory {

    RequestService getRequestService();

    SampleService getSampleService();

    UserService getUserService();
    
    RunService getRunService();
    
    NewsService getNewsService();
    
    RequestFormService getRequestFormService();
    
    ExternalUsersService getExternalUsersService();
    
    IndexService getIndexService();
    
}
