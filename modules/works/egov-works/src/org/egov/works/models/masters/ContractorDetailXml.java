package org.egov.works.models.masters;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Department")
public class ContractorDetailXml {
	
	private String cclass;
	private Date fromDate;
	private Date toDate;
	private String deptCode;
	
	public String getCclass() {
		return cclass;
	}
	public void setCclass(String cclass) {
		this.cclass = cclass;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	

}
