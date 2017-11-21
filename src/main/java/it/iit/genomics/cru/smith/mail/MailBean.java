/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.mail;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Samplesheet;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.samplesSheetBeans.SampleSheetHelper;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Date;
import java.util.Properties;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @(#)MailBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for email alerts.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "mailBean")
@ApplicationScoped
public class MailBean implements Serializable{
    
    private Preferences preferences;    
    private String subjectRequestReceived = "Your NGS request -> requestIDs";
    private String subjectFastQReadyAlert = "Your new NGS data are now available";
    private String sentByMailAddress = "ngs@company.com"; 
    private String sentByUnitName = "Company Sequencing Facility"; 
    private String smtpServer = "smtp.company.com";
    private List<User> usersList;

    /**
     * Bean constructor
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public MailBean() {
        if(Preferences.getVerbose()){
            System.out.println("init MailBean");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if(context != null){
            preferences = (Preferences)context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);  
            subjectRequestReceived = preferences.getSubjectRequestReceived();
            subjectFastQReadyAlert = preferences.getSubjectFastQReadyAlert();
            sentByMailAddress = preferences.getSentByMailAddress();
            sentByUnitName = preferences.getSentByUnitName();
            smtpServer = preferences.getSmtpServer();
            usersList = UserHelper.getUsersList();
        }else{
            usersList = UserHelper.getUsersList();
        }
        
    }
    
    /**
     * Generates an email text to alert users about successful sample submission.
     *
     * @author Heiko Muller
     * @param userFirstName
     * @param sampleIDList - the list of sampleID-sampleName pairs generated during sample submission
     * @return String
     * @since 1.0
     */
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
    
    /**
     * Generates an email text to alert users about successful sample submission.
     *
     * @author Heiko Muller
     * @param recipients
     * @param message
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @since 1.0
     */
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
        msg.setSubject(subjectRequestReceived);
        msg.setContent(message, "text/html");
        Transport.send(msg);
       
    }
    
    
    
    /**
     * Generates an email alert about FastQ data generation.
     *
     * @author Heiko Muller
     * @param runfolder
     * @return String
     * @since 1.0
     */
    public String sendFastQDeliveryMails(String runfolder){
        StringBuilder sb = new StringBuilder();
        List<String> recipients = findNonredundantRecipients(viewSamplesheet(findFCID(runfolder)));
        for(int i = 0; i < recipients.size(); i++){
            User u = findUser(recipients.get(i));
            if(u != null){
                sb.append(sendFastqDeliveryMail(u, runfolder));
            }
        }
        return sb.toString();
    }
    
    /**
     * Parses flow cell barcode from runfolder name.
     *
     * @author Heiko Muller
     * @param runfolder
     * @return String
     * @since 1.0
     */
    private String findFCID(String runfolder){
        return runfolder.split("_")[3].substring(1);
    }
    
   
    /**
     * Loads samplesheet from database corresponding to flow cell using the flow cell barcode.
     *
     * @author Heiko Muller
     * @param fcid - flow cell barcode
     * @return List<Samplesheet>
     * @since 1.0
     */
    private List<Samplesheet> viewSamplesheet(String fcid){
        return SampleSheetHelper.loadSamplesheet(fcid);
    }
    
    /**
     * Generates a non-redundant list of users whose samples where run on a flow cell.
     *
     * @author Heiko Muller
     * @param ssheet - sample sheet
     * @return List<String>
     * @since 1.0
     */
    private List<String> findNonredundantRecipients(List<Samplesheet> ssheet){
        HashSet<String> hs = new HashSet<String>();
        ArrayList<String> u = new ArrayList<String>();
        for(int i = 0; i < ssheet.size(); i++){
            Samplesheet s = ssheet.get(i);
            if(!hs.contains(s.getSampleProject())){
                hs.add(s.getSampleProject());
                u.add(s.getSampleProject());
            }
        }
        return u;
    }
    
    /**
     * Loads user from database who submitted the sample.
     *
     * @author Heiko Muller
     * @param login - sample sheet
     * @return User
     * @since 1.0
     */
    private User findUser(String login){
        return UserHelper.getUserByLoginName(login);
    }
    
    /**
     * Generates an email text sent to a user communicating the path to his fastq data.
     *
     * @author Heiko Muller
     * @param u - the user
     * @param runfolder
     * @return String - the email text
     * @since 1.0
     */
    private String getFastqDataPaths(User u, String runfolder) {
        String rundate = getRunDate(runfolder);
        String login = u.getLogin();
        User upi = UserHelper.getPi(u);
        String pi = upi.getUserName().split(",")[1].trim();        
        String s = "";        
                
                s = s + "your new NGS data are now available in fastq format: \r\n\r\n";
                s = s + "FASTQ folder reference from login node: \r\n";
                s = s + "/Illumina/PublicData/FASTQ/" + runfolder + "/Project_" + login + "/\r\n";
                s = s + "FASTQ folder reference from web: \r\n";
                s = s + "http://company.com/data/FASTQ/" + runfolder + "/Project_" + login + "/\r\n\r\n";

                s = s + "Interrogate the FASTQ folder via web to see the FastQC quality control data.\r\n\r\n";

                s = s + "User folder reference from clogin node: \r\n";
                s = s + "/Illumina/PublicData/" + pi + "/" + login + "/FASTQ/" + rundate + "/\r\n";
                s = s + "User folder reference from web: \r\n";
                s = s + "http://company.com/data/" + pi + "/" + login + "/FASTQ/" + rundate + "/\r\n\r\n";
                
        return s;
    }
    
    
    /**
     * Parses run date from runfolder name.
     *
     * @author Heiko Muller
     * @param folder - the runfolder name
     * @return String - the run date
     * @since 1.0
     */
    private String getRunDate(String folder) {
        return folder.split("_")[0];
    }
    
    /**
     * Sends Fastq delivery mail to a user.
     *
     * @author Heiko Muller
     * @param u - the user
     * @param runfolder
     * @return String - a message reporting send success
     * @since 1.0
     */
    private String sendFastqDeliveryMail(User u, String runfolder){
        StringBuilder sb = new StringBuilder();       
        List<User> cc = UserHelper.getUserCommunications(u);
        String msg = composeFastqDeliveryMessage(u, runfolder);
        
        if(msg != null && u != null && cc != null){
            try {
                sendAnalysisReadyMail(u, cc, msg);  
                sb.append("Mail sent to " + u.getUserName() + "\r\n");
            } 
            catch (MessagingException e1) {
                e1.printStackTrace();
                sb.append("Mail not sent to " + u.getUserName() + "\r\n");
            } 
            catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                sb.append("Mail not sent to " + u.getUserName() + "\r\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * Composes a Fastq delivery mail for a user.
     *
     * @author Heiko Muller
     * @param u - the user
     * @param runfolder
     * @return String - the email text
     * @since 1.0
     */
    private String composeFastqDeliveryMessage(User u, String runfolder) {
        StringBuilder sb = new StringBuilder();
        String recipient = u.getUserName().split(",")[0].trim();
        sb.append("<html><body>");
        sb.append("Dear " + recipient + ",<div> <br/>");

        String text = getFastqDataPaths(u, runfolder); //mail main text;
        String[] ta = text.split("\n");

        for (int i = 0; i < ta.length; i++) {
            sb.append(ta[i] + "<br/>");
        }

        sb.append("<br/><br/>");
        sb.append("Thanks for using our service.<br/><br/></div></body></html>");
        sb.append(sentByUnitName);
        return sb.toString();
    }
    
    /**
     * Sends an email.
     *
     * @author Heiko Muller
     * @param recipient - the user the mail is sent to
     * @param ccs - recipients of the email in cc
     * @param message
     * @since 1.0
     */
    private void sendAnalysisReadyMail(User recipient, List<User> ccs, String message) throws MessagingException, UnsupportedEncodingException{
        boolean debug = false;
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
        addressTo[0] = new InternetAddress(recipient.getMailAddress());
         
        
        InternetAddress[] addressCc = new InternetAddress[ccs.size() + 1];
        for (int i = 0; i < ccs.size(); i++) {
            addressCc[i] = new InternetAddress(ccs.get(i).getMailAddress());
        }
        addressCc[addressCc.length -1] = new InternetAddress(sentByMailAddress);
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setRecipients(Message.RecipientType.CC, addressCc);


        // Optional : You can also set your custom headers in the Email if you Want
        //msg.addHeader("IIT_Genomic_Unit", "NGS analysis done");
        msg.setSentDate(new Date(System.currentTimeMillis()));

        // Setting the Subject and Content Type
        msg.setSubject(subjectFastQReadyAlert);
        msg.setContent(message, "text/html");
        Transport.send(msg);
       
    }
    
    /**
     * Getter for Preferences.
     *
     * @author Heiko Muller
     * @return Preferences
     * @since 1.0
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * Getter for subjectRequestReceived.
     *
     * @author Heiko Muller
     * @return String - subjectRequestReceived
     * @since 1.0
     */
    public String getSubjectRequestReceived() {
        return subjectRequestReceived;
    }

    /**
     * Getter for subjectFastQReadyAlert.
     *
     * @author Heiko Muller
     * @return String - subjectFastQReadyAlert
     * @since 1.0
     */
    public String getSubjectFastQReadyAlert() {
        return subjectFastQReadyAlert;
    }

    /**
     * Getter for sentByMailAddress.
     *
     * @author Heiko Muller
     * @return String - sentByMailAddress
     * @since 1.0
     */
    public String getSentByMailAddress() {
        return sentByMailAddress;
    }

    /**
     * Getter for sentByUnitName.
     *
     * @author Heiko Muller
     * @return String - sentByUnitName
     * @since 1.0
     */
    public String getSentByUnitName() {
        return sentByUnitName;
    }

    /**
     * Getter for smtpServer.
     *
     * @author Heiko Muller
     * @return String - smtpServer
     * @since 1.0
     */
    public String getSmtpServer() {
        return smtpServer;
    }

    /**
     * Getter for usersList.
     *
     * @author Heiko Muller
     * @return List<User> - smtpServer
     * @since 1.0
     */
    public List<User> getUsersList() {
        return usersList;
    }  
}
