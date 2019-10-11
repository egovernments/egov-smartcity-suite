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
package org.egov.services.bills;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillregister;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.services.voucher.JournalVoucherActionHelper;
import org.egov.utils.CancelBillAndVoucher;
import org.egov.utils.CheckListHelper;
import org.egov.utils.FinancialConstants;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.domain.BillRegisterBean;

@Service
@Transactional(readOnly = true)
public class EgBillRegisterService extends PersistenceService<EgBillregister, Long> {
    private static final String CANCEL_QUERY_STR = " billstatus=:billStatus, statusid=:statusId ";
    private static final String STATUS_QUERY_STR = "moduletype=:moduleType and description=:description";
    private static final String BILL_STATUS = "billStatus";
    private static final String DESCRIPTION = "description";
    private static final String MODULE_TYPE = "moduleType";
    private static final Logger LOGGER = Logger.getLogger(JournalVoucherActionHelper.class);
    private static final String FAILED = "Transaction failed";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<EgBillregister> billRegisterWorkflowService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private BillsService billsService;
    @Autowired
    private CancelBillAndVoucher cancelBillAndVoucher;
   

    public EgBillRegisterService() {
        super(EgBillregister.class);
    }

    public EgBillRegisterService(Class<EgBillregister> type) {
        super(type);
    }

