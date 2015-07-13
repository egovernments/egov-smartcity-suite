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
/**
 * 
 */
package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Bankaccount;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.model.cheque.AccountCheques;
import org.egov.model.cheque.ChequeDeptMapping;
import org.egov.model.masters.ChequeDetail;
import org.egov.model.voucher.VoucherDetails;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author manoranjan
 *
 */
@Transactional(readOnly=true)
public class AccountChequeAction extends BaseFormAction{
	
	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(AccountChequeAction.class);
	private AccountCheques accountCheques = new AccountCheques();
	private EgovCommon egovCommon;
	private List<ChequeDeptMapping> chequeList ;
	private Bankaccount bankaccount;
	private List<ChequeDetail> chequeDetailsList;
	private PersistenceService<AccountCheques, Long> accChqSer;
	private PersistenceService<ChequeDeptMapping, Long> chqDeptSer;
	private String deletedChqDeptId;
	public AccountChequeAction(){
		
		addRelatedEntity("bankAccountId",Bankaccount.class);
	}
	@Override
	public Object getModel() {
		return accountCheques;
	}
    
	public void prepare(){
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("departmentList", masterCache.get("egi-department"));
	}
	
@Action(value="/masters/accountCheque-newform")
	public String newform(){
		
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("bankList", Collections.EMPTY_LIST);
		addDropdownData("accNumList", Collections.EMPTY_LIST);
		addDropdownData("fundList",  masterCache.get("egi-fund"));
		return "new";
		
	}
	@ValidationErrorPage(value="manipulateCheques")	
	@SuppressWarnings("unchecked")
@Action(value="/masters/accountCheque-manipulateCheques")
	public String manipulateCheques(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("AccountChequeAction | manipulateCheques | Start");
		Integer bankAccId = Integer.valueOf(parameters.get("bankAccId")[0]);
		bankaccount = (Bankaccount)persistenceService.find("from Bankaccount where id = "+bankAccId);
		
		// Get cheque leafs presents for this particular account number
		StringBuffer query = new StringBuffer(200);
		query.append("select cd from ChequeDeptMapping cd where accountCheque.bankAccountId.id =?");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("AccountChequeAction | manipulateCheques | query = "+ query.toString());
		chequeList =(List<ChequeDeptMapping> ) persistenceService.findAllBy(query.toString(), bankAccId);
		if(chequeList.size()>0){
			prepareChequeDetails(chequeList);
		}
		return "manipulateCheques";
	}
	
	private void  prepareChequeDetails( List<ChequeDeptMapping> chequeList ){
		
		chequeDetailsList = new ArrayList<ChequeDetail>();
		ChequeDetail chequeDetail;
		for (ChequeDeptMapping chequeDeptMapping : chequeList) {
			
			chequeDetail = new ChequeDetail();
			chequeDetail.setFromChqNo(chequeDeptMapping.getAccountCheque().getFromChequeNumber());
			chequeDetail.setToChqNo(chequeDeptMapping.getAccountCheque().getToChequeNumber());
			chequeDetail.setDeptName(chequeDeptMapping.getAllotedTo().getName());
			//chequeDetail.setDeptId(chequeDeptMapping.getAllotedTo().getId());
			chequeDetail.setReceivedDate(Constants.DDMMYYYYFORMAT2.format(chequeDeptMapping.getAccountCheque().getReceivedDate()));
			chequeDetail.setSerialNo(chequeDeptMapping.getAccountCheque().getSerialNo());
			if(null != chequeDeptMapping.getAccountCheque().getIsExhausted() && chequeDeptMapping.getAccountCheque().getIsExhausted()){
				chequeDetail.setIsExhusted("Yes");
			}else{
				chequeDetail.setIsExhusted("No");
			}
			
			chequeDetail.setNextChqPresent(chequeDeptMapping.getAccountCheque().getNextChqNo()!=null?"Yes":"No" );
			chequeDetail.setAccountChequeId(chequeDeptMapping.getAccountCheque().getId());
			chequeDetail.setChequeDeptId(chequeDeptMapping.getId());
			chequeDetailsList.add(chequeDetail);
		}
		
	}
	@ValidationErrorPage(value="manipulateCheques")	
	@SuppressWarnings("unchecked")
	public String save(){
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("AccountChequeAction | save | Start");
		Session session =HibernateUtil.getCurrentSession();
		Map<String, AccountCheques> chequeMap = new HashMap<String, AccountCheques>();
		Map<String, String> chequeIdMap = new HashMap<String, String>();
		AccountCheques accountCheques;
		ChequeDeptMapping chqDept;
		removeEmptyRows();
		bankaccount = (Bankaccount)persistenceService.find("from Bankaccount where id ="+Integer.valueOf(parameters.get("bankAccId")[0]) );
		if(null == chequeDetailsList){
			deleteRecords();
			addActionMessage("Cheque Master deleted Successfully : No cheque leafs available");
			return "manipulateCheques";
		}
		for (ChequeDetail chequeDetail : chequeDetailsList) {
			
			// modify the existing cheque that are not used and  insert new cheque leaf.
			if(chequeDetail.getNextChqPresent().equalsIgnoreCase("No") && chequeDetail.getIsExhusted().equalsIgnoreCase("No")){
				
				if(chequeDetail.getAccountChequeId() != null && null == chequeIdMap.get(chequeDetail.getAccountChequeId().toString())){
					session.createQuery("delete from ChequeDeptMapping where accountCheque.id="+chequeDetail.getAccountChequeId()).executeUpdate();
					session.createQuery("delete from AccountCheques where id="+chequeDetail.getAccountChequeId()).executeUpdate();
					chequeIdMap.put(chequeDetail.getAccountChequeId().toString(),chequeDetail.getAccountChequeId().toString());
					
				}
				if(null == chequeMap.get(chequeDetail.getFromChqNo()+chequeDetail.getToChqNo()+chequeDetail.getSerialNo())){
						accountCheques = new AccountCheques(); 
						accountCheques.setBankAccountId(bankaccount);
						accountCheques.setFromChequeNumber(chequeDetail.getFromChqNo());
						accountCheques.setToChequeNumber(chequeDetail.getToChqNo());
						accountCheques.setSerialNo(chequeDetail.getSerialNo());
						try {
							accountCheques.setReceivedDate(Constants.DDMMYYYYFORMAT2.parse(chequeDetail.getReceivedDate()));
						} catch (ParseException e) {
							LOGGER.error("ERROR"+e.getMessage(), e);
					}
						accChqSer.persist(accountCheques);
						chequeMap.put(accountCheques.getFromChequeNumber()+accountCheques.getToChequeNumber()+accountCheques.getSerialNo(), accountCheques);
					}else{
						accountCheques = chequeMap.get(chequeDetail.getFromChqNo()+chequeDetail.getToChqNo()+chequeDetail.getSerialNo());
					}
					chqDept = 	 new ChequeDeptMapping();
					chqDept.setAccountCheque(accountCheques);
					Department dept = (Department)persistenceService.find("from Department where id="+chequeDetail.getDeptId());
					chqDept.setAllotedTo(dept);
					chqDeptSer.persist(chqDept);
			}
			
		} // for loop chequeDetailsList Ends. 
		deleteRecords();
		// Get cheque leafs presents for this particular account number
		StringBuffer query = new StringBuffer(200);
		query.append("select cd from ChequeDeptMapping cd where accountCheque.bankAccountId.id =? ");
		chequeList =(List<ChequeDeptMapping> ) persistenceService.findAllBy(query.toString(), bankaccount.getId());
		if(chequeList.size()>0){
			prepareChequeDetails(chequeList);
		}
		addActionMessage("Cheque Master updated Successfully");
		return "manipulateCheques";
	}
	
