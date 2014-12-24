/**
 *
 */
package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Functionary;
import org.egov.commons.Fundsource;
import org.egov.commons.Relation;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.egf.bills.model.Cbill;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.service.RelationService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.masters.model.AccountEntity;
import org.egov.model.bills.EgBillSubType;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.HibernateException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author msahoo
 *
 */
public class CommonAction extends BaseFormAction{


	private static final Logger	LOGGER	= Logger.getLogger(CommonAction.class);
	private static final long serialVersionUID = 1L;
	private Integer fundId;
	private Integer schemeId;
	private Integer bankId;
	private List<Map<String, Object>> bankBranchList;
	private Integer  branchId;
	private Integer departmentId;
	private Integer bankaccountId;
	private String chequeNumber;
	private List<Bankaccount> accNumList ;
	private String value;
	private List<Scheme> schemeList;
	private List<SubScheme> subSchemes;
	private List<Bankbranch> branchList;
	private String type;
	private ArrayList<Map<String, String>> nameList;
	private InstrumentService instrumentService;
	private List<String> detailCodes = new ArrayList<String>();
	private  List<UserImpl> userList;
	private Integer  designationId;
	private VoucherService voucherService;
	private String functionaryName;
	private EgovCommon egovCommon;
	private List<CChartOfAccounts>	accountCodesForDetailTypeList;
	private List<EntityType>	entitiesList;
	private Integer accountDetailType;
	private Integer	billSubtypeId;
	private String billType;
	private String searchType;
	private GenericHibernateDaoFactory genericDao;
	private List<AppConfigValues>	checkList;
	private RelationService relationService;
	private String accountDetailTypeName;
	private String typeOfAccount;
	private Date asOnDate;
	private String scriptName;
	private Long recoveryId;
	private Integer subSchemeId;
	private List<Fundsource> fundSouceList;
	private  List<Map<String, Object>> designationList;
	private String	startsWith;
	private FinancingSourceService financingSourceService;
	private String defaultDepartment;
	private Long billRegisterId;

	public Long getBillRegisterId() {
		return billRegisterId;
	}
	public void setBillRegisterId(Long billRegisterId) {
		this.billRegisterId = billRegisterId;
	}
	public void setRelationService(RelationService relationService) {
		this.relationService = relationService;
	}
	public CommonAction()
	{
	}
	public Object getModel() {

		return null;
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadSchemes()
	{
		LOGGER.debug("Fund Id received is : " + fundId);
		if (null == fundId) {
			schemeList = getPersistenceService().findAllBy(
					" from Scheme where fund.id=? and isActive=1 order by name", -1);
		} else {
			schemeList = getPersistenceService().findAllBy(" from Scheme where fund.id=? and isActive=1 order by name",fundId);
		}
		LOGGER.debug("Scheme List size : " + schemeList.size());
		return "schemes";
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadSubSchemes()
	{
		LOGGER.debug("Scheme Id received is : "+schemeId);
		if(null != schemeId && schemeId !=-1){
			subSchemes = getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=1 order by name", schemeId);
			LOGGER.debug("Subscheme List size : "+subSchemes.size());
		}else{
			subSchemes = Collections.EMPTY_LIST;
		}

		return Constants.SUBSCHEMES;
	}

	@SuppressWarnings("unchecked")
	//@Deprecated
	public String ajaxLoadBanks(){

		LOGGER.debug("CommonAction | ajaxLoadBanks");

		try {
			List<Object[]> bankBranch = (List<Object[]>)getPersistenceService().findAllBy("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname " +
					" FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount " +
					" where  bank.isactive='1'  and bankBranch.isactive='1' and bankaccount.isactive='1'  and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id" +
					" and bankaccount.fund.id=?",fundId);


			LOGGER.debug("Bank list size is "+ bankBranch.size());
			bankBranchList = new ArrayList<Map<String, Object>>();
			Map<String, Object> bankBrmap ;
			for(Object[] element : bankBranch){
				bankBrmap = new HashMap<String, Object> ();
				bankBrmap.put("bankBranchId", element[0].toString());
				bankBrmap.put("bankBranchName", element[1].toString());
				bankBranchList.add(bankBrmap);
			}

		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));

		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}


		return "bank";

	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadBanksByFundAndType(){

		LOGGER.debug("CommonAction | ajaxLoadBanks");
		int index = 0;
		String [] strArray = null;
		StringBuffer query = new StringBuffer();
		query.append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname ")
		.append("FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount where  bank.isactive='1'  and bankBranch.isactive='1' and ")
		.append(" bankaccount.isactive='1' and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id ");
		if(fundId!=null)
		{
		query.append("and bankaccount.fund.id=? and bankaccount.type in(");
		}else
		{
			query.append("and bankaccount.type in(");
		}
		if(typeOfAccount.indexOf(",") != -1){
			strArray = typeOfAccount.split(",");
			for (String type : strArray) {
				index++;
				query.append("'").append(type).append("'");
				if(strArray.length > index ){
					query.append(",");
				}

			}
		}else{
			query.append("'").append(typeOfAccount).append("'");
		}

		query.append(")");
		try {
			List<Object[]> bankBranch=null;
			if(fundId!=null)
			{
				bankBranch = (List<Object[]>)getPersistenceService().findAllBy(query.toString(),fundId);
			}else
			{
				 bankBranch = (List<Object[]>)getPersistenceService().findAllBy(query.toString());
			}
			LOGGER.debug("Bank list size is "+ bankBranch.size());
			bankBranchList = new ArrayList<Map<String, Object>>();
			Map<String, Object> bankBrmap ;
			for(Object[] element : bankBranch){
				bankBrmap = new HashMap<String, Object> ();
				bankBrmap.put("bankBranchId", element[0].toString());
				bankBrmap.put("bankBranchName", element[1].toString());
				bankBranchList.add(bankBrmap);
			}

		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));

		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}


