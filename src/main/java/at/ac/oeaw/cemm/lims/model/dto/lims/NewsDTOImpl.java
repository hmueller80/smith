/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.NewsDTO;
import java.util.Date;

/**
 *
 * @author dbarreca
 */
public class NewsDTOImpl implements NewsDTO {
    private final Integer id;
    private String header;
    private String body;
    private Date date;

    public NewsDTOImpl(Integer id, String header, String body, Date date) {
        this.id = id;
        this.header = header;
        this.body=body;
        this.date = date;
    }

    
    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
    
    
   
}
