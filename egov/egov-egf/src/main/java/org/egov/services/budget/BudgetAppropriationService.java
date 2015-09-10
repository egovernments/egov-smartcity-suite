/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.services.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
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
@Transactional(readOnly=true)
public class BudgetAppropriationService extends PersistenceService{
	private FinancialYearDAO financialYearDAO;
	private BudgetDetailsDAO budgetDetailsDAO;
	@Autowired  private AppConfigValueService appConfigValuesService;
	//private GenericDaoFactory genericDao;
	
	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}
	
	public List<Object> getBudgetDetailsForBill(EgBillregister bill) {
		List<Object> list = new ArrayList<Object>();
		if(bill!=null && bill.getEgBillregistermis()!=null && bill.getEgBillregistermis().getBudgetaryAppnumber()!=null && 
				!"".equalsIgnoreCase(bill.getEgBillregistermis().getBudgetaryAppnumber())){
			CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(bill.getBilldate());
			for (EgBilldetails detail : bill.getEgBilldetailes()) {
				CChartOfAccounts coa =(CChartOfAccounts) find("from CChartOfAccounts where id=?",Long.valueOf(detail.getGlcodeid().toString()));
				if(isBudgetCheckNeeded(coa)){
					BudgetReportEntry budgetReportEntry = new BudgetReportEntry();
					populateDepartmentForBill(bill, budgetReportEntry);
					populateFundForBill(bill, budgetReportEntry);
					populateDataForBill(bill, financialYear, detail, budgetReportEntry,coa);
					list.add(budgetReportEntry);
				}
			}
		}
		return list;
	}

	public List<Object> getBudgetDetailsForVoucher(CVoucherHeader voucher) {
		List<Object> list = new ArrayList<Object>();
		if(voucher!=null && voucher.getVouchermis().getBudgetaryAppnumber()!=null && voucher.getVouchermis().getBudgetaryAppnumber()!=null 
				&& !"".equalsIgnoreCase(voucher.getVouchermis().getBudgetaryAppnumber())){
			CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(voucher.getVoucherDate());
			List<CGeneralLedger> ledgerDetails = findAllBy("from CGeneralLedger where voucherHeaderId.id=?",voucher.getId());
			Department dept = getDepartmentForVoucher(voucher);
			for (CGeneralLedger detail : ledgerDetails) {
				if(isBudgetCheckNeeded(detail.getGlcodeId())){
					BudgetReportEntry budgetReportEntry = new BudgetReportEntry();
					populateDataForVoucher(voucher, financialYear, detail, budgetReportEntry);
					budgetReportEntry.setDepartmentName(dept.getName());
					if(voucher.getFundId()!=null)
						budgetReportEntry.setFundName(voucher.getFundId().getName());
					list.add(budgetReportEntry);
				}
			}
		}
		return list;
	}
	
	private boolean isBudgetCheckNeeded(CChartOfAccounts coa){
    	List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey("EGF","budgetCheckRequired");
    	boolean checkReq=false;
    	if("Y".equalsIgnoreCase(((AppConfigValues)list.get(0)).getValue())){
			if(null!=coa &&  null!= coa.getBudgetCheckReq() &&  coa.getBudgetCheckReq()){	
				checkReq=true;
			}
		}
    	return checkReq;
	}

	private void populateDataForBill(EgBillregister bill,CFinancialYear financialYear, EgBilldetails detail,BudgetReportEntry budgetReportEntry, CChartOfAccounts coa) {
		CFunction function = getFunction(detail);
		Map<String,Object> budgetDataMap = getRequiredBudgetDataForBill(bill,financialYear,function,coa);
		budgetReportEntry.setFunctionName(function.getName());
		budgetReportEntry.setGlCode(coa.getGlcode());
		budgetReportEntry.setFinancialYear(financialYear.getFinYearRange()); 
		BigDecimal budgetedAmtForYear = BigDecimal.ZERO;
		try {
			budgetedAmtForYear = budgetDetailsDAO.getBudgetedAmtForYear(budgetDataMap);
		} catch (ValidationException e) {
			throw e;
		}
		budgetReportEntry.setBudgetedAmtForYear(budgetedAmtForYear);
		populateBudgetAppNumber(bill, budgetReportEntry);
		BigDecimal billAmount = getBillAmount(detail);
		BigDecimal soFarAppropriated = BigDecimal.ZERO;
		soFarAppropriated = getSoFarAppropriated(budgetDataMap,billAmount);
		budgetReportEntry.setAccountCode(coa.getGlcode());
		budgetReportEntry.setSoFarAppropriated(soFarAppropriated);
		BigDecimal balance = budgetReportEntry.getBudgetedAmtForYear().subtract(budgetReportEntry.getSoFarAppropriated());
		budgetReportEntry.setBalance(balance);
		budgetReportEntry.setCumilativeIncludingCurrentBill(soFarAppropriated.add(billAmount));
		budgetReportEntry.setCurrentBalanceAvailable(balance.subtract(billAmount));
		budgetReportEntry.setCurrentBillAmount(billAmount);
	}
	
	private void populateDataForVoucher(CVoucherHeader voucher,CFinancialYear financialYear, CGeneralLedger detail,BudgetReportEntry budgetReportEntry) {
		CFunction function = getFunctionForGl(detail);
		CChartOfAccounts coa = detail.getGlcodeId();
		Map<String,Object> budgetDataMap = getRequiredBudgetDataForVoucher(voucher,financialYear,function,coa);
		budgetReportEntry.setFunctionName(function.getName());
		budgetReportEntry.setGlCode(coa.getGlcode());
		budgetReportEntry.setFinancialYear(financialYear.getFinYearRange()); 
		BigDecimal budgetedAmtForYear = BigDecimal.ZERO;
		try {
			budgetedAmtForYear = budgetDetailsDAO.getBudgetedAmtForYear(budgetDataMap);
		} catch (ValidationException e) {
			throw e;
		}
		budgetReportEntry.setBudgetedAmtForYear(budgetedAmtForYear);
		populateBudgetAppNumberForVoucher(voucher, budgetReportEntry);
		BigDecimal billAmount = getVoucherAmount(detail);
		BigDecimal soFarAppropriated = BigDecimal.ZERO;
		soFarAppropriated = getSoFarAppropriated(budgetDataMap,billAmount);
		budgetReportEntry.setAccountCode(coa.getGlcode());
		budgetReportEntry.setSoFarAppropriated(soFarAppropriated);
		BigDecimal balance = budgetReportEntry.getBudgetedAmtForYear().subtract(budgetReportEntry.getSoFarAppropriated());
		budgetReportEntry.setBalance(balance);
		budgetReportEntry.setCumilativeIncludingCurrentBill(soFarAppropriated.add(billAmount));
		budgetReportEntry.setCurrentBalanceAvailable(balance.subtract(billAmount));
		budgetReportEntry.setCurrentBillAmount(billAmount);
	}

	private BigDecimal getBillAmount(EgBilldetails billDetail) {
		BigDecimal currentBillAmount = BigDecimal.ZERO;
		if(billDetail.getDebitamount()!=null && billDetail.getDebitamount().compareTo(BigDecimal.ZERO)!=0){
			currentBillAmount=billDetail.getDebitamount();
		}else{
			currentBillAmount=billDetail.getCreditamount();
		}
		return currentBillAmount;
	}
	
	private BigDecimal getVoucherAmount(CGeneralLedger detail) {
		BigDecimal currentBillAmount = BigDecimal.ZERO;
		if(detail.getDebitAmount()!=null && detail.getDebitAmount()!=0){
			currentBillAmount=new BigDecimal(detail.getDebitAmount());
		}else{
			currentBillAmount=new BigDecimal(detail.getCreditAmount());
		}
		return currentBillAmount;
	}

	private void populateFundForBill(EgBillregister bill,BudgetReportEntry budgetReportEntry) {
		if(bill.getEgBillregistermis().getFund()!=null)
			budgetReportEntry.setFundName(bill.getEgBillregistermis().getFund().getName());
	}
	
	private void populateDepartmentForBill(EgBillregister bill,BudgetReportEntry budgetReportEntry) {
		if(bill.getEgBillregistermis().getEgDepartment()!=null)
			budgetReportEntry.setDepartmentName(bill.getEgBillregistermis().getEgDepartment().getName());
	}
	
	private Department getDepartmentForVoucher(CVoucherHeader voucher) {
		if(voucher!=null && voucher.getVouchermis().getDepartmentid()!=null)
			return (Department) find("from Department where id=?",voucher.getVouchermis().getDepartmentid().getId());
		return null;
	}

	private CFunction getFunction(EgBilldetails detail) {
		return (CFunction) find("from CFunction where id=?",Long.valueOf(detail.getFunctionid().toString()));
	}

	private CFunction getFunctionForGl(CGeneralLedger detail) {
		if(detail.getFunctionId()!=null)
			return (CFunction) find("from CFunction where id=?",Long.valueOf(detail.getFunctionId().toString()));
		return null;
	}

	private void populateBudgetAppNumber(EgBillregister bill,BudgetReportEntry budgetReportEntry) {
		if(!StringUtils.isBlank(bill.getEgBillregistermis().getBudgetaryAppnumber()))
			budgetReportEntry.setBudgetApprNumber(bill.getEgBillregistermis().getBudgetaryAppnumber());
		else if(bill.getEgBillregistermis().getVoucherHeader()!=null && !StringUtils.isBlank(bill.getEgBillregistermis().getVoucherHeader().getVouchermis().getBudgetaryAppnumber()))
			budgetReportEntry.setBudgetApprNumber(bill.getEgBillregistermis().getVoucherHeader().getVouchermis().getBudgetaryAppnumber());
	}
	
	private void populateBudgetAppNumberForVoucher(CVoucherHeader voucher,BudgetReportEntry budgetReportEntry) {
		if(!StringUtils.isBlank(voucher.getVouchermis().getBudgetaryAppnumber()))
			budgetReportEntry.setBudgetApprNumber(voucher.getVouchermis().getBudgetaryAppnumber());
	}

	private Map<String, Object> getRequiredBudgetDataForBill(EgBillregister cbill,CFinancialYear financialYear,CFunction function,CChartOfAccounts coa)  {
		Map<String,Object> budgetDataMap = new HashMap<String, Object>();
		budgetDataMap.put("financialyearid", financialYear.getId());
		budgetDataMap.put(Constants.DEPTID,cbill.getEgBillregistermis().getEgDepartment().getId());
		if(cbill.getEgBillregistermis().getFunctionaryid()!=null)
			budgetDataMap.put(Constants.FUNCTIONARYID,cbill.getEgBillregistermis().getFunctionaryid().getId());
		if(cbill.getEgBillregistermis().getScheme()!=null)
			budgetDataMap.put(Constants.SCHEMEID,cbill.getEgBillregistermis().getScheme().getId());
		if(cbill.getEgBillregistermis().getSubScheme()!=null)
			budgetDataMap.put(Constants.SUBSCHEMEID,cbill.getEgBillregistermis().getSubScheme().getId());
		budgetDataMap.put(Constants.FUNDID,cbill.getEgBillregistermis().getFund().getId());
		budgetDataMap.put(Constants.BOUNDARYID,cbill.getDivision());
		budgetDataMap.put(Constants.ASONDATE, cbill.getBilldate());
		budgetDataMap.put(Constants.FUNCTIONID,function.getId());
		budgetDataMap.put("fromdate", financialYear.getStartingDate());  
		budgetDataMap.put("glcode", coa.getGlcode());
		budgetDataMap.put("glcodeid", coa.getId());  
		budgetDataMap.put("budgetheadid",budgetDetailsDAO.getBudgetHeadByGlcode(coa));
		return budgetDataMap;
	}

	private Map<String, Object> getRequiredBudgetDataForVoucher(CVoucherHeader voucher,CFinancialYear financialYear,CFunction function,CChartOfAccounts coa)  {
		Map<String,Object> budgetDataMap = new HashMap<String, Object>();
		budgetDataMap.put("financialyearid", financialYear.getId());
		if(voucher.getVouchermis().getDepartmentid()!=null)
			budgetDataMap.put(Constants.DEPTID,voucher.getVouchermis().getDepartmentid().getId());
		if(voucher.getVouchermis().getFunctionary()!=null)
			budgetDataMap.put(Constants.FUNCTIONARYID,voucher.getVouchermis().getFunctionary().getId());
		if(voucher.getVouchermis().getSchemeid()!=null)
			budgetDataMap.put(Constants.SCHEMEID,voucher.getVouchermis().getSchemeid().getId());
		if(voucher.getVouchermis().getSubschemeid()!=null)
			budgetDataMap.put(Constants.SUBSCHEMEID,voucher.getVouchermis().getSubschemeid().getId());
		budgetDataMap.put(Constants.FUNDID,voucher.getFundId().getId());
		if(voucher.getVouchermis().getDivisionid()!=null)
			budgetDataMap.put(Constants.BOUNDARYID,voucher.getVouchermis().getDivisionid().getBndryId());
		budgetDataMap.put(Constants.ASONDATE, voucher.getVoucherDate());
		if(function!=null)
			budgetDataMap.put(Constants.FUNCTIONID,function.getId());
		budgetDataMap.put("fromdate", financialYear.getStartingDate());  
		budgetDataMap.put("glcode", coa.getGlcode());
		budgetDataMap.put("glcodeid", coa.getId());  
		budgetDataMap.put("budgetheadid",budgetDetailsDAO.getBudgetHeadByGlcode(coa));
		return budgetDataMap;
	}

	private BigDecimal getSoFarAppropriated(Map<String,Object> budgetDataMap,BigDecimal billAmount) {
		BigDecimal soFarAppropriated = BigDecimal.ZERO;
		BigDecimal actualAmtFromVoucher = budgetDetailsDAO.getActualBudgetUtilizedForBudgetaryCheck(budgetDataMap); // get actual amount from vouchers
		BigDecimal actualAmtFromBill = budgetDetailsDAO.getBillAmountForBudgetCheck(budgetDataMap);// get actual amount from bills
		if(actualAmtFromVoucher!=null){
			actualAmtFromVoucher = actualAmtFromVoucher==null?BigDecimal.ZERO:actualAmtFromVoucher;
			soFarAppropriated=soFarAppropriated.add(actualAmtFromVoucher);
		}
		if(actualAmtFromBill!=null){
			actualAmtFromBill = actualAmtFromBill==null?BigDecimal.ZERO:actualAmtFromBill;
			soFarAppropriated=soFarAppropriated.add(actualAmtFromBill).subtract(billAmount);
		}
		return soFarAppropriated;
	}

}

