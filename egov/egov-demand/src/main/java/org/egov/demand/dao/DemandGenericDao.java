/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.demand.dao;

import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infra.admin.master.entity.Module;

import java.math.BigDecimal;
import java.util.List;


public interface DemandGenericDao {
	
		
	/**
	 * This method called getDemandReasonMasterByModule gets  List<EgDemandReasonMaster> objects.
	 *
	 * <p>This method returns  List<EgDemandReasonMaster> objects for given  module.</p>
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  List<EgDemandReasonMaster> objects.
	 *
	 * 
	 */
	public List<EgDemandReasonMaster> getDemandReasonMasterByModule(Module module);
	
	/**
	 * This method called getDemandReasonMasterByCategoryAndModule gets  List<EgDemandReasonMaster> objects.
	 *
	 * <p>This method returns  List<EgDemandReasonMaster> objects for given egReasonCategory and  module.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgReasonCategory egReasonCategory.
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  List<EgDemandReasonMaster> objects.
	 *
	 * 
	 */
	public List<EgDemandReasonMaster> getDemandReasonMasterByCategoryAndModule(EgReasonCategory egReasonCategory,Module module);
	
	/**
	 * This method called getDemandReasonMasterByCode gets EgDemandReasonMaster object.
	 *
	 * <p>This method returns  EgDemandReasonMaster object for given code and  module.</p>
	 *
	 * @param java.lang.Integer code.
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  EgDemandReasonMaster object.
	 *
	 * 
	 */
	public EgDemandReasonMaster getDemandReasonMasterByCode(String code,Module module);  
	
	/**
	 * This method called getDemandReasonByInstallmentAndModule gets  List<EgDemandReason> objects.
	 *
	 * <p>This method returns  List<EgDemandReason> objects for given egReasonCategory and  module.</p>
	 *
	 * @param org.egov.commons.Installment installment.
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  List<EgDemandReason> objects.
	 *
	 * 
	 */
	public List<EgDemandReason> getDemandReasonByInstallmentAndModule(Installment installment,Module module);
	
	/**
	 * This method called getEgReasonCategoryByCode gets  List<EgReasonCategory> objects.
	 *
	 * <p>This method returns  List<EgReasonCategory> objects for given code and  module.</p>
	 *
	 * @param java.lang.String code.
	 *
	 * @return  List<EgReasonCategory> objects.
	 *
	 * 
	 */
	public List<EgReasonCategory> getEgReasonCategoryByCode(String code);
	
	/**
	 * This method called getDemandReasonByDemandReasonMaster gets  List<EgDemandReason> objects.
	 *
	 * <p>This method returns  List<EgDemandReason> objects for given code and  module.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemandReasonMaster egDemandReasonMaster.
	 *
	 * @return  List<EgDemandReason> objects.
	 *
	 * 
	 */
	public List<EgDemandReason> getDemandReasonByDemandReasonMaster(EgDemandReasonMaster egDemandReasonMaster);
	
	/**
	 * This method called getDmdReasonByDmdReasonMsterInstallAndMod gets  EgDemandReason object.
	 *
	 * <p>This method returns  EgDemandReason object for given demandReasonMaster , installment  and  module.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemandReasonMaster egDemandReasonMaster.
	 * 
	 * @param org.egov.commons.Installment installment.
	 * 
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  EgDemandReason object.
	 *
	 * 
	 */
	public EgDemandReason getDmdReasonByDmdReasonMsterInstallAndMod(EgDemandReasonMaster demandReasonMaster,Installment installment,Module module);
	
	/**
	 * This method called getDemandDetailsForDemand gets  List<EgDemandDetails> objects.
	 *
	 * <p>This method returns  List<EgDemandDetails> objects for given EgDemand and  EgwStatus.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemand demand.
	 * 
	 * @param org.egov.commons.EgwStatus status.
	 *
	 * @return  List<EgDemandDetails> objects.
	 *
	 * 
	 */
	public List<EgDemandDetails> getDemandDetailsForDemand(EgDemand demand, EgwStatus status) ;
	
	/**
	 * This method called getDemandDetailsForDemandAndReasons gets  List<EgDemandDetails> objects.
	 *
	 * <p>This method returns  List<EgDemandDetails> objects for given EgDemand and List<EgDemandReason> .</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemand demand.
	 * 
	 * @param java.util.List<org.egov.infstr.DCB.model.EgDemandReason> list.
	 *
	 * @return  List<EgDemandDetails> objects.
	 *
	 * 
	 */
	public List<EgDemandDetails>  getDemandDetailsForDemandAndReasons(EgDemand  demand ,List<EgDemandReason> demandReasonList );
	
