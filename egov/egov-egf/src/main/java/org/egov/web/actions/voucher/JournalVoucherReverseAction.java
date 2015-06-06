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
package org.egov.web.actions.voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.model.voucher.VoucherDetails;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
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
