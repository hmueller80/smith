
package it.iit.genomics.cru.smith.data;


import it.iit.genomics.cru.analysis.AnalysisManager;
import it.iit.genomics.cru.analysis.AnalysisManagerCEMM;
//import it.iit.genomics.cru.analysis.AnalysisManagerSEMM;
import it.iit.genomics.cru.smith.defaults.Preferences;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * @(#)DataUpdaterBean.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Class launches AnalysisManager on call to updateData().
 *
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "dataUpdaterBean")
@SessionScoped
public class DataUpdaterBean {
    
    static boolean isProgress = false;
    static AnalysisManager am;
    //static {
    //    am = new AnalysisManager();
    //}
        
        
    /**
     * Bean constructor
     *
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public DataUpdaterBean(){
        if(Preferences.getVerbose()){
            System.out.println("init DataUpdaterBean");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        //am = (AnalysisManager) context.getApplication().evaluateExpressionGet(context, "#{analysisManager}", AnalysisManagerSEMM.class);
        am = (AnalysisManager) context.getApplication().evaluateExpressionGet(context, "#{analysisManager}", AnalysisManagerCEMM.class);
    }
    
    /**
     * launches AnalysisManager
     *
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public static void updateData(){
            if(isProgress == false){   
                isProgress = true;
                System.out.println("Data update has started");
                
                //do processing
                //am.runTest();
                am.run();
                
                
                isProgress = false;
            }else{
                System.out.println("Data update is in progress");
            }
        }
}
