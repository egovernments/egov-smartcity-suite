/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.models.extd;

import org.egov.commons.Bank;
import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

public class RegistrationDDDetailsExtn extends BaseModel {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal lwddAmount;
	private Date lwddDate;
	private String lwddNumber;
	private Integer lwddbankName;
	private BigDecimal msddAmount;
	private Date msddDate;
	private String msddNumber;
	private Integer msddbankName;
	private RegistrationExtn registration;
	private BigDecimal ddAmount;
	private Date ddDate;
	private String ddNumber;
	private Bank ddBank;

	public Integer getLwddbankName() {
		return lwddbankName;
	}

	public void setLwddbankName(Integer lwddbankName) {
		this.lwddbankName = lwddbankName;
	}

	public Integer getMsddbankName() {
		return msddbankName;
	}

	public void setMsddbankName(Integer msddbankName) {
		this.msddbankName = msddbankName;
	}

	public Bank getDdBank() {
		return ddBank;
	}

	public void setDdBank(Bank ddBank) {
		this.ddBank = ddBank;
	}

	private String ddType;

	public BigDecimal getDdAmount() {
		return ddAmount;
	}

	public void setDdAmount(BigDecimal ddAmount) {
		this.ddAmount = ddAmount;
	}

	public Date getDdDate() {
		return ddDate;
	}

	public void setDdDate(Date ddDate) {
		this.ddDate = ddDate;
	}

	public String getDdNumber() {
		return ddNumber;
	}

	public void setDdNumber(String ddNumber) {
		this.ddNumber = ddNumber;
	}

	public String getDdType() {
		return ddType;
	}

	public void setDdType(String ddType) {
		this.ddType = ddType;
	}

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public BigDecimal getLwddAmount() {
		return lwddAmount;
	}

	public void setLwddAmount(BigDecimal lwddAmount) {
		this.lwddAmount = lwddAmount;
	}

	public Date getLwddDate() {
		return lwddDate;
	}

	public void setLwddDate(Date lwddDate) {
		this.lwddDate = lwddDate;
	}

	public String getLwddNumber() {
		return lwddNumber;
	}

	public void setLwddNumber(String lwddNumber) {
		this.lwddNumber = lwddNumber;
	}

	public BigDecimal getMsddAmount() {
		return msddAmount;
	}

	public void setMsddAmount(BigDecimal msddAmount) {
		this.msddAmount = msddAmount;
	}

	public Date getMsddDate() {
		return msddDate;
	}

	public void setMsddDate(Date msddDate) {
		this.msddDate = msddDate;
	}

	public String getMsddNumber() {
		return msddNumber;
	}

	public void setMsddNumber(String msddNumber) {
		this.msddNumber = msddNumber;
	}

}
