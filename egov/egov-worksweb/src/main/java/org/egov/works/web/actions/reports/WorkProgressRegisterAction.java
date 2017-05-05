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
package org.egov.works.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.MilestoneActivity;
import org.egov.works.milestone.entity.PaymentDetail;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.entity.TrackMilestoneActivity;
import org.egov.works.milestone.entity.WorkProgressRegister;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.ContractorBillService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
        @Result(name = WorkProgressRegisterAction.PRINT_PDF, type = "stream", location = "workProgressRegisterStream", params = {
                "inputName", "workProgressRegisterStream", "contentType", "application/pdf", "contentDisposition",
                "no-cache;filename=WorkProgressRegisterReport.pdf" }),
        @Result(name = WorkProgressRegisterAction.PRINT_EXCEL, type = "stream", location = "workProgressRegisterStream", params = {
                "inputName", "workProgressRegisterStream", "contentType", "application/xls", "contentDisposition",
                "no-cache;filename=WorkProgressRegisterReport.xls" }) })
public class WorkProgressRegisterAction extends SearchFormAction {

    private static final long serialVersionUID = -30571718037168928L;
    private static final Logger logger = Logger.getLogger(WorkProgressRegisterAction.class);
    private static final String ASSIGNED_USER_LIST1 = "assignedUserList1";
    private static final String ASSIGNED_USER_LIST2 = "assignedUserList2";
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private BoundaryService boundaryService;
    private EgovCommon egovCommon;
    private ContractorBillService contractorBillService;
    ReportService reportService;
    private final Map<String, String> milestoneStatuses = new LinkedHashMap<String, String>();
    public static final String dateFormat = "dd/MM/yyyy";
    private Long parentCategory;
    private Long category;
    private String workOrderStatus;
    private String milestoneStatus;
    @Autowired
    private FundHibernateDAO fundDao;
    @Autowired
    private FunctionHibernateDAO functionHibDao;
    private Long expenditureType;
    private Integer fund;
    private Long function;
    private Long budgetHead;
    private Date toDate;
    private Date fromDate;
    private Long execDept;
    private Integer preparedBy;
    private Integer engineerIncharge;
    private Integer engineerIncharge2;
    private static final String TENDER_NOTICE_STATUS = "Noticeinvitingtenderreleased";
    private static final String TENDER_FINALIZATION_STATUS = "L1 tender finalised";
    private static final String BUDGET_HEAD_EMPTY_MSG = "Not Applicable";
    private static final String TENDER_AGGREEMENT_ORDER = "Agreement Order signed";
    private String exportType;
    private InputStream workProgressRegisterStream;
    public static final String PRINT_PDF = "printPDF";
    public static final String PRINT_EXCEL = "printExcel";
    private Integer woId;
    private String sourcePage = "";
    private Integer scheme;
    private Integer subScheme;
    private PersistenceService<OfflineStatus, Long> worksStatusService;
    private static final String STATUS_OBJECTID = "getStatusDateByObjectId_Type_Desc";
    private static final String WO_OBJECT_TYPE = "WorkOrder";
    private static final String WORK_COMMENCED = "Work commenced";
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    private List<WorkProgressRegister> workProgressList = new ArrayList<WorkProgressRegister>();
    private Integer estId;
    private String contractorName = "";
    private Long contractorId;
    private String searchCriteria = "";
    private Long wardId;
    private String wardName = "";

    @Override
    public void prepare() {

        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        populateCategoryList(ajaxEstimateAction, parentCategory != null);

        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw where etw.parentid is null"));
        addDropdownData(
                ASSIGNED_USER_LIST1,
                getPersistenceService()
                        .findAllBy(
                                "select distinct wo.engineerIncharge from  WorkOrder wo where wo.engineerIncharge.employeeName is not null"));
        addDropdownData(
                ASSIGNED_USER_LIST2,
                getPersistenceService()
                        .findAllBy(
                                "select distinct wo.engineerIncharge2 from  WorkOrder wo where wo.engineerIncharge2.employeeName is not null"));
        addDropdownData("typeList", getPersistenceService().findAllBy("from NatureOfWork dt"));
        addDropdownData("executingDepartmentList",
                getPersistenceService().findAllBy("from Department order by upper(ame)"));

        final List<EgwStatus> workOrdStatusList = getPersistenceService().findAllBy(
                "from EgwStatus st where st.moduletype=? and st.code in (?,?,?,?)", WO_OBJECT_TYPE,
                WorksConstants.APPROVED, WorksConstants.WO_STATUS_WOACKNOWLEDGED,
                WorksConstants.WO_STATUS_WOSITEHANDEDOVER, WorksConstants.WO_STATUS_WOCOMMENCED);
        addDropdownData("workOrderStatuses", workOrdStatusList);
        addDropdownData("fundList", fundDao.findAll());
        addDropdownData("budgetGroupList", getPersistenceService().findAllBy("from BudgetGroup order by name"));
        addDropdownData("functionList", functionHibDao.findAll());
        addDropdownData(
                "preparedByList",
                getPersistenceService()
                        .findAllBy(
                                "select distinct wo.workOrderPreparedBy from WorkOrder wo where wo.workOrderPreparedBy.employeeName is not null"));
        addDropdownData("schemeList",
                getPersistenceService().findAllBy("from org.egov.commons.Scheme sc where sc.isactive=true"));
        final AjaxWorkProgressAction ajaxWorkProgressAction = new AjaxWorkProgressAction();
        populateSubSchemeList(ajaxWorkProgressAction, getScheme() != null);
        prepareMilestoneStatuses();
    }

