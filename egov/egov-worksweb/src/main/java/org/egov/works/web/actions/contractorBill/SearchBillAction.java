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
package org.egov.works.web.actions.contractorBill;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infra.persistence.utils.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.ContractorBillServiceImpl;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Result(name = SearchBillAction.SEARCH, location = "searchBill-search.jsp")
public class SearchBillAction extends BaseFormAction {

    private static final long serialVersionUID = 1691106236053172675L;
    private String status;
    private String contractor;
    private Date fromDate;
    private Date toDate;
    private String workordercode;
    private String billno;
    private WorkOrderService workOrderService;
    private String workorderid;
    private Long contractorId;
    @Autowired
    private EmployeeServiceOld employeeService;
    private ContractorBillService contractorBillService;
    private WorksService worksService;
    public static final String SEARCH = "search";
    private static final String BILL_MODULE_KEY = "CONTRACTORBILL";
    private static final String NEW_STATUS = "NEW";
    private static final String DATE_FORMAT_DD_MMM_YYYY = "dd-MMM-yyyy";

    private ContractorBillRegister contractorBillRegister = new ContractorBillRegister();
    private MeasurementBookServiceImpl measurementBookService;
    private PersistenceService<MBForCancelledBill, Long> cancelBillService;
    private Long contractorBillId;
    private String messageKey;
    private String sourcePage;
    private String billNumber;
    private String cancellationReason;
    private String cancelRemarks;
    private Integer execDeptid;
    private Integer billDeptId;
    private Integer page = 1;
    private Integer pageSize = 30;
    private EgovPaginatedList pagedResults;
    List<ContractorBillRegister> contractorBillList = null;
    private String estimateNo;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    // @Override
    @Override
    public Object getModel() {

        return null;
    }

    public List<EgwStatus> getBillStatuses() {
        final List<EgwStatus> statusList = egwStatusHibernateDAO.getStatusByModule(BILL_MODULE_KEY);
        final List<EgwStatus> latestStatusList = new ArrayList<EgwStatus>();
        if (!statusList.isEmpty())
            for (final EgwStatus egwStatus : statusList)
                if (!egwStatus.getCode().equals(NEW_STATUS))
                    latestStatusList.add(egwStatus);
        return latestStatusList;
    }

    @Action(value = "/contractorBill/searchBill-search")
    public String search() {
        return SEARCH;
    }

    public String edit() {
        return searchBill();
    }

    @Override
    public String execute() {
        return searchBill();
    }

    public String searchBill() {
        final Map<String, Object> criteriaMap = new HashMap<String, Object>();
        final List<Object> paramList = new ArrayList<Object>();
        List<String> qryObj = new ArrayList<String>();
        Page resPage;
        Long count;
        Object[] params;
        if (StringUtils.isNotBlank(getWorkordercode()))
            criteriaMap.put(ContractorBillServiceImpl.WORKORDER_NO, getWorkordercode());
        if (getContractorId() != -1)
            criteriaMap.put(ContractorBillServiceImpl.CONTRACTOR_ID, getContractorId());
        if (StringUtils.isNotBlank(getBillno()))
            criteriaMap.put(ContractorBillServiceImpl.BILLNO, getBillno());
        if (fromDate != null && toDate != null && !DateUtils.compareDates(getToDate(), getFromDate()))
            addFieldError("fromDate", getText("greaterthan.endDate.fromDate"));
        if (toDate != null && !DateUtils.compareDates(new Date(), getToDate()))
            addFieldError("toDate", getText("greaterthan.endDate.currentdate"));

        if (fromDate != null && toDate == null)
            criteriaMap.put(ContractorBillServiceImpl.FROM_DATE,
                    new Date(DateUtils.getFormattedDate(getFromDate(), DATE_FORMAT_DD_MMM_YYYY)));
        else if (toDate != null && fromDate == null)
            criteriaMap.put(ContractorBillServiceImpl.TO_DATE,
                    new Date(DateUtils.getFormattedDate(getToDate(), DATE_FORMAT_DD_MMM_YYYY)));
        else if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            criteriaMap.put(ContractorBillServiceImpl.FROM_DATE,
                    new Date(DateUtils.getFormattedDate(getFromDate(), DATE_FORMAT_DD_MMM_YYYY)));
            criteriaMap.put(ContractorBillServiceImpl.TO_DATE,
                    new Date(DateUtils.getFormattedDate(getToDate(), DATE_FORMAT_DD_MMM_YYYY)));
        }

