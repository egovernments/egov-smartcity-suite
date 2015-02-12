package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name="pgr_complaintstatus")
public class ComplaintStatus extends AbstractPersistable<Long> {
	private static final long serialVersionUID = -9009821412847211632L;
	@NotNull
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
