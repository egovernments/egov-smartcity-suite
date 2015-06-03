package org.egov.infra.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "eg_permanent_address") 
@DiscriminatorValue("PERMANENT")
public class PermanentAddress extends Address {

    private static final long serialVersionUID = 904354449098564364L;

}
