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
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.admin.master.entity.User;
import org.egov.web.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;
  
@Result(name="success", type="redirect", location = "bankRemittance")  
  
@ParentPackage("egov")  
@Transactional(readOnly=true)
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

	@Action(value="/receipts/bankRemittance-list", results = { @Result(name = NEW,type="redirect") })
	public String list() {
		long startTimeMillis = System.currentTimeMillis();
		User user=collectionsUtil.getLoggedInUser();
		
		StringBuilder jurValuesId = new StringBuilder();
		/* TODO: Uncomment after the getting all jurisdictions
		for (Iterator iter = user.getAllJurisdictions().iterator(); iter.hasNext();) {
			JurisdictionValues element = (JurisdictionValues) iter.next();
			if (jurValuesId.length() > 0) {
				jurValuesId.append(',');
			}
			jurValuesId.append(element.getBoundary().getId());
		}*/
			
		paramList= receiptHeaderService.findAllRemitanceDetails(jurValuesId.toString());
		addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForBankRemittanceApproval(
				collectionsUtil.getLoggedInUser()));
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

	@Transactional
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