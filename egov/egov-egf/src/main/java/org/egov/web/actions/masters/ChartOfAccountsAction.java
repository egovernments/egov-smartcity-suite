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
package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgfAccountcodePurpose;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.exility.common.TaskFailedException;
@ParentPackage("egov")
@Transactional(readOnly=true)
public class ChartOfAccountsAction extends BaseFormAction{
	private static final long LONG_FOUR = 4l;
	private static final long LONG_TWO = 2l;
	PersistenceService<CChartOfAccounts, Long> chartOfAccountService;
	CChartOfAccounts chartOfAccounts = new CChartOfAccounts();
	List<String> accountDetailTypeList = new ArrayList<String>();
	List<Accountdetailtype> accountDetailType = new ArrayList<Accountdetailtype>();
	private static final Logger	LOGGER	= Logger.getLogger(ChartOfAccountsAction.class);
	
	boolean activeForPosting = false;
	boolean functionRequired = false;
	boolean budgetCheckRequired = false;
	Long coaId;
	Long parentId;
	GenericHibernateDaoFactory genericDao;
	String glCode = "";
	List<CChartOfAccounts> allChartOfAccounts;
	int majorCodeLength = 0;
	int minorCodeLength = 0;
	int subMinorCodeLength = 0;
	int detailedCodeLength = 0;
	EgfAccountcodePurpose accountcodePurpose;
	private String generatedGlcode;
	String newGlcode;
	String parentForDetailedCode="";
	private Map<Long,Integer> glCodeLengths = new HashMap<Long, Integer>(); 
	private boolean updateOnly =false;

	public EgfAccountcodePurpose getAccountcodePurpose() {
		return accountcodePurpose;
	}

	public void setAccountcodePurpose(EgfAccountcodePurpose purposeName) {
		this.accountcodePurpose = purposeName;
	}

