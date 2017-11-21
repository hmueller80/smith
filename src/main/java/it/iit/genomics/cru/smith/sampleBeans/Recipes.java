package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * @(#)Recipes.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Utility class for recipes.
 *
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "recipes")
@ApplicationScoped
public class Recipes {
    //format 
    //Paired end : 35 mio reads : one fourth of a lane : 50 bases
    private static final String defaultRecipeString = "Single read : 30 mio reads : one fourth of a lane : 50 bases";
    private static final String pairedEnd = "Paired end";
    private static final String singleRead = "Single read";
    
    private static final String depthIndicator = " mio reads";
    private static final String lengthIndicator = " bases";
    
    private static final String[]  fifths = {"one fiths of a lane", "two fiths of a lane", 
    "three fiths of a lane", "four fiths of a lane", "full lane"};
    
    private static final String[]  fours = {"one fourth of a lane", "half of a lane", 
    "three fourth of a lane", "full lane"}; 
    
    private static final String separator = " : ";
    private static final String delimiter = ":";
    
    //recipies can be set by default in int function or by current applications in update function
    List<String> recipes;
    
    public Recipes (){
        if(Preferences.getVerbose()){
            System.out.println("init Recipes...");
        }
        recipes = new ArrayList<String>();
        init();
        if(Preferences.getVerbose()){
            System.out.println("init Recipes...done");
        }
    }
        
    public final void init(){
        //Preferences p = new Preferences();
        FacesContext context = FacesContext.getCurrentInstance();
        Preferences p  = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);    
        
        for (int i = 0; i <p.getDepth_SR().length; i++){
            StringBuilder rec = new StringBuilder("");
            rec.append(singleRead);
            rec.append(separator);
            rec.append(Preferences.getDepth_SR()[i]);
            rec.append(depthIndicator);
            rec.append(separator);
            rec.append(fifths[i]);
            rec.append(separator);
            
            for (String l : p.getReadlength()){
                String rl = rec.toString();
                rl = rl + l;
                rl = rl + lengthIndicator;
                recipes.add(rl);
            }
            
            for (String l : p.getReadlengthdnaseq()){
                String rl = rec.toString();
                rl = rl + l;
                rl = rl + lengthIndicator;
                recipes.add(rl);
            }
            
        }
        
        for (int i = 0; i <p.getDepth_PE().length; i++){
            StringBuilder rec = new StringBuilder("");
            rec.append(pairedEnd);
            rec.append(separator);
            rec.append(p.getDepth_PE()[i]);
            rec.append(depthIndicator);
            rec.append(separator);
            rec.append(fours[i]);
            rec.append(separator);
            
            for (String l : p.getReadlength()){
                String rl = rec.toString();
                rl = rl + l;
                rl = rl + lengthIndicator;
                recipes.add(rl);
            }
            
            for (String l : p.getReadlengthdnaseq()){
                String rl = rec.toString();
                rl = rl + l;
                rl = rl + lengthIndicator;
                recipes.add(rl);
            }
            
        }
    }
    
    public final void update(List<Application> applications){
        recipes.clear();
        for (Application a : applications){
            String r = getRecipeStringByApplication(a);
            if(!recipes.contains(r)){
                recipes.add(r);
            }
        }
    }
    
    public List<String> getRecipes(){
        return recipes;
    }
    
    public void updateApplicationByRecipe(String recipe, Application a){
        StringTokenizer st = new StringTokenizer(recipe, delimiter);
        
        if(st.hasMoreTokens()){
            String mode = st.nextToken().trim();
            if(pairedEnd.equals(mode)){
                a.setReadmode("PE");
            }else{
                a.setReadmode("SR");
            }
        }
        
        if(st.hasMoreTokens()){
            String mode = st.nextToken().trim();
            StringTokenizer dt = new StringTokenizer(mode);
            if(dt.hasMoreTokens()){
                a.setDepth(Integer.parseInt(dt.nextToken().trim()));
            }
        }
        
        if(st.hasMoreTokens()){
            String lanes = st.nextToken().trim();
        }
        
        if(st.hasMoreTokens()){
            String length = st.nextToken().trim();
            StringTokenizer dt = new StringTokenizer(length);
            if(dt.hasMoreTokens()){
                a.setReadlength(Integer.parseInt(dt.nextToken().trim()));
            }
        }
    }
    
    public String findDefaultRecipe(Application a){
        String result = "";
        String appName = a.getApplicationname();

        if("ChIP-Seq".equals(appName)){
            result = "Single read : 30 mio reads : one fiths of a lane : 50 bases";
            
        }else if("DNA-Seq".equals(appName)){
            result = "Paired end : 70 mio reads : half a lane : 100 bases";
            
        }else if("ExomeSeq".equals(appName)){
            result = "Paired end : 70 mio reads : half a lane : 100 bases";
            
        }else if("mRNA-Seq".equals(appName)){
            result = "Paired end : 70 mio reads : half a lane : 50 bases";
            
        }else if("RNA-Seq".equals(appName)){
            result = "Paired end : 35 mio reads : one fourth of a lane : 50 bases";
            
        }else{
            result = "Single read : 30 mio reads : one fiths of a lane : 50 bases";
            
        }
        
        if(!recipes.contains(result)){
            recipes.add(result);
        }
        
        return result;
    }
    
    public String getRecipeStringByApplication(Application a){
        StringBuilder result = new StringBuilder("");
        
        if("PE".equals(a.getReadmode())){
            result.append(pairedEnd);
        }else{
            result.append(singleRead);
        }
        result.append(separator);
              
        result.append(a.getDepth());
        result.append(" ");
        result.append(depthIndicator);
        result.append(separator);
        
        String laneInfo = "unknown";
        if("PE".equals(a.getReadmode())){
            for (int i = 0; i < Preferences.getDepth_PE().length; i++){
                if (Preferences.getDepth_PE()[i].equals(a.getDepth())){
                    if(i < fours.length){
                        laneInfo = fours[i];
                    }
                }
            }
        }else{
            for (int i = 0; i < Preferences.getDepth_SR().length; i++){
                if (Preferences.getDepth_SR()[i].equals(a.getDepth())){
                    if(i < fifths.length){
                        laneInfo = fifths[i];
                    }
                }
            }
        }
        result.append(laneInfo);
        result.append(separator);
        
        result.append(a.getReadlength());
        result.append(lengthIndicator);
        
        return result.toString();
    }
    
    public Recipes getCurrentInstance(){
        return this;
    }
}
