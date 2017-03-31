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
package org.egov.works.masters.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Bank;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_CONTRACTOR")
@Unique(fields = { "code" }, enableDfltMsg = true)
@NamedQueries({
        @NamedQuery(name = Contractor.GET_CONTRACTORS_BY_STATUS, query = " select distinct cont from Contractor cont inner join cont.contractorDetails as cd where cd.status.description = ? ") })
@SequenceGenerator(name = Contractor.SEQ_EGW_CONTRACTOR, sequenceName = Contractor.SEQ_EGW_CONTRACTOR, allocationSize = 1)
public class Contractor extends AbstractAuditable implements EntityType {

    private static final long serialVersionUID = 6858362239507609219L;
    public static final String SEQ_EGW_CONTRACTOR = "SEQ_EGW_CONTRACTOR";
    public static final String GET_CONTRACTORS_BY_STATUS = "GET_CONTRACTORS_BY_STATUS";

    @Id
    @GeneratedValue(generator = SEQ_EGW_CONTRACTOR, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 50, message = "contractor.code.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHSPECIALCHAR, message = "contractor.code.alphaNumeric")
    private String code;

    @Required(message = "contractor.name.null")
    @Length(max = 100, message = "contractor.name.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHSPECIALCHAR, message = "contractor.name.alphaNumeric")
    private String name;

    @Length(max = 250, message = "contractor.correspondenceAddress.length")
    @Column(name = "CORRESPONDENCE_ADDRESS")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "contractor.correspondenceaddress.alphaNumeric")
    private String correspondenceAddress;

    @Length(max = 250, message = "contractor.paymentAddress.length")
    @Column(name = "PAYMENT_ADDRESS")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "contractor.paymentaddress.alphaNumeric")
    private String paymentAddress;

    @Length(max = 100, message = "contractor.contactPerson.length")
    @OptionalPattern(regex = Constants.ALPHANUMERIC_WITHSPACE, message = "contractor.contactPerson.alphaNumeric")
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;

    @OptionalPattern(regex = Constants.EMAIL, message = "contractor.email.invalid")
    @Length(max = 100, message = "contractor.email.length")
    private String email;

    @Length(max = 1024, message = "contractor.narration.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "contractor.narration.alphaNumeric")
    private String narration;

    @Length(max = 10, message = "contractor.panNumber.length")
    @OptionalPattern(regex = Constants.PANNUMBER, message = "contractor.panNumber.alphaNumeric")
    @Column(name = "PAN_NUMBER")
    private String panNumber;

    @Length(max = 14, message = "contractor.tinNumber.length")
    @OptionalPattern(regex = Constants.ALPHANUMERIC, message = "contractor.tinNumber.alphaNumeric")
    @Column(name = "TIN_NUMBER")
    private String tinNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @Length(max = 15, message = "contractor.ifscCode.length")
    @OptionalPattern(regex = Constants.ALPHANUMERIC, message = "contractor.ifscCode.alphaNumeric")
    @Column(name = "IFSC_CODE")
    private String ifscCode;

    @Length(max = 22, message = "contractor.bankAccount.length")
    @OptionalPattern(regex = Constants.ALPHANUMERIC, message = "contractor.bankAccount.alphaNumeric")
    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    @Length(max = 50, message = "contractor.pwdApprovalCode.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHSPECIALCHAR, message = "contractor.pwdApprovalCode.alphaNumeric")
    @Column(name = "PWD_APPROVAL_CODE")
    private String pwdApprovalCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "EXEMPTION")
    private ExemptionForm exemptionForm;

    @OptionalPattern(regex = Constants.MOBILE_NUM, message = "depositworks.roadcut.invalid.mobileno")
    @Length(max = 10)
    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "contractor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ContractorDetail.class)
    private final List<ContractorDetail> contractorDetails = new ArrayList<ContractorDetail>(0);

    @Transient
    private List<ContractorDetail> tempContractorDetails = new ArrayList<ContractorDetail>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCorrespondenceAddress() {
        return correspondenceAddress;
    }

    public void setCorrespondenceAddress(final String correspondenceAddress) {
        this.correspondenceAddress = correspondenceAddress;
    }

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public void setPaymentAddress(final String paymentAddress) {
        this.paymentAddress = paymentAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(final String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(final String panNumber) {
        this.panNumber = panNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(final String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(final Bank bank) {
        this.bank = bank;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(final String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(final String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPwdApprovalCode() {
        return pwdApprovalCode;
    }

    public void setPwdApprovalCode(final String pwdApprovalCode) {
        this.pwdApprovalCode = pwdApprovalCode;
    }

    public ExemptionForm getExemptionForm() {
        return exemptionForm;
    }

    public void setExemptionForm(final ExemptionForm exemptionForm) {
        this.exemptionForm = exemptionForm;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<ContractorDetail> getContractorDetails() {
        return contractorDetails;
    }

    public List<ContractorDetail> getTempContractorDetails() {
        return tempContractorDetails;
    }

    public void setTempContractorDetails(final List<ContractorDetail> tempContractorDetails) {
        this.tempContractorDetails = tempContractorDetails;
    }

    @Override
    public String getBankname() {
        if (bank == null)
            return StringUtils.EMPTY;
        else
            return bank.getName();

    }

    @Override
    public String getBankaccount() {
        return bankAccount;
    }

    @Override
    public String getPanno() {
        return panNumber;
    }

    @Override
    public String getTinno() {
        return tinNumber;
    }

    @Override
    public String getIfsccode() {
        return ifscCode;
    }

    @Override
    public String getModeofpay() {
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
    public EgwStatus getEgwStatus() {
        return null;
    }

}
