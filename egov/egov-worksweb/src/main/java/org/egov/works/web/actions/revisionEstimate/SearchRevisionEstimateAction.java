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
package org.egov.works.web.actions.revisionEstimate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionWorkOrder;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.RevisionEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@ParentPackage("egov")
@Result(name = SearchRevisionEstimateAction.SEARCH, location = "searchRevisionEstimate-search.jsp")
public class SearchRevisionEstimateAction extends SearchFormAction {

    private static final long serialVersionUID = -4526273645489797831L;
    private static final Logger LOGGER = Logger.getLogger(SearchRevisionEstimateAction.class);
    private AbstractEstimate estimates = new AbstractEstimate();
    private RevisionEstimateService revisionEstimateService;
    private Date fromDate;
    private Date toDate;
    @Autowired
    private EmployeeServiceOld employeeServiceOld;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    public static final String SEARCH = "search";
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private List<RevisionAbstractEstimate> revEstimateList;
    private WorksService worksService;
    private WorkOrderEstimate workOrderEstimate;
    private String estimateNumber;
    private String workOrderNumber;
    private Integer execDept = -1;
    private Integer reStatus;
    private Long revWOId;
    private String source;
    private PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService;
    private PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService;
    private String messageKey;
    private String revisionEstimateNumber;
    private String cancelRemarks;
    private String cancellationReason;
    public static final String UNCHECKED = "unchecked";
    public static final String CANCEL_RE = "cancelRE";
    private MeasurementBookService measurementBookService;
    private ContractorAdvanceService contractorAdvanceService;

    public SearchRevisionEstimateAction() {
        addRelatedEntity("category", EgwTypeOfWork.class);
        addRelatedEntity("parentCategory", EgwTypeOfWork.class);
        addRelatedEntity("executingDepartment", Department.class);
        addRelatedEntity("natureOfWork", NatureOfWork.class);
        addRelatedEntity("egwStatus", EgwStatus.class);
    }

    @Override
    public Object getModel() {
        return estimates;
    }

    @Override
    public void prepare() {
        super.prepare();
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        addDropdownData(
                "statusList",
                persistenceService
                        .findAllBy(
                                "from EgwStatus s where moduletype=? and code in ('CREATED','REJECTED','RESUBMITTED','CANCELLED','BUDGETARY_APPR_CHECKED','BUDGETARY_APPROPRIATION_DONE','BUDGETARY_APPR_VALIDATED','APPROVED') order by orderId",
                                AbstractEstimate.class.getSimpleName()));
        addDropdownData("executingDepartmentList", persistenceService.findAllBy("from Department dt"));
        addDropdownData("typeList", persistenceService.findAllBy("from NatureOfWork"));
        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
        addDropdownData("categoryList", Collections.emptyList());
        populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
        if (CANCEL_RE.equals(source)) {
            final EgwStatus egwstat = (EgwStatus) persistenceService.find(
                    "from EgwStatus where moduletype=? and code = 'APPROVED' order by orderId",
                    AbstractEstimate.class.getSimpleName());
            setReStatus(egwstat.getId());
        }
    }

    @Action(value = "/revisionEstimate/searchRevisionEstimate-beforeSearch")
    public String beforeSearch() {
        return SEARCH;
    }

    public String searchRE() {
        return SEARCH;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final StringBuffer query = new StringBuffer(300);
        final List<Object> paramList = new ArrayList<Object>();
        query.append(
                "from WorkOrderEstimate woeP, WorkOrderEstimate woeC where woeP.estimate.id= woeC.estimate.parent.id and woeC.estimate.egwStatus.code!='NEW' ");
        if (reStatus != null && reStatus != -1) {
            query.append(" and woeC.estimate.egwStatus.id = ?");
            paramList.add(reStatus);
        }

        if (getExecDept() != null && getExecDept() != -1) {
            query.append(" and woeC.estimate.executingDepartment.id = ?");
            paramList.add(getExecDept());
        }
        if (null != estimates.getEstimateNumber() && StringUtils.isNotEmpty(estimates.getEstimateNumber())) {
            query.append(" and woeC.estimate.estimateNumber like '%'||?||'%'");
            paramList.add(estimates.getEstimateNumber());
        }
        if (estimates.getNatureOfWork() != null) {
            query.append(" and woeP.estimate.natureOfWork.id = ?");
            paramList.add(estimates.getNatureOfWork().getId());
        }
        if (null != fromDate && getFieldErrors().isEmpty()) {
            query.append(" and woeC.estimate.estimateDate >= ?");
            paramList.add(fromDate);
        }
        if (null != toDate && getFieldErrors().isEmpty()) {
            query.append(" and woeC.estimate.estimateDate <= ?");
            paramList.add(toDate);
        }
        if (estimates.getCategory() != null) {
            query.append(" and woeP.estimate.category.id = ?");
            paramList.add(estimates.getCategory().getId());
        }
        if (estimates.getParentCategory() != null) {
            query.append(" and woeP.estimate.parentCategory.id = ?");
            paramList.add(estimates.getParentCategory().getId());
        }

        if (null != workOrderNumber && StringUtils.isNotEmpty(workOrderNumber)) {
            query.append(" and woeC.workOrder.parent.workOrderNumber like '%'||?||'%'");
            paramList.add(workOrderNumber);
        }
        if (source != null && source.equalsIgnoreCase(CANCEL_RE)) {
            query.append(
                    " and woeC.estimate.id = (select max(absEst.id) from AbstractEstimate absEst where absEst.parent.id = woeP.estimate.id and  "
                            + " absEst.egwStatus.code!= ? ) ");
            paramList.add(WorksConstants.CANCELLED_STATUS);
        }
        LOGGER.debug("SearchRevisionEstimate | prepareQuery | query >>>> " + query.toString());
        return new SearchQueryHQL("select woeC " + query.toString(), "select count(distinct woeC.id) "
                + query.toString(), paramList);

    }

