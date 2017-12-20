/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.util;

import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class RequestIdBean {
    @Inject ServiceFactory services;
    
    private Lock lock = new ReentrantLock();

    public Integer getNextId() {
        lock.lock();
        Integer maxIdinDB = services.getRequestFormService().getMaxRequestId();   
        System.out.println("Max Id " +maxIdinDB);
        return maxIdinDB+1;
    }
    
    public void unlock(){
        lock.unlock();
    } 
    
}
