/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;

import java.util.Date;

/**
 *
 * @author dbarreca
 */
public interface NewsDTO {

    String getBody();

    Date getDate();

    String getHeader();

    void setBody(String body);

    void setDate(Date date);

    void setHeader(String header);
    
}
