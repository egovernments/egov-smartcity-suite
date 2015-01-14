package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "pgr_receiving_center", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class ReceivingCenter extends AbstractPersistable<Long> {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
