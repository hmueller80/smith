/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.requestform;

/**
 *
 * @author dbarreca
 */
public class Affiliation {
    
    private String name;
    private String deptartment;
    private String address;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptartment() {
        return deptartment;
    }

    public void setDeptartment(String deptartment) {
        this.deptartment = deptartment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
}