		return "bank";

	}
	@SuppressWarnings("unchecked")
	@Deprecated
	public String ajaxLoadAccNum(){
		LOGGER.debug("CommonAction | ajaxLoadAccNum");
		try {

			accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and ba.bankbranch.bank.id=? and isactive='1' order by id",branchId,bankId);
			LOGGER.debug("Bank account Number list size =  "+ accNumList.size());
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting bank account numbers "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		catch (Exception e) {
			LOGGER.error("Exception occured while getting bank account numbers "+e.getMessage(),new HibernateException(e.getMessage()));
		}

		return "bankAccNum" ;
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadAccountNumbers(){
		LOGGER.debug("CommonAction | ajaxLoadAccountNumbers");
		try {
			if(fundId!=null && fundId!=-1 && fundId!=0)
				accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and fund.id=? and isactive='1' order by id",branchId,fundId);
			else
				accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and isactive='1' order by id",branchId);
			LOGGER.debug("Bank account Number list size =  "+ accNumList.size());
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting bank account numbers "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		catch (Exception e) {
			LOGGER.error("Exception occured while getting bank account numbers "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		return "bankAccNum" ;
	}

	public String ajaxLoadAccNumAndType(){
		LOGGER.debug("CommonAction | ajaxLoadAccNum");
		try {
			LOGGER.debug("typeOfAccount in  ajaxLoadBankAccounts method >>>>>>>" +typeOfAccount);
			if(typeOfAccount != null && !typeOfAccount.equals("")) {
				if(typeOfAccount.indexOf(",") !=  -1 ) {
					String [] strArray = typeOfAccount.split(",");
					if(fundId!=null)
					{
					accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive='1' and type in (?, ?) order by id",branchId,fundId,bankId, (String)strArray[0], (String)strArray[1]);
					}else
					{
						accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and  ba.bankbranch.bank.id=? and isactive='1' and type in (?, ?) order by id",branchId,bankId, (String)strArray[0], (String)strArray[1]);
					}
				} else {
					if(fundId!=null)
					{
					accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive='1' and type in (?) order by id",branchId,fundId,bankId,typeOfAccount);
					}else
					{
						accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=?  and ba.bankbranch.bank.id=? and isactive='1' and type in (?) order by id",branchId,bankId,typeOfAccount);
					}
				}
			} else {
				if(fundId!=null)
				{
				accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive='1' order by id",branchId,fundId,bankId);
				}
				else
				{
					accNumList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=?  and ba.bankbranch.bank.id=? and isactive='1' order by id",branchId,bankId);
				}
			}
			LOGGER.debug("Bank account Number list size =  "+ accNumList.size());
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting bank account numbers "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		catch (Exception e) {
			LOGGER.error("Exception occured while getting bank account numbers "+e.getMessage(),new HibernateException(e.getMessage()));
		}

		return "bankAccNumAndType" ;
	}
	@SuppressWarnings("unchecked")
	public String loadAccNumNarration(){
		LOGGER.debug("CommonAction | loadAccNumNarration");
		try {
			value="";
			String accountNumId = parameters.get("accnum")[0];
			LOGGER.debug("Bank account number id received = "+accountNumId);
			value = (String)getPersistenceService().find("select narration from Bankaccount where id=?",Integer.valueOf(accountNumId));
			LOGGER.debug("Naration value = "+value);
		}catch (HibernateException e) {
			LOGGER.error("Exception occured while getting bank account narration "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		catch (Exception e) {
			LOGGER.error("Exception occured while getting bank account narration "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		return "result";
	}
	public String loadAccNumNarrationAndFund(){
		LOGGER.debug("CommonAction | loadAccNumNarration");
		try {
			value="";
			String accountNumId = parameters.get("accnum")[0];
			LOGGER.debug("Bank account number id received = "+accountNumId);
			value = (String)getPersistenceService().find("select concat(concat(narration,'-'),fund.id) from Bankaccount where id=?",Integer.valueOf(accountNumId));
			LOGGER.debug("Naration value = "+value);
		}catch (HibernateException e) {
			LOGGER.error("Exception occured while getting bank account narration "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		catch (Exception e) {
			LOGGER.error("Exception occured while getting bank account narration "+e.getMessage(),new HibernateException(e.getMessage()));
		}
		return "result";
	}
	public String getDetailType() throws Exception{
		LOGGER.debug("Inside getDetailType method");
		value="";
		String accountCode = parameters.get("accountCode")[0];
		String index = parameters.get("index")[0];
		LOGGER.debug("Account code selected is : = " +accountCode);
		LOGGER.debug("index is : = " +index);
		List<Accountdetailtype> list = getPersistenceService().findAllBy(" from Accountdetailtype" +
				" where id in (select detailTypeId from CChartOfAccountDetail where glCodeId=(select id from CChartOfAccounts where glcode=?))  ", accountCode);
		LOGGER.debug(" list :"+list);
		for(Accountdetailtype accountdetailtype :list)
		{
			value= value+index+"~"+accountdetailtype.getDescription()+"~"+accountdetailtype.getId().toString()+"#";
		}
		if(!value.equals(""))
			value=value.substring(0, value.length()-1);

		LOGGER.debug("The Detail type Id is :"+value);
		return "result";
}
	public String ajaxLoadBankBranch()
	{
		try
		{
			branchList =  (List<Bankbranch>)persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund.id=? ) and br.isactive='1' order by br.bank.name asc",fundId);
		}catch(Exception e)
		{
			LOGGER.error("Exception while loading ajaxLoadBankBranch="+e.getMessage());
		}
		return "branch";
	}
	public String ajaxLoadBankAccounts()
	{
		try
		{
			LOGGER.debug("typeOfAccount in  ajaxLoadBankAccounts method >>>>>>>" +typeOfAccount);
			if(typeOfAccount != null && !typeOfAccount.equals("")) {
				if(typeOfAccount.indexOf(",") !=  -1 ) {
					String [] strArray = typeOfAccount.split(",");
					accNumList =  (List<Bankaccount>)persistenceService.findAllBy(" from Bankaccount where fund.id=? and bankbranch.id=? and isactive='1'  and type in (?,?) ", fundId,branchId, (String)strArray[0], (String)strArray[1]);
				} else {
					accNumList =  (List<Bankaccount>)persistenceService.findAllBy(" from Bankaccount where fund.id=? and bankbranch.id=? and isactive='1'  and type in (?) ", fundId,branchId, typeOfAccount);
				}
			} else {
				accNumList =  (List<Bankaccount>)persistenceService.findAllBy(" from Bankaccount where fund.id=? and bankbranch.id=? and isactive='1' ",fundId,branchId);
			}
		}catch(Exception e)
		{
			LOGGER.error("Exception while loading ajaxLoadBankAccounts="+e.getMessage());
		}
		return "bankAccNum" ;
	}

	public String ajaxLoadBankAccountsBySubscheme(){

		SubScheme subScheme = (SubScheme)persistenceService.find("from SubScheme where id = "+subSchemeId);
		fundId = subScheme.getScheme().getFund().getId();
		String [] strArray = typeOfAccount.split(",");
		accNumList =  (List<Bankaccount>)persistenceService.findAllBy(" from Bankaccount where fund.id=? and isactive='1'  and type in (?,?) ", fundId, (String)strArray[0], (String)strArray[1]);
		return "bankAccNum" ;
	}
	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}
	public String ajaxValidateDetailCode()
	{
		LOGGER.debug("Inside ajaxValidateDetailCode method");
		String code = parameters.get("code")[0];
		String index = parameters.get("index")[0];
		try{


		Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(" from Accountdetailtype where id=?",Integer.valueOf(parameters.get("detailtypeid")[0]));
		if(adt==null)
		{
			value=index+"~"+ERROR;
			return "result";
		}
		if(adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE"))
		{
			PersonalInformation information = (PersonalInformation) getPersistenceService().find(" from PersonalInformation where employeeCode=? and isActive=1", code);
			if(information==null)
				value=index+"~"+ERROR;
			else
				value=index+"~"+information.getIdPersonalInformation()+"~"+information.getEmployeeFirstName();
		}
		else if(adt.getTablename().equalsIgnoreCase("RELATION"))
		{
			Relation relation = (Relation) getPersistenceService().find(" from Relation where code=? and isactive='1'", code);
			if(relation==null)
				value=index+"~"+ERROR;
			else
				value=index+"~"+relation.getId()+"~"+relation.getName();
		}
		else if(adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER"))
		{
			AccountEntity accountEntity = (AccountEntity) getPersistenceService().find(" from AccountEntity where code=? and isactive='1' ", code);
			if(accountEntity==null)
				value=index+"~"+ERROR;
			else
				value=index+"~"+accountEntity.getId()+"~"+accountEntity.getCode();
		}
		}catch (HibernateException e) {
			LOGGER.debug("Exception occuerd while getting detail code "+e.getMessage());
			value=index+"~"+ERROR;
		}
		catch (Exception e) {
			LOGGER.debug("Exception occuerd while getting detail code "+e.getMessage());
			value=index+"~"+ERROR;
		}
		return "result";
	}
	public String getDetailCode() throws Exception{
		value="";
		String arr[] = parameters.get("accountCodes")[0].split(",");
		List<String> list = Arrays.asList(arr); // remove duplicate account codes.
        Set<String> set = new HashSet<String>(list);
        String[] accountCodes = new String[set.size()];
        set.toArray(accountCodes);
		for (int i = 0; i < accountCodes.length; i++) {

			CChartOfAccountDetail  chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(" from CChartOfAccountDetail" +
					" where glCodeId=(select id from CChartOfAccounts where glcode=?)", accountCodes[i]);

			if(null != chartOfAccountDetail){
				if(value.trim().length()!=0){
					value=value+"~"+accountCodes[i]+"~"+chartOfAccountDetail.getGlCodeId().getId().toString();
				}else {
					value=accountCodes[i]+"~"+chartOfAccountDetail.getGlCodeId().getId().toString();
				}

			}

		}/*if(values.trim().length()!=0){
			values=index+"~"+values;
		}*/
		LOGGER.debug("The account Detail  codes are :"+value);
		return "result";
}

	public String ajaxGetDetailCode(){
		String index = parameters.get("index")[0];
		try{
			Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(" from Accountdetailtype where id=?",Integer.valueOf(parameters.get("detailtypeid")[0]));
			if(adt==null){
				value=index+"~"+ERROR;
				return "result";
			}
			if(adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE")){
				List<PersonalInformation> information = getPersistenceService().findAllBy("from PersonalInformation where isActive=1 order by employeeCode");
				if(information==null)
					value=index+"~"+ERROR;
				else{
					for (PersonalInformation personalInformation : information) {
						detailCodes.add(personalInformation.getIdPersonalInformation()+"-"+personalInformation.getEmployeeFirstName());
					}
				}
			}
			else if(adt.getTablename().equalsIgnoreCase("RELATION")){
				List<Relation> relation = getPersistenceService().findAllBy("from Relation where isactive='1' order by code");
				if(relation==null)
					value=index+"~"+ERROR;
				else{
					for (Relation rel : relation) {
						detailCodes.add(rel.getId()+"-"+rel.getName());
					}
				}
			}
			else if(adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER")){
				List<AccountEntity> accountEntity = getPersistenceService().findAllBy(" from AccountEntity where isactive='1' order by code");
				if(accountEntity==null)
					value=index+"~"+ERROR;
				else{
					for (AccountEntity rel : accountEntity) {
						detailCodes.add(rel.getId()+"-"+rel.getCode());
					}
				}
			}
		}catch (Exception e) {
			LOGGER.debug("Exception occuerd while getting detail code "+e.getMessage());
			value=index+"~"+ERROR;
		}
		return "detailedCodes";
	}
	public String ajaxLoadVoucherNames() {
			List<Object> voucherNameList = getPersistenceService().findAllBy("select  distinct name from  CVoucherHeader where type=?",type);
			nameList=new ArrayList<Map<String,String>>();
			Map <String,String> voucherNamesMap;
			for(Object voucherName: voucherNameList )
			{
				LOGGER.info("..................................................................."+(String)voucherName);
				voucherNamesMap=new LinkedHashMap<String,String>();
				voucherNamesMap.put("key",(String)voucherName);
				voucherNamesMap.put("val",(String)voucherName);
				nameList.add(voucherNamesMap);
			}

		return "voucherNames";

	}

	public String ajaxValidateChequeNumber()
	{
		String index = parameters.get("index")[0];
		value = (instrumentService.isChequeNumberValid(chequeNumber, bankaccountId,departmentId)==true)?index+"~true":index+"~false";
		return "result";
	}
	
	public String ajaxValidateReassignSurrenderChequeNumber()
	{
		String index = parameters.get("index")[0];
		value = (instrumentService.isReassigningChequeNumberValid(chequeNumber, bankaccountId,departmentId)==true)?index+"~true":index+"~false";
		return "result";
	}
	public String ajaxLoadUser()throws Exception
	{
		userList = new ArrayList<UserImpl>();
		LOGGER.debug("CommonAction | ajaxLoadUserByDesg | Start");
		LOGGER.debug("Functionar received : = "+ functionaryName);
		String functionaryId = null;
		if(! "ANYFUNCTIONARY".equalsIgnoreCase(functionaryName)){
			Functionary functionary = (Functionary) persistenceService.find("from Functionary where name='"+functionaryName+"'");
			functionaryId =  functionary !=null?functionary.getId().toString():null;
		}
		if(departmentId!=-1 && designationId!=-1 && null !=functionaryName  && functionaryName.trim().length()!=0){
			List<EmployeeView>   empInfoList = voucherService.getUserByDeptAndDesgName(departmentId.toString(),
					designationId.toString(),functionaryId);
			 for (EmployeeView employeeView : empInfoList) {
					userList.add(employeeView.getUserMaster());
				}
		}
		return "users";
	}

	public String ajaxHodForDept() throws Exception{
		userList = new ArrayList<UserImpl>();
		List<PersonalInformation> listOfPI = new EisUtilService().getAllHodEmpByDept(departmentId);
		for (PersonalInformation personalInformation : listOfPI) {

			userList.add(personalInformation.getUserMaster());
		}

		return "users";
	}
	public String ajaxLoadCodesOfDetailType()
	{
		LOGGER.debug("Detail type id  : " + accountDetailType);
		if (null == accountDetailType) {
			accountCodesForDetailTypeList = egovCommon.getAllAccountCodesForAccountDetailType(-1);
		} else {
			accountCodesForDetailTypeList=egovCommon.getAllAccountCodesForAccountDetailType(accountDetailType);
		}
		LOGGER.debug("Scheme List size : " + accountCodesForDetailTypeList.size());
		return "accountcodes";
	}
@SuppressWarnings("unchecked")
public String ajaxLoadEntites() throws ClassNotFoundException
	{
	if(accountDetailType==null)
	{
		entitiesList= new ArrayList<EntityType>();
	}
	else{
	Accountdetailtype detailType = (Accountdetailtype)persistenceService.find("from Accountdetailtype where id=? order by name",accountDetailType);
	String table=detailType.getFullQualifiedName();
	Class<?> service = Class.forName(table);
	String simpleName = service.getSimpleName();
	simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";

	WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
	EntityTypeService entityService=	(EntityTypeService)wac.getBean(simpleName);
	entitiesList=(List<EntityType>)entityService.getAllActiveEntities(accountDetailType);
	}

	     return "entities";
	}


@SuppressWarnings("unchecked")
public String ajaxLoadEntitesBy20() throws ClassNotFoundException
{
	if(accountDetailType==null || accountDetailType == 0)
	{
		entitiesList= new ArrayList<EntityType>();
	}
	else{
		Accountdetailtype detailType = (Accountdetailtype)persistenceService.find("from Accountdetailtype where id=? order by name",accountDetailType);
		String table=detailType.getFullQualifiedName();
		Class<?> service = Class.forName(table);
		String simpleName = service.getSimpleName();
		simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";

		WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
		EntityTypeService entityService=	(EntityTypeService)wac.getBean(simpleName);
		entitiesList=(List<EntityType>)entityService.filterActiveEntities(startsWith, 20, detailType.getId());
	}
	return "entities";
}



public String ajaxLoadCheckList()
{
	LOGGER.info("..............................................................................ajaxLoadCheckList");
	EgBillSubType egBillSubType =(EgBillSubType) persistenceService.find("from EgBillSubType where id=?",billSubtypeId);
	checkList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", egBillSubType.getName());
	if(checkList.size()==0)
	{
		checkList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",FinancialConstants.CBILL_DEFAULTCHECKLISTNAME);
	}


	return "checkList";
}

	@SuppressWarnings("unchecked")
	public String searchEntites() throws ClassNotFoundException {
		searchType = "EntitySearch";
		if(accountDetailType==null) {
			entitiesList= new ArrayList<EntityType>();
		}
		else {
			Accountdetailtype detailType = (Accountdetailtype)persistenceService.find("from Accountdetailtype where id=? order by name",accountDetailType);
			String table=detailType.getFullQualifiedName();
			accountDetailTypeName = detailType.getName();
			try {
				Class<?> service = Class.forName(table);
				String simpleName = service.getSimpleName();
				simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";

				WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
				EntityTypeService entityService = null;

				entityService=	(EntityTypeService)wac.getBean(simpleName);
				entitiesList=(List<EntityType>)entityService.getAllActiveEntities(accountDetailType);
			} catch(Exception e) {
				LOGGER.debug("Service Not Available Exception : "+e.getMessage());
				entitiesList= new ArrayList<EntityType>();
			}
		}
	    return "searchResult";
	}

	/**
	 *This method will load the bank and branch for which there are cheqeues assigned and the cheque status is "NEW"
	 */
	public String ajaxLoadBanksWithAssignedCheques() {
		try {
			String vouchersWithNewInstrumentsQuery =
				"select voucherheaderid from egf_instrumentvoucher eiv,egf_instrumentheader ih," +
				" egw_status egws where eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New'  ";
			List<BigDecimal> results = persistenceService.getSession().createSQLQuery(vouchersWithNewInstrumentsQuery).list();
			StringBuffer queryString = new StringBuffer();
			queryString = queryString.append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' ')," +
					"bankBranch.branchname) as bankbranchname from  voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount, ")
				.append(" paymentheader ph where  ")
				.append(" ph.voucherheaderid=vh.id and vh.id  in (:newInst) and bank.isactive='1'  and bankBranch.isactive='1' ")
				.append(" and  bank.id = bankBranch.bankid and bankBranch.id = bankaccount.BRANCHID and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date")
				.append(" and ph.bankaccountnumberid=bankaccount.id  and bankaccount.isactive='1'");
			List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
										.setParameterList("newInst", results)
										.setParameter("date", getAsOnDate())
										.list();
			LOGGER.debug("Bank list size is "+ bankBranch.size());
			bankBranchList = new ArrayList<Map<String, Object>>();
			Map<String, Object> bankBrmap ;
			for(Object[] element : bankBranch){
				bankBrmap = new HashMap<String, Object> ();
				bankBrmap.put("bankBranchId", element[0].toString());
				bankBrmap.put("bankBranchName", element[1].toString());
				bankBranchList.add(bankBrmap);
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bank";
	}
/**
 * This method is to get the list of bank accounts for a particular bank branch for which there are cheques assigned in "NEW" status.
 * @return
 */
	@SuppressWarnings("unchecked")
	public String ajaxLoadBanksAccountsWithAssignedCheques(){
		try {
			accNumList = new ArrayList<Bankaccount>();
			StringBuffer queryString = new StringBuffer();
			queryString = queryString.append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode " +
					" from  voucherheader vh,chartofaccounts coa,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,paymentheader ph,  "+
					"egf_instrumentvoucher eiv,egf_instrumentheader ih,egw_status egws ")
					.append("where ph.voucherheaderid=vh.id and coa.id=bankaccount.glcodeid and vh.id=eiv.VOUCHERHEADERID and ")
					.append("  eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' ")
					.append("and ih.instrumenttype=(select id from egf_instrumenttype where upper(type)='CHEQUE') and ispaycheque=1 ")
					.append(" and bank.isactive='1'  and bankBranch.isactive='1' and bankaccount.isactive='1' ")
				.append(" and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid="+branchId+"  and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date");

			queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  order by vh.voucherdate desc");
			List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
										.setDate("date", getAsOnDate())
										.list();
			LOGGER.debug("Bank list size is "+ bankAccounts.size());
			List<String> addedBanks = new ArrayList<String>();
			for(Object[] account : bankAccounts){
				String accountNumberAndType = account[0].toString()+"-"+account[1].toString();
				if(!addedBanks.contains(accountNumberAndType)){
					Bankaccount bankaccount = new Bankaccount();
					bankaccount.setAccountnumber(account[0].toString());
					bankaccount.setAccounttype(account[1].toString());
					bankaccount.setId(Integer.valueOf(account[2].toString()));
					CChartOfAccounts chartofaccounts = new CChartOfAccounts();
					chartofaccounts.setGlcode(account[3].toString());
					bankaccount.setChartofaccounts(chartofaccounts);
					addedBanks.add(accountNumberAndType);
					accNumList.add(bankaccount);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bankAccNum";
	}

	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public List<Bankaccount> getAccNumList() {
		return accNumList;
	}
	public void setAccNumList(List<Bankaccount> accNumList) {
		this.accNumList = accNumList;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	public List<Scheme> getSchemeList() {
		return schemeList;
	}
	public void setSchemeList(List<Scheme> schemeList) {
		this.schemeList = schemeList;
	}
	public List<SubScheme> getSubSchemes() {
		return subSchemes;
	}
	public void setSubSchemes(List<SubScheme> subSchemes) {
		this.subSchemes = subSchemes;
	}
	public List<Map<String, Object>> getBankBranchList() {
		return bankBranchList;
	}
	public void setBankBranchList(List<Map<String, Object>> bankBranchList) {
		this.bankBranchList = bankBranchList;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public List<Bankbranch> getBranchList() {
		return branchList;
	}
	public void setBranchList(List<Bankbranch> branchList) {
		this.branchList = branchList;
	}
	public Integer getBankaccountId() {
		return bankaccountId;
	}
	public void setBankaccountId(Integer bankaccountId) {
		this.bankaccountId = bankaccountId;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public void setDetailCodes(List<String> detailCodes) {
		this.detailCodes = detailCodes;
	}
	public List<String> getDetailCodes() {
		return detailCodes;
	}
	public List<UserImpl> getUserList() {
		return userList;
	}
	public void setUserList(List<UserImpl> userList) {
		this.userList = userList;
	}
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}
	public EgovCommon getEgovCommon() {
		return egovCommon;
	}
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	public List<CChartOfAccounts> getAccountCodesForDetailTypeList() {
		return accountCodesForDetailTypeList;
	}
	public void setAccountCodesForDetailTypeList(List<CChartOfAccounts> accountCodesForDetailTypeList) {
		this.accountCodesForDetailTypeList = accountCodesForDetailTypeList;
	}
	public List<EntityType> getEntitiesList() {
		return entitiesList;
	}
	public void setEntitiesList(List<EntityType> entitiesList) {
		this.entitiesList = entitiesList;
	}
	public Integer getAccountDetailType() {
		return accountDetailType;
	}
	public void setAccountDetailType(Integer accountDetailType) {
		this.accountDetailType = accountDetailType;
	}
	public Integer getBillSubtypeId() {
		return billSubtypeId;
	}
	public void setBillSubtypeId(Integer billSubtypeId) {
		this.billSubtypeId = billSubtypeId;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	public List<AppConfigValues> getCheckList() {
		return checkList;
	}
	public void setCheckList(List<AppConfigValues> checkList) {
		this.checkList = checkList;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public List getNameList() {
		return nameList;
	}
	public void setNameList(List nameList) {
		this.nameList = (ArrayList<Map<String, String>>) nameList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getAccountDetailTypeName() {
		return accountDetailTypeName;
	}
	public void setAccountDetailTypeName(String accountDetailTypeName) {
		this.accountDetailTypeName = accountDetailTypeName;
	}
	public String getTypeOfAccount() {
		return typeOfAccount;
	}
	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadBanksWithApprovedPayments(){
		try {
			StringBuffer queryString = new StringBuffer();
			// query to fetch vouchers for which no cheque has been assigned
			queryString = queryString.append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' ')," +
					"bankBranch.branchname) as bankbranchname,vh.VOUCHERNUMBER,vh.id,iv.VOUCHERHEADERID " +
					"from voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d," +
					"generalledger gl,paymentheader ph,eg_wf_states es,egf_instrumentvoucher iv right outer join voucherheader vh1 on " +
					"vh1.id =iv.VOUCHERHEADERID,egw_status egws where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and " +
					"vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and " +
					"ph.voucherheaderid=vh.id and bank.isactive='1'  and bankBranch.isactive='1' and bank.id = bankBranch.bankid and " +
					"bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')" +
					" and  vh1.id=vh.id and iv.VOUCHERHEADERID is null ");
			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			if(departmentId!=null && departmentId!=0 && departmentId!=-1){
				queryString = queryString.append(" and vmis.departmentid="+departmentId);
			}
			queryString = queryString.append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date1 "+
				" and ph.bankaccountnumberid=bankaccount.id and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name!='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'  ");
			//query to fetch vouchers for which cheque has been assigned and surrendered
			queryString=queryString.append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' ')," +
					"bankBranch.branchname) as bankbranchname,vh.VOUCHERNUMBER,vh.id,iv.VOUCHERHEADERID from egf_instrumentvoucher iv,voucherheader vh," +
					"Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl," +
					"paymentheader ph,eg_wf_states es, egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, " +
					"(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid," +
					"instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber " +
					"and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and " +
					"vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id " +
					"and bank.isactive='1'  and bankBranch.isactive='1' and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and " +
					"bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and " +
					"ih.id_status=egws.id and egws.description in  ('Surrendered','Surrender_For_Reassign')");
			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			if(departmentId!=null && departmentId!=0 && departmentId!=-1){
				queryString = queryString.append(" and vmis.departmentid="+departmentId);
			}
			queryString = queryString.append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date2 "+
				" and ph.bankaccountnumberid=bankaccount.id and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name!='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'  ");
			List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
										.setDate("date1", getAsOnDate())
										.setDate("date2", getAsOnDate())
										.list();
			LOGGER.debug("Bank list size is "+ bankBranch.size());
			bankBranchList = new ArrayList<Map<String, Object>>();
			List<String> addedBanks = new ArrayList<String>();
			for(Object[] account : bankBranch){
				String bankBranchName = account[1].toString();
				if(!addedBanks.contains(bankBranchName)){
					addedBanks.add(bankBranchName);
					Map<String, Object> bankBrmap = new HashMap<String, Object> ();
					bankBrmap.put("bankBranchId", account[0].toString());
					bankBrmap.put("bankBranchName", bankBranchName);
					bankBranchList.add(bankBrmap);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bank";
	}


	@SuppressWarnings("unchecked")
	public String ajaxLoadBanksWithApprovedRemittances(){
		try {
			StringBuffer queryString = new StringBuffer();
			queryString = queryString.append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' ')," +
					"bankBranch.branchname) as bankbranchname,vh.VOUCHERNUMBER,vh.id,iv.VOUCHERHEADERID " +
					 "from voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,eg_remittance rem," +
					"generalledger gl left outer join function f on gl.functionid=f.id,paymentheader ph,eg_wf_states es," +
					"egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID,egw_status egws " +
					"where  ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END'" +
					"and rem.paymentvhid=vh.id and rem.tdsid="+recoveryId+
					" and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive='1'  and bankBranch.isactive='1' " +
					"and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')" +
					" and  vh1.id=vh.id and iv.VOUCHERHEADERID is null ");
			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			queryString = queryString.append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date1")
				.append(" and ph.bankaccountnumberid=bankaccount.id and ph.type='"+FinancialConstants.MODEOFPAYMENT_CASH+"'  and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"' ");

			queryString.append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' ')," +
					"bankBranch.branchname) as bankbranchname,vh.VOUCHERNUMBER,vh.id,iv.VOUCHERHEADERID from egf_instrumentvoucher iv,voucherheader vh,eg_remittance rem," +
					"Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl left outer join function f on " +
					"gl.functionid=f.id,paymentheader ph,eg_wf_states es, egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, " +
					"(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid," +
					"instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber " +
					"and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and " +
					"vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id " +
					"and bank.isactive='1'  and bankBranch.isactive='1' and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and " +
					"bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and " +
					" rem.paymentvhid=vh.id and rem.tdsid="+recoveryId+" and "+
			"ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') ");
			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			queryString = queryString.append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date2")
				.append(" and ph.bankaccountnumberid=bankaccount.id and ph.type='"+FinancialConstants.MODEOFPAYMENT_CASH+"' and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'");
			List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
										.setDate("date1", getAsOnDate())
										.setDate("date2", getAsOnDate())
										.list();
			LOGGER.debug("Bank list size is "+ bankBranch.size());
			bankBranchList = new ArrayList<Map<String, Object>>();
			List<String> addedBanks = new ArrayList<String>();
			for(Object[] account : bankBranch){
				String bankBranchName = account[1].toString();
				if(!addedBanks.contains(bankBranchName)){
					addedBanks.add(bankBranchName);
					Map<String, Object> bankBrmap = new HashMap<String, Object> ();
					bankBrmap.put("bankBranchId", account[0].toString());
					bankBrmap.put("bankBranchName", bankBranchName);
					bankBranchList.add(bankBrmap);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bank";
	}

	/**
	 * @param voucherStatusKey - The appconfig key which gives the voucher workflow status
	 * @param asOnDate
	 * @param fundId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String ajaxLoadBanksWithPaymentInWorkFlow(){
		try {
			String voucherStatusKey = parameters.get("voucherStatusKey")[0];
			List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,voucherStatusKey);
			if(appConfig == null || appConfig.isEmpty())
				throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");
			String voucherStatus = ((AppConfigValues)appConfig.get(0)).getValue();
			List<Bankaccount> bankAccounts = persistenceService.findAllBy("select p.bankaccount" +
					" from Paymentheader p where p.voucherheader.voucherDate<='"+Constants.DDMMYYYYFORMAT2.format(asOnDate)+"' and p.state.type='Paymentheader' " +
					" and p.bankaccount.bankbranch.bank.isactive='1'  and p.bankaccount.bankbranch.isactive='1' " +
					"and p.bankaccount.fund.id=? and p.state.value like '"+voucherStatus+"' order by p.bankaccount.bankbranch.bank.name,p.bankaccount.bankbranch.branchname",fundId);
			bankBranchList = new ArrayList<Map<String, Object>>();
			List<String> addedBanks = new ArrayList<String>();
			for(Bankaccount account : bankAccounts){
				String bankBranchName = account.getBankbranch().getBank().getName()+"-"+account.getBankbranch().getBranchname();
				if(!addedBanks.contains(bankBranchName)){
					addedBanks.add(bankBranchName);
					Map<String, Object> bankBrmap = new HashMap<String, Object> ();
					bankBrmap.put("bankBranchId", account.getBankbranch().getBank().getId()+"-"+account.getBankbranch().getId());
					bankBrmap.put("bankBranchName", bankBranchName);
					bankBranchList.add(bankBrmap);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bank";
	}

	/**
	 * @param voucherStatusKey - The appconfig key which gives the voucher workflow status
	 * @param asOnDate
	 * @param fundId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String ajaxLoadBankAccountsWithPaymentInWorkFlow(){
		try {
			accNumList = new ArrayList<Bankaccount>();
			String voucherStatusKey = parameters.get("voucherStatusKey")[0];
			List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,voucherStatusKey);
			if(appConfig == null || appConfig.isEmpty())
				throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");
			String voucherStatus = ((AppConfigValues)appConfig.get(0)).getValue();
			List<Bankaccount> bankAccounts = persistenceService.findAllBy("select p.bankaccount" +
					" from Paymentheader p where p.voucherheader.voucherDate<='"+Constants.DDMMYYYYFORMAT2.format(asOnDate)+"' and p.state.type='Paymentheader' " +
					" and p.bankaccount.isactive='1'  and p.bankaccount.bankbranch.isactive='1' and  p.bankaccount.bankbranch.id=?" +
					"and p.bankaccount.fund.id=? and p.state.value like '"+voucherStatus+"' order by p.bankaccount.bankbranch.bank.name,p.bankaccount.bankbranch.branchname",branchId,fundId);
			List<String> addedBanks = new ArrayList<String>();
			for(Bankaccount account : bankAccounts){
				String bankBranchName = account.getAccountnumber()+"-"+account.getAccounttype();
				if(!addedBanks.contains(bankBranchName)){
					addedBanks.add(bankBranchName);
					accNumList.add(account);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bankaccount dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bankaccount dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bankAccNum" ;
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadBankAccountsWithApprovedPayments(){
		try {
			accNumList = new ArrayList<Bankaccount>();
			StringBuffer queryString = new StringBuffer();
			// query to fetch vouchers for which no cheque has been assigned
			queryString = queryString.append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode " +
					"from chartofaccounts coa,voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d," +
					"generalledger gl ,paymentheader ph,eg_wf_states es," +
					"egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID,egw_status egws " +
					"where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END'" +
					" and coa.id=bankaccount.glcodeid and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive='1'  and bankBranch.isactive='1' " +
					"and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bankaccount.branchid="+branchId+
					" and  vh1.id=vh.id and iv.VOUCHERHEADERID is null");

			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			if(departmentId!=null && departmentId!=0 && departmentId!=-1){
				queryString = queryString.append(" and vmis.departmentid="+departmentId);
			}
			queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name!='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'");
			//query to fetch vouchers for which cheque has been assigned and surrendered
			queryString.append(" union select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode " +
					" from chartofaccounts coa,egf_instrumentvoucher iv,voucherheader vh," +
					"Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl," +
					"paymentheader ph,eg_wf_states es, egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, " +
					"(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid," +
					"instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber " +
					"and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and " +
					"vmis.departmentid= d.id_dept and coa.id=bankaccount.glcodeid and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id " +
					"and bank.isactive='1'  and bankBranch.isactive='1' and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and " +
					"bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and bankaccount.branchid="+branchId+
					" and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign')");
			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			if(departmentId!=null && departmentId!=0 && departmentId!=-1){
				queryString = queryString.append(" and vmis.departmentid="+departmentId);
			}
			queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name!='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'");

			List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
										.list();
			LOGGER.debug("Bank list size is "+ bankAccounts.size());
			List<String> addedBanks = new ArrayList<String>();
			for(Object[] account : bankAccounts){
				String accountNumberAndType = account[0].toString()+"-"+account[1].toString();
				if(!addedBanks.contains(accountNumberAndType)){
					Bankaccount bankaccount = new Bankaccount();
					bankaccount.setAccountnumber(account[0].toString());
					bankaccount.setAccounttype(account[1].toString());
					CChartOfAccounts chartofaccounts = new CChartOfAccounts();
					chartofaccounts.setGlcode(account[3].toString());
					bankaccount.setChartofaccounts(chartofaccounts);
					bankaccount.setId(Integer.valueOf(account[2].toString()));
					addedBanks.add(accountNumberAndType);
					accNumList.add(bankaccount);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bankAccNum";
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadBankAccountsWithApprovedRemittances(){
		try {
			accNumList = new ArrayList<Bankaccount>();
			StringBuffer queryString = new StringBuffer();
			queryString = queryString.append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode " +
					"from chartofaccounts coa,voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,EG_REMITTANCE rem ," +
					"generalledger gl left outer join function f on gl.functionid=f.id,paymentheader ph,eg_wf_states es," +
					"egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID,egw_status egws,egf_instrumentheader ih " +
					"where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END'" +
					"and rem.paymentvhid=vh.id and rem.tdsid="+recoveryId+
					" and coa.id=bankaccount.glcodeid and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive='1'  and bankBranch.isactive='1' " +
					" and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bankaccount.branchid="+branchId+
					" and  vh1.id=vh.id and iv.VOUCHERHEADERID is null");

			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id and ph.type='"+FinancialConstants.MODEOFPAYMENT_CASH+"' and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'");

			queryString.append(" union select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode " +
					" from chartofaccounts coa,egf_instrumentvoucher iv,voucherheader vh," +
					"Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,eg_remittance rem ,generalledger gl left outer join function f on " +
					"gl.functionid=f.id,paymentheader ph,eg_wf_states es, egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, " +
					"(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid," +
					" instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber " +
					" and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid  " +
					" and rem.paymentvhid=vh.id and rem.tdsid="+recoveryId+
					" and vmis.departmentid= d.id_dept and coa.id=bankaccount.glcodeid and ph.state_id=es.id and es.value='END' and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id " +
					" and bank.isactive='1'  and bankBranch.isactive='1' and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid="+branchId+
					" and  bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and " +
					"ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign')");
			if(fundId!=null && fundId!=0 && fundId!=-1){
				queryString = queryString.append(" and bankaccount.fundid="+fundId);
			}
			queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id and  ph.type='"+FinancialConstants.MODEOFPAYMENT_CASH+"' and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name='"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"'");

			List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
										.list();
			LOGGER.debug("Bank list size is "+ bankAccounts.size());
			List<String> addedBanks = new ArrayList<String>();
			for(Object[] account : bankAccounts){
				String accountNumberAndType = account[0].toString()+"-"+account[1].toString();
				if(!addedBanks.contains(accountNumberAndType)){
					Bankaccount bankaccount = new Bankaccount();
					bankaccount.setAccountnumber(account[0].toString());
					bankaccount.setAccounttype(account[1].toString());
					CChartOfAccounts chartofaccounts = new CChartOfAccounts();
					chartofaccounts.setGlcode(account[3].toString());
					bankaccount.setChartofaccounts(chartofaccounts);
					bankaccount.setId(Integer.valueOf(account[2].toString()));
					addedBanks.add(accountNumberAndType);
					accNumList.add(bankaccount);
				}
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data for bank dropdown "+e.getMessage(),new Exception(e.getMessage()));
		}
		return "bankAccNum";
	}	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}
	public Date getAsOnDate() {
		return asOnDate;
	}

	public String ajaxLoadDesg(){
		LOGGER.debug("CommonAction | ajaxLoadDesg | Start ");

		Map<String, Object>  map=null;
		if(getBillRegisterId()!=null)
		{
			Cbill cbill = (Cbill) persistenceService.find(" from EgBillregister where id=?", getBillRegisterId());
			map = voucherService.getDesgBYPassingWfItem(scriptName, cbill, departmentId);
		}
		else
			map = voucherService.getDesgBYPassingWfItem(scriptName, null, departmentId);

		designationList = (List<Map<String, Object>>)map.get("designationList");
		LOGGER.debug("CommonAction | ajaxLoadDesg | End ");
		return "desg";
	}

	public String ajaxLoadDefaultDepartment(){
		defaultDepartment = voucherService.getDefaultDepartment().toString();
		return "defaultDepartment";
	}

	public String ajaxLoadFundSource(){
		LOGGER.debug("CommonAction | subscheme id received = "+ subSchemeId);
		if(null != subSchemeId){
			fundSouceList = financingSourceService.getFinancialSourceBasedOnSubScheme(subSchemeId);
		}
		return Constants.FUNDSOURCE;
	}

	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public List<Map<String, Object>> getDesignationList() {
		return designationList;
	}
	public void setDesignationList(List<Map<String, Object>> designationList) {
		this.designationList = designationList;
	}
	public String getStartsWith() {
		return startsWith;
	}
	public void setStartsWith(String startsWith) {
		this.startsWith = startsWith;
	}
	public Long getRecoveryId() {
		return recoveryId;
	}
	public void setRecoveryId(Long recoveryId) {
		this.recoveryId = recoveryId;
	}
	public void setSubSchemeId(Integer subSchemeId) {
		this.subSchemeId = subSchemeId;
	}
	public Integer getSubSchemeId() {
		return subSchemeId;
	}
	public List<Fundsource> getFundSouceList() {
		return fundSouceList;
	}
	public void setFundSouceList(List<Fundsource> fundSouceList) {
		this.fundSouceList = fundSouceList;
	}
	public void setFinancingSourceService(
			FinancingSourceService financingSourceService) {
		this.financingSourceService = financingSourceService;
	}
	public void setDefaultDepartment(String defaultDepartment) {
		this.defaultDepartment = defaultDepartment;
	}
	public String getDefaultDepartment() {
		return defaultDepartment;
	}
}



