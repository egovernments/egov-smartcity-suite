package org.egov.ptis.domain.entity.recovery;

import java.util.Date;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.validator.constraints.Length;

/**
 * IntimationNotice(NMC-Notice 155) entity. @author MyEclipse Persistence Tools
 */


public class IntimationNotice extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Recovery recovery;
    private PtNotice notice;
    private String remarks;
    
    private Date paymentDueDate;
    
    @Required(message="intimation.recovery.null")
    public Recovery getRecovery() {
        return recovery;
    }
   
    public PtNotice getNotice() {
        return notice;
    }

    @Length(max = 1024, message = "intimation.remark.length")
    public String getRemarks() {
        return remarks;
    }
  
    @DateFormat(message = "invalid.fieldvalue.paymentDueDate")
    @Required(message="intimation.paymentDueDate.null")
    public Date getPaymentDueDate() {
        return paymentDueDate;
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

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }
   
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("recoveryId :").append(null!=recovery?recovery.getId():" ");
    	sb.append("paymentDueDate :").append(null!=paymentDueDate?paymentDueDate:" ");
    	sb.append("NoticeNo :").append(null!=notice?notice.getNoticeNo():" ");
    	
    	return sb.toString();
    }
}