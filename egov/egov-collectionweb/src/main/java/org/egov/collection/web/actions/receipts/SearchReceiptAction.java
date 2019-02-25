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
package org.egov.collection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.displaytag.pagination.PaginatedList;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.EgwStatus;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.models.ServiceDetails;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
        @Result(name = SearchReceiptAction.SUCCESS, location = "searchReceipt.jsp")
})
public class SearchReceiptAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private Integer serviceTypeId = -1;
    private Long userId = (long) -1;
    private String instrumentType;
    private String receiptNumber;
    private Date fromDate;
    private Date toDate;
    private Integer searchStatus = -1;
    private String target = NEW;
    private String manualReceiptNumber;
    private List<ReceiptHeader> resultList;
    private String serviceClass = "-1";
    private TreeMap<String, String> serviceClassMap = new TreeMap<>();
    private Integer branchId;
    private int pageNum = CollectionConstants.PAGENUM;
    private int pageSize = CollectionConstants.PAGESIZE;
    private PaginatedList searchResult;

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private CollectionsUtil collectionsUtil;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Object getModel() {
        return null;
    }


    @Action(value = "/receipts/searchReceipt-reset")
    public String reset() {
        setPage(1);
        serviceTypeId = -1;
        userId = (long) -1;
        receiptNumber = CollectionConstants.BLANK;
        fromDate = null;
        toDate = null;
        instrumentType = CollectionConstants.BLANK;
        searchStatus = -1;
        manualReceiptNumber = CollectionConstants.BLANK;
        serviceClass = "-1";
        branchId = -1;
        return SUCCESS;
    }

    @Override
    public void prepare() {
        super.prepare();
        setupDropdownDataExcluding();
        addDropdownData("instrumentTypeList",
                getCurrentSession().createQuery("from InstrumentType i where i.isActive = true order by type").list());
        addDropdownData("userList",
                entityManager.createNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS, User.class).getResultList());
        serviceClassMap.putAll(CollectionConstants.SERVICE_TYPE_CLASSIFICATION);
        serviceClassMap.remove(CollectionConstants.SERVICE_TYPE_PAYMENT);
        addDropdownData("serviceTypeList", Collections.emptyList());
        addDropdownData("bankBranchList", collectionsUtil.getBankCollectionBankBranchList());
        if (!getServiceClass().equals("-1"))
            addDropdownData("serviceTypeList",
                    entityManager.createNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE, ServiceDetails.class)
                            .setParameter(1, getServiceClass()).getResultList());

    }

    @Override
    @Action(value = "/receipts/searchReceipt")
    public String execute() {
        return SUCCESS;
    }

    public List<EgwStatus> getReceiptStatuses() {
        Query query = getCurrentSession()
                .createQuery("from EgwStatus s where moduletype=:moduleType and code !=:statusCode order by description");
        query.setParameter("moduleType", CollectionConstants.MODULE_NAME_RECEIPTHEADER, StringType.INSTANCE);
        query.setParameter("statusCode", CollectionConstants.RECEIPT_STATUS_CODE_PENDING, StringType.INSTANCE);
        return query.list();
    }

    @Action(value = "/receipts/searchReceipt-search")
    public String search() {
        target = "searchresult";
        final StringBuilder searchQueryString = new StringBuilder("select distinct receipt ");
        final StringBuilder countQueryString = new StringBuilder("select count(distinct receipt) ");
        final String orderByString = " group by receipt.receiptdate,receipt.id  order by receipt.receiptdate desc";
        final String criteriaString = prepareQuery();
        final String searchQuery = searchQueryString.append(criteriaString).append(orderByString).toString();
        final String countQuery = countQueryString.append(criteriaString).toString();
        final Query searchReceiptQuery = getCurrentSession().createQuery(searchQuery);
        setQueryParameters(searchReceiptQuery);
        Page receiptPage = new Page<>(searchReceiptQuery, getPage(), getPageSize());
        final Query searchReceiptCountQuery = getCurrentSession().createQuery(countQuery);
        setQueryParameters(searchReceiptCountQuery);
        searchResult = new EgovPaginatedList(receiptPage, Integer.parseInt(searchReceiptCountQuery.list().get(0).toString()));
        ArrayList<ReceiptHeader> receiptList = new ArrayList<>(0);
        receiptList.addAll(searchResult.getList());
        searchResult.getList().clear();
        for (ReceiptHeader receiptHeader : receiptList) {
            if (receiptHeader.getState() != null && receiptHeader.getState().getOwnerPosition() != null) {
                List<Assignment> assignments = assignmentService.getAssignmentsForPosition(
                        receiptHeader.getState().getOwnerPosition().getId(), receiptHeader.getCreatedDate());
                if (!assignments.isEmpty())
                    receiptHeader.setWorkflowUserName(assignments.get(0).getEmployee().getUsername());
            }
            searchResult.getList().add(receiptHeader);
        }
        resultList = searchResult.getList();
        return SUCCESS;
    }

    private String prepareQuery() {
        final StringBuilder fromString = new StringBuilder(" from org.egov.collection.entity.ReceiptHeader receipt ");

        // Get only those receipts whose status is NOT PENDING
        final StringBuilder criteriaString = new StringBuilder(" where receipt.status.code !=:status ");

        if (StringUtils.isNotBlank(getInstrumentType())) {
            fromString.append(" inner join receipt.receiptInstrument as instruments ");
            criteriaString.append(" and instruments.instrumentType.type =:instrumentType ");
        }
        if (StringUtils.isNotBlank(getReceiptNumber()))
            criteriaString.append(" and upper(receiptNumber) like :receiptNo ");
        if (StringUtils.isNotBlank(getManualReceiptNumber()))
            criteriaString.append(" and upper(receipt.manualreceiptnumber) like :manualReceiptNo ");
        if (getSearchStatus() != -1)
            criteriaString.append(" and receipt.status.id =:statusId ");
        if (getFromDate() != null)
            criteriaString.append(" and receipt.receiptdate >= date(:fromDate)");
        if (getToDate() != null)
            criteriaString.append(" and receipt.receiptdate < date(:toDate)");
        if (getServiceTypeId() != -1)
            criteriaString.append(" and receipt.service.id =:serviceId ");

        if (!getServiceClass().equals("-1"))
            criteriaString.append(" and receipt.service.serviceType =:serviceType ");

        if (getUserId() != -1)
            criteriaString.append(" and receipt.createdBy.id =:userId ");
        if (getBranchId() != -1)
            criteriaString.append(" and receipt.receiptMisc.depositedBranch.id =:branchId ");
        return fromString.append(criteriaString).toString();
    }

    private void setQueryParameters(Query query) {
        query.setParameter("status", CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
        if (StringUtils.isNotBlank(getInstrumentType()))
            query.setParameter("instrumentType", getInstrumentType(), StringType.INSTANCE);
        if (StringUtils.isNotBlank(getReceiptNumber()))
            query.setParameter("receiptNo", "%" + getReceiptNumber().toUpperCase() + "%", StringType.INSTANCE);
        if (StringUtils.isNotBlank(getManualReceiptNumber()))
            query.setParameter("manualReceiptNo", "%" + getManualReceiptNumber().toUpperCase() + "%", StringType.INSTANCE);
        if (getSearchStatus() != -1)
            query.setParameter("statusId", getSearchStatus());
        if (getFromDate() != null)
            query.setParameter("fromDate", fromDate, TemporalType.DATE);
        if (getToDate() != null)
            query.setParameter("toDate", DateUtils.add(toDate, Calendar.DATE, 1), TemporalType.DATE);
        if (getServiceTypeId() != -1)
            query.setParameter("serviceId", Long.valueOf(getServiceTypeId()));
        if (!getServiceClass().equals("-1"))
            query.setParameter("serviceType", getServiceClass());
        if (getUserId() != -1)
            query.setParameter("userId", userId);
        if (getBranchId() != -1)
            query.setParameter("branchId", getBranchId());
    }
    

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(final Integer serviceType) {
        serviceTypeId = serviceType;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(final String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
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

    public String getTarget() {
        return target;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Integer getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(final Integer searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getManualReceiptNumber() {
        return manualReceiptNumber;
    }

    public void setManualReceiptNumber(final String manualReceiptNumber) {
        this.manualReceiptNumber = manualReceiptNumber;
    }

    public List<ReceiptHeader> getResultList() {
        return resultList;
    }

    public void setResultList(List<ReceiptHeader> resultList) {
        this.resultList = resultList;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public TreeMap<String, String> getServiceClassMap() {
        return serviceClassMap;
    }

    public void setServiceClassMap(TreeMap<String, String> serviceClassMap) {
        this.serviceClassMap = serviceClassMap;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public void setPage(final int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPage() {
        return pageNum;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PaginatedList getSearchResult() {
        return searchResult;
    }

}
