/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.util;

import at.ac.oeaw.cemm.lims.api.analysis.AnalysisManager;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;
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
public class TimedTask {
    
    @Inject private AnalysisManager analysisManager;
    
    @ManagedProperty(value="#{preferences}")
    Preferences preferences;
    
    @PostConstruct
    public void init(){
        System.out.println("Initializing timer...");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                analysisManager.run();
            }
        },0, Preferences.getRunfolderScanInterval() );
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
        
    
}
