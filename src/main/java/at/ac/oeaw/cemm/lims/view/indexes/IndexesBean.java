/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.indexes;

import at.ac.oeaw.cemm.lims.api.dto.lims.BarcodeDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.persistence.entity.BarcodeKit;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class IndexesBean {
    @Inject ServiceFactory services;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private TreeNode root;
    private TreeNode selectedNode;
    
    //PERMISSIONS
    public void hasViewPermission() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!roleManager.hasIndexPermission()) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error401.xhtml");
        }else{
            final Pattern pattern = Pattern.compile("(.*)([0-9]+)$");
            
            root = new DefaultTreeNode(new IndexRow("Kits","-",false,null));
            selectedNode = root;
            Map<String, Map<IndexType, Set<BarcodeDTO>>> allKits = services.getIndexService().getAllBarcodes();
            List<String> kits = new LinkedList<> (allKits.keySet());
            Collections.sort(kits);
            for (String kit: kits){
                TreeNode kitNode = new DefaultTreeNode(new IndexRow(kit,"-",false,kit),root);
                List<IndexType> indexes = new LinkedList<>(allKits.get(kit).keySet());
                Collections.sort(indexes);
                for (IndexType index: indexes){
                    TreeNode indexNode = new DefaultTreeNode(new IndexRow(index.toString(),"-",false,kit),kitNode);
                    List<BarcodeDTO> barcodes = new LinkedList<>(allKits.get(kit).get(index));
                    Collections.sort(barcodes, new Comparator<BarcodeDTO>() {
                        @Override
                        public int compare(BarcodeDTO o1, BarcodeDTO o2) {
                            Matcher matcher1 = pattern.matcher(o1.getIndexName());
                            Matcher matcher2 = pattern.matcher(o2.getIndexName());
                            if (matcher1.matches() && matcher2.matches()){
                                String baseName1 = matcher1.group(1);
                                String baseName2 = matcher2.group(1);
                                if (baseName1.equals(baseName2)){
                                    Integer code1 = Integer.parseInt(matcher1.group(2));
                                    Integer code2 = Integer.parseInt(matcher2.group(2));
                                    
                                    return code1.compareTo(code2);
                                }else{
                                    return baseName1.compareTo(baseName2);
                                }
                            }
                            
                            
                            return o1.getIndexName().compareTo(o2.getIndexName());
                        }
                    });
                    for (BarcodeDTO barcode: barcodes){
                        TreeNode sequenceNode = new DefaultTreeNode(new IndexRow(barcode.getIndexName(),barcode.getIndex().getIndex(),true,kit),indexNode);
                    }
                }
            }
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public TreeNode getSelectedNode() {
        return selectedNode;
    }
 
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public ServiceFactory getServices() {
        return services;
    }
    
    public String editSample(){
        if (selectedNode == null || selectedNode.getData() == null || ((IndexRow)selectedNode.getData()).getKitName()==null){
            return null;
        }else{
            return "indexEdit?faces-redirect=true&kitName="+((IndexRow)selectedNode.getData()).getKitName();
        }
    }
    
    
}
