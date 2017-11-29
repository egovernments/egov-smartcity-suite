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
/**
 *
 */
package org.egov.model.contra;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author msahoo
 *
 */
@Entity
@Table(name = "TRANSACTIONSUMMARY")
@SequenceGenerator(name = TransactionSummary.SEQ_TRANSACTIONSUMMARY, sequenceName = TransactionSummary.SEQ_TRANSACTIONSUMMARY, allocationSize = 1)
public class TransactionSummary extends AbstractAuditable{
	
	private static final long serialVersionUID = -4555037259173138199L;
    public static final String SEQ_TRANSACTIONSUMMARY = "SEQ_TRANSACTIONSUMMARY";
    
    @Id
    @GeneratedValue(generator = SEQ_TRANSACTIONSUMMARY, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ACCOUNTDETAILTYPEID")
    private Accountdetailtype accountdetailtype;
    
    @ManyToOne
    @JoinColumn(name = "FINANCIALYEARID")
    private CFinancialYear financialyear;
    
    @ManyToOne
    @JoinColumn(name = "FUNDSOURCEID")
    private Fundsource fundsource;
    
    @ManyToOne
    @JoinColumn(name = "FUNDID")
    private Fund fund;
    
    @ManyToOne
    @JoinColumn(name = "GLCODEID")
    private CChartOfAccounts glcodeid;
    
    @Transient
    private String glcodeDetail;
    
    private BigDecimal openingdebitbalance;
    
    private BigDecimal openingcreditbalance;
    
    private Integer accountdetailkey;
    
    @Length(max = 300)
    private String narration;
    
    @ManyToOne
    @JoinColumn(name="DEPARTMENTID")
    private Department departmentid;
    
    @ManyToOne
    @JoinColumn(name="FUNCTIONARYID")
    private Functionary functionaryid;
    
    @ManyToOne
    @JoinColumn(name="FUNCTIONID")
    private CFunction functionid;
    
    private Integer divisionid;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Accountdetailtype getAccountdetailtype() {
        return accountdetailtype;
    }

    public void setAccountdetailtype(final Accountdetailtype accountdetailtype) {
        this.accountdetailtype = accountdetailtype;
    }

    public CFinancialYear getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(final CFinancialYear financialyear) {
        this.financialyear = financialyear;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(final Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public CChartOfAccounts getGlcodeid() {
        return glcodeid;
    }

    public void setGlcodeid(final CChartOfAccounts glcodeid) {
        this.glcodeid = glcodeid;
    }

    public BigDecimal getOpeningdebitbalance() {
        return openingdebitbalance;
    }

    public void setOpeningdebitbalance(final BigDecimal openingdebitbalance) {
        this.openingdebitbalance = openingdebitbalance;
    }

    public BigDecimal getOpeningcreditbalance() {
        return openingcreditbalance;
    }

    public void setOpeningcreditbalance(final BigDecimal openingcreditbalance) {
        this.openingcreditbalance = openingcreditbalance;
    }

    public Integer getAccountdetailkey() {
        return accountdetailkey;
    }

    public void setAccountdetailkey(final Integer accountdetailkey) {
        this.accountdetailkey = accountdetailkey;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public Functionary getFunctionaryid() {
		return functionaryid;
	}

	public void setFunctionaryid(final Functionary functionaryid) {
        this.functionaryid = functionaryid;
    }

    public CFunction getFunctionid() {
        return functionid;
    }

    public void setFunctionid(final CFunction functionid) {
        this.functionid = functionid;
    }

    public Integer getDivisionid() {
        return divisionid;
    }

    public void setDivisionid(final Integer divisionid) {
        this.divisionid = divisionid;
    }

    public Department getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(final Department departmentid) {
        this.departmentid = departmentid;
    }

	public String getGlcodeDetail() {
		return glcodeDetail;
	}

	public void setGlcodeDetail(String glcodeDetail) {
		this.glcodeDetail = glcodeDetail;
	}
}
