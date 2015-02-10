package org.egov.pims.commons;

import java.util.Date;

import org.egov.lib.rjbac.user.User;

public class Position {
	
	private Integer id;
	private User createdBy;
	private Date createdDate;
	private User modifiedBy;
	private Date modifiedDate;
	private String name;
	//private DrawingOfficer drawingOfficer;
	private DeptDesig deptDesigId;
	private Integer isPostOutsourced;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DeptDesig getDeptDesigId() {
		return deptDesigId;
	}

	public void setDeptDesigId(DeptDesig deptDesigId) {
		this.deptDesigId = deptDesigId;
	}

	public Integer getIsPostOutsourced() {
		return isPostOutsourced;
	}

	public void setIsPostOutsourced(Integer isPostOutsourced) {
		this.isPostOutsourced = (isPostOutsourced==null?0:isPostOutsourced);
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}	

}
