package org.egov.works.models.masters;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.Bank;
import org.egov.commons.Bidder;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;
import javax.validation.Valid;

@Unique(fields={"code"},id="id",tableName="EGW_CONTRACTOR",columnName={"CODE"},message="contractor.code.isunique")
public class Contractor  extends BaseModel implements EntityType,Bidder{
	
	@Required(message="contractor.code.null")
	@Length(max=50,message="contractor.code.length")
	@OptionalPattern(regex=WorksConstants.alphaNumericwithspecialchar,message="contractor.code.alphaNumeric")  
	private String code;
	 	 
	@Required(message="contractor.name.null") 
	@Length(max=100,message="contractor.name.length")
	private String name;
	
	@Length(max=250,message="contractor.correspondenceAddress.length")
	private String correspondenceAddress;
	
	@Length(max=250,message="contractor.paymentAddress.length")
	private String paymentAddress;
	
	@Length(max=100,message="contractor.contactPerson.length")
	private String contactPerson;

	@OptionalPattern(regex=ValidatorConstants.email,message="contractor.email.invalid")
	@Length(max=120,message="contractor.email.length")
	private String email;
	
	@Length(max=250,message="contractor.narration.length")
	private String narration;
	
	@Length(max=14,message="contractor.panNumber.length")
	@OptionalPattern(regex=ValidatorConstants.alphaNumeric,message="contractor.panNumber.alphaNumeric")
	private String panNumber;
	
	@Length(max=14,message="contractor.tinNumber.length")
	@OptionalPattern(regex=ValidatorConstants.alphaNumeric,message="contractor.tinNumber.alphaNumeric")
	private String tinNumber;
	
	private Bank bank;
	
	@Length(max=15,message="contractor.ifscCode.length")
	@OptionalPattern(regex=ValidatorConstants.alphaNumeric,message="contractor.ifscCode.alphaNumeric")
	private String ifscCode;
	
	@Length(max=15,message="contractor.bankAccount.length")
	@OptionalPattern(regex=ValidatorConstants.alphaNumeric,message="contractor.bankAccount.alphaNumeric")
	private String bankAccount;
	
	@Length(max=50,message="contractor.pwdApprovalCode.length")
	@OptionalPattern(regex=ValidatorConstants.caseNumberRegx,message="contractor.pwdApprovalCode.alphaNumeric")
	private String pwdApprovalCode;
	
	
	
	@Valid
	private List<ContractorDetail> contractorDetails = new LinkedList<ContractorDetail>();
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCorrespondenceAddress() {
		return correspondenceAddress;
	}

	public void setCorrespondenceAddress(String correspondenceAddress) {
		this.correspondenceAddress = StringEscapeUtils.unescapeHtml(correspondenceAddress);
	}

	public String getPaymentAddress() {
		return paymentAddress;
	}

	public void setPaymentAddress(String paymentAddress) {
		this.paymentAddress = StringEscapeUtils.unescapeHtml(paymentAddress);
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = StringEscapeUtils.unescapeHtml(narration);
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getPwdApprovalCode() {
		return pwdApprovalCode;
	}

	public void setPwdApprovalCode(String pwdApprovalCode) {
		this.pwdApprovalCode = pwdApprovalCode;
	}

	public List<ContractorDetail> getContractorDetails() {
		return contractorDetails;
	}

	public void setContractorDetails(List<ContractorDetail> contractorDetails) {
		this.contractorDetails = contractorDetails;
	}
	
	public void addContractorDetail(ContractorDetail contractorDetail){
		this.contractorDetails.add(contractorDetail);
	}
	
	public List<ValidationError> validate()	{
		List<ValidationError> errorList = null;
		if(contractorDetails!= null && contractorDetails.isEmpty()) {
			return Arrays.asList(new ValidationError("contractorDetails","contractor.details.altleastone_details_needed"));
		}
		else if(contractorDetails!= null && !contractorDetails.isEmpty()) {			
			for (ContractorDetail contractorDetail : contractorDetails) {				
				errorList = contractorDetail.validate();
				if(errorList!=null)	{
					return errorList;
				}
			}			
		}
		return errorList;
	}

	public String getBankaccount() {
		// TODO Auto-generated method stub
		return this.bankAccount;
	}

	public String getBankname() {
		// TODO Auto-generated method stub
		return this.bank.getName();
	}

	public String getIfsccode() {
		// TODO Auto-generated method stub
		return this.ifscCode;
	}

	public String getPanno() {
		// TODO Auto-generated method stub
		return this.panNumber;
	}

	public String getTinno() {
		// TODO Auto-generated method stub
		return this.tinNumber;
	}

	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEntityId() {
		return Integer.valueOf(id.intValue());
	}
	
	@Override
	public String getEntityDescription() {
		
		return getName();
	}	
	
	@Override
	public String getAddress() {
		return getPaymentAddress();
}

	@Override
	public String getBidderType() {
		return "CONTRACTOR";
	}
	
	@Override
	public Integer getBidderId() {
		return this.id.intValue();
	}
}

