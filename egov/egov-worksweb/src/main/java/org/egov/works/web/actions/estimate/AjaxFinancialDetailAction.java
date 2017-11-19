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
package org.egov.works.web.actions.estimate;

import org.apache.log4j.Logger;
import org.egov.commons.CFunction;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.budget.BudgetGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AjaxFinancialDetailAction extends BaseFormAction {

    private static final long serialVersionUID = 7300734573956975326L;
    private static final Logger logger = Logger.getLogger(AjaxFinancialDetailAction.class);
    private BudgetGroupDAO budgetGroupDAO;
    private static final String SUBSCHEMES = "subschemes";
    private static final String SCHEMES = "schemes";
    private static final String BUDGETGROUPS = "budgetgroups";
    private List<SubScheme> subSchemes;
    private List<Scheme> schemes;
    private List<BudgetGroup> budgetGroups;
    private Integer schemeId;
    private Integer fundId;
    private Long functionId;
    private Date estimateDate;
    private String loadBudgetGroupsValidationError = "";

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public AjaxFinancialDetailAction() {

    }

    public String loadSchemes() {
        schemes = getPersistenceService()
                .findAllBy(
                        "from org.egov.commons.Scheme sc where sc.isactive=true and sc.fund.id=? and ? between validfrom and validto",
                        fundId, estimateDate);
        return SCHEMES;
    }

    public String loadSubSchemes() {
        subSchemes = getPersistenceService().findAllBy(
                "from org.egov.commons.SubScheme where scheme.id=? and ? between validfrom and validto", schemeId,
                estimateDate);
        return SUBSCHEMES;
    }

    public String loadBudgetGroups() {
        try {
            if (functionId == -1)
                budgetGroups = budgetGroupDAO.getBudgetGroupList();
            else {
                final CFunction function = (CFunction) getPersistenceService().find(
                        "from org.egov.commons.CFunction where id = ? ", functionId);
                if (function == null)
                    throw new ValidationException(Arrays.asList(new ValidationError("nobudgetforfunction",
                            "Budget head information not available for the chosen function")));
                else
                    budgetGroups = budgetGroupDAO.getBudgetHeadByFunction(function.getCode());
            }
        } catch (final ValidationException egovEx) {
            logger.error("Unable to load budget head information>>>" + egovEx.getMessage());
            budgetGroups = new ArrayList<BudgetGroup>();
            addActionError("Unable to load budget head information");
            return BUDGETGROUPS;

        } catch (final Exception e) {
            logger.error("Budgetunavailable : Unable to load budget head information>>>" + e.getMessage());
            addFieldError("budgetunavailable", "Unable to load budget head information");
        }
        return BUDGETGROUPS;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final Integer schemeId) {
        this.schemeId = schemeId;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(final Integer fundId) {
        this.fundId = fundId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Long functionId) {
        this.functionId = functionId;
    }

    public List<SubScheme> getSubSchemes() {
        return subSchemes;
    }

    public void setSubSchemes(final List<SubScheme> subSchemes) {
        this.subSchemes = subSchemes;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(final List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public List<BudgetGroup> getBudgetGroups() {
        return budgetGroups;
    }

    public void setBudgetGroups(final List<BudgetGroup> budgetGroups) {
        this.budgetGroups = budgetGroups;
    }

    public void setBudgetGroupDAO(final BudgetGroupDAO budgetGroupDAO) {
        this.budgetGroupDAO = budgetGroupDAO;
    }

    public BudgetGroupDAO getBudgetGroupDAO() {
        return budgetGroupDAO;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public String getLoadBudgetGroupsValidationError() {
        return loadBudgetGroupsValidationError;
    }

    public void setLoadBudgetGroupsValidationError(final String loadBudgetGroupsValidationError) {
        this.loadBudgetGroupsValidationError = loadBudgetGroupsValidationError;
    }
}
