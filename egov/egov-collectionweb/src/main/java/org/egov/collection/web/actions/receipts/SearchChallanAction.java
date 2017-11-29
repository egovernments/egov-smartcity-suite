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

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({ @Result(name = SearchChallanAction.SUCCESS, location = "searchChallan.jsp") })
public class SearchChallanAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private Long serviceId = (long) -1;
    private Long serviceCategoryId = (long) -1;
    private Date fromDate;
    private Date toDate;
    private Integer status = -1;
    private Long departmentId = (long) -1;
    private String challanNumber;
    private List<ReceiptHeader> results = new ArrayList<ReceiptHeader>(0);
    private String target = "new";
    private final static String sourcePage = "search";

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        setupDropdownDataExcluding();
        addDropdownData("departmentList",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS));
        addDropdownData("serviceCategoryList", getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_SERVICE_CATEGORY));
        if (null != serviceCategoryId && serviceCategoryId != -1)
            addDropdownData("serviceList",  getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_CATEGORY_FOR_TYPE,serviceCategoryId,
                    CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION, Boolean.TRUE));
        else
            addDropdownData("serviceList",Collections.EMPTY_LIST);
        setFromDate(new Date());
        setToDate(new Date());
    }

    public SearchChallanAction() {
        super();
    }

    @Action(value = "/receipts/searchChallan-reset")
    public String reset() {
        departmentId = (long) -1;
        results = null;
        serviceId = (long) -1;
        serviceCategoryId = (long) -1;
        challanNumber = "";
        fromDate = null;
        toDate = null;
        status = -1;
        return SUCCESS;
    }

    public List getChallanStatuses() {
        return persistenceService
                .findAllBy("select distinct s from ReceiptHeader receipt inner join receipt.challan.status s");
    }

    @Action(value = "/receipts/searchChallan-search")
    public String search() {
        final StringBuilder queryString = new StringBuilder(
                " select distinct receipt from org.egov.collection.entity.ReceiptHeader receipt");
        final StringBuilder criteria = new StringBuilder();
        final StringBuilder joinString = new StringBuilder();
        final StringBuilder whereString = new StringBuilder(" order by receipt.createdDate desc");
        final ArrayList<Object> params = new ArrayList<Object>(0);
        if (StringUtils.isNotBlank(getChallanNumber())) {
            criteria.append(" upper(receipt.challan.challanNumber) like ? ");
            params.add("%" + getChallanNumber().toUpperCase() + "%");
        }
        if (getDepartmentId() != -1) {
            criteria.append(getJoinOperand(criteria)).append(" receipt.receiptMisc.department.id = ? ");
            params.add(getDepartmentId());
        }
        if (getStatus() != -1) {
            criteria.append(getJoinOperand(criteria)).append(" receipt.challan.status.id = ? ");
            params.add(getStatus());
        }
        if (getFromDate() != null) {
            criteria.append(getJoinOperand(criteria)).append(" receipt.challan.challanDate >= ? ");
            params.add(fromDate);
        }
        if (getToDate() != null) {
            criteria.append(getJoinOperand(criteria)).append(" receipt.challan.challanDate < ? ");
            params.add(DateUtils.add(toDate, Calendar.DATE, 1));
        }
        if (getServiceId() != -1) {
            criteria.append(getJoinOperand(criteria)).append(" receipt.service.id = ? ");
            params.add(getServiceId());
        }
        if (getServiceCategoryId() != -1) {
            criteria.append(getJoinOperand(criteria)).append(" receipt.service.serviceCategory.id = ? ");
            params.add(getServiceCategoryId());
        }
        criteria.append(getJoinOperand(criteria)).append(" receipt.receipttype = ? ");
        params.add(CollectionConstants.RECEIPT_TYPE_CHALLAN);

        queryString.append(StringUtils.isBlank(joinString.toString()) ? "" : joinString);
        queryString.append(StringUtils.isBlank(criteria.toString()) ? "" : " where ").append(criteria);
        queryString.append(whereString);
        results = getPersistenceService().findAllBy(queryString.toString(), params.toArray());
        if (results.size() > 500) {
            results.clear();
            throw new ValidationException(Arrays.asList(new ValidationError("searchchallan.changecriteria",
                    "More than 500 results found.Please add more search criteria")));

        }
        target = "searchresult";
        return SUCCESS;
    }

    private String getJoinOperand(final StringBuilder criteria) {
        return StringUtils.isBlank(criteria.toString()) ? "" : " and ";
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(final Long serviceId) {
        this.serviceId = serviceId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(final String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getTarget() {
        return target;
    }

    public List<ReceiptHeader> getResults() {
        return results;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(final Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }
}
