package org.egov.ptis.domain.entity.recovery;
import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.ptis.notice.PtNotice;

/**
 * Warrant Notice(NMC-Notice156) entity. @author MyEclipse Persistence Tools
 */

public class WarrantNotice extends BaseModel {

	
	private static final long serialVersionUID = 1L;
	private Recovery recovery;
	private PtNotice notice;
	private String remarks;
	private Date warrantReturnByDate;
	private String cclist;
	
	@Required(message="warrantNotice.recovery.null")
	public Recovery getRecovery() {
		return recovery;
	}
	public PtNotice getNotice() {
		return notice;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}
	public void setNotice(PtNotice notice) {
		this.notice = notice;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCclist() {
		return cclist;
	}
	public void setCclist(String cclist) {
		this.cclist = cclist;
	}
	@Required(message="warrantNotice.warrantReturnByDate.null")
	public Date getWarrantReturnByDate() {
		return warrantReturnByDate;
	}
	public void setWarrantReturnByDate(Date warrantReturnByDate) {
		this.warrantReturnByDate = warrantReturnByDate;
	}
	@Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("warrantReturnByDate :").append(null!=warrantReturnByDate?warrantReturnByDate:" ");
    	sb.append("noticeNo :").append(null!=notice?notice.getNoticeNo():" ");
    	return sb.toString();
    }
}