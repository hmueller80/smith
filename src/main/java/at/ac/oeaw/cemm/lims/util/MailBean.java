/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.util;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import javax.mail.*;
import javax.mail.internet.*;

@ApplicationScoped
public class MailBean implements Serializable{
    
    private final String SUBJ_REQ_RECEIVED = "Your NGS request -> requestIDs";
    private String sentByMailAddress = "ngs@company.com"; 
    private String sentByUnitName = "Company Sequencing Facility"; 
    private String smtpServer = "smtp.company.com";


    @PostConstruct
    public void init() {      
            sentByMailAddress = Preferences.getSentByMailAddress();
            sentByUnitName = Preferences.getSentByUnitName();
            smtpServer = Preferences.getSmtpServer();  
    }
    

    public String composeRequestReceivedMessage(String userFirstName, String sampleIDList){
        StringBuilder sb = new StringBuilder();         
        sb.append("<html><body>");
        sb.append("Dear " + userFirstName + ",<div> <br/></div>"); 
        sb.append("Your request has been added to the pipeline. Please label your samples with the following requestIDs:");  
        sb.append("<div><br/><table>");
        sb.append("<tr><td>requestID</td><td>sampleName</td></tr>");        
        sb.append(sampleIDList);
        sb.append("</table> <br/></div>");
        sb.append("Thanks<div> <br/></div></body></html>");
        sb.append(sentByUnitName);    
        return sb.toString();    
    }
    
  
    public void sendRequestIDMail(String[] recipients, String message) throws MessagingException, UnsupportedEncodingException{
        boolean debug = false;

        //Set the host smtp address
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpServer);

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);


        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(sentByMailAddress, sentByUnitName);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[1];
        InternetAddress[] addressCc = new InternetAddress[1];

        addressTo[0] = new InternetAddress(recipients[0]);
        addressCc[0] = new InternetAddress(recipients[1]);

        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setRecipients(Message.RecipientType.CC, addressCc);


        // Optional : You can also set your custom headers in the Email if you Want
        msg.setSentDate(new Date(System.currentTimeMillis()));

        // Setting the Subject and Content Type
        msg.setSubject(SUBJ_REQ_RECEIVED);
        msg.setContent(message, "text/html");
        Transport.send(msg);
       
    }
}
