package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.lib.rjbac.role.RoleImpl;

@Entity
@Table(name="pgr_complaintstatus_mapping")
public class ComplaintStatusMapping extends AbstractPersistable<Long> {

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}

	public RoleImpl getRole() {
		return role;
	}

	public void setRole(RoleImpl role) {
		this.role = role;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1671713502661376820L;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="complaintstatus_id")
	private ComplaintStatus status;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
    @Valid
    @JoinColumn(name="role_id")
	private RoleImpl role;
	
	@NotNull
	private Integer orderNo;
	

}
