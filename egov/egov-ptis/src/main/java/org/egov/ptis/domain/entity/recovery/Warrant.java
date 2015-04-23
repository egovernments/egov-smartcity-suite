package org.egov.ptis.domain.entity.recovery;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.ptis.notice.PtNotice;

/**
 * EgptWarrant entity. @author MyEclipse Persistence Tools
 */

public class Warrant extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Recovery recovery;
    private PtNotice notice;
    private String remarks;

    @Valid
	private List<WarrantFee> warrantFees = new LinkedList<WarrantFee>();
    
    @Required(message="warrant.recovery.null")
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

	public void setWarrantFees(List<WarrantFee> warrantFees) {
		this.warrantFees = warrantFees;
	}

	public List<WarrantFee> getWarrantFees() {
		return warrantFees;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(null!=notice?notice.getNoticeNo():" ").
		append("|").append(null!=remarks?remarks:" ");
		return sb.toString();
	}

}