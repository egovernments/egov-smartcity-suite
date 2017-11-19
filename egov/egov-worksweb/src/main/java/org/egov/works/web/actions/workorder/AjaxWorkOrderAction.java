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
package org.egov.works.web.actions.workorder;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AjaxWorkOrderAction extends BaseFormAction {

    private static final long serialVersionUID = 6775725729956509634L;
    private String departmentName;
    private Long desgId;
    private static final String WORKORDER_DESIG_LIST = "workOrderDesignations";
    private static final String WORKORDER_USER_LIST = "workOrderUsers";
    private static final String WORKORDER_ASSIGNED_LIST = "workOrderAssignedUsers";
    private List<Designation> workOrderDesigList = new ArrayList<Designation>();
    private String traIds;
    private List<TenderResponseActivity> tenderResponseActivitylist = new ArrayList<TenderResponseActivity>();
    private PersistenceService<TenderResponseActivity, Long> tenderResponseActivityService;
    private Long executingDepartment;
    private PersonalInformationService personalInformationService;
    private List userList;
    private MeasurementBookService measurementBookService;
    List<MBHeader> approvedMBList = new ArrayList<MBHeader>();;
    private Long workOrderId;
    private String query = "";
    private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
    private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
    private String trackMlsCheck;
    private String yearEndApprCheck;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    private String estimateNo;
    private static final String VALID = "valid";
    private static final String INVALID = "invalid";
    private String advanceRequisitionNo;
    private String owner = "";
    private String arfInWorkFlowCheck;
    private ContractorAdvanceService contractorAdvanceService;
    @Autowired
    private EmployeeServiceOld employeeService;
    private static final String ARF_IN_WORKFLOW_CHECK = "arfInWorkflowCheck";
    @Autowired
    private ScriptService scriptService;

    @Override
    public Object getModel() {
        return null;
    }

    public String getDesignationByDeptId() {
        // List<Script> scriptList =
        // persistenceService.findAllByNamedQuery(Script.BY_NAME,"workOrder.Designation.ByDepartment");
        if (StringUtils.isNotBlank(departmentName)) {
            // List<String> list = (List<String>)
            // scriptList.get(0).eval(Script.createContext("department",departmentName));
            final ScriptContext scriptContext = ScriptService.createContext("department", departmentName);
            final List<String> desglist = (List<String>) scriptService.executeScript(
                    "workOrder.Designation.ByDepartment", scriptContext);
            workOrderDesigList.addAll(getPersistenceService().findAllByNamedQuery("getDesignationForListOfDesgNames",
                    desglist));
        }
        return WORKORDER_DESIG_LIST;
    }

    public String getUsersForDesg() {
        try {
            final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
            criteriaParams.put("designationId", desgId.intValue());
            criteriaParams.put("departmentId", executingDepartment);
            if (executingDepartment == null || executingDepartment == -1)
                userList = Collections.EMPTY_LIST;
            else
                userList = personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return WORKORDER_USER_LIST;
    }

    public String getWOAssignedTo1ForDepartment() {
        try {
            if (executingDepartment == null || executingDepartment == -1)
                userList = Collections.EMPTY_LIST;
            else
                userList = persistenceService
                        .findAllBy(
                                "select distinct woe.workOrder.engineerIncharge from  WorkOrderEstimate woe where woe.estimate.executingDepartment.id=?",
                                executingDepartment);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return WORKORDER_ASSIGNED_LIST;
    }

    public String getWOAssignedTo2ForDepartment() {
        try {
            if (executingDepartment == null || executingDepartment == -1)
                userList = Collections.EMPTY_LIST;
            else
                userList = persistenceService
                        .findAllBy(
                                "select distinct woe.workOrder.engineerIncharge2 from  WorkOrderEstimate woe where woe.estimate.executingDepartment.id=?",
                                executingDepartment);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return WORKORDER_ASSIGNED_LIST;
    }

    public String getTenderResponseActivityList() {
        if (StringUtils.isNotBlank(traIds)) {
            final Set<Long> traIdentifierSet = new HashSet<Long>();
            final Map<Long, Double> traMap = new HashMap<Long, Double>();
            final String[] values = traIds.split("\\^");// To split the data
                                                        // (For
            // Eg:1^2)
            final Long[] traIdLong = new Long[values.length];
            int j = 0;
            for (final String value : values)
                if (StringUtils.isNotBlank(value)) {
                    traIdLong[j] = Long.valueOf(value.split("~")[0].trim());// selected
                                                                            // TenderResponseActivity
                                                                            // Id
                    traMap.put(traIdLong[j], Double.valueOf(value.split("~")[1]));// selected
                                                                                  // TenderResponseActivity
                                                                                  // 's
                                                                                  // UnAssigned
                                                                                  // Qty
                    j++;
                }
            traIdentifierSet.addAll(Arrays.asList(traIdLong));
            List<TenderResponseActivity> tempList = new ArrayList<TenderResponseActivity>();
            tempList = tenderResponseActivityService.findAllByNamedQuery("getTenderResponseActivityByIds",
                    traIdentifierSet);
            for (final TenderResponseActivity tenderResponseActivity : tempList) {
                final Double unAssignedQuantity = traMap.get(tenderResponseActivity.getId());
                tenderResponseActivity.setNegotiatedQuantity(unAssignedQuantity);// Temperorily
                // setting
                // UnAssigned
                // Qty
                // instead
                // of
                // Negotiated
                // Qty
                getPersistenceService().getSession().evict(tenderResponseActivity);// To
                // evict
                // the
                // changes
                // done
                // on
                // tenderResponseActivity
                // not
                // to
                // be
                // committed.
                tenderResponseActivitylist.add(tenderResponseActivity);
            }
        }
        return "tenderResponseActivities";
    }

    public String getApprovedMBsForWorkOrder() {
        approvedMBList = measurementBookService.findAllBy(
                " from MBHeader where workOrder.id=? and egwStatus.code<>'CANCELLED'", workOrderId);
        return "approvedMBs";
    }

    public String advanceRequisitionInWorkflowCheck() {
        arfInWorkFlowCheck = VALID;
        final List<WorkOrderEstimate> woeList = persistenceService.findAllBy(
                " from WorkOrderEstimate woe where woe.workOrder.id = ? ", workOrderId);
        if (woeList.size() == 1) {
            final ContractorAdvanceRequisition arf = contractorAdvanceService.getContractorARFInWorkflowByWOEId(woeList
                    .get(0).getId());
            if (arf != null) {
                arfInWorkFlowCheck = INVALID;
                advanceRequisitionNo = arf.getAdvanceRequisitionNumber();
                estimateNo = arf.getWorkOrderEstimate().getEstimate().getEstimateNumber();
                final PersonalInformation emp = employeeService.getEmployeeforPosition(arf.getCurrentState()
                        .getOwnerPosition());
                if (emp != null)
                    owner = emp.getUserMaster().getName();
            }
        }
        return ARF_IN_WORKFLOW_CHECK;
    }

    public String trackMilestoneForBillCreationCheck() {
        final List<TrackMilestone> tm = persistenceService
                .findAllBy(
                        " select trmls from WorkOrderEstimate as woe left join woe.milestone mls left join mls.trackMilestone trmls where trmls.egwStatus.code='APPROVED' and woe.workOrder.id = ? and trmls.total>0 ",
                        workOrderId);
        trackMlsCheck = "invalid";
        if (tm != null && !tm.isEmpty() && tm.get(0) != null)
            trackMlsCheck = "valid";
        return "trackMlsForBillCreationCheck";
    }

    public String yearEndApprForBillCreationCheck() {
        final List<WorkOrderEstimate> woeList = persistenceService.findAllBy(
                " from WorkOrderEstimate woe where woe.workOrder.id = ? ", workOrderId);
        yearEndApprCheck = VALID;
        Long currentFinYearId = 0l;
        if (woeList.size() == 1) {
            estimateNo = woeList.get(0).getEstimate().getEstimateNumber();
            CFinancialYear currFinancialYear;
            try {
                currFinancialYear = finHibernateDao.getFinancialYearByDate(new Date());
            } catch (final Exception e) {
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "yrEnd.appr.verification.for.bill.financialyear.invalid",
                        "yrEnd.appr.verification.for.bill.financialyear.invalid")));
            }

            if (currFinancialYear != null)
                currentFinYearId = currFinancialYear.getId();

            if (woeList.get(0).getEstimate().getDepositCode() == null) {
                final AbstractEstimateAppropriation aeaObj = (AbstractEstimateAppropriation) persistenceService
                        .findByNamedQuery("getLatestBudgetUsageForEstimate", woeList.get(0).getEstimate().getId());
                if (aeaObj != null && aeaObj.getBudgetUsage().getConsumedAmount() > 0) {
                    if (aeaObj.getBudgetUsage().getFinancialYearId().intValue() != currentFinYearId.intValue())
                        yearEndApprCheck = INVALID;
                } else
                    yearEndApprCheck = INVALID;
            }
        }
        return "yearEndApprForBillCreationCheck";
    }

    public String searchEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent is null and UPPER(woe.estimate.estimateNumber) like '%'||?||'%' "
                    + " and woe.workOrder.egwStatus.code = ? )";
            params.add(query.toUpperCase());
            params.add("APPROVED");
            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateNoSearchResults";
    }

    public String searchWorkOrderNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = " from WorkOrder wo where wo.parent is null and UPPER(wo.workOrderNumber) like '%'||?||'%' "
                    + "and wo.egwStatus.code = ? ";
            params.add(query.toUpperCase());
            params.add("APPROVED");
            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public List<Designation> getWorkOrderDesigList() {
        return workOrderDesigList;
    }

    public void setWorkOrderDesigList(final List<Designation> workOrderDesigList) {
        this.workOrderDesigList = workOrderDesigList;
    }

    public void setDesgId(final Long desgId) {
        this.desgId = desgId;
    }

    public String getTraIds() {
        return traIds;
    }

    public void setTraIds(final String traIds) {
        this.traIds = traIds;
    }

    public List<TenderResponseActivity> getTenderResponseActivitylist() {
        return tenderResponseActivitylist;
    }

    public void setTenderResponseActivitylist(final List<TenderResponseActivity> tenderResponseActivitylist) {
        this.tenderResponseActivitylist = tenderResponseActivitylist;
    }

    public void setTenderResponseActivityService(
            final PersistenceService<TenderResponseActivity, Long> tenderResponseActivityService) {
        this.tenderResponseActivityService = tenderResponseActivityService;
    }

    public Long getExecutingDepartment() {
        return executingDepartment;
    }

    public void setExecutingDepartment(final Long executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(final List userList) {
        this.userList = userList;
    }

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public List<MBHeader> getApprovedMBList() {
        return approvedMBList;
    }

    public void setApprovedMBList(final List<MBHeader> approvedMBList) {
        this.approvedMBList = approvedMBList;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(final Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(final List<AbstractEstimate> estimateList) {
        this.estimateList = estimateList;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public String getTrackMlsCheck() {
        return trackMlsCheck;
    }

    public String getYearEndApprCheck() {
        return yearEndApprCheck;
    }

    public void setYearEndApprCheck(final String yearEndApprCheck) {
        this.yearEndApprCheck = yearEndApprCheck;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(final String estimateNo) {
        this.estimateNo = estimateNo;
    }

    public String getAdvanceRequisitionNo() {
        return advanceRequisitionNo;
    }

    public void setAdvanceRequisitionNo(final String advanceRequisitionNo) {
        this.advanceRequisitionNo = advanceRequisitionNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getArfInWorkFlowCheck() {
        return arfInWorkFlowCheck;
    }

    public void setArfInWorkFlowCheck(final String arfInWorkFlowCheck) {
        this.arfInWorkFlowCheck = arfInWorkFlowCheck;
    }

    public void setContractorAdvanceService(final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

}
