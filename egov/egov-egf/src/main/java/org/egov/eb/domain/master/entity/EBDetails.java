/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.domain.master.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.eb.utils.EBUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.model.bills.EgBillregister;
import org.egov.pims.commons.Position;

/*
 * 
 * @author Parvati
 */
public class EBDetails extends StateAware {

	private EBConsumer ebConsumer;
	private String billNo;
	private BigDecimal billAmount;
	private Integer month = 0;
	private BigDecimal prevBillAmount;
	private String comments;
	private EgBillregister egBillregister;
	private Date billDate;
	private EgwStatus status;
	private String receiptNo;
	private Date receiptDate;
	private Date dueDate;
	private Boundary ward;
	private TargetArea area;
	private Position position;
	private String region;
	private CFinancialYear financialyear;
	private BigDecimal variance;
	
	@Transient
	private boolean isProcess;
	
	@Transient
	private boolean isHighlight;
	
	/**
	 * Used to capture the comments, given before approval or rejection for variance 
	 * This will be persisted in ebdetails.state.text2 field.
	 */
	@Transient
	private String remarks;

	public EBConsumer getEbConsumer() {
		return ebConsumer;
	}

	public void setEbConsumer(EBConsumer ebConsumer) {
		this.ebConsumer = ebConsumer;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public Integer getMonth() {
		return month;	
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public BigDecimal getPrevBillAmount() {
		return prevBillAmount;
	}

	public void setPrevBillAmount(BigDecimal prevBillAmount) {
		this.prevBillAmount = prevBillAmount;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	public EgBillregister getEgBillregister() {
		return egBillregister;
	}

	public void setEgBillregister(EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}

	public EgwStatus getStatus() {
		return status;
	}

	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	//TODO this will be billno
	@Override
	public String getStateDetails() {
		return new StringBuilder()
			.append(EBUtils.getShortMonthName(month)).append("-").append(EBUtils.getYear(this.dueDate))
			.append(", Region: ").append(this.ebConsumer.getRegion())
			.append(", Target Area: ").append(this.area.getName())
			.toString();
	}
	
	@Override
	public String myLinkId() {
		return String.valueOf(this.getId());
	}
	
	/*
     * FIXME PHOENIX use HashCodeBuilder from apache commons for hashcode generation 
     * and equals method as well with EqualsBuilder
     * 
	@Override
	public int hashCode() {
		
		int seedValue = HashCodeUtil.SEED;

		seedValue = HashCodeUtil.hash(seedValue, this.billAmount);
		seedValue = HashCodeUtil.hash(seedValue, this.billDate);
		seedValue = HashCodeUtil.hash(seedValue, this.billNo);
		seedValue = HashCodeUtil.hash(seedValue, this.dueDate);
		seedValue = HashCodeUtil.hash(seedValue, this.ebConsumer);
		seedValue = HashCodeUtil.hash(seedValue, this.egBillregister);
		seedValue = HashCodeUtil.hash(seedValue, this.month);
		seedValue = HashCodeUtil.hash(seedValue, this.prevBillAmount);
		seedValue = HashCodeUtil.hash(seedValue, this.receiptDate);
		seedValue = HashCodeUtil.hash(seedValue, this.receiptNo);
		seedValue = HashCodeUtil.hash(seedValue, this.status);
		seedValue = HashCodeUtil.hash(seedValue, this.ward);
		
		return seedValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		EBDetails other = (EBDetails) obj;
		
		if (billAmount == null) {
			if (other.billAmount != null)
				return false;
		} else if (!billAmount.equals(other.billAmount))
			return false;
		
		if (billDate == null) {
			if (other.billDate != null)
				return false;
		} else if (!billDate.equals(other.billDate))
			return false;
		
		if (billNo == null) {
			if (other.billNo != null)
				return false;
		} else if (!billNo.equals(other.billNo))
			return false;
		
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		
		if (ebConsumer == null) {
			if (other.ebConsumer != null)
				return false;
		} else if (!ebConsumer.equals(other.ebConsumer))
			return false;
		
		if (egBillregister == null) {
			if (other.egBillregister != null)
				return false;
		} else if (!egBillregister.equals(other.egBillregister))
			return false;
		
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		
		if (prevBillAmount == null) {
			if (other.prevBillAmount != null)
				return false;
		} else if (!prevBillAmount.equals(other.prevBillAmount))
			return false;
		
		if (receiptDate == null) {
			if (other.receiptDate != null)
				return false;
		} else if (!receiptDate.equals(other.receiptDate))
			return false;
		
		if (receiptNo == null) {
			if (other.receiptNo != null)
				return false;
		} else if (!receiptNo.equals(other.receiptNo))
			return false;
		
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		
		if (ward == null) {
			if (other.ward != null)
				return false;
		} else if (!ward.equals(other.ward))
			return false;
		
		return true;
	}
*/
	
	//FIXME PHOENIX remove entity's from toString
	@Override
	public String toString() {
		return new StringBuilder(250).append("EBDEtails [")
				.append("id=").append(this.getId())
				.append(", ebConsumer=").append(ebConsumer)
				.append(", billNo=").append(billNo)
				.append(", billAmount=").append(billAmount)
				.append(", month=").append(month)
				.append(", prevBillAmount=").append(prevBillAmount)
				.append(", egBillregister=").append(egBillregister)
				.append(", billDate=").append(billDate)
				.append(", status=").append(status)
				.append(", receiptNo=").append(receiptNo)
				.append(", receiptDate=").append(receiptDate)
				.append(", dueDate=").append(dueDate)
				.append(", ward=").append(ward)
				.append(", area=").append(area)
				.append(", position=").append(position)
				.append("]").toString();
	}
	
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Boundary getWard() {
		return ward;
	}

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public TargetArea getArea() {
		return area;
	}

	public void setArea(TargetArea area) {
		this.area = area;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public boolean getIsProcess() {
		return isProcess;
	}

	public void setIsProcess(boolean isProcess) {
		this.isProcess = isProcess;
	}

	public void setProcess(boolean isProcess) {
		this.isProcess = isProcess;
	}
	
	public BigDecimal getVariance() {
		return variance;
	}

	public void setVariance(BigDecimal variance) {
		this.variance = variance;
	}

	public boolean getIsHighlight() {
		return isHighlight;
	}

	public void setIsHighlight(boolean isHighlight) {
		this.isHighlight = isHighlight;
	}
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public CFinancialYear getFinancialyear() {
		return financialyear;
	}

	public void setFinancialyear(CFinancialYear financialyear) {
		this.financialyear = financialyear;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
