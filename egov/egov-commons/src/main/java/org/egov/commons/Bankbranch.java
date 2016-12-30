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
package org.egov.commons;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BANKBRANCH")
@SequenceGenerator(name = Bankbranch.SEQ_BANKBRANCH, sequenceName = Bankbranch.SEQ_BANKBRANCH, allocationSize = 1)
@Unique(fields = { "branchMICR" }, enableDfltMsg = true)
public class Bankbranch extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -1445070413847273114L;

    public static final String SEQ_BANKBRANCH = "SEQ_BANKBRANCH";

    @Id
    @GeneratedValue(generator = SEQ_BANKBRANCH, strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bankid")
    private Bank bank;

    @Required
    @Length(max = 50)
    private String branchcode;

    @Required
    @Length(max = 50)
    private String branchname;

    @Required
    @Length(max = 50)
    private String branchaddress1;

    @Length(max = 50)
    private String branchaddress2;

    @Length(max = 50)
    private String branchcity;

    @Length(max = 50)
    private String branchstate;

    @Length(max = 50)
    private String branchpin;

    @Length(max = 15)
    private String branchphone;

    @Length(max = 15)
    private String branchfax;

    @Length(max = 50)
    private String contactperson;

    @Required
    private Boolean isactive;

    @Length(max = 250)
    private String narration;

    @Length(max = 50)
    @Column(name = "micr")
    private String branchMICR;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bankbranch", targetEntity = Bankaccount.class)
    @JsonIgnore
    private Set<Bankaccount> bankaccounts = new HashSet<>(0);

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby")
    private User createdBy;

    @JsonIgnore
    private Date createdDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastModifiedBy")
    private User lastModifiedBy;

    @JsonIgnore
    private Date lastModifiedDate;

    public boolean isAccountsExist() {
        return bankaccounts != null && !bankaccounts.isEmpty();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(final Bank bank) {
        this.bank = bank;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(final String branchcode) {
        this.branchcode = branchcode;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(final String branchname) {
        this.branchname = branchname;
    }

    public String getBranchaddress1() {
        return branchaddress1;
    }

    public void setBranchaddress1(final String branchaddress1) {
        this.branchaddress1 = branchaddress1;
    }

    public String getBranchaddress2() {
        return branchaddress2;
    }

    public void setBranchaddress2(final String branchaddress2) {
        this.branchaddress2 = branchaddress2;
    }

    public String getBranchcity() {
        return branchcity;
    }

    public void setBranchcity(final String branchcity) {
        this.branchcity = branchcity;
    }

    public String getBranchstate() {
        return branchstate;
    }

    public void setBranchstate(final String branchstate) {
        this.branchstate = branchstate;
    }

    public String getBranchpin() {
        return branchpin;
    }

    public void setBranchpin(final String branchpin) {
        this.branchpin = branchpin;
    }

    public String getBranchphone() {
        return branchphone;
    }

    public void setBranchphone(final String branchphone) {
        this.branchphone = branchphone;
    }

    public String getBranchfax() {
        return branchfax;
    }

    public void setBranchfax(final String branchfax) {
        this.branchfax = branchfax;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(final String contactperson) {
        this.contactperson = contactperson;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getBranchMICR() {
        return branchMICR;
    }

    public void setBranchMICR(final String branchMICR) {
        this.branchMICR = branchMICR;
    }

    public Set<Bankaccount> getBankaccounts() {
        return bankaccounts;
    }

    public void setBankaccounts(final Set<Bankaccount> bankaccounts) {
        this.bankaccounts = bankaccounts;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(final Boolean isactive) {
        this.isactive = isactive;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}