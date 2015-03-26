package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name="pgr_complaintstatus_mapping")
public class ComplaintStatusMapping extends AbstractPersistable<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1671713502661376820L;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="current_status_id")
	private ComplaintStatus currentStatus;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="show_status_id")
	private ComplaintStatus showStatus;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
    @Valid
    @JoinColumn(name="role_id")
	private Role role;
	
	@NotNull
	private Integer orderNo;
	
	public ComplaintStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(ComplaintStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public ComplaintStatus getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(ComplaintStatus showStatus) {
		this.showStatus = showStatus;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}


}
