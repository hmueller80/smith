/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

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
    
}
