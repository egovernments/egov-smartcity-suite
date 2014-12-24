package org.egov.erpcollection.util;

import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.erpcollection.models.Challan;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceNumberGenerator;

public class CollectionsNumberGenerator {
	private SequenceNumberGenerator sequenceGenerator;
	private ScriptService scriptExecutionService;
	private CollectionsUtil collectionsUtil;

	/**
	 * This method generates the receipt number for the given receipt header
	 *  
	 * @param receiptHeader
	 *            an instance of <code>ReceiptHeader</code>
	 * 
	 * @return a <code>String</code> representing the receipt number
	 */
	public String generateReceiptNumber(ReceiptHeader receiptHeader){
		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(new Date());
		return (String)scriptExecutionService.executeScript(CollectionConstants.SCRIPT_RECEIPTNUMBER_GENERERATOR, ScriptService.createContext(
				"receiptHeader",receiptHeader,"finYear",financialYear,
				"sequenceGenerator",sequenceGenerator));
	}
	
	/**
	 * This method returns a list of internal reference numbers, each one corresponding 
	 * to the instrument type for the receipt
	 * 
	 * @param receiptHeader an instance of <code>ReceiptHeader</code> containing the
	 * receipt details
	 *  
	 * @param financialYear an instance of <code>CFinancialYear</code> representing the
	 * financial year for the receipt date
	 * 
	 * @return a <code>List</code> of strings, each representing the internal reference 
	 * number corresponding to a particular instrument type.
	 */
	public List<String> generateInternalReferenceNumber(ReceiptHeader receiptHeader,
			CFinancialYear financialYear,CFinancialYear currentFinancialYear){
		
		return (List<String>) scriptExecutionService.executeScript(
				CollectionConstants.SCRIPT_INTERNALREFNO_GENERERATOR, 
				ScriptService.createContext(
				"receiptHeader",receiptHeader,"finYear",financialYear,
				"currFinYr",currentFinancialYear,
				"sequenceGenerator",sequenceGenerator));
	}
	
	/**
	 * This method generates the challan number for the given receipt header
	 * 
	 * @param challan an instance of <code>Challan</code>
	 * 
	 * @return a <code>String</code> representing the challan number
	 */
	public String generateChallanNumber(Challan challan,CFinancialYear financialYear){
		return (String) scriptExecutionService.executeScript(
				CollectionConstants.SCRIPT_CHALLANNO_GENERERATOR, ScriptService
						.createContext("challan", challan, "sequenceGenerator",
								sequenceGenerator, "finYear", financialYear,
								"collectionsUtil", collectionsUtil));
	}

	public void setSequenceGenerator(SequenceNumberGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	/**
	 * @param scriptExecutionService the scriptExecutionService to set
	 */
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
}