    public void prepareMilestoneStatuses() {
        milestoneStatuses.put("Inprogress1to25", "between 0.01 and 25");
        milestoneStatuses.put("Inprogress26to50", "between 25.01 and 50");
        milestoneStatuses.put("Inprogress51to75", "between 50.01 and 75");
        milestoneStatuses.put("Inprogress76to99", "between 75.01 and 99.99");
        milestoneStatuses.put("Completed", "=100");
    }

    protected void populateSubSchemeList(final AjaxWorkProgressAction ajaxWorkProgressAction,
            final boolean schemePopulated) {
        if (schemePopulated) {
            ajaxWorkProgressAction.setPersistenceService(getPersistenceService());
            ajaxWorkProgressAction.setSchemeId(getScheme());
            ajaxWorkProgressAction.loadSubSchemes();
            addDropdownData("subSchemeList", ajaxWorkProgressAction.getSubSchemes());
        } else
            addDropdownData("subSchemeList", Collections.emptyList());
    }

    public List getMilestoneStatuses() {
        return new LinkedList<String>(milestoneStatuses.keySet());
    }

    @Override
    public String search() {

        return "search";
    }

    public List<EgwStatus> getWorkOrderStatuses() {
        return egwStatusHibernateDAO.getStatusByModule(WorkOrder.class.getSimpleName());
    }

    @SkipValidation
    public String searchDetails() {
        if (!DateUtils.compareDates(toDate, fromDate)) {
            addFieldError("enddate", getText("greaterthan.endDate.fromDate"));
            return "search";
        }

        super.search();

        final List<Object> objects = searchResult.getList();
        final ArrayList<WorkProgressRegister> workProgressRegisterList = (ArrayList<WorkProgressRegister>) getWorkProgressRegisterList(
                objects);
        searchResult.getList().clear();
        searchResult.getList().addAll(workProgressRegisterList);

        return "search";
    }

