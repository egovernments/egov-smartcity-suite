/**
 * 
 */
package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.contra.ContraBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.HibernateException;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

/**
 * @author msahoo
 *
 */
public class BaseVoucherAction extends BaseFormAction {                       
	private static final long serialVersionUID = 1L;	
	protected final String            INVALIDPAGE   = "invalidPage";
	private static final String						FAILED						= "Transaction failed";
	private static final String						EXCEPTION_WHILE_SAVING_DATA	= "Exception while saving data";
	private static final Logger	LOGGER	= Logger.getLogger(BaseVoucherAction.class);
	public CVoucherHeader voucherHeader = new CVoucherHeader();
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	protected String voucherNumManual;
	protected UserService userMngr;   
	protected EisUtilService eisService;          
	
	
	protected String reversalVoucherNumber;
	protected String reversalVoucherDate;
	final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
	final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
	private Integer voucherNumberPrefixLength;
	public static final String	ZERO	= "0";
	private FinancingSourceService financingSourceService ;
	List<String> voucherTypes=VoucherHelper.VOUCHER_TYPES;
	Map<String,List<String>> voucherNames=VoucherHelper.VOUCHER_TYPE_NAMES;
	public BaseVoucherAction()
	{
		voucherHeader.setVouchermis(new Vouchermis());
		addRelatedEntity("vouchermis.departmentid", Department.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.function", CFunction.class);
		addRelatedEntity("vouchermis.fundsource", Fundsource.class);
		addRelatedEntity("vouchermis.divisionid", Boundary.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
	}
	@Override
	public Object getModel() {
	
		return voucherHeader;
	}
	@SkipValidation
	public String newform()
	{
		return NEW; 
		
	}
	@Override
	public void prepare() {
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Prepare method");
		getHeaderMandateFields();
		if(headerFields.contains("department")){
			addDropdownData("departmentList", masterCache.get("egi-department"));
		}
		if(headerFields.contains("functionary")){
			addDropdownData("functionaryList", masterCache.get("egi-functionary"));
		}
		if(headerFields.contains("function")){
			addDropdownData("functionList", masterCache.get("egi-function"));
		}
		if(headerFields.contains("fund")){
			addDropdownData("fundList",  masterCache.get("egi-fund"));
		}
		if(headerFields.contains("fundsource")){
			addDropdownData("fundsourceList", masterCache.get("egi-fundSource"));
		}
		if(headerFields.contains("field")){
			addDropdownData("fieldList", masterCache.get("egi-ward"));
		}
		if(headerFields.contains("scheme")){
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(headerFields.contains("subscheme")){
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
		
		//addDropdownData("typeList", persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh where vh.status!=4 order by vh.type"));
		addDropdownData("typeList",VoucherHelper.VOUCHER_TYPES);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Number of  MIS attributes are :"+headerFields.size());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Number of mandate MIS attributes are :"+mandatoryFields.size());
	}       
@Deprecated
	protected void getHeaderMandateFields() {
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf("|"));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf("|")+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
		setOneFunctionCenterValue();       
		mandatoryFields.add("voucherdate");
	}
	public boolean isOneFunctionCenter(){
		setOneFunctionCenterValue();
		return false;//voucherHeader.getIsRestrictedtoOneFunctionCenter();   
	}

	public void setOneFunctionCenterValue() {
		AppConfigValues appConfigValues = (AppConfigValues) persistenceService.find("from AppConfigValues where key in " +
				"(select id from AppConfig where key_name='ifRestrictedToOneFunctionCenter' and module='EGF' )");
		if(appConfigValues==null)
			throw new ValidationException("Error","ifRestrictedToOneFunctionCenter is not defined");
		else{   
			//voucherHeader.setIsRestrictedtoOneFunctionCenter(appConfigValues.getValue().equalsIgnoreCase("yes")?true:false);   
		}
	}
	public boolean isBankBalanceMandatory()
	{  
		AppConfigValues appConfigValues = (AppConfigValues) persistenceService.find("from AppConfigValues where key in (select id from AppConfig where key_name='bank_balance_mandatory' and module='EGF' )");
		if(appConfigValues==null)
			throw new ValidationException("","bank_balance_mandatory parameter is not defined");
		return appConfigValues.getValue().equals("Y")?true:false;
	}
	
	protected void loadSchemeSubscheme(){
		if(headerFields.contains("scheme") && null != voucherHeader.getFundId()){
			addDropdownData("schemeList", getPersistenceService().findAllBy(findAllBy"from Scheme where fund=?", voucherHeader.getFundId()));
		}
		if(headerFields.contains("subscheme") && voucherHeader.getVouchermis()!=null && null !=  voucherHeader.getVouchermis().getSchemeid()){
			addDropdownData("subschemeList", getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=1 order by name", voucherHeader.getVouchermis().getSchemeid().getId()));
		}
	} 
	
	protected void loadFundSource(){
		if(headerFields.contains("fundsource") && null != voucherHeader.getVouchermis().getSubschemeid()){
			List<Fundsource> fundSourceList = financingSourceService.getFinancialSourceBasedOnSubScheme(voucherHeader.getVouchermis().getSubschemeid().getId());
			addDropdownData("fundsourceList", fundSourceList);
		}
	} 
	/*protected void loadFunction(){
		if(headerFields.contains("function") && null != voucherHeader.getVouchermis().getFunction()){
			List<Fundsource> fundSourceList = financingSourceService.getFinancialSourceBasedOnSubScheme(voucherHeader.getVouchermis().getSubschemeid().getId());
			addDropdownData("fundsourceList", fundSourceList);
		}
	} */
	
protected HashMap<String, Object> createHeaderAndMisDetails() throws ValidationException
	{
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();  
		headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
		headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());                
		headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE, voucherHeader.getVoucherSubType());                                
		headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
		headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
		headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());
		                                       
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeader.getVouchermis().getDepartmentid().getCode());
		if(voucherHeader.getFundId()!=null)
			headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader.getFundId().getCode());
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
			headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader.getVouchermis().getSchemeid().getCode());
		if( voucherHeader.getVouchermis().getSubschemeid()!=null)
			headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader.getVouchermis().getSubschemeid().getCode());
		if(voucherHeader.getVouchermis().getFundsource()!=null)
			headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader.getVouchermis().getFundsource().getCode());
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
			headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader.getVouchermis().getDivisionid().getId());
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
			headerdetails.put(VoucherConstant.FUNCTIONARYCODE,voucherHeader.getVouchermis().getFunctionary().getCode());
		if(voucherHeader.getVouchermis().getFunction()!=null)
			headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
		return headerdetails;   
	}                                 
