package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
public class JournalVoucherAction extends BaseVoucherAction
{
	private static final Logger	LOGGER	= Logger.getLogger(JournalVoucherAction.class);
	private static final long serialVersionUID = 1L;
	private List<VoucherDetails> billDetailslist;
	private List<VoucherDetails> subLedgerlist;
	UserServiceImpl userServiceImpl;
	private String target;
	private VoucherService voucherService;
	private VoucherTypeBean voucherTypeBean;
	private String buttonValue;
	private String message = "";
	@SkipValidation
	public String newform()
	{
		billDetailslist = new ArrayList<VoucherDetails>();
		subLedgerlist = new ArrayList<VoucherDetails>();
		billDetailslist.add(new VoucherDetails());
		billDetailslist.add(new VoucherDetails());
		subLedgerlist.add(new VoucherDetails());
		LOGGER.debug("JournalVoucherAction | new | End");
		return NEW;
	}
	@Override
	public Object getModel() {
		voucherHeader=(CVoucherHeader)super.getModel();
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
		//voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL);
		return voucherHeader;
		
	};
	public String create() throws Exception{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			
			@Override
			public String execute(Connection conn) throws SQLException {
				LOGGER.debug("VoucherAction | create Method | Start");
				removeEmptyRowsAccoutDetail(billDetailslist);
				removeEmptyRowsSubledger(subLedgerlist);
				target="";
				// for manual voucher number.
				String voucherNumber  = voucherHeader.getVoucherNumber();
				LOGGER.debug("Bill details List size  : "+billDetailslist.size());
				LOGGER.debug("Sub ledger details List size  : "+subLedgerlist.size());
				loadSchemeSubscheme();
				validateFields();
				if(! validateData(billDetailslist,subLedgerlist)){
					try {
						voucherHeader = voucherService.postIntoVoucherHeader(voucherHeader,voucherTypeBean);
						voucherService.insertIntoRecordStatus(voucherHeader);
						 List<Transaxtion> transactions =voucherService.postInTransaction(billDetailslist,subLedgerlist,voucherHeader);
						 HibernateUtil.getCurrentSession().flush();
						 ChartOfAccounts engine=ChartOfAccounts.getInstance();
						 Transaxtion txnList[]=new Transaxtion[transactions.size()];
						 txnList=(Transaxtion[])transactions.toArray(txnList);						 
						 if(!engine.postTransaxtions(txnList, conn,Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())))
						 {
							 voucherHeader.setVoucherNumber(voucherNumber);
							 List<ValidationError> errors=new ArrayList<ValidationError>();
							 errors.add(new ValidationError("exp","Engine Validation failed"));
							 throw new ValidationException(errors);
						 }else {
							// budgetCheck();
							 if( ! "JVGeneral".equalsIgnoreCase(voucherTypeBean.getVoucherName())){
								 String totalamount = parameters.get("totaldbamount")[0];
								 LOGGER.debug(" Journal Voucher Action | Bill create | voucher name = "+ voucherTypeBean.getVoucherName());
								 voucherService.createBillForVoucherSubType(billDetailslist, subLedgerlist, voucherHeader, voucherTypeBean,new BigDecimal(totalamount));
							 }
							 message = "VoucherNumber  "+voucherHeader.getVoucherNumber()+"  created sucessfully ";
							 addActionMessage(getMessage());
							 target = "success";
							 billDetailslist.clear();
							 subLedgerlist.clear();
							 if(voucherHeader.getVouchermis().getBudgetaryAppnumber()!=null)
							 {
								 addActionMessage(getText("budget.recheck.sucessful",new String[]{voucherHeader.getVouchermis().getBudgetaryAppnumber()}));
							 }
							 LOGGER.debug("JournalVoucherAction | create  | Success | message === "+ message);
							 return newform();
						}
						
					} catch (ValidationException e) {
						clearMessages();
						 if(subLedgerlist.size() == 0){
							 subLedgerlist.add(new VoucherDetails());
						 }
						voucherHeader.setVoucherNumber(voucherNumber);
						 List<ValidationError> errors=new ArrayList<ValidationError>();
						 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
						 throw new ValidationException(errors);
					} 
					catch (Exception e) {
						clearMessages();
						if(subLedgerlist.size() == 0){
							subLedgerlist.add(new VoucherDetails());
						}
						voucherHeader.setVoucherNumber(voucherNumber);
						 List<ValidationError> errors=new ArrayList<ValidationError>();
						 errors.add(new ValidationError("exp",e.getMessage()));
						 throw new ValidationException(errors);
					}
					
				}else if(subLedgerlist.size() == 0){
					subLedgerlist.add(new VoucherDetails());
				}
				LOGGER.debug("VoucherAction | create Method | End");
				return NEW;
			}
		});
	}
	
	public List<VoucherDetails> getBillDetailslist() {
		return billDetailslist;
	}
	public void setBillDetailslist(List<VoucherDetails> billDetailslist) {
		this.billDetailslist = billDetailslist;
	}

	public List<VoucherDetails> getSubLedgerlist() {
		return subLedgerlist; 
	}

	public void setSubLedgerlist(List<VoucherDetails> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public VoucherTypeBean getVoucherTypeBean() {
		return voucherTypeBean;
	}
	public void setVoucherTypeBean(VoucherTypeBean voucherTypeBean) {
		this.voucherTypeBean = voucherTypeBean;
	}
	@ValidationErrorPage(value="new")
	public String saveAndView() throws Exception{
		try {
			buttonValue="view";
			return create();
		} catch (ValidationException e) {
			throw e;
		}
	}

	@ValidationErrorPage(value="new")
	public String saveAndPrint() throws Exception{
		try {
			buttonValue="print";
			return create();
		} catch (ValidationException e) {
			throw e;
		}
	}
	@ValidationErrorPage(value="new")
	public String saveAndNew() throws Exception{
		try {
			buttonValue="new";
			return create();
		} catch (ValidationException e) {
			throw e;
		}
	}
	@ValidationErrorPage(value="new")
	public String saveAndClose() throws Exception{
		buttonValue="close";
		return create();
	}
	public String getMessage() {
		return message;
	}
	public String getButtonValue() {
		return buttonValue;
	}
}