    public String list() {
        boolean isError = false;
        if (fromDate != null && toDate == null) {
            addFieldError("enddate", getText("search.endDate.null"));
            isError = true;
        }
        if (toDate != null && fromDate == null) {
            addFieldError("startdate", getText("search.startDate.null"));
            isError = true;
        }

        if (!DateUtils.compareDates(getToDate(), getFromDate())) {
            addFieldError("enddate", getText("greaterthan.endDate.fromDate"));
            isError = true;
        }

        if (isError)
            return SEARCH;
        setPageSize(WorksConstants.PAGE_SIZE);
        search();
        showOwnerName();
        return SEARCH;
    }

    public List<String> getActionsList() {
        final String actions = worksService.getWorksConfigValue("RE_SHOW_ACTIONS");
        if (actions != null)
            return Arrays.asList(actions.split(","));
        return new ArrayList<String>();
    }

    protected void populateCategoryList(final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(estimates.getParentCategory().getId());
            ajaxEstimateAction.subcategories();
            addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());
        } else
            addDropdownData("categoryList", Collections.emptyList());
    }

    @ValidationErrorPage(value = "search")
    public String cancelApprovedRE() {
        final RevisionWorkOrder revWorkOrder = revisionWorkOrderService.findById(revWOId, false);
        final RevisionAbstractEstimate re = revisionAbstractEstimateService.findById(revWorkOrder
                .getWorkOrderEstimates().get(0).getEstimate().getId(), false);
        if (!validateRECancellation(revWorkOrder, re))
            return list();

        validateARFForRE(re);

        revWorkOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("WorkOrder", "CANCELLED"));
        final PersonalInformation prsnlInfo = employeeServiceOld.getEmpForUserId(worksService.getCurrentLoggedInUserId());
        String empName = "";
        re.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("AbstractEstimate", "CANCELLED"));
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
        /****
         * State oldEndState = re.getCurrentState(); Position owner = prsnlInfo.getAssignment(new Date()).getPosition();
         * oldEndState.setCreatedBy(prsnlInfo.getUserMaster()); oldEndState.setModifiedBy(prsnlInfo.getUserMaster());
         * oldEndState.setCreatedDate(new Date()); oldEndState.setModifiedDate(new Date()); oldEndState.setOwner(owner);
         * oldEndState.setValue(WorksConstants.CANCELLED_STATUS); oldEndState.setText1(cancellationText); re.changeState("END",
         * owner, null);
         ***/

        // Release the budget
        revisionEstimateService.releaseBudget(re);
        revisionEstimateNumber = re.getEstimateNumber();
        messageKey = revisionEstimateNumber + ": " + getText("revisionEstimate.Cancel");
        return SUCCESS;
    }

    private boolean validateRECancellation(final RevisionWorkOrder revWorkOrder,
            final RevisionAbstractEstimate revEstimate) {
        final List<WorkOrderActivity> revWoaList = persistenceService.findAllBy(
                "from WorkOrderActivity where workOrderEstimate.workOrder.id=?", revWorkOrder.getId());
        final List<Long> activtityIdList = new ArrayList<Long>();
        if (revWoaList != null && revWoaList.size() > 0) {
            List<MBHeader> mbheaderlist = new ArrayList<MBHeader>();
            String errorMessage;
            // First check if any non tendered or lumpsum items are present, if
            // yes then dont allow to cancel
            for (final WorkOrderActivity revWoa : revWoaList)
                if (revWoa.getActivity().getRevisionType() != null
                        && (revWoa.getActivity().getRevisionType().equals(RevisionType.LUMP_SUM_ITEM) || revWoa
                                .getActivity().getRevisionType().equals(RevisionType.NON_TENDERED_ITEM))) {
                    mbheaderlist = measurementBookService
                            .findAllBy(
                                    "select distinct mbd.mbHeader from MBDetails mbd where mbd.workOrderActivity.workOrderEstimate.estimate.id=? and mbd.workOrderActivity.workOrderEstimate.workOrder.id=? and  mbd.workOrderActivity.activity.id=? "
                                            + "and mbd.mbHeader.egwStatus.code<>'CANCELLED'",
                                    revEstimate.getId(),
                                    revWorkOrder.getId(), revWoa.getActivity().getId());
                    if (mbheaderlist != null && !mbheaderlist.isEmpty()) {
                        final StringBuffer mbNos = new StringBuffer();
                        for (final MBHeader mbHdr : mbheaderlist)
                            mbNos.append(mbHdr.getMbRefNo() + ", ");
                        errorMessage = getText("cancelRE.MB.created.message")
                                + mbNos.toString().substring(0, mbNos.length() - 2) + ". "
                                + getText("cancelRE.MB.created.message.part2");
                        addActionError(errorMessage);
                        return false;
                    }
                }
            for (final WorkOrderActivity revWoa : revWoaList)
                // Add only additional quantity items activities
                if (revWoa.getActivity().getRevisionType() != null
                        && revWoa.getActivity().getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
                    activtityIdList.add(revWoa.getActivity().getParent().getId()); // Passing
            // parent
            // of
            // Rev
            // Work
            // order
            // activity,
            // as
            // only
            // these
            // can
            // have
            // MBs
            // created
            // for
            // them
            if (activtityIdList != null && activtityIdList.size() > 0) {
                final Query qry = getPersistenceService()
                        .getSession()
                        .createQuery(
                                " select workOrderActivity.activity.id, nvl(sum(quantity),0)  from MBDetails where mbHeader.egwStatus.code!='CANCELLED' and workOrderActivity.activity.id in (:activtityIdList) group by workOrderActivity.activity.id ");
                qry.setParameterList("activtityIdList", activtityIdList);
                final List<Object[]> activityIdQuantityList = qry.list();
                if (activityIdQuantityList != null && activityIdQuantityList.size() > 0)
                    for (final WorkOrderActivity revWoa : revWoaList) {
                        if (revWoa.getActivity().getRevisionType() != null
                                && !revWoa.getActivity().getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
                            continue;
                        for (final Object[] activityIdQuantity : activityIdQuantityList)
                            if (Long.parseLong(activityIdQuantity[0].toString()) == revWoa.getActivity().getParent()
                                    .getId().longValue()) {
                                Long activityId = null;
                                if (revWoa.getActivity().getParent() == null)
                                    activityId = revWoa.getActivity().getId();
                                else
                                    activityId = revWoa.getActivity().getParent().getId();
                                final double originalQuantity = (Double) persistenceService
                                        .find("select sum(woa.approvedQuantity) from WorkOrderActivity woa  group by woa,woa.activity having activity.id = ?",
                                                activityId);
                                final Object revEstQuantityObj = persistenceService
                                        .find(" select sum(woa.approvedQuantity*nvl(decode(woa.activity.revisionType,'REDUCED_QUANTITY',-1,'ADDITIONAL_QUANTITY',1,'NON_TENDERED_ITEM',1,'LUMP_SUM_ITEM',1),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code = 'APPROVED'  and woa.activity.abstractEstimate.id != ? group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id = ? )  ",
                                                revEstimate.getId(), revWoa.getActivity().getParent().getId());
                                final double revEstQuantity = revEstQuantityObj == null ? 0.0
                                        : (Double) revEstQuantityObj;
                                if (originalQuantity + revEstQuantity >= Double.parseDouble(activityIdQuantity[1]
                                        .toString()))
                                    continue;
                                else {
                                    final MBDetails mbDetails = (MBDetails) persistenceService
                                            .find(" from MBDetails mbd where mbd.mbHeader.egwStatus.code != 'CANCELLED' and mbd.workOrderActivity.activity.id = ? and (mbdetailsDate is not null or OrderNumber is not null) ",
                                                    revWoa.getActivity().getParent().getId());
                                    if (mbDetails != null) {
                                        Double maxPercent = worksService.getConfigval();
                                        if (maxPercent != null)
                                            maxPercent += 100;
                                        else
                                            maxPercent = 100d;
                                        final Double maxAllowedQuantity = maxPercent
                                                * (originalQuantity + revEstQuantity) / 100;
                                        if (maxAllowedQuantity >= Double.parseDouble(activityIdQuantity[1].toString()))
                                            continue;
                                        else {
                                            addActionError(getText("cancelRE.MBs.present.message"));
                                            return false;
                                        }
                                    } else {
                                        addActionError(getText("cancelRE.MBs.present.message"));
                                        return false;
                                    }
                                }
                            }
                    }
            }
        }
        return true;
    }

    /*
     * Validate is there any Advance Requisition forms created for the Work Order Estimate pertaining to this RE
     */
    private void validateARFForRE(final RevisionAbstractEstimate revisionEstimate) {
        String arfNo = "";
        BigDecimal advanceAmount = new BigDecimal(0);
        final List<WorkOrderEstimate> woeList = persistenceService.findAllBy(
                " from WorkOrderEstimate woe where woe.workOrder.egwStatus.code = 'APPROVED' and woe.estimate=?",
                revisionEstimate.getParent());
        if (woeList != null && !woeList.isEmpty())
            for (final WorkOrderEstimate woe : woeList)
                for (final ContractorAdvanceRequisition arf : woe.getContractorAdvanceRequisitions())
                    if (!arf.getStatus()
                            .getCode()
                            .equalsIgnoreCase(
                                    ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED
                                            .toString())) {
                        advanceAmount = advanceAmount.add(arf.getAdvanceRequisitionAmount());
                        if (!arfNo.equals(""))
                            arfNo = arfNo.concat(", ").concat(arf.getAdvanceRequisitionNumber());
                        else
                            arfNo = arfNo.concat(arf.getAdvanceRequisitionNumber());
                    }
        if (!arfNo.equals("")) {
            final BigDecimal totalEstimateValueIncludingRE = contractorAdvanceService
                    .getTotalEstimateValueIncludingRE(revisionEstimate.getParent());
            if (totalEstimateValueIncludingRE.subtract(new BigDecimal(revisionEstimate.getTotalAmount().getValue()))
                    .longValue() < advanceAmount.longValue())
                throw new ValidationException(Arrays.asList(new ValidationError("cancelRE.arf.created.message",
                        getText("cancelRE.arf.created.message", new String[] { arfNo }))));
        }
    }

    /**
     * @return List of abstract estimates with "positionAndUserName" populated
     */
    @SuppressWarnings(UNCHECKED)
    private void showOwnerName() {
        final List<WorkOrderEstimate> woEstimateList = new LinkedList<WorkOrderEstimate>();

        final Iterator iter = searchResult.getList().iterator();
        while (iter.hasNext()) {
            final Object row = iter.next();
            final WorkOrderEstimate woe = (WorkOrderEstimate) row;
            if (!woe.getEstimate().getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                    && !woe.getEstimate().getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)) {
                final PersonalInformation emp = employeeServiceOld.getEmployeeforPosition(woe.getEstimate().getState()
                        .getOwnerPosition());
                if (emp != null)
                    if (emp.getUserMaster() != null)
                        woe.getEstimate().setPositionAndUserName(emp.getUserMaster().getName());
            }
            woEstimateList.add(woe);
        }
        searchResult.getList().clear();
        final HashSet<WorkOrderEstimate> uniqueWOEstimateList = new HashSet<WorkOrderEstimate>(woEstimateList);
        searchResult.getList().addAll(uniqueWOEstimateList);
    }

    public AbstractEstimate getEstimates() {
        return estimates;
    }

    public void setEstimates(final AbstractEstimate estimates) {
        this.estimates = estimates;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<RevisionAbstractEstimate> getRevEstimateList() {
        return revEstimateList;
    }

    public void setRevEstimateList(final List<RevisionAbstractEstimate> revEstimateList) {
        this.revEstimateList = revEstimateList;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Integer getExecDept() {
        return execDept;
    }

    public void setExecDept(final Integer execDept) {
        this.execDept = execDept;
    }

    public Integer getReStatus() {
        return reStatus;
    }

    public void setReStatus(final Integer reStatus) {
        this.reStatus = reStatus;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Long getRevWOId() {
        return revWOId;
    }

    public void setRevWOId(final Long revWOId) {
        this.revWOId = revWOId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public void setRevisionAbstractEstimateService(
            final PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
        this.revisionAbstractEstimateService = revisionAbstractEstimateService;
    }

    public void setRevisionWorkOrderService(final PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
        this.revisionWorkOrderService = revisionWorkOrderService;
    }

    public String getRevisionEstimateNumber() {
        return revisionEstimateNumber;
    }

    public void setRevisionEstimateNumber(final String revisionEstimateNumber) {
        this.revisionEstimateNumber = revisionEstimateNumber;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setCancelRemarks(final String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public void setRevisionEstimateService(final RevisionEstimateService revisionEstimateService) {
        this.revisionEstimateService = revisionEstimateService;
    }

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public void setContractorAdvanceService(final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

}