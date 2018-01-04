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
public class RunIdBean {
    @Inject ServiceFactory services;
    
    @ManagedProperty(value="#{sampleLock}")
    SampleLock sampleLock;
    
    private ReentrantLock lock = new ReentrantLock();

    public void getLock(){
        lock.lock();
        sampleLock.lock();
    }
    
    public Integer getNextId() {
        lock.lock();
        Integer maxIdinDB = services.getRunService().getMaxRunIdInDB();
        System.out.println("Max Id " +maxIdinDB);
        unlockThis();
        return maxIdinDB+1;
    }
    
    
    public void unlock(){
        unlockThis();
        
        try {
            sampleLock.unlock();
        } catch (Exception e) {
        }
    } 
    
    private void unlockThis() {
        try{
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }catch(Exception e){}
    }

    public SampleLock getSampleLock() {
        return sampleLock;
    }

    public void setSampleLock(SampleLock sampleLock) {
        this.sampleLock = sampleLock;
    }
    
    
    
}
