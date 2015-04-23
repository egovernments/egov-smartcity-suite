package org.egov.web.actions.voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.model.voucher.VoucherDetails;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.annotation.ValidationErrorPage;

public class JournalVoucherReverseAction extends BaseVoucherAction {
	
	private static final Logger	LOGGER	= Logger.getLogger(JournalVoucherReverseAction.class);
	
	public  Map<String,String> nameList = new LinkedHashMap<String, String>();
	public List<Map<String,Object>> voucherList;
	public GenericHibernateDaoFactory genericDao;
	
	public String fromDate;
	public String toDate;
	private String showMode;
	private String message;
	private String	button;
	private String cgNumber;
	private boolean close;
	private String target;
	
	public static final String SEARCH = "search";
	public static final String REVERSE = "reverse";
	public final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	
	private List<VoucherDetails> billDetailslist;
	private List<VoucherDetails> subLedgerlist;
	private VoucherService voucherService;
	
	public String beforeReverse() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("JournalVoucherReverseAction  | reverse | Start ");
		Map<String, Object> vhInfoMap = voucherService.getVoucherInfo(voucherHeader.getId());
		voucherHeader = (CVoucherHeader)vhInfoMap.get(Constants.VOUCHERHEADER);
		billDetailslist = (List<VoucherDetails>) vhInfoMap.get(Constants.GLDEATILLIST);
		subLedgerlist = (List<VoucherDetails>) vhInfoMap.get("subLedgerDetail");
		loadSchemeSubscheme();
		return REVERSE;
	}
	
	@ValidationErrorPage(value=REVERSE)
	public String reverse(){
		saveReverse();
		setMessage(getText("transaction.success") + voucherHeader.getVoucherNumber());
		return REVERSE;
	}

	public void saveReverse() {
		CVoucherHeader reversalVoucher = null;
		HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getReversalVoucherDate    :::::::::::::"+getReversalVoucherDate());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getReversalVoucherNumber    :::::::::::::"+getReversalVoucherNumber());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader.getId()    :::::::::::::"+voucherHeader.getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader.getType()    :::::::::::::"+voucherHeader.getType());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader.getName()    :::::::::::::"+voucherHeader.getName());
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader.formatter date     :::::::::::::"+formatter.format(getReversalVoucherDate()) );
	
		reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
		//by #1388 in erp_financials all adjustments(reversal etc) should be JV General 
		reversalVoucherMap.put("Reversal voucher type",FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
		reversalVoucherMap.put("Reversal voucher name",FinancialConstants.JOURNALVOUCHER_NAME_GENERAL);
		setTarget("stay");
		Date reversalDate = null;
		try {
			if(getReversalVoucherDate() != null && !getReversalVoucherDate().equals("")) {
				String str1 = getReversalVoucherDate();
				reversalDate = formatter.parse(str1);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader.reversalDate     :::::::::::::"+reversalDate.getDate());
			}
			
		} catch(ParseException pe) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader.reversalDate     :::::::::::::"+pe);
		}
		
		
		reversalVoucherMap.put("Reversal voucher date", reversalDate);
		reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
		List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
		reversalList.add(reversalVoucherMap);
		try {
			reversalVoucher = new CreateVoucher().reverseVoucher(reversalList);
		} catch (ValidationException e) {
			clearMessages();
			resetVoucherHeader();
			if(subLedgerlist.size() ==0){
				subLedgerlist.add(new VoucherDetails());
			}
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
			 throw new ValidationException(errors);
		} catch (Exception e) {
			clearMessages();
			resetVoucherHeader();
			if(subLedgerlist.size() ==0){
				subLedgerlist.add(new VoucherDetails());
			}
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		addActionMessage(getText("transaction.success") + reversalVoucher.getVoucherNumber());
		voucherHeader = reversalVoucher;
		setTarget("success");
	}
	
	protected void getHeaderFieldsLoad() {
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULT_SEARCH_MISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) 
		{
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) 
			{
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf('|'));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf('|')+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
	}
	
	public Map<String, String> getNameList() {
		return nameList;
	}

	public void setNameList(Map<String, String> nameList) {
		this.nameList = nameList;
	}
	
	public Map<String, String> getVoucherNameMap(String type) {
		List<Object> voucherNameList = getPersistenceService().findAllBy("select  distinct name from  CVoucherHeader where type=?",type);
		nameList=new LinkedHashMap<String,String>();
	
		for(Object voucherName: voucherNameList )
		{
			nameList.put((String)voucherName,(String)voucherName);
		}
		return nameList;
	}
	
	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	@Override
	public Object getModel() {
		return voucherHeader;
	}
	

	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate; 
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public VoucherService getVoucherService() {
		return voucherService;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
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

	public String getCgNumber() {
		return cgNumber;
	}

	public void setCgNumber(String cgNumber) {
		this.cgNumber = cgNumber;
	}

	public List<VoucherDetails> getBillDetailslist() {
		return billDetailslist;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	public boolean isClose() {
		return close;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getFormattedNewDate(){
		return Constants.DDMMYYYYFORMAT2.format(new Date());
	}
}
