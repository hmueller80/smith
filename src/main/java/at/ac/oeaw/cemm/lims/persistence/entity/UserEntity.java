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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    
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


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer userId) {
        this.id = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