    @Transactional
    public EgBillregister createBill(EgBillregister bill, WorkflowBean workflowBean, List<CheckListHelper> checkListsTable) {
        try {
            applyAuditing(bill);
            if (FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()) && bill.getState() == null) {
                bill.setBillstatus("APPROVED");
                EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS);
                bill.setStatus(egwStatus);
            } else {
                bill = transitionWorkFlow(bill, workflowBean);
                applyAuditing(bill.getState());
            }
            persist(bill);
            bill.getEgBillregistermis().setSourcePath(
                    "/EGF/bill/contingentBill-beforeView.action?billRegisterId=" + bill.getId().toString());
            createCheckList(bill, checkListsTable);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return bill;
    }

    @Transactional
    public void createCheckList(final EgBillregister bill, List<CheckListHelper> checkListsTable) {
        try {
            if (checkListsTable != null)
                for (final CheckListHelper clh : checkListsTable) {
                    final EgChecklists checkList = new EgChecklists();
                    final AppConfigValues configValue = (AppConfigValues) persistenceService.find(
                            "from AppConfigValues where id = ?1",
                            clh.getId());
                    checkList.setObjectid(bill.getId());
                    checkList.setAppconfigvalue(configValue);
                    checkList.setChecklistvalue(clh.getVal());
                    persistenceService.getSession().saveOrUpdate(checkList);
                }
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
    }

    @Transactional
    public EgBillregister sendForApproval(EgBillregister bill, WorkflowBean workflowBean) {
        try {
            bill = transitionWorkFlow(bill, workflowBean);
            applyAuditing(bill.getState());
            persist(bill);

        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return bill;
    }

    @Transactional
    public EgBillregister transitionWorkFlow(final EgBillregister billregister, WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        Assignment userAssignment=null;
        final List<Assignment> assignments = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
        if(assignments.isEmpty()) {
        	throw new ApplicationRuntimeException("User assignment got expired");
        }else {
        	userAssignment = assignments.get(0); 
        }
        Position pos = null;
        Assignment wfInitiator = null;
        if (null != billregister.getId())
            wfInitiator = getWorkflowInitiator(billregister);

        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                billregister.transition().end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
                billregister.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(FinancialConstants.WF_STATE_EOA_Approval_Pending);
            }

        } else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {

            final WorkFlowMatrix wfmatrix = billRegisterWorkflowService.getWfMatrix(billregister.getStateType(), null,
                    null, null, billregister.getCurrentState().getValue(), null);
            billregister.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withStateValue(wfmatrix.getCurrentDesignation() + " Approved").withDateInfo(currentDate.toDate())
                    .withOwner(pos)
                    .withNextAction(wfmatrix.getNextAction());

            EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                    FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS);
            billregister.setStatus(egwStatus);
            billregister.transition().end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                    FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS);
            billregister.setStatus(egwStatus);
            billregister.setBillstatus(FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS);
            billregister.transition().end().withStateValue(FinancialConstants.WORKFLOW_STATE_CANCELLED)
                    .withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = (Position) persistenceService.find("from Position where id = ?1", workflowBean.getApproverPositionId());
            if (null == billregister.getState()) {
                final WorkFlowMatrix wfmatrix = billRegisterWorkflowService.getWfMatrix(billregister.getStateType(), null,
                        null, null, workflowBean.getCurrentState(), null);
                billregister.transition().start().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (billregister.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                billregister.transition().end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = billRegisterWorkflowService.getWfMatrix(billregister.getStateType(), null,
                        null, null, billregister.getCurrentState().getValue(), null);
                billregister.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }
        }
        return billregister;
    }
    
    @SuppressWarnings("deprecation")
    @Transactional
    public Map<String, Object> cancelBills(final List<BillRegisterBean> billListDisplay, final String expType) {
        final Map<String, Object> map = new HashMap<>();
        EgBillregister billRegister;
        final Long[] idList = new Long[billListDisplay.size()];
        int i = 0;
        int idListLength = 0;
        final List<Long> ids = new ArrayList<>();
        final List<String> billNumbers = new ArrayList<>();
        final Map<String, Object> statusQueryMap = new HashMap<>();
        final Map<String, Object> cancelQueryMap = new HashMap<>();
        final StringBuilder statusQuery = new StringBuilder(
                "from EgwStatus where ");
        final StringBuilder cancelQuery = new StringBuilder(
                "Update eg_billregister set ");
        for (final BillRegisterBean billRgistrBean : billListDisplay)
            if (billRgistrBean.getIsSelected()) {
                idList[i++] = Long.parseLong(billRgistrBean.getId());
                idListLength++;
            }
        if (expType == null || expType.equalsIgnoreCase("") || FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(expType)) {
            statusQuery.append(STATUS_QUERY_STR);
            statusQueryMap.put(MODULE_TYPE, FinancialConstants.CONTINGENCYBILL_FIN);
            statusQueryMap.put(DESCRIPTION, FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS);
            cancelQuery.append(CANCEL_QUERY_STR);
            cancelQueryMap.put(BILL_STATUS, FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS);
        } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY
                .equalsIgnoreCase(expType)) {
            statusQuery.append(STATUS_QUERY_STR);
            statusQueryMap.put(MODULE_TYPE, FinancialConstants.SALARYBILL);
            statusQueryMap.put(DESCRIPTION, FinancialConstants.SALARYBILL_CANCELLED_STATUS);
            cancelQuery.append(CANCEL_QUERY_STR);
            cancelQueryMap.put(BILL_STATUS, FinancialConstants.SALARYBILL_CANCELLED_STATUS);
        } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(expType)) {
            statusQuery.append(STATUS_QUERY_STR);
            statusQueryMap.put(MODULE_TYPE, FinancialConstants.SUPPLIERBILL);
            statusQueryMap.put(DESCRIPTION, FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS);
            cancelQuery.append(CANCEL_QUERY_STR);
            cancelQueryMap.put(BILL_STATUS, FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS);
        } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(expType)) {
            statusQuery.append(STATUS_QUERY_STR);
            statusQueryMap.put(MODULE_TYPE, FinancialConstants.CONTRACTORBILL);
            statusQueryMap.put(DESCRIPTION, FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS);
            cancelQuery.append(CANCEL_QUERY_STR);
            cancelQueryMap.put(BILL_STATUS, FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Status Query - " + statusQuery.toString());
        final Query query = persistenceService.getSession().createQuery(statusQuery.toString());
        statusQueryMap.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        final EgwStatus status = (EgwStatus) query.uniqueResult();
        if (idListLength != 0) {
            for (i = 0; i < idListLength; i++) {
                billRegister = billsService.getBillRegisterById(idList[i].intValue());
                final boolean value = cancelBillAndVoucher.canCancelBill(billRegister);
                if (!value) {
                    billNumbers.add(billRegister.getBillnumber());
                    continue;
                }
                ids.add(idList[i]);
            }
            cancelQuery.append(" where id in (:ids)");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" Cancel Query - " + cancelQuery.toString());
            final NativeQuery totalNativeQuery = getSession()
                    .createNativeQuery(cancelQuery.toString());
            totalNativeQuery.setParameter("statusId", Long.valueOf(status.getId()), LongType.INSTANCE);
            cancelQueryMap.entrySet().forEach(entry -> totalNativeQuery.setParameter(entry.getKey(), entry.getValue()));
            totalNativeQuery.setParameterList("ids", ids);
            if (!ids.isEmpty())
                totalNativeQuery.executeUpdate();
        }
        map.put("ids", ids);
        map.put("billNumbers", billNumbers);
        return map;
    }

    private Assignment getWorkflowInitiator(final EgBillregister billregister) {
        return assignmentService.findByEmployeeAndGivenDate(billregister.getCreatedBy().getId(), new Date())
                .get(0);
    }
}