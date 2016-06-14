/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lcms.masters.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * EglcAdvocateMaster entity.
 *
 * @author MyEclipse Persistence Tools
 */

@Unique(fields = { "advocateName", "mobileNumber", "pannumber" }, id = "id", columnName = { "ADVOCATE_NAME",
		"MOBILE_NUMBER", "PANNUMBER" }, tableName = "EGLC_ADVOCATE_MASTER", message = "advocate.mobNo.isunique")
public class AdvocateMaster extends BaseModel implements EntityType {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	@Required(message = "advocate.name.null")
	@Length(max = 32, message = "advocate.name.length")
	@OptionalPattern(regex = "[0-9a-zA-Z-&, .]+", message = "advocate.name.text2")
	private String advocateName;
	@Length(max = 128, message = "advocate.addr.length")
	private String advocateAddr;
	@OptionalPattern(regex = LcmsConstants.numericiValForPhoneNo, message = "advocate.phNo.text")
	private String contactPhone;
	private String advocateSpecialty;
	@OptionalPattern(regex = LcmsConstants.lengthCheckForMobileNo, message = "advocate.mobileNo.length")
	private String mobileNumber;
	@OptionalPattern(regex = LcmsConstants.email, message = "advocate.email.invalid")
	private String email;

	@Required(message = "advocate.fee.req")
	@Min(value = 1, message = "advocate.fee.min")
	// @Max(value=999999999999,message="advocate.fee.max")
	private Long monthlyRenumeration;

	private Boolean isRetaineradvocate;
	private String firmname;
	@Required(message = "advocate.passno.null")
	@Length(max = 10, message = "advocate.pannumber.length")
	@OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "advocate.pannumber.text")
	private String pannumber;
	private Boolean isActive;
	private boolean isSenioradvocate;
	private String salutation;
	private String paymentmode;

	@Length(max = 50, message = "advocate.bankaccount.lenght")
	private String bankaccount;

	@Length(max = 11, message = "advocate.ifsccode.length")
	@OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "advocate.ifsccode.text")
	private String ifsccode;

	@Length(max = 10, message = "advocate.tinumber.length")
	@OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "advocate.tinumber.text")
	private String tinumber;

	private Bank eglBank;
	private boolean isPay;
	private Long approvedFee;
	private String remarks;

	private Bankbranch eglbankbranch;

	public boolean getIsPay() {
		return isPay;
	}

	public void setIsPay(final boolean isPay) {
		this.isPay = isPay;
	}

	public Long getApprovedFee() {
		return approvedFee;
	}

	public void setApprovedFee(final Long approvedFee) {
		this.approvedFee = approvedFee;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public Bank getEglBank() {
		return eglBank;
	}

	public void setEglBank(final Bank eglBank) {
		this.eglBank = eglBank;
	}

	public Bankbranch getEglbankbranch() {
		return eglbankbranch;
	}

	public void setEglbankbranch(final Bankbranch eglbankbranch) {
		this.eglbankbranch = eglbankbranch;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(final String salutation) {
		this.salutation = salutation;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(final String advocateName) {
		this.advocateName = advocateName;
	}

	public String getAdvocateAddr() {
		return advocateAddr;
	}

	public void setAdvocateAddr(final String advocateAddr) {
		this.advocateAddr = advocateAddr;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(final String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getAdvocateSpecialty() {
		return advocateSpecialty;
	}

	public void setAdvocateSpecialty(final String advocateSpecialty) {
		this.advocateSpecialty = advocateSpecialty;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirmname() {
		return firmname;
	}

	public void setFirmname(final String firmname) {
		this.firmname = firmname;
	}

	public String getPannumber() {
		return pannumber;
	}

	public void setPannumber(final String pannumber) {
		this.pannumber = pannumber;
	}

	public Boolean getIsRetaineradvocate() {
		return isRetaineradvocate;
	}

	public void setIsRetaineradvocate(final Boolean isRetaineradvocate) {
		this.isRetaineradvocate = isRetaineradvocate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(final Boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsSenioradvocate() {
		return isSenioradvocate;
	}

	public void setIsSenioradvocate(final boolean isSenioradvocate) {
		this.isSenioradvocate = isSenioradvocate;
	}

	public String getPaymentmode() {
		return paymentmode;
	}

	public void setPaymentmode(final String paymentmode) {
		this.paymentmode = paymentmode;
	}

	@Override
	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(final String bankaccount) {
		this.bankaccount = bankaccount;
	}

	@Override
	public String getIfsccode() {
		return ifsccode;
	}

	public void setIfsccode(final String ifsccode) {
		this.ifsccode = ifsccode;
	}

	public String getTinumber() {
		return tinumber;
	}

	public void setTinumber(final String tinumber) {
		this.tinumber = tinumber;
	}

	public void setSenioradvocate(final boolean isSenioradvocate) {
		this.isSenioradvocate = isSenioradvocate;
	}

	@Override
	public List<ValidationError> validate() {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		/*
		 * if("Cheque".equals(paymentmode)) { if(this.eglBank==null) {
		 * errors.add(new ValidationError(ERROR_KEY,"advocate.err.bankname")); }
		 * if(StringUtils.isBlank(bankaccount)) { errors.add(new
		 * ValidationError(ERROR_KEY,"advocate.err.bankaccount")); }
		 * if(this.eglbankbranch==null) { errors.add(new
		 * ValidationError(ERROR_KEY,"advocate.err.bankbranch")); } } else
		 */
		if ("RTGS".equals(paymentmode)) {
			if (eglBank == null)
				errors.add(new ValidationError(LcmsConstants.ERROR_KEY, "advocate.err.bankname"));

			if (StringUtils.isBlank(bankaccount))
				errors.add(new ValidationError(LcmsConstants.ERROR_KEY, "advocate.err.bankaccount"));
			if (eglbankbranch == null)
				errors.add(new ValidationError(LcmsConstants.ERROR_KEY, "advocate.err.bankbranch"));

			if (StringUtils.isBlank(ifsccode))
				errors.add(new ValidationError(LcmsConstants.ERROR_KEY, "advocate.err.ifsccode"));

			if (StringUtils.isBlank(tinumber))
				errors.add(new ValidationError(LcmsConstants.ERROR_KEY, "advocate.err.tinumbe"));
		}

		return errors;
	}

	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return eglBank.getName();
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub\
		return advocateName;
	}

	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return paymentmode;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return advocateName;
	}

	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return pannumber;
	}

	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return tinumber;
	}

	public Long getMonthlyRenumeration() {
		return monthlyRenumeration;
	}

	public void setMonthlyRenumeration(final Long monthlyRenumeration) {
		this.monthlyRenumeration = monthlyRenumeration;
	}

	@Override
	public Integer getEntityId() {
		return Integer.valueOf(id.toString());
	}

	@Override
	public String getEntityDescription() {

		return getName();
	}

	@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}