package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "pgr_receiving_center", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class ReceivingCenter extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1568590266889348235L;
    private String name;

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

}