@Deprecated
	protected void validateFields() { 
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Validate Method");
		
		checkMandatoryField("vouchernumber",voucherHeader.getVoucherNumber(),"voucher.field.vouchernumber");
		checkMandatoryField("voucherdate",voucherHeader.getVoucherDate(),"voucher.field.voucherdate");
		checkMandatoryField("fund",voucherHeader.getFundId(),"voucher.fund.mandatory");     
		checkMandatoryField("function",voucherHeader.getVouchermis().getFunction(),"voucher.function.mandatory");
		checkMandatoryField("department",voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatoryField("scheme",voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");    
		checkMandatoryField("subscheme",voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatoryField("functionary",voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatoryField("fundsource",voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatoryField("field",voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
		
	}
	protected void checkMandatoryField(String fieldName,Object value,String errorKey) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Filed name :="+fieldName +" Value = :"+value); 
			
		if(mandatoryFields.contains(fieldName) && (value == null || StringUtils.isEmpty(value.toString())))
			throw new ValidationException(Arrays.asList(new ValidationError(errorKey,errorKey)));
		
	} 
	public CVoucherHeader createVoucherAndledger(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist)  {
		//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
		try {
			final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
			// update DirectBankPayment source path
		//	headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/payment/directBankPayment!beforeView.action?voucherHeader.id=");
			HashMap<String, Object> detailMap = null;
			HashMap<String, Object> subledgertDetailMap = null;
			final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
			final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
			
			detailMap = new HashMap<String, Object>();
			Map<String, Object> glcodeMap = new HashMap<String, Object>();
			for (VoucherDetails voucherDetail : billDetailslist)
			{                
				detailMap = new HashMap<String, Object>();
				if (voucherDetail.getFunctionIdDetail() != null) {
					if(voucherHeader.getIsRestrictedtoOneFunctionCenter())
						detailMap.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
					else if(null!=voucherDetail.getFunctionIdDetail()){
						CFunction function = (CFunction) HibernateUtil.getCurrentSession().load(CFunction.class, voucherDetail.getFunctionIdDetail());
						detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
					} else{
						if(null!=voucherHeader.getVouchermis().getFunction())
						detailMap.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
					}
				}
				if (voucherDetail.getCreditAmountDetail().compareTo(BigDecimal.ZERO) ==0) {
					
					detailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getDebitAmountDetail().toString());
					detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
					detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
					accountdetails.add(detailMap);
					glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.DEBIT);
				}
				else {
					detailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getCreditAmountDetail().toString());
					detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
					detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
					accountdetails.add(detailMap);
					glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.CREDIT);
				}
				
			}
			
			for (VoucherDetails voucherDetail : subLedgerlist) {
				subledgertDetailMap = new HashMap<String, Object>();
				String amountType = glcodeMap.get(voucherDetail.getSubledgerCode())!=null?glcodeMap.get(voucherDetail.getSubledgerCode()).toString():null; // Debit or Credit.
				if(voucherDetail.getFunctionDetail()!=null && !voucherDetail.getFunctionDetail().equalsIgnoreCase("") && !voucherDetail.getFunctionDetail().equalsIgnoreCase("0")){
				CFunction function = (CFunction) persistenceService.find("from CFunction where id = ?",Long.parseLong(voucherDetail.getFunctionDetail()));
				subledgertDetailMap.put(VoucherConstant.FUNCTIONCODE, function!=null?function.getCode():"");
				}
				if(null != amountType && amountType.equalsIgnoreCase(VoucherConstant.DEBIT)){
					subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getAmount());
				}else if(null != amountType ){
					subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getAmount());
				}
				subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, voucherDetail.getDetailType().getId());
				subledgertDetailMap.put(VoucherConstant.DETAILKEYID, voucherDetail.getDetailKeyId());
				subledgertDetailMap.put(VoucherConstant.GLCODE, voucherDetail.getSubledgerCode());
				
				subledgerDetails.add(subledgertDetailMap);
			}
			
			// create voucehr API
			final CreateVoucher cv = new CreateVoucher();
			voucherHeader = cv.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);
			
		} catch (final HibernateException e) {
			LOGGER.error(e.getMessage(),e);
			throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		} catch (final EGOVRuntimeException e) {
			LOGGER.error(e.getMessage(),e);
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		} catch (final ValidationException e) {
			LOGGER.error(e.getMessage(),e);
			throw e;
		} catch (final Exception e) {
			// handle engine exception
			LOGGER.error(e.getMessage(),e);
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
		return voucherHeader;
		
	}
	
	protected boolean  validateData(final List<VoucherDetails> billDetailslist, final List<VoucherDetails> subLedgerList){
		BigDecimal totalDrAmt = BigDecimal.ZERO;
		BigDecimal totalCrAmt = BigDecimal.ZERO;
		int index=0;
		boolean isValFailed = false;
		Map<String,List<String>> accCodeFuncMap = new HashMap<String,List<String>> ();
		
		//Map<String, String> glcodeMap = new HashMap<String, String>();// to support same account code to be used multiple times.
		for (VoucherDetails voucherDetails : billDetailslist) {
			index = index+1;
			totalDrAmt = totalDrAmt.add(voucherDetails.getDebitAmountDetail());
			totalCrAmt = totalCrAmt.add(voucherDetails.getCreditAmountDetail());
			/*if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0 
					&& voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0 
					&& voucherDetails.getGlcodeDetail().trim().length()==0) {
				
				addActionError(getText("journalvoucher.accdetail.emptyaccrow",new String[]{""+index}));
				isValFailed= true ;
			}
			else */
			/*if(null != glcodeMap.get(voucherDetails.getGlcodeDetail().trim())){
				addActionError(getText("journalvoucher.accdetail.repeated",new String[]{voucherDetails.getGlcodeDetail()}));
				isValFailed= true ;	
			}else{
				glcodeMap.put(voucherDetails.getGlcodeDetail().trim(), voucherDetails.getGlcodeDetail().trim());
			}*/ // to support same account code to be used multiple times.
			         
			if(voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0 && 
					voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0 && voucherDetails.getGlcodeDetail().trim().length()!=0){
				addActionError(getText("journalvoucher.accdetail.amountZero",new String[]{voucherDetails.getGlcodeDetail()}));
				isValFailed= true ;
			}else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) >0 && 
					voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO)>0) {
				addActionError(getText("journalvoucher.accdetail.amount",new String[]{voucherDetails.getGlcodeDetail()}));
				isValFailed= true ;
			}else if (( voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) >0 
					||voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) >0)
					&& voucherDetails.getGlcodeDetail().trim().length()==0) {
				addActionError(getText("journalvoucher.accdetail.accmissing",new String[]{""+index}));
				isValFailed= true ;
			}
			else {
				String functionId =null;
				/*if(voucherHeader.getIsRestrictedtoOneFunctionCenter()){
					functionId =  voucherHeader.getFunctionId()!=null?voucherHeader.getFunctionId().toString():"0";
				}else{ */ 
					functionId=voucherDetails.getFunctionIdDetail()!=null?voucherDetails.getFunctionIdDetail().toString():"0";
				//}
				List<String> existingFuncs = accCodeFuncMap.get(voucherDetails.getGlcodeDetail());
				if(null == existingFuncs){
					List<String> list = new ArrayList<String>();
					list.add(functionId);
					accCodeFuncMap.put(voucherDetails.getGlcodeDetail(), list); 
				}else{
					if(functionId.equals("0") || existingFuncs.contains("0")){
						addActionError(getText("jv.multiplecodes.funcMissing",new String[]{voucherDetails.getGlcodeDetail()}));
						isValFailed= true ;break;
					}else if(existingFuncs.contains(functionId)){
						addActionError(getText("jv.multiplecodes.duplicateFunc",new String[]{voucherDetails.getGlcodeDetail()}));
						isValFailed= true ; break;
						
					}else{
						existingFuncs.add(functionId);
						accCodeFuncMap.put(voucherDetails.getGlcodeDetail(),existingFuncs); 
					}
					
				}
			}
		}
		 if (totalDrAmt.compareTo(totalCrAmt) != 0 && !isValFailed) {
			addActionError(getText("journalvoucher.accdetail.drcrmatch"));
			isValFailed= true ;
		}
		 else if(!isValFailed){
			 isValFailed= validateSubledgerDetails( billDetailslist,subLedgerList);
		}
		 return isValFailed;
	}
	/*
	 * description -  validate the total subledger data.
	 */
	@SuppressWarnings("unchecked")
	protected boolean validateSubledgerDetails(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist){
		
		Map<String,Object> accountDetailMap ;
		List<Map<String,Object>> subLegAccMap=  new ArrayList<Map<String,Object>>(); // this list will contain  the details about the account code those are detail codes.
		List<String> repeatedglCodes = VoucherHelper.getRepeatedGlcodes(billDetailslist);
		for (VoucherDetails voucherDetails : billDetailslist) {
			CChartOfAccountDetail  chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().findInCache(" from CChartOfAccountDetail" +
					" where glCodeId=(select id from CChartOfAccounts where glcode=?)", voucherDetails.getGlcodeDetail());
			if(null != chartOfAccountDetail){
				accountDetailMap = new HashMap<String,Object>();
				if(repeatedglCodes.contains(voucherDetails.getGlcodeIdDetail().toString())){
					/*if(voucherHeader.getIsRestrictedtoOneFunctionCenter()){
						accountDetailMap.put("glcodeId-funcId", voucherDetails.getGlcodeIdDetail()+"-"+voucherHeader.getFunctionId());
					}else{*/
						accountDetailMap.put("glcodeId-funcId", voucherDetails.getGlcodeIdDetail()+"-"+voucherDetails.getFunctionIdDetail());
					//}
				}else{
					accountDetailMap.put("glcodeId-funcId", voucherDetails.getGlcodeIdDetail()+"-"+"0");
				}
				accountDetailMap.put("glcode", voucherDetails.getGlcodeDetail());
				if(voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0){
					accountDetailMap.put("amount", voucherDetails.getCreditAmountDetail());
				}else if (voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO)==0) {
					accountDetailMap.put("amount", voucherDetails.getDebitAmountDetail());
				}
					subLegAccMap.add(accountDetailMap);
			}
		}
		
		Map<String,BigDecimal> subledAmtmap = new HashMap<String,BigDecimal> ();
		Map<String, String> subLedgerMap = new HashMap<String, String> ();
		for (VoucherDetails voucherDetails : subLedgerlist) {
			
			if(voucherDetails.getGlcode().getId() != 0){
				String function = repeatedglCodes.contains(voucherDetails.getGlcode().getId().toString())?voucherDetails.getFunctionDetail():"0";
				//above is used to check if this account code is used multiple times in the account grid or not, if it used multiple times
				// then take the function into consideration while calculating the total sl amount , else igone the function by paasing function value=0
				if(null !=subledAmtmap.get(voucherDetails.getGlcode().getId()+"-"+function)){
					BigDecimal debitTotalAmount =subledAmtmap.get(voucherDetails.getGlcode().getId()+"-"+function)
												.add(voucherDetails.getAmount());
					subledAmtmap.put(voucherDetails.getGlcode().getId()+"-"+function,debitTotalAmount);
				}else {
					subledAmtmap.put(voucherDetails.getGlcode().getId()+"-"+function
							,voucherDetails.getAmount());
				}
				StringBuffer subledgerDetailRow = new StringBuffer();
				if(voucherDetails.getDetailType().getId() ==0 || null == voucherDetails.getDetailKeyId()){
					addActionError(getText("journalvoucher.subledger.entrymissing",new String[]{voucherDetails.getSubledgerCode()}));
					return true;
				}else
				{
					subledgerDetailRow.append( voucherDetails.getGlcode().getId().toString()).
					append(voucherDetails.getDetailType().getId().toString()).
					append(voucherDetails.getDetailKeyId().toString()).
					append(voucherDetails.getFunctionDetail());
				}
				if(null == subLedgerMap.get(subledgerDetailRow.toString())){
					subLedgerMap.put(subledgerDetailRow.toString(),subledgerDetailRow.toString());
				}// to check for  the same subledger should not allow for same gl code
				else{
					addActionError(getText("journalvoucher.samesubledger.repeated"));
					return true;
				}
				
			}
		}
		if(subLegAccMap.size() > 0){
			for (Map<String,Object> map : subLegAccMap) {
				 String glcodeIdAndFuncId = map.get("glcodeId-funcId").toString();
				if(null == subledAmtmap.get(glcodeIdAndFuncId) ){
					String functionId = glcodeIdAndFuncId.split("-")[1];
					if(functionId.equalsIgnoreCase("0")){
						addActionError(getText("journalvoucher.subledger.entrymissing",new String[]{map.get("glcode").toString()}));
					}else{
						CFunction function = (CFunction)persistenceService.get(CFunction.class,Long.valueOf(functionId));
						addActionError(getText("journalvoucher.subledger.entrymissingFunc",new String[]{map.get("glcode").toString(),function.getName()}));
					}
					return true;
				}else if(((BigDecimal) subledAmtmap.get(glcodeIdAndFuncId)).compareTo(new BigDecimal(map.get("amount").toString()))!=0){
					String functionId = glcodeIdAndFuncId.split("-")[1];
					if(functionId.equalsIgnoreCase("0")){
						addActionError(getText("journalvoucher.subledger.amtnotmatchinng",new String[]{map.get("glcode").toString()}));
					}else{
						CFunction function = (CFunction)persistenceService.find("from CFunction where id=?",Long.valueOf(functionId));
						addActionError(getText("journalvoucher.subledger.amtnotmatchinngFunc",new String[]{map.get("glcode").toString(),function.getName()}));
					}
					return true;
				}
			}
		}
		
		StringBuffer fyQuery = new StringBuffer();
		fyQuery.append("from CFinancialYear where isActiveForPosting=1 and startingDate <= '").
			append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("'");
		List<CFinancialYear> list = persistenceService.findAllBy(fyQuery.toString());
		if(list.size() == 0){
			addActionError(getText("journalvoucher.fYear.notActive"));
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected void loadBankAccountNumber(ContraBean contraBean){
		if(null != contraBean.getBankBranchId() && !contraBean.getBankBranchId().equalsIgnoreCase("-1")){
			 int index1 = contraBean.getBankBranchId().indexOf("-");
			 Integer branchId = Integer.valueOf(contraBean.getBankBranchId().substring(index1+1, contraBean.getBankBranchId().length()));
			 List<Bankaccount> bankAccountList = getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? " +
			 		"  and isactive=1 order by id",branchId);
			 addDropdownData("accNumList",bankAccountList );
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Account number list size "+bankAccountList.size() );
		}
		
	}
	@SuppressWarnings("unchecked")
	protected void loadBankAccountNumber(String  bankBranchId){
		if(null != bankBranchId && !bankBranchId.equalsIgnoreCase("-1")){
			 int index1 = bankBranchId.indexOf("-");
			 Integer branchId = Integer.valueOf(bankBranchId.substring(index1+1, bankBranchId.length()));
			 List<Bankaccount> bankAccountList = getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? " +
			 		"  and isactive=1 order by id",branchId);
			 addDropdownData("accNumList",bankAccountList );
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Account number list size "+bankAccountList.size() );
		}
		
	}
