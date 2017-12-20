/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleSheet;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author dbarreca
 */
public class LockTest {
    
    @Ignore
    public void testLock() throws InterruptedException{
        final LockToTest toTest = new LockToTest();
        
        Runnable task = new Runnable(){
             @Override
            public void run() {
                try {
                    System.out.println("Running task");
                    toTest.doSomething();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }finally{
                    toTest.unlock();
                }
            }};
        
        
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
