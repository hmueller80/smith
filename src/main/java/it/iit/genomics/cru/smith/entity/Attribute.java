package it.iit.genomics.cru.smith.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "attribute")
public class Attribute implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idattribute")
    private Integer id;

    @Column(name = "attributename")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "value")
    private Set<AttributeValue> values = new HashSet<AttributeValue>(0);

    public Attribute() {
    }

    public Attribute(String name) {
        this.name = name;
    }

//	public Attribute(String name, Set<AttributeValue> values) {
//		this.name = name;
//		this.values = values;
//	}
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

//	public Set<AttributeValue> getValues() {
//		return this.values;
//	}
//
//	public void setValues(Set<AttributeValue> values) {
//		this.values = values;
//	}
}
