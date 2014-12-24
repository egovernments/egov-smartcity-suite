package org.egov.web.actions.payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Bankaccount;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationException;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

@ParentPackage("egov")
public class ConcurrenceReportAction extends BaseFormAction{
	List<ConcurrenceReportData> paymentHeaderList = new ArrayList<ConcurrenceReportData>();
	private Date asOnDate = new Date();
	private BigDecimal currentReceiptsAmount = BigDecimal.ZERO;
	private Bankaccount bankAccount;
	private BigDecimal availableBalance = BigDecimal.ZERO;
	private BigDecimal bankBalance = BigDecimal.ZERO;
	private EgovCommon egovCommon;


	@Override
	public String execute() throws Exception {
		return "form";
	}
	
	@Override
	public void prepare() {
		super.prepare();
		if(!parameters.containsKey("skipPrepare")){
			addDropdownData("bankList", Collections.EMPTY_LIST);
			addDropdownData("accNumList", Collections.EMPTY_LIST);
		}
	}
	
	public String ajaxLoadPaymentHeader(){
		if(parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0]!=null){
			if(parameters.containsKey("asOnDate") && parameters.get("asOnDate")[0]!=null){
				try {
					setAsOnDate(Constants.DDMMYYYYFORMAT2.parse(parameters.get("asOnDate")[0]));
				} catch (ParseException e) {
					throw new ValidationException("Invalid date","Invalid date");
				}
			}
			if(parameters.containsKey("currentReceiptsAmount") && parameters.get("currentReceiptsAmount")[0]!=null){
				currentReceiptsAmount = new BigDecimal(parameters.get("currentReceiptsAmount")[0]);
			}
			Integer id = Integer.valueOf(parameters.get("bankAccount.id")[0]);
			bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",id);
			//filter out the vouchers for which cheques are already assigned
			String vouchersWithNewInstrumentsQuery =
				"select voucherheaderid from egf_instrumentvoucher eiv,egf_instrumentheader ih," + 
				" egw_status egws where eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New'";
			List<BigDecimal> results = persistenceService.getSession().createSQLQuery(vouchersWithNewInstrumentsQuery).list();
			results.add(BigDecimal.ZERO);
			Query query = persistenceService.getSession().createSQLQuery(getQueryString().toString())
							.addScalar("departmentName")
							.addScalar("functionCode")
							.addScalar("billNumber")
							.addScalar("billDate")
							.addScalar("bpvNumber")
							.addScalar("bpvDate")
							.addScalar("bpvAccountCode")
							.addScalar("amount")
							.setDate("date", asOnDate)
							.setResultTransformer(Transformers.aliasToBean(ConcurrenceReportData.class));
			paymentHeaderList.addAll(query.list());
			bankBalance = computeBankBalance(id);
		}
		return "results";
	}

	private BigDecimal computeBankBalance(Integer id) {
		return egovCommon.getAccountBalance(getAsOnDate(), id).add(egovCommon.getAmountForApprovedPaymentAndChequeNotAssigned(getAsOnDate(), id));
	}

	private StringBuffer getQueryString() {
		StringBuffer queryString = new StringBuffer(); 
		//query to fetch vouchers for which no cheque has been assigned
		queryString = queryString.append("select d.dept_name as departmentName,f.code as functionCode,ms.billnumber as billNumber, ")
		.append("to_char(ms.billdate,'dd/mm/yyyy') as billDate, vh.vouchernumber as bpvNumber, vh.voucherdate as bpvDate, gl.glcode as bpvAccountCode,")
		.append("ms.amount as amount from miscbilldetail ms, voucherheader vh,vouchermis vmis, eg_department d," +
				"generalledger gl left outer join function f on gl.functionid=f.id,paymentheader ph,eg_wf_states es,egf_instrumentvoucher iv right outer join voucherheader vh1 on " +
				"vh1.id =iv.VOUCHERHEADERID,egw_status egws where ph.voucherheaderid=vh.id and gl.debitamount!=0 and gl.debitamount is not null and vh.id= vmis.voucherheaderid and " +
				"vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and " +
				" ms.payvhid=vh.id and ph.voucherheaderid=vh.id and vh.voucherdate <= :date and ph.bankaccountnumberid="+bankAccount.getId()+
				" and  vh1.id=vh.id and vh.status=0 and iv.VOUCHERHEADERID is null group by ms.billnumber, d.dept_name,f.code,")
		.append(" ms.billdate,gl.glcode,vh.vouchernumber,vh.voucherdate, ms.amount ")
		.append(" union ")
		//query to fetch vouchers for which cheque has been assigned and surrendered
		.append(" select d.dept_name as departmentName,f.code as functionCode,ms.billnumber as billNumber, ") 
		.append("to_char(ms.billdate,'dd/mm/yyyy') as billDate, vh.vouchernumber as bpvNumber, vh.voucherdate as bpvDate, gl.glcode as bpvAccountCode,") 
		.append("ms.amount as amount from miscbilldetail ms,  egf_instrumentvoucher iv,voucherheader vh," + 
				"vouchermis vmis, eg_department d,generalledger gl left outer join function f on gl.functionid=f.id," +
				"paymentheader ph,eg_wf_states es, egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, " + 
				"(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid," + 
				"instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber " + 
				"and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and ms.payvhid=vh.id and vh.id= vmis.voucherheaderid and " + 
				"vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id " + 
				" and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and vh.status=0 and " + 
				"ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date " + 
				" and ph.bankaccountnumberid="+bankAccount.getId()+" and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"'");
		return queryString;
	}
	
	public List<ConcurrenceReportData> getPaymentHeaderList(){
		return paymentHeaderList;
	}

	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Bankaccount getBankAccount() {
		return bankAccount;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}
	public String getFormattedAsOnDate() {
		return Constants.DDMMYYYYFORMAT2.format(asOnDate);
	}

	public void setCurrentReceiptsAmount(BigDecimal currentReceiptsAmount) {
		this.currentReceiptsAmount = currentReceiptsAmount;
	}

	public BigDecimal getCurrentReceiptsAmount() {
		return currentReceiptsAmount;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public BigDecimal getBankBalance() {
		return bankBalance;
	}


}
