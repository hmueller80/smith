/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.bsf.barcode.IlluminaAdapterSequences;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.LibrarySubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.RequestSubmissionForm;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SampleSubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SequencingRequestSubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SubmissionSummary;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;

/**
 *
 * @author hmueller
 */
public class AssembleRequestSubmissionForm {

 /*   ArrayList<LibrarySubmission> librarySubmissionList;
    ArrayList<SequencingRequestSubmission> requestSubmissionList;
    ArrayList<SampleSubmission> sampleSubmissionList;
    SubmissionSummary submissionSummary;
    ArrayList<RequestSubmissionForm> form;

    String formId = "homeform";
    String componentId = "hometabs";
    NgsLimsUtility messageBean;

    public AssembleRequestSubmissionForm(SubmissionSummary submissionSummary, ArrayList<SampleSubmission> sampleSubmissionList, ArrayList<LibrarySubmission> librarySubmissionList, ArrayList<SequencingRequestSubmission> requestSubmissionList) {
        this.submissionSummary = submissionSummary;
        this.sampleSubmissionList = sampleSubmissionList;
        this.librarySubmissionList = librarySubmissionList;
        this.requestSubmissionList = requestSubmissionList;
        FacesContext context = FacesContext.getCurrentInstance();        
        messageBean = ((NgsLimsUtility) context.getApplication().evaluateExpressionGet(context, "#{messageBean}", NgsLimsUtility.class));
        
    }

    public ArrayList<RequestSubmissionForm> getSubmissionForm() {
        if (sampleSubmissionList == null || librarySubmissionList == null || submissionSummary == null || requestSubmissionList == null) {
            if (sampleSubmissionList == null || sampleSubmissionList.isEmpty()) {                
                messageBean.setFailMessage(formId, componentId, "Sample worksheet error", "Unparsable Sample worksheet detected. Make sure 'Sample' worksheet exists.");
                
            }
            if (librarySubmissionList == null || librarySubmissionList.isEmpty()) {                
                messageBean.setFailMessage(formId, componentId, "Libraries worksheet error", "Unparsable Libraries worksheet detected. Make sure 'Libraries' worksheet exists.");
            }
            if (submissionSummary == null || submissionSummary.dump().equals("")) {
                messageBean.setFailMessage(formId, componentId, "Summary worksheet error", "Unparsable Summary worksheet detected. Make sure 'Summary' worksheet exists.");
            }
            if (requestSubmissionList == null || requestSubmissionList.isEmpty()) {
                messageBean.setFailMessage(formId, componentId, "Sequencing request worksheet error", "Unparsable Sequencing request worksheet detected. Make sure 'Sequencing Request' worksheet exists.");
            }            
            messageBean.setFailMessage(formId, componentId, "Unknown error", "An ill-defined error occured.");
            return null;
        }
        if (sampleSubmissionList.size() != librarySubmissionList.size()) {
            System.out.println(sampleSubmissionList.size());
            System.out.println(librarySubmissionList.size());
            //System.out.println("null 2");
            messageBean.setFailMessage(formId, componentId, "Samples error", "Number of samples listed in Samples and in Libraries sheet doesn't match.");
            return null;
        }
        if(!assertUserEmailCorrect(submissionSummary.getContactPersonEmail())){
            messageBean.setFailMessage(formId, componentId, "Contact person error", "Contact person email address not valid.");
        }
        
        if (submissionSummary.getContactPersonEmail().indexOf("@") == -1) {
            messageBean.setFailMessage(formId, componentId, "Contact person error", "No contact person email address found.");
            return null;
        }
        String contacts = submissionSummary.getContactPersonEmail();
        contacts = contacts.replace(",", "");
        contacts = contacts.replace(";", "");
        contacts = contacts.trim();
        String[] contacta = contacts.split("@");
        if (contacta.length > 2) {
            messageBean.setFailMessage(formId, componentId, "Contact person error", "More than one contact person email address found. Please choose exactly one contact person email address.");
            return null;
        }
        //User u = ub.getUserByEmail(contacts);
        UserEntity u = getUserByEmail(contacts);
        if (u == null) {
            messageBean.setFailMessage(formId, componentId, "Contact person error", "Contact person email address not valid. Please make sure email is spelled correcly and contact person is registered with the LIMS.");
            return null;
        }
        UserEntity pi = getUserById(u.getPi());

        form = new ArrayList<RequestSubmissionForm>();
        int submissionid = getNextSubmissionId();
        for (int i = 0; i < librarySubmissionList.size(); i++) {
            if (!sampleSubmissionList.get(i).getSampleName().equals(librarySubmissionList.get(i).getSampleName())) {
                messageBean.setFailMessage(formId, componentId, "Sample names error", "Sample names for sample " + (i + 1) + " in Samples and Libraries sheet don't match.");
                return null;
            }
            SequencingRequestSubmission req = getRequestForLibrary(librarySubmissionList.get(i));

            RequestSubmissionForm f = assembleSubmissionFormRow(u, pi, submissionSummary, sampleSubmissionList.get(i), librarySubmissionList.get(i), req, submissionid);
         
            
            if(f.barcodeLettersAreValid()){
                form.add(f);
            }else{
                messageBean.setFailMessage(formId, componentId, "Barcode error", "Barcode in sample " + f.getSampleName() + " has invalid letters.");
                return null;
            }
        }
        return form;
    }

    private SequencingRequestSubmission getRequestForLibrary(LibrarySubmission ls) {
        SequencingRequestSubmission result = null;
        if (requestSubmissionList != null && requestSubmissionList.size() > 0) {
            for (int i = 0; i < requestSubmissionList.size(); i++) {
                SequencingRequestSubmission temp = requestSubmissionList.get(i);
                if (temp.getLibraryName().equals(ls.getLibraryName())) {
                    result = temp;
                    return result;
                } else if (temp.getLibraryName().equals(ls.getLibraryLabel())) {
                    result = temp;
                    ls.setLibraryName(ls.getLibraryLabel());
                    return result;
                }
            }
            if (result == null) {
                messageBean.setFailMessage(formId, componentId, "Sequencing Request error", "Library " + ls.getLibraryName() + " not found in Sequencing Request.");
                return null;
            }
        } else {
            messageBean.setFailMessage(formId, componentId, "Sequencing Request error", "Sequencing Request not defined.");
            return null;
        }
        return result;
    }

    public RequestSubmissionForm assembleSubmissionFormRow(User u, User pi, SubmissionSummary y, SampleSubmission s, LibrarySubmission l, SequencingRequestSubmission r, int submissionid) {
        if (!s.getSampleName().equals(l.getSampleName())) {
            messageBean.setFailMessage(formId, componentId, "Sample error", "Sample names in Sample and Libraries sheet don't match.");
            return null;
        }
        
        if(u == null){
            messageBean.setFailMessage(formId, componentId, "User error", "No contact person could be found.");
            return null;
        }
        if(pi == null){
            messageBean.setFailMessage(formId, componentId, "User group error", "No user group could be found.");
            return null;
        }
        if(y == null){
            messageBean.setFailMessage(formId, componentId, "Submission Summary error", "No submission Summary could be found,");
            return null;
        }
        
        if(s == null){
            messageBean.setFailMessage(formId, componentId, "Sample error", "No Samples could be found.");
            return null;
        }
        
        if(l == null){
            messageBean.setFailMessage(formId, componentId, "Library error", "No Libraries could be found.");
            return null;
        }
        
        if(r == null){
            messageBean.setFailMessage(formId, componentId, "Sequencing Request error", "No Sequencing Request could be found.");
            return null;
        }
        
        RequestSubmissionForm result = new RequestSubmissionForm();
        String username = u.getUserName();
        username = username.replace(",", "");
        result.setUserName(username);
        result.setUserLogin(u.getLogin());
        result.setUserTel(u.getPhone());
        result.setInstitute(y.getSubmittingOrganizationName());
        String piusername = pi.getUserName();
        piusername = piusername.replace(",", "");
        result.setPI(piusername);
        result.setSampleName(l.getSampleName());

        //barcode handling
        String barcodei7 = l.getBarcodeSequencei7();
        if (!IlluminaAdapterSequences.getDetailedIndexInfo(barcodei7).equals(IlluminaAdapterSequences.isI7Index)) {
            barcodei7 = "";
        }
        String barcodei5 = l.getBarcodeSequencei5();
        if (!(IlluminaAdapterSequences.getDetailedIndexInfo(barcodei5).equals(IlluminaAdapterSequences.isI5Index) || IlluminaAdapterSequences.getDetailedIndexInfo(barcodei5).equals(IlluminaAdapterSequences.isI5revcomplIndex))) {
            barcodei5 = "";
        } else if (!IlluminaAdapterSequences.getDetailedIndexInfo(barcodei5).equals(IlluminaAdapterSequences.isI5Index)) {
            barcodei5 = IlluminaAdapterSequences.getReverseComplement(barcodei5);
        }
        if (l.getBarcodeSequencei7().length() == 0 && l.getBarcodeSequencei5().length() > 0) {
            String temp = "";
            for (int i = 0; i < l.getBarcodeSequencei5().length(); i++) {
                temp = temp + "N";
            }
            l.setBarcodeSequencei7(temp);
        }
        result.setBarcodeByName(l.getBarcodeSequencei7() + l.getBarcodeSequencei5());
        result.setApplication(l.getLibraryType());
        String readlength = r.getReadLength();
        //System.out.println(readlength);
        if (readlength.length() == 0 || readlength.equals("null") || readlength.equals("NA")) {
            readlength = "0";
        }
        if (readlength.endsWith(".0")) {
            readlength = readlength.replace(".0", "");
        }
        if (readlength.endsWith("bp")) {
            readlength = readlength.replace("bp", "");
        }
        readlength = readlength.trim();
        try {
            int rl = Integer.parseInt(readlength);
        } catch (NumberFormatException nfe) {
            messageBean.setFailMessage(formId, componentId, "Read length error", "Read length for library " + l.getLibraryName() + " is not a valid integer number.");
            return null;
        }
        result.setReadLength(readlength);
        result.setReceipe(r.getSequencingType() + " : " + "30 mio reads" + " : full lane");
        result.setSampleType("");
        result.setLibrarySynthesis("Needed");
        result.setOrganism(s.getOrganism());
        result.setAntibody("");
        result.setSampleDescription(s.getSampleDescription());
        result.setBioAnalyzerdate(l.getLibraryDateAsString());
        result.setBioAnalyzernanomolarity(0 + "");

        String conc = l.getLibraryDNAConcentration();
        if (conc.length() == 0 || conc.equals("null") || conc.equals("NA")) {
            conc = "0.0";
        }
        if (conc.endsWith("nM")) {
            conc = conc.replace("nM", "");
            conc = conc.trim();
        }
        if (conc.contains("/")) {            
            conc = conc.replace("ng/ul", "");
            conc = conc.replace("ng/µl", "");
            conc = conc.trim();
        }
        try {
            double c = Double.parseDouble(conc);
        } catch (NumberFormatException nfe) {
            messageBean.setFailMessage(formId, componentId, "Library concentration error", "Concentration for library " + l.getLibraryName() + " is not a valid floating point number.");
            return null;
        }
        result.setSampleConcentration(conc);

        String amount = l.getLibraryDNAAmount();
        if (amount.length() == 0 || amount.equals("null") || amount.equals("NA")) {
            amount = "0.0";
        }
        if (amount.endsWith("ng")) {
            amount = amount.replace("ng", "");
            amount = amount.trim();
        }
        try {
            double c = Double.parseDouble(amount);
        } catch (NumberFormatException nfe) {
            messageBean.setFailMessage(formId, componentId, "Library DNA amount error", "Library DNA amount for library " + l.getLibraryName() + " is not a valid floating point number.");
            return null;
        }
        result.setTotalAmount(amount);

        String size = l.getLibraryInsertSize();
        //System.out.println(size);
        if (size.length() == 0 || size.equals("null") || size.equals("NA")) {
            size = "0";
        }
        if (size.endsWith("bp")) {
            size = size.replace("bp", "");
            size = size.trim();
        }
        if (size.endsWith(".0")) {
            size = size.replace(".0", "");
        }
        try {
            int c = Integer.parseInt(size);
        } catch (NumberFormatException nfe) {
            messageBean.setFailMessage(formId, componentId, "Library Insert Size error", "Library Insert Size for library " + l.getLibraryName() + " is not a valid integer number.");
            return null;
        }
        result.setBulkFragmentSize(size);
        result.setComments(l.getLibraryComment());
        result.setLibrary(l.getLibraryName());
        result.setSubmissionid(submissionid + "");
        result.setSubmissiondate(l.getLibraryDateAsString());

        String vol = l.getLibraryVolume();
        if (vol.length() == 0 || vol.equals("null") || vol.equals("NA")) {
            vol = "0.0";
        }
        if (vol.endsWith("µl")) {
            vol = vol.replace("µl", "");
            vol = vol.trim();
        }
        try {
            double c = Double.parseDouble(vol);
        } catch (NumberFormatException nfe) {
            messageBean.setFailMessage(formId, componentId, "Library volume error", "Library volume for library " + l.getLibraryName() + " is not a valid floating point number.");
            return null;
        }
        result.setLibraryVolume(vol);

        return result;
    }
    
    private boolean assertUserEmailCorrect(String s){
        if(s == null){
            return false;
        }
        if(s.trim().equals("")){
            return false;
        }
        if(s.indexOf("@") < 0){
            return false;
        }
        if(s.indexOf("@") != s.lastIndexOf("@")){
            return false;
        }        
        return true;
    }

    private int getNextSubmissionId() {
        Session session = at.ac.oeaw.cemm.bsf.bsfdb.entity.HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            List<Sample> l = session.createQuery("from Sample s where s.submissionId > 460").list();
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getSubmissionId() > result) {
                    result = l.get(i).getSubmissionId();
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            //throw e; // or display error message
        } finally {
            session.close();
        }
        return result + 1;
    }
    
    private User getUserByEmail(String email){
        Session session = at.ac.oeaw.cemm.bsf.bsfdb.entity.HibernateUtil.getSessionFactory().openSession();                       
                Transaction tx = null;
                List<User> u = null;
                User user = null;
                try {
                    tx = session.beginTransaction();                    
                    //List<User> l = session.createQuery("select all from User u order by u.userName asc").list();
                    //u = session.createQuery("select all from User u where u.mailAddress='" + email + "'").list();
                    u = session.createQuery("from User u where u.mailAddress='" + email + "'").list();
                    if(u != null && u.size() == 1){
                        user = u.get(0);
                    }
                    
                    //System.out.println("user count " + l.size());
                    //users = parseUsers(l);
                    tx.commit();
                    
                    
                }
                catch (RuntimeException e) {
                    tx.rollback();
                    e.printStackTrace();
                    //throw e; // or display error message
                }
                finally {
                    session.close();
                }  
                return user;
    }
    
    private User getUserById(int id){
        Session session = at.ac.oeaw.cemm.bsf.bsfdb.entity.HibernateUtil.getSessionFactory().openSession();                       
                Transaction tx = null;
                List<User> u = null;
                User user = null;
                try {
                    tx = session.beginTransaction();                    
                    //List<User> l = session.createQuery("select all from User u order by u.userName asc").list();
                    u = session.createQuery("from User u where u.id=" + id).list();
                    if(u != null && u.size() == 1){
                        user = u.get(0);
                    }
                    
                    //System.out.println("user count " + l.size());
                    //users = parseUsers(l);
                    tx.commit();
                    
                    
                }
                catch (RuntimeException e) {
                    tx.rollback();
                    e.printStackTrace();
                    //throw e; // or display error message
                }
                finally {
                    session.close();
                }  
                return user;
    }
*/
}
