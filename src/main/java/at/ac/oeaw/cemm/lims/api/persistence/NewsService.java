/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.lims.NewsDTO;
import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface NewsService {

    public List<NewsDTO> retrieveAllNews();

    public void submitNews(NewsDTO newNews) throws Exception;
    
    public boolean newsExists(String body);
    
}
