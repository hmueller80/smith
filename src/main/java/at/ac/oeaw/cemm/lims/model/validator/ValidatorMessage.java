/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator;

import java.util.Objects;

/**
 *
 * @author dbarreca
 */
public class ValidatorMessage {
    private ValidatorSeverity type;
    private String summary;
    private String description;

    public ValidatorMessage(ValidatorSeverity type, String summary, String description) {
        this.type = type;
        this.summary = summary;
        this.description = description;
    }

    public ValidatorSeverity getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.type);
        hash = 17 * hash + Objects.hashCode(this.summary);
        hash = 17 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidatorMessage other = (ValidatorMessage) obj;
        if (!Objects.equals(this.summary, other.summary)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
    
    
    
}
