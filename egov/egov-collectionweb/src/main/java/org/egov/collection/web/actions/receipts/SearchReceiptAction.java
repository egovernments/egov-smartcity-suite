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
package org.egov.collection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
        @Result(name = SearchReceiptAction.SUCCESS, location = "searchReceipt.jsp")
})
public class SearchReceiptAction extends SearchFormAction {

    private static final long serialVersionUID = 1L;
    private Integer serviceTypeId = -1;
    private Long userId = (long) -1;
    private String instrumentType;
    private String receiptNumber;
    private Date fromDate;
    private Date toDate;
    private Integer searchStatus = -1;
    private String target = "new";
    private String manualReceiptNumber;
    private List resultList;
    private String serviceClass = "-1";
    private TreeMap<String, String> serviceClassMap = new TreeMap<String, String>();
    private CollectionsUtil collectionsUtil;
    private Integer branchId;

    @Autowired
    private EisCommonService eisCommonService;

    @Override
    public Object getModel() {
        return null;
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

    @Action(value = "/receipts/searchReceipt-reset")
    public String reset() {
        setPage(1);
        serviceTypeId = -1;
        userId = (long) -1;
        receiptNumber = "";
        fromDate = null;
        toDate = null;
        instrumentType = "";
        searchStatus = -1;
        manualReceiptNumber = "";
        serviceClass = "-1";
        branchId = -1;
        return SUCCESS;
    }

    @Override
    public void prepare() {
        super.prepare();
        setupDropdownDataExcluding();
        addDropdownData("instrumentTypeList",
                getPersistenceService().findAllBy("from InstrumentType i where i.isActive = true order by type"));
        addDropdownData("userList",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS));
        serviceClassMap.putAll(CollectionConstants.SERVICE_TYPE_CLASSIFICATION);
        serviceClassMap.remove(CollectionConstants.SERVICE_TYPE_PAYMENT);
        addDropdownData("serviceTypeList", Collections.EMPTY_LIST);
        addDropdownData("bankBranchList", collectionsUtil.getBankCollectionBankBranchList());
    }

    @Override
    @Action(value = "/receipts/searchReceipt")
    public String execute() {
        return SUCCESS;
    }

    public List getReceiptStatuses() {
        return persistenceService.findAllBy(
                "from EgwStatus s where moduletype=? and code != ? order by description",
                ReceiptHeader.class.getSimpleName(), CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
    }

    @Override
    @Action(value = "/receipts/searchReceipt-search")
    public String search() {
        target = "searchresult";
        super.search();
        ArrayList<ReceiptHeader> receiptList = new ArrayList<ReceiptHeader>(0);
        receiptList.addAll(searchResult.getList());
        searchResult.getList().clear();
        if (getServiceClass() != "-1")
            addDropdownData("serviceTypeList",
                    getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE, getServiceClass()));

        for (ReceiptHeader receiptHeader : receiptList) {
            if (receiptHeader.getState() != null && receiptHeader.getState().getOwnerPosition() != null) {
                Long posId = receiptHeader.getState().getOwnerPosition().getId();
                receiptHeader.setWorkflowUserName(
                        eisCommonService.getUserForPosition(posId, receiptHeader.getCreatedDate()).getUsername());
            }
            searchResult.getList().add(receiptHeader);
        }
        resultList = searchResult.getList();
        return SUCCESS;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortDir) {
        final ArrayList<Object> params = new ArrayList<Object>(0);
        final StringBuilder searchQueryString = new StringBuilder("select distinct receipt ");
        final StringBuilder countQueryString = new StringBuilder("select count(distinct receipt) ");
        final StringBuilder fromString = new StringBuilder(" from org.egov.collection.entity.ReceiptHeader receipt ");
        final String orderByString = " group by receipt.receiptdate,receipt.id  order by receipt.receiptdate desc";

        // Get only those receipts whose status is NOT PENDING
        final StringBuilder criteriaString = new StringBuilder(" where receipt.status.code != ? ");
        params.add(CollectionConstants.RECEIPT_STATUS_CODE_PENDING);

        if (StringUtils.isNotBlank(getInstrumentType())) {
            fromString.append(" inner join receipt.receiptInstrument as instruments ");
            criteriaString.append(" and instruments.instrumentType.type = ? ");
            params.add(getInstrumentType());
        }

        if (StringUtils.isNotBlank(getReceiptNumber())) {
            criteriaString.append(" and upper(receiptNumber) like ? ");
            params.add("%" + getReceiptNumber().toUpperCase() + "%");
        }
        if (StringUtils.isNotBlank(getManualReceiptNumber())) {
            criteriaString.append(" and upper(receipt.manualreceiptnumber) like ? ");
            params.add("%" + getManualReceiptNumber().toUpperCase() + "%");
        }
        if (getSearchStatus() != -1) {
            criteriaString.append(" and receipt.status.id = ? ");
            params.add(getSearchStatus());
        }
        if (getFromDate() != null) {
            criteriaString.append(" and receipt.receiptdate >= ? ");
            params.add(fromDate);
        }
        if (getToDate() != null) {
            criteriaString.append(" and receipt.receiptdate < ? ");
            params.add(DateUtils.add(toDate, Calendar.DATE, 1));
        }
        if (getServiceTypeId() != -1) {
            criteriaString.append(" and receipt.service.id = ? ");
            params.add(Long.valueOf(getServiceTypeId()));
        }

        if (getServiceClass() != "-1") {
            criteriaString.append(" and receipt.service.serviceType = ? ");
            params.add(getServiceClass());
        }

        if (getUserId() != -1) {
            criteriaString.append(" and receipt.createdBy.id = ? ");
            params.add(userId);
        }
        if (getBranchId() != -1) {
            criteriaString.append(" and receipt.receiptMisc.depositedBranch.id = ? ");
            params.add(getBranchId());
        }

        final String searchQuery = searchQueryString.append(fromString).append(criteriaString).append(orderByString).toString();
        final String countQuery = countQueryString.append(fromString).append(criteriaString).toString();

        return new SearchQueryHQL(searchQuery, countQuery, params);
    }

    public Integer getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(final Integer searchStatus) {
        this.searchStatus = searchStatus;
    }

    public SearchQuery prepareQuery() {

        return null;
    }

    public String getManualReceiptNumber() {
        return manualReceiptNumber;
    }

    public void setManualReceiptNumber(final String manualReceiptNumber) {
        this.manualReceiptNumber = manualReceiptNumber;
    }

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
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

    /**
     * @param collectionsUtil the collectionsUtil to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
}
