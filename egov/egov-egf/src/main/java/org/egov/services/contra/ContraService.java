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
package org.egov.services.contra;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.VoucherDetail;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.contra.ContraBean;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.voucher.PayInBean;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import com.exilant.GLEngine.Transaxtion;

/**
 * 
 * @author msahoo
 *
 */

public class ContraService extends PersistenceService<ContraJournalVoucher, Long>
{
	private static final Logger	LOGGER	= Logger.getLogger(ContraService.class);
	private  PersistenceService persistenceService;
	private PersistenceService<ContraJournalVoucher, Long> contrajournalService;
	private PersistenceService<Bankreconciliation, Integer> bankReconService;
	private PersistenceService<VoucherDetail, Long> vdPersitSer;
	
	private CommonsService comm;
	private InstrumentService instrumentService;
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	protected EisCommonService eisCommonService;
	private GenericHibernateDaoFactory genericDao;
	private EisUtilService eisService;
	private int preapprovalStatus=0;
	private int instrumentCount=0;
	
	public  ContraService() throws Exception{
	}
	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao; 
	}
	public Position getPositionForWfItem(ContraJournalVoucher rv)
	{
		return eisCommonService.getPositionByUserId(rv.getCreatedBy().getId());
	}
	public Department getDepartmentForUser(User user) 
	{
		return new EgovCommon().getDepartmentForUser(user, eisCommonService, eisService,persistenceService);
    }
	public ContraJournalVoucher updateIntoContraJournal(CVoucherHeader voucherHeader,ContraBean contraBean){
		 ContraJournalVoucher existingCJV;
			 try{
				 existingCJV =contrajournalService.find("from ContraJournalVoucher where voucherHeaderId=?",voucherHeader );
				 existingCJV.setToBankAccountId(comm.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())));
				 contrajournalService.update(existingCJV);
			 }catch (HibernateException e) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception occuerd while postiong into contractorJournal");
				throw new HibernateException(e);
			}
			 catch (Exception e) {
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception occuerd while postiong into contractorJournal");
				throw new HibernateException(e);
					
			}
			 return existingCJV;
		 }
	 public Bankreconciliation updateBankreconciliation(InstrumentHeader instrHeader,ContraBean contraBean ){
		 Bankreconciliation existingBR;
		 try {
			 Long iHeaderId = instrHeader.getId();
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("instrHeader.getId() = " + iHeaderId);
			 existingBR = bankReconService.find("from Bankreconciliation where instrumentHeaderId=?",iHeaderId);
			 existingBR.setAmount(contraBean.getAmount());
			 existingBR.setBankaccount(comm.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())));
			 bankReconService.update(existingBR);
		}catch (HibernateException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception occuerd while updateBankreconciliation"+e);
			throw new HibernateException(e);
		}catch (Exception e) {
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception occuerd while updateBankreconciliation"+e);
				throw new HibernateException(e);
					
			}
		
		 return existingBR;
	 }
	 
	public  List<Transaxtion> postInTransaction(CVoucherHeader voucherHeader,ContraBean contraBean){
		
		 List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		 VoucherDetail vDetailBank = new VoucherDetail();
		 Transaxtion transactionBank = new Transaxtion();
		 
		 vDetailBank.setLineId(1);
		 vDetailBank.setVoucherHeaderId(voucherHeader);
		 CChartOfAccounts bankAccountCode = comm.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())).getChartofaccounts();
		 vDetailBank.setGlCode(bankAccountCode.getGlcode());
		 vDetailBank.setAccountName(bankAccountCode.getName());
		 vDetailBank.setDebitAmount(contraBean.getAmount());
		 vDetailBank.setCreditAmount(BigDecimal.ZERO);
		 vDetailBank.setNarration(voucherHeader.getVouchermis().getNarration());
	
		 transactionBank.setVoucherLineId("1");
		 transactionBank.setGlCode(bankAccountCode.getGlcode());
		 transactionBank.setGlName(bankAccountCode.getName());
		 transactionBank.setVoucherHeaderId(voucherHeader.getId().toString());
		 transactionBank.setDrAmount(contraBean.getAmount().toString());
		 transactionBank.setCrAmount("0");
		 
		 VoucherDetail vDetailCash = new VoucherDetail();
		 Transaxtion transactionCash = new Transaxtion();
		 
		 vDetailCash.setLineId(2);
		 vDetailCash.setVoucherHeaderId(voucherHeader);
		 CChartOfAccounts cashAccountCode = comm.getCChartOfAccountsByGlCode(contraBean.getCashInHand());
		 vDetailCash.setGlCode(cashAccountCode.getGlcode());
		 vDetailCash.setAccountName(cashAccountCode.getName());
		 vDetailCash.setDebitAmount(BigDecimal.ZERO);
		 vDetailCash.setCreditAmount(contraBean.getAmount());
		 vDetailCash.setNarration(voucherHeader.getVouchermis().getNarration());
		
		 transactionCash.setVoucherLineId("2");
		 transactionCash.setGlCode(cashAccountCode.getGlcode());
		 transactionCash.setGlName(cashAccountCode.getName());
		 transactionCash.setVoucherHeaderId(voucherHeader.getId().toString());
		 transactionCash.setDrAmount("0");
		 transactionCash.setCrAmount(contraBean.getAmount().toString());
		 
		 try {
			 vdPersitSer.persist(vDetailBank);
			 vdPersitSer.persist(vDetailCash);
		} catch (HibernateException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception Occured in Contra Service while preparing transaction"+e);
			throw new HibernateException(e);
		}
		
		 
		 transaxtionList.add(transactionBank);
		 transaxtionList.add(transactionCash);
		 
		 return transaxtionList;
	}
	public Map<String, Object> getCTBVoucher(final String voucherId,ContraBean contraBean){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("ContraService | getCTBVoucher | Start");
		Map<String, Object> voucherMap = new HashMap<String, Object>();
		CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", Long.valueOf(voucherId));
		voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);	
		InstrumentVoucher iVoucher = (InstrumentVoucher) persistenceService.find("from InstrumentVoucher where voucherHeaderId=?", voucherHeader);
		Bankaccount bankAccount = iVoucher.getInstrumentHeaderId().getBankAccountId();	
		contraBean.setAccountNumberId(bankAccount.getId().toString());
		contraBean.setAccnumnar(bankAccount.getNarration());
		contraBean.setBankBranchId(bankAccount.getBankbranch().getBank().getId()+"-"+
									   bankAccount.getBankbranch().getId().toString());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Cash amount = "+iVoucher.getInstrumentHeaderId().getInstrumentAmount());
		contraBean.setAmount(iVoucher.getInstrumentHeaderId().getInstrumentAmount());
		contraBean.setChequeNumber(iVoucher.getInstrumentHeaderId().getTransactionNumber());
		if(iVoucher.getInstrumentHeaderId().getTransactionDate()!=null)
		{
			contraBean.setChequeDate(Constants.DDMMYYYYFORMAT2.format(iVoucher.getInstrumentHeaderId().getTransactionDate()));
		}
		voucherMap.put("contrabean", contraBean); 
		return voucherMap;
		
		}
	
	 public Map<String, Object> getpayInSlipVoucher(final Long voucherId ,ContraBean contraBean,List<PayInBean> iHeaderList){
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("ContraService | getpayInSlipVoucher | Start");
		 final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		 Map<String, Object> voucherMap = new HashMap<String, Object>();
		 iHeaderList = new ArrayList<PayInBean>();
		 CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?",voucherId);
		 voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
		 List<InstrumentOtherDetails> iOther = (List<InstrumentOtherDetails>) persistenceService.findAllBy("from InstrumentOtherDetails where payinslipId=?", voucherHeader);
		 Bankaccount bankAccount = iOther.get(0).getInstrumentHeaderId().getBankAccountId();
		 contraBean.setAccountNumberId(bankAccount.getId().toString());
		 contraBean.setAccnumnar(bankAccount.getNarration());
		 contraBean.setBankBranchId(bankAccount.getBankbranch().getBank().getId()+"-"+
										   bankAccount.getBankbranch().getId().toString());
		 voucherMap.put("contraBean", contraBean);
		 PayInBean payInBean;
		 BigDecimal totalInstrAmt=BigDecimal.ZERO;
		 for (InstrumentOtherDetails instrumentOtherDetails : iOther) {
			 InstrumentHeader iHeader = instrumentOtherDetails.getInstrumentHeaderId();
			 int index = 0;
				payInBean = new PayInBean();
				payInBean.setInstId(Long.valueOf(iHeader.getId().toString()));
				payInBean.setInstrumentNumber(iHeader.getInstrumentNumber());
				try {
					payInBean.setInstrumentDate(formatter.format(formatter1.parse(iHeader.getInstrumentDate().toString())));
					InstrumentVoucher iVoucher = (InstrumentVoucher) persistenceService.find("from InstrumentVoucher where instrumentHeaderId=?",iHeader);
					payInBean.setVoucherDate(formatter.format(formatter1.parse(iVoucher.getVoucherHeaderId().getVoucherDate().toString())));
				} catch (ParseException e) {
					LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
				}
				payInBean.setInstrumentAmount(iHeader.getInstrumentAmount().toString());
				payInBean.setVoucherNumber(instrumentOtherDetails.getPayinslipId().getVoucherNumber());
				payInBean.setSelectChq(true);
				payInBean.setSerialNo(++index);
				iHeaderList.add(payInBean);
				totalInstrAmt = totalInstrAmt.add(iHeader.getInstrumentAmount());
		}
		 voucherMap.put("iHeaderList", iHeaderList);
		 voucherMap.put("totalInstrAmt", totalInstrAmt.toString());
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("ContraService | getpayInSlipVoucher | End");
		 return voucherMap;
		 
	 }
	 
	/**
	 * This method will be called for remit to bank in case of cheque/dd/card/atm/online deposit where a Contra Voucher is generated
	 * @param payInId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */
		
	public void updateCheque_DD_Card_Deposit(Long payInId,String toBankaccountGlcode,InstrumentHeader instrumentHeader,Map valuesMap)
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit | Start");
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit for"+instrumentHeader +"and payin id"+payInId);
		CVoucherHeader payIn=(CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",payInId);
	
		updateInstrumentAndPayin(payIn,(Bankaccount)valuesMap.get("depositedBankAccount"),instrumentHeader,(EgwStatus)valuesMap.get("instrumentDepositedStatus"));
		ContraJournalVoucher cjv = addToContra(payIn,(Bankaccount)valuesMap.get("depositedBankAccount"),instrumentHeader);
		addToBankRecon(payIn,instrumentHeader,(EgwStatus)valuesMap.get("instrumentReconciledStatus")); 
		if(cjv.getVoucherHeaderId().getModuleId()!=null && payIn.getStatus()==preapprovalStatus)
			new CreateVoucher().startWorkflow(cjv);
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit | End");
	}
	
	/**
	 *  Call this api before calling followin apis and send the map returned by this api into 
	 *  updateCheque_DD_Card_Deposit
	 *  updateCheque_DD_Card_Deposit_Receipt
	 *  updateCashDeposit
	 *  and 
	 * @param toBankaccountGlcode
	 * @return
	 */
	
	
	public Map prepareForUpdateInstrumentDeposit(String toBankaccountGlcode)
	{
		Map<String,Object> valuesMap=new HashMap<String, Object>();
		List<AppConfigValues> configValuesByModuleAndKey = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","PREAPPROVEDVOUCHERSTATUS");
		preapprovalStatus=Integer.valueOf(configValuesByModuleAndKey.get(0).getValue());
		EgwStatus instrumentDepositedStatus = (EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
				FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		
		EgwStatus instrumentReconciledStatus = (EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
				FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
		Bankaccount depositedBankAccount=(Bankaccount)	persistenceService.find("from Bankaccount where chartofaccounts.glcode=?",toBankaccountGlcode);
		valuesMap.put("preapprovalStatus", preapprovalStatus);
		valuesMap.put("instrumentDepositedStatus", instrumentDepositedStatus);
		valuesMap.put("instrumentReconciledStatus", instrumentReconciledStatus);
		valuesMap.put("depositedBankAccount", depositedBankAccount);
		return valuesMap;
		
	}
	/**
	 * This method will be called for remit to bank in case of cheque/dd/card/atm/online deposit where a Receipt Voucher is generated
	 * @param payInId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */
	public void updateCheque_DD_Card_Deposit_Receipt(Long receiptId,String toBankaccountGlcode,InstrumentHeader instrumentHeader,Map valuesMap)
	{
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit_Receipt | Start");
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit_Receipt for"+instrumentHeader +"and receiptId"+receiptId);
		CVoucherHeader payIn=(CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",receiptId);
		//Bankaccount depositedBankAccount=(Bankaccount)	persistenceService.find("from Bankaccount where chartofaccounts.glcode=?",toBankaccountGlcode);
		updateInstrumentAndPayin(payIn,(Bankaccount)valuesMap.get("depositedBankAccount"),instrumentHeader,(EgwStatus)valuesMap.get("instrumentDepositedStatus"));
		addToBankRecon(payIn,instrumentHeader,(EgwStatus)valuesMap.get("instrumentReconciledStatus"));
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit_Receipt | End");
	}
	/**
	 * used by modules which are integrating 
	 * @return
	 */
		
	public void updateCashDeposit(Long payInId,String toBankaccountGlcode,InstrumentHeader instrumentHeader,Map valuesMap)
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Contra Service | updateCashDeposit | Start");
		
		 PersistenceService<AppConfig, Integer> appConfigSer;
		 appConfigSer = new PersistenceService<AppConfig, Integer>();
		 appConfigSer.setType(AppConfig.class);
		
		 AppConfig appConfig= (AppConfig) appConfigSer.find("from AppConfig where key_name =?", "PREAPPROVEDVOUCHERSTATUS");
		 if(null != appConfig && null!= appConfig.getAppDataValues() ){
			 for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				 preapprovalStatus=Integer.valueOf(appConfigVal.getValue());
				 }
		 }else
			 throw new EGOVRuntimeException("Appconfig value for PREAPPROVEDVOUCHERSTATUS is not defined in the system");
		 
		CVoucherHeader payIn=(CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",payInId);
		//Bankaccount depositedBankAccount=(Bankaccount)	persistenceService.find("from Bankaccount where chartofaccounts.glcode=?",toBankaccountGlcode);
		
		updateInstrumentAndPayin(payIn,(Bankaccount)valuesMap.get("depositedBankAccount"),instrumentHeader,(EgwStatus)valuesMap.get("instrumentReconciledStatus"));
		ContraJournalVoucher cjv = addToContra(payIn,(Bankaccount)valuesMap.get("depositedBankAccount"),instrumentHeader);
		addToBankRecon(payIn,instrumentHeader,(EgwStatus)valuesMap.get("instrumentReconciledStatus"));
		if(cjv.getVoucherHeaderId().getModuleId()!=null && payIn.getStatus()==preapprovalStatus )
		{
			LOGGER.error("Caaling StartWorkflow...................................................................................................");
			new CreateVoucher().startWorkflow(cjv);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Contra Service | updateCashDeposit | End");
	}
	public void createVoucherfromPreApprovedVoucher(ContraJournalVoucher cjv){
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","APPROVEDVOUCHERSTATUS");
		final String approvedVoucherStatus = appList.get(0).getValue();
		cjv.getVoucherHeaderId().setStatus(Integer.valueOf(approvedVoucherStatus));
	}
	
	public void cancelVoucher(ContraJournalVoucher cjv){
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","cancelledstatus");
		final String approvedVoucherStatus = appList.get(0).getValue();
		cjv.getVoucherHeaderId().setStatus(Integer.valueOf(approvedVoucherStatus));
	}
	public String getDesginationName()
	{
		 PersonalInformation pi =eisCommonService.getEmployeeByUserId(EGOVThreadLocals.getUserId());
		 Assignment assignment =eisCommonService.getLatestAssignmentForEmployeeByToDate( pi.getIdPersonalInformation(),new Date());
		 return assignment.getDesignation().getName();
	}
	
	public Department getDepartmentForWfItem(ContraJournalVoucher cjv)
	{
		PersonalInformation pi =eisCommonService.getEmployeeByUserId(cjv.getCreatedBy().getId());
		Assignment assignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(pi.getIdPersonalInformation(),new Date());
		return assignment.getDepartment();
	}
	public Boundary getBoundaryForUser(ContraJournalVoucher rv)
	{
		return new EgovCommon().getBoundaryForUser(rv.getCreatedBy());
	}
	public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException
	{
		return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getIdPersonalInformation());
	}
	private void addToBankRecon(CVoucherHeader payIn,InstrumentHeader instrumentHeader,EgwStatus instrumentReconciledStatus) {
		instrumentService.addToBankReconcilationWithLoop(payIn, instrumentHeader,instrumentReconciledStatus);
	
		
	}
	private ContraJournalVoucher addToContra(CVoucherHeader payIn,Bankaccount depositedBank,InstrumentHeader instrumentHeader) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Adding to contra");
		ContraJournalVoucher cjv=new ContraJournalVoucher();
		cjv.setToBankAccountId(depositedBank);
		cjv.setInstrumentHeaderId(instrumentHeader);
		cjv.setVoucherHeaderId(payIn);
		contrajournalService.persist(cjv);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Adding to contra completed");
		return cjv;
	}
	private void updateInstrumentAndPayin(CVoucherHeader payIn, Bankaccount account,InstrumentHeader instrumentHeader,EgwStatus status) 
	{
	
			if(LOGGER.isDebugEnabled())     LOGGER.debug("updateInstrumentAndPayin | Start");
			Map<String,Object> iMap=new HashMap<String,Object>();
			List<Map<String,Object>> iList=new ArrayList<Map<String,Object>>();
			//List<InstrumentHeader> iHeaderList = createInstruements();
			iMap.put("Instrument header",instrumentHeader);
			iMap.put("Payin slip id", payIn);
			iMap.put("Instrument status date",payIn.getVoucherDate());
			iMap.put("Status id", status);
			iMap.put("Bank account id",account);
			iList.add(iMap);
			instrumentService.updateInstrumentOtherDetails(iList);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("updateInstrumentAndPayin | End");
     }
     public Map prepareForUpdateInstrumentDepositSQL()
	{
		Map<String,Object> valuesMap=new HashMap<String, Object>();
		List<AppConfigValues> configValuesByModuleAndKey = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","PREAPPROVEDVOUCHERSTATUS");
		preapprovalStatus=Integer.valueOf(configValuesByModuleAndKey.get(0).getValue());
		Integer instrumentDepositedStatusId = (Integer) persistenceService.find("select id from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
				FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		
		Integer instrumentReconciledStatusId = (Integer) persistenceService.find("select id from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
				FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
		valuesMap.put("preapprovalStatus", preapprovalStatus);
		valuesMap.put("instrumentDepositedStatus", instrumentDepositedStatusId.longValue());
		valuesMap.put("instrumentReconciledStatus", instrumentReconciledStatusId.longValue());
		return valuesMap;
		
	}
	/**This method will be called for remit to bank in case of cheque/dd/card/atm/online deposit where a Receipt Voucher is generated
	 *  This api will be used for only when it is called in loop 
	 * 
	 * @param isntrumentDetailsMap
	 * Map should contain  with following 
	 * No. key                       value
	 * 1.  "instrumentheader"         InstrumentHeader - org.egov.model.instrument.InstrumentHeader
	 * 2.  "bankaccountid"            Integer - id of the BankAccount
	 * 3.  "instrumentamount"         BigDecimal - instrumentamount
	 * 4.  "instrumenttype"           String - type of instrument (eg. Cheque,DD....)
	 * 5.  "depositdate"              Date - Date of remittance
	 * 6.  "createdby"                Integer - userid who is depositing     
	 * 7.  "ispaycheque"              boolean - saying whether it is paymentcheque or receipt cheque
	 * 8.  "payinid"                  Long     - Voucher header id
	 *  Map will also contain data returned by prepareForUpdateInstrumentDepositSQL which should be called only once    
	 */
	
	public void updateCheque_DD_Card_Deposit_Receipt(Map instrumentDetailsMap)
	{
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting updateCheque_DD_Card_Deposit_ReceiptSql ");
		updateInstrumentAndPayinSql(instrumentDetailsMap);
		addToBankReconcilationSQL(instrumentDetailsMap);
	}
	/**
	 * 
	 * @param isntrumentDetailsMap
	 * @see public void updateCheque_DD_Card_Deposit_Receipt(Map isntrumentDetailsMap) fordetails
	 */
	public void updateCheque_DD_Card_Deposit(Map instrumentDetailsMap)
	{
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit | start");
		updateInstrumentAndPayinSql(instrumentDetailsMap);
		addToBankReconcilationSQL(instrumentDetailsMap);
		addToContraSql(instrumentDetailsMap);
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(" updateCheque_DD_Card_Deposit | End"+instrumentCount);
	}
	/**
	 * @see public void updateCheque_DD_Card_Deposit_Receipt(Map isntrumentDetailsMap) fordetails
	 * @param isntrumentDetailsMap
	 *  Workflow will be called seperatly in createvoucher--startWorkflowForCashUpdate
	 */
	public void updateCashDeposit(Map instrumentDetailsMap)
	{
		updateInstrumentAndPayinSql(instrumentDetailsMap);
		addToBankReconcilationSQL(instrumentDetailsMap);
		addToContraSql(instrumentDetailsMap);
	}
	private void updateInstrumentAndPayinSql(Map instrumentDetailsMap) {
		String ioSql = "update EGF_INSTRUMENTOTHERDETAILS set PAYINSLIPID=:payinId,INSTRUMENTSTATUSDATE=:ihStatusDate," +
				       " LASTMODIFIEDBY=:modifiedBy, LASTMODIFIEDDATE =:modifiedDate where INSTRUMENTHEADERID=:ihId";

		SQLQuery ioSQLQuery = HibernateUtil.getCurrentSession().createSQLQuery(ioSql);
		ioSQLQuery.setLong("payinId", (Long)instrumentDetailsMap.get("payinid"))
		          .setLong("ihId", (Long)instrumentDetailsMap.get("instrumentheader"))
		          .setDate("ihStatusDate",(Date)instrumentDetailsMap.get("depositdate") )
		          .setDate("modifiedDate", new Date())
				  .setInteger("modifiedBy", (Integer)instrumentDetailsMap.get("createdby"));
		ioSQLQuery.executeUpdate();

		String ihSql = "update EGF_instrumentheader  set ID_STATUS=:statusId,BANKACCOUNTID=:bankAccId,LASTMODIFIEDBY=:modifiedBy,"
				     + " LASTMODIFIEDDATE =:modifiedDate where id=:ihId";

		SQLQuery ihSQLQuery = HibernateUtil.getCurrentSession().createSQLQuery(ihSql);
		ihSQLQuery.setLong("statusId",(Long)instrumentDetailsMap.get("instrumentDepositedStatus"))
		          .setLong("ihId", (Long)instrumentDetailsMap.get("instrumentheader"))
		          .setInteger("bankAccId",(Integer)instrumentDetailsMap.get("bankaccountid"))
		          .setDate("modifiedDate", new Date())
		          .setInteger("modifiedBy", (Integer)instrumentDetailsMap.get("createdby"));
		ihSQLQuery.executeUpdate();

	}
	/**
	 * 
	 * @param instrumentDetailsMap
	 * @throws EGOVRuntimeException
	 * 
	 * Will update bank reconcilation and set isreconciled to true for the type
	 * 1. cash
	 * 2.ECS
	 * 3. bank challan
	 * 4. bank 
	 */
	public void addToBankReconcilationSQL(Map instrumentDetailsMap)
	throws EGOVRuntimeException {
		String brsSql=	"Insert into bankreconciliation (ID,BANKACCOUNTID,AMOUNT,TRANSACTIONTYPE,INSTRUMENTHEADERID) values " +
        				" (seq_bankreconciliation.nextVal,:bankAccId,:amount,:trType,:ihId)";
		SQLQuery brsSQLQuery = HibernateUtil.getCurrentSession().createSQLQuery(brsSql);
		
		brsSQLQuery.setInteger("bankAccId",(Integer)instrumentDetailsMap.get("bankaccountid"))
					.setBigDecimal("amount",(BigDecimal)instrumentDetailsMap.get("instrumentamount") )
					.setString("trType", ("1".equalsIgnoreCase((String)instrumentDetailsMap.get("ispaycheque")))?"Cr":"Dr")
					.setLong("ihId", (Long)instrumentDetailsMap.get("instrumentheader"));
   		brsSQLQuery.executeUpdate();

	if(FinancialConstants.INSTRUMENT_TYPE_CASH.equalsIgnoreCase((String)instrumentDetailsMap.get("instrumenttype"))||
			FinancialConstants.INSTRUMENT_TYPE_BANK.equalsIgnoreCase((String)instrumentDetailsMap.get("instrumenttype"))||
	FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK.equalsIgnoreCase((String)instrumentDetailsMap.get("instrumenttype"))||
	FinancialConstants.INSTRUMENT_TYPE_ECS.equalsIgnoreCase((String)instrumentDetailsMap.get("instrumenttype")))
	{
		String ioSql=	"update EGF_instrumentOtherdetails set reconciledamount=:reconciledAmt,INSTRUMENTSTATUSDATE=:ihStatusDate,LASTMODIFIEDBY=:modifiedBy," +
        " LASTMODIFIEDDATE =:modifiedDate where INSTRUMENTHEADERID=:ihId";

		SQLQuery ioSQLQuery = HibernateUtil.getCurrentSession().createSQLQuery(ioSql);
		ioSQLQuery.setLong("ihId", (Long)instrumentDetailsMap.get("instrumentheader"))
				  .setBigDecimal("reconciledAmt",(BigDecimal)instrumentDetailsMap.get("instrumentamount") )
				  .setDate("ihStatusDate", (Date)instrumentDetailsMap.get("depositdate"))
				  .setDate("modifiedDate", new Date())
				  .setInteger("modifiedBy", (Integer)instrumentDetailsMap.get("createdby"));
		ioSQLQuery.executeUpdate();
		
		String ihSql=	"update EGF_instrumentheader  set ID_STATUS=:statusId,LASTMODIFIEDBY=:modifiedBy," +
        				" LASTMODIFIEDDATE =:modifiedDate where id=:ihId";
		SQLQuery ihSQLQuery = HibernateUtil.getCurrentSession().createSQLQuery(ihSql);
		ihSQLQuery.setLong("statusId",(Long)instrumentDetailsMap.get("instrumentReconciledStatus"))
				  .setLong("ihId", (Long)instrumentDetailsMap.get("instrumentheader"))
				  .setDate("modifiedDate", new Date())
				  .setInteger("modifiedBy", (Integer)instrumentDetailsMap.get("createdby"));
		ihSQLQuery.executeUpdate();

	}

}
	private void addToContraSql(Map instrumentDetailsMap) {
		
		 String ioSql=	"Insert into contrajournalvoucher (ID,VOUCHERHEADERID,FROMBANKACCOUNTID,TOBANKACCOUNTID,INSTRUMENTHEADERID"+
		 				" ,STATE_ID,CREATEDBY,LASTMODIFIEDBY) values "+
		 				" (seq_contrajournalvoucher.nextVal,:vhId,null,:depositedBankId,:ihId,null,:createdBy,:createdBy)";
		 SQLQuery ioSQLQuery = HibernateUtil.getCurrentSession().createSQLQuery(ioSql);
		 ioSQLQuery.setLong("vhId",(Long)instrumentDetailsMap.get("payinid"))
		 	       	.setLong("ihId", (Long)instrumentDetailsMap.get("instrumentheader"))
		 	       	.setLong("depositedBankId", (Integer)instrumentDetailsMap.get("bankaccountid"))
		 	       	.setInteger("createdBy", (Integer)instrumentDetailsMap.get("createdby"));
		 ioSQLQuery.executeUpdate();

		}

	
	
	@SuppressWarnings("unchecked")
	public void editInstruments(Long voucherId){
		
		List<InstrumentOtherDetails> iOtherdetails =(List<InstrumentOtherDetails>) persistenceService.findAllBy("from InstrumentOtherDetails  io where payinslipId.id=?",voucherId);
	
		for (InstrumentOtherDetails instrumentOtherDetails : iOtherdetails) {
			instrumentService.editInstruments(instrumentOtherDetails);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Modifying "+instrumentOtherDetails);
		}
	}
		
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public PersistenceService<ContraJournalVoucher, Long> getContrajournalService() {
		return contrajournalService;
	}
	public void setContrajournalService(
			PersistenceService<ContraJournalVoucher, Long> contrajournalService) {
		this.contrajournalService = contrajournalService;
	}
	public PersistenceService<Bankreconciliation, Integer> getBankReconService() {
		return bankReconService;
	}
	public void setBankReconService(
			PersistenceService<Bankreconciliation, Integer> bankReconService) {
		this.bankReconService = bankReconService;
	}
	public PersistenceService<VoucherDetail, Long> getVdPersitSer() {
		return vdPersitSer;
	}
	public void setVdPersitSer(PersistenceService<VoucherDetail, Long> vdPersitSer) {
		this.vdPersitSer = vdPersitSer;
	}
	public InstrumentService getInstrumentService() {
		return instrumentService;
	}
	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
}
