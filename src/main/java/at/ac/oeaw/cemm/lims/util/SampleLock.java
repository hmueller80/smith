/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.util;

import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import java.util.concurrent.locks.ReentrantLock;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class SampleLock {
    @Inject ServiceFactory services;
    
    @ManagedProperty(value="#{requestIdBean}")
    RequestIdBean requestIdLock;
    
    private ReentrantLock lock = new ReentrantLock();

    
    public void lock() {
        lock.lock();
        requestIdLock.getLock();
    }
    
    
    public void unlock() {
        try{
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }catch(Exception e){
        }finally{
            try{
                requestIdLock.unlock();
            }catch(Exception e){}
        }
    }

    public RequestIdBean getRequestIdLock() {
        return requestIdLock;
    }

    public void setRequestIdLock(RequestIdBean requestIdLock) {
        this.requestIdLock = requestIdLock;
    }
    
    
    
}
