package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Communications;
import it.iit.genomics.cru.smith.entity.CommunicationsPK;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Class managing the queries for the User entity
 * 
 * @author Francesco Venco. 
 * @author Heiko Muller
 */
public class UserHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init UserHelper");
        }
    }

    /**
     *
     * Return the list of users with ID between startID and andID
     * 
     * @author Francesco Venco
     * @param startID start User id
     * @param endID end User id
     * @return List<User> list of Users with id in range     
     *
     */
    public static List<User> getUsersList(int startID, int endID) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session
                    .createQuery("from User as user where user.id between '"
                            + startID + "' and '" + endID + "'");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return userList;
    }

    /**
     *
     * Return the list of all users in the system, including the standard guest
     * 
     * @author Francesco Venco
     * @return List<User> list of Users except User guest     *
     */
    public static List<User> getUsersList(String order) {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        List<User> userList = null;
        Transaction tx = null;        
        try {
            tx = session.beginTransaction();
            //Query q = session.createQuery("from User as user where user.login != '" + Preferences.ROLE_GUEST + "'");
            Query q = session.createQuery("from User order by " + order);
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return userList;
    }
    
    public static List<User> getUsersList() {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        List<User> userList = null;
        Transaction tx = null;        
        try {
            tx = session.beginTransaction();
            //Query q = session.createQuery("from User as user where user.login != '" + Preferences.ROLE_GUEST + "'");
            Query q = session.createQuery("from User");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return userList;
    }

    /**
     * Return the users in the same group
     * 
     * @author Francesco Venco
     * @param PI the User id of the Principal Investigator
     * @return List<User> list of Users with a given Principal Investigator
     */
    public static List<User> getUsersGroup(int PI) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where pi='" + PI + "'");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return userList;
    }
    
    
    /**
     *
     * Return the list of Users with a given role
     * 
     * @author Francesco Venco
     * @param role the role of the User
     * @return List<User> list of Users with a given role
     *
     */
    public static List<User> getUsersListByRole(String role) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where userRole='" + role
                    + "'");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return userList;
    }

    /**
     *
     * Return the User object with a given login
     * 
     * @author Francesco Venco
     * @param login the login of the User
     * @return User object of the corresponding User
     *
     */
    public static User getUserByLoginName(String login) {
        //if(login.equals("null")){
        //    return null;
        //}
        Logger.getLogger("name").info("Search user with user name " + login);
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where login='" + login + "'");
            userList = q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            //tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * Return the User object with a given name
     * 
     * @author Francesco Venco
     * @param name the name of the User
     * @return User object of the corresponding User
     *
     */
    public static User getUserByName(String name) {
        //System.out.println("Search user with user name " + name);
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where username='" + name
                    + "'");
            userList = q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            //tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }
    }
    
    
    
    public static User findUserByName(String name) {
        User u = null;
        
        u = getUserByName(name);
        if(u != null){
            return u;
        }
        
        if(!name.contains(",")){
            //add comma
            String [] nameWords = name.split(" ");
            u = getUserByName(join(", ", nameWords));
            if(u != null){
               return u;
            }
            
            //reverse
            for (int i = 0; i < nameWords.length / 2; i++){
                String tmp = nameWords[i];
                nameWords[i] = nameWords[nameWords.length - i - 1];
                nameWords[nameWords.length - i - 1] = tmp;
            }
            u = getUserByName(join(", ", nameWords));
            if(u != null){
               return u;
            }
        }
        
        /*
        public static String getUserName(String name){
        String res = name;
        
        if(name.contains(",")){
            return name;
        }
        
        if (name.contains(" ")){
            String [] nameWords = name.split(" ");
            return String.join(", ", nameWords);
        }
        
        return res;
        }
        */
                
                
        
        //System.out.println("Search user with user name " + name);
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where username='" + name
                    + "'");
            userList = q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            //tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }
    }
    
    private static String join(String delim, String[] words){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < words.length; i++){
                if(i < words.length - 1){
                    sb.append(words[i] + delim);
                }else{
                    sb.append(words[i]);
                }
            }
            return sb.toString();
        }
    
    /**
     *
     * Return the User object belonging to the Principal Investigator of User u
     * 
     * @author Francesco Venco
     * @param u the User object of a group member
     * @return User object of PI
     *
     */
    public static User getPi(User u) {
        //System.out.println("Search user PI name for " + u.getLogin());
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where id='" + u.getPi() + "'");
            userList = q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            //tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * Return true if the User with login name exists, false otherwise
     * 
     * @author Heiko Muller
     * @param login the User login name
     * @return true if User exists
     *
     */
    public static boolean getUserExists(String login) {
        //System.out.println("Search user with user name " + login);
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where login='" + login
                    + "'");
            userList = q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            //tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * Return the object User searching in the data base by his ID
     * 
     * @author Francesco Venco
     * @param id the User id
     * @return User object with that id
     *
     */
    @SuppressWarnings("unchecked")
    public static User getUserByID(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where id='" + id + "'");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Persists User information
     * 
     * @author Francesco Venco
     * @param u the User to be persisted
     */
    public static void addUser(User u) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (u == null) {
            NgsLimsUtility.setFailMessage("saveUserButton", "SaveUser", "User Not Inserted. No user defined", "Save user failed.");
            return;
        }
        if (u.getUserName().length() == 0 || u.getLogin().length() == 0 || u.getPhone().length() == 0 || u.getMailAddress().length() == 0) {
            NgsLimsUtility.setFailMessage("saveUserButton", "SaveUser", "User Not Inserted. Fill in all fields!", "Save user failed.");
            return;
        }
        if (u.getUserName().indexOf(",") == -1) {
            NgsLimsUtility.setFailMessage("saveUserButton", "SaveUser", "User Not Inserted. Need to separate first and second name with comma.", "Save user failed.");
            return;
        }
        if (u.getMailAddress().indexOf("@") == -1) {
            NgsLimsUtility.setFailMessage("saveUserButton", "SaveUser", "User Not Inserted. Email is ill-defined.", "Save user failed.");
            return;
        }
        if (u != null && !getUserExists(u.getLogin())) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(u);
                tx.commit();
                NgsLimsUtility.setSuccessMessage("saveUserButton", "SaveUser", "User Inserted", "Save succesful");
            } catch (RuntimeException e) {
                e.printStackTrace();
                tx.rollback();

            } finally {
                session.close();
            }
        } else {
            NgsLimsUtility.setFailMessage("saveUserButton", "SaveUser", "User Not Inserted. User may exist.", "Save user failed.");
        }
    }
    
    public static void updateUser(User u) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (u == null) {
            NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. No user defined", "Modify user failed.");
            return;
        }
        if (u.getUserName().length() == 0 || u.getLogin().length() == 0 || u.getPhone().length() == 0 || u.getMailAddress().length() == 0) {
            NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. Fill in all fields!", "Modify user failed.");
            return;
        }
        if (u.getUserName().indexOf(",") == -1) {
            NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. Need to separate first and second name with comma.", "Modify user failed.");
            return;
        }
        if (u.getMailAddress().indexOf("@") == -1) {
            NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. Email is ill-defined.", "Modify user failed.");
            return;
        }
        if (u != null && getUserExists(u.getLogin())) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User m = UserHelper.getUserByLoginName(u.getLogin());
                m.setUserName(u.getUserName());
                m.setMailAddress(u.getMailAddress());
                m.setPi(u.getPi());
                m.setPhone(u.getPhone());
                m.setUserRole(u.getUserRole());
                session.update(m);
                tx.commit();
                NgsLimsUtility.setSuccessMessage("UserModbutton", "ModifyUser", "User Modified", "Modify successful");
            } catch (RuntimeException e) {
                e.printStackTrace();
                tx.rollback();

            } finally {
                session.close();
            }
        } else {
            NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. User may not exist.", "Modify user failed.");
        }
    }
    
    public static void updateUserCommunications(User u, List<User> ul) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        if (u != null && getUserExists(u.getLogin())) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User m = UserHelper.getUserByLoginName(u.getLogin());
                Query q = session.createQuery("from Communications where user_id='" + m.getId() + "'");
                List<Communications> usercommuications = q.list();
                if(usercommuications.size() > 0){
                    for(Communications c : usercommuications){
                        session.delete(c);
                    }
                }
                for(User t : ul){
                    CommunicationsPK c = new CommunicationsPK();
                    c.setUserId(m.getId());
                    c.setCollaboratorId(t.getId());
                    Communications cc = new Communications();
                    cc.setCommunicationsPK(c);
                    session.save(cc); 
                }                
                tx.commit();
                NgsLimsUtility.setSuccessMessage("UserModbutton", "ModifyUserCommunications", "User Communications Modified", "Modify successful");
            } catch (RuntimeException e) {
                e.printStackTrace();
                tx.rollback();

            } finally {
                session.close();
            }
        } else {
            NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. User may not exist.", "Modify user failed.");
        }
    }
    
    /*
    public static List<User> getUserCommunications(User u) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> result = new ArrayList<User>();
        if (u != null && getUserExists(u.getLogin())) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User m = UserHelper.getUserByLoginName(u.getLogin());
                Query q = session.createQuery("from Communications where user_id='" + m.getId() + "'");
                List<Communications> usercommuications = q.list();
                for(Communications c : usercommuications){
                    User collaborator = UserHelper.getUserByID(c.getCommunicationsPK().getCollaboratorId());
                    result.add(collaborator);
                }
                             
                tx.commit();
                //NgsLimsUtility.setSuccessMessage("UserModbutton", "ModifyUserCommunications", "User Communications Modified", "Modify successful");
            } catch (RuntimeException e) {
                e.printStackTrace();
                tx.rollback();

            } finally {
                session.close();
            }
        } else {
            //NgsLimsUtility.setFailMessage("UserModbutton", "ModifyUser", "User Not Modified. User may not exist.", "Modify user failed.");
        }
        return result;
    }
    */

    /**
     * Retrieve the standard guest information
     * @author Francesco Venco
     * @return User guest
     */
    public static User getGuest() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where login='" + Preferences.ROLE_GUEST + "'");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }

    }
    
    /** 
     * return list of Users who wish to be included in communications with User u
     *
     * @author Heiko Muller
     * @param u the recipient of email communications
     * @return List<User> Users put in cc in email communications
     */
    public static List<User> getUserCommunications(User u) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<User> userList = new ArrayList<User>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Communications where user_id=" + u.getId());
            List<Communications> c = q.list();
            for(Communications cc : c){
                q = session.createQuery("from User where user_id=" + cc.getCommunicationsPK().getCollaboratorId());
                List<User> temp = q.list();
                if(temp != null && temp.size() > 0){
                    userList.add(temp.get(0));
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return userList;
    }   
    
}