    private List getWorkProgressRegisterList(final List workPorgressRegisterList) {
        final Iterator iter = workPorgressRegisterList.iterator();
        final List<WorkProgressRegister> tempList = new ArrayList<WorkProgressRegister>();
        while (iter.hasNext()) {
            final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) iter.next();
            WorkOrder workOrder = null;
            AbstractEstimate estimate = null;
            Milestone milestone = null;
            TrackMilestone trackMilestone = null;
            Set<MBHeader> mbHeaders = null;

            workOrder = workOrderEstimate.getWorkOrder();
            estimate = workOrderEstimate.getEstimate();
            mbHeaders = workOrderEstimate.getMbHeaders();

            if (workOrderEstimate != null && workOrderEstimate.getMilestone() != null
                    && workOrderEstimate.getMilestone().size() != 0)
                for (final Milestone tempMilestone : workOrderEstimate.getMilestone())
                    if (tempMilestone.getStatus().getCode().equalsIgnoreCase("APPROVED"))
                        milestone = tempMilestone;

            if (milestone != null && milestone.getTrackMilestone() != null && milestone.getTrackMilestone().size() != 0)
                for (final TrackMilestone temptrackMilestone : milestone.getTrackMilestone())
                    if (temptrackMilestone.getStatus().getCode().equalsIgnoreCase("APPROVED"))
                        trackMilestone = temptrackMilestone;
            if (workOrderEstimate != null) {
                final WorkProgressRegister workProgress = new WorkProgressRegister();
                workProgress.setDept(estimate.getExecutingDepartment().getName());
                workProgress.setWard(estimate.getWard().getName());
                workProgress.setLocation(estimate.getLocation());
                workProgress.setProjectCode(estimate.getProjectCode().getCode());
                workProgress.setEstimateNo(estimate.getEstimateNumber());
                workProgress.setNameOfWork(estimate.getName());
                workProgress.setTypeOfWork(estimate.getParentCategory().getDescription());
                workProgress.setEstimateDate(DateUtils.getFormattedDate(estimate.getEstimateDate(), dateFormat));
                workProgress.setEstimateAmt(new BigDecimal(estimate.getWorkValue()));
                final Date techSanctionDate = getTechSanctionDate(estimate);
                if (techSanctionDate != null)
                    workProgress.setTechSanctionDate(DateUtils.getFormattedDate(techSanctionDate, dateFormat));
                else
                    workProgress.setTechSanctionDate(null);
                workProgress.setAdminSanctionDate(DateUtils.getFormattedDate(estimate.getState().getCreatedDate(), dateFormat));
                workProgress.setFund(estimate.getFinancialDetails().get(0).getFund().getCode());
                workProgress.setFunction(estimate.getFinancialDetails().get(0).getFunction().getCode());
                if (estimate.getFinancialDetails().get(0).getBudgetGroup() != null)
                    workProgress.setBudgetHead(estimate.getFinancialDetails().get(0).getBudgetGroup().getName());
                else
                    workProgress.setBudgetHead(BUDGET_HEAD_EMPTY_MSG);
                workProgress.setApprInfo(workProgress.getFund() + " || " + workProgress.getFunction() + " || "
                        + workProgress.getBudgetHead());
                int count = 0;
                if (estimate.getAbstractEstimateAppropriations() != null) {

                    String apprDetails = null;
                    for (final AbstractEstimateAppropriation estimateApp : estimate.getAbstractEstimateAppropriations()) {
                        ++count;
                        if (estimateApp.getBudgetUsage() != null) {
                            if (estimateApp.getBudgetUsage().getConsumedAmount() != 0) {
                                final String finyearRange = finHibernateDao.getFinancialYearById(
                                        estimateApp.getBudgetUsage().getFinancialYearId().longValue())
                                        .getFinYearRange();
                                if (apprDetails != null)
                                    apprDetails = apprDetails
                                            + ", "
                                            + count
                                            + ")"
                                            + finyearRange
                                            + ", "
                                            + NumberUtil.formatNumber(new BigDecimal(estimateApp.getBudgetUsage()
                                                    .getConsumedAmount()));
                                else
                                    apprDetails = count
                                            + ")"
                                            + finyearRange
                                            + ", "
                                            + NumberUtil.formatNumber(new BigDecimal(estimateApp.getBudgetUsage()
                                                    .getConsumedAmount()));
                            }
                        } else if (estimateApp.getDepositWorksUsage().getConsumedAmount().equals(BigDecimal.ZERO)) {
                            final String finyearRange = finHibernateDao.getFinancialYearById(
                                    estimateApp.getDepositWorksUsage().getFinancialYear().getId().longValue())
                                    .getFinYearRange();
                            if (apprDetails != null)
                                apprDetails = apprDetails
                                        + ", "
                                        + count
                                        + ")"
                                        + finyearRange
                                        + ", "
                                        + NumberUtil.formatNumber(new BigDecimal(estimateApp.getBudgetUsage()
                                                .getConsumedAmount()));
                            else
                                apprDetails = count
                                        + ")"
                                        + finyearRange
                                        + ", "
                                        + NumberUtil.formatNumber(new BigDecimal(estimateApp.getBudgetUsage()
                                                .getConsumedAmount()));
                        }
                    }
                    workProgress.setApprDetails(apprDetails);
                }
                final Map tenderDetail = getTenderDetails(estimate.getId());
                if (tenderDetail.get("tenderDate") != null)
                    workProgress.setTenderDate(DateUtils.getFormattedDate((Date) tenderDetail.get("tenderDate"),
                            dateFormat));
                if (tenderDetail.get("tenderFinalizationDate") != null)
                    workProgress.setTenderFinalizationDate(DateUtils.getFormattedDate(
                            (Date) tenderDetail.get("tenderFinalizationDate"), dateFormat));
                if (tenderDetail.get("aggreementDate") != null)
                    workProgress.setTenderAgreementDate(DateUtils.getFormattedDate(
                            (Date) tenderDetail.get("aggreementDate"), dateFormat));
                if (mbHeaders != null) {
                    final Map paymentDetails = getPaymentDetail(mbHeaders);
                    workProgress.setPaymentDetails((List<PaymentDetail>) paymentDetails.get("paymentDetails"));
                    workProgress.setTotalBillAmt((BigDecimal) paymentDetails.get("totalBillAmt"));
                    workProgress.setTotalReleasedAmt((BigDecimal) paymentDetails.get("totalReleasedAmt"));
                    workProgress.setTotalOutstandingAmt((BigDecimal) paymentDetails.get("totalOutstandingAmt"));
                    if ((Boolean) paymentDetails.get("isFinalBillCreated") != null)
                        workProgress.setIsFinalBillCreated((Boolean) paymentDetails.get("isFinalBillCreated"));
                }
                workProgress.setWorkOrderValue(new BigDecimal(workOrder.getWorkOrderAmount()));
                if (workOrder != null) {
                    final OfflineStatus objStatusForWorkComncd = worksStatusService.findByNamedQuery(STATUS_OBJECTID,
                            workOrder.getId(), WO_OBJECT_TYPE, WORK_COMMENCED);
                    final OfflineStatus objStatusForSiteHandOver = worksStatusService.findByNamedQuery(STATUS_OBJECTID,
                            workOrder.getId(), WO_OBJECT_TYPE, WorksConstants.WO_STATUS_WOSITEHANDEDOVER);
                    if (objStatusForWorkComncd != null)
                        workProgress.setWorkCommencementDate(DateUtils.getFormattedDate(
                                objStatusForWorkComncd.getStatusDate(), dateFormat));
                    if (objStatusForSiteHandOver != null)
                        workProgress.setSiteHandedOverDate(DateUtils.getFormattedDate(
                                objStatusForSiteHandOver.getStatusDate(), dateFormat));
                }
                workProgress.setContractPeriod(workOrder.getContractPeriod().toString());
                workProgress.setWorkOrderDate(DateUtils.getFormattedDate(workOrder.getWorkOrderDate(), dateFormat));
                if (trackMilestone != null && "APPROVED".equalsIgnoreCase(trackMilestone.getStatus().getCode())) {
                    workProgress.setTrackMilestoneActivities(trackMilestone.getActivities());
                    workProgress.setCompletedPercentage(trackMilestone.getTotalPercentage());
                } else if (milestone != null && "APPROVED".equalsIgnoreCase(milestone.getStatus().getCode())) {
                    final List<TrackMilestoneActivity> trackList = new LinkedList<TrackMilestoneActivity>();
                    for (final MilestoneActivity milestoneActivity : milestone.getActivities()) {
                        final TrackMilestoneActivity trackMilestoneActivity = new TrackMilestoneActivity();
                        trackMilestoneActivity.setMilestoneActivity(milestoneActivity);
                        trackList.add(trackMilestoneActivity);
                    }
                    workProgress.setTrackMilestoneActivities(trackList);
                } else
                    workProgress.setTrackMilestoneActivities(Collections.EMPTY_LIST);

                workProgress.setContractorName(workOrder.getContractor().getCode() + "-"
                        + workOrder.getContractor().getName());
                workProgress.setProjectCode(estimate.getProjectCode().getCode());
                if (workProgress.getIsFinalBillCreated())
                    workProgress.setProjectStatus("Completed");
                else if ("END".equalsIgnoreCase(workOrder.getCurrentState().getValue())
                        && workOrder.getEgwStatus().getCode().equalsIgnoreCase("CANCELLED"))
                    workProgress.setProjectStatus("Cancelled");
                else
                    workProgress.setProjectStatus("In Progress");

                tempList.add(workProgress);
            }
        }

