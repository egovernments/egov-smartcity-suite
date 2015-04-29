package org.egov.works.models.contractorBill;

import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillregister;

public class StatutoryDeductionsForBill{
	private Long id;
	private EgPartytype subPartyType;
	private EgwTypeOfWork typeOfWork;
	private EgBillregister egBillReg;
	private EgBillPayeedetails egBillPayeeDtls;
	
	public EgPartytype getSubPartyType() {
		return subPartyType;
	}
	public void setSubPartyType(EgPartytype subPartyType) {
		this.subPartyType = subPartyType;
	}
	public EgwTypeOfWork getTypeOfWork() {
		return typeOfWork;
	}
	public void setTypeOfWork(EgwTypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	public EgBillregister getEgBillReg() {
		return egBillReg;
	}
	public void setEgBillReg(EgBillregister egBillReg) {
		this.egBillReg = egBillReg;
	}
	public EgBillPayeedetails getEgBillPayeeDtls() {
		return egBillPayeeDtls;
	}
	public void setEgBillPayeeDtls(EgBillPayeedetails egBillPayeeDtls) {
		this.egBillPayeeDtls = egBillPayeeDtls;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

}
