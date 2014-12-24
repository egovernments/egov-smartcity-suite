package org.egov.payroll.client.providentfund;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.struts.action.ActionForm;
import org.egov.payroll.model.providentfund.PFDetails;

/**
 * @author Ilayaraja P
 *
 */
public class PFSetupForm extends ActionForm
{
	private String id="";
	private String pfAccountId="";
	private String pfLoanAccountId="";
	private String pfIntExpAccountId="";
	
	private String pfAccount="";
	private String pfLoanAccount="";
	private String pfIntExpAccount="";
	
	private String pfAccountName="";
	private String pfLoanAccountName="";
	private String pfIntExpAccountName="";
	private String frequency="";
	
	private String[] dateFrom;
	private String[] dateTo;
	private String[] annualRateOfInterest;
	private String[] detailId;
	private String[] pfHeaderId;
	private String deletedRowsId="";
	
	
	public PFSetupForm()
	{}
	
	public PFSetupForm(List pfDetailList)
	{
		this.dateFrom = new String[pfDetailList.size()];
		this.dateTo = new String[pfDetailList.size()];
		this.annualRateOfInterest = new String[pfDetailList.size()];
		this.detailId = new String[pfDetailList.size()];
		this.pfHeaderId = new String[pfDetailList.size()];
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		for(int i=0;i<pfDetailList.size();i++)
		{
			PFDetails details = (PFDetails) pfDetailList.get(i);
			dateFrom[i] = sdf.format(details.getFromDate());
			if(details.getToDate()==null ){
				dateTo[i] = "";
			}
			else{
				dateTo[i] = sdf.format(details.getToDate());
			}
			annualRateOfInterest[i] = details.getAnnualRateOfInterest().toString();
			detailId[i] = details.getDetailId().toString();
			pfHeaderId[i] = details.getPfHeaderId().toString();
			
		}
	}
	
	public String getPfAccountId() {
		return pfAccountId;
	}
	public void setPfAccountId(String pfAccountId) {
		this.pfAccountId = pfAccountId;
	}
	public String getPfIntExpAccountId() {
		return pfIntExpAccountId;
	}
	public void setPfIntExpAccountId(String pfIntExpAccountId) {
		this.pfIntExpAccountId = pfIntExpAccountId;
	}
	public String getPfAccount() {
		return pfAccount;
	}
	public void setPfAccount(String pfAccount) {
		this.pfAccount = pfAccount;
	}
	public String getPfIntExpAccount() {
		return pfIntExpAccount;
	}
	public void setPfIntExpAccount(String pfIntExpAccount) {
		this.pfIntExpAccount = pfIntExpAccount;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String[] getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String[] dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String[] getDateTo() {
		return dateTo;
	}
	public void setDateTo(String[] dateTo) {
		this.dateTo = dateTo;
	}
	public String[] getAnnualRateOfInterest() {
		return annualRateOfInterest;
	}
	public void setAnnualRateOfInterest(String[] annualRateOfInterest) {
		this.annualRateOfInterest = annualRateOfInterest;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getPfAccountName() {
		return pfAccountName;
	}

	public void setPfAccountName(String pfAccountName) {
		this.pfAccountName = pfAccountName;
	}

	public String getPfIntExpAccountName() {
		return pfIntExpAccountName;
	}

	public void setPfIntExpAccountName(String pfIntExpAccountName) {
		this.pfIntExpAccountName = pfIntExpAccountName;
	}

	public String[] getDetailId() {
		return detailId;
	}

	public void setDetailId(String[] detailId) {
		this.detailId = detailId;
	}

	public String[] getPfHeaderId() {
		return pfHeaderId;
	}

	public void setPfHeaderId(String[] pfHeaderId) {
		this.pfHeaderId = pfHeaderId;
	}

	public String getDeletedRowsId() {
		return deletedRowsId;
	}

	public void setDeletedRowsId(String deletedRowsId) {
		this.deletedRowsId = deletedRowsId;
	}

	public String getPfLoanAccountId() {
		return pfLoanAccountId;
	}

	public void setPfLoanAccountId(String pfLoanAccountId) {
		this.pfLoanAccountId = pfLoanAccountId;
	}

	public String getPfLoanAccount() {
		return pfLoanAccount;
	}

	public void setPfLoanAccount(String pfLoanAccount) {
		this.pfLoanAccount = pfLoanAccount;
	}

	public String getPfLoanAccountName() {
		return pfLoanAccountName;
	}

	public void setPfLoanAccountName(String pfLoanAccountName) {
		this.pfLoanAccountName = pfLoanAccountName;
	}
	
	
}
