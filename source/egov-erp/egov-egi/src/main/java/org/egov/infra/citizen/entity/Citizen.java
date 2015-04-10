package org.egov.infra.citizen.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.springframework.context.annotation.Description;

@Entity
@Table(name = "eg_citizen")
@DiscriminatorValue("CITIZEN")
public class Citizen extends User {

    private static final long serialVersionUID = -521416613072970524L;
    
    private String activationCode;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
    

}