        if (execDeptid != null && execDeptid != -1)
            criteriaMap.put(ContractorBillServiceImpl.EXEC_DEPT_ID, execDeptid);

        if (billDeptId != null && billDeptId != -1)
            criteriaMap.put(ContractorBillServiceImpl.BILL_DEPT_ID, billDeptId);

        if (estimateNo != null && !"".equalsIgnoreCase(estimateNo))
            criteriaMap.put(ContractorBillServiceImpl.EST_NO, estimateNo);

        if (!getStatus().equals("-1"))
            criteriaMap.put(ContractorBillServiceImpl.BILLSTATUS, getStatus());

        qryObj = contractorBillService.searchContractorBill(criteriaMap, paramList);
        final String qry = qryObj.get(0);
        if (paramList.isEmpty()) {
            params = null;
            count = (Long) persistenceService.find(qryObj.get(0));
            final Query qry1 = persistenceService.getSession().createQuery(qry);
            resPage = new Page(qry1, page, pageSize);
        } else {
            params = new Object[paramList.size()];
            params = paramList.toArray(params);
            count = (Long) persistenceService.find(qryObj.get(1), params);
            resPage = persistenceService.findPageBy(qry, page, pageSize, params);
        }
        pagedResults = new EgovPaginatedList(resPage, count.intValue());
        contractorBillList = pagedResults != null ? pagedResults.getList() : null;

        if (!contractorBillList.isEmpty())
            contractorBillList = getPositionAndUser(contractorBillList);

