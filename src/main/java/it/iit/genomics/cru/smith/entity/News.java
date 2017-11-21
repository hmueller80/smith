package it.iit.genomics.cru.smith.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "news")
public class News implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "news_id")
    private int id;

    @Column(name = "date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @Column(name = "header")
    private String header;

    @Column(name = "body")
    private String body;

    public News() {
    }

    public News(int id, Date date, String header, String body) {
        this.id = id;
        this.date = date;
        this.header = header;
        this.body = body;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int newsId) {
        this.id = newsId;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
