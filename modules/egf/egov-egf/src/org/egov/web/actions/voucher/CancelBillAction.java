package org.egov.web.actions.voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.Session;

import com.exilant.eGov.src.domain.BillRegisterBean;


public class CancelBillAction extends BaseFormAction  {
	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(CancelBillAction.class);
	private String billNumber;
	private String fromDate;
	private String toDate;
	private Fund fund=new Fund();
	private String expType;
	private List<BillRegisterBean> billListDisplay= new ArrayList<BillRegisterBean>();
	private boolean afterSearch=false;
	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);

	
	@Override
	public Object getModel() {

		return null;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setFromDate(String fromBillDate) {
		this.fromDate = fromBillDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setToDate(String toBillDate) {
		this.toDate = toBillDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public Fund getFund() {
		return fund;
	}
	
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public String getExpType() {
		return expType;
	}
	public void  prepare()
	{
		super.prepare();
		//get this from master data cache
		addDropdownData("fundList", persistenceService.findAllBy("from Fund where isactive='1' and isnotleaf='0' order by name"));
		// Important - Remove the like part of the query below to generalize the bill cancellation screen 
		addDropdownData("expenditureList",persistenceService.findAllBy("select distinct bill.expendituretype from EgBillregister bill where bill.expendituretype like '"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"'   order by bill.expendituretype"));
	}
	public void prepareBeforeSearch()
	{
		fund.setId(null);
		billNumber="";
		fromDate="";
		toDate="";
		expType="";
		billListDisplay.clear();
	}
	@SkipValidation
	public String beforeSearch()
	{
		return "search" ;
	}
	public StringBuilder filterQuery()
	{
		StringBuilder query=new StringBuilder(" select bill.id, bill.billnumber, bill.billdate, bill.billamount  from EgBillregister bill, EgBillregistermis billmis   where billmis.egBillregister.id=bill.id ");
		if(fund!=null && fund.getId()!=null && fund.getId()!=-1 && fund.getId()!=0 )
		{
			query.append(" and billmis.fund.id="+fund.getId());
		}
		if(billNumber!=null && billNumber.length()!=0)
		{
			query.append(" and bill.billnumber ='"+billNumber+"'");
		}
		if(fromDate!=null && fromDate.length()!=0)
		{
			Date fDate;
			try {
				fDate = formatter.parse(fromDate);
				query.append(" and bill.billdate >= '"+Constants.DDMMYYYYFORMAT1.format(fDate)+"'");
			} catch (ParseException e) {
				LOGGER.error(" From Date parse error");
				//e.printStackTrace();
			}
		}
		if(toDate!=null && toDate.length()!=0)
		{
			Date tDate;
			try {
				tDate = formatter.parse(toDate);
				query.append(" and bill.billdate <= '"+Constants.DDMMYYYYFORMAT1.format(tDate)+"'");
			} catch (ParseException e) {
				LOGGER.error(" To Date parse error");
				//e.printStackTrace();
			}
		} 
		if(expType==null || expType.equalsIgnoreCase(""))
		{
			query.append(" and bill.expendituretype ='"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"'");
			query.append(" and bill.status.moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and bill.status.description='"
					+FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS+"'");
		}
		else
		{
			query.append(" and bill.expendituretype ='"+expType+"'");
			if(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equalsIgnoreCase(expType))
			{
				query.append(" and bill.status.moduletype='"+FinancialConstants.SALARYBILL+"' and bill.status.description='"
						+FinancialConstants.SALARYBILL_APPROVED_STATUS+"'");
			}else if(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(expType))
			{
				query.append(" and bill.status.moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and bill.status.description='"
						+FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS+"'");
				
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(expType))
			{
				query.append(" and bill.status.moduletype='"+FinancialConstants.SUPPLIERBILL+"' and bill.status.description='"
						+FinancialConstants.SUPPLIERBILL_PASSED_STATUS+"'");
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(expType))
			{
				query.append(" and bill.status.moduletype='"+FinancialConstants.CONTRACTORBILL+"' and bill.status.description='"
						+FinancialConstants.CONTRACTORBILL_PASSED_STATUS+"'");
			}
		}  
		
		return query;
		  
	}
	public String[] query()
	{
		String[] retQry= new String[2];
		StringBuilder filterQry= filterQuery();
		
		retQry[0]=filterQry+" and billmis.voucherHeader is null ";
		retQry[1]=filterQry+" and billmis.voucherHeader.status in ("+FinancialConstants.REVERSEDVOUCHERSTATUS+","+FinancialConstants.CANCELLEDVOUCHERSTATUS+") ";
		
		return retQry;
	}
	
	public void prepareSearch()
	{
		billListDisplay.clear();
	}
	public void validateFund()
	{
		if(fund==null || fund.getId()==-1)
			addFieldError("fund.id", getText("voucher.fund.mandatory"));
	}
	
	@ValidationErrorPage(value="search") 
	public String search()
	{
		validateFund();
		if(!hasFieldErrors())
		{
			billListDisplay.clear();
			String[] searchQuery=query();
			List<Object[]> tempBillList = new ArrayList<Object[]>();
			List<Object[]> billListWithNoVouchers,billListWithCancelledReversedVouchers;
			LOGGER.debug("Search Query - "+searchQuery);
			billListWithNoVouchers=persistenceService.findAllBy(searchQuery[0]);
			billListWithCancelledReversedVouchers=persistenceService.findAllBy(searchQuery[1]);
			tempBillList.addAll(billListWithNoVouchers);
			tempBillList.addAll(billListWithCancelledReversedVouchers);
			
			BillRegisterBean billRegstrBean;
			LOGGER.debug("Size of tempBillList - "+tempBillList.size());
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			for(Object[] bill:tempBillList)
			{
				billRegstrBean=new BillRegisterBean();
				billRegstrBean.setId(bill[0].toString());
				billRegstrBean.setBillNumber(bill[1].toString());
				if(!bill[2].toString().equalsIgnoreCase(""))
				{
					billRegstrBean.setBillDate(sdf.format(bill[2]));
				}
				billRegstrBean.setBillAmount(Double.parseDouble((bill[3].toString())));
				billListDisplay.add(billRegstrBean);
			}
			afterSearch=true;
		}
		return "search" ;
	}  
	public String cancelBill()
	{
		Long[] idList=new  Long[billListDisplay.size()];
		int i=0,idListLength=0;
		String idString="";
		StringBuilder statusQuery=new StringBuilder("from EgwStatus where ");
		StringBuilder cancelQuery=new StringBuilder("Update EgBillregister set " );
		for(BillRegisterBean billRgstrBean: billListDisplay)
		{
			if(billRgstrBean.getIsSelected())
			{
				idList[i++]=Long.parseLong(billRgstrBean.getId());
				idListLength++;
			}
		}
		if(expType==null || expType.equalsIgnoreCase(""))
		{
			statusQuery.append("moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and description='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"'");
			cancelQuery.append(" billstatus='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
		}
		else
		{
			if(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.SALARYBILL+"' and description='"+FinancialConstants.SALARYBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.SALARYBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
				
			}else if(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and description='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.SUPPLIERBILL+"' and description='"+FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.CONTRACTORBILL+"' and description='"+FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
			}
		}
		LOGGER.debug(" Status Query - "+statusQuery.toString());
		EgwStatus status=(EgwStatus) persistenceService.find(statusQuery.toString());
		Session session=persistenceService.getSession();
		
		if(idListLength!=0)
		{
				for(i=0;i<idListLength;i++)
				{
					idString+=idList[i]+((i==idListLength-1)?"":",");
				}
				
				cancelQuery.append(" where id in ("+idString+")");
				LOGGER.debug(" Cancel Query - "+cancelQuery.toString());
				Query query = session.createQuery(cancelQuery.toString());
				query.setLong("statusId", status.getId());
				int executeUpdate = query.executeUpdate();
		}
		addActionMessage("Bills Cancelled Successfully");
		prepareBeforeSearch();
		return "search";
	}
	public void setBillListDisplay(List<BillRegisterBean> billListDisplay) {
		this.billListDisplay = billListDisplay;
	}
	public List<BillRegisterBean> getBillListDisplay() {
		return billListDisplay;
	}
	public void setAfterSearch(boolean afterSearch) {
		this.afterSearch = afterSearch;
	}
	public boolean getAfterSearch() {
		return afterSearch;
	}
}
