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

@Unique(fields = { "advocateName", "mobileNumber", "pannumber" }, id = "id", columnName = {
        "ADVOCATE_NAME", "MOBILE_NUMBER", "PANNUMBER" }, tableName = "EGLC_ADVOCATE_MASTER", message = "advocate.mobNo.isunique")
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

    private Boolean isRetaineradvocate ;
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

    public void setIsPay(boolean isPay) {
        this.isPay = isPay;
    }

    public Long getApprovedFee() {
        return approvedFee;
    }

    public void setApprovedFee(Long approvedFee) {
        this.approvedFee = approvedFee;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Bank getEglBank() {
        return eglBank;
    }

    public void setEglBank(Bank eglBank) {
        this.eglBank = eglBank;
    }

    public Bankbranch getEglbankbranch() {
        return eglbankbranch;
    }

    public void setEglbankbranch(Bankbranch eglbankbranch) {
        this.eglbankbranch = eglbankbranch;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getAdvocateName() {
        return this.advocateName;
    }

    public void setAdvocateName(String advocateName) {
        this.advocateName = advocateName;
    }

    public String getAdvocateAddr() {
        return this.advocateAddr;
    }

    public void setAdvocateAddr(String advocateAddr) {
        this.advocateAddr = advocateAddr;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAdvocateSpecialty() {
        return this.advocateSpecialty;
    }

    public void setAdvocateSpecialty(String advocateSpecialty) {
        this.advocateSpecialty = advocateSpecialty;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   

    public String getFirmname() {
        return this.firmname;
    }

    public void setFirmname(String firmname) {
        this.firmname = firmname;
    }

    public String getPannumber() {
        return this.pannumber;
    }

    public void setPannumber(String pannumber) {
        this.pannumber = pannumber;
    }

    

    public Boolean getIsRetaineradvocate() {
		return isRetaineradvocate;
	}

	public void setIsRetaineradvocate(Boolean isRetaineradvocate) {
		this.isRetaineradvocate = isRetaineradvocate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsSenioradvocate() {
        return isSenioradvocate;
    }

    public void setIsSenioradvocate(boolean isSenioradvocate) {
        this.isSenioradvocate = isSenioradvocate;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getIfsccode() {
        return ifsccode;
    }

    public void setIfsccode(String ifsccode) {
        this.ifsccode = ifsccode;
    }

    public String getTinumber() {
        return tinumber;
    }

    public void setTinumber(String tinumber) {
        this.tinumber = tinumber;
    }

    public void setSenioradvocate(boolean isSenioradvocate) {
        this.isSenioradvocate = isSenioradvocate;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        /*
         * if("Cheque".equals(paymentmode)) { if(this.eglBank==null) { errors.add(new
         * ValidationError(ERROR_KEY,"advocate.err.bankname")); } if(StringUtils.isBlank(bankaccount)) { errors.add(new
         * ValidationError(ERROR_KEY,"advocate.err.bankaccount")); } if(this.eglbankbranch==null) { errors.add(new
         * ValidationError(ERROR_KEY,"advocate.err.bankbranch")); } } else
         */
        if ("RTGS".equals(this.paymentmode)) {
            if (eglBank == null) {
                errors.add(new ValidationError(LcmsConstants.ERROR_KEY,
                        "advocate.err.bankname"));
            }

            if (StringUtils.isBlank(bankaccount)) {
                errors.add(new ValidationError(LcmsConstants.ERROR_KEY,
                        "advocate.err.bankaccount"));
            }
            if (eglbankbranch == null) {
                errors.add(new ValidationError(LcmsConstants.ERROR_KEY,
                        "advocate.err.bankbranch"));
            }

            if (StringUtils.isBlank(ifsccode)) {
                errors.add(new ValidationError(LcmsConstants.ERROR_KEY,
                        "advocate.err.ifsccode"));
            }

            if (StringUtils.isBlank(tinumber)) {
                errors.add(new ValidationError(LcmsConstants.ERROR_KEY,
                        "advocate.err.tinumbe"));
            }
        }

        return errors;
    }

    @Override
    public String getBankname() {
        // TODO Auto-generated method stub
        return this.eglBank.getName();
    }

    @Override
    public String getCode() {
        // TODO Auto-generated method stub\
        return this.advocateName;
    }

    @Override
    public String getModeofpay() {
        // TODO Auto-generated method stub
        return this.paymentmode;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.advocateName;
    }

    @Override
    public String getPanno() {
        // TODO Auto-generated method stub
        return this.pannumber;
    }

    @Override
    public String getTinno() {
        // TODO Auto-generated method stub
        return this.tinumber;
    }

    public Long getMonthlyRenumeration() {
        return monthlyRenumeration;
    }

    public void setMonthlyRenumeration(Long monthlyRenumeration) {
        this.monthlyRenumeration = monthlyRenumeration;
    }

    public Integer getEntityId() {
        return Integer.valueOf(this.id.toString());
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