        return tempList;

    }

    @Override
    public Object getModel() {

        return null;
    }

    protected void populateCategoryList(final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(parentCategory);
            ajaxEstimateAction.subcategories();
            addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());
        } else
            addDropdownData("categoryList", Collections.emptyList());
    }

    private Date getTechSanctionDate(final AbstractEstimate estimate) {
        for (final StateHistory stateHistory : estimate.getCurrentState().getHistory())
            if (stateHistory.getValue().equalsIgnoreCase("TECH_SANCTIONED"))
                return stateHistory.getCreatedDate();
        return null;
    }

    private Map getTenderDetails(final Long estimateId) {

        final String query = "select wpkg,tr from TenderResponse tr,WorksPackage wpkg left outer join wpkg.worksPackageDetails wpkgd where tr.tenderEstimate.worksPackage.id=wpkg.id and wpkgd.estimate.id=?";
        final ArrayList<Object> paramList = new ArrayList<Object>();
        paramList.add(estimateId);
        final List<Object> result = persistenceService.findAllBy(query, paramList.toArray());
        Object[] objects;
        final Iterator iterator = result.iterator();
        final HashMap<String, Object> tenderDates = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            objects = (Object[]) iterator.next();
            if (objects[0] != null) {
                final WorksPackage worksPackage = (WorksPackage) objects[0];
                for (final OfflineStatus status : worksPackage.getOfflineStatuses()) {
                    if (TENDER_NOTICE_STATUS.equalsIgnoreCase(status.getEgwStatus().getCode()))
                        tenderDates.put("tenderDate", status.getCreatedDate());
                    if (TENDER_FINALIZATION_STATUS.equalsIgnoreCase(status.getEgwStatus().getCode()))
                        tenderDates.put("tenderFinalizationDate", status.getCreatedDate());
                }
            }
            if (objects[1] != null) {
                final TenderResponse tenderResponse = (TenderResponse) objects[1];
                if (tenderResponse != null) {
                    final OfflineStatus objStatusForSite = worksStatusService.findByNamedQuery(STATUS_OBJECTID,
                            tenderResponse.getTenderResponseContractors().get(0).getId(), "TenderResponseContractors",
                            TENDER_AGGREEMENT_ORDER);
                    if (objStatusForSite != null)
                        tenderDates.put("aggreementDate", objStatusForSite.getStatusDate());
                }
            }
        }
        return tenderDates;
    }

    public Map<String, Object> getPaymentDetail(final Set<MBHeader> mbHeaders) {
        BigDecimal totalBillAmount = BigDecimal.ZERO;
        BigDecimal totalReleasedAmt = BigDecimal.ZERO;
        BigDecimal totalOutstandingAmt = BigDecimal.ZERO;
        BigDecimal totalNetPayableAmt = BigDecimal.ZERO;
        final HashMap<String, Object> result = new HashMap<String, Object>();
        final List<PaymentDetail> paymentDetailList = new LinkedList<PaymentDetail>();
        for (final MBHeader mbHeader : mbHeaders) {
            final PaymentDetail paymentDetail = new PaymentDetail();
            final EgBillregister egBillRegister = mbHeader.getEgBillregister();
            BigDecimal netPayableAmt = BigDecimal.ZERO;

            if (egBillRegister != null)
                if (egBillRegister.getStatus() != null
                        && egBillRegister.getStatus().getCode().equalsIgnoreCase("APPROVED")) {
                    paymentDetail.setBillAmount(egBillRegister.getBillamount());
                    paymentDetail.setBillDate(DateUtils.getFormattedDate(egBillRegister.getBilldate(), dateFormat));
                    paymentDetail.setBillNumber(egBillRegister.getBillnumber());
                    paymentDetail.setBillType(egBillRegister.getBilltype());
                    if (egBillRegister.getEgBillregistermis().getVoucherHeader() != null
                            && egBillRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber() != null
                            && !egBillRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber().equals("")
                            && egBillRegister.getEgBillregistermis().getVoucherHeader().getStatus() != null
                            && egBillRegister.getEgBillregistermis().getVoucherHeader().getStatus() == 0) {

                        paymentDetail.setCjvNo(egBillRegister.getEgBillregistermis().getVoucherHeader()
                                .getVoucherNumber());
                        logger.debug("Bill Number : " + egBillRegister.getBillnumber() + " --- CJVNo : "
                                + egBillRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber());
                    }
                    try {
                        paymentDetail.setReleasedAmount(egovCommon.getPaymentAmount(egBillRegister.getId()));
                        netPayableAmt = contractorBillService.getNetPayableAmountForGlCodeId(egBillRegister.getId());
                    } catch (final ApplicationException egovExp) {
                        logger.error("Error: Getting payment for a contractor bill", egovExp);
                        paymentDetail.setReleasedAmount(BigDecimal.ZERO);
                    }

                    paymentDetail.setOutstandingAmount(netPayableAmt.subtract(paymentDetail.getReleasedAmount()));
                    totalNetPayableAmt = totalNetPayableAmt.add(netPayableAmt);
                    totalBillAmount = totalBillAmount.add(paymentDetail.getBillAmount());
                    totalReleasedAmt = totalReleasedAmt.add(paymentDetail.getReleasedAmount());
                    if (egBillRegister.getBilltype().equalsIgnoreCase(
                            (String) contractorBillService.getBillType().get(1))
                            && egBillRegister.getEgBillregistermis().getVoucherHeader() != null
                            && egBillRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber() != null
                            && !egBillRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber().equals("")
                            && egBillRegister.getEgBillregistermis().getVoucherHeader().getStatus() != null
                            && egBillRegister.getEgBillregistermis().getVoucherHeader().getStatus() == 0)
                        result.put("isFinalBillCreated", Boolean.TRUE);
                    else
                        result.put("isFinalBillCreated", Boolean.FALSE);
                    paymentDetailList.add(paymentDetail);
                }
        }

        totalOutstandingAmt = totalNetPayableAmt.subtract(totalReleasedAmt);
        result.put("paymentDetails", paymentDetailList);
        result.put("totalBillAmt", totalBillAmount);
        result.put("totalReleasedAmt", totalReleasedAmt);
        result.put("totalOutstandingAmt", totalOutstandingAmt);

        return result;
    }

    public Long getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(final Long parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(final Long category) {
        this.category = category;
    }

    public String getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(final String workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public void setMilestoneStatus(final String milestoneStatus) {
        this.milestoneStatus = milestoneStatus;
    }

    public Long getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(final Long expenditureType) {
        this.expenditureType = expenditureType;
    }

    public Integer getFund() {
        return fund;
    }

    public void setFund(final Integer fund) {
        this.fund = fund;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Long getExecDept() {
        return execDept;
    }

    public void setExecDept(final Long execDept) {
        this.execDept = execDept;
    }

    public Integer getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(final Integer preparedBy) {
        this.preparedBy = preparedBy;
    }

    public Integer getEngineerIncharge() {
        return engineerIncharge;
    }

    public void setEngineerIncharge(final Integer engineerIncharge) {
        this.engineerIncharge = engineerIncharge;
    }

    public Integer getEngineerIncharge2() {
        return engineerIncharge2;
    }

    public void setEngineerIncharge2(final Integer engineerIncharge2) {
        this.engineerIncharge2 = engineerIncharge2;
    }

    @SkipValidation
    public String viewWorkProgressRegister() {
        final HashMap<String, Object> queryMap = getQueryForWorkProgressRegister();
        final String finalQuery = (String) queryMap.get("finalQuery");
        final ArrayList<Object> paramList = (ArrayList<Object>) queryMap.get("params");
        final List workPorgressRegisterList = persistenceService.findAllBy(finalQuery, paramList.toArray());
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("searchCriteria", searchCriteria);
        final ReportRequest reportInput = new ReportRequest("workProgressRegister",
                getWorkProgressRegisterList(workPorgressRegisterList), reportParams);
        if (PRINT_EXCEL.equalsIgnoreCase(exportType))
            reportInput.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            workProgressRegisterStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        if (PRINT_PDF.equalsIgnoreCase(exportType))
            return PRINT_PDF;
        else
            return PRINT_EXCEL;
    }

    public HashMap<String, Object> getQueryForWorkProgressRegister() {
        final StringBuffer query = new StringBuffer(500);
        final ArrayList<Object> paramList = new ArrayList<Object>();
        final HashMap<String, Object> queryMap = new HashMap<String, Object>();
        final StringBuilder srchCrit = new StringBuilder(3000);
        final StringBuffer orderQry = new StringBuffer(100);
        srchCrit.append("Report");
        query.append(
                "from org.egov.works.models.workorder.WorkOrderEstimate as woe left outer join woe.milestone milestone left outer join milestone.trackMilestone trackMilestone ");
        query.append("where woe.workOrder.parent is null and woe.workOrder.egwStatus.code='APPROVED' ");
        query.append("and milestone.egwStatus.code='APPROVED' and trackMilestone.egwStatus.code='APPROVED' ");
        if (sourcePage == null || StringUtils.isEmpty(sourcePage)) {
            if (!workOrderStatus.equalsIgnoreCase("-1")) {
                srchCrit.append(" for Work Order Status " + workOrderStatus);
                if (workOrderStatus.equalsIgnoreCase("APPROVED")) {
                    query.append(" and woe.workOrder.egwStatus.code=?");
                    query.append(
                            " and woe.workOrder.id not in (select objectId from org.egov.works.models.tender.OfflineStatus where objectType=?)");
                    paramList.add(workOrderStatus);
                    paramList.add(WorkOrder.class.getSimpleName());
                } else {
                    query.delete(0, query.length() - 1);
                    query.append(
                            "from org.egov.works.models.workorder.WorkOrderEstimate  as woe left outer join woe.milestone milestone left outer join milestone.trackMilestone trackMilestone,org.egov.works.models.tender.OfflineStatus st");
                    query.append(
                            " where st.objectId=woe.workOrder.id and st.id=(select max(id) from org.egov.works.models.tender.OfflineStatus where objectId=woe.workOrder.id and objectType=?) and st.objectType=? and st.egwStatus.code=?");
                    query.append(" and woe.workOrder.parent is null and woe.workOrder.egwStatus.code='APPROVED' ");
                    query.append(" and milestone.egwStatus.code='APPROVED' and trackMilestone.egwStatus.code='APPROVED' ");
                    paramList.add(WorkOrder.class.getSimpleName());
                    paramList.add(WorkOrder.class.getSimpleName());
                    paramList.add(workOrderStatus);

                }
            }

            if (execDept != -1) {
                final Department dept = departmentService.getDepartmentById(execDept);
                srchCrit.append(" in " + dept.getName() + " Department ");
                query.append(" and woe.estimate.executingDepartment.id=?");
                paramList.add(execDept);
            }

            if (fromDate != null && toDate == null && getFieldErrors().isEmpty()) {
                srchCrit.append(" from " + DateUtils.getFormattedDate(fromDate, dateFormat) + " to current date ");
                query.append(" and woe.workOrder.workOrderDate >= ? ");
                paramList.add(fromDate);
            }
            if (fromDate == null && toDate != null && getFieldErrors().isEmpty()) {
                srchCrit.append(" as on " + DateUtils.getFormattedDate(toDate, dateFormat));
                query.append(" and woe.workOrder.workOrderDate <= ? ");
                paramList.add(toDate);
            }
            if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
                srchCrit.append(" for date range " + DateUtils.getFormattedDate(fromDate, dateFormat) + " - "
                        + DateUtils.getFormattedDate(toDate, dateFormat));
                query.append(" and woe.workOrder.workOrderDate between ? and ? ");
                paramList.add(fromDate);
                paramList.add(toDate);
            }

            if (contractorId != null) {
                final Contractor contractor = (Contractor) getPersistenceService().find("from Contractor where id=?",
                        contractorId);
                srchCrit.append(" for Contractor " + contractor.getCode() + "-" + contractor.getName());
                query.append("and woe.workOrder.contractor.id=? ");
                paramList.add(contractorId);
            }

            if (expenditureType != -1) {
                final NatureOfWork wtype = (NatureOfWork) getPersistenceService().find("from NatureOfWork where id=?",
                        expenditureType);
                srchCrit.append(" with Nature of Work " + wtype.getName());
                query.append(" and woe.estimate.type.id=?");
                paramList.add(expenditureType);
            }

            if (fund != -1) {
                final Fund f = (Fund) getPersistenceService().find("from Fund where id=?", fund);
                srchCrit.append(" under Fund " + f.getName());
                query.append(" and woe.estimate.financialDetails[0].fund.id=?");
                paramList.add(fund);
            }

            if (function != -1) {
                final CFunction fun = (CFunction) getPersistenceService().find("from CFunction where id=?", function);
                srchCrit.append(" for Function " + fun.getName());
                query.append(" and woe.estimate.financialDetails[0].function.id=?");
                paramList.add(function);
            }

            if (parentCategory != -1) {
                final EgwTypeOfWork tow = (EgwTypeOfWork) getPersistenceService().find(
                        "from EgwTypeOfWork etw where etw.parentid is null and id=?", parentCategory);
                srchCrit.append(" with Type of Work " + tow.getDescription());
                query.append(" and woe.estimate.parentCategory.id=?");
                paramList.add(parentCategory);
            }

            if (category != -1) {
                final EgwTypeOfWork subtow = (EgwTypeOfWork) getPersistenceService().find(
                        "from EgwTypeOfWork etw where id=? and parentid.id=?", category, parentCategory);
                srchCrit.append(" and Subtype of Work " + subtow.getDescription());
                query.append(" and woe.estimate.category.id=?");
                paramList.add(category);
            }

            if (preparedBy != -1) {
                final PersonalInformation prepBy = (PersonalInformation) getPersistenceService().find(
                        "from PersonalInformation where id=?", preparedBy);
                srchCrit.append(" as Prepared by " + prepBy.getEmployeeName());
                query.append(" and woe.workOrder.workOrderPreparedBy.idPersonalInformation=?");
                paramList.add(preparedBy);
            }

            if (getScheme() != null && getScheme() != -1) {
                final Scheme sch = (Scheme) getPersistenceService().find("from Scheme where isactive=true and id=?",
                        getScheme());
                srchCrit.append(" under Scheme " + sch.getName());
                query.append(" and woe.estimate.financialDetails[0].scheme.id=?");
                paramList.add(getScheme());
            }

            if (getSubScheme() != null && getSubScheme() != -1) {
                final SubScheme subsch = (SubScheme) getPersistenceService().find("from SubScheme where id=?",
                        getSubScheme());
                srchCrit.append(" and Subscheme " + subsch.getName());
                query.append(" and woe.estimate.financialDetails[0].subScheme.id=?");
                paramList.add(getSubScheme());
            }

            if (budgetHead != -1) {
                final BudgetGroup bh = (BudgetGroup) getPersistenceService().find("from BudgetGroup where id=?",
                        budgetHead);
                srchCrit.append(" with Budget Head " + bh.getName());
                query.append(" and woe.estimate.financialDetails[0].budgetGroup.id=?");
                paramList.add(budgetHead);
            }

            if (wardId != null) {
                final Boundary wardObj = boundaryService.getBoundaryById(wardId);
                srchCrit.append(" under Jurisdiction " + wardObj.getName());
                query.append(" and woe.estimate.ward.id = ? ");
                paramList.add(wardId);
            }

            if (!milestoneStatus.equalsIgnoreCase("-1")) {
                srchCrit.append(" with Milestone Status " + milestoneStatus);
                query.append(" and trackMilestone.total " + milestoneStatuses.get(milestoneStatus));
                query.append(" and trackMilestone.egwStatus.code=? ");
                paramList.add("APPROVED");
            }

            if (engineerIncharge != null && engineerIncharge != -1) {
                final PersonalInformation engInc1 = (PersonalInformation) getPersistenceService().find(
                        "from PersonalInformation where id=?", engineerIncharge);
                srchCrit.append(" for Work Order Assigned to User1 " + engInc1.getEmployeeName());
                query.append(" and woe.workOrder.engineerIncharge.idPersonalInformation=?");
                paramList.add(engineerIncharge);
            }

            if (engineerIncharge2 != null && engineerIncharge2 != -1) {
                final PersonalInformation engInc2 = (PersonalInformation) getPersistenceService().find(
                        "from PersonalInformation where id=?", engineerIncharge2);
                srchCrit.append(" for Work Order Assigned to User2 " + engInc2.getEmployeeName());
                query.append(" and woe.workOrder.engineerIncharge2.idPersonalInformation=?");
                paramList.add(engineerIncharge2);
            }
            searchCriteria = srchCrit.toString();
        }
        if (sourcePage != null
                && (sourcePage.equalsIgnoreCase("deptWiseReport") || sourcePage.equalsIgnoreCase("deptWiseReportForWP"))) {
            if (woId != null) {
                query.append(" and woe.workOrder.id=?");
                paramList.add(Long.valueOf(woId.toString()));
            }
            if (estId != null) {
                query.append(" and woe.estimate.id=?");
                paramList.add(Long.valueOf(estId.toString()));
            }
        }

        orderQry.append(" order by woe.id desc ");
        final String countQuery = "select count(distinct woe.id) " + query.toString();
        final String finalQuery = "select woe  " + query.append(orderQry).toString();
        queryMap.put("countQuery", countQuery);
        queryMap.put("finalQuery", finalQuery);
        queryMap.put("params", paramList);

        return queryMap;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final HashMap<String, Object> queryMap = getQueryForWorkProgressRegister();
        setPageSize(10);
        final String finalQuery = (String) queryMap.get("finalQuery");
        final String countQuery = (String) queryMap.get("countQuery");
        final ArrayList<Object> paramList = (ArrayList<Object>) queryMap.get("params");
        return new SearchQueryHQL(finalQuery, countQuery, paramList);
    }

    public void setWorkProgressList(final List<WorkProgressRegister> workProgressList) {
        this.workProgressList = workProgressList;
    }

    public Long getFunction() {
        return function;
    }

    public void setFunction(final Long function) {
        this.function = function;
    }

    public Long getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(final Long budgetHead) {
        this.budgetHead = budgetHead;
    }

    public List<WorkProgressRegister> getWorkProgressList() {
        return workProgressList;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(final String exportType) {
        this.exportType = exportType;
    }

    public InputStream getWorkProgressRegisterStream() {
        return workProgressRegisterStream;
    }

    public void setWorkProgressRegisterStream(final InputStream workProgressRegisterStream) {
        this.workProgressRegisterStream = workProgressRegisterStream;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public Integer getWoId() {
        return woId;
    }

    public void setWoId(final Integer woId) {
        this.woId = woId;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(final String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public Integer getScheme() {
        return scheme;
    }

    public void setScheme(final Integer scheme) {
        this.scheme = scheme;
    }

    public Integer getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final Integer subScheme) {
        this.subScheme = subScheme;
    }

    public PersistenceService<OfflineStatus, Long> getWorksStatusService() {
        return worksStatusService;
    }

    public void setWorksStatusService(final PersistenceService<OfflineStatus, Long> worksStatusService) {
        this.worksStatusService = worksStatusService;
    }

    public Integer getEstId() {
        return estId;
    }

    public void setEstId(final Integer estId) {
        this.estId = estId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(final String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }
}
