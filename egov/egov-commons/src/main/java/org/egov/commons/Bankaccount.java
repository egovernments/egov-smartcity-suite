/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
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
    private Set<EgSurrenderedCheques> egSurrenderedChequeses = new HashSet<>(0);

    @ManyToOne
    @JoinColumn(name = "chequeformatid")
    private ChequeFormat chequeformat;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Bankbranch getBankbranch() {
        return bankbranch;
    }

    public void setBankbranch(final Bankbranch bankbranch) {
        this.bankbranch = bankbranch;
    }

    public CChartOfAccounts getChartofaccounts() {
        return chartofaccounts;
    }

    public void setChartofaccounts(final CChartOfAccounts chartofaccounts) {
        this.chartofaccounts = chartofaccounts;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(final String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(final String accounttype) {
        this.accounttype = accounttype;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public BankAccountType getType() {
        return type;
    }

    public void setType(final BankAccountType type) {
        this.type = type;
    }

    public Set<EgSurrenderedCheques> getEgSurrenderedChequeses() {
        return egSurrenderedChequeses;
    }

    public void setEgSurrenderedChequeses(final Set<EgSurrenderedCheques> egSurrenderedChequeses) {
        this.egSurrenderedChequeses = egSurrenderedChequeses;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(final String payTo) {
        this.payTo = payTo;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(final Boolean isactive) {
        this.isactive = isactive;
    }

    public ChequeFormat getChequeformat() {
        return chequeformat;
    }

    public void setChequeformat(final ChequeFormat chequeformat) {
        this.chequeformat = chequeformat;
    }

}