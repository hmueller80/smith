package at.ac.oeaw.cemm.lims.view.news;

import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.NewsDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;


@ManagedBean(name = "newNewsBean")
@ViewScoped
public class NewsBean implements Serializable{
    
    @Inject private ServiceFactory services;
    @Inject private DTOFactory myDTOFactory;
    
    private List<NewsDTO> news = null;

    @PostConstruct
    public void init(){
        news = services.getNewsService().retrieveAllNews();
    }
    
  
   
    public NewsDTO getNewsItem(int idx) {
        if(idx < news.size() ){
            return news.get(idx);
        }
        return myDTOFactory.createEmptyNews();
    }


  
  
}
