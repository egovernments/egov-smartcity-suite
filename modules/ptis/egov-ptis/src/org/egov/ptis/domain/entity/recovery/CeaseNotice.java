package org.egov.ptis.domain.entity.recovery;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.Required;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.validator.constraints.Length;

/**
 * CeaseNotice(NMC-Notice159) entity. @author MyEclipse Persistence Tools
 */

public class CeaseNotice extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Recovery recovery;
    private PtNotice notice;
    private Date executionDate;
    private String remarks;
    
    @Required(message="intimation.recovery.null")
    public Recovery getRecovery() {
        return recovery;
    }

    public PtNotice getNotice() {
        return notice;
    }
    @CheckDateFormat(message = "invalid.fieldvalue.executionDate")
    @Required(message="intimation.executionDate.null")
    public Date getExecutionDate() {
        return executionDate;
    }

    public void setRecovery(Recovery recovery) {
        this.recovery = recovery;
    }

    public void setNotice(PtNotice notice) {
        this.notice = notice;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
    @Length(max = 1024, message = "intimation.remark.length")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("executionDate :").append(null!=executionDate?executionDate:" ");
    	sb.append("NoticeNo :").append(null!=notice?notice.getNoticeNo():" ");
    	return sb.toString();
    }
}