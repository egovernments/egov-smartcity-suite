/*
 * @(#)Relation.java 3.0, 6 Jun, 2013 4:30:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.utils.EntityType;

public class Relation implements java.io.Serializable, EntityType, Bidder {

	private Integer id;

	private Relationtype relationtype;

	private String code;

	private String name;

	private String address1;

	private String address2;

	private String city;

	private String pin;

	private String phone;

	private String fax;

	private String contactperson;

	private String mobile;

	private String email;

	private String narration;

	private Boolean isactive;

	private Date lastmodified;

	private Date created;

	private BigDecimal modifiedby;

	private BigDecimal tdsid;

	private BigDecimal glcodeid;

	private String panno;

	private String tinno;

	private Date inactiveon;

	private BigDecimal createdby;

	private BigDecimal statusid;

	private BigDecimal gradeid;

	private String pwdapprovalcode;

	private String regno;

	private String bankaccount;

	private String bankname;

	private String ifsccode;
	private String modeofpay;
	private String tablename = "relation";

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public Relation() {
		 //For hibernate to work
	}

	public Relation(Integer id, String code, String name, String address1, Date created, BigDecimal createdby) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.address1 = address1;
		this.created = created;
		this.createdby = createdby;
	}

	public Relation(Integer id, Relationtype relationtype, String code, String name, String address1, String address2, String city, String pin, String phone, String fax, String contactperson, String mobile, String email, String narration, Boolean isactive,
			Date lastmodified, Date created, BigDecimal modifiedby, BigDecimal tdsid, BigDecimal glcodeid, String panno, String tinno, Date inactiveon, BigDecimal createdby, BigDecimal statusid, BigDecimal gradeid, String pwdapprovalcode, String regno,
			String bankaccount, String bankname, String ifsccode) {
		this.id = id;
		this.relationtype = relationtype;
		this.code = code;
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.pin = pin;
		this.phone = phone;
		this.fax = fax;
		this.contactperson = contactperson;
		this.mobile = mobile;
		this.email = email;
		this.narration = narration;
		this.isactive = isactive;
		this.lastmodified = lastmodified;
		this.created = created;
		this.modifiedby = modifiedby;
		this.tdsid = tdsid;
		this.glcodeid = glcodeid;
		this.panno = panno;
		this.tinno = tinno;
		this.inactiveon = inactiveon;
		this.createdby = createdby;
		this.statusid = statusid;
		this.gradeid = gradeid;
		this.pwdapprovalcode = pwdapprovalcode;
		this.regno = regno;
		this.bankaccount = bankaccount;
		this.bankname = bankname;
		this.ifsccode = ifsccode;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Relationtype getRelationtype() {
		return this.relationtype;
	}

	public void setRelationtype(Relationtype relationtype) {
		this.relationtype = relationtype;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getContactperson() {
		return this.contactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(BigDecimal modifiedby) {
		this.modifiedby = modifiedby;
	}

	public BigDecimal getTdsid() {
		return this.tdsid;
	}

	public void setTdsid(BigDecimal tdsid) {
		this.tdsid = tdsid;
	}

	public BigDecimal getGlcodeid() {
		return this.glcodeid;
	}

	public void setGlcodeid(BigDecimal glcodeid) {
		this.glcodeid = glcodeid;
	}

	public String getPanno() {
		return this.panno;
	}

	public void setPanno(String panno) {
		this.panno = panno;
	}

	public String getTinno() {
		return this.tinno;
	}

	public void setTinno(String tinno) {
		this.tinno = tinno;
	}

	public Date getInactiveon() {
		return this.inactiveon;
	}

	public void setInactiveon(Date inactiveon) {
		this.inactiveon = inactiveon;
	}

	public BigDecimal getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(BigDecimal createdby) {
		this.createdby = createdby;
	}

	public BigDecimal getStatusid() {
		return this.statusid;
	}

	public void setStatusid(BigDecimal statusid) {
		this.statusid = statusid;
	}

	public BigDecimal getGradeid() {
		return this.gradeid;
	}

	public void setGradeid(BigDecimal gradeid) {
		this.gradeid = gradeid;
	}

	public String getPwdapprovalcode() {
		return this.pwdapprovalcode;
	}

	public void setPwdapprovalcode(String pwdapprovalcode) {
		this.pwdapprovalcode = pwdapprovalcode;
	}

	public String getRegno() {
		return this.regno;
	}

	public void setRegno(String regno) {
		this.regno = regno;
	}

	public String getBankaccount() {
		return this.bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getIfsccode() {
		return this.ifsccode;
	}

	public void setIfsccode(String ifsccode) {
		this.ifsccode = ifsccode;
	}

	public String getModeofpay() {
		return modeofpay;
	}

	public void setModeofpay(String modeofpay) {
		this.modeofpay = modeofpay;
	}

	@Override
	public Integer getEntityId() {
		return this.id;

	}

	@Override
	public String getEntityDescription() {
		return getName();
	}

	@Override
	public String getAddress() {
		return this.address1;
	}

	@Override
	public String getBidderType() {
		// TODO Auto-generated method stub
		return "RELATION";
	}

	@Override
	public Integer getBidderId() {
		// TODO Auto-generated method stub
		return this.id;
	}

}