@SuppressWarnings("unchecked")
public void loadBankBranchForFund(){
	if(LOGGER.isDebugEnabled())     LOGGER.debug("BaseVoucherAction | loadBankBranchForFund | Start");
	try {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("FUND ID = "+voucherHeader.getFundId().getId());
		List<Object[]> bankBranch = (List<Object[]>)getPersistenceService().findAllBy("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname " +
				" FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount " +
				" where  bank.isactive=1  and bankBranch.isactive=1 and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id" +
				" and bankaccount.fund.id=?",voucherHeader.getFundId().getId());
		
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Bank list size is "+ bankBranch.size());
		List<Map<String, Object>> bankBranchList = new ArrayList<Map<String, Object>>();
		Map<String, Object> bankBrmap ;
		for(Object[] element : bankBranch){
			bankBrmap = new HashMap<String, Object> ();
			bankBrmap.put("bankBranchId", element[0].toString()); 
			bankBrmap.put("bankBranchName", element[1].toString());
			bankBranchList.add(bankBrmap);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Bank branch list size :"+bankBranchList.size() );
		addDropdownData("bankList",bankBranchList);
	} catch (HibernateException e) {
		LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		
	}
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public boolean shouldShowHeaderField(String field){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside shouldShowHeaderField menthod");
		if(field.equals("vouchernumber"))
		{
			String  vNumGenMode="Manual";
			if(voucherHeader.getType()!=null && voucherHeader.getType().equalsIgnoreCase("Journal Voucher"))
			{
				vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
			}
			else
			{
			 vNumGenMode= new VoucherTypeForULB().readVoucherTypes(voucherHeader.getType());
			}
			if(!"Auto".equalsIgnoreCase(vNumGenMode)){
				mandatoryFields.add("vouchernumber");
				return true	;
			}else
			{
			return false;
			}
		
		}
		else
		{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Header field contains = "+ headerFields.contains(field));
		return  headerFields.contains(field);
		}
	}
	public String getTransactionType()
	{
		return voucherHeader.getType();
	}
	public List<String> getHeaderFields() {
		return headerFields;
	}
	public void setHeaderFields(List<String> headerFields) { 
		this.headerFields = headerFields;
	}
	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}
	public void setMandatoryFields(List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}
	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}
	public String getVoucherNumManual() {
		return voucherNumManual;
	}
	public void setVoucherNumManual(String voucherNumManual) {
		this.voucherNumManual = voucherNumManual;
	}
	protected void removeEmptyRowsAccoutDetail(List list) {
		for (Iterator<VoucherDetails> detail = list.iterator(); detail.hasNext();) {
			VoucherDetails next = detail.next();
			if (next != null && next.getGlcodeDetail().trim().isEmpty() && (next.getFunctionDetail().trim().isEmpty())
					&& 
					next.getDebitAmountDetail().equals(BigDecimal.ZERO) && next.getCreditAmountDetail().equals(BigDecimal.ZERO) ) {
					detail.remove();
			}
			else if(next==null)
			{
				detail.remove();
			}
		}
	}
	
	protected void removeEmptyRowsSubledger(List<VoucherDetails> list) {
		for (Iterator<VoucherDetails> detail = list.iterator(); detail.hasNext();) {
			VoucherDetails next = detail.next();
			if( (next != null) && (next.getSubledgerCode() == null || next.getSubledgerCode().equals(""))) {
					detail.remove();
				
			}else if(next==null)
			{
				detail.remove();
			}
				
		}
	
	}
	public void saveReverse(String voucherName,String type) {
		CVoucherHeader reversalVoucher = null;
		HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
		reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
		reversalVoucherMap.put("Reversal voucher type", type);
		reversalVoucherMap.put("Reversal voucher name", voucherName);
		reversalVoucherMap.put("Reversal voucher date", getReversalVoucherDate());
		reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
		List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
		reversalList.add(reversalVoucherMap);
		try {
			reversalVoucher = new CreateVoucher().reverseVoucher(reversalList);
		} catch (ValidationException e) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("ERROR in Reversing voucher"+e.getMessage());
			addActionError(getText(e.getErrors().get(0).getMessage()));
			return;
		} catch (EGOVRuntimeException e) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("ERROR "+e.getMessage());
			addActionError(getText(e.getMessage()));
			return;
		} catch (ParseException e) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("ERROR "+e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("invalid.date.format", "invalid.date.format")));
		}
		addActionMessage(getText("transaction.success") + reversalVoucher.getVoucherNumber());
		voucherHeader = reversalVoucher;
	}
	/**
	 * 
	 */
	protected void loadDefalutDates() {
		final Date currDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			voucherHeader.setVoucherDate(sdf.parse(sdf.format(currDate)));
		} catch (final ParseException e) {
			LOGGER.error("Inside loadDefalutDates"+e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting voucher date","Transaction failed")));
		}
	}
	
	protected void resetVoucherHeader(){
		// required only for manual voucher number.
		if(null != parameters.get("voucherNumberPrefix")){
			String voucherNumeditPart = voucherHeader.getVoucherNumber();
			voucherHeader.setVoucherNumber(parameters.get("voucherNumberPrefix")[0]+voucherNumeditPart);
		}
	}
	
	
	public Integer getVoucherNumberPrefixLength() {
		voucherNumberPrefixLength=Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH);
		return voucherNumberPrefixLength;
	}
	
	public void setVoucherNumberPrefixLength(Integer voucherNumberPrefixLength) {
		this.voucherNumberPrefixLength = voucherNumberPrefixLength;
	}
	
	public String getReversalVoucherNumber() {
		return reversalVoucherNumber;
	}
	
	public void setReversalVoucherNumber(String reversalVoucherNumber) {
		this.reversalVoucherNumber = reversalVoucherNumber;
	}
	
	public String getReversalVoucherDate() {
		return reversalVoucherDate;
	}
	
	public void setReversalVoucherDate(String reversalVoucherDate) {
		this.reversalVoucherDate = reversalVoucherDate;
	}
	public void setFinancingSourceService(
			FinancingSourceService financingSourceService) {
		this.financingSourceService = financingSourceService;
	}
	protected Boolean validateOwner(State state)
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("validating owner for user "+EGOVThreadLocals.getUserId());
		List<Position> positionsForUser=null;
		positionsForUser = null;//This fix is for Phoenix Migration.eisService.getPositionsForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), new Date());
		if(positionsForUser.contains(state.getOwnerPosition()))      
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Valid Owner :return true");
			return true;
		}else
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Invalid  Owner :return false");
			return false;
		}
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	public EisUtilService getEisService() {
		return eisService;
	}
	public List<String> getVoucherTypes() {
		return voucherTypes;
	}
	public void setVoucherTypes(List<String> voucherTypes) {
		this.voucherTypes = voucherTypes;
	}
	public Map<String, List<String>> getVoucherNames() {
		return voucherNames;
	}
	public void setVoucherNames(Map<String, List<String>> voucherNames) {
		this.voucherNames = voucherNames;
	}
	
}
