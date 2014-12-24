package org.egov.pims.model;

import org.egov.lib.rjbac.user.User;


public class DisciplinaryPunishmentApproval implements java.io.Serializable {
	private Integer id;
	private User approvedBy;
	private org.egov.pims.model.DisciplinaryPunishment disciplinaryPunishmentId;
	private String sanctionNo;
	
	public User getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSanctionNo() {
		return sanctionNo;
	}
	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}
	public org.egov.pims.model.DisciplinaryPunishment getDisciplinaryPunishmentId() {
		return disciplinaryPunishmentId;
	}
	public void setDisciplinaryPunishmentId(
			org.egov.pims.model.DisciplinaryPunishment disciplinaryPunishmentId) {
		this.disciplinaryPunishmentId = disciplinaryPunishmentId;
	}

}
