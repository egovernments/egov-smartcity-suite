/**
 * 
 */
package org.egov.egf.masters.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;  
import org.egov.commons.SubScheme;
import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentVoucher;

/**
 * @author mani used for both loans and grants
 */
public class LoanGrantHeader extends BaseModel {
	private static final long			serialVersionUID	= 7699342818798141533L;
	final static Logger					LOGGER				= Logger.getLogger(LoanGrantHeader.class);
	private SubScheme					subScheme;
	private String						councilResNo;
	private String						govtOrderNo;
	private String						amendmentNo;
	private Date 						councilResDate;
	private Date 						govtOrderDate;
	private Date 						amendmentDate;
	private BigDecimal					projectCost;
	private BigDecimal					sanctionedCost;
	private BigDecimal					revisedCost;
	private List<SubSchemeProject>	projectList=new ArrayList<SubSchemeProject>();
	private List<LoanGrantDetail>		detailList=new ArrayList<LoanGrantDetail>();
	private List<LoanGrantReceiptDetail>		receiptList=new ArrayList<LoanGrantReceiptDetail>();
	
	public SubScheme getSubScheme() {
		return subScheme;
	}
	
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	
	public String getCouncilResNo() {
		return councilResNo;
	}
	
	public void setCouncilResNo(String councilResNo) {
		this.councilResNo = councilResNo;
	}
	
	public String getGovtOrderNo() {
		return govtOrderNo;
	}
	
	public void setGovtOrderNo(String govtOrderNo) {
		this.govtOrderNo = govtOrderNo;
	}
	
	public String getAmendmentNo() {
		return amendmentNo;
	}
	
	public void setAmendmentNo(String amendmentNo) {
		this.amendmentNo = amendmentNo;
	}
	
	public BigDecimal getProjectCost() {
		return projectCost;
	}
	
	public void setProjectCost(BigDecimal projectCost) {
		this.projectCost = projectCost;
	}
	
	public BigDecimal getSanctionedCost() {
		return sanctionedCost;
	}
	
	public void setSanctionedCost(BigDecimal sanctionedCost) {
		this.sanctionedCost = sanctionedCost;
	}
	
	public BigDecimal getRevisedCost() {
		return revisedCost;
	}
	
	public void setRevisedCost(BigDecimal revisedCost) {
		this.revisedCost = revisedCost;
	}
	
	public List<SubSchemeProject> getProjectList() {
		return projectList;
	}
	
	public void setProjectList(List<SubSchemeProject> projectList) {
		this.projectList = projectList;
	}
	
	public List<LoanGrantDetail> getDetailList() {
		return detailList;
	}
	
	public void setDetailList(List<LoanGrantDetail> detailList) {
		this.detailList = detailList;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setCouncilResDate(Date councilResDate) {
		this.councilResDate = councilResDate;
	}

	public Date getCouncilResDate() {
		return councilResDate;
	}

	public void setGovtOrderDate(Date govtOrderDate) {
		this.govtOrderDate = govtOrderDate;
	}

	public Date getGovtOrderDate() {
		return govtOrderDate;
	}

	public void setAmendmentDate(Date amendmentDate) {
		this.amendmentDate = amendmentDate;
	}

	public Date getAmendmentDate() {
		return amendmentDate;
	}

	public List<LoanGrantReceiptDetail> getReceiptList() {
		return receiptList;
	}

	public void setReceiptList(List<LoanGrantReceiptDetail> receiptList) {
		this.receiptList = receiptList;
	}

	
}
