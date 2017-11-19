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
package org.egov.works.web.actions.tender;

import org.apache.commons.lang.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AjaxTenderNegotiationAction extends BaseFormAction {

    private static final long serialVersionUID = -9058467908860158263L;
    private static final String MARKETVALUE = "marketValue";
    private AbstractEstimateService abstractEstimateService;
    private WorksService worksService;
    private Long estimateId;
    private Long packageId;
    private Date asOnDate;
    private double marketRateAmount = 0;
    private WorksPackageService workspackageService;
    @Autowired
    private AssignmentService assignmentService;
    private TenderResponse tenderResp = new TenderResponse();
    // for prepared by
    private static final String USERS_IN_DEPT = "usersInDept";
    private static final String DESIGN_FOR_EMP = "designForEmp";
    private Integer executingDepartment;
    private Long empID;
    private List usersInExecutingDepartment;
    private Assignment assignment;
    private String tenderNo;
    private Long checkId;
    private Long id;
    private static final String TENDERNUMBERUNIQUECHECK = "tenderNumberUniqueCheck";
    private PersonalInformationService personalInformationService;
    private static final String GET_WORKORDERS_TN = "getWorkOrdersForTN";
    private List<WorkOrder> workOrderList = new ArrayList<WorkOrder>();
    private WorkOrderService workOrderService;
    private String tenderNegotiationNo;
    private String query = "";
    private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
    private List<ProjectCode> projectCodeList = new LinkedList<ProjectCode>();
    private List<String> negotiationNumberList = new LinkedList<String>();
    private List<WorksPackage> wpNumberList = new LinkedList<WorksPackage>();
    private List<WorksPackage> tenderFileNumberList = new LinkedList<WorksPackage>();

    public String getMarketValue() {
        if (estimateId != null)
            getMarketValueForEstimate(estimateId);
        if (packageId != null)
            getMarketValueForPackage(packageId);
        return MARKETVALUE;
    }

    public double getMarketValueForEstimate(final Long estimateId) {
        final AbstractEstimate abstractEstimate = abstractEstimateService.findById(estimateId, false);
        final Collection<Activity> sorActivities = abstractEstimate.getSORActivities();
        final Map<String, Integer> exceptionaSorMap = worksService.getExceptionSOR();
        for (final Activity activity : sorActivities) {
            double marketrate = 0;
            double marketrateAmt = 0;
            final ScheduleOfRate scheduleOfRate = activity.getSchedule();
            if (scheduleOfRate.hasValidMarketRateFor(asOnDate)) {
                double result = 1;
                final MarketRate marketRate = scheduleOfRate.getMarketRateOn(asOnDate);
                if (activity.getUom() != null && StringUtils.isNotBlank(activity.getUom().getUom())
                        && exceptionaSorMap.containsKey(activity.getUom().getUom()))
                    result = exceptionaSorMap.get(activity.getUom().getUom());
                marketrate = marketRate.getMarketRate().getValue() / result;
            }
            if (marketrate == 0)
                marketrateAmt = activity.getAmount().getValue();
            else
                marketrateAmt = activity.getQuantity() * marketrate;
            marketRateAmount = marketRateAmount + marketrateAmt;
        }
        final Collection<Activity> nonSorActivities = abstractEstimate.getNonSORActivities();
        for (final Activity activity : nonSorActivities)
            marketRateAmount = marketRateAmount + activity.getAmount().getValue();
        return marketRateAmount;
    }

    public double getMarketValueForPackage(final Long packageId) {
        final WorksPackage workspackage = workspackageService.findById(packageId, false);
        for (final EstimateLineItemsForWP estlineItem : workspackage.getActivitiesForEstimate()) {
            double marketrate = 0;
            double marketrateAmt = 0;
            final Map<String, Integer> exceptionaSorMap = worksService.getExceptionSOR();
            if (StringUtils.isNotBlank(estlineItem.getCode())) {
                final ScheduleOfRate scheduleOfRate = estlineItem.getActivity().getSchedule();
                if (scheduleOfRate.hasValidMarketRateFor(asOnDate)) {
                    double result = 1;
                    final MarketRate marketRate = scheduleOfRate.getMarketRateOn(asOnDate);
                    if (exceptionaSorMap.containsKey(estlineItem.getUom()))
                        result = exceptionaSorMap.get(estlineItem.getUom());
                    marketrate = marketRate.getMarketRate().getValue() / result;
                }
                if (marketrate == 0)
                    marketrateAmt = estlineItem.getAmt();
                else
                    marketrateAmt = estlineItem.getQuantity() * marketrate;
                marketRateAmount = marketRateAmount + marketrateAmt;
            } else
                marketRateAmount = marketRateAmount + estlineItem.getActivity().getAmount().getValue();
        }
        return marketRateAmount;
    }

    public double getMarketRateWithTax(final double marketrate, final Activity activity) {
        double marketrateAmt = 0;
        if (marketrate == 0)
            marketrateAmt = activity.getAmountIncludingTax().getValue();
        else {
            marketrateAmt = activity.getQuantity() * marketrate;
            marketrateAmt += activity.getServiceTaxPerc() * marketrateAmt / 100;
        }
        return marketrateAmt;
    }

    public String tenderNumberUniqueCheck() {
        return TENDERNUMBERUNIQUECHECK;
    }

    // for preparedby
    public String designationForUser() {
        try {
            assignment = assignmentService.getPrimaryAssignmentForEmployee(empID);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return DESIGN_FOR_EMP;
    }

    public boolean getTendernoCheck() {
        boolean tenderNoexistsOrNot = false;
        TenderResponse tenderResponseObj = null;
        if (id == null) {
            tenderResponseObj = (TenderResponse) getPersistenceService().findByNamedQuery("TenderNumberUniqueCheck",
                    tenderNo);
            if (tenderResponseObj != null)
                tenderNoexistsOrNot = true;
        } else if (getPersistenceService().findByNamedQuery("TenderNumberUniqueCheckForEdit", tenderNo, id) != null)
            tenderNoexistsOrNot = true;
        return tenderNoexistsOrNot;
    }

    public String usersInExecutingDepartment() {
        try {
            final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
            criteriaParams.put("departmentId", executingDepartment);
            criteriaParams.put("isPrimary", "Y");
            if (executingDepartment == null || executingDepartment == -1)
                usersInExecutingDepartment = Collections.EMPTY_LIST;
            else
                usersInExecutingDepartment = personalInformationService.getListOfEmployeeViewBasedOnCriteria(
                        criteriaParams, -1, -1);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return USERS_IN_DEPT;
    }

    public String getWODetailsForTN() throws Exception {
        List<WorkOrder> woList = new ArrayList<WorkOrder>();
        woList = workOrderService
                .findAllBy(
                        "select distinct wo from WorkOrder wo where wo.egwStatus.code<>'CANCELLED' and wo.negotiationNumber=? ",
                        tenderNegotiationNo);

        if (woList != null && !woList.isEmpty())
            workOrderList.addAll(woList);

        return GET_WORKORDERS_TN;
    }

    public String searchNegotiationNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = " select distinct tr.negotiationNumber from TenderResponse tr where upper(tr.negotiationNumber) like '%'||?||'%' and tr.egwStatus.code <> ? ";
            params.add(query.toUpperCase());
            params.add("NEW");
            negotiationNumberList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "negotiationNumberSearchResults";
    }

    public String searchWorksPackageNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct tr.tenderEstimate.worksPackage from TenderResponse tr where upper(tr.tenderEstimate.worksPackage.wpNumber) like '%'||?||'%' and tr.egwStatus.code <> ? ";
            params.add(query.toUpperCase());
            params.add("NEW");
            wpNumberList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "worksPackageNumberSearchResults";
    }

    public String searchTenderFileNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct tr.tenderEstimate.worksPackage from TenderResponse tr where upper(tr.tenderEstimate.worksPackage.tenderFileNumber) like '%'||?||'%' and tr.egwStatus.code <> ? ";
            params.add(query.toUpperCase());
            params.add("NEW");
            tenderFileNumberList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "tenderFileNumberSearchResults";
    }

    public String searchProjectCode() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct wpd.estimate.projectCode from WorksPackageDetails wpd where upper(wpd.estimate.projectCode.code) like '%'||?||'%' "
                    + " and wpd.worksPackage.id in (select distinct tr.tenderEstimate.worksPackage.id from TenderResponse tr where tr.egwStatus.code <> ? )";
            params.add(query.toUpperCase());
            params.add("NEW");
            projectCodeList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "projectCodeSearchResults";
    }

    public String searchEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select wpd.estimate from WorksPackageDetails wpd where upper(wpd.estimate.estimateNumber) like '%'||?||'%' "
                    + " and wpd.worksPackage.id in (select distinct tr.tenderEstimate.worksPackage.id from TenderResponse tr where tr.egwStatus.code <> ? )";
            params.add(query.toUpperCase());
            params.add("NEW");
            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateNoSearchResults";
    }

    @Override
    public Object getModel() {

        return tenderResp;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public double getMarketRateAmount() {
        return marketRateAmount;
    }

    public void setMarketRateAmount(final double marketRateAmount) {
        this.marketRateAmount = marketRateAmount;
    }

    public void setEmpID(final Long empID) {
        this.empID = empID;
    }

    public List getUsersInExecutingDepartment() {
        return usersInExecutingDepartment;
    }

    public void setExecutingDepartment(final Integer executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(final Long packageId) {
        this.packageId = packageId;
    }

    public void setWorkspackageService(final WorksPackageService workspackageService) {
        this.workspackageService = workspackageService;
    }

    public String getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(final String tenderNo) {
        this.tenderNo = tenderNo;
    }

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(final Long checkId) {
        this.checkId = checkId;
    }

    public TenderResponse getTenderResp() {
        return tenderResp;
    }

    public void setTenderResp(final TenderResponse tenderResp) {
        this.tenderResp = tenderResp;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public String getTenderNegotiationNo() {
        return tenderNegotiationNo;
    }

    public void setTenderNegotiationNo(final String tenderNegotiationNo) {
        this.tenderNegotiationNo = tenderNegotiationNo;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(final List<AbstractEstimate> estimateList) {
        this.estimateList = estimateList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<ProjectCode> getProjectCodeList() {
        return projectCodeList;
    }

    public void setProjectCodeList(final List<ProjectCode> projectCodeList) {
        this.projectCodeList = projectCodeList;
    }

    public List<String> getNegotiationNumberList() {
        return negotiationNumberList;
    }

    public List<WorksPackage> getWpNumberList() {
        return wpNumberList;
    }

    public void setWpNumberList(final List<WorksPackage> wpNumberList) {
        this.wpNumberList = wpNumberList;
    }

    public List<WorksPackage> getTenderFileNumberList() {
        return tenderFileNumberList;
    }

    public void setTenderFileNumberList(final List<WorksPackage> tenderFileNumberList) {
        this.tenderFileNumberList = tenderFileNumberList;
    }

}
