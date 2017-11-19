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
package org.egov.services.budget;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.utils.BudgetReportEntry;
import org.egov.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public class BudgetAppropriationService extends PersistenceService {
	@Autowired
    private FinancialYearDAO financialYearDAO;
    private BudgetDetailsDAO budgetDetailsDAO;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    
    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    public BudgetAppropriationService() {
        super(null);
    }

    // private GenericDaoFactory genericDao;

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public List<Object> getBudgetDetailsForBill(final EgBillregister bill) {
        final List<Object> list = new ArrayList<Object>();
        if (bill != null && bill.getEgBillregistermis() != null && bill.getEgBillregistermis().getBudgetaryAppnumber() != null &&
                !"".equalsIgnoreCase(bill.getEgBillregistermis().getBudgetaryAppnumber())) {
            final CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(bill.getBilldate());
            for (final EgBilldetails detail : bill.getEgBilldetailes()) {
                final CChartOfAccounts coa = (CChartOfAccounts) find("from CChartOfAccounts where id=?",
                        Long.valueOf(detail.getGlcodeid().toString()));
                if (isBudgetCheckNeeded(coa)) {
                    final BudgetReportEntry budgetReportEntry = new BudgetReportEntry();
                    populateDepartmentForBill(bill, budgetReportEntry);
                    populateFundForBill(bill, budgetReportEntry);
                    populateDataForBill(bill, financialYear, detail, budgetReportEntry, coa);
                    list.add(budgetReportEntry);
                }
            }
        }
        return list;
    }

    public List<Object> getBudgetDetailsForVoucher(final CVoucherHeader voucher) {
        final List<Object> list = new ArrayList<Object>();
        if (voucher != null && voucher.getVouchermis().getBudgetaryAppnumber() != null
                && voucher.getVouchermis().getBudgetaryAppnumber() != null
                && !"".equalsIgnoreCase(voucher.getVouchermis().getBudgetaryAppnumber())) {
            final CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(voucher.getVoucherDate());
            final List<CGeneralLedger> ledgerDetails = findAllBy("from CGeneralLedger where voucherHeaderId.id=?",
                    voucher.getId());
            final Department dept = getDepartmentForVoucher(voucher);
            for (final CGeneralLedger detail : ledgerDetails)
                if (isBudgetCheckNeeded(detail.getGlcodeId())) {
                    final BudgetReportEntry budgetReportEntry = new BudgetReportEntry();
                    populateDataForVoucher(voucher, financialYear, detail, budgetReportEntry);
                    budgetReportEntry.setDepartmentName(dept.getName());
                    if (voucher.getFundId() != null)
                        budgetReportEntry.setFundName(voucher.getFundId().getName());
                    list.add(budgetReportEntry);
                }
        }
        return list;
    }

    private boolean isBudgetCheckNeeded(final CChartOfAccounts coa) {
    	 boolean checkReq=false; 
    	if(!budgetControlTypeService.getConfigValue().equals(BudgetControlType.BudgetCheckOption.NONE.toString()))
			if (null != coa && null != coa.getBudgetCheckReq() && coa.getBudgetCheckReq())
                checkReq = true;  
        return checkReq;
    }

    private void populateDataForBill(final EgBillregister bill, final CFinancialYear financialYear, final EgBilldetails detail,
            final BudgetReportEntry budgetReportEntry, final CChartOfAccounts coa) {
        final CFunction function = getFunction(detail);
        final Map<String, Object> budgetDataMap = getRequiredBudgetDataForBill(bill, financialYear, function, coa);
        budgetReportEntry.setFunctionName(function.getName());
        budgetReportEntry.setGlCode(coa.getGlcode());
        budgetReportEntry.setFinancialYear(financialYear.getFinYearRange());
        BigDecimal budgetedAmtForYear = BigDecimal.ZERO;
        try {
            budgetedAmtForYear = budgetDetailsDAO.getBudgetedAmtForYear(budgetDataMap);
        } catch (final ValidationException e) {
            throw e;
        }
        budgetReportEntry.setBudgetedAmtForYear(budgetedAmtForYear);
        populateBudgetAppNumber(bill, budgetReportEntry);
        final BigDecimal billAmount = getBillAmount(detail);
        BigDecimal soFarAppropriated = BigDecimal.ZERO;
        soFarAppropriated = getSoFarAppropriated(budgetDataMap, billAmount);
        budgetReportEntry.setAccountCode(coa.getGlcode());
        budgetReportEntry.setSoFarAppropriated(soFarAppropriated);
        final BigDecimal balance = budgetReportEntry.getBudgetedAmtForYear().subtract(budgetReportEntry.getSoFarAppropriated());
        budgetReportEntry.setBalance(balance);
        budgetReportEntry.setCumilativeIncludingCurrentBill(soFarAppropriated.add(billAmount));
        budgetReportEntry.setCurrentBalanceAvailable(balance.subtract(billAmount));
        budgetReportEntry.setCurrentBillAmount(billAmount);
    }

    private void populateDataForVoucher(final CVoucherHeader voucher, final CFinancialYear financialYear,
            final CGeneralLedger detail,
            final BudgetReportEntry budgetReportEntry) {
        final CFunction function = getFunctionForGl(detail);
        final CChartOfAccounts coa = detail.getGlcodeId();
        final Map<String, Object> budgetDataMap = getRequiredBudgetDataForVoucher(voucher, financialYear, function, coa);
        budgetReportEntry.setFunctionName(function.getName());
        budgetReportEntry.setGlCode(coa.getGlcode());
        budgetReportEntry.setFinancialYear(financialYear.getFinYearRange());
        BigDecimal budgetedAmtForYear = BigDecimal.ZERO;
        try {
            budgetedAmtForYear = budgetDetailsDAO.getBudgetedAmtForYear(budgetDataMap);
        } catch (final ValidationException e) {
            throw e;
        }
        budgetReportEntry.setBudgetedAmtForYear(budgetedAmtForYear);
        populateBudgetAppNumberForVoucher(voucher, budgetReportEntry);
        final BigDecimal billAmount = getVoucherAmount(detail);
        BigDecimal soFarAppropriated = BigDecimal.ZERO;
        soFarAppropriated = getSoFarAppropriated(budgetDataMap, billAmount);
        budgetReportEntry.setAccountCode(coa.getGlcode());
        budgetReportEntry.setSoFarAppropriated(soFarAppropriated);
        final BigDecimal balance = budgetReportEntry.getBudgetedAmtForYear().subtract(budgetReportEntry.getSoFarAppropriated());
        budgetReportEntry.setBalance(balance);
        budgetReportEntry.setCumilativeIncludingCurrentBill(soFarAppropriated.add(billAmount));
        budgetReportEntry.setCurrentBalanceAvailable(balance.subtract(billAmount));
        budgetReportEntry.setCurrentBillAmount(billAmount);
    }

    private BigDecimal getBillAmount(final EgBilldetails billDetail) {
        BigDecimal currentBillAmount = BigDecimal.ZERO;
        if (billDetail.getDebitamount() != null && billDetail.getDebitamount().compareTo(BigDecimal.ZERO) != 0)
            currentBillAmount = billDetail.getDebitamount();
        else
            currentBillAmount = billDetail.getCreditamount();
        return currentBillAmount;
    }

    private BigDecimal getVoucherAmount(final CGeneralLedger detail) {
        BigDecimal currentBillAmount = BigDecimal.ZERO;
        if (detail.getDebitAmount() != null && detail.getDebitAmount() != 0)
            currentBillAmount = new BigDecimal(detail.getDebitAmount());
        else
            currentBillAmount = new BigDecimal(detail.getCreditAmount());
        return currentBillAmount;
    }

    private void populateFundForBill(final EgBillregister bill, final BudgetReportEntry budgetReportEntry) {
        if (bill.getEgBillregistermis().getFund() != null)
            budgetReportEntry.setFundName(bill.getEgBillregistermis().getFund().getName());
    }

    private void populateDepartmentForBill(final EgBillregister bill, final BudgetReportEntry budgetReportEntry) {
        if (bill.getEgBillregistermis().getEgDepartment() != null)
            budgetReportEntry.setDepartmentName(bill.getEgBillregistermis().getEgDepartment().getName());
    }

    private Department getDepartmentForVoucher(final CVoucherHeader voucher) {
        if (voucher != null && voucher.getVouchermis().getDepartmentid() != null)
            return (Department) find("from Department where id=?", voucher.getVouchermis().getDepartmentid().getId());
        return null;
    }

    private CFunction getFunction(final EgBilldetails detail) {
        return (CFunction) find("from CFunction where id=?", Long.valueOf(detail.getFunctionid().toString()));
    }

    private CFunction getFunctionForGl(final CGeneralLedger detail) {
        if (detail.getFunctionId() != null)
            return (CFunction) find("from CFunction where id=?", Long.valueOf(detail.getFunctionId().toString()));
        return null;
    }

    private void populateBudgetAppNumber(final EgBillregister bill, final BudgetReportEntry budgetReportEntry) {
        if (!StringUtils.isBlank(bill.getEgBillregistermis().getBudgetaryAppnumber()))
            budgetReportEntry.setBudgetApprNumber(bill.getEgBillregistermis().getBudgetaryAppnumber());
        else if (bill.getEgBillregistermis().getVoucherHeader() != null
                && !StringUtils.isBlank(bill.getEgBillregistermis().getVoucherHeader().getVouchermis().getBudgetaryAppnumber()))
            budgetReportEntry.setBudgetApprNumber(bill.getEgBillregistermis().getVoucherHeader().getVouchermis()
                    .getBudgetaryAppnumber());
    }

    private void populateBudgetAppNumberForVoucher(final CVoucherHeader voucher, final BudgetReportEntry budgetReportEntry) {
        if (!StringUtils.isBlank(voucher.getVouchermis().getBudgetaryAppnumber()))
            budgetReportEntry.setBudgetApprNumber(voucher.getVouchermis().getBudgetaryAppnumber());
    }

    private Map<String, Object> getRequiredBudgetDataForBill(final EgBillregister cbill, final CFinancialYear financialYear,
            final CFunction function, final CChartOfAccounts coa) {
        final Map<String, Object> budgetDataMap = new HashMap<String, Object>();
        budgetDataMap.put("financialyearid", financialYear.getId());
        budgetDataMap.put(Constants.DEPTID, cbill.getEgBillregistermis().getEgDepartment().getId());
        if (cbill.getEgBillregistermis().getFunctionaryid() != null)
            budgetDataMap.put(Constants.FUNCTIONARYID, cbill.getEgBillregistermis().getFunctionaryid().getId());
        if (cbill.getEgBillregistermis().getScheme() != null)
            budgetDataMap.put(Constants.SCHEMEID, cbill.getEgBillregistermis().getScheme().getId());
        if (cbill.getEgBillregistermis().getSubScheme() != null)
            budgetDataMap.put(Constants.SUBSCHEMEID, cbill.getEgBillregistermis().getSubScheme().getId());
        budgetDataMap.put(Constants.FUNDID, cbill.getEgBillregistermis().getFund().getId());
        budgetDataMap.put(Constants.BOUNDARYID, cbill.getDivision());
        budgetDataMap.put(Constants.ASONDATE, cbill.getBilldate());
        budgetDataMap.put(Constants.FUNCTIONID, function.getId());
        budgetDataMap.put("fromdate", financialYear.getStartingDate());
        budgetDataMap.put("glcode", coa.getGlcode());
        budgetDataMap.put("glcodeid", coa.getId());
        budgetDataMap.put("budgetheadid", budgetDetailsDAO.getBudgetHeadByGlcode(coa));
        return budgetDataMap;
    }

    private Map<String, Object> getRequiredBudgetDataForVoucher(final CVoucherHeader voucher, final CFinancialYear financialYear,
            final CFunction function, final CChartOfAccounts coa) {
        final Map<String, Object> budgetDataMap = new HashMap<String, Object>();
        budgetDataMap.put("financialyearid", financialYear.getId());
        if (voucher.getVouchermis().getDepartmentid() != null)
            budgetDataMap.put(Constants.DEPTID, voucher.getVouchermis().getDepartmentid().getId());
        if (voucher.getVouchermis().getFunctionary() != null)
            budgetDataMap.put(Constants.FUNCTIONARYID, voucher.getVouchermis().getFunctionary().getId());
        if (voucher.getVouchermis().getSchemeid() != null)
            budgetDataMap.put(Constants.SCHEMEID, voucher.getVouchermis().getSchemeid().getId());
        if (voucher.getVouchermis().getSubschemeid() != null)
            budgetDataMap.put(Constants.SUBSCHEMEID, voucher.getVouchermis().getSubschemeid().getId());
        budgetDataMap.put(Constants.FUNDID, voucher.getFundId().getId());
        if (voucher.getVouchermis().getDivisionid() != null)
            budgetDataMap.put(Constants.BOUNDARYID, voucher.getVouchermis().getDivisionid().getBndryId());
        budgetDataMap.put(Constants.ASONDATE, voucher.getVoucherDate());
        if (function != null)
            budgetDataMap.put(Constants.FUNCTIONID, function.getId());
        budgetDataMap.put("fromdate", financialYear.getStartingDate());
        budgetDataMap.put("glcode", coa.getGlcode());
        budgetDataMap.put("glcodeid", coa.getId());
        budgetDataMap.put("budgetheadid", budgetDetailsDAO.getBudgetHeadByGlcode(coa));
        return budgetDataMap;
    }

    private BigDecimal getSoFarAppropriated(final Map<String, Object> budgetDataMap, final BigDecimal billAmount) {
        BigDecimal soFarAppropriated = BigDecimal.ZERO;
        BigDecimal actualAmtFromVoucher = budgetDetailsDAO.getActualBudgetUtilizedForBudgetaryCheck(budgetDataMap); // get actual
        // amount from
        // vouchers
        BigDecimal actualAmtFromBill = budgetDetailsDAO.getBillAmountForBudgetCheck(budgetDataMap);// get actual amount from bills
        if (actualAmtFromVoucher != null) {
            actualAmtFromVoucher = actualAmtFromVoucher == null ? BigDecimal.ZERO : actualAmtFromVoucher;
            soFarAppropriated = soFarAppropriated.add(actualAmtFromVoucher);
        }
        if (actualAmtFromBill != null) {
            actualAmtFromBill = actualAmtFromBill == null ? BigDecimal.ZERO : actualAmtFromBill;
            soFarAppropriated = soFarAppropriated.add(actualAmtFromBill).subtract(billAmount);
        }
        return soFarAppropriated;
    }

}
