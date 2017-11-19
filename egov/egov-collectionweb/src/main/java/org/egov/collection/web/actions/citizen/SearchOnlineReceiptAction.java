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
package org.egov.collection.web.actions.citizen;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.models.ServiceDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = SearchOnlineReceiptAction.SUCCESS, location = "searchOnlineReceipt.jsp")
})
public class SearchOnlineReceiptAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private Long serviceTypeId = Long.valueOf(-1);
    private Long referenceId;
    private Date fromDate;
    private Date toDate;
    private Integer searchTransactionStatus = -1;
    private List<OnlinePayment> results = new ArrayList<OnlinePayment>(0);
    private String target = "new";

    @Override
    public Object getModel() {
        return null;
    }

    public SearchOnlineReceiptAction() {
        super();
        addRelatedEntity("serviceType", ServiceDetails.class, "name");
    }

    @Action(value = "/citizen/searchOnlineReceipt-reset")
    public String reset() {
        results = Collections.emptyList();
        serviceTypeId = Long.valueOf(-1);
        fromDate = null;
        toDate = null;
        searchTransactionStatus = -1;
        referenceId = null;
        return SUCCESS;
    }

    @Override
    public void prepare() {
        super.prepare();
        setupDropdownDataExcluding();
        addDropdownData("serviceTypeList", persistenceService.findAllByNamedQuery(
                CollectionConstants.QUERY_SERVICE_CATEGORY_FOR_TYPE, CollectionConstants.SERVICE_TYPE_BILLING, Boolean.TRUE));
    }

    @Override
    @Action(value = "/citizen/searchOnlineReceipt")
    public String execute() {
        return SUCCESS;
    }

    public List getOnlineReceiptStatuses() {
        return persistenceService.findAllByNamedQuery(
                CollectionConstants.QUERY_ALL_STATUSES_FOR_MODULE,
                OnlinePayment.class.getSimpleName());
    }

    public List getOnlineReceiptTransitionStatuses() {
        final List<String> statusCodes = new ArrayList<String>();
        statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS);
        statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED);
        statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED);
        return persistenceService.findAllByNamedQuery(
                CollectionConstants.QUERY_STATUSES_FOR_MODULE_AND_CODES,
                OnlinePayment.class.getSimpleName(), statusCodes);
    }

    @Action(value = "/citizen/searchOnlineReceipt-search")
    public String search() {
        final StringBuilder queryString = new StringBuilder(
                " select distinct onlinePayment from org.egov.collection.entity.OnlinePayment onlinePayment");
        final StringBuilder criteria = new StringBuilder();
        final StringBuilder joinString = new StringBuilder();
        final StringBuilder whereString = new StringBuilder();// " order by receipt.createdDate desc");
        final ArrayList<Object> params = new ArrayList<Object>();
        if (getReferenceId() != null) {
            criteria.append("onlinePayment.receiptHeader.id = ? ");
            params.add(getReferenceId());
        }
        if (getSearchTransactionStatus() != -1) {
            criteria.append(getJoinOperand(criteria)).append(" onlinePayment.status.id = ? ");
            params.add(getSearchTransactionStatus());
        }
        if (getFromDate() != null) {
            criteria.append(getJoinOperand(criteria)).append(" onlinePayment.createdDate >= ? ");
            params.add(fromDate);
        }
        if (getToDate() != null) {
            criteria.append(getJoinOperand(criteria)).append(" onlinePayment.createdDate <= ? ");
            final Calendar newTodate = Calendar.getInstance();
            newTodate.setTime(toDate);
            newTodate.add(Calendar.DATE, 1);
            params.add(newTodate.getTime());
        }
        if (getServiceTypeId() != -1) {
            criteria.append(getJoinOperand(criteria)).append(" onlinePayment.receiptHeader.service.serviceCategory.id = ? ");
            params.add(getServiceTypeId());
        }

        queryString.append(StringUtils.isBlank(joinString.toString()) ? "" : joinString);
        queryString.append(StringUtils.isBlank(criteria.toString()) ? "" : " where ").append(criteria);
        queryString.append(whereString);

        results = getPersistenceService().findAllBy(queryString.toString(), params.toArray());

        target = "searchresult";
        return SUCCESS;
    }

    private String getJoinOperand(final StringBuilder criteria) {
        return StringUtils.isBlank(criteria.toString()) ? "" : " and ";
    }

    public List<OnlinePayment> getResults() {
        return results;
    }

    public Integer getSearchTransactionStatus() {
        return searchTransactionStatus;
    }

    public void setSearchTransactionStatus(final Integer searchTransactionStatus) {
        this.searchTransactionStatus = searchTransactionStatus;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(final Long serviceType) {
        serviceTypeId = serviceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(final Long referenceId) {
        this.referenceId = referenceId;
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

}
