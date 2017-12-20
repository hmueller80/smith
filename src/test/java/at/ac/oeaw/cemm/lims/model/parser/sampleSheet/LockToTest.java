/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleSheet;

import static java.lang.Thread.sleep;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author dbarreca
 */
public class LockToTest {
    private Lock lock = new ReentrantLock();
    
    public void doSomething() throws InterruptedException{
        lock.lock();
        sleep(10000L);
        System.out.println(System.currentTimeMillis()+" DONE");
    }
    
    public void unlock(){
        lock.unlock();
    }
}
