package org.egov.web.actions.bill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.voucher.VoucherSearchAction;

@ParentPackage("egov")
public class BillViewAction extends BaseFormAction
 {
	private static final Logger	LOGGER	= Logger.getLogger(VoucherSearchAction.class);
	private static final long serialVersionUID = 1L;
	private long billId;
	EgBillregister egBillRegister = new EgBillregister();
	List<Map<String,Object>> billDetailsList = new ArrayList<Map<String,Object>>();
	List<Map<String,Object>> subledgerList = new ArrayList<Map<String,Object>>();
	
	public List<Map<String, Object>> getSubledgerList() {
		return subledgerList;
	}

	public void setSubledgerList(List<Map<String, Object>> subledgerList) {
		this.subledgerList = subledgerList;
	}

	public List<Map<String, Object>> getBillDetailsList() {
		return billDetailsList;
	}

	public void setBillDetailsList(List<Map<String, Object>> billDetailsList) {
		this.billDetailsList = billDetailsList;
	}

	public EgBillregister getEgBillRegister() {
		return egBillRegister;
	}

	public void setEgBillRegister(EgBillregister egBillRegister) {
		this.egBillRegister = egBillRegister;
	}

	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	
	@Override
	public Object getModel() {
		return egBillRegister;
	}
	
	public void prepare()
	{
		super.prepare();
	}
	

@Action(value="/bill/billView-view")
	public String view() throws EGOVException,ParseException
	{
		loadBillDetails();
		return Constants.VIEW;
	}

	private void loadBillDetails() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------Start of loadBillDetails()-----------");
		Map<String,Object> temp = null;
		Map<String, Object> subLedgerTemp = null;
		if(egBillRegister.getEgBilldetailes()!= null && egBillRegister.getEgBilldetailes().size() != 0)
		{
			List<EgBilldetails> billDetList = persistenceService.findAllBy(" from EgBilldetails where egBillregister.id=? order by decode(debitamount,null,0, debitamount) desc ,decode(creditamount,null,0, creditamount) asc ", egBillRegister.getId());
			for(EgBilldetails billDetail:billDetList)
			{
				CChartOfAccounts coa =   (CChartOfAccounts) persistenceService.find(" from CChartOfAccounts where id=?  ", billDetail.getGlcodeid().longValue() ) ;
				temp = new HashMap<String, Object>();
				if(billDetail.getFunctionid() != null)
				{
					CFunction function = (CFunction)getPersistenceService().find("from CFunction where id=?", billDetail.getFunctionid().longValue());
					temp.put(Constants.FUNCTION, function.getName());
				}
				else
				{
					temp.put(Constants.FUNCTION, "");
				}
				
				temp.put("glcode", coa.getGlcode());
				temp.put("accountHead", coa.getName());
				temp.put("debitAmount",  billDetail.getDebitamount()==null?0:billDetail.getDebitamount().longValue());
				temp.put("creditAmount", billDetail.getCreditamount()==null?0:billDetail.getCreditamount().longValue());
				
				billDetailsList.add(temp);
				
				for(EgBillPayeedetails payeeDetails:billDetail.getEgBillPaydetailes())
				{
					Accountdetailtype detailtype = (Accountdetailtype) persistenceService.find(" from Accountdetailtype where id=?",payeeDetails.getAccountDetailTypeId());
					subLedgerTemp = new HashMap<String,Object>();
					try {
						subLedgerTemp = getAccountDetails(detailtype,payeeDetails.getAccountDetailKeyId(),subLedgerTemp);
					} catch (EGOVException e) {
						List<ValidationError> errors=new ArrayList<ValidationError>();
						errors.add(new ValidationError("exp",e.getMessage()));
						throw new ValidationException(errors);
					}
					subLedgerTemp.put(Constants.FUNCTION, temp.get(Constants.FUNCTION));
					subLedgerTemp.put("glcode", coa.getGlcode());
					if(payeeDetails.getDebitAmount() != null && payeeDetails.getDebitAmount().longValue()!=0)
					{
						subLedgerTemp.put("amount", payeeDetails.getDebitAmount().longValue());
					}
					else
					{
						subLedgerTemp.put("amount", payeeDetails.getCreditAmount().longValue());
					}
					subledgerList.add(subLedgerTemp);
				}
			}
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------End of loadBillDetails()-----------");
	}
	
	public Map<String, Object> getAccountDetails(final Accountdetailtype detailtype,final Integer detailkeyid,Map<String,Object> tempMap) throws EGOVException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------Start of getAccountDetails()-----------");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------detailtype::" + detailtype.getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------detailkeyid::" + detailkeyid);
		EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		EntityType entityType = common.getEntityType(detailtype,detailkeyid);
		tempMap.put(Constants.DETAILKEY,entityType.getName());
		tempMap.put(Constants.DETAILTYPE_NAME,detailtype.getName());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------End of loadBillDetails()-----------");
		return tempMap;
	}

	public void setBillId(long billId) {
		this.billId = billId;
		egBillRegister = (EgBillregister) persistenceService.find(" from EgBillregister where id = ?",billId);
	}

 }
