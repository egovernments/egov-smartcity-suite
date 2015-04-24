package org.egov.erpcollection.web.actions.receipts;  
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.egov.commons.CVoucherHeader;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.user.User;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;
  
@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "bankRemittance.action")  
  
@ParentPackage("egov")  
public class BankRemittanceAction extends BaseFormAction{  
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(BankRemittanceAction.class);
	private List<HashMap<String, Object>> paramList=null;
	private ReceiptHeaderService receiptHeaderService;
	private final ReceiptHeader receiptHeaderIntsance=new ReceiptHeader();
	private List<CVoucherHeader> voucherHeaderValues = new ArrayList<CVoucherHeader>();
	private String[] serviceNameArray;
	private String[] totalCashAmountArray;
	private String[] totalChequeAmountArray;
	private String[] totalCardAmountArray;
	private String[] receiptDateArray;
	private String[] totalOnlineAmountArray;
	private String[] fundCodeArray;
	private String[] departmentCodeArray;
	private Integer accountNumberMaster;
	private CollectionsUtil collectionsUtil;
	
	//Added for Manual Work Flow
	private Integer positionUser;
	private Integer designationId;
	
	/**
	 * @param collectionsUtil the collectionsUtil to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	public String execute() throws Exception {
		return list();
	}

	public String newform(){
		return NEW;
	}

	/**
	 * 
	 * @return String
	 */
	public String list() {
		long startTimeMillis = System.currentTimeMillis();
		User user=collectionsUtil.getLoggedInUser(getSession());
		
		StringBuilder jurValuesId = new StringBuilder();
		
		for (Iterator iter = user.getAllJurisdictions().iterator(); iter.hasNext();) {
			JurisdictionValues element = (JurisdictionValues) iter.next();
			if (jurValuesId.length() > 0) {
				jurValuesId.append(',');
			}
			jurValuesId.append(element.getBoundary().getId());
		}
			
		paramList= receiptHeaderService.findAllRemitanceDetails(jurValuesId.toString());
		addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForBankRemittanceApproval(
				collectionsUtil.getLoggedInUser(getSession())));
		addDropdownData("designationMasterList", new ArrayList());
		addDropdownData("postionUserList", new ArrayList());
		
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        
        LOGGER.info("$$$$$$ Time taken to populate the remittance list (ms) = " + elapsedTimeMillis);
		return NEW;
	}

	public String edit(){
		return EDIT;
	}

	public String save(){
		return SUCCESS;
	}
	
	public void prepare() {
		super.prepare();
		addDropdownData("bankBranchList", new ArrayList());
		addDropdownData("accountNumberList", new ArrayList());
	}	

	public String create(){
		long startTimeMillis = System.currentTimeMillis();
		voucherHeaderValues=receiptHeaderService.createBankRemittance(getServiceNameArray(),getTotalCashAmountArray(),getTotalChequeAmountArray(),
				getTotalCardAmountArray(),getTotalOnlineAmountArray(),getReceiptDateArray(),getFundCodeArray(),getDepartmentCodeArray(),accountNumberMaster,positionUser);
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
	    LOGGER.info("$$$$$$ Time taken to persist the remittance list (ms) = " + elapsedTimeMillis);
		return INDEX;
	}
	
	public Object getModel() {
		return receiptHeaderIntsance;
	}

	
	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}

	/**
	 * @return the paramList
	 */
	public List<HashMap<String, Object>> getParamList() {
		return paramList;
	}

	/**
	 * @param paramList the paramList to set
	 */
	public void setParamList(List<HashMap<String, Object>> paramList) {
		this.paramList = paramList;
	}

	/**
	 * @return the serviceName
	 */
	public String[] getServiceNameArray() {
		return serviceNameArray;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceNameArray(String[] serviceNameArray) {
		this.serviceNameArray = serviceNameArray;
	}

	/**
	 * @return the totalCashAmount
	 */
	public String[] getTotalCashAmountArray() {
		return totalCashAmountArray;
	}

	/**
	 * @param totalCashAmount the totalCashAmount to set
	 */
	public void setTotalCashAmountArray(String[] totalCashAmountArray) {
		this.totalCashAmountArray = totalCashAmountArray;
	}

	/**
	 * @return the totalChequeAmount
	 */
	public String[] getTotalChequeAmountArray() {
		return totalChequeAmountArray;
	}

	/**
	 * @param totalChequeAmount the totalChequeAmount to set
	 */
	public void setTotalChequeAmountArray(String[] totalChequeAmountArray) {
		this.totalChequeAmountArray = totalChequeAmountArray;
	}

	/**
	 * @return the receiptDate
	 */
	public String[] getReceiptDateArray() {
		return receiptDateArray;
	}

	/**
	 * @param receiptDate the receiptDate to set
	 */
	public void setReceiptDateArray(String[] receiptDateArray) {
		this.receiptDateArray = receiptDateArray;
	}

	/**
	 * @return the voucherHeaderValues
	 */
	public List<CVoucherHeader> getVoucherHeaderValues() {
		return voucherHeaderValues;
	}

	/**
	 * @param voucherHeaderValues the voucherHeaderValues to set
	 */
	public void setVoucherHeaderValues(List<CVoucherHeader> voucherHeaderValues) {
		this.voucherHeaderValues = voucherHeaderValues;
	}

	/**
	 * @return the accountNumberMaster
	 */
	public Integer getAccountNumberMaster() {
		return accountNumberMaster;
	}

	/**
	 * @param accountNumberMaster the accountNumberMaster to set
	 */
	public void setAccountNumberMaster(Integer accountNumberMaster) {
		this.accountNumberMaster = accountNumberMaster;
	}

	
	/**
	 * @return the totalOnlineAmountArray
	 */
	public String[] getTotalOnlineAmountArray() {
		return totalOnlineAmountArray;
	}

	/**
	 * @param totalOnlineAmountArray the totalOnlineAmountArray to set
	 */
	public void setTotalOnlineAmountArray(String[] totalOnlineAmountArray) {
		this.totalOnlineAmountArray = totalOnlineAmountArray;
	}

	/**
	 * @return the fundCodeArray
	 */
	public String[] getFundCodeArray() {
		return fundCodeArray;
	}

	/**
	 * @param fundCodeArray the fundCodeArray to set
	 */
	public void setFundCodeArray(String[] fundCodeArray) {
		this.fundCodeArray = fundCodeArray;
	}

	/**
	 * @return the departmentCodeArray
	 */
	public String[] getDepartmentCodeArray() {
		return departmentCodeArray;
	}

	/**
	 * @param departmentCodeArray the departmentCodeArray to set
	 */
	public void setDepartmentCodeArray(String[] departmentCodeArray) {
		this.departmentCodeArray = departmentCodeArray;
	}

	/**
	 * @return the totalCardAmountArray
	 */
	public String[] getTotalCardAmountArray() {
		return totalCardAmountArray;
	}

	/**
	 * @param totalCardAmountArray the totalCardAmountArray to set
	 */
	public void setTotalCardAmountArray(String[] totalCardAmountArray) {
		this.totalCardAmountArray = totalCardAmountArray;
	}

	/**
	 * @return the positionUser
	 */
	public Integer getPositionUser() {
		return positionUser;
	}

	/**
	 * @param positionUser the positionUser to set
	 */
	public void setPositionUser(Integer positionUser) {
		this.positionUser = positionUser;
	}

	/**
	 * @return the designationId
	 */
	public Integer getDesignationId() {
		return designationId;
	}

	/**
	 * @param designationId the designationId to set
	 */
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	

	
}