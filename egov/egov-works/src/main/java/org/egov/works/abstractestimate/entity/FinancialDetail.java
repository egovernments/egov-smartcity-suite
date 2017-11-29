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
package org.egov.works.abstractestimate.entity;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.model.budget.BudgetGroup;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EGW_ESTIMATE_FINANCIALDETAIL")
@NamedQueries({
        @NamedQuery(name = FinancialDetail.FINANCIALDETAILS_BY_ESTIMATEID, query = "from FinancialDetail fd where fd.abstractEstimate.id = ?") })
@SequenceGenerator(name = FinancialDetail.SEQ_EGW_ESTIMATEFINANCIALDETAIL, sequenceName = FinancialDetail.SEQ_EGW_ESTIMATEFINANCIALDETAIL, allocationSize = 1)
public class FinancialDetail extends AbstractAuditable {

    private static final long serialVersionUID = 3577937589290853091L;

    public static final String SEQ_EGW_ESTIMATEFINANCIALDETAIL = "SEQ_EGW_ESTIMATE_FINANCIALDETAIL";
    public static final String FINANCIALDETAILS_BY_ESTIMATEID = "FINANCIALDETAILS_BY_ESTIMATEID";

    @Id
    @GeneratedValue(generator = SEQ_EGW_ESTIMATEFINANCIALDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abstractEstimate")
    private AbstractEstimate abstractEstimate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund")
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function")
    private CFunction function;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionary")
    private Functionary functionary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subScheme")
    private SubScheme subScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budgetGroup")
    private BudgetGroup budgetGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coa")
    private CChartOfAccounts coa;

    private transient String apprYear;// values will be previous or running

    @Valid
    @OrderBy("id")
    @OneToMany(mappedBy = "financialDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = FinancingSource.class)
    private List<FinancingSource> financingSources = new ArrayList<FinancingSource>(0);

    public FinancialDetail() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate estimate) {
        abstractEstimate = estimate;
    }

    public BudgetGroup getBudgetGroup() {
        return budgetGroup;
    }

    public void setBudgetGroup(final BudgetGroup budgetGroup) {
        this.budgetGroup = budgetGroup;
    }

    @NotNull(message = "financial.fund.null")
    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public List<FinancingSource> getFinancingSources() {
        return financingSources;
    }

    public void setFinancingSources(final List<FinancingSource> financingSources) {
        this.financingSources = financingSources;
    }

    public void addFinancingSource(final FinancingSource financingSource) {
        financingSources.add(financingSource);
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        double total = 0;
        boolean finSourceError = false;

        if (fund == null)
            validationErrors.add(new ValidationError("fund_null", "financial.fund.null"));

        if (financingSources == null || financingSources.isEmpty())
            validationErrors.add(new ValidationError("financingsource_null", "financingsource.null"));

        final int errorCnt = validationErrors.size();

        if (financingSources != null)
            for (final FinancingSource financingSource : financingSources) {
                if (!finSourceError)
                    validationErrors.addAll(financingSource.validate());

                // if one financial source row has invalid values, same check
                // need not be done
                // for the remaining objects, and duplicate error messages can
                // be avoided
                if (!finSourceError && errorCnt < validationErrors.size())
                    finSourceError = true;

                total += financingSource.getPercentage();
            }

        if (financingSources != null && !financingSources.isEmpty() && total != 100)
            validationErrors.add(new ValidationError("percentageequalto100",
                    "financingsource.percentage.percentageequalto100"));

        return validationErrors;
    }

    /**
     * This method is invoked from the script to generate the budget appropriation number
     *
     * @return an instance of <code>FinancingSource</code> having the maximum of the financial sources selected
     */
    public FinancingSource getMaxFinancingSource() {
        double max = 0.0;
        FinancingSource maxFinSource = null;
        for (final FinancingSource finSource : financingSources)
            if (finSource.getPercentage() > max) {
                max = finSource.getPercentage();
                maxFinSource = finSource;
            }

        return maxFinSource;
    }

    public CChartOfAccounts getCoa() {
        return coa;
    }

    public void setCoa(final CChartOfAccounts coa) {
        this.coa = coa;
    }

    public String getApprYear() {
        return apprYear;
    }

    public void setApprYear(final String apprYear) {
        this.apprYear = apprYear;
    }
}
