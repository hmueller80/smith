package it.iit.genomics.cru.smith.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "username")
    private String userName;

    @Column(name = "login")
    private String login;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mailadress")
    private String mailAddress;

    @Column(name = "pi")
    private Integer pi;

    @Column(name = "userRole")
    private String userRole;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Collaboration> collaborations = new HashSet<Collaboration>(0);

    @OneToMany(mappedBy = "userByOperatorUserId", fetch = FetchType.LAZY)
//	@JoinColumn(name = "operator_user_id")
    private Set<Reagent> reagentsForOperatorUserId = new HashSet<Reagent>(0);

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//	@JoinColumn(name = "operator_user_id")
    private Set<SampleRun> sampleruns = new HashSet<SampleRun>(0);

    @OneToMany(mappedBy = "userByOwnerId", fetch = FetchType.LAZY)
//	@JoinColumn(name = "owner_id")
    private Set<Reagent> reagentsForOwnerId = new HashSet<Reagent>(0);

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//	@JoinColumn(name = "creator_user_id")
    private Set<Project> projects = new HashSet<Project>(0);

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//	@JoinColumn(name = "requester_user_id")
    private Set<Sample> samples = new HashSet<Sample>(0);

    public User() {
    }

    public User(String username, String login, Integer pi, String userRole) {
        this.userName = username;
        this.login = login;
        this.pi = pi;
        this.userRole = userRole;
    }

    public User(String username, String login, String phone, String mailadress,
            Integer pi, String userRole, Set<Collaboration> collaborations,
            Set<Reagent> reagentsForOperatorUserId, Set<SampleRun> sampleruns,
            Set<Reagent> reagentsForOwnerId, Set<Project> projects,
            Set<Sample> samples) {
        this.userName = username;
        this.login = login;
        this.phone = phone;
        this.mailAddress = mailadress;
        this.pi = pi;
        this.userRole = userRole;
        this.collaborations = collaborations;
        this.reagentsForOperatorUserId = reagentsForOperatorUserId;
        this.sampleruns = sampleruns;
        this.reagentsForOwnerId = reagentsForOwnerId;
        this.projects = projects;
        this.samples = samples;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer userId) {
        this.id = userId;
    }

    public String getUserName() {
        return this.userName;
    }
    
    public String getUserSurname() {
        String res = "";
        String fullName = getUserName();
        int idx = fullName.indexOf(",");
        if(idx != -1){
            res = fullName.substring(idx+1);
        }
        return res.trim();
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailAddress() {
        return this.mailAddress;
    }

    public void setMailAddress(String mailadress) {
        this.mailAddress = mailadress;
    }

    public Integer getPi() {
        return this.pi;
    }

    public void setPi(Integer pi) {
        this.pi = pi;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Set<Collaboration> getCollaborations() {
        return this.collaborations;
    }

    public void setCollaborations(Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
    }

    public Set<Reagent> getReagentsForOperatorUserId() {
        return this.reagentsForOperatorUserId;
    }

    public void setReagentsForOperatorUserId(
            Set<Reagent> reagentsForOperatorUserId) {
        this.reagentsForOperatorUserId = reagentsForOperatorUserId;
    }

    public Set<SampleRun> getSampleruns() {
        return this.sampleruns;
    }

    public void setSampleruns(Set<SampleRun> sampleruns) {
        this.sampleruns = sampleruns;
    }

    public Set<Reagent> getReagentsForOwnerId() {
        return this.reagentsForOwnerId;
    }

    public void setReagentsForOwnerId(Set<Reagent> reagentsForOwnerId) {
        this.reagentsForOwnerId = reagentsForOwnerId;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Sample> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }
    
    public String getFirstName(){
        String firstName = "";
        if(userName != null){
            if(userName.indexOf(",") > -1){
                firstName = userName.split(",")[0];
            }else{
                firstName = userName;
            }
        }
        return firstName;
    }
    
}
