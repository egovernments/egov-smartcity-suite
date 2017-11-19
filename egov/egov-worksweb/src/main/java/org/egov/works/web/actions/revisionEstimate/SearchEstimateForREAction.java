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
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ParentPackage("egov")
@Result(name = SearchEstimateForREAction.SEARCH_WO, location = "searchEstimateForRE-searchWO.jsp")
public class SearchEstimateForREAction extends SearchFormAction {

    private static final long serialVersionUID = -8446251759865551258L;
    private AbstractEstimate estimates = new AbstractEstimate();
    private WorkOrder workOrder = new WorkOrder();
    private String searchType = "searchType";
    private Date fromDate;
    private Date toDate;
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final String SEARCH_WO = "searchWO";
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private WorkOrderService workOrderService;
    private Integer deptId;
    private WorksService worksService;
    public final static String APPROVED = "APPROVED";
    private String estimateNumber;
    private Long typeId;
    private String workOrderNumber;
    private Long parentCategory;
    private Long category;
    private Long contractorId;
    private Long execDept;
    private AbstractEstimateService abstractEstimateService;

    public SearchEstimateForREAction() {
        addRelatedEntity("category", EgwTypeOfWork.class);
        addRelatedEntity("parentCategory", EgwTypeOfWork.class);
        addRelatedEntity("executingDepartment", Department.class);
        addRelatedEntity("natureOfWork", NatureOfWork.class);

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
        addDropdownData("executingDepartmentList", worksService.getAllDeptmentsForLoggedInUser());
        addDropdownData("typeList", persistenceService.findAllBy("from NatureOfWork "));
        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
        addDropdownData("categoryList", Collections.emptyList());
        populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
        if (abstractEstimateService.getLatestAssignmentForCurrentLoginUser() != null)
            execDept = abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDepartment().getId();
    }

    @ValidationErrorPage(value = SEARCH_WO)
    @Action(value = "/revisionEstimate/searchEstimateForRE-searchWO")
    public String searchWorkOrder() {

        return SEARCH_WO;
    }

    private Map getQuery() {
        final StringBuffer query = new StringBuffer(700);
        final List<Object> paramList = new ArrayList<Object>();
        final HashMap<String, Object> queryAndParams = new HashMap<String, Object>();
        query.append(
                "from WorkOrderEstimate woe where woe.workOrder.id is not null and woe.workOrder.parent is null and woe.workOrder.egwStatus.code<>? "
                        + "and woe.workOrder.egwStatus.code = ? and woe.estimate.parent is null");
        paramList.add("NEW");
        paramList.add("APPROVED");
        if (getDeptId() != null && getDeptId() != -1) {
            query.append(" and woe.estimate.executingDepartment.id=? ");
            paramList.add(getDeptId());
        }
        if (getTypeId() != -1) {
            query.append(" and woe.estimate.natureOfWork.id=? ");
            paramList.add(Long.valueOf(getTypeId()));
        }
        if (StringUtils.isNotBlank(getEstimateNumber())) {
            query.append(" and UPPER(woe.estimate.estimateNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(getEstimateNumber()).toUpperCase());
        }

        if (StringUtils.isNotBlank(getWorkOrderNumber())) {
            query.append(" and UPPER(woe.workOrder.workOrderNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(getWorkOrderNumber()).toUpperCase());
        }

        if (estimates.getCategory() != null) {
            query.append(" and woe.estimate.category.id=?");
            paramList.add(estimates.getCategory().getId());
        }
        if (estimates.getParentCategory() != null) {
            query.append(" and woe.estimate.parentCategory.id=?");
            paramList.add(estimates.getParentCategory().getId());
        }

        if (getContractorId() != -1) {
            query.append(" and woe.workOrder.contractor.id=? ");
            paramList.add(Long.valueOf(getContractorId()));
        }

        if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            query.append(" and woe.workOrder.workOrderDate between ? and ? ");
            paramList.add(fromDate);
            paramList.add(toDate);
        }
        query.append("and woe.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where"
                + " mbh.egwStatus.code = ? and (mbh.egBillregister.billstatus <> ? and mbh.egBillregister.billtype = ?) and"
                + " mbh.workOrderEstimate.workOrder.egwStatus.code='APPROVED' and mbh.workOrderEstimate.estimate.egwStatus.code=?) "
                +
                // " and woe.workOrder.id not in (select wo1.parent.id from WorkOrder wo1 where wo1.parent is not null and
                // wo1.egwStatus.code not in ('APPROVED','CANCELLED')) "
                // +
                "and woe.estimate.id not in "
                + "(select ae.parent.id from AbstractEstimate ae where ae.parent is not null and ae.egwStatus.code not in ('APPROVED','CANCELLED'))");
        paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
        paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
        paramList.add(getFinalBillTypeConfigValue());
        paramList.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
        queryAndParams.put("query", query.toString());
        queryAndParams.put("params", paramList);

        return queryAndParams;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {

        String query = null;
        String countQuery = null;
        Map queryAndParms = null;
        List<Object> paramList = new ArrayList<Object>();
        queryAndParms = getQuery();
        paramList = (List<Object>) queryAndParms.get("params");
        query = (String) queryAndParms.get("query");
        countQuery = "select count(distinct woe.id) " + query;
        query = "select distinct woe " + query;
        return new SearchQueryHQL(query, countQuery, paramList);
    }

    @Override
    public String search() {
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
            return "searchWO";

        setPageSize(WorksConstants.PAGE_SIZE);
        super.search();
        return "searchWO";
    }

    protected void populateCategoryList(final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(estimates.getParentCategory().getId());
            ajaxEstimateAction.subcategories();
            addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());
        } else
            addDropdownData("categoryList", Collections.emptyList());
    }

    public Map<String, Object> getContractorForApprovedWorkOrder() {
        final Map<String, Object> contractorsWithWOList = new LinkedHashMap<String, Object>();
        if (workOrderService.getContractorsWithWO() != null)
            for (final Contractor contractor : workOrderService.getContractorsWithWO())
                contractorsWithWOList.put(contractor.getId() + "", contractor.getCode() + " - " + contractor.getName());
        return contractorsWithWOList;
    }

    public String getFinalBillTypeConfigValue() {
        return worksService.getWorksConfigValue("FinalBillType");
    }

    public String getApprovedValue() {
        return WorksConstants.APPROVED;
    }

    public AbstractEstimate getEstimates() {
        return estimates;
    }

    public void setEstimates(final AbstractEstimate estimates) {
        this.estimates = estimates;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(final String searchType) {
        this.searchType = searchType;
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

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(final Integer deptId) {
        this.deptId = deptId;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(final Long typeId) {
        this.typeId = typeId;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
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

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public Long getExecDept() {
        return execDept;
    }

    public void setExecDept(final Long execDept) {
        this.execDept = execDept;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

}