	// delete the record from chequedeptmapping and accountcheques if all the departments that are mapped for that cheque leaf is deleted by user.
	private void  deleteRecords(){
		
		Session session =HibernateUtil.getCurrentSession();
		
		if(null != deletedChqDeptId && !deletedChqDeptId.equalsIgnoreCase("")){
			Query qry = session.createQuery("delete from ChequeDeptMapping where id in ("+deletedChqDeptId +")");
			qry.executeUpdate();
		}
		
		// delete the cheque leafs that are not mapped to any department.
		StringBuffer accChqDelquery = new StringBuffer();
		accChqDelquery.append("delete from AccountCheques where id in ( select id from AccountCheques where id not in").
		append("( select ac.id from AccountCheques ac,ChequeDeptMapping cd where ac.id=cd.accountCheque.id ").
		append(" and ac.bankAccountId.id=:bankAccId) and bankAccountId.id=:bankAccId)");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("AccountChequeAction | save | accChqDelquery "+ accChqDelquery.toString());
		Query delqry = session.createQuery(accChqDelquery.toString());
		delqry.setInteger("bankAccId",bankaccount.getId().intValue());
		delqry.executeUpdate();
		
	}

	
	private void  removeEmptyRows(){
		List<ChequeDetail> trash=new ArrayList<ChequeDetail>();
        if(chequeDetailsList!=null)
        {
                for(ChequeDetail cd:chequeDetailsList)
                {
                        if(cd==null)
                        {
                                trash.add(cd);
                        }
                }
        }
        
        for(ChequeDetail cd:trash)
        {
        	chequeDetailsList.remove(cd);
        }
        trash.clear(); 
	}
	public AccountCheques getAccountCheques() {
		return accountCheques;
	}

	public void setAccountCheques(AccountCheques accountCheques) {
		this.accountCheques = accountCheques;
	}
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	public List<ChequeDeptMapping> getChequeList() {
		return chequeList;
	}
	public void setChequeList(List<ChequeDeptMapping> chequeList) {
		this.chequeList = chequeList;
	}
	public Bankaccount getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}
	public List<ChequeDetail> getChequeDetailsList() {
		return chequeDetailsList;
	}
	public void setChequeDetailsList(List<ChequeDetail> chequeDetailsList) {
		this.chequeDetailsList = chequeDetailsList;
	}
	public void setAccChqSer(PersistenceService<AccountCheques, Long> accChqSer) {
		this.accChqSer = accChqSer;
	}
	public void setChqDeptSer(PersistenceService<ChequeDeptMapping, Long> chqDeptSer) {
		this.chqDeptSer = chqDeptSer;
	}
	public String getDeletedChqDeptId() {
		return deletedChqDeptId;
	}
	public void setDeletedChqDeptId(String deletedChqDeptId) {
		this.deletedChqDeptId = deletedChqDeptId;
	}
	

}