	/**
	 * This method called getAllBillsForDemand gets  List<EgBill> objects .
	 *
	 * <p>This method returns  List<EgBill> objects for given EgDemand , includeHistory and includeCancelled .</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemand demand.
	 * 
	 * @param java.lang.String includeHistory.
	 * 
	 * @param java.lang.String includeCancelled.
	 *
	 * @return  List<EgBill> objects.
	 *
	 * 
	 */
	public List<EgBill> getAllBillsForDemand(EgDemand demand, String includeHistory, String includeCancelled);
	
	/**
	 * This method called getBillsByBillNumber gets  List<EgBill> objects .
	 *
	 * <p>This method returns  List<EgBill> objects for given BillNo and Module .</p>
	 *
	 * @param org.egov.infstr.commons.Module module.
	 * 
	 * @param java.lang.Long BillNo.
	 *
	 * @return  List<EgBill> objects.
	 *
	 * 
	 */
	public List getBillsByBillNumber(String BillNo,Module module);
	
	
	/** Called to get the EgDemandDetails List For a specific EgDemandReasonMaster with installment and Module.
    * Combination of EgDemandreasonMaster ,Installment and module is unique and  gives a single EgDemandReason.
    * EgDemand reason has one to Many relationship with EgDemandDetails.
    *
    *@param org.egov.demand.model.EgDemand
    *@param org.egov.commons.Installment
    *@param org.egov.infstr.commons.Module
    *@param org.egov.demand.model.EgDemandReasonMaster
    *
    *@return java.util.List<EgDemandDetails>.
    *
    */
   
   public List<EgDemandDetails> getDmdDetailList(EgDemand egDemand,Installment installment,Module module,EgDemandReasonMaster dmdResMster);
   
   public List  getDmdAmtAndCollAmt(EgDemand egDemand,Installment installment);
   
	public List getDCB(EgDemand egDemand,Module module);

	public List getEgDemandReasonMasterIds(EgDemand egDemand);
	
	public List<BillReceipt> getBillReceipts(EgDemand egDemand);
	
	 public List<BillReceipt> getBillReceipts(List<EgDemand> egDemand);
	 
	   /**
		 * getReasonCategoryByCode gets  EgReasonCategory object.
		 *
		 * <p>This method returns  EgReasonCategory object for code.</p>
		 *
		 * @param code.
		 *
		 * @return  EgReasonCategory object.
		 *
		 * 
		 */

		public EgReasonCategory getReasonCategoryByCode(String code);
		public EgDemandReason getEgDemandReasonByCodeInstallmentModule(String demandReasonMasterCode,
				Installment installment, Module module,String egReasonCategoryCode);
		
		/**
	     * Method called to get the balance Amount for the given Demand reason Master Code ,module and demand
	     * Installment will be taken from EgDemand.
	     *   
	     *@param demand - EgDemand Object.
	     *@param dmdReasonMasterCode - Code of the EgDemandReasonMaster 
	     *@param module - EgModule object 
	     *@return java.math.BigDecimal - returns the Balance(Demand - Collection)
	     * 
	     */
		public BigDecimal getBalanceByDmdMasterCode(EgDemand demand, String dmdReasonMasterCode,
				Module module);
		
		/**
	     * Method called to get all EgdmCollectedReceipts with given receiptNumber.
	     *   
	     *@param demand - EgDemand Object.
	     *@param dmdReasonMasterCode - Code of the EgDemandReasonMaster 
	     *@param module - EgModule object 
	     *@return java.math.BigDecimal - returns the Balance(Demand - Collection)
	     * 
	     */
		public List<EgdmCollectedReceipt> getAllEgdmCollectedReceipts(String receiptNo);  
		
		/**
	     * Method called to get the balance Amount for the given Demand reason Master Code ,module, demand
	     * and Installment.
	     *   
	     *@param demand - EgDemand Object.
	     *@param dmdReasonMasterCode - Code of the EgDemandReasonMaster 
	     *@param module - EgModule object 
	     *@param installment - Installment object 
	     *@return java.math.BigDecimal - returns the Balance(Demand - Collection)
	     * 
	     */
		public BigDecimal getBalanceByDmdMasterCodeInst(EgDemand demand, String dmdReasonMasterCode,
				Module module, Installment installment);

}
