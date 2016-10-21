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

import org.egov.commons.utils.BankAccountType;
import org.egov.infra.persistence.entity.AbstractAuditable;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.egov.commons.Bankaccount.SEQ_BANKACCOUNT;

@Entity
@Table(name = "BANKACCOUNT")
@SequenceGenerator(name = SEQ_BANKACCOUNT, sequenceName = SEQ_BANKACCOUNT, allocationSize = 1)
public class Bankaccount extends AbstractAuditable implements java.io.Serializable {

    public static final String SEQ_BANKACCOUNT = "SEQ_BANKACCOUNT";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = SEQ_BANKACCOUNT, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "branchid", nullable = true)
    private Bankbranch bankbranch;
    @ManyToOne
    @JoinColumn(name = "glcodeid")
    private CChartOfAccounts chartofaccounts;
    @ManyToOne
    @JoinColumn(name = "fundid")
    private Fund fund;
    @NotNull
    private String accountnumber;
    private String accounttype;
    private String narration;
    @NotNull
    private Boolean isactive;
    private String payTo;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BankAccountType type;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bankaccount")
    private Set<EgSurrenderedCheques> egSurrenderedChequeses = new HashSet<EgSurrenderedCheques>(0);

    @ManyToOne
    @JoinColumn(name = "chequeformatid")
    private ChequeFormat chequeformat;

    public Bankaccount() {
        // For hibernate to work
    }

    public Bankaccount(Bankbranch bankbranch, String accountnumber, String accounttype, Boolean isactive, Date created,
                       BigDecimal modifiedby, Date lastmodified, BigDecimal currentbalance, String payTo, BankAccountType type) {
        this.bankbranch = bankbranch;
        this.accountnumber = accountnumber;
        this.accounttype = accounttype;
        this.isactive = isactive;
        this.payTo = payTo;
        this.type = type;
    }

    public Bankaccount(Bankbranch bankbranch, CChartOfAccounts chartofaccounts, Fund fund, String accountnumber,
                       String accounttype, String narration, Boolean isactive, Date created, BigDecimal modifiedby, Date lastmodified,
                       BigDecimal currentbalance,
                       String payTo, Set<EgSurrenderedCheques> egSurrenderedChequeses) {
        this.bankbranch = bankbranch;
        this.chartofaccounts = chartofaccounts;
        this.fund = fund;
        this.accountnumber = accountnumber;
        this.accounttype = accounttype;
        this.narration = narration;
        this.isactive = isactive;
        this.payTo = payTo;
        this.egSurrenderedChequeses = egSurrenderedChequeses;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bankbranch getBankbranch() {
        return this.bankbranch;
    }

    public void setBankbranch(Bankbranch bankbranch) {
        this.bankbranch = bankbranch;
    }

    public CChartOfAccounts getChartofaccounts() {
        return this.chartofaccounts;
    }

    public void setChartofaccounts(CChartOfAccounts chartofaccounts) {
        this.chartofaccounts = chartofaccounts;
    }

    public Fund getFund() {
        return this.fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public String getAccountnumber() {
        return this.accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getAccounttype() {
        return this.accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getNarration() {
        return this.narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public BankAccountType getType() {
        return type;
    }

    public void setType(BankAccountType type) {
        this.type = type;
    }

    public Set<EgSurrenderedCheques> getEgSurrenderedChequeses() {
        return this.egSurrenderedChequeses;
    }

    public void setEgSurrenderedChequeses(Set<EgSurrenderedCheques> egSurrenderedChequeses) {
        this.egSurrenderedChequeses = egSurrenderedChequeses;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(String payTo) {
        this.payTo = payTo;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public ChequeFormat getChequeformat() {
        return this.chequeformat;
    }

    public void setChequeformat(ChequeFormat chequeformat) {
        this.chequeformat = chequeformat;
    }

}