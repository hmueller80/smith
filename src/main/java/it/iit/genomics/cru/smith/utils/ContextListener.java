package it.iit.genomics.cru.smith.utils;

//import it.iit.genomics.cru.analysis.AnalysisManagerSEMM;
import it.iit.genomics.cru.analysis.AnalysisManager;
import it.iit.genomics.cru.analysis.AnalysisManagerCEMM;
import it.iit.genomics.cru.analysis.DailyTask;
//import static it.iit.genomics.cru.smith.utils.TimerServlet.MINS_10;

import java.sql.Date;
import it.iit.genomics.cru.smith.defaults.Preferences;
import java.util.Timer;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 *
 * @author yvaskin
 */
@ManagedBean(name = "contextListener")
@ApplicationScoped
public class ContextListener implements ServletContextListener {
    
    private Timer timer = null;
    private long runfolderscaninterval = 60 * 60 * 1000;     
    //public static final long HOURS_6 = 6 * 3600 * 1000;
    //public static final long HOURS_1 = 1 * 3600 * 1000;
    //public static final long MINS_1 = 60 * 1000;
    //public static final long MINS_10 = 600 * 1000;
    //public static final long SECS_10 = 10 * 1000;
    
    

        
    public void contextInitialized(ServletContextEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Preferences prefs = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);
        
        //*
     if (timer == null) {
         //Preferences prefs = new Preferences();
            timer = new Timer();
            runfolderscaninterval = prefs.getRunfolderScanInterval();
            System.out.println("contextlistener runfolderscaninterval " + runfolderscaninterval + " ms.");
            DailyTask dt = new DailyTask();            
            AnalysisManager analysisManager = (AnalysisManager) context.getApplication().evaluateExpressionGet(context, "#{analysisManager}", AnalysisManagerCEMM.class);
            dt.setAnalysisManager(analysisManager);
            timer.scheduleAtFixedRate(dt,   new Date(System.currentTimeMillis()), runfolderscaninterval); 
            
        }
                //*/
    }
 
    public void contextDestroyed(ServletContextEvent event) {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }    
}
