package org.egov.erpcollection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Fund;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;

public class AjaxBankRemittanceAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	private static final String BANKBRANCHLIST = "bankBranchList";
	private static final String ACCOUNTLIST = "accountList";
	private String serviceName;
	private String fundName;
	private static final String USERLIST = "userList";
	private static final String DESIGNATIONLIST = "designationList";
	private Integer designationId;
	private Integer approverDeptId;
	private List<EmployeeView> postionUserList = new ArrayList<EmployeeView>();
	private List<DesignationMaster> designationMasterList = new ArrayList<DesignationMaster>();
	private CollectionsUtil collectionsUtil;
	
	/**
	 * A <code>Long</code> representing the fund id. The fund id is arriving from 
	 * the miscellanoeus receipt screen
	 * 
	 */
	private Integer fundId;
	private Integer branchId;
	private final List<Bankbranch> bankBranchArrayList=new ArrayList<Bankbranch>();
	private List<Bankaccount> bankAccountArrayList;
	
	public String bankBranchList() {
		if(getFundId()!=null){
			Fund fund = (Fund) persistenceService.find("from Fund where id=?",fundId);
			if(fund==null){
				throw new ValidationException(Arrays.asList(new ValidationError("fund.not.found","Fund information not available")));
			}
			setFundName(fund.getName());
		}
		String bankBranchQueryString="select distinct(bb.id) as branchid,b.NAME||'-'||bb.BRANCHNAME as branchname from BANK b,BANKBRANCH bb, BANKACCOUNT ba," +
		"EG_BANKACCOUNTSERVICEMAPPING asm,EG_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNTID=ba.ID and asm.SERVICEID=sd.ID and " +
		"ba.BRANCHID=bb.ID and bb.BANKID=b.ID and fd.ID=ba.FUNDID and sd.SERVICENAME='"+serviceName+"' and fd.NAME='"+getFundName()+"'";

		Query bankBranchQuery=HibernateUtil.getCurrentSession().createSQLQuery(bankBranchQueryString);
		List<Object[]> queryResults=bankBranchQuery.list();
				
		for(int i=0;i<queryResults.size();i++)
		{	
			Object[] arrayObjectInitialIndex=queryResults.get(i);
			Bankbranch newBankbranch=new Bankbranch();
			newBankbranch.setId(Integer.valueOf(arrayObjectInitialIndex[0].toString()));
			newBankbranch.setBranchname(arrayObjectInitialIndex[1].toString());
			bankBranchArrayList.add(newBankbranch);
		}
		return BANKBRANCHLIST;
	}
	
	public Object getModel() {
		return null;
	}

	public String accountList() {
		if(fundId!=null && fundId!=-1){
			Fund fund = (Fund) persistenceService.find("from Fund where id=?",fundId);
			if(fund==null){
				throw new ValidationException(Arrays.asList(new ValidationError("fund.not.found","Fund information not available")));
			}
			setFundName(fund.getName());
		}
		String bankAccountQueryString="select ba.id as accountid,ba.accountnumber as accountnumber from BANKACCOUNT ba," +
		"EG_BANKACCOUNTSERVICEMAPPING asm,EG_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNTID=ba.ID and asm.SERVICEID=sd.ID and fd.ID=ba.FUNDID and " +
		"ba.BRANCHID="+branchId+" and sd.SERVICENAME='"+serviceName+"' and fd.NAME='"+fundName+"'";
		
		Query bankAccountQuery=HibernateUtil.getCurrentSession().createSQLQuery(bankAccountQueryString);
		List<Object[]> queryResults=bankAccountQuery.list();
		
		bankAccountArrayList=new ArrayList<Bankaccount>();
		for(int i=0;i<queryResults.size();i++)
		{	
			Object[] arrayObjectInitialIndex=queryResults.get(i);
			Bankaccount newBankaccount=new Bankaccount();
			newBankaccount.setId(Integer.valueOf(arrayObjectInitialIndex[0].toString()));
			newBankaccount.setAccountnumber(arrayObjectInitialIndex[1].toString());
			getBankAccountArrayList().add(newBankaccount);
		}
		
		return ACCOUNTLIST;
		 
	}
	
	public String positionUserList() {
		if(designationId!=null && approverDeptId!=null)
		{	try
			{
				postionUserList=collectionsUtil.getPositionBySearchParameters(null,designationId,approverDeptId,null,null,new Date(),0);
			}	
			catch(NoSuchObjectException e){
				throw new EGOVRuntimeException("Designation Postion not found",e);
			}	
		}
		return USERLIST;
		 
	}
	
	public String approverDesignationList(){
		if(approverDeptId!=null)
		{
			
			designationMasterList=collectionsUtil.getDesignationsAllowedForBankRemittanceApproval(approverDeptId);
		}	
		
		return DESIGNATIONLIST;
	}
	
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	/**
	 * @param branchId the branchId to set
	 */
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the bankBranchArrayListList
	 */
	public List getBankBranchArrayList() {
		return bankBranchArrayList;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the bankAccountArrayList
	 */
	public List<Bankaccount> getBankAccountArrayList() {
		return bankAccountArrayList;
	}

	/**
	 * @return the fundName
	 */
	public String getFundName() {
		return fundName;
	}

	/**
	 * @param fundName the fundName to set
	 */
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	
	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}

	/**
	 * @param designationId the designationId to set
	 */
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	/**
	 * @return the approverDeptId
	 */
	public Integer getApproverDeptId() {
		return approverDeptId;
	}

	/**
	 * @param approverDeptId the approverDeptId to set
	 */
	public void setApproverDeptId(Integer approverDeptId) {
		this.approverDeptId = approverDeptId;
	}

	/**
	 * @return the postionUserList
	 */
	public List<EmployeeView> getPostionUserList() {
		return postionUserList;
	}

	/**
	 * @return the designationMasterList
	 */
	public List<DesignationMaster> getDesignationMasterList() {
		return designationMasterList;
	}

	/**
	 * @param collectionsUtil the collectionsUtil to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

}
