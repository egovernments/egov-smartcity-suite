//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\ChartOfAccounts.java
package com.exilant.GLEngine;
//import com.exilant.eGov.src.domain.GeneralLedger;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.model.budget.BudgetDetail;
import org.egov.utils.Constants;
import org.hibernate.Session;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import com.exilant.eGov.src.domain.ClosedPeriods;
import com.exilant.eGov.src.domain.EgRemittanceGldtl;
import com.exilant.eGov.src.domain.GeneralLedger;
import com.exilant.eGov.src.domain.GeneralLedgerDetail;
import com.exilant.eGov.src.transactions.ExilPrecision;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;
/**
 * This Singleton class contains all the account codes for the organization
 */
/**
* @@org.jboss.cache.aop.InstanceOfAopMarker
*/
public class ChartOfAccounts {
	static ChartOfAccounts singletonInstance;
	private static final Logger LOGGER = Logger.getLogger(ChartOfAccounts.class);

	private static final String ROOTNODE = "/COA";
	private static final String GLACCCODENODE ="GlAccountCodes";
	private static final String GLACCIDNODE ="GlAccountIds";
	private static final String ACCOUNTDETAILTYPENODE ="AccountDetailType";
	private static final String EXP="Exp=";
	private static final String EXILRPERROR = "exilRPError";

	private ResultSet resultset;

	private static Cache<Object, Object> cache;
	
