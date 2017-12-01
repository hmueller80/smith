/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="testBean")
@ViewScoped
public class TestBean {
    String text ="";
    int counter = 0;
 
    private UploadedFile file;
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        System.out.println(text);
        counter ++;
    }
    
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
        setText("TRIGGERED! ("+counter+")");
    }
    
    public void submit() {
                System.out.println("Uploaded file name : " + file.getFileName());

    }
    
    public void trigger() {
        setText("TRIGGERED! ("+counter+")");
    }
    
    
}
