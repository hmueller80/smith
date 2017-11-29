/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.NewsDTO;
import at.ac.oeaw.cemm.lims.api.persistence.NewsService;
import at.ac.oeaw.cemm.lims.persistence.dao.NewsDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.NewsEntity;
import at.ac.oeaw.cemm.lims.persistence.service.TransactionManager.TransactionCallable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class NewsServiceImpl implements NewsService {

    @Inject
    private NewsDAO newsDAO;
    @Inject
    DTOMapper myDTOMapper;

    @Override
    public List<NewsDTO> retrieveAllNews() {
        final List<NewsDTO> result = new LinkedList<>();

        try {
            TransactionManager.doInTransaction(
                    new TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<NewsEntity> allNews = newsDAO.getAllNews();
                    if (allNews != null) {
                        for (NewsEntity news : allNews) {
                            result.add(myDTOMapper.getNewsDTOFromEntity(news));
                        }
                    }
                    return null;
                }
            }
            );
        } catch (Exception ex) {
            Logger.getLogger(NewsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public void submitNews(final NewsDTO newNews) throws Exception {
         TransactionManager.doInTransaction(
                    new TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    NewsEntity newsEntity = new NewsEntity();
                    newsEntity.setHeader(newNews.getHeader());
                    newsEntity.setBody(newNews.getBody());
                    newsEntity.setDate(newNews.getDate());
                    newsEntity.setId(newsDAO.getMaxId()+1);
                    newsDAO.save(newsEntity);
                    return null;
                }
            }
            );
    }

    @Override
    public boolean newsExists(final String body) {
        boolean result = false;

        try {
            result = TransactionManager.doInTransaction(
                    new TransactionCallable<Boolean>() {
                @Override
                public Boolean execute() throws Exception {

                    return newsDAO.newsExists(body);
                }
            }
            );
        } catch (Exception ex) {
            Logger.getLogger(NewsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
