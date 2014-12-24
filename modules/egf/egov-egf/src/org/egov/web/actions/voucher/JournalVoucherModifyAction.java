/**
 * 
 */
package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.bills.EgBillregister;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;


/**
 * @author msahoo
 *
 */
public class JournalVoucherModifyAction  extends BaseVoucherAction{
	
	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(JournalVoucherModifyAction.class);
	private VoucherService voucherService;
	private List<VoucherDetails> billDetailslist;
	private List<VoucherDetails> subLedgerlist;
	private String voucherNumManual;
	private String target;
	private String saveMode;
	private VoucherTypeBean  voucherTypeBean;
	public static final String EXEPMSG = "Exception occured in voucher service while updating voucher ";
	@SuppressWarnings("unchecked")
	public String beforeModify(){
		LOGGER.debug("JournalVoucherModifyAction | loadvouchers | Start ");
		Map<String, Object> vhInfoMap = voucherService.getVoucherInfo(voucherHeader.getId());
		voucherHeader = (CVoucherHeader)vhInfoMap.get(Constants.VOUCHERHEADER);
		billDetailslist = (List<VoucherDetails>) vhInfoMap.get(Constants.GLDEATILLIST);
		subLedgerlist = (List<VoucherDetails>) vhInfoMap.get("subLedgerDetail");
		getBillInfo();
		loadSchemeSubscheme();
		loadFundSource();
		
		if(null != parameters.get("showMode") && parameters.get("showMode")[0].equalsIgnoreCase("view")){
			return "view";
		}
			
		return "showVoucher";
		
	}
	@ValidationErrorPage(value="showVoucher")
	public String saveAndPrint() throws Exception{
		try {
			saveMode = "saveprint";
			return updateVoucher();
		} catch (ValidationException e) {
			throw e;
		}
	}

@ValidationErrorPage(value="showVoucher")	
	@SuppressWarnings("deprecation")
	public String updateVoucher() {	
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			
			@Override
			public String execute(Connection conn) throws SQLException {
				LOGGER.debug("JournalVoucherModifyAction | updateVoucher | Start");
				target="";
				loadSchemeSubscheme();
				validateFields();
				if(null != voucherNumManual && StringUtils.isNotEmpty(voucherNumManual)){
					voucherHeader.setVoucherNumber(voucherNumManual); 
				}
				removeEmptyRowsAccoutDetail(billDetailslist);
				removeEmptyRowsSubledger(subLedgerlist);
				try {
					
					if(! validateData(billDetailslist,subLedgerlist)){
						voucherHeader = voucherService.updateVoucherHeader(voucherHeader,voucherTypeBean); 
						voucherService.deleteGLDetailByVHId(voucherHeader.getId());
						voucherService.deleteVDByVHId(voucherHeader.getId());
						List<Transaxtion> transactions = voucherService.postInTransaction(billDetailslist,subLedgerlist,
								voucherHeader );
						 ChartOfAccounts engine=ChartOfAccounts.getInstance();
						 Transaxtion txnList[]=new Transaxtion[transactions.size()];
						 txnList=(Transaxtion[])transactions.toArray(txnList);
						 SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						 
						 if(!engine.postTransaxtions(txnList, conn,formatter.format(voucherHeader.getVoucherDate())))
						 {
							 List<ValidationError> errors=new ArrayList<ValidationError>();
							 errors.add(new ValidationError("exp","Engine Validation failed"));
							 throw new ValidationException(errors);
						 }
						 else{
							 if(! "JVGeneral".equalsIgnoreCase(voucherHeader.getName())){
								 String totalamount = parameters.get("totaldbamount")[0];
								 LOGGER.debug("Journal Voucher Modify Action | Bill modify | voucher name = "+ voucherHeader.getName());
								 voucherService.updateBillForVSubType(billDetailslist,subLedgerlist,voucherHeader,voucherTypeBean,new BigDecimal(totalamount));
							 }
							 
							 target="success";
						 }
					}else if(subLedgerlist.size() ==0){
						subLedgerlist.add(new VoucherDetails());
						resetVoucherHeader();
					}else{
						resetVoucherHeader();
					}
				} catch (ValidationException e) {
					clearMessages();
					resetVoucherHeader();
					if(subLedgerlist.size() ==0){
						subLedgerlist.add(new VoucherDetails());
					}
					 List<ValidationError> errors=new ArrayList<ValidationError>();
					 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
					 throw new ValidationException(errors);
				} 
				catch (Exception e) {
					clearMessages();
					resetVoucherHeader();
					if(subLedgerlist.size() ==0){
						subLedgerlist.add(new VoucherDetails());
					}
					 List<ValidationError> errors=new ArrayList<ValidationError>();
					 errors.add(new ValidationError("exp",e.getMessage()));
					 throw new ValidationException(errors);
				}
				LOGGER.debug("JournalVoucherModifyAction | updateVoucher | End");
				return "showVoucher";
			}
		});
	}

	public void getBillInfo(){
		LOGGER.debug("JournalVoucherModify | getBillInfo | Start");
		EgBillregister billRegister =(EgBillregister) persistenceService.find("from EgBillregister br where br.egBillregistermis.voucherHeader.id="+voucherHeader.getId());
		/**
		 * If its not General JV.
		 */
		if(null != billRegister){
			voucherTypeBean.setPartyBillNum(billRegister.getEgBillregistermis().getPartyBillNumber());
			voucherTypeBean.setPartyName(billRegister.getEgBillregistermis().getPayto());
			voucherTypeBean.setPartyBillDate(billRegister.getEgBillregistermis().getPartyBillDate());
			voucherTypeBean.setBillNum(billRegister.getBillnumber());
			voucherTypeBean.setBillDate(billRegister.getBilldate());
			if(null == billRegister.getEgBillregistermis().getEgBillSubType()){
				voucherTypeBean.setVoucherSubType(billRegister.getExpendituretype());
			}else{
				voucherTypeBean.setVoucherSubType(billRegister.getEgBillregistermis().getEgBillSubType().getName());
			}
		}else{ // If its a General JV.
			voucherTypeBean.setVoucherSubType(voucherHeader.getName());
		}
		
		
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public List<VoucherDetails> getBillDetailslist() {
		return billDetailslist;
	}
	public VoucherTypeBean getVoucherTypeBean() {
		return voucherTypeBean;
	}

	public void setVoucherTypeBean(VoucherTypeBean voucherTypeBean) {
		this.voucherTypeBean = voucherTypeBean;
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

	public String getVoucherNumManual() {
		return voucherNumManual;
	}

	public void setVoucherNumManual(String voucherNumManual) {
		this.voucherNumManual = voucherNumManual;
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}
	
}
