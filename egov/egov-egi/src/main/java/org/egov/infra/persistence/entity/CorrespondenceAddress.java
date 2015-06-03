package org.egov.infra.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "eg_correspondence_address") 
@DiscriminatorValue("CORRESPONDENCE")
public class CorrespondenceAddress extends Address {

    private static final long serialVersionUID = 4214477948989260101L;

}
