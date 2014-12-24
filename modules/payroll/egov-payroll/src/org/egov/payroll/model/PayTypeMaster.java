package org.egov.payroll.model;


import java.util.Date;

import org.egov.lib.rjbac.user.User;

public class PayTypeMaster implements java.io.Serializable{
	private Integer id;
	private String narration;
	private User createdby;
	private Date createddate;
	private String paytype ;
	private String lastmodifiedby ;
	private String lastmodifieddate ;
	private String billType ;
		
	public Integer getId()
	{
		return this.id;
	}
	public void setId(Integer id)
	{
		this.id=id;
	}
	public PayTypeMaster(Integer id,String narration,User createdby,Date createddate,String paytype) {
		this.id = id;
		this.narration = narration;
		this.createdby=createdby;
		this.createddate=createddate;
	}	
	public PayTypeMaster() {
		// TODO Auto-generated constructor stub
	}		
	public User getCreatedby() {
		return createdby;
	}
	public void setCreatedby(User createdby) {
		this.createdby = createdby;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public String getLastmodifiedby() {
		return lastmodifiedby;
	}
	public void setLastmodifiedby(String lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}
	public String getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(String lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	
}
