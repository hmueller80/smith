package at.ac.oeaw.cemm.lims.persistence.entity;

import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity implements java.io.Serializable {

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

    @Column(name = "userRole")
    private String userRole;

    @Column(name = "pi")
    private int pi;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<SampleRunEntity> sampleruns = new HashSet<SampleRunEntity>(0);

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<SampleEntity> samples = new HashSet<SampleEntity>(0);
    
    @JoinColumns({
        @JoinColumn(name = "organization_department", referencedColumnName = "department_name")
        , @JoinColumn(name = "organization_name", referencedColumnName = "organization_name")})
    @ManyToOne
    private DepartmentEntity department;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<RequestEntity> requestSet;

    public UserEntity() {
    }

    public UserEntity(String username, String login, Integer pi, String userRole) {
        this.userName = username;
        this.login = login;
        this.pi = pi;
        this.userRole = userRole;
    }

    public UserEntity(String username, String login, String phone, String mailadress,
            Integer pi, String userRole,Set<SampleRunEntity> sampleruns,
            Set<SampleEntity> samples) {
        this.userName = username;
        this.login = login;
        this.phone = phone;
        this.mailAddress = mailadress;
        this.pi = pi;
        this.userRole = userRole;
        this.sampleruns = sampleruns;
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


    public String getUserRole() {
        return this.userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }


    public Set<SampleRunEntity> getSampleruns() {
        return this.sampleruns;
    }

    public void setSampleruns(Set<SampleRunEntity> sampleruns) {
        this.sampleruns = sampleruns;
    }

    public Set<SampleEntity> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<SampleEntity> samples) {
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

    public int getPi() {
        return pi;
    }

    public void setPi(int pi) {
        this.pi = pi;
    }

    public Set<RequestEntity> getRequestSet() {
        return requestSet;
    }

    public void setRequestSet(Set<RequestEntity> requestSet) {
        this.requestSet = requestSet;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }
    
    
    
}
