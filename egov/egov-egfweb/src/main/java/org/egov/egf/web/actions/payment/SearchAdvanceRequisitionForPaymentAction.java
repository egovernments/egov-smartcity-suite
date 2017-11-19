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
package org.egov.egf.web.actions.payment;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.VoucherHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Results({
    @Result(name = "search", location = "searchAdvanceRequisitionForPayment-search.jsp")
})
public class SearchAdvanceRequisitionForPaymentAction extends SearchFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(SearchAdvanceRequisitionForPaymentAction.class);
    public static final String ARF_STATUS_APPROVED = "APPROVED";
    public static final String ARF_TYPE = "Contractor";
    private Date fromDate;
    private Date toDate;
    private Integer departmentId = -1;
    private String arfNumber;
    private VoucherHelper voucherHelper;
    private VoucherService voucherService;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
        if (departmentId == null || departmentId == -1)
            departmentId = voucherService.getCurrentDepartment().getId().intValue();
    }

    @Action(value = "/payment/searchAdvanceRequisitionForPayment-beforeSearch")
    public String beforeSearch() {
        return "search";
    }

    private Map getQuery() {
        final StringBuffer query = new StringBuffer(700);
        final List<Object> paramList = new ArrayList<Object>();
        final HashMap<String, Object> queryAndParams = new HashMap<String, Object>();
        query.append("from EgAdvanceRequisition arf where arf.arftype = ? and arf.status.code = ? and "
                +
                " NOT EXISTS (select 1 from CVoucherHeader vh where vh.id=arf.egAdvanceReqMises.voucherheader.id and arf.egAdvanceReqMises.voucherheader.status<>4) ");
        paramList.add(ARF_TYPE);
        paramList.add(ARF_STATUS_APPROVED);

        if (StringUtils.isNotBlank(arfNumber)) {
            query.append(" and UPPER(arf.advanceRequisitionNumber) like '%'||?||'%'");
            paramList.add(StringUtils.trim(arfNumber).toUpperCase());
        }

        if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            query.append(" and arf.advanceRequisitionDate between ? and ? ");
            paramList.add(fromDate);
            paramList.add(toDate);
        }

        if (departmentId != 0 && departmentId != -1) {
            query.append(" and arf.egAdvanceReqMises.egDepartment.id = ? ");
            paramList.add(departmentId);
        }
        // TODO - Order by Department and advanceRequisitionDate
        query.append(" order by arf.advanceRequisitionDate");

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
        countQuery = "select count(distinct arf.id) " + query;
        query = "select distinct arf " + query;
        return new SearchQueryHQL(query, countQuery, paramList);
    }

    @Override
    @Action(value = "/payment/searchAdvanceRequisitionForPayment-search")
    public String search() {
        return "search";
    }

    public String searchList() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        boolean isError = false;
        if (fromDate != null && toDate == null) {
            addFieldError("toDate", getText("search.toDate.null"));
            isError = true;
        }
        if (toDate != null && fromDate == null) {
            addFieldError("fromDate", getText("search.fromDate.null"));
            isError = true;
        }

        if (!DateUtils.compareDates(toDate, fromDate)) {
            addFieldError("toDate", getText("fromDate.greaterthan.toDate"));
            isError = true;
        }

        if (!DateUtils.compareDates(new Date(), toDate)) {
            addFieldError("toDate", getText("toDate.greaterthan.currentdate"));
            isError = true;
        }

        if (isError)
            return "search";

        setPageSize(30);
        super.search();

        return "search";
    }

    public String getArfNumber() {
        return arfNumber;
    }

    public void setArfNumber(final String arfNumber) {
        this.arfNumber = arfNumber;
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public void setVoucherHelper(final VoucherHelper voucherHelper) {
        this.voucherHelper = voucherHelper;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

}