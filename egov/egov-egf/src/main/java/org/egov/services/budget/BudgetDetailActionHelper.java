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
package org.egov.services.budget;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetProposalBean;
import org.egov.model.service.BudgetDefinitionService;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class BudgetDetailActionHelper {
    @Autowired
    private BudgetDefinitionService budgetDefinitionService;
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    private static Logger LOGGER = Logger.getLogger(BudgetDetailActionHelper.class);
    private static final String REFERENCEBUDGET = "no.reference.budget";

    @Transactional
    public void create(final BudgetDetailHelperBean parameterObject) {

        if (!parameterObject.addNewDetails)
            deleteExisting(parameterObject.budgetDetail.getBudget(), parameterObject.searchFunctionId,
                    parameterObject.budgetGroupId);
        final Position pos = positionMasterService.getPositionById(parameterObject.workflowBean.getApproverPositionId());
        saveBudgetDetails(true, parameterObject.budgetDetail.getBudget(), parameterObject.budgetDetailList,
                parameterObject.beAmounts, parameterObject.egwStatus, parameterObject.workflowBean,
                pos);
        if (parameterObject.budgetDetail.getBudget().getState() == null)
            parameterObject.budgetDetail.getBudget().transition().start()
                    .withSenderName(securityUtils.getCurrentUser().getName())
                    .withComments(parameterObject.workflowBean.getApproverComments()).withStateValue("Created")
                    .withDateInfo(new Date()).withOwner(pos);
        else
            parameterObject.budgetDetail.getBudget().transition()
                    .withSenderName(securityUtils.getCurrentUser().getName())
                    .withComments(parameterObject.workflowBean.getApproverComments()).withStateValue("Created")
                    .withDateInfo(new Date()).withOwner(pos);

        budgetDefinitionService.update(parameterObject.budgetDetail.getBudget());

    }

    public void saveBudgetDetails(final Boolean addNewDetails, final Budget budget,
            final List<BudgetDetail> budgetDetailList, final List<BigDecimal> beAmounts, final EgwStatus egwStatus,
            final WorkflowBean workflowBean, final Position owner) {

        int index = 0;
        Budget refBudget;
        refBudget = budgetService.getReferenceBudgetFor(budget);
        if (refBudget == null)
            throw new ValidationException(Arrays.asList(new ValidationError(REFERENCEBUDGET, REFERENCEBUDGET)));

        int i = 0;
        BudgetDetail beNextYear;
        for (final BudgetDetail detail : budgetDetailList) {
            if (detail != null)
                detail.setId(null);
            BudgetDetail reCurrentYear = budgetDetailService.setRelatedEntitesOn(detail);
            reCurrentYear.setUniqueNo(budgetDetailService.generateUniqueNo(reCurrentYear));
            budgetDetailService.applyAuditing(reCurrentYear);
            reCurrentYear = budgetDetailService.transitionWorkFlow(reCurrentYear, workflowBean);
            budgetDetailService.applyAuditing(reCurrentYear.getState());
            budgetDetailService.persist(reCurrentYear);

            beNextYear = new BudgetDetail();
            beNextYear.copyFrom(detail);
            beNextYear.setBudget(refBudget);
            beNextYear.setOriginalAmount(beAmounts.get(index));
            beNextYear.setDocumentNumber(detail.getDocumentNumber());
            beNextYear.setAnticipatoryAmount(reCurrentYear.getAnticipatoryAmount());
            beNextYear = budgetDetailService.setRelatedEntitesOn(beNextYear);
            beNextYear.setUniqueNo(budgetDetailService.generateUniqueNo(beNextYear));
            if (workflowBean.getWorkFlowAction().equalsIgnoreCase(FinancialConstants.BUTTONSAVE))
                beNextYear.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.BUDGETDETAIL,
                        FinancialConstants.WORKFLOW_STATE_NEW));
            else
                beNextYear.setStatus(egwStatus);
            budgetDetailService.applyAuditing(beNextYear);
            budgetDetailService.persist(beNextYear);

            index++;

            if (++i % 15 == 0) {
                persistenceService.getSession().flush();
                persistenceService.getSession().clear();
            }
            LOGGER.error("saved" + i + "Item");
        }
    }

    public String generateUniqueNo(final BudgetDetail detail) {
        return detail.getFund().getId() + "-" + detail.getExecutingDepartment().getId() + "-"
                + detail.getFunction().getId() + "-" + detail.getBudgetGroup().getId();

    }

    private void deleteExisting(final Budget budget, final Long searchfunctionid, final Long searchbudgetGroupid) {

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Initiating deletion ..........");
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(budget);
        final StringBuffer addlCondtion = new StringBuffer(150);

        addlCondtion.append("delete from egf_budgetdetail where budget=:budgetid ");
        addlCondtion.append("and status not in (SELECT id FROM egw_status   WHERE code ='NEW')");
        if (referenceBudgetFor != null)
            addlCondtion
                    .append(" or (budget=:referenceBudget and status not in (SELECT id FROM egw_status   WHERE code ='NEW'))");
        if (searchfunctionid != null && searchfunctionid != 0)
            addlCondtion.append("and function.id=:functionid");
        if (searchbudgetGroupid != null && searchbudgetGroupid != 0)
            addlCondtion.append("and budgetGroup.id=:budgetGroupid");
        new ArrayList<BudgetDetail>();
        final Query qry = persistenceService.getSession().createSQLQuery(addlCondtion.toString());
        qry.setLong("budgetid", budget.getId());
        if (referenceBudgetFor != null)
            qry.setLong("referenceBudget", referenceBudgetFor.getId());
        if (searchfunctionid != null && searchfunctionid != 0)
            qry.setLong("functionid", searchfunctionid);
        if (searchbudgetGroupid != null && searchbudgetGroupid != 0)
            qry.setLong("budgetGroupid", searchbudgetGroupid);
        qry.executeUpdate();
    }

    @Transactional
    public void update(final List<BudgetProposalBean> bpBeanList, final WorkflowBean workflowBean) {
        BudgetDetail bd;
        BudgetDetail be;
        for (final BudgetProposalBean bpBean : bpBeanList) {
            if (bpBean == null || bpBean.getId() == null)
                continue;
            bd = budgetDetailService.find("from BudgetDetail where id=?", bpBean.getId());
            bd.setOriginalAmount(bpBean.getProposedRE());
            be = budgetDetailService.find("from BudgetDetail where id=?", bpBean.getNextYrId());
            be.setOriginalAmount(bpBean.getProposedBE());
            if (bpBean.getDocumentNumber() != null)
                bd.setDocumentNumber(bpBean.getDocumentNumber());

            bd = budgetDetailService.transitionWorkFlow(bd, workflowBean);

            if (workflowBean.getWorkFlowAction().contains("Verify"))
                be.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.BUDGETDETAIL,
                        FinancialConstants.BUDGETDETAIL_VERIFIED_STATUS));
            else
                be.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.BUDGETDETAIL,
                        FinancialConstants.BUDGETDETAIL_CREATED_STATUS));
            budgetDetailService.applyAuditing(bd.getState());
            budgetDetailService.update(bd);
            budgetDetailService.update(be);
        }
    }

}
