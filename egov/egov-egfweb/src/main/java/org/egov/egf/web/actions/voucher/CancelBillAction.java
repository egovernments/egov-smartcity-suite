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
package org.egov.egf.web.actions.voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.bills.EgBillRegisterService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.exilant.eGov.src.domain.BillRegisterBean;

@Results({ @Result(name = "search", location = "cancelBill-search.jsp") })
public class CancelBillAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger
            .getLogger(CancelBillAction.class);
    private String billNumber;
    private String fromDate;
    private String toDate;
    private Fund fund = new Fund();
    private Department deptImpl = new Department();
    private String expType;
    private transient List<BillRegisterBean> billListDisplay = new ArrayList<>();
    private boolean afterSearch = false;
    Integer loggedInUser = ApplicationThreadLocals.getUserId().intValue();
    public final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd/MM/yyyy", Constants.LOCALE);
    @Autowired
    @Qualifier("persistenceService")
    private transient PersistenceService persistenceService;
    @Autowired
    private transient DepartmentService departmentService;
    @Autowired
    private transient EgBillRegisterService egBillRegisterService;

    @Override
    public Object getModel() {

        return null;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setFromDate(final String fromBillDate) {
        fromDate = fromBillDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setToDate(final String toBillDate) {
        toDate = toBillDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Fund getFund() {
        return fund;
    }

    public void setExpType(final String expType) {
        this.expType = expType;
    }

    public String getExpType() {
        return expType;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside Prepare method");
        dropdownData.put("DepartmentList",departmentService.getAllDepartments());
        // get this from master data cache
        addDropdownData(
                "fundList",
                persistenceService
                        .findAllBy("from Fund where isactive=true and isnotleaf=false order by name"));
        // Important - Remove the like part of the query below to generalize the
        // bill cancellation screen
        addDropdownData("expenditureList",
                persistenceService
                        .findAllBy(
                                "select distinct bill.expendituretype from EgBillregister bill where bill.expendituretype like ?1 order by bill.expendituretype", FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
    }

    public void prepareBeforeSearch() {
        fund.setId(null);
        billNumber = "";
        fromDate = "";
        toDate = "";
        expType = "";
        billListDisplay.clear();
    }

    @SkipValidation
    @Action(value = "/voucher/cancelBill-beforeSearch")
    public String beforeSearch() {
        return "search";
    }

    public Map<String, Map<String, Object>> filterQuery() {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> params = new HashMap<>();
        final String userCond = " where ";
        final StringBuilder query = new StringBuilder(
                " select billmis.egBillregister.id, billmis.egBillregister.billnumber, billmis.egBillregister.billdate, billmis.egBillregister.billamount, billmis.egDepartment.name")
                .append("  from EgBillregistermis billmis ");
        // if the logged in user is same as creator or is superruser
        query.append(userCond);

        if (fund != null && fund.getId() != null && fund.getId() != -1
                && fund.getId() != 0) {
            query.append(" billmis.fund.id=:fundId");
            params.put("fundId", fund.getId());
        }

        if (billNumber != null && billNumber.length() != 0) {
            query.append(" and billmis.egBillregister.billnumber =:billNumber");
            params.put("billNumber", billNumber);
        }
        if (deptImpl != null && deptImpl.getId() != null
                && deptImpl.getId() != -1 && deptImpl.getId() != 0) {
            query.append(" and billmis.egDepartment.id =:deptId");
            params.put("deptId", deptImpl.getId());
        }
        if (fromDate != null && fromDate.length() != 0) {
            Date fDate;
            try {
                fDate = formatter.parse(fromDate);
                query.append(" and billmis.egBillregister.billdate >= :fromDate");
                params.put("fromDate", fDate);
            } catch (final ParseException e) {
                LOGGER.error(" From Date parse error");
                //
            }
        }
        if (toDate != null && toDate.length() != 0) {
            Date tDate;
            try {
                tDate = formatter.parse(toDate);
                query.append(" and billmis.egBillregister.billdate <= :toDate");
                params.put("toDate", tDate);
            } catch (final ParseException e) {
                LOGGER.error(" To Date parse error");
                //
            }
        }
        if (expType == null || expType.equalsIgnoreCase("")) {
            query.append(" and billmis.egBillregister.expendituretype =:expenditureType");
            params.put("expenditureType", FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
            query.append(" and billmis.egBillregister.status.moduletype=:moduleType");
            params.put("moduleType", FinancialConstants.CONTINGENCYBILL_FIN);
            query.append(" and billmis.egBillregister.status.description=:description");
            params.put("description", FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS);
        } else {
            query.append(" and billmis.egBillregister.expendituretype =:expenditureType");
            params.put("expenditureType", expType);
            if (FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY
                    .equalsIgnoreCase(expType)) {
                query.append(" and billmis.egBillregister.status.moduletype=:moduleType");
                params.put("moduleType", FinancialConstants.SALARYBILL);
                query.append(" and billmis.egBillregister.status.description=:description");
                params.put("description", FinancialConstants.SALARYBILL_APPROVED_STATUS);
            } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
                    .equalsIgnoreCase(expType)) {
                query.append(" and billmis.egBillregister.status.moduletype=:moduleType");
                params.put("moduleType", FinancialConstants.CONTINGENCYBILL_FIN);
                query.append(" and billmis.egBillregister.status.description=:description");
                params.put("description", FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS);
            } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE
                    .equalsIgnoreCase(expType)) {
                query.append(" and billmis.egBillregister.status.moduletype=:moduleType");
                params.put("moduleType", FinancialConstants.SUPPLIERBILL);
                query.append(" and billmis.egBillregister.status.description=:description");
                params.put("description", FinancialConstants.SUPPLIERBILL_PASSED_STATUS);
            } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS
                    .equalsIgnoreCase(expType)) {
                query.append(" and billmis.egBillregister.status.moduletype=:moduleType");
                params.put("moduleType", FinancialConstants.CONTRACTORBILL);
                query.append(" and billmis.egBillregister.status.description=:description");
                params.put("description", FinancialConstants.CONTRACTORBILL_PASSED_STATUS);
            }
        }
        queryMap.put(query.toString(), params);
        return queryMap;

    }

    public Map<String, Map<String, Object>> query() {
        final Map<String, Map<String, Object>> queries = new HashMap<>();
        final Map.Entry<String, Map<String, Object>> mapQueryEntry = filterQuery().entrySet().iterator().next();
        String query = mapQueryEntry.getKey() + " and billmis.voucherHeader is null ";
        queries.put(query, mapQueryEntry.getValue());
        final Map<String, Object> params = new HashMap<>();
        params.putAll(mapQueryEntry.getValue());
        query = mapQueryEntry.getKey() + " and billmis.voucherHeader.status in (:vhStatus)";
        params.put("vhStatus", Arrays.asList(FinancialConstants.REVERSEDVOUCHERSTATUS, FinancialConstants.CANCELLEDVOUCHERSTATUS));
        queries.put(query, params);
        return queries;
    }

    public void prepareSearch() {
        billListDisplay.clear();
    }

    public void validateFund() {
        if (fund == null || fund.getId() == -1)
            addFieldError("fund.id", getText("voucher.fund.mandatory"));
    }

    @ValidationErrorPage(value = "search")
    @Action(value = "/voucher/cancelBill-search")
    public String search() {
        validateFund();
        if (!hasFieldErrors()) {
            billListDisplay.clear();
            final Map<String, Map<String, Object>> queries = query();
            final List<String> list = queries.keySet().stream().collect(Collectors.toList());
            final List<Object[]> tempBillList = new ArrayList<Object[]>();
            List<Object[]> billListWithNoVouchers, billListWithCancelledReversedVouchers;
            final Query queryOne = persistenceService.getSession().createQuery(list.get(0));
            queries.get(list.get(0)).entrySet().forEach(entry -> queryOne.setParameter(entry.getKey(), entry.getValue()));
            billListWithNoVouchers = queryOne.list();
            final Query queryTwo = persistenceService.getSession().createQuery(list.get(1));
            queries.get(list.get(1)).entrySet().forEach(entry -> queryTwo.setParameter(entry.getKey(), entry.getValue()));
            billListWithCancelledReversedVouchers = queryTwo.list();

            tempBillList.addAll(billListWithNoVouchers);
            tempBillList.addAll(billListWithCancelledReversedVouchers);

            BillRegisterBean billRegstrBean;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Size of tempBillList - " + tempBillList.size());
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (final Object[] bill : tempBillList) {
                billRegstrBean = new BillRegisterBean();
                billRegstrBean.setId(bill[0].toString());
                billRegstrBean.setBillNumber(bill[1].toString());
                if (!bill[2].toString().equalsIgnoreCase(""))
                    billRegstrBean.setBillDate(sdf.format(bill[2]));
                billRegstrBean.setBillAmount(Double.parseDouble(bill[3]
                        .toString()));
                billRegstrBean.setBillDeptName(bill[4].toString());
                billListDisplay.add(billRegstrBean);
            }
            afterSearch = true;
        }
        return "search";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/cancelBill-cancelBill")
    public String cancelBill() {
        final Map<String, Object> map = egBillRegisterService.cancelBills(billListDisplay, expType);
        ((List<String>) map.get("billNumbers")).forEach(rec -> addActionError(getText("Cancellation Failure", new String[] {rec})));
        if (!((List<Long>) map.get("ids")).isEmpty())
            addActionMessage(getText("Cancelled Successfully"));

        prepareBeforeSearch();
        return "search";
    }

    public void setBillListDisplay(final List<BillRegisterBean> billListDisplay) {
        this.billListDisplay = billListDisplay;
    }

    public List<BillRegisterBean> getBillListDisplay() {
        return billListDisplay;
    }

    public void setAfterSearch(final boolean afterSearch) {
        this.afterSearch = afterSearch;
    }

    public boolean getAfterSearch() {
        return afterSearch;
    }

    public Department getDeptImpl() {
        return deptImpl;
    }

    public void setDeptImpl(final Department deptImpl) {
        this.deptImpl = deptImpl;
    }

    public Integer getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(final Integer loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}