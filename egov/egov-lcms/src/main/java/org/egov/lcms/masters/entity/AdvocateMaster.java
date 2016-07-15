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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eglc_advocate_master")
@Unique(id = "id", tableName = "eglc_advocate_master", columnName = { "name", "mobilenumber", "pannumber" }, fields = {
        "name", "mobileNumber", "panNumber" }, enableDfltMsg = true)
@SequenceGenerator(name = AdvocateMaster.SEQ_ADVOCATE_MASTER, sequenceName = AdvocateMaster.SEQ_ADVOCATE_MASTER, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class AdvocateMaster extends AbstractAuditable {

    private static final long serialVersionUID = 796823780349590496L;
    public static final String SEQ_ADVOCATE_MASTER = "SEQ_EGLC_ADVOCATE_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_ADVOCATE_MASTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Audited
    @Length(max = 20)
    private String salutation;

    @NotNull
    @Length(min = 3, max = 100)
    @Audited
    private String name;

    @Length(min = 3, max = 128)
    @Audited
    private String address;

    @Audited
    @Length(max = 20)
    @SafeHtml
    private String contactPhone;

    @Audited
    @Length(max = 20)
    private String specilization;

    @Audited
    @Length(max = 10)
    @SafeHtml
    @Pattern(regexp = LcmsConstants.lengthCheckForMobileNo)
    private String mobileNumber;

    @Audited
    @Email(regexp = LcmsConstants.email)
    @SafeHtml
    private String email;

    @NotNull
    @Min(value = 1)
    @Audited
    private double monthlyRenumeration;

    @NotNull
    @Audited
    private Boolean isRetaineradvocate;

    @Audited
    private String firmName;

    @NotNull
    @Length(max = 20)
    @Audited
    private String panNumber;

    @Audited
    @NotNull
    private Boolean isActive;

    @Audited
    @NotNull
    private boolean isSenioradvocate;

    @Audited
    private String paymentmode;

    @Length(max = 20)
    @Audited
    private String bankaccount;

    @Length(max = 20)
    @Audited
    private String ifsccode;

    @Length(max = 20)
    @Audited
    private String tinumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankname")
    @NotAudited
    private Bank bankName;

    @Audited
    private double fee;

    @Length(max = 256)
    @Audited
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankbranch")
    @NotAudited
    private Bankbranch bankBranch;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(final String salutation) {
        this.salutation = salutation;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(final String contactPhone) {
        this.contactPhone = contactPhone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getSpecilization() {
        return specilization;
    }

    public void setSpecilization(final String specilization) {
        this.specilization = specilization;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(final String firmName) {
        this.firmName = firmName;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(final String panNumber) {
        this.panNumber = panNumber;
    }

    public Bank getBankName() {
        return bankName;
    }

    public void setBankName(final Bank bankName) {
        this.bankName = bankName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(final double fee) {
        this.fee = fee;
    }

    public Bankbranch getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(final Bankbranch bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(final String bankaccount) {
        this.bankaccount = bankaccount;
    }

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

    public double getMonthlyRenumeration() {
        return monthlyRenumeration;
    }

    public void setMonthlyRenumeration(final double monthlyRenumeration) {
        this.monthlyRenumeration = monthlyRenumeration;
    }

}