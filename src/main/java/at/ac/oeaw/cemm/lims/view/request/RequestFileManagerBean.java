/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "requestFileManagerBean")
@ViewScoped
public class RequestFileManagerBean {
    private final static FileNameMap fileNameMap = URLConnection.getFileNameMap();
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    @ManagedProperty(value = "#{requestBean}")
    private RequestBean requestBean;
    
    private TreeNode rootFolder;
    private TreeNode selectedNode;
    
    @PostConstruct
    public void init(){
        if(requestBean.isNewRequest()){
            rootFolder = null;
        }else {
           File rootFolderPath = requestBean.getSampleAnnotationPath();
           rootFolder = new DefaultTreeNode(rootFolderPath,null);
           selectedNode = rootFolder;
           recursivelyExplore(rootFolder, rootFolderPath);
           
        }
    }
    
    private void recursivelyExplore(TreeNode root, File toExplore){
        if (toExplore.isDirectory()){
            TreeNode dir = new DefaultTreeNode(toExplore,root);
            for (File file: toExplore.listFiles()){
                recursivelyExplore(dir,file);
            }
        }else{
            new DefaultTreeNode("document",toExplore,root);
        }
    }

    public TreeNode getRootFolder() {
        return rootFolder;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }
    
    public Boolean isSelectedDirectory() {
        if (selectedNode == null || selectedNode.getData()==null){
            return null;
        }
        return ((File) selectedNode.getData()).isDirectory();
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        if (!requestBean.canView()) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "File Upload", "You do not have permission to upload this file");
            return;
        }

        UploadedFile file = event.getFile();
        if (file == null){
            NgsLimsUtility.setFailMessage("filesMessage", null, "File Upload", "No file selected"); 
            return;
        }
        
        String fileName = file.getFileName();

        if (fileName.trim().toLowerCase().replace("_", "").replace("-", "").replace(" ", "").contains("sampleannotationsheet")) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "File Upload", "You cannot upload a sample annotation sheet.");
            return;
        }
        
        InputStream in = null;
        OutputStream out = null;
        try {
            File toWrite = new File ( ((File) selectedNode.getData()).getAbsolutePath(),fileName);
            if (toWrite.exists()){
                NgsLimsUtility.setFailMessage("filesMessage", null, "File Upload", "This file already exists");
                return;                
            }
            
            in = file.getInputstream();
            out = new FileOutputStream(toWrite);

            int reader = 0;
            byte[] bytes = new byte[(int) file.getSize()];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            out.flush();
            NgsLimsUtility.setSuccessMessage("filesMessage", null, "File Upload", "File "+fileName+" Uploaded correctly");
            init();
        } catch (Exception ex) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "File Upload", "Error while uploading file "+ex.getMessage());
            ex.printStackTrace();
        }finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException ex) {}
            }
             if (out!=null){
                try {
                    out.close();
                } catch (IOException ex) {}
            }
        }

    }
    
    public void deleteFile(){
        File toDelete = (File) selectedNode.getData();
        
        if (!roleManager.hasAnnotationSheetDeletePermission()) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "Delete File", "You do not have permission to delete files");
            return;
        }

        if (toDelete.isDirectory()) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "Delete File", "Folders cannot be deleted");
            return;
        }

        if (toDelete.getName().trim().toLowerCase().replace("_", "").replace("-", "").replace(" ", "").contains("sampleannotationsheet")) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "Delete File", "You cannot delete a sample annotation sheet.");
            return;
        }
         
        if (toDelete.delete()){
            NgsLimsUtility.setSuccessMessage("filesMessage", null, "Delete File", "File "+toDelete.getName()+" deleted correctly");
            init();
        }else{
            NgsLimsUtility.setFailMessage("filesMessage", null, "Delete File", "File "+toDelete.getName()+" could not be deleted");
        }
        
    }

    public void setSelectedNode(TreeNode selectedNode) {
        System.out.println("SETTER CALLED");
        this.selectedNode = selectedNode;
    }
    

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean requestBean) {
        this.requestBean = requestBean;
    }
           
 
    public StreamedContent getFile() {
        
        if (selectedNode == null || selectedNode.getData() == null){
            NgsLimsUtility.setFailMessage("filesMessage", null, "Download File", "No file selected");
            return null;
        }
        
        File selectedFile = (File) selectedNode.getData();

        if (selectedFile.isDirectory()){
            NgsLimsUtility.setFailMessage("filesMessage", null, "Download File", "Folders cannot be downloaded");
            return null;
        }
        
        InputStream stream;
        try {
            stream = new FileInputStream(selectedFile);
            return new DefaultStreamedContent(stream, fileNameMap.getContentTypeFor(selectedFile.getName()), selectedFile.getName());
        } catch (FileNotFoundException ex) {
            NgsLimsUtility.setFailMessage("filesMessage", null, "Download File", "File "+selectedFile.getName()+" not found");
            return null;
        }
    }
    
}