        pagedResults.setList(contractorBillList);
        return SEARCH;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("ContractorList", workOrderService.getAllContractorForWorkOrder());
        final List departmentList = getPersistenceService().findAllBy("from DepartmentImpl");
        addDropdownData("billDepartmentList", departmentList);
        addDropdownData("executingDepartmentList", departmentList);
        if ("cancelBill".equals(sourcePage))
            setStatus(ContractorBillRegister.BillStatus.APPROVED.toString());
    }

    @ValidationErrorPageExt(action = "search", makeCall = true, toMethod = "searchBill")
    public String cancelApprovedBill() throws Exception {
        contractorBillRegister = contractorBillService.findById(contractorBillId, false);
        final List<MBHeader> mbHeaderListForBillId = measurementBookService.findAllByNamedQuery("getAllMBsForBillId",
                MBHeader.MeasurementBookStatus.APPROVED.toString(), contractorBillRegister.getId());
        for (final MBHeader mbObj : mbHeaderListForBillId) {
            final MBForCancelledBill mbCB = new MBForCancelledBill();
            mbCB.setEgBillregister(mbObj.getEgBillregister());
            mbCB.setMbHeader(mbObj);
            cancelBillService.persist(mbCB);
        }

        contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(BILL_MODULE_KEY,
                ContractorBillRegister.BillStatus.CANCELLED.toString()));
        contractorBillRegister.setBillstatus(ContractorBillRegister.BillStatus.CANCELLED.toString());

        final PersonalInformation prsnlInfo = employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());
        String empName = "";
        if (prsnlInfo.getEmployeeFirstName() != null)
            empName = prsnlInfo.getEmployeeFirstName();
        if (prsnlInfo.getEmployeeLastName() != null)
            empName = empName.concat(" ").concat(prsnlInfo.getEmployeeLastName());
        if (cancelRemarks != null && StringUtils.isNotBlank(cancelRemarks)) {
        } else {
        }

        // TODO - The setter methods of variables in State.java are protected.
        // Need to alternative way to solve this issue.
        // Set the status and workflow state to cancelled
        /*
         * State oldEndState = contractorBillRegister.getCurrentState(); Position owner = prsnlInfo.getAssignment(new
         * Date()).getPosition(); oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
         * oldEndState.setLastModifiedBy(prsnlInfo.getUserMaster()); oldEndState.setCreatedDate(new DateTime());
         * oldEndState.setLastModifiedDate(new DateTime()); oldEndState.setOwner(owner);
         * oldEndState.setValue(WorksConstants.CANCELLED_STATUS); oldEndState.setText1(cancellationText);
         * contractorBillRegister.changeState("END", owner, null);
         */

        billNumber = contractorBillRegister.getBillnumber();
        messageKey = "contractorBill." + contractorBillRegister.getBillstatus();
        return SUCCESS;
    }

    public String getContractor() {
        return contractor;
    }

    public String getWorkordercode() {
        return workordercode;
    }

    public String getBillno() {
        return billno;
    }

    public void setContractor(final String contractor) {
        this.contractor = contractor;
    }

    public void setWorkordercode(final String workordercode) {
        this.workordercode = workordercode;
    }

    public void setBillno(final String billno) {
        this.billno = billno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public String getWorkorderid() {
        return workorderid;
    }

    public void setWorkorderid(final String workorderid) {
        this.workorderid = workorderid;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    protected List<ContractorBillRegister> getPositionAndUser(final List<ContractorBillRegister> results) {
        final List<ContractorBillRegister> billList = new ArrayList<ContractorBillRegister>();
        for (final ContractorBillRegister br : results) {
            PersonalInformation emp = null;
            if (!br.getStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                    && !br.getStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)) {
                if (br.getCurrentState() != null)
                    emp = employeeService.getEmployeeforPosition(br.getCurrentState().getOwnerPosition());
                if (emp != null && StringUtils.isNotBlank(emp.getEmployeeName()))
                    br.setOwner(emp.getEmployeeName());
            }

            billList.add(br);
            final String actions = worksService.getWorksConfigValue("BILL_SHOW_ACTIONS");
            if (StringUtils.isNotBlank(actions)) {
                final List<String> showBillActionsList = Arrays.asList(actions.split(","));
                for (final String action : showBillActionsList)
                    if (action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCOMPLETIONCERTIFICATE)
                            && br.getBilltype().equals(
                                    worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL)))
                        br.getBillActions().add(action);
                    else if (!action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCOMPLETIONCERTIFICATE))
                        br.getBillActions().add(action);

            }
            // To get workorder ID by passing work order number.
            final WorkOrder workOrderObj = (WorkOrder) getPersistenceService().find(
                    "from WorkOrder where workOrderNumber = ?", br.getWorkordernumber());
            br.setWorkOrder(workOrderObj);
        }
        return billList;
    }

    public Map<String, Object> getContractorForApprovedWorkOrder() {
        final Map<String, Object> contractorsWithWOList = new HashMap<String, Object>();
        if (workOrderService.getContractorsWithWO() != null)
            for (final Contractor contractor : workOrderService.getContractorsWithWO())
                contractorsWithWOList.put(contractor.getId() + "", contractor.getCode() + " - " + contractor.getName());
        return contractorsWithWOList;
    }

    public String getFinalBillTypeConfigValue() {
        return worksService.getWorksConfigValue("FinalBillType");
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public void setMeasurementBookService(final MeasurementBookServiceImpl measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public void setCancelBillService(final PersistenceService<MBForCancelledBill, Long> cancelBillService) {
        this.cancelBillService = cancelBillService;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setCancelRemarks(final String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public ContractorBillRegister getContractorBillRegister() {
        return contractorBillRegister;
    }

    public void setContractorBillRegister(final ContractorBillRegister contractorBillRegister) {
        this.contractorBillRegister = contractorBillRegister;
    }

    public Long getContractorBillId() {
        return contractorBillId;
    }

    public void setContractorBillId(final Long contractorBillId) {
        this.contractorBillId = contractorBillId;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(final String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public Integer getExecDeptid() {
        return execDeptid;
    }

    public void setExecDeptid(final Integer execDeptid) {
        this.execDeptid = execDeptid;
    }

    public List<ContractorBillRegister> getContractorBillList() {
        return contractorBillList;
    }

    public EgovPaginatedList getPagedResults() {
        return pagedResults;
    }

    public void setPagedResults(final EgovPaginatedList pagedResults) {
        this.pagedResults = pagedResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getBillDeptId() {
        return billDeptId;
    }

    public void setBillDeptId(final Integer billDeptId) {
        this.billDeptId = billDeptId;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(final String estimateNo) {
        this.estimateNo = estimateNo;
    }

}
