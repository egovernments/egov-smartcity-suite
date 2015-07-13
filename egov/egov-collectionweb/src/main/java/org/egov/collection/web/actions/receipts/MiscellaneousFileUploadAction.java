/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.web.actions.receipts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.instrument.InstrumentHeader;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

@ParentPackage("egov")  
@Transactional(readOnly=true)
public class MiscellaneousFileUploadAction extends BaseFormAction{ 
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MiscellaneousFileUploadAction.class);
	private File importFile;
	private String importContentType; 
    private String importFileName; 
    private String importfileCaption;
    
    /**
     * An array list of String arrays. Each String array represents an 
     * input set of values for misc receipt creation. 
     */
    private List<String[]> inputList = new ArrayList<String[]>();
    
	private List<ReceiptDetailInfo> billCreditDetailslist;
	
	private List<ReceiptDetailInfo> billRebateDetailslist;
	
	private List<ReceiptDetailInfo> subLedgerlist;
	
	SimpleDateFormat sdfInput =  new SimpleDateFormat ("dd.MM.yy", Locale.getDefault());  
    
	private ReceiptAction receiptAction;
    
    private CollectionCommon collectionCommon;
    private FinancialsUtil financialsUtil;
    private CollectionsUtil collectionsUtil;
    
    private CommonsServiceImpl commonsServiceImpl;
    
    private ReceiptHeaderService receiptHeaderService;
    
    private Map<Integer, String> errorRowMap = new TreeMap<Integer, String>();
    
    private Integer successNo=0;
    
	public Map<Integer, String> getErrorRowMap() {
		return errorRowMap;
	}

	private List<String> errorMsgsList = new ArrayList<String>();
    

	private void injectObjectsIntoAction(){
		receiptAction=new ReceiptAction();

		receiptAction.setSession(getSession());
		receiptAction.setPersistenceService(getPersistenceService());
		
		receiptAction.setCollectionCommon(collectionCommon);
		receiptAction.setCollectionsUtil(collectionsUtil);
		//receiptAction.setCommonsManager(commonsServiceImpl);
		receiptAction.setFinancialsUtil(financialsUtil);
		
		receiptAction.setReceiptHeaderService(receiptHeaderService);
		//receiptAction.setReceiptPayeeDetailsService(receiptPayeeDetailsService);
		receiptAction.setReceiptBulkUpload(Boolean.TRUE);
	}
	
	private void initialiseObjectsInAction(String[] input){
		receiptAction.setBillSource("misc");
		receiptAction.setPayeename(input[3]);
		receiptAction.setPaidBy(input[3]);
		
		billCreditDetailslist=new ArrayList<ReceiptDetailInfo>();
		billRebateDetailslist=new ArrayList<ReceiptDetailInfo>();
		
		subLedgerlist= new ArrayList<ReceiptDetailInfo>();
		receiptAction.setReceiptMisc(createReceiptMisc(input));
		
		Department dept = (Department) persistenceService.find(
				"from Department d where d.deptName=? ",input[1]);
		
		receiptAction.setDeptId(dept.getId().toString());
		
		Date dt = new Date();
		try {
			dt = sdfInput.parse(input[0]);
			receiptAction.setManualReceiptDate(new DateTime(dt));
			receiptAction.setVoucherDate(new DateTime(dt));
		} catch (ParseException e) {
			LOGGER.debug("Error occured while parsing receipt date [ " + input[0] + " ] : "+ e.getMessage());
		}
		
		if(input[9].equals(CollectionConstants.INSTRUMENTTYPE_CASH)){
			receiptAction.setCashOrCardInstrumenttotal(BigDecimal.ZERO);
			InstrumentHeader instrHeaderCash = new InstrumentHeader();
			instrHeaderCash.setInstrumentAmount(new BigDecimal(input[8]));
			
			receiptAction.setInstrHeaderCash(instrHeaderCash);
			receiptAction.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
			/*String[] instrumentType = {};
			receiptAction.setInstrumentType(instrumentType);*/
		}
		
		if(input[9].equals(CollectionConstants.INSTRUMENTTYPE_BANK)){
			receiptAction.setCashOrCardInstrumenttotal(BigDecimal.ZERO);
			
			InstrumentHeader instrHeaderBank = new InstrumentHeader();
			instrHeaderBank.setInstrumentAmount(new BigDecimal(input[8]));
			instrHeaderBank.setTransactionDate(receiptAction.getManualReceiptDate().toDate());
			
			instrHeaderBank.setTransactionNumber(dept.getCode()+new SimpleDateFormat ("ddMMyy", Locale.getDefault()).format(dt)+"UPL");
			
			Bankaccount account = (Bankaccount) persistenceService.find("from Bankaccount where accountnumber=?",input[4]);
			receiptAction.setBankAccountId(account.getId().intValue());
			
			receiptAction.setInstrHeaderBank(instrHeaderBank);
			receiptAction.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_BANK);
			/*String[] instrumentType = {};
			receiptAction.setInstrumentType(instrumentType);*/
		}
		
		createCreditDetailslist(input);
		createEmptyRebatelist();
		createEmptySubLedgerlist();
		
		receiptAction.setBillCreditDetailslist(billCreditDetailslist);
		receiptAction.setSubLedgerlist(subLedgerlist);
		receiptAction.setBillRebateDetailslist(billRebateDetailslist);
	}
	
	/**
	 * This method is invoked from UI to validate the miscellaneous receipt records.
	 * @return
	 */
	public String fileValidate(){
		this.readColumn();

		removeNullListEntries();

		// populate receipt header and challan objects for each of the input
		// sets of values

		String[] input;
		for (int i = 0; i < inputList.size(); i++) {
		//for (int i = 0; i < 10; i++) {
			input = inputList.get(i);
			input = formatValues(input);
			if (validateExcel(input)) {
				successNo++;
			}
			else{
				LOGGER.debug("Misc row no: " + i
						+ " not persisted due to some incomplete data!!!!");
			}
		}
		generateErrorReport();
		return SUCCESS;
	}
    
	
	private void generateErrorReport(){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("ManualMiscUploadErrorReport.txt"));
			String[] errors = new String[20];
			
			if(!errorRowMap.isEmpty()){
				fos.write(("Errors in Uploading Following Rows \n").getBytes());
				fos.write("********************************** \n\n".getBytes());
				Iterator it = errorRowMap.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			       
			        errors = pairs.getValue().toString().split(",");
			        for(int i=0;i<errors.length;i++){
			        	 fos.write(("  Row " + pairs.getKey().toString() + " : " + errors[i] + "\n").getBytes());
			        }
			    }
			}
			LOGGER.debug("Written to file successfully ");
			fos.close(); 
			
		} catch (FileNotFoundException e) {
			LOGGER.error("Error occrured while generating report : " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error occrured while generating report : " + e.getMessage());
		}
	}
    /**
	 * This method is used to persist the data from the uploaded file.
	 * 
	 * @return
	 */
	public String save() {
		this.readColumn();

		//remove empty rows from being processed
		removeNullListEntries();
		
		injectObjectsIntoAction();
		
		String[] input;
		
		//tried for first 10 records only. Should loop through till end of inputList
		//for (int i = 0; i < 100; i++) {
//		for(String[] input : inputList){
		for (int i = 0; i < inputList.size(); i++) {
			input = inputList.get(i);
			input = formatValues(input);
			
			if (validateExcel(input)) {
				
				
				initialiseObjectsInAction(input);
				
				//HibernateUtil.beginTransaction();
				try{
					receiptAction.save();
				}
				catch(Exception ex){
					//Some error in Receipt creation. Log, rollback and move to next record.
					String errMsg = "Error in Receipt Creation : " + ex.getMessage();
					LOGGER.error(errMsg, ex);
					errorRowMap.put(Integer.valueOf(input[11]), errMsg);
					//HibernateUtil.rollbackTransaction();
					continue;
				}
				
				//HibernateUtil.commitTransaction();
				successNo++;
				
				LOGGER.info(" Persisted " + (i+1) + " records ");
				LOGGER.debug(" Persisted " + (i+1) + " records ");
			}
		}
		return SUCCESS;
	}
	
	public void createCreditDetailslist(String[] inputArray) {
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		CChartOfAccounts account = (CChartOfAccounts)persistenceService.find(
				"from CChartOfAccounts  where glcode=? ", inputArray[10]);
		vd.setAccounthead(account.getName());
		vd.setCreditAmountDetail(new BigDecimal(inputArray[8]));
		vd.setDebitAmountDetail(BigDecimal.ZERO);
		vd.setAmount(BigDecimal.ZERO);
		vd.setGlcodeDetail(account.getGlcode());
		vd.setGlcodeIdDetail(account.getId());
		
		CFinancialYear financialYear1011=(CFinancialYear)persistenceService.find(
				"from CFinancialYear  where finYearRange=? ","2010-11");
		
		vd.setFinancialYearId(financialYear1011.getId());
		billCreditDetailslist.add(vd);
	}
	
	
	public void createEmptySubLedgerlist(){
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setCreditAmountDetail(BigDecimal.ZERO);
		vd.setDebitAmountDetail(BigDecimal.ZERO);
		vd.setAmount(BigDecimal.ZERO);
		vd.setDetailCode(CollectionConstants.BLANK);
		vd.setDetailKey(CollectionConstants.BLANK);
		subLedgerlist.add(vd);
		
	}
	public void createEmptyRebatelist(){
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setAccounthead("");
		vd.setCreditAmountDetail(BigDecimal.ZERO);
		vd.setDebitAmountDetail(BigDecimal.ZERO);
		vd.setAmount(BigDecimal.ZERO);
		vd.setGlcodeDetail("");
		billRebateDetailslist.add(vd);
	}
	public ReceiptMisc createReceiptMisc(String[] inputArray) {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		Fund fund = (Fund) persistenceService.find("from Fund  where name=? ",inputArray[2]);
		receiptMisc.setFund(fund);
		return receiptMisc;
	}
	
	
	private String[] formatValues(String[] inputArray){
		
		//To change bank code from 201417.0 to 201417
		if(inputArray[4].endsWith(".0")){
			inputArray[4]=inputArray[4].substring(0, inputArray[4].indexOf('.'));
		}
		
		//To change account code from 140200201.0 to 140200201
		if(inputArray[10].endsWith(".0")){
			inputArray[10]=inputArray[10].substring(0, inputArray[10].indexOf('.'));
		}
		//To change account code from 1.401306E8 to 140200201
		BigDecimal bd = new BigDecimal(inputArray[10]);
		inputArray[10]=bd.toPlainString();
		
		return inputArray;
	}
	
	
	private boolean validateExcel(String[] inputArray){
		boolean valid=true;
		// errorMsgsList.clear();
		String errorMsgs="";
		
		// Receipt date should be mandatory, should be of valid format and should belong to a financial year in database.
		if(inputArray[0]==null || inputArray[0].equals(CollectionConstants.BLANK)){
			errorMsgs+="Receipt Date is null/Empty";
			LOGGER.debug("Receipt Date is null/Empty");
			valid=false;
		}
		
		try {
			Date date = sdfInput.parse(inputArray[0]);
			collectionsUtil.getFinancialYearforDate(date);
		} 
		catch (ParseException e) {
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Invalid Receipt Date[ " + inputArray[0] + " ]";
			}
			else
				errorMsgs+=", Invalid Receipt Date[ " + inputArray[0] + " ]";
			valid=false;
		}
		catch(Exception ex){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="No financial year found for receipt date [" + inputArray[0] + "]";
			}
			else
				errorMsgs+=", No financial year found for receipt date [" + inputArray[0] + "]";
			valid=false;
		}
		
		//Department name should be mandatory
		if(inputArray[1]==null || inputArray[1].equals(CollectionConstants.BLANK)){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Department is null/Empty";
			}
			else
				errorMsgs+=", Department is null/Empty";
			LOGGER.debug("Department is null/Empty");
			valid=false;
		}	
		else{
			Department dept = (Department) persistenceService.find(
					"from Department d where d.deptName=? ",inputArray[1]);
			if(dept==null){
				if(errorMsgs.equals(CollectionConstants.BLANK)){
					errorMsgs+="Incorrect value for Department [" + inputArray[1] + "]";
				}
				else
					errorMsgs+=", Incorrect value for Department [" + "]";
				
				LOGGER.debug("Incorrect value for Department " + inputArray[1] + "]");
				valid=false;
			}
		}
		
		// Fund name should be mandatory and should be valid
		if(inputArray[2]==null || inputArray[2].equals(CollectionConstants.BLANK)){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Fund is null/Empty";
			}
			else
				errorMsgs+=", Fund is null/Empty";
			LOGGER.debug("Fund is null/Empty");
			valid=false;
		}	
		else{
			Fund fund = (Fund) persistenceService.find("from Fund  where name=? ",inputArray[2]);
			if(fund==null){
				if(errorMsgs.equals(CollectionConstants.BLANK)){
					errorMsgs+="Incorrect value for fund [" + inputArray[2] + "]";
				}
				else
					errorMsgs+=", Incorrect value for fund " + inputArray[2] + "]";
				
				LOGGER.debug("Incorrect value for fund" + inputArray[2] + "]");
				valid=false;
			}
		}
		
		// Payee name should be mandatory
		if(inputArray[3]==null || inputArray[3].equals(CollectionConstants.BLANK)){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Payee Name is null/Empty";
			}
			else
				errorMsgs+=", Payee Name is null/Empty";
			LOGGER.debug("Payee Name is null/Empty");
			valid=false;
		}
		/* Commented  to ignore the debit account codes 
		if(inputArray[5]==null || inputArray[5].equals(CollectionConstants.BLANK)){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Account Code is null/Empty";
			}
			else
				errorMsgs+=", Account Code is null/Empty";
			LOGGER.debug("Account Code is null/Empty");
			valid=false;
		}*/
		// Credit Amount should be mandatory
		if(inputArray[6]==null || inputArray[6].equals(CollectionConstants.BLANK) || inputArray[6].equals("0.0") ){
			errorMsgsList.add("Account head Amount is null/Empty/Zero");
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Account head Amount is null/Empty/Zero";
			}
			else
				errorMsgs+=", Account head Amount is null/Empty/Zero";
			LOGGER.debug("Account head Amount is null/Empty/Zero");
			valid=false;
		}
		
		// Amount should be mandatory
		if(inputArray[8]==null || inputArray[8].equals(CollectionConstants.BLANK) || inputArray[8].equals("0.0") ){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Payment Amount is null/Empty";
			}
			else
				errorMsgs+=", Payment Amount is null/Empty";
			LOGGER.debug("Payment Amount is null/Empty");
			valid=false;
		}
		
		//Mode of payment should be mandatory
		if(inputArray[9]==null || inputArray[9].equals(CollectionConstants.BLANK)){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Mode of payment is null/Empty";
			}
			else
				errorMsgs+=", Mode of payment is null/Empty";
			LOGGER.debug("Mode of payment is null/Empty");
			valid=false;
		}
		
		//if instrument type is 'bank', bank name should be present 
		if(inputArray[9].equals(CollectionConstants.INSTRUMENTTYPE_BANK)){
			if(inputArray[3].equals(CollectionConstants.BLANK)){
				if(errorMsgs.equals(CollectionConstants.BLANK)){
					errorMsgs+="Bank Name is null/Empty";
				}
				else
					errorMsgs+=", Bank Name is null/Empty";
				LOGGER.debug("Bank Name is null/Empty");
				valid=false;
			}
		}
		
		//if instrument type is 'bank', bank account name should be present and should be valid
		if(inputArray[9].equals(CollectionConstants.INSTRUMENTTYPE_BANK)){
			if(inputArray[4].equals(CollectionConstants.BLANK)){
				if(errorMsgs.equals(CollectionConstants.BLANK)){
					errorMsgs+="Bank Account Code is null/Empty";
				}
				else
					errorMsgs+=", Bank Account Code is null/Empty";
				LOGGER.debug("Bank Account Code is null/Empty");
				valid=false;
			}
			else{
				Bankaccount bankAccount = (Bankaccount) persistenceService.find(
						"from Bankaccount where accountnumber=?",inputArray[4]);
				if(bankAccount==null){
					if(errorMsgs.equals(CollectionConstants.BLANK)){
						errorMsgs+="Incorrect value for Bank Account Code [" + inputArray[4] + "]";
					}
					else
						errorMsgs+=", Incorrect value for Bank Account Code [" + inputArray[4] + "]";
					
					LOGGER.debug("Incorrect value for Bank Account Code [" + inputArray[4] + "]");
					valid=false;
				}
			}
		}
		
		//Account code (Debit Receivable code should be present and should be valid
		if(inputArray[10]==null || inputArray[10].equals(CollectionConstants.BLANK)){
			if(errorMsgs.equals(CollectionConstants.BLANK)){
				errorMsgs+="Department Receivable Code is null/Empty";
			}
			else
				errorMsgs+=", Department Receivable Code is null/Empty";
			LOGGER.debug("Department Receivable Code is null/Empty");
			valid=false;
		}
		else{
			CChartOfAccounts account = (CChartOfAccounts)persistenceService.find(
					"from CChartOfAccounts  where glcode=? ", inputArray[10]);
			if(account==null){
				if(errorMsgs.equals(CollectionConstants.BLANK)){
					errorMsgs+="Incorrect value for Account Code [" + inputArray[10] +"]";
				}
				else
					errorMsgs+=", Incorrect value for Account Code [" + inputArray[10] +"]";
				
				LOGGER.debug("Incorrect value for Account Code [" + inputArray[10] +"]");
				valid=false;
			}
		}
		
		if(!valid){
			errorRowMap.put(Integer.valueOf(inputArray[11]), errorMsgs);
		}
		return valid;
	}
	/**
	 * This method removes any rows which have all null/empty entries in the column cells.
	 */
	private void removeNullListEntries(){
		List<String[]> tempInputList = new ArrayList<String[]>();
		
		for (String[] currentArray : inputList){
			boolean isNull=true;
			for(int j=0;j<currentArray.length-1;j++){
				if(currentArray[j]!=null && !currentArray[j].equals(CollectionConstants.BLANK)){
					isNull=false;
					break;
				}
			}
			if(!isNull)
				tempInputList.add(currentArray);
		}
		inputList=tempInputList;
	}
    public String execute() throws Exception {  
		return list();  
	}  
	  
	public String newform(){  
		return NEW;  
	} 
	
	public String list() {  
		return INDEX;  
	}  
  
	public String edit(){  
		  
		return EDIT;  
	}  
	public String create(){  
		  
		return SUCCESS;  
	}  

	private void readColumn()
	{
		try{
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(this.getImportFile()));
				HSSFWorkbook wb = new HSSFWorkbook(fs);
			    HSSFSheet sheet = wb.getSheetAt(1);
			    HSSFRow row;
			    HSSFCell cell;
			    
			    int rows; // Number of rows
			    rows = sheet.getPhysicalNumberOfRows();

			    int cols = 0; // Number of columns
			    int tmp = 0;
			    
			    for(int i = 0; i < 15 || i < rows; i++) {
			        row = sheet.getRow(i);
			        if(row != null) {
			            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
			            if(tmp > cols) cols = tmp;
			        }
			    }
			    for(int r=1;r<rows;r++)
			    {
			    	
			    	row=sheet.getRow(r);
			    	if(row!=null)
			    	{
			    		// Each row represents a set of input values for challan receipt creation
			    		String[] inputArray = new String[12];
			    		for(int c=0;c<11;c++)
			    		{
			    			cell = row.getCell(c);
			    			LOGGER.info(" Cell : " + c + " Value : " + cell);
			    			
			    			if(cell==null){
			    				inputArray[c]="";
			    			}
			    			else{
			    				int ct = cell.getCellType();
				    			if(ct==HSSFCell.CELL_TYPE_FORMULA){
				    				inputArray[c] = Double.toString(cell.getNumericCellValue());
				    			}
				    			else
			    				inputArray[c]=cell.toString();
			    			}
			    				
			    		}	
			    		//the last element holds the correct row number
			    		inputArray[11]=String.valueOf(r+1);
			    		inputList.add(inputArray);
			    	}
			    }
			    
			}catch(Exception e){
				LOGGER.debug("Exception in reading columns : " + e.getMessage());
				
			}
		}
	
	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public String getImportContentType() {
		return importContentType;
	}

	public void setImportContentType(String importContentType) {
		this.importContentType = importContentType;
	}

	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	public String getImportfileCaption() {
		return importfileCaption;
	}

	public void setImportfileCaption(String importfileCaption) {
		this.importfileCaption = importfileCaption;
	}
	
	public Object getModel() {  
		return null;  
	}
	/**
	 * @param collectionCommon
	 *            the collectionCommon to set
	 */
	public void setCollectionCommon(CollectionCommon collectionCommon) {
		this.collectionCommon = collectionCommon;
	}
	/**
	 * @param receiptHeaderService
	 *            The receipt header service to set
	 */
	public void setReceiptHeaderService(
			ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}
	public void setFinancialsUtil(FinancialsUtil financialsUtil) {
		this.financialsUtil = financialsUtil;
	}

	public List<ReceiptDetailInfo> getSubLedgerlist() {
		return subLedgerlist;
	}

	public void setSubLedgerlist(List<ReceiptDetailInfo> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}

	public List<ReceiptDetailInfo> getBillCreditDetailslist() {
		return billCreditDetailslist;
	}

	public void setBillCreditDetailslist(
			List<ReceiptDetailInfo> billCreditDetailslist) {
		this.billCreditDetailslist = billCreditDetailslist;
	}

	public List<ReceiptDetailInfo> getBillRebateDetailslist() {
		return billRebateDetailslist;
	}

	public void setBillRebateDetailslist(
			List<ReceiptDetailInfo> billRebateDetailslist) {
		this.billRebateDetailslist = billRebateDetailslist;
	}
	
	public Integer getSuccessNo() {
		return successNo;
	}

	public void setSuccessNo(Integer successNo) {
		this.successNo = successNo;
	}
	
}
