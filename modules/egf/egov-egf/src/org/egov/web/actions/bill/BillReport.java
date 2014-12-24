/**
 * 
 */
package org.egov.web.actions.bill;

import java.util.Map;

import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.EgBillregister;
import org.egov.model.voucher.VoucherDetails;

import com.exilant.eGov.src.domain.EGBillPayeeDetails;

/**
 * @author mani
 *
 */
public class BillReport {
	
	private PersistenceService persistenceService;
	private DepartmentImpl department;
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public DepartmentImpl getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public VoucherDetails getVoucherDetails() {
		return voucherDetails;
	}
	public void setVoucherDetails(VoucherDetails voucherDetails) {
		this.voucherDetails = voucherDetails;
	}
	public EgBillregister getBill() {
		return bill;
	}
	public void setBill(EgBillregister bill) {
		this.bill = bill;
	}
	public EGBillPayeeDetails getBillPayeeDetails() {
		return billPayeeDetails;
	}
	public void setBillPayeeDetails(EGBillPayeeDetails billPayeeDetails) {
		this.billPayeeDetails = billPayeeDetails;
	}
	VoucherDetails voucherDetails;
	EgBillregister bill;
	EGBillPayeeDetails billPayeeDetails;
	Map<String,Object> budgetApprDetails;
	public Map<String,Object>  getBudgetApprDetails() {
		return budgetApprDetails;
	}
	public void setBudgetAppropriationdetails(Map<String,Object>  budgetAppropriationdetails) {
		this.budgetApprDetails = budgetAppropriationdetails;
	}
	/**
	 * @param persistenceService
	 * @param billDetails
	 * @param bill
	 */
	public BillReport(PersistenceService persistenceService, VoucherDetails voucherDetails, EgBillregister bill,Map<String,Object> budgetAppropriationdetails) {
		super();
		this.persistenceService = persistenceService;
		this.voucherDetails = voucherDetails;
		this.bill = bill;
		this.budgetApprDetails=budgetAppropriationdetails;
	}
	

}

 