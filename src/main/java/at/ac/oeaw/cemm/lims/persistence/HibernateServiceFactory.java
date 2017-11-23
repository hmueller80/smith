/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence;

import at.ac.oeaw.cemm.lims.persistence.dao.ApplicationDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.LibraryDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.IndexDAO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.persistence.UserService;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.api.persistence.UploadService;
import at.ac.oeaw.cemm.lims.persistence.service.LazySampleService;
import at.ac.oeaw.cemm.lims.persistence.service.RequestUploadServiceImpl;
import at.ac.oeaw.cemm.lims.persistence.service.UserServiceImpl;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class HibernateServiceFactory implements ServiceFactory {

    @Inject LazySampleService sampleService;
    @Inject UserService userService;
    @Inject UploadService requestUploadService;

    public HibernateServiceFactory() {
        System.out.println("Initializing Hibernate Service Factory");
        /*ApplicationDAO appDAO = new ApplicationDAO();
        IndexDAO indexDAO = new IndexDAO();
        LibraryDAO libraryDAO = new LibraryDAO();
        SampleDAO sampleDAO = new SampleDAO();
        UserDAO userDAO = new UserDAO();
        
        
        sampleService = new LazySampleService(sampleDAO, libraryDAO, indexDAO, appDAO, userDAO);
        userService = new UserServiceImpl(userDAO);
        requestUploadService = new RequestUploadServiceImpl(sampleDAO, libraryDAO, indexDAO, appDAO, userDAO,  sampleService);*/
    }

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

}