	public List<CChartOfAccounts> getAllChartOfAccounts() {
		return allChartOfAccounts;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public boolean isBudgetCheckRequired() {
		return budgetCheckRequired;
	}

	public boolean isFunctionRequired() {
		return functionRequired;
	}
	public void setBudgetCheckRequired(boolean budgetCheckReq) {
		this.budgetCheckRequired = budgetCheckReq;
	}

	public void setFunctionRequired(boolean functionReqd) {
		this.functionRequired = functionReqd;
	}

	public Long getCoaId() {
		return coaId;
	}

	public void setCoaId(Long id) {
		this.coaId = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long id) {
		this.parentId = id;
	}

	public void setActiveForPosting(boolean activeForPosting) {
		this.activeForPosting = activeForPosting;
	}

	public List<String> getAccountDetailTypeList() {
		return accountDetailTypeList;
	}

	public void setAccountDetailTypeList(List<String> accountDetailTypeList) {
		this.accountDetailTypeList = accountDetailTypeList;
	}

	public List<Accountdetailtype> getAccountDetailType() {
		return accountDetailType;
	}

	public void setAccountDetailType(List<Accountdetailtype> accountDetailType) {
		this.accountDetailType = accountDetailType;
	}

	public void setChartOfAccountService(PersistenceService<CChartOfAccounts, Long> chartOfAccountService) {
		this.chartOfAccountService = chartOfAccountService;
	}

	public CChartOfAccounts getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(CChartOfAccounts chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	@Override
	public Object getModel() {
		return chartOfAccounts;
	}
	
	public ChartOfAccountsAction() {
		addRelatedEntity("purpose", AccountCodePurpose.class);
		addRelatedEntity("chartOfAccountDetails.detailTypeId", AccountCodePurpose.class);
	}

	@Override
	public void prepare() {
		super.prepare();
		populateChartOfAccounts();
		populateCodeLength();
		parentForDetailedCode = getAppConfigValueFor("EGF", "parent_for_detailcode");
		populateGlCodeLengths();
		allChartOfAccounts = chartOfAccountService.findAllBy("from CChartOfAccounts where classification=?",Long.valueOf(parentForDetailedCode));
		if(chartOfAccounts!=null){
			if(accountcodePurpose!=null && accountcodePurpose.getId()!=null)
				accountcodePurpose = getPurposeCode(Integer.valueOf(accountcodePurpose.getId()));
		}
		dropdownData.put("purposeList", persistenceService.findAllBy("from EgfAccountcodePurpose order by name"));
		dropdownData.put("accountDetailTypeList", persistenceService.findAllBy("from Accountdetailtype order by name"));
	}

	private void populateAccountDetailTypeList() {
		if(chartOfAccounts.getChartOfAccountDetails() != null){
			for (CChartOfAccountDetail entry : chartOfAccounts.getChartOfAccountDetails()) {
				accountDetailTypeList.add(entry.getDetailTypeId().getId().toString());
			}
		}
	}

	void populateGlCodeLengths() {
		getGlCodeLengths().put(Constants.LONG_ONE, majorCodeLength);
		getGlCodeLengths().put(LONG_TWO, minorCodeLength-majorCodeLength);
		getGlCodeLengths().put(3l, subMinorCodeLength-minorCodeLength);
		if(parentForDetailedCode.equals("2"))
			getGlCodeLengths().put(LONG_FOUR, detailedCodeLength-minorCodeLength);
		else if(parentForDetailedCode.equals("3"))
			getGlCodeLengths().put(LONG_FOUR, detailedCodeLength-subMinorCodeLength);
	}

	private EgfAccountcodePurpose getPurposeCode(Integer id) {
		return ((EgfAccountcodePurpose)persistenceService.find("from EgfAccountcodePurpose where id=?",id));
	}

	private void populateChartOfAccounts() {
		if(chartOfAccounts.getId() != null)
			chartOfAccounts = chartOfAccountService.findById(chartOfAccounts.getId(),false);
	}
	
	@Override
	public String execute() throws Exception {
		return NEW;
	}
	
@Action(value="/masters/chartOfAccounts-view")
	public String view() throws Exception {
		populateAccountCodePurpose();
		populateAccountDetailTypeList();
		populateCoaRequiredFields();
		coaId = chartOfAccounts.getId();
		return Constants.VIEW;
	}
	
	public boolean shouldAllowCreation(){
		return !Long.valueOf("4").equals(chartOfAccounts.getClassification());
	}
	
	public String modify() throws Exception {
		populateAccountDetailTypeList();
		populateCoaRequiredFields();
		populateAccountCodePurpose();
		return Constants.EDIT;
	}

	private void populateCoaRequiredFields() {
		activeForPosting = getIsActiveForPosting();
		functionRequired = getFunctionReqd();
		budgetCheckRequired = budgetCheckReq();
	}

	private void populateAccountCodePurpose() {
		if(chartOfAccounts!= null && chartOfAccounts.getPurposeId()!=null)
			accountcodePurpose = getPurposeCode(Integer.valueOf(chartOfAccounts.getPurposeId()));
	}

	public String update() throws Exception {
		setPurposeOnCoa();
		updateOnly=true;
		populateAccountDetailType();
		chartOfAccounts.setIsActiveForPosting(activeForPosting);
		chartOfAccounts.setFunctionReqd(functionRequired);
		chartOfAccounts.setBudgetCheckReq(budgetCheckRequired);
		if("0".equals(chartOfAccounts.getPurposeId()))
			chartOfAccounts.setPurposeId(null);
		chartOfAccountService.persist(chartOfAccounts);
		saveCoaDetails(chartOfAccounts);
		addActionMessage(getText("chartOfAccount.modified.successfully"));
		clearCache();
		coaId = chartOfAccounts.getId();
		return Constants.VIEW;
	}

	private void setPurposeOnCoa() {
		if(accountcodePurpose!=null && accountcodePurpose.getId()!=null)
			chartOfAccounts.setPurposeId(accountcodePurpose.getId().toString());
		if("0".equals(chartOfAccounts.getPurposeId()))
			chartOfAccounts.setPurposeId(null);
	}

	private void populateAccountDetailType() {
		persistenceService.setType(Accountdetailtype.class);
		for (String row : accountDetailTypeList) {
			accountDetailType.add((Accountdetailtype) persistenceService.find("from Accountdetailtype where id=?", Integer.valueOf(row)));
		}
	}
	
	void deleteAccountDetailType(List<Accountdetailtype> accountDetailType,CChartOfAccounts accounts) {
		String accountDetail = "";
		if(accounts.getChartOfAccountDetails() == null)
			return;
		chartOfAccountService.getSession().flush();
		persistenceService.setType(CChartOfAccountDetail.class);
		try {
			for (Accountdetailtype row : accountDetailType) {
				Iterator<CChartOfAccountDetail> iterator = accounts.getChartOfAccountDetails().iterator();
				while(iterator.hasNext()){
					CChartOfAccountDetail next = iterator.next();
					accountDetail = row.getName();
					if(next == null || next.getDetailTypeId().getId().equals(row.getId())){
						iterator.remove();
						persistenceService.delete(persistenceService.findById(next.getId(), false));
						HibernateUtil.getCurrentSession().flush();
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			populateAccountDetailTypeList();
			String message = accountDetail.concat(" ").concat(e.toString());
			throw new ValidationException(Arrays.asList(new ValidationError(message,message)));
		}
	}
	
	boolean hasReference(Integer id,String glCode){
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery("select * from chartofaccounts c,generalledger gl,generalledgerdetail gd " +
				"where c.glcode='"+glCode+"' and gl.glcodeid=c.id and gd.generalledgerid=gl.id and gd.DETAILTYPEID="+id);
		List list = query.list();
		if(list!=null && list.size() > 0)
			return true;
		return false;
	}

	boolean validAddtition(String glCode){
		StringBuffer strQuery = new StringBuffer();
		strQuery.append("select bd.billid from  eg_billdetails bd, chartofaccounts coa,  eg_billregistermis brm where coa.glcode = '"+glCode+"' and bd.glcodeid = coa.id and brm.billid = bd.billid and brm.voucherheaderid is null "); 
		strQuery.append(" intersect SELECT br.id FROM eg_billregister br, eg_billdetails bd, chartofaccounts coa,egw_status  sts WHERE coa.glcode = '"+glCode+"' AND bd.glcodeid = coa.id AND br.id= bd.billid AND br.statusid=sts.id ");
		strQuery.append(" and sts.id not in (select id from egw_status where upper(moduletype) like '%BILL%' and upper(description) like '%CANCELLED%') ");
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery(strQuery.toString());
		List list = query.list();
		if(list!=null && list.size() > 0)
			return false;
		return true;
	}

	void saveCoaDetails(CChartOfAccounts accounts) {
		List<Accountdetailtype> rowsToBeDeleted = getAccountDetailTypeToBeDeleted(accountDetailType,accounts);
		List<Accountdetailtype> rowsToBeAdded = getAccountDetailTypeToBeAdded(accountDetailType,accounts);
		deleteAccountDetailType(rowsToBeDeleted,accounts);
		if (accountDetailType.size()==1 && rowsToBeAdded.size()==1 && rowsToBeDeleted.size() ==0 && updateOnly) {
			if (!validAddtition(chartOfAccounts.getGlcode()))
			{
				String message = getText("chartOfAccount.accDetail.uncancelled.bills");
				throw new ValidationException(Arrays.asList(new ValidationError(message,message))); 
			}
		}
			for (Accountdetailtype entry : rowsToBeAdded) {
				if ((!coaHasAccountdetailtype(entry, accounts))) {
					CChartOfAccountDetail chartOfAccountDetail = new CChartOfAccountDetail();
					chartOfAccountDetail.setDetailTypeId(entry);
					chartOfAccountDetail.setGlCodeId(accounts);
					accounts.getChartOfAccountDetails().add(chartOfAccountDetail);
				}
			}
		chartOfAccountService.persist(accounts);
		chartOfAccountService.getSession().flush();
	}

	List<Accountdetailtype> getAccountDetailTypeToBeDeleted(List<Accountdetailtype> accountDetailType,CChartOfAccounts accounts) {
		List<Accountdetailtype> rowsToBeDeleted = new ArrayList<Accountdetailtype>();
		for (CChartOfAccountDetail entry : accounts.getChartOfAccountDetails()) {
			if(accountDetailType!=null && accountDetailType.isEmpty())
				rowsToBeDeleted.add(entry.getDetailTypeId());
			else if(!accountDetailTypeContains(accountDetailType, entry.getDetailTypeId())){
				rowsToBeDeleted.add(entry.getDetailTypeId());
			}
		}
		return rowsToBeDeleted;
	}

	List<Accountdetailtype> getAccountDetailTypeToBeAdded(List<Accountdetailtype> accountDetailType,CChartOfAccounts accounts) {
		List<Accountdetailtype> rowsToBeAdded = new ArrayList<Accountdetailtype>();
		for (Accountdetailtype row : accountDetailType) {
			if(!coaHasAccountdetailtype(row, accounts))
				rowsToBeAdded.add(row);
		}
		return rowsToBeAdded;
	}
	private boolean coaHasAccountdetailtype(Accountdetailtype entry, CChartOfAccounts accounts) {
		for (CChartOfAccountDetail row : accounts.getChartOfAccountDetails()) {
			if(row.getDetailTypeId().getId().equals(entry.getId()))
				return true;
		}
		return false;
	}
	
	private boolean accountDetailTypeContains(List<Accountdetailtype> list,Accountdetailtype entry) {
		for (Accountdetailtype row : list) {
			if(row.getId().equals(entry.getId())){
				return true;
			}
		}
		return false;
	}

@Action(value="/masters/chartOfAccounts-addNew")
	public String addNewCoa() throws Exception {
		chartOfAccounts = new CChartOfAccounts();
		if(parentId != null)
			chartOfAccounts.setParentId(parentId);
		CChartOfAccounts parent = chartOfAccountService.findById(parentId, false);
		chartOfAccounts.setType(parent.getType());
		setClassification(parent);
		Long glCode = findNextGlCode(parent);
		if(parent.getClassification() == null || parent.getClassification() == 0)
			generatedGlcode = "";
		else
			generatedGlcode = parent.getGlcode();
		if(glCode == null){
			populateGlcode();
			newGlcode = chartOfAccounts.getGlcode();
		}else{
			newGlcode = String.valueOf(glCode+1);
			if(chartOfAccounts.getClassification().equals(LONG_TWO))
				newGlcode = newGlcode.substring(majorCodeLength, newGlcode.length());
			else if(chartOfAccounts.getClassification().equals(3l))
				newGlcode = newGlcode.substring(minorCodeLength, newGlcode.length());
			else if(chartOfAccounts.getClassification().equals(LONG_FOUR)){
				extractDetailCode();
			}
		}
		return NEW;
	}

	private Long findNextGlCode(CChartOfAccounts parentCoa) {
		return (Long)persistenceService.find("select max(cast(glcode,long)) from CChartOfAccounts where parentId=?",parentCoa.getId());
	}

	void setClassification(CChartOfAccounts parentCoa){
		if(parentCoa.getClassification() == null)
			chartOfAccounts.setClassification(Constants.LONG_ONE);
		else if(Constants.LONG_ZERO.equals(parentCoa.getClassification()))
			chartOfAccounts.setClassification(Constants.LONG_ONE);
		else if(Constants.LONG_ONE.equals(parentCoa.getClassification()))
			chartOfAccounts.setClassification(LONG_TWO);
		else if(parentCoa.getClassification().equals(LONG_TWO)){
			if(parentForDetailedCode.equals("2"))
				chartOfAccounts.setClassification(LONG_FOUR);
			else
				chartOfAccounts.setClassification(3l);
		}
		else if(parentCoa.getClassification().equals(3l))
			chartOfAccounts.setClassification(LONG_FOUR);
	}
	
	String getAppConfigValueFor(String module,String key){
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key).get(0).getValue();
	}

	void populateCodeLength() {
		majorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		subMinorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_subminorcode_length"));
		detailedCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_detailcode_length"));
	}

	void populateGlcode() {
		chartOfAccounts.setGlcode(StringUtils.leftPad("",glCodeLengths.get(chartOfAccounts.getClassification()),'0'));
	}
	
	public String save() throws Exception {
		if(generatedGlcode == null || newGlcode == null) {
			addActionMessage(getText("chartOfAccount.invalid.glcode"));
			return NEW;
		}
		CChartOfAccounts coa = chartOfAccountService.find("from CChartOfAccounts where glcode=?",generatedGlcode.concat(newGlcode));
		if(coa != null){
			addActionMessage(getText("chartOfAccount.glcode.already.exists"));
			return NEW;
		}
		chartOfAccounts.setGlcode(generatedGlcode.concat(newGlcode));
		if("0".equals(chartOfAccounts.getPurposeId()))
			chartOfAccounts.setPurposeId(null);
		if(parentId != null){
			CChartOfAccounts parent = chartOfAccountService.findById(parentId,false);
			chartOfAccounts.setParentId(parentId);
			chartOfAccounts.setType(parent.getType());
		}
		setPurposeOnCoa();
		chartOfAccounts.setIsActiveForPosting(activeForPosting);
		chartOfAccounts.setBudgetCheckReq(budgetCheckRequired);
		chartOfAccounts.setFunctionReqd(functionRequired);
		populateAccountDetailType();
		chartOfAccounts.setMajorCode(chartOfAccounts.getGlcode().substring(0,majorCodeLength));
		chartOfAccountService.persist(chartOfAccounts);
		saveCoaDetails(chartOfAccounts);
		addActionMessage(getText("chartOfAccount.saved.successfully"));
		clearCache();
		reset();
		return NEW;
	}

	private void reset() {
		activeForPosting = false;
		budgetCheckRequired = false;
		functionRequired = false;
		generatedGlcode = "";
		newGlcode = "";
		chartOfAccounts = new CChartOfAccounts();
	}
	
	public boolean isActiveForPosting(){
		return activeForPosting;
	}
	public boolean budgetCheckReq(){
		if(chartOfAccounts != null && Constants.LONG_ONE.equals(chartOfAccounts.getBudgetCheckReq()))
			return true;
		return false;
	}
	public boolean getFunctionReqd(){
		if(chartOfAccounts != null && Constants.LONG_ONE.equals(chartOfAccounts.getFunctionReqd()))
			return true;
		return false;
	}
	public boolean getIsActiveForPosting(){
		if(chartOfAccounts != null && Constants.LONG_ONE.equals(chartOfAccounts.getIsActiveForPosting()))
			return true;
		return false;
	}

@Action(value="/masters/chartOfAccounts-detailed")
	public String detailed() throws Exception {
		allChartOfAccounts = chartOfAccountService.findAllBy("from CChartOfAccounts where classification=4");
		return "detailed-code";
	}
	
	public String searchAndModify() throws Exception {
		if(glCode != null){
			chartOfAccounts = chartOfAccountService.find("from CChartOfAccounts where classification=4 and glcode=?",glCode.split("-")[0]);
			if(chartOfAccounts == null){
				addActionMessage(getText("charOfAccount.no.record"));
				return detailed();
			}
			populateAccountDetailTypeList();
			populateCoaRequiredFields();
			populateAccountCodePurpose();
			return modify();
		}else{
			addActionMessage(getText("charOfAccount.no.record"));
			return detailed();
		}
	}
	
	public String searchAndView() throws Exception {
		if(glCode != null){
			chartOfAccounts = chartOfAccountService.find("from CChartOfAccounts where classification=4 and glcode=?",glCode.split("-")[0]);
			if(chartOfAccounts == null){
				addActionMessage(getText("charOfAccount.no.record"));
				return detailed();
			}
			coaId = chartOfAccounts.getId();
			populateAccountDetailTypeList();
			populateCoaRequiredFields();
			populateAccountCodePurpose();
			return Constants.VIEW;
		}else{
			addActionMessage(getText("charOfAccount.no.record"));
			return detailed();
		}
	}
@Action(value="/masters/chartOfAccounts-addNew")
	public String addNew() throws Exception {
		populateCodeLength();
		chartOfAccounts = new CChartOfAccounts();
		chartOfAccounts.setClassification(4l);
		return "detailed";
	}

	public String create() throws Exception {
		if(glCode != null){
			CChartOfAccounts parent =  chartOfAccountService.find("from CChartOfAccounts where glcode=?",glCode.split("-")[0]);
			if(parent == null){
				addActionMessage(getText("chartOfAccount.no.data"));
				return detailed();
			}
			if(generatedGlcode == null || newGlcode == null) {
				addActionMessage(getText("chartOfAccount.invalid.glcode"));
				return "detailed";
			}
			CChartOfAccounts coa = chartOfAccountService.find("from CChartOfAccounts where glcode=?",generatedGlcode.concat(newGlcode));
			if(coa != null){
				addActionMessage(getText("chartOfAccount.glcode.already.exists"));
				return "detailed";
			}
			parentId = parent.getId();
			chartOfAccounts.setParentId(parentId);
			chartOfAccounts.setBudgetCheckReq(budgetCheckRequired);
			chartOfAccounts.setFunctionReqd(functionRequired);
			chartOfAccounts.setType(parent.getType());
			setClassification(parent);
			chartOfAccounts.setGlcode(generatedGlcode.concat(newGlcode));
			chartOfAccounts.setMajorCode(chartOfAccounts.getGlcode().substring(0,majorCodeLength));          
			setPurposeOnCoa();
			chartOfAccounts.setIsActiveForPosting(activeForPosting);
			populateAccountDetailType();
			chartOfAccountService.persist(chartOfAccounts);
			saveCoaDetails(chartOfAccounts);
			addActionMessage(getText("chartOfAccount.detailed.saved"));
		}else{
			addActionMessage(getText("chartOfAccount.no.data"));
		}
		clearCache();
		return detailed();
	}

	public void setGeneratedGlcode(String generatedGlcode) {
		this.generatedGlcode = generatedGlcode;
	}

	public String getGeneratedGlcode() {
		return generatedGlcode;
	}

	public void setNewGlcode(String newGlcode) {
		this.newGlcode = newGlcode;
	}

	public String getNewGlcode() {
		return newGlcode;
	}
	
@Action(value="/masters/chartOfAccounts-ajaxNextGlCode")
	public String ajaxNextGlCode(){
		String parentGlcode = parameters.get("parentGlcode")[0];
		if(parentGlcode!=null || !StringUtils.isBlank(parentGlcode)){
			CChartOfAccounts coa = chartOfAccountService.find("from CChartOfAccounts where glcode=?",parentGlcode);
			Long glCode = findNextGlCode(coa);
			if(glCode == null){
				populateGlcode();
				newGlcode = chartOfAccounts.getGlcode();
			}else{
				newGlcode = String.valueOf(glCode+1);
				extractDetailCode();
			}
		}
		return "generated-glcode";
	}

	void extractDetailCode() {
		if(parentForDetailedCode.equals("2"))
			newGlcode = newGlcode.substring(minorCodeLength, newGlcode.length());
		else
			newGlcode = newGlcode.substring(subMinorCodeLength, newGlcode.length());
	}

	public Map<Long,Integer> getGlCodeLengths() {
		return glCodeLengths;
	}
	
	void clearCache(){
		try {
			ChartOfAccounts.getInstance().reLoadAccountData();
		} catch (TaskFailedException e) {
			
			LOGGER.error("Error"+e.getMessage(),e);
		}
	}
}
