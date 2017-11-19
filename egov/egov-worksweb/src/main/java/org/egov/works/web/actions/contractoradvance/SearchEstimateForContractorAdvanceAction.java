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
package org.egov.works.web.actions.contractoradvance;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.TenderResponseService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sathish P
 *
 */
@ParentPackage("egov")
@Result(name = SearchEstimateForContractorAdvanceAction.SEARCH, location = "searchEstimateForContractorAdvance-search.jsp")
public class SearchEstimateForContractorAdvanceAction extends SearchFormAction {

    private static final long serialVersionUID = 9140665581257254905L;
    @Autowired
    private DepartmentService departmentService;
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final String SEARCH = "search";
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private PersistenceService<Contractor, Long> contractorService;
    private TenderResponseService tenderResponseService;
    private String estimateStatus;
    private String estimateNumber;
    private Date fromDate;
    private Date toDate;
    private Long contractorId;
    private String wpNumber;
    private String tenderNegotiationNumber;
    private int executingDepartmentId;
    private String workOrderNumber;

    public SearchEstimateForContractorAdvanceAction() {
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void prepare() {

        super.prepare();
        addDropdownData("executingDepartmentList", departmentService.getAllDepartments());
        estimateStatus = AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString();
    }

    @Action(value = "/contractoradvance/searchEstimateForContractorAdvance-beforeSearch")
    public String beforeSearch() {
        return SEARCH;
    }

    private Map getQuery() {
        final StringBuffer query = new StringBuffer(700);
        final List<Object> paramList = new ArrayList<Object>();
        final HashMap<String, Object> queryAndParams = new HashMap<String, Object>();
        query.append("from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.workOrder.egwStatus.code = ? ");
        paramList.add("APPROVED");

        query.append(" and NOT EXISTS (select 1 from ContractorAdvanceRequisition arf where arf.workOrderEstimate.id = woe.id " +
                " and arf.status.code not in(?,?)) ");
        paramList.add(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString());
        paramList.add(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString());

        query.append("and NOT EXISTS (select 1 from MBHeader mbh where mbh.workOrderEstimate.id = woe.id and " +
                " mbh.egwStatus.code = ? and (mbh.egBillregister is not null and mbh.egBillregister.billstatus <> ?))");
        paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
        paramList.add(ContractorBillRegister.BillStatus.CANCELLED.toString());

        if (StringUtils.isNotBlank(estimateNumber)) {
            query.append(" and UPPER(woe.estimate.estimateNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(estimateNumber).toUpperCase());
        }

        if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            query.append(" and woe.workOrder.approvedDate between ? and ? ");
            paramList.add(fromDate);
            paramList.add(toDate);
        }

        if (contractorId != 0 && contractorId != -1) {
            query.append(" and woe.workOrder.contractor.id = ? ");
            paramList.add(Long.valueOf(contractorId));
        }

        if (StringUtils.isNotBlank(wpNumber)) {
            query.append(" and UPPER(woe.workOrder.packageNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(wpNumber).toUpperCase());
        }

        if (StringUtils.isNotBlank(tenderNegotiationNumber)) {
            query.append(" and UPPER(woe.workOrder.negotiationNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(tenderNegotiationNumber).toUpperCase());
        }

        if (executingDepartmentId != 0 && executingDepartmentId != -1) {
            query.append(" and woe.estimate.executingDepartment.id = ? ");
            paramList.add(executingDepartmentId);
        }

        if (StringUtils.isNotBlank(workOrderNumber)) {
            query.append(" and UPPER(woe.workOrder.workOrderNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(workOrderNumber).toUpperCase());
        }

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
        return SEARCH;
    }

    public String searchList() {
        getPersistenceService().getSession().setDefaultReadOnly(true);
        getPersistenceService().getSession().setFlushMode(FlushMode.MANUAL);
        boolean isError = false;
        if (fromDate != null && toDate == null) {
            addFieldError("enddate", getText("search.endDate.null"));
            isError = true;
        }
        if (toDate != null && fromDate == null) {
            addFieldError("startdate", getText("search.startDate.null"));
            isError = true;
        }

        if (!DateUtils.compareDates(toDate, fromDate)) {
            addFieldError("enddate", getText("greaterthan.endDate.fromDate"));
            isError = true;
        }

        if (isError)
            return SEARCH;

        setPageSize(WorksConstants.PAGE_SIZE);
        super.search();

        if (searchResult != null && searchResult.getList() != null && !searchResult.getList().isEmpty())
            setTenderType();
        return SEARCH;
    }

    protected void setTenderType() {
        final List<WorkOrderEstimate> woeList = new ArrayList<WorkOrderEstimate>();

        final Iterator i = searchResult.getList().iterator();
        while (i.hasNext()) {
            final WorkOrderEstimate woe = (WorkOrderEstimate) i.next();
            final TenderResponse tenderResponse = tenderResponseService
                    .find("from TenderResponse tr where tr.negotiationNumber =? " +
                            "and tr.egwStatus.code = ?", woe.getWorkOrder().getNegotiationNumber(),
                            TenderResponse.TenderResponseStatus.APPROVED.toString());
            woe.getWorkOrder().setTenderType(tenderResponse.getTenderEstimate().getTenderType());
            woeList.add(woe);
        }
        searchResult.getList().clear();
        searchResult.getList().addAll(woeList);
    }

    public Map<String, Object> getContractorForApprovedWorkOrder() {
        final Map<String, Object> contractorsWithWOList = new LinkedHashMap<String, Object>();
        final List<Contractor> approvedContractorList = contractorService.findAllByNamedQuery("getApprovedContractorsWithWO");
        if (approvedContractorList != null)
            for (final Contractor contractor : approvedContractorList)
                contractorsWithWOList.put(contractor.getId() + "", contractor.getCode() + " - " + contractor.getName());
        return contractorsWithWOList;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void setContractorService(
            final PersistenceService<Contractor, Long> contractorService) {
        this.contractorService = contractorService;
    }

    public String getEstimateStatus() {
        return estimateStatus;
    }

    public void setEstimateStatus(final String estimateStatus) {
        this.estimateStatus = estimateStatus;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
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

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getWpNumber() {
        return wpNumber;
    }

    public void setWpNumber(final String wpNumber) {
        this.wpNumber = wpNumber;
    }

    public String getTenderNegotiationNumber() {
        return tenderNegotiationNumber;
    }

    public void setTenderNegotiationNumber(final String tenderNegotiationNumber) {
        this.tenderNegotiationNumber = tenderNegotiationNumber;
    }

    public int getExecutingDepartmentId() {
        return executingDepartmentId;
    }

    public void setExecutingDepartmentId(final int executingDepartmentId) {
        this.executingDepartmentId = executingDepartmentId;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public void setTenderResponseService(final TenderResponseService tenderResponseService) {
        this.tenderResponseService = tenderResponseService;
    }

}