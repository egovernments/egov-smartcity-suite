package org.egov.eis.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.User;

@Entity
@Table(name = "eis_employee")
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    private static final long serialVersionUID = -1105585841211211215L;


}