	BudgetDetailsHibernateDAO budgetDetailsDAO = null;
	static
	{
		singletonInstance = new ChartOfAccounts();
		try
		{
   //TODO   Commenting reading cache from infinispan temporarily and building cachemanager through code 
			
			//cache = new DefaultCacheManager("infinispan.xml").getCache("egfMasterCache");
			
			EmbeddedCacheManager manager = new DefaultCacheManager();
			 manager.defineConfiguration("chartofaccounts-cache", new ConfigurationBuilder().build());
			 cache = manager.getCache("chartofaccounts-cache");

			 

		} catch (Exception e)
		{
			LOGGER.debug(EXP+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
	}

   private ChartOfAccounts(){

   }
   public static  ChartOfAccounts getInstance()throws TaskFailedException{
   	LOGGER.debug("getInstancw called");
   	if(getGlAccountCodes()==null || getGlAccountIds()==null || getAccountDetailType()==null){
   		LOGGER.debug("getInstancw called");
   		loadAccountData();
   	}
	return singletonInstance;
   }
   public  void reLoadAccountData()throws TaskFailedException{
   	LOGGER.debug("reLoadAccountData called");
	/*  1.Loads all the account codes and details of that as GLAccount objects
	 *  in  theGLAccountCode,theGLAccountId HashMap's
	 */
   		if(getGlAccountCodes()!=null)    getGlAccountCodes().clear();
   		if(getGlAccountIds()!=null)      getGlAccountIds().clear();
   		if(getAccountDetailType()!=null)	getAccountDetailType().clear();

   		//Temporary place holders
   		HashMap glAccountCodes = new HashMap();
   		HashMap glAccountIds = new HashMap();
   		HashMap accountDetailType= new HashMap();
   		DataExtractor de=DataExtractor.getExtractor();
		String sql="select ID as \"ID\",name as  \"name\",tableName as \"tableName\","+
		"description as \"description\",columnName as \"columnName\",attributeName as \"attributeName\""+
		",nbrOfLevels as  \"nbrOfLevels\" from accountDetailType";
		accountDetailType =  de.extractIntoMap(sql,"attributeName",AccountDetailType.class);
		sql="select ID as \"ID\", glCode as \"glCode\" ,name as \"name\" ," +
				" isactiveforposting as \"isActiveForPosting\"  from chartofaccounts ";
		glAccountCodes = de.extractIntoMap(sql,"glCode",GLAccount.class);
		glAccountIds = de.extractIntoMap(sql,"ID",GLAccount.class);
		loadParameters(glAccountCodes,glAccountIds);
		try
		{
			HashMap<String, HashMap> hm = new HashMap<String, HashMap>();
			hm.put(ACCOUNTDETAILTYPENODE,accountDetailType);
			hm.put(GLACCCODENODE,glAccountCodes);
			hm.put(GLACCIDNODE,glAccountIds);
			LOGGER.debug("ReLoading size:" + glAccountCodes.size());
			//cache.put(ROOTNODE+"/"+FilterName.get(),ACCOUNTDETAILTYPENODE,accountDetailType);
			//cache.put(ROOTNODE+"/"+FilterName.get(),gLAccCodeNode,glAccountCodes);
			//cache.put(ROOTNODE+"/"+FilterName.get(),GLACCIDNODE,glAccountIds);
			cache.put(ROOTNODE+"/"+EGOVThreadLocals.getDomainName() ,hm);
		} catch (Exception e)
		{
			LOGGER.error(EXP+e.getMessage());
			throw new TaskFailedException(e);
		}
   }

   static void loadAccountData()throws TaskFailedException{
   			LOGGER.debug("loadAccountData called");
		/*  1.Loads all the account codes and details of that as GLAccount objects
		 *  in  theGLAccountCode,theGLAccountId HashMap's
		 */
	   		if(getGlAccountCodes()!=null)    getGlAccountCodes().clear();
	   		if(getGlAccountIds()!=null)      getGlAccountIds().clear();
	   		if(getAccountDetailType()!=null)	getAccountDetailType().clear();
	   		//Temporary place holders
	   		HashMap glAccountCodes = new HashMap();
	   		HashMap glAccountIds = new HashMap();
	   		HashMap accountDetailType= new HashMap();


			DataExtractor de=DataExtractor.getExtractor();

			String sql="select ID as \"ID\",name as  \"name\",tableName as \"tableName\","+
			"description as \"description\",columnName as \"columnName\",attributeName as \"attributeName\""+
			",nbrOfLevels as  \"nbrOfLevels\" from AccountDetailType";
			accountDetailType = de.extractIntoMap(sql,"attributeName",AccountDetailType.class);
			sql="select ID as \"ID\", glCode as \"glCode\" ,name as \"name\" ," +
					"isactiveforposting as \"isActiveForPosting\"  from chartofaccounts ";
			glAccountCodes =  de.extractIntoMap(sql,"glCode",GLAccount.class);
			glAccountIds = de.extractIntoMap(sql,"ID",GLAccount.class);
			loadParameters(glAccountCodes,glAccountIds);
			try
			{
				HashMap<String, HashMap> hm = new HashMap<String, HashMap>();
				hm.put(ACCOUNTDETAILTYPENODE,accountDetailType);
				hm.put(GLACCCODENODE,glAccountCodes);
				LOGGER.debug("Loading size:" + glAccountCodes.size());
				hm.put(GLACCIDNODE,glAccountIds);
				//cache.put(ROOTNODE+"/"+FilterName.get(),ACCOUNTDETAILTYPENODE,accountDetailType);
				//cache.put(ROOTNODE+"/"+FilterName.get(),GLACCCODENODE,glAccountCodes);
				//cache.put(ROOTNODE+"/"+FilterName.get(),GLACCIDNODE,glAccountIds);
				cache.put(ROOTNODE+"/"+EGOVThreadLocals.getDomainName(),hm);
			} catch (Exception e)
			{
				LOGGER.error(EXP+e.getMessage());
				throw new TaskFailedException(e);

			}
   }
 
//   private static synchronized void loadParameters(HashMap glAccountCodes, HashMap glAccountIds)throws TaskFailedException{
//	LOGGER.info("loadParameters called");
//	Iterator it=glAccountCodes.keySet().iterator();
//	String sql="";
//	DataExtractor de=DataExtractor.getExtractor();
//	ArrayList reqParam;
//	while(it.hasNext()){
//		String  obj=(String)it.next();
//
//		GLAccount glAccCode=(GLAccount)glAccountCodes.get(obj);
//		GLAccount glAccId=(GLAccount)glAccountIds.get(String.valueOf(glAccCode.getId()));
//		sql="select  b.id as \"detailId\" , b.attributename as \"detailName\"" +
//			" from " +
//			"chartofaccountdetail a,accountDetailType b "  +
//			"where  b.id=a.detailtypeid   and glcodeid='"+glAccCode.getId()+"'";
//		reqParam=new ArrayList();
//		reqParam=de.extractIntoList(sql,GLParameter.class);
//		glAccCode.setGLParameters(reqParam);
//		glAccId.setGLParameters(reqParam);
//	}
//}
   
   private static synchronized void loadParameters(HashMap glAccountCodes, HashMap glAccountIds)throws TaskFailedException{
		PersistenceService persistenceService = new PersistenceService();
		persistenceService.setSessionFactory(new SessionFactory());
		persistenceService.setType(CChartOfAccountDetail.class);
		List<CChartOfAccountDetail> chList = persistenceService.findAllBy("from CChartOfAccountDetail");
		for (CChartOfAccountDetail chartOfAccountDetail : chList) {
			GLParameter parameter = new GLParameter();
			parameter.setDetailId(chartOfAccountDetail.getDetailTypeId().getId());
			parameter.setDetailName(chartOfAccountDetail.getDetailTypeId().getAttributename());
			GLAccount glAccCode=getGlAccCode(chartOfAccountDetail.getGlCodeId(),glAccountCodes);
			GLAccount glAccId=getGlAccId(chartOfAccountDetail.getGlCodeId(),glAccountIds);
			if(glAccCode!=null && glAccCode.getGLParameters()!=null)
				glAccCode.getGLParameters().add(parameter);
			if(glAccId!=null && glAccId.getGLParameters()!=null)
				glAccId.getGLParameters().add(parameter);
		}
	}

	private static GLAccount getGlAccCode(CChartOfAccounts glCodeId,Map glAccountCodes) {
		for (Object key : glAccountCodes.keySet()) {
			if(((String)key).equalsIgnoreCase(glCodeId.getGlcode()))
				return (GLAccount)glAccountCodes.get(key);
		}
		return null;
	}
	
	private static GLAccount getGlAccId(CChartOfAccounts glCodeId,Map glAccountIds) {
		for (Object key : glAccountIds.keySet()) {
			if(((String)key).equalsIgnoreCase(glCodeId.getId().toString()))
				return (GLAccount)glAccountIds.get(key);
		}
		return null;
	}

private boolean validateGLCode(Transaxtion txn,DataCollection dc,Connection con) throws TaskFailedException{
   		// validate each gl code
   	LOGGER.info("Inside the ValidateGLCode2");
   		HashMap hm = (HashMap)getGlAccountCodes();
   		//LOGGER.info("HashMap value is :"+hm);
   		if(hm == null)
   		{
   			LOGGER.error("Account Codes not initialized");
   			dc.addMessage("Account Codes not initialized",txn.getGlCode()+" For "+txn.getGlName());
   			return false;
   		}

   		if(hm.get(txn.getGlCode())==null)
   		{
   			LOGGER.debug("looking for:" + txn.getGlCode() + ":");
   			Iterator itr = hm.keySet().iterator();
   			while(itr.hasNext())
   			{
   				LOGGER.debug("GLCode:" + (String)itr.next() + ":");
   			}

   		}
   		Object obj = hm.get(txn.getGlCode());
   		LOGGER.info("Class Name:" + obj.getClass());
   		GLAccount glAcc=(GLAccount)hm.get(txn.getGlCode());
   		if(glAcc==null){
   			dc.addMessage("exilInvalidCode",txn.getGlCode()+" For "+txn.getGlName());
   			return false;
   		}
   		txn.setGlName(glAcc.getName());
   		LOGGER.info(txn.getGlCode()+" is activefor posting :"+glAcc.isActiveForPosting());
   		if(glAcc.isActiveForPosting()==0){
   			dc.addMessage("exilInActiveAccount",txn.getGlCode()+" For "+txn.glName);
   			return false;
   		}
   		LOGGER.info("Classification....in   :"+getClassificationForCode(txn.getGlCode(),con));
		if(getClassificationForCode(txn.getGlCode(),con)!=4){
			LOGGER.info("classification is not detailed code");
			dc.addMessage("exilNotDetailAccount",txn.getGlCode());
			return false;
			}

   		if(Double.parseDouble(txn.getDrAmount())>0
   				&& Double.parseDouble(txn.getCrAmount())>0){
   			dc.addMessage("exilInvalidTrxn");
   			return false;
   		}
   		if(!isRequiredPresent(txn,glAcc,dc)){
   			//dc.addMessage("exilDataInsufficient");
   			return false;
   		}
   		//return checkAllMasters(dc,con);
   		return true;
   }
   public boolean validateGLCode(Transaxtion txn,Connection con) throws Exception{
		// validate each gl code
   	LOGGER.info("Inside the ValidateGLCode1");
		GLAccount glAcc=(GLAccount)getGlAccountCodes().get(txn.getGlCode());
		if(glAcc==null){
			LOGGER.error("GLCode is null");
		return false;
		}
		txn.setGlName(glAcc.getName());
		LOGGER.info(txn.getGlCode()+" is activefor posting :"+glAcc.isActiveForPosting());
		if(glAcc.isActiveForPosting()==0){
			return false;
		}
		LOGGER.info("Classification....:"+getClassificationForCode(txn.getGlCode(),con));
		if(getClassificationForCode(txn.getGlCode(),con)!=4){
			LOGGER.info("classification is not detailed code");
			throw new TaskFailedException("Cannot post to "+ txn.getGlCode());
			}
		LOGGER.info("Going to check the Amount.Debit: "+txn.getDrAmount()+" ** Credit :"+txn.getCrAmount());
		if(Double.parseDouble(txn.getDrAmount())>0
				&& Double.parseDouble(txn.getCrAmount())>0){
			throw new TaskFailedException("Both Debit and Credit cannot be greater than Zero.");
//				return false;
		}
		if(!isRequiredPresent(txn,glAcc)){
			return false;
		}
		return true;
}
   /**
    * This function is to get the classification of any glcode provided
    * @param glcode
    * @param con
    * @return
    * @throws TaskFailedException
    */
   private int getClassificationForCode(String glcode,Connection con) throws TaskFailedException
   {
   	int retVal=0;
   	 ResultSet rs=null;
   	 Statement stmt=null;
   	try{
   		stmt=con.createStatement();
   	   	rs=stmt.executeQuery("select classification from chartofaccounts where glcode='"+glcode+"'");
   	   	if(rs.next())
   	   		retVal=rs.getInt(1);
   	   	rs.close();
   	   	stmt.close();
   	}catch (Exception e)
	{
		LOGGER.debug(EXP+e.getMessage());
		throw new TaskFailedException();
	}
   	return retVal;
   }
   private boolean isRequiredPresent(Transaxtion txn,GLAccount glAcc,DataCollection dc)throws TaskFailedException{
   	int requiredCount=0;
   	int foundCount=0;
   	ArrayList glParamList=glAcc.getGLParameters();
   	for(int i=0;i<glParamList.size();i++){
   		GLParameter glPrm=(GLParameter)glParamList.get(i);
   	    requiredCount++;
   	    /*if(!glPrm.getDetailKey().equalsIgnoreCase("0")&&glPrm.getDetailKey().length()>0){
   	    	foundCount++;
   	    	continue;
   	    }*/
   	 	for(int j=0;j<txn.transaxtionParameters.size();j++){
   	 		TransaxtionParameter txnPrm=(TransaxtionParameter)txn.transaxtionParameters.get(j);
   	 		//LOGGER.info(glAcc.getCode()+" "+txnPrm.getDetailName()+" "+txnPrm.getDetailKey());
   	 		if(txnPrm.getDetailName().equalsIgnoreCase(glPrm.getDetailName())){
   	 			int id=glPrm.getDetailId();
   	 			//validates the master keys here
   	 			RequiredValidator rv=new RequiredValidator();
   	 			if(rv.validateKey(id,txnPrm.getDetailKey())){
   	 				foundCount++;
   	 			}else{
   	 				dc.addMessage("exilWrongData", txnPrm.getDetailName());
   	 				return false;
   	 			}
   	 		}
   	 	}
   	 }
   	 if(foundCount<requiredCount){
   	 	dc.addMessage("exilDataInsufficient");
		return false;
   	 }
   	 return true;
   }
   private boolean isRequiredPresent(Transaxtion txn,GLAccount glAcc)throws Exception{
   	int requiredCount=0;
   	int foundCount=0;
   	ArrayList glParamList=glAcc.getGLParameters();
   	for(int i=0;i<glParamList.size();i++){
   		GLParameter glPrm=(GLParameter)glParamList.get(i);
   		TransaxtionParameter txnPrm1=(TransaxtionParameter)txn.transaxtionParameters.get(0);
   		if(glPrm.getDetailId()==Integer.parseInt(txnPrm1.getDetailTypeId()))
   		    requiredCount++;
   	   /* if(!glPrm.getDetailKey().equalsIgnoreCase("0")&&glPrm.getDetailKey().length()>0){
   	    	foundCount++;
   	    	continue;
   	    }*/
   	 	for(int j=0;j<txn.transaxtionParameters.size();j++){
   	 		TransaxtionParameter txnPrm=(TransaxtionParameter)txn.transaxtionParameters.get(j);
   	 		//LOGGER.info(glAcc.getCode()+" "+txnPrm.getDetailName()+" "+txnPrm.getDetailKey());
   	 		if(txnPrm.getDetailName().equalsIgnoreCase(glPrm.getDetailName())){
   	 			int id=glPrm.getDetailId();
   	 			//validates the master keys here
   	 			RequiredValidator rv=new RequiredValidator();
   	 			if(rv.validateKey(id,txnPrm.getDetailKey())){
   	 				foundCount++;
   	 			}else{
   	 				return false;
   	 			}
   	 		}
   	 	}
   	 }
   	 if(foundCount<requiredCount){
   	 	return false;
   	 }
   	 return true;
   }
	private boolean validateTxns(Transaxtion txnList[],DataCollection dc,Connection con)throws TaskFailedException{
		// validate the array list for the total number of txns
		if(txnList.length<2){
			dc.addMessage("exilWrongTrxn");
			return false;
		}
		double dbAmt=0;
		double crAmt=0;
		try{
			for(int i=0;i<txnList.length;i++){
				Transaxtion txn=(Transaxtion)txnList[i];
				if(!validateGLCode(txn,dc,con))return false;
				dbAmt+=Double.parseDouble(txn.getDrAmount());
				crAmt+=Double.parseDouble(txn.getCrAmount());
			}
		}catch(Exception e){
			dc.addMessage(EXILRPERROR,e.toString());
			throw new TaskFailedException();
		}
		dbAmt=ExilPrecision.convertToDouble(dbAmt,2);
		crAmt=ExilPrecision.convertToDouble(crAmt,2);
		if(dbAmt!=crAmt){
			dc.addMessage("exilAmountMismatch");
			return false;
		}
   		return true;
	}
	private boolean validateTxns(Transaxtion txnList[],Connection con)throws Exception{
		// validate the array list for the total number of txns
		if(txnList.length<2){
			return false;
		}
		double dbAmt=0;
		double crAmt=0;
		try{
			for(int i=0;i<txnList.length;i++){
				Transaxtion txn=(Transaxtion)txnList[i];
				if(!validateGLCode(txn,con))return false;
				dbAmt+=Double.parseDouble(txn.getDrAmount());
				crAmt+=Double.parseDouble(txn.getCrAmount());
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return false;
		}
		dbAmt=ExilPrecision.convertToDouble(dbAmt,2);
		crAmt=ExilPrecision.convertToDouble(crAmt,2);
		LOGGER.info("Total Checking.....Debit total is :"+dbAmt+"  Credit total is :"+crAmt);
		if(dbAmt!=crAmt){
			throw new TaskFailedException("Total debit and credit not matching. Total debit amount is: "+dbAmt+" Total credit amount is :"+crAmt);
			//return false;
		}
   		return true;
	}
	public boolean postTransaxtions(Transaxtion txnList[],Connection con,DataCollection dc) throws Exception,TaskFailedException, ParseException,SQLException,EGOVException,ValidationException
	{
		if(!checkBudget(txnList))
			throw new TaskFailedException("Budgetary check is failed");
   			//if objects are lost load them
   		if(getGlAccountCodes() ==null ||	getGlAccountIds()==null || getAccountDetailType()==null ||
   			   getGlAccountCodes().size() ==0 || getGlAccountIds().size()==0 || getAccountDetailType().size()==0){
   				reLoadAccountData();
   			}
   		try{
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATEFORMAT,Constants.LOCALE);
			dt = sdf.parse( vdt );
			String dateformat = formatter.format(dt);
		    if(!validPeriod(dateformat,con)){
				dc.addMessage("exilPostingPeriodError");
				return false;
			}
			if(!validateTxns(txnList,dc,con))
				return false;
		}catch(Exception e){
   				throw new  TaskFailedException();
		}
   		if(dc.getValue("modeOfExec").toString().equalsIgnoreCase("edit")){
   			if(!updateInGL(txnList,con,dc)){
   	   			return false;
   	   		}
   		}
   		else{
			if(!postInGL(txnList,con,dc)){
   				return false;
			}
		}
  		return true;
   }
	
	public void setBudgetDetailsDAO() {
		budgetDetailsDAO = new BudgetDetailsHibernateDAO(BudgetDetail.class,HibernateUtil.getCurrentSession());
		budgetDetailsDAO.setGenericDao(new GenericHibernateDaoFactory());
		PersistenceService service = new PersistenceService();
		service.setSessionFactory(new SessionFactory());
		budgetDetailsDAO.setPersistenceService(service);
	}
	public void setScriptService()
	{
		ScriptService scriptService = new ScriptService(100,100,100,100);
		scriptService.setSessionFactory(new SessionFactory());
		budgetDetailsDAO.setScriptExecutionService(scriptService);
		SequenceGenerator sequenceGenerator = new SequenceGenerator(new SessionFactory());
		budgetDetailsDAO.setSequenceGenerator(sequenceGenerator);
	}

   private boolean checkBudget(Transaxtion txnList[]) throws Exception,ValidationException
   {
	   Map<String,Object> paramMap = null;
	   Transaxtion txnObj=null;
	   PersistenceService<CVoucherHeader,Long> persistenceService = new PersistenceService<CVoucherHeader,Long>();
	   persistenceService.setSessionFactory(new SessionFactory());
	   persistenceService.setType(CVoucherHeader.class);
	   CVoucherHeader voucherHeader=null;
	   setBudgetDetailsDAO();
	   setScriptService();
	   for(int i=0;i<txnList.length;i++)
	   {
		   txnObj=txnList[i];
		   voucherHeader = persistenceService.findById(Long.valueOf(txnObj.voucherHeaderId), false);
		   paramMap = new HashMap<String,Object>();
		   if(txnObj.getDrAmount()==null || txnObj.getDrAmount().equals(""))
			   paramMap.put("debitAmt", null);
		   else
			   paramMap.put("debitAmt", new BigDecimal(txnObj.getDrAmount()+""));
		   if(txnObj.getCrAmount()==null || txnObj.getCrAmount().equals(""))
			   paramMap.put("creditAmt", null);
		   else
			   paramMap.put("creditAmt", new BigDecimal(txnObj.getCrAmount()+""));
		   if(voucherHeader.getFundId()!=null)
			   paramMap.put("fundid", voucherHeader.getFundId().getId());
		   if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			   paramMap.put("deptid", voucherHeader.getVouchermis().getDepartmentid().getId());
		   if(txnObj.functionId!=null && !txnObj.functionId.equals(""))
			   paramMap.put("functionid", Long.valueOf(txnObj.functionId));
		   if(voucherHeader.getVouchermis().getFunctionary()!=null)
			   paramMap.put("functionaryid",voucherHeader.getVouchermis().getFunctionary().getId());
		   if(voucherHeader.getVouchermis().getSchemeid()!=null)
			   paramMap.put("schemeid",voucherHeader.getVouchermis().getSchemeid().getId());
		   if(voucherHeader.getVouchermis().getSubschemeid()!=null)
			   paramMap.put("subschemeid",voucherHeader.getVouchermis().getSubschemeid().getId());
		   if(voucherHeader.getVouchermis().getDivisionid()!=null)
			   paramMap.put("boundaryid", voucherHeader.getVouchermis().getDivisionid().getId());
		   paramMap.put("glcode", txnObj.getGlCode());
		   paramMap.put("asondate", voucherHeader.getVoucherDate());
		   paramMap.put("mis.budgetcheckreq", voucherHeader.getVouchermis().isBudgetCheckReq());
		   paramMap.put("voucherHeader",voucherHeader);
		   if(txnObj.getBillId()!=null)
			   paramMap.put("bill",txnObj.getBillId());
		   if(!budgetDetailsDAO.budgetaryCheck(paramMap))
			   throw new Exception("Budgetary check is failed for "+txnObj.getGlCode());
	   }
	   return true;
   }
    public boolean postTransaxtions(Transaxtion txnList[],Connection con,String vDate)throws Exception,ValidationException
   	{
   		if(!checkBudget(txnList))
   			throw new Exception("Budgetary check is failed");
      			//if objects are lost load them
      	if(getGlAccountCodes() ==null ||	getGlAccountIds()==null || getAccountDetailType()==null ||
      			   getGlAccountCodes().size() ==0 || getGlAccountIds().size()==0 || getAccountDetailType().size()==0){
      				reLoadAccountData();
      	}
      	try
      	{
      		if(!validPeriod(vDate,con)){
		    	throw new TaskFailedException("Voucher Date is not within an open period. Please use an open period for posting");
			}
			if(!validateTxns(txnList,con))
				return false;
		}catch(Exception e){
			throw new TaskFailedException(e.getMessage());
		}
		if(!postInGL(txnList,con)){
			return false;
        }
   		return true;
   	}
   private void checkfuctreqd(String glcode, String fuctid,Connection con,DataCollection dc)throws Exception{


 	 Statement statement=con.createStatement();
 	 ResultSet rs=null;
   rs = statement.executeQuery("select FUNCTIONREQD from chartofaccounts where glcode ='"+glcode+"'");
   if(rs.next())
   {
   	if(rs.getInt(1)==1){
   	  if(fuctid.length()>0 && fuctid!=null){
   	  	LOGGER.info("in COA33--"+fuctid);
   	  }
   	  else{
   	  	dc.addMessage("exilError","Select functionName for this glcode "+glcode);
   	     	 throw new TaskFailedException();

   	  }
   	}

   }

 }
   private boolean checkfuctreqd(String glcode){
	   Session session = HibernateUtil.getCurrentSession();
	  List<CChartOfAccounts> list =(List<CChartOfAccounts>) session.createQuery("from CChartOfAccounts where functionReqd=1 and glcode='"+glcode+"'").list();
	  return list.size() == 1 ? true:false;
	 }
   private boolean postInGL(Transaxtion txnList[],Connection con,DataCollection dc)throws ParseException{
   		GeneralLedger gLedger=new GeneralLedger();
		GeneralLedgerDetail  gLedgerDet=new GeneralLedgerDetail();
		EgRemittanceGldtl  egRemitGldtl=new EgRemittanceGldtl();
		//DataExtractor de=DataExtractor.getExtractor();

		for(int i=0;i<txnList.length;i++){
			Transaxtion txn=txnList[i];
			LOGGER.info("GL Code is :"+txn.getGlCode()     +"  Debit Amount :"+Double.parseDouble(txn.getDrAmount())+" Credit Amt :"+Double.parseDouble(txn.getCrAmount()));
			//double dbAmt=Double.parseDouble(txn.getDrAmount());

			if((Double.parseDouble(txn.getDrAmount()))==0.0 && (Double.parseDouble(txn.getCrAmount()))==0.0){
				LOGGER.info("Comming in the zero  block");

			}
			else
			{	GLAccount glAcc=(GLAccount)getGlAccountCodes().get(txn.getGlCode());
				gLedger.setvoucherLineId(txn.getVoucherLineId());
				gLedger.setglCodeId(String.valueOf(glAcc.getId()));
				gLedger.setglCode(txn.getGlCode());
				gLedger.setdebitAmount(txn.getDrAmount());
				gLedger.setcreditAmount(txn.getCrAmount());
				gLedger.setdescription(txn.getNarration());
				gLedger.setvoucherHeaderId(txn.getVoucherHeaderId());
				LOGGER.info("Value of function in COA before setting :"+txn.getFunctionId());
				if(!(txn.getFunctionId()==null || txn.getFunctionId().equals("")))
				{
				  gLedger.setfunctionId(txn.getFunctionId());
				}
				else
				  gLedger.setfunctionId(null);
				LOGGER.info("txn.getGlCode()"+txn.getGlCode());
				LOGGER.info("txn.getFunctionId()"+txn.getFunctionId());
				String fucid=txn.getFunctionId();
				if(fucid!=null && !fucid.equals(""))

				   			try{
				   				checkfuctreqd(txn.getGlCode(),txn.getFunctionId(),con,dc);
				   				}
				   				catch(Exception e)
				   				{
				   					return false;
				   				}

				   				// gLedger.setfunctionId(txn.getFunctionId());
				   				 LOGGER.info("Value of function id in COA --"+txn.getFunctionId());


				try{
				//	LOGGER.info("inside the postin gl function before insert ----");
					gLedger.insert(con);
				}catch(Exception e){
					LOGGER.info("error in the gl++++++++++"+e);
					return false;
				}
				//if that code doesnot require any details no nedd to insert in GL details
				if(glAcc.getGLParameters().size()<=0)continue;
				ArrayList glParamList=glAcc.getGLParameters();
				LOGGER.info("glParamList size.... :"+glParamList.size());
				ArrayList txnPrm=txn.getTransaxtionParam();
				String detKeyId="";
				for(int a=0;a<glParamList.size();a++){
					try{
						//post the defaults set for details
						GLParameter glPrm=(GLParameter)glParamList.get(a);
				   	    /*if(!glPrm.getDetailKey().equalsIgnoreCase("0")&&glPrm.getDetailKey().length()>0){
				   	    	gLedgerDet.setGLId(String.valueOf(gLedger.getId()));
					 		gLedgerDet.setDetailTypeId(String.valueOf(glPrm.getDetailId()));
					 		gLedgerDet.setDetailKeyId(glPrm.getDetailKey());
					 		LOGGER.info("glPrm.getDetailAmt() in glPrm:"+glPrm.getDetailAmt());
					 		gLedgerDet.setDetailAmt(glPrm.getDetailAmt());
					 		gLedgerDet.insert(con);
					 		try
						   	 {
						   		 if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
						   		 {
							   	    egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
							   	    egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
							   		if(glPrm.getTdsId()!=null)
							   			egRemitGldtl.setTdsId(glPrm.getTdsId());
							   	    egRemitGldtl.insert(con);
						   		 }
						   	 }
						   	catch(Exception e)
						   	{
								LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
								return false;
							}
				   	    }else*/{ //Post  the details  sent  apart from defaults
				   	   		for(int z=0;z<txnPrm.size();z++){
				 	  			TransaxtionParameter tParam=(TransaxtionParameter)txnPrm.get(z);
				 	  			if(tParam.getDetailName().equalsIgnoreCase(glPrm.getDetailName()) && tParam.getGlcodeId().equalsIgnoreCase(gLedger.getglCodeId()))
				 	  			{
				 	  				detKeyId=(String)tParam.getDetailKey();
				 	  				gLedgerDet.setGLId(String.valueOf(gLedger.getId()));
							 		gLedgerDet.setDetailTypeId(String.valueOf(glPrm.getDetailId()));
							 		gLedgerDet.setDetailKeyId(detKeyId);
							 		LOGGER.info("glPrm.getDetailAmt() in tParam:"+tParam.getDetailAmt());
							 		gLedgerDet.setDetailAmt(tParam.getDetailAmt());
							 		gLedgerDet.insert(con);
							 		try
								   	 {
								   		 if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
								   		 {
									   	    egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
									   	    egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
										   	if(tParam.getTdsId()!=null)
										   		egRemitGldtl.setTdsId(tParam.getTdsId());
									   	    egRemitGldtl.insert(con);
								   		 }
								   	 }
								   	catch(Exception e)
								   	{
										LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
										return false;
									}
				 	  			}
				   	   		}
				   	   }
				   	     //post the gldetailid, gldtlamt to eg_remittance_gldtl table
					   	/* try
					   	 {
					   		 if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
					   		 {
						   	    egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
						   	    egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
						   	    egRemitGldtl.insert(con);
					   		 }
					   	 }
					   	catch(Exception e)
					   	{
							LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
							return false;
						}*/
					}catch(Exception e){
				 		dc.addMessage(EXILRPERROR,"General Ledger Details Error "+e.toString());
						return false;
				 	}
				}
			}
   }
		return true;
}


   private boolean postInGL(Transaxtion txnList[],Connection con)throws Exception{
	GeneralLedger gLedger=new GeneralLedger();
	GeneralLedgerDetail  gLedgerDet=new GeneralLedgerDetail();
	EgRemittanceGldtl  egRemitGldtl=new EgRemittanceGldtl();
//	DataExtractor de=DataExtractor.getExtractor();

	for(int i=0;i<txnList.length;i++){
		Transaxtion txn=txnList[i];
		LOGGER.info("GL Code is :"+txn.getGlCode()+ ":txn.getFunctionId()"+txn.getFunctionId()     +"  Debit Amount :"+String.valueOf(txn.getDrAmount())+" Credit Amt :"+String.valueOf(txn.getCrAmount()));
		if((String.valueOf(txn.getDrAmount()))=="0" && (String.valueOf(txn.getCrAmount()))=="0"){
			LOGGER.info("Comming in the zero  block");
			return false;
		}
		else
		{

			GLAccount glAcc=(GLAccount)getGlAccountCodes().get(txn.getGlCode());
			gLedger.setvoucherLineId(txn.getVoucherLineId());
			gLedger.setglCodeId(String.valueOf(glAcc.getId()));
			gLedger.setglCode(txn.getGlCode());
			gLedger.setdebitAmount(String.valueOf(txn.getDrAmount()));
			gLedger.setcreditAmount(String.valueOf(txn.getCrAmount()));
			gLedger.setdescription(txn.getNarration());
			gLedger.setvoucherHeaderId(txn.getVoucherHeaderId());
            LOGGER.info("Value of function in COA before setting :"+txn.getFunctionId());
            if(!(txn.getFunctionId()==null || txn.getFunctionId().trim().equals("") || txn.getFunctionId().equals("0")))
            {
                LOGGER.info("txn.getFunctionId()"+txn.getFunctionId());
                gLedger.setfunctionId(txn.getFunctionId());
            }
            else if(checkfuctreqd(txn.getGlCode())){
            	List<ValidationError> errors=new ArrayList<ValidationError>();
				 errors.add(new ValidationError("exp","function is required for account code : "+ txn.getGlCode()));
				 throw new ValidationException(errors);
            	 
            }else{
            	gLedger.setfunctionId(null);
            }
             

			try{
			//	LOGGER.info("inside the postin gl function before insert ----");
				gLedger.insert(con);
			}catch(Exception e){
				LOGGER.info("error in the gl++++++++++"+e);
					return false;
			}
				//if that code doesnot require any details no nedd to insert in GL details
			if(glAcc.getGLParameters().size()<=0)continue;
			ArrayList glParamList=glAcc.getGLParameters();
			ArrayList txnPrm=txn.getTransaxtionParam();
			String detKeyId="";
			LOGGER.info("glParamList size :"+glParamList.size());
			for(int a=0;a<glParamList.size();a++){
				try{
					//post the defaults set for details
					GLParameter glPrm=(GLParameter)glParamList.get(a);
			   	    /*if(!glPrm.getDetailKey().equalsIgnoreCase("0")&&glPrm.getDetailKey().length()>0){
			   	    	gLedgerDet.setGLId(String.valueOf(gLedger.getId()));
				 		gLedgerDet.setDetailTypeId(String.valueOf(glPrm.getDetailId()));
				 		gLedgerDet.setDetailKeyId(glPrm.getDetailKey());
				 		LOGGER.info("glPrm.getDetailAmt() in glParam:"+glPrm.getDetailAmt());
				 		gLedgerDet.setDetailAmt(glPrm.getDetailAmt());
				 		gLedgerDet.insert(con);
				 		 try
					   	 {
					   		if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
					   		 {
						   		 egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
						   	     egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
						   	     if(glPrm.getTdsId()!=null)
						   	    	 egRemitGldtl.setTdsId(glPrm.getTdsId());
						   	     egRemitGldtl.insert(con);
					   		 }
					   	 }
					   	catch(Exception e)
					   	{
							LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
							return false;
						}
			   	    }else*/{ //Post  the details  sent  apart from defaults
			   	   		for(int z=0;z<txnPrm.size();z++)
			   	   		{
			 	  			TransaxtionParameter tParam=(TransaxtionParameter)txnPrm.get(z);
			 	  			LOGGER.info("tParam.getGlcodeId():"+tParam.getGlcodeId());
			 	  			LOGGER.info("gLedger.getglCodeId():"+gLedger.getglCodeId());
			 	  			if(tParam.getDetailName().equalsIgnoreCase(glPrm.getDetailName()) && tParam.getGlcodeId().equalsIgnoreCase(gLedger.getglCodeId()))
			 	  			{
			 	  				detKeyId=(String)tParam.getDetailKey();
			 	  				gLedgerDet.setGLId(String.valueOf(gLedger.getId()));
						 		gLedgerDet.setDetailTypeId(String.valueOf(glPrm.getDetailId()));
						 		gLedgerDet.setDetailKeyId(detKeyId);
						 		gLedgerDet.setDetailAmt(tParam.getDetailAmt());
						 		gLedgerDet.insert(con);
						 		 try
							   	 {
							   		if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
							   		 {
								   		 egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
								   	     egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
								   	     if(tParam.getTdsId()!=null)
								   	    	 egRemitGldtl.setTdsId(tParam.getTdsId());
								   	     egRemitGldtl.insert(con);
							   		 }
							   	 }
							   	catch(Exception e)
							   	{
									LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
									return false;
								}
			 	  			}
			   	   		}
			   	   }
				}catch(Exception e){
			 		return false;
			 	}
			}
		}
	}
	return true;
}


   private boolean updateInGL(Transaxtion txnList[],Connection con,DataCollection dc)throws TaskFailedException,ParseException,SQLException{
	GeneralLedger gLedger=new GeneralLedger();
			GeneralLedgerDetail  gLedgerDet=new GeneralLedgerDetail();
			EgRemittanceGldtl  egRemitGldtl=new EgRemittanceGldtl();
		//	DataExtractor de=DataExtractor.getExtractor();
			ArrayList glHeaderId=new ArrayList();
			Transaxtion txn1=txnList[0];
			int VoucherHeaderId=Integer.parseInt(txn1.getVoucherHeaderId());
			LOGGER.info("VoucherHeaderId----"+VoucherHeaderId);
			Statement statement=con.createStatement();
			LOGGER.info("select id from generalledger where voucherheaderid="+VoucherHeaderId+" order by id");

			resultset = statement.executeQuery("select id from generalledger where voucherheaderid="+VoucherHeaderId+" order by id");
			int c=0;
			while(resultset.next()){

				glHeaderId.add(c,(resultset.getString("id")));
				c++;
			}

			int count=glHeaderId.size();
			LOGGER.info("count**********"+count);
			for(int k=0;k<count;k++)
			{
				try{
					String delremitsql="delete from eg_remittance_gldtl where gldtlid in (select id from generalledgerdetail where generalledgerid='"+glHeaderId.get(k).toString()+"')";
					LOGGER.info("deleting remittance Query "+delremitsql);
					statement.executeUpdate(delremitsql);
					LOGGER.info("delete from generalledgerdetail where generalledgerid='"+glHeaderId.get(k).toString()+"'");
					int del=statement.executeUpdate("delete from generalledgerdetail where generalledgerid='"+glHeaderId.get(k).toString()+"'");
				if(del>0)
					LOGGER.info("Records deleted from general ledger detail for GLH "+glHeaderId.get(k).toString());
				}catch(Exception e){
					LOGGER.info("Exp in reading from generalledgerdetail: "+e);
					throw new TaskFailedException(e.getMessage());
				}

			}
			/*//added by girish
			if(count>0)
			{
				try{
					//Statement statement=connection.createStatement();
					int del=statement.executeUpdate("DELETE FROM egf_mappedTds WHERE voucherheaderid="+VoucherHeaderId);
				if(del>0)
					LOGGER.info("DELETE FROM egf_mappedTds WHERE voucherheaderid="+VoucherHeaderId);

				}catch(Exception e){
					LOGGER.info("Exp in reading from egf_mappedTds: "+e);
				}
			}*/


			if(count>0)
			{
				try{
					//Statement statement=connection.createStatement();
					int del=statement.executeUpdate("DELETE FROM generalledger WHERE voucherheaderid="+VoucherHeaderId);
				if(del>0)
					LOGGER.info("DELETE FROM generalledger WHERE voucherheaderid="+VoucherHeaderId);

				}catch(Exception e){
					LOGGER.info("Exp in reading from generalledger: "+e);
				}
			}
			resultset.close();
			statement.close();

			for(int i=0;i<txnList.length;i++){
				Transaxtion  txn=txnList[i];
				GLAccount glAcc=(GLAccount)getGlAccountCodes().get(txn.getGlCode());
				gLedger.setvoucherLineId(txn.getVoucherLineId());
				gLedger.setglCodeId(String.valueOf(glAcc.getId()));
				gLedger.setglCode(txn.getGlCode());
				gLedger.setdebitAmount(String.valueOf(txn.getDrAmount()));
				gLedger.setcreditAmount(String.valueOf(txn.getCrAmount()));
				gLedger.setdescription(txn.getNarration());
				gLedger.setvoucherHeaderId(txn.getVoucherHeaderId());
				gLedger.setfunctionId(txn.getFunctionId());

				try{
				//	LOGGER.info("inside the postin gl function before insert ----");
					gLedger.insert(con);
				}catch(Exception e){
					LOGGER.info("error in the gl++++++++++"+e);
					dc.addMessage("exilSQLError",e.toString());
						return false;
				}
				//if that code doesnot require any details no nedd to insert in GL details
				if(glAcc.getGLParameters().size()<=0)continue;
				ArrayList glParamList=glAcc.getGLParameters();
				ArrayList txnPrm=txn.getTransaxtionParam();
				String detKeyId="";
				for(int a=0;a<glParamList.size();a++){
					try{
						//post the defaults set for details
						GLParameter glPrm=(GLParameter)glParamList.get(a);
				   	    /*if(!glPrm.getDetailKey().equalsIgnoreCase("0")&&glPrm.getDetailKey().length()>0){
				   	    	gLedgerDet.setGLId(String.valueOf(gLedger.getId()));
					 		gLedgerDet.setDetailTypeId(String.valueOf(glPrm.getDetailId()));
					 		gLedgerDet.setDetailKeyId(glPrm.getDetailKey());
					 		gLedgerDet.setDetailAmt(glPrm.getDetailAmt());
					 		gLedgerDet.insert(con);
					 		 try
						   	 {
						   		if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
						   		 {
							   		 egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
							   	     egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
							   	     if(glPrm.getTdsId()!=null)
							   	    	 egRemitGldtl.setTdsId(glPrm.getTdsId());
							   	     egRemitGldtl.insert(con);
						   		 }
						   	 }
						   	catch(Exception e)
						   	{
								LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
								return false;
							}
				   	    }else*/{ //Post  the details  sent  apart from defaults
				   	   		for(int z=0;z<txnPrm.size();z++){
				 	  			TransaxtionParameter tParam=(TransaxtionParameter)txnPrm.get(z);
				 	  			if(tParam.getDetailName().equalsIgnoreCase(glPrm.getDetailName()) && tParam.getGlcodeId().equalsIgnoreCase(gLedger.getglCodeId()))
				 	  			{
				 	  				detKeyId=(String)tParam.getDetailKey();
				 	  				gLedgerDet.setGLId(String.valueOf(gLedger.getId()));
							 		gLedgerDet.setDetailTypeId(String.valueOf(glPrm.getDetailId()));
							 		gLedgerDet.setDetailKeyId(detKeyId);
							 		gLedgerDet.setDetailAmt(tParam.getDetailAmt());
							 		gLedgerDet.insert(con);
							 		 try
								   	 {
								   		if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
								   		 {
									   		 egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
									   	     egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
									   	     if(tParam.getTdsId()!=null)
									   	    	 egRemitGldtl.setTdsId(tParam.getTdsId());
									   	     egRemitGldtl.insert(con);
								   		 }
								   	 }
								   	catch(Exception e)
								   	{
										LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
										return false;
									}
				 	  			}
				   	   		}
				   	   }
				   	    //post the gldetailid, gldtlamt to eg_remittance_gldtl table
					   	/* try
					   	 {
					   		if(validRecoveryGlcode(gLedger.getglCodeId(),con) && Double.parseDouble(gLedger.getcreditAmount())>0)
					   		 {
						   		 egRemitGldtl.setGldtlId(String.valueOf(gLedgerDet.getId()));
						   	     egRemitGldtl.setGldtlAmt(gLedgerDet.getDetailAmt());
						   	     egRemitGldtl.insert(con);
					   		 }
					   	 }
					   	catch(Exception e)
					   	{
							LOGGER.error("Error while inserting to eg_remittance_gldtl "+e);
							return false;
						}*/
					}catch(Exception e){
				 		dc.addMessage(EXILRPERROR,"General Ledger Details Error "+e.toString());
						return false;
				 	}
				}
				/*Post all the details  sent  apart from defaults
				for(int j=0;j<getAccountDetailType().size();j++){
			 	   try{

				 	}catch(Exception e){
				 		dc.addMessage(EXILRPERROR,"General Ledger Details Error "+e.toString());
						return false;
				 	}
				}*/
			}
			//LOGGER.info("HI- 396-6");// TBR
			return true;
}

   public String getGLCode(String detailName,String detailKey,Connection con)throws TaskFailedException{
   	String code="";
   	try{
	Statement statement = con.createStatement();
	String str="select glcode as \"code\" from chartofaccounts,bankaccount where bankaccount.glcodeid=chartofaccounts.id and bankaccount.id="+detailKey;
   	ResultSet resultset = statement.executeQuery(str);

   	if(resultset.next())
   	code=resultset.getString("code");
   	}
   	catch(Exception e)
	{
   		LOGGER.info("error"+e.toString());
	}
   	return code;
   }

   public String getFiscalYearID(String voucherDate,Connection con,DataCollection dc){
   		String fiscalyearid="";
   		String sql="select ID as \"fiscalperiodID\" from fiscalperiod where "+
		   "to_date('"+voucherDate+"','dd-mon-yyyy') between startingdate and endingdate";
   		try{
   			Statement statement=con.createStatement();
   			ResultSet rs=statement.executeQuery(sql);
   			if(rs.next())
   				fiscalyearid=rs.getString("fiscalperiodID");
   		}catch(Exception e){
   			LOGGER.debug("Excepion in getFiscalYearID() "+e);
   		}
   		return fiscalyearid;
}
 
   private boolean validPeriod(String vDate,Connection con){
   	try{
   		if(ClosedPeriods.isClosedForPosting(vDate,con)){
   			return false;
   		}
   	}catch(Exception e){
   		return false;
   	}
   	return true;
   }
   
   public void test()  throws TaskFailedException{
		Iterator it=getGlAccountCodes().keySet().iterator();
		while(it.hasNext()){
			GLAccount glAcc=(GLAccount)getGlAccountCodes().get(it.next());
			ArrayList a=glAcc.getGLParameters();
			for(int i=0;i<a.size();i++){
				GLParameter glp=(GLParameter)a.get(i);
			}
		}
   }

/**
 * @return Returns the getAccountDetailType().
 */
public static HashMap getAccountDetailType()
{
	LOGGER.debug("in getAccountDetailType():jndi name is :"+EGOVThreadLocals.getDomainName());
	HashMap retMap = null;
	try
	{
		HashMap cacheValuesHashMap=new HashMap<Object, Object>();
		cacheValuesHashMap=(HashMap)cache.get(ROOTNODE + "/" + (String)EGOVThreadLocals.getDomainName());
		if(cacheValuesHashMap!=null && !cacheValuesHashMap.isEmpty())
		{
			retMap = (HashMap) cacheValuesHashMap.get(ACCOUNTDETAILTYPENODE);
		}
		
	} catch (Exception e)
	{
		LOGGER.debug(EXP+e.getMessage());
		throw new EGOVRuntimeException(e.getMessage());
	}
	return retMap;
}
	/**
	 * @return Returns the getGlAccountCodes().
	 */
	public static HashMap getGlAccountCodes()
	{
		LOGGER.debug("in getGlAccountCodes():jndi name is :"+EGOVThreadLocals.getDomainName());
		HashMap retMap = null;
		try
		{
			HashMap cacheValuesHashMap=new HashMap<Object, Object>();
			cacheValuesHashMap=(HashMap)cache.get(ROOTNODE + "/" + (String)EGOVThreadLocals.getDomainName());
			if(cacheValuesHashMap!=null && !cacheValuesHashMap.isEmpty())
			{
				retMap = (HashMap) cacheValuesHashMap.get(GLACCCODENODE);
			}
			if(retMap!=null)
			{
				LOGGER.debug("in getGlAccountCodes() size is :"+ retMap.size());
			}

		} catch (Exception e)
		{
			LOGGER.debug(EXP+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return retMap;
	}
	/**
	 * @return Returns the getGlAccountIds().
	 */
	public static HashMap getGlAccountIds()
	{
		LOGGER.debug("in getGlAccountIds():jndi name is :"+EGOVThreadLocals.getDomainName());
		HashMap retMap = null;
		try
		{
			HashMap cacheValuesHashMap=new HashMap<Object, Object>();
			cacheValuesHashMap=(HashMap)cache.get(ROOTNODE + "/" + (String)EGOVThreadLocals.getDomainName());
			if(cacheValuesHashMap!=null && !cacheValuesHashMap.isEmpty())
			{
				retMap = (HashMap) cacheValuesHashMap.get(GLACCIDNODE);
			}

		} catch (Exception e)
		{
			LOGGER.debug(EXP+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return retMap;
	}

	 private boolean validRecoveryGlcode(String glcodeId,Connection con)
	 {
	   	try
	   	{
	   		Statement st=con.createStatement();
	   		String query="select id from tds where glcodeid="+ glcodeId + " and isactive='1'";
	   		LOGGER.info("query-->"+query);
	   		ResultSet rset=st.executeQuery(query);
	   		if(rset.next())
	   		{
	   		 	if(rset.getString(1)=="0")
	   				return false;
	   		 }
	   		else
	   			return false;

	   		rset.close();
			st.close();
	   	}
	   	catch(Exception e)
	   	{
	   		return false;
	   	}
	   	return true;
	 }
	 public void setCacheInstance(Cache<Object, Object> cacheInstance) {
			cache = cacheInstance; 
		}
}
