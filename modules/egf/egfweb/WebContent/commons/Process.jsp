

<%@page import="org.egov.billsaccounting.services.WorksBillService"%>
<%@page import="org.egov.billsaccounting.services.SalaryBillService"%>
<%@page import="org.egov.commons.service.CommonsServiceImpl"%>
<%@page import="org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl"%>
<%@ page language="java" import="java.sql.*,java.util.HashMap,java.util.*,org.egov.lib.rjbac.dept.DepartmentImpl,
org.egov.billsaccounting.services.SalaryBillServiceImpl,
org.egov.billsaccounting.model.Contractorbilldetail,org.egov.billsaccounting.model.Supplierbilldetail, 
org.egov.commons.CVoucherHeader, 
org.egov.utils.GetEgfManagers, java.text.*,java.math.BigDecimal,
org.egov.commons.Fundsource,org.egov.commons.Relation,
org.egov.infstr.utils.EGovConfig,org.apache.log4j.Logger,
org.egov.billsaccounting.model.Salarybilldetail,org.egov.commons.Bankaccount,org.egov.infstr.utils.HibernateUtil,org.hibernate.jdbc.Work"%>

<%

List<String[]> resultList=null;
StringBuffer accCode=new StringBuffer();
DepartmentServiceImpl departmentServiceImpl= new DepartmentServiceImpl();
CommonsServiceImpl commonsServiceImpl= new CommonsServiceImpl();
SalaryBillServiceImpl salaryBillServiceImpl= new SalaryBillServiceImpl();

WorksBillService worksBillService = GetEgfManagers.getWorksBillService();
//Based on the type we will execute the query
Logger logger = Logger.getLogger("Process.jsp");
try{
if(request.getParameter("type").equalsIgnoreCase("coaSubMinorCode")){

String accountCode=request.getParameter("id");
String classValue=request.getParameter("classification");
String query="select glcode as \"code\" from chartofaccounts where classification='"+classValue+"' and glcode like '"+accountCode+"'|| '%' order by glcode ";
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllCoaCodes")){
String query="select glcode||'`-`'||name||'`~`'||ID as code from chartofaccounts where classification=4 and isactiveforposting = 1 order by glcode ";
logger.info("query :"+query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllCoaCodesExceptCashBank")){
String query="SELECT glcode || '`-`' || NAME || '`-`' || ID AS \"code\" FROM chartofaccounts   WHERE classification = 4 AND isactiveforposting = 1 AND parentid  not in(select id  from chartofaccounts where purposeid in ( SELECT ID FROM egf_accountcode_purpose WHERE UPPER (NAME) = UPPER ('Cash In Hand') OR UPPER (NAME) = UPPER ('Bank Codes') OR UPPER (NAME) = UPPER ('Cheque In Hand')) ) "+
			" and id  not in(select id  from chartofaccounts where purposeid in ( SELECT ID FROM egf_accountcode_purpose WHERE UPPER (NAME) = UPPER ('Cash In Hand') OR UPPER (NAME) = UPPER ('Bank Codes') OR UPPER (NAME) = UPPER ('Cheque In Hand')) ) and glcode not like '471%' ORDER BY glcode ";
logger.info("query :"+query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllAssetCodes")){
String query="select glcode||'`-`'||name|| '`-`' || ID as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'A' order by glcode ";
//String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'A' order by glcode ";
logger.info("query :"+query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllLiabCodes")){
String query="select glcode||'`-`'||name|| '`-`' || ID as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'L' order by glcode ";
//String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'L' order by glcode ";
logger.info("query :"+query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("coaDetailCode")){

String accountCode=request.getParameter("glCode");
String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and glcode like '"+accountCode+"'|| '%' order by glcode ";
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("coaDetailCodeType")){

String accountCode=request.getParameter("glCode");
String typeClass=request.getParameter("typeClass");
String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and glcode like '"+accountCode+"'|| '%' and type = '"+typeClass+"' order by glcode ";
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("functionName")){

String functionCode=request.getParameter("name");
String query="select name as \"code\" from function where  isactive = 1 AND isnotleaf=0 and upper(name) like upper('"+functionCode+"%')  order by name ";
logger.info(query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllFunctionName")){
String query="select name||'`~`'||id as \"code\" from function where  isactive = 1 AND isnotleaf=0 order by name ";
logger.info(query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}

else if(request.getParameter("type").equalsIgnoreCase("contractorName")){
 	String query="select name||'`-`'||code as \"code\" from relation where id in(select relationid from worksdetail where totalvalue>0 and isactive=1) and isactive=1 and relationTypeid=2  order by upper(\"code\") ";
 	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}

else if(request.getParameter("type").equalsIgnoreCase("supplierName")){
	String query="select name||'`-`'||code as \"code\" from relation where id in(select relationid from worksdetail where totalvalue>0 and isactive=1) and isactive=1 and relationTypeid=1  order by upper(\"code\") ";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("workDetailName")){
	String name=request.getParameter("name");
	String relTypeId=request.getParameter("relationTypeId");
	String relationId=request.getParameter("relationId");
	name=name+'%';
	String query="select ' '||wd.name||'`-`'||wd.code as \"code\" from worksdetail wd,relation r where wd.relationid=r.id and r.relationtypeid="+relTypeId+" and r.isactive=1 and wd.relationid="+relationId+"  order by upper(wd.name) ";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getGLreportCodes")){ 
	//logger.info("inside common function "+type);
	String query="SELECT concat(concat(concat(concat(glCode,'`-`'), name),'-$-'), ID) as \"code\" FROM chartofaccounts WHERE glcode not in (select glcode from chartofaccounts where glcode like '47%' AND glcode not like '471%' AND glcode !='4741') "+
		" AND glcode not in (select glcode from chartofaccounts where glcode='471%') AND isactiveforposting=1 AND classification=4 ORDER BY glcode ";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}

else if(request.getParameter("type").equalsIgnoreCase("getActiveContractorListwithCode")){ 
	logger.info("inside common function "+request.getParameter("type"));
	String query="SELECT   NAME || '`--`' || code  || '`-`' ||ID  AS \"code\"  FROM relation  WHERE 1 = 1 AND relationtypeid = 2 AND isactive = 1 ORDER BY NAME";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getActiveSupplierListwithCode")){ 
	logger.info("inside common function "+request.getParameter("type"));
	String query="SELECT   NAME || '`--`' || code  || '`-`' ||ID  AS \"code\"  FROM relation  WHERE 1 = 1 AND relationtypeid = 1 AND isactive = 1 ORDER BY NAME";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
// For Procurement Order-get all Party Name and Id based on the relationtypeid 
else if(request.getParameter("type").equalsIgnoreCase("getAllActivePartyName")){ 

	//logger.info("inside common function "+request.getParameter("type"));
	String relTypeId=request.getParameter("relationTypeId");
	
	String query="SELECT   NAME || '`-`' ||ID  AS \"code\"  FROM relation  WHERE relationtypeid="+relTypeId+" AND isactive = 1 ORDER BY upper(NAME)";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
// For Dishonored cheque fill Bank Charges glcode,Receipt Reversal and Payment Reversal Chque glcode
else if(request.getParameter("type").equalsIgnoreCase("getBankGlcode")){ 
	logger.info("inside common function "+request.getParameter("type"));
	String query="select ca.glcode || '`--`' || ca.name || '`--`' || ca.id as \"code\" "
	+" from Chartofaccounts ca where purposeid=30 ORDER BY ca.glcode";
	
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getChqueGlcode")){ 
	logger.info("inside common function "+request.getParameter("type"));
	
	String vouchHeaderId =request.getParameter("vouchHeaderId");
	
	String query="select gl.glcode || '`-`' || ca.name || '`-`' || gl.glcodeid || '`-`' || gl.creditamount as \"code\" "
	+" from generalledger gl,Chartofaccounts ca "
	+" WHERE gl.glcodeid=ca.id and gl.creditamount>0 and gl.VOUCHERHEADERID='"+vouchHeaderId+"' "
	+" ORDER BY gl.glcode";
		
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getGlcodeForPayReversal")){ 
	logger.info("inside common function "+request.getParameter("type"));
	
	String glcodeParam =request.getParameter("glcodeParam");
	
	String query="select ca.glcode || '`--`' || ca.name || '`--`' || ca.id as \"code\" "
	+" from Chartofaccounts ca where ca.isactiveforposting = 1 and ca.classification=4 and ca.glcode='"+glcodeParam+"' ";
	
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getBillNo")){
	//logger.info("inside common function "+type);
	String cgn =request.getParameter("cgn");
	String query=" SELECT eb.billnumber||'`-`'||v.id AS code FROM VOUCHERHEADER v,OTHERBILLDETAIL ob,EG_BILLREGISTER eb"+
                  " WHERE v.name='Expense Journal' AND v.id NOT IN"+
                  "(SELECT m.BILLVHID FROM MISCBILLDETAIL m, VOUCHERHEADER vh ,PAYMENTHEADER p WHERE m.billvhid IS NOT NULL "+
                  " AND (m.isreversed=0 OR m.isreversed IS NULL) AND p.VOUCHERHEADERID=vh.id  AND p.MISCBILLDETAILID=m.id AND"+
                  " vh.STATUS!=4 )   AND v.status=0 AND v.id = ob.voucherheaderid AND ob.billid =eb.id";
	String queryedit=" SELECT eb.billnumber||'`-`'||v.id AS code FROM VOUCHERHEADER v,OTHERBILLDETAIL ob,EG_BILLREGISTER eb"+
	                  " WHERE v.name='Expense Journal' AND v.id IN"+
	                  "(SELECT m.BILLVHID FROM MISCBILLDETAIL m, VOUCHERHEADER vh ,PAYMENTHEADER p WHERE m.billvhid IS NOT NULL "+
	                  " AND (m.isreversed=0 OR m.isreversed IS NULL) AND p.VOUCHERHEADERID=vh.id  AND p.MISCBILLDETAILID=m.id AND"+
                  " vh.STATUS!=4 and vh.cgn = '"+cgn+"')   AND v.status=0 AND v.id = ob.voucherheaderid AND ob.billid =eb.id";


	if((request.getParameter("mode").equalsIgnoreCase("paymentBank"))|| (request.getParameter("mode").equalsIgnoreCase("paymentCash")))
		  { logger.info(query);
		    resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
	      }
	      else
	       { logger.info(query+" union "+queryedit);
		  	 resultList=HibernateUtil.getCurrentSession().createSQLQuery(query+" union "+queryedit).list();
       }


}else 
if(request.getParameter("type").equalsIgnoreCase("payeeDetailCode")){
String query = null;

String detailCode=request.getParameter("detailCode");
String colName=request.getParameter("colName");
String tableName=request.getParameter("tableName");
logger.info("tableName" + tableName);
if(tableName.equalsIgnoreCase("eg_employee"))
{
logger.info("tableName inside process 2" + tableName);
 query="select name||'`-`'||id  as \"code\" from "+tableName+" where isactive=1 and code like '"+detailCode+"'  order by name  " ;
}else if(tableName.equalsIgnoreCase("eg_item"))
{
	
	query="select itemno||'`-`'||id  as \"code\" from "+tableName+" where itemno like '"+detailCode+"'  order by itemno  " ;
}
else
 query="select name||'`-`'||id  as \"code\" from "+tableName+" where isactive=1 and code like '"+detailCode+"'  order by name  " ;
logger.info(query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getBankAccountGlcode"))
{ 
	logger.info("inside common function "+request.getParameter("type"));
	
	String accountId =request.getParameter("accountId");
	
	String query="select ca.glcode || '`-`' || ca.name || '`-`' || ca.id as \"code\" "
	+" from bankaccount ba,Chartofaccounts ca "
	+" WHERE ba.glcodeid=ca.id and ba.id='"+accountId+"' ";	
		
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
 else if(request.getParameter("type").equalsIgnoreCase("getEntityDetail")){
	 
	 String glCode =request.getParameter("glCode");
	 String accEntityKey =request.getParameter("accEntityKey");
	//logger.info("inside common function "+type);
	String query="SELECT distinct concat ( concat ( concat ( concat ( decode ( adk.DETAILTYPEID ,(select id from accountdetailtype where UPPER(name)= UPPER('Creditor') ),(select code from relation where id = adk.detailkey),(select id from accountdetailtype where UPPER(name) = UPPER('Employee') ),(select code from eg_employee where id = adk.detailkey ),(select code from accountentitymaster where ID =adk.detailkey   AND detailtypeid = adk.detailtypeid )), '`-`' )," 
	+" decode ( adk.DETAILTYPEID ,(select id from accountdetailtype where UPPER(name) = UPPER('Creditor') ),(select name from relation where id = adk.detailkey ),(select id from accountdetailtype where UPPER(name) = UPPER('Employee') ),(select emp_firstname from eg_employee where id= adk.detailkey ),(select name from accountentitymaster where  detailtypeid = adk . detailtypeid AND ID = adk.detailkey ))), '-$-' ),adk.detailkey ) as \"code\" "
	+" from generalledgerdetail gld, accountdetailkey adk where gld.generalledgerid in (select id from generalledger where glcode = '"+glCode+"' ) and gld.detailkeyid = adk.detailkey and gld.detailtypeid = adk.detailtypeid and adk.DETAILTYPEID ="+accEntityKey+""
	+" UNION "
	+" SELECT distinct concat ( concat ( concat ( concat ( decode ( adk.DETAILTYPEID,(select id from accountdetailtype where UPPER(name)=UPPER('Creditor')),(select code from relation where id = adk.detailkey),(select id from accountdetailtype where UPPER(name) = UPPER('Employee') ),(select code from eg_employee where id = adk.detailkey ),(select code from accountentitymaster where detailtypeid = adk.detailtypeid AND ID = adk.detailkey)), '`-`' ),"
	+" decode ( adk.DETAILTYPEID,(select id from accountdetailtype where UPPER(name) = UPPER('Creditor') ),(select name from relation where id = adk.detailkey ),(select id from accountdetailtype where UPPER(name) = UPPER('Employee') ),(select emp_firstname from eg_employee where id= adk . detailkey ),(select name from accountentitymaster where detailtypeid = adk . detailtypeid  AND ID = adk.detailkey))), '-$-' ),adk.detailkey ) as \"code\" "
	+" from transactionsummary ts, accountdetailkey adk where ts.GLCODEID=(select id from chartofaccounts where glcode='"+glCode+"') and ts.ACCOUNTDETAILTYPEID=adk.detailtypeid and adk.detailtypeid="+accEntityKey+" and ts.accountdetailkey=adk.detailkey";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getEntityDetailForAdvRegister")){

	String arMajorGLCode=EGovConfig.getProperty("egf_config.xml","ARGLCode","","AdvanceRegisterCode");
	//logger.info("AdvanceRegister arMajorGLCode--->"+arMajorGLCode);
	 
	  String accEntityKeyId =request.getParameter("partyTypeId");
	  String paramFromDate =request.getParameter("paramFromDate");
	  String paramToDate =request.getParameter("paramToDate");
	 
	//logger.info("Inside Process.jsp function "+type);
	//logger.info("Inside Process.jsp partyTypeId-->"+accEntityKeyId);
	
	String query="SELECT DISTINCT CONCAT(CONCAT(CONCAT(CONCAT("
	+" DECODE (adt.tablename ,'accountEntityMaster',(SELECT name FROM accountentitymaster WHERE id= adk.detailkey ),"
	+" 'relation',(SELECT name FROM relation WHERE id = adk.detailkey ),"
	+" 'eg_employee',(SELECT emp_firstname FROM eg_employee WHERE id= adk.detailkey )),'`-`'),"
	+" DECODE(adt.tablename ,'accountEntityMaster',(SELECT code FROM accountentitymaster WHERE id= adk.detailkey ),"
	+" 'relation',(SELECT code FROM relation WHERE id = adk.detailkey ),"
	+" 'eg_employee',(SELECT code FROM eg_employee WHERE id= adk.detailkey ))),'-$-' ),"
	+" adk.detailkey ) AS \"code\""
	+" FROM generalledgerdetail gld, accountdetailkey adk,accountdetailtype adt"
	+" WHERE gld.generalledgerid IN (SELECT id FROM generalledger WHERE EFFECTIVEDATE>=TO_DATE('"+paramFromDate+"','dd/MM/yyyy') AND EFFECTIVEDATE<=TO_DATE('"+paramToDate+"','dd/MM/yyyy') AND glcode LIKE '"+arMajorGLCode+"%"+"')"
	+" AND adt.id = adk.detailtypeid AND gld.detailkeyid = adk.detailkey AND gld.detailtypeid = adk.detailtypeid "
	+" AND adk.DETAILTYPEID="+accEntityKeyId+""
	+" UNION SELECT DISTINCT CONCAT ( CONCAT ( CONCAT ( CONCAT ( DECODE "
	+" ( adt.tablename,'relation',(SELECT name FROM relation WHERE id = adk.detailkey),"
	+" 'eg_employee',(SELECT emp_firstname FROM eg_employee WHERE id = adk.detailkey ),"
	+" 'accountEntityMaster',(SELECT name FROM accountentitymaster "
	+" WHERE detailtypeid = adk.detailtypeid )), '`-`' ),"
	+" DECODE ( adt.tablename,'relation',(SELECT code FROM relation WHERE id = adk.detailkey ),"
	+" 'eg_employee',(SELECT code FROM eg_employee WHERE id= adk.detailkey ),"
	+" 'accountEntityMaster',(SELECT code FROM accountentitymaster WHERE detailtypeid = adk.detailtypeid ))), '-$-' ),"
	+" adk.detailkey ) AS \"code\""
	+" FROM transactionsummary ts, accountdetailkey adk,accountdetailtype adt"
	+" WHERE ts.GLCODEID IN(SELECT id FROM chartofaccounts WHERE glcode LIKE '"+arMajorGLCode+"%"+"')"
	+" AND adt.id=adk.detailtypeid AND ts.ACCOUNTDETAILTYPEID = adk.detailtypeid AND adk.DETAILTYPEID="+accEntityKeyId+""
	+" AND ts.accountdetailkey=adk.detailkey AND ts.FINANCIALYEARID IN(SELECT id FROM financialyear "
	+" WHERE startingdate<=TO_DATE('"+paramFromDate+"','dd/MM/yyyy') AND endingdate>=TO_DATE('"+paramToDate+"','dd/MM/yyyy'))";
	
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("checkPartyNameForAdvRegister"))
{
		logger.info("inside common function--->checkPartyNameForAdvRegister");
		
		String arMajorGLCode=EGovConfig.getProperty("egf_config.xml","ARGLCode","","AdvanceRegisterCode");
		String accEntityKeyId =request.getParameter("partyTypeId");
		String partyDetailkey =request.getParameter("paramPartyKey");
		String partyName =request.getParameter("paramPartyName");
		String paramFromDate =request.getParameter("paramFromDate");
	 	String paramToDate =request.getParameter("paramToDate");
		
		String query="SELECT DISTINCT DECODE (adt.tablename,'accountEntityMaster', "
		+" (SELECT NAME FROM accountentitymaster WHERE ID =adk.detailkey AND UPPER(NAME)=UPPER('"+partyName+"')),"
		+" 'relation', (SELECT NAME  FROM relation WHERE ID = adk.detailkey AND UPPER(NAME)=UPPER('"+partyName+"')),"
		+" 'eg_employee', (SELECT emp_firstname FROM eg_employee  WHERE ID = adk.detailkey AND UPPER(NAME)=UPPER('"+partyName+"'))) AS \"code\""
		+" FROM generalledgerdetail gld,accountdetailkey adk,accountdetailtype adt"
		+" WHERE gld.generalledgerid IN (SELECT id FROM generalledger WHERE EFFECTIVEDATE>=TO_DATE('"+paramFromDate+"','dd/MM/yyyy') AND EFFECTIVEDATE<=TO_DATE('"+paramToDate+"','dd/MM/yyyy') AND glcode LIKE '"+arMajorGLCode+"%"+"')"
		+" AND adt.ID = adk.detailtypeid AND gld.detailkeyid = adk.detailkey"
		+" AND gld.detailtypeid = adk.detailtypeid AND adk.DETAILTYPEID="+accEntityKeyId+" AND adk.detailkey="+partyDetailkey+""
		+" UNION SELECT DISTINCT DECODE (adt.tablename, 'relation',(SELECT NAME FROM relation WHERE ID =adk.detailkey AND UPPER(NAME)=UPPER('"+partyName+"')),"
		+" 'eg_employee', (SELECT emp_firstname FROM eg_employee WHERE ID =adk.detailkey AND UPPER(NAME)=UPPER('"+partyName+"')),'accountEntityMaster', "
		+" (SELECT NAME FROM accountentitymaster WHERE detailtypeid =adk.detailtypeid AND UPPER(NAME)=UPPER('"+partyName+"')))"
		+" AS \"code\" FROM transactionsummary ts,accountdetailkey adk,accountdetailtype adt"
		+" WHERE ts.glcodeid IN (SELECT ID FROM chartofaccounts WHERE glcode LIKE '"+arMajorGLCode+"%"+"')"
		+" AND adt.ID = adk.detailtypeid AND ts.accountdetailtypeid = adk.detailtypeid AND ts.accountdetailkey = adk.detailkey"
		+" AND adk.DETAILTYPEID="+accEntityKeyId+" AND adk.detailkey="+partyDetailkey+" AND ts.FINANCIALYEARID IN(SELECT id FROM financialyear "
		+" WHERE startingdate<=TO_DATE('"+paramFromDate+"','dd/MM/yyyy') AND endingdate>=TO_DATE('"+paramToDate+"','dd/MM/yyyy'))";
	
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list(); 
}
else if(request.getParameter("type").equalsIgnoreCase("getSubLedgerScheduleCodes"))
{
		 //logger.info("inside common function "+type);
		String query="SELECT UNIQUE CONCAT(CONCAT(CONCAT(CONCAT(coa.glCode,'`-`'), coa.name),'-$-'), coa.ID) AS \"code\" FROM" 
		+" chartOfAccounts coa,chartOfAccountDetail cod  WHERE coa.id = cod.glCodeId AND"
		+" coa.classification=4";
	   logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list(); 
}
else if(request.getParameter("type").equalsIgnoreCase("getCBill"))
{		
		 int j = 0;
		 /*CbillManager cbm = GetEgfManagers.getCbillManager();
		String deptId=request.getParameter("departmentId");
		String functionary=request.getParameter("functionaryId");
			DepartmentImpl dept=null;
if(deptId!=null)
	{
dept=(DepartmentImpl)departmentServiceImpl.getDepartment(Integer.parseInt(deptId));
	}
        String fundId=request.getParameter("fundId");
        Fundsource fundSource=null;
        String fundSourceId=request.getParameter("fundSourceId");
         
        if(!fundSourceId.equals(""))
        {
        	fundSource=commonsServiceImpl.getFundSourceById(new Integer(fundSourceId));
        }
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date vhFromDate = null;
        java.util.Date vhToDate = null;
        String voucherFromDate=request.getParameter("voucherFromDate");
        String voucherToDate=request.getParameter("voucherToDate");
        if(!voucherFromDate.equals(""))
        	vhFromDate = dateFormat.parse(voucherFromDate);       
        if(!voucherToDate.equals(""))
        	vhToDate = dateFormat.parse(voucherToDate);
        
		java.util.Date billFromDate = null;
        java.util.Date billToDate = null;	
		String billFromDate1=request.getParameter("billFromDate");
        String billToDate1=request.getParameter("billToDate");
        if(!billFromDate1.equals(""))
        	billFromDate = dateFormat.parse(billFromDate1);       
        if(!billToDate1.equals(""))
        	billToDate = dateFormat.parse(billToDate1);
        String vhNoFrom=null, vhNoTo=null;
        String cBillVhNoFrom = request.getParameter("cBillVhNoFrom");
        String cBillVhNoTo = request.getParameter("cBillVhNoTo");
        if(!cBillVhNoFrom.equals(""))
        	vhNoFrom=cBillVhNoFrom;
        if(!cBillVhNoTo.equals(""))	
        	vhNoTo=cBillVhNoTo;
		ArrayList cBillList=(ArrayList)cbm.getCBillFilterBy(new Integer(fundId), fundSource, vhFromDate, vhToDate, billFromDate, billToDate, vhNoFrom,vhNoTo,dept,functionary);
		if(cBillList!=null && cBillList.size()>0)
		{
			for (Iterator it = cBillList.iterator(); it.hasNext(); ) 
			{
				OtherBillDetail cBill=(OtherBillDetail)it.next();
				String dedAmt= commonsServiceImpl.getCBillDeductionAmtByVhId(cBill.getVoucherheaderByVoucherheaderid().getId());
				BigDecimal netAmt=cBill.getEgBillregister().getPassedamount().subtract(new BigDecimal(dedAmt)); 
				if(j > 0)
				{
					accCode.append("$");
					accCode.append(cBill.getId().toString()+"~"+cBill.getVoucherheaderByVoucherheaderid().getVoucherNumber()+"~"+dateFormat.format(cBill.getVoucherheaderByVoucherheaderid().getVoucherDate())+"~"+cBill.getEgBillregister().getPassedamount()+"~"+dedAmt+"~"+"0"+"~"+netAmt);
				}
				else
				{
					accCode.append(cBill.getId().toString()+"~"+cBill.getVoucherheaderByVoucherheaderid().getVoucherNumber()+"~"+dateFormat.format(cBill.getVoucherheaderByVoucherheaderid().getVoucherDate())+"~"+cBill.getEgBillregister().getPassedamount()+"~"+dedAmt+"~"+"0"+"~"+netAmt);
				}
				j++;
			}
			accCode.append("^");
		} */
}

else if(request.getParameter("type").equalsIgnoreCase("getAllTdsCodes"))
	{   String accountCode=request.getParameter("id");
String query=" select t.id||'`-`'||t.type||'`-`'||ca.glcode as\"code\" from tds t,egw_works_deductions ed,chartofaccounts ca,worksdetail w  where t.id=ed.tdsid and ed.worksdetailid="+accountCode+" AND w.id="+accountCode+"  and t.glcodeid=ca.id" ;

logger.info("query :"+query);
try{
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}catch(Exception e)
		{
			logger.debug(e.getMessage());
		}

}


else if(request.getParameter("type").equalsIgnoreCase("getTdsAmount"))
	{  
String tdsid=request.getParameter("id");
String amount=request.getParameter("amt");


String query="SELECT DISTINCT   DECODE (dt.incometax,NULL, 0.0,dt.incometax)+DECODE (dt.surcharge, NULL, 0.0, dt.surcharge)                + DECODE (dt.education, NULL, 0.0, dt.education) ||'`-`'||DECODE (dt.amount, NULL, '0', dt.amount) ||'`-`'||b.glcode ||'`-`'||b.name AS \"code\" FROM tds t, eg_deduction_details dt, chartofaccounts b WHERE t.ID = dt.tdsid AND t.glcodeid = b.ID             AND SYSDATE BETWEEN dt.datefrom AND DECODE (dt.dateto, NULL, SYSDATE, dt.dateto) AND "+amount+" BETWEEN dt.lowlimit                          AND DECODE (dt.highlimit,NULL, 99999999999999999999999,dt.highlimit)AND t.id="+tdsid;
logger.info("query :"+query);
try{
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}catch(Exception e)
		{
	logger.info(e.getMessage());
		}

}
else if(request.getParameter("type").equalsIgnoreCase("getConSupName"))
{
String worksDetId=request.getParameter("worksdetailid");
String query="SELECT r.id||'`-`'||r.name  as \"code\" FROM relation r ,worksdetail w WHERE r.id=w.relationid AND w.id="+worksDetId;
logger.info("===================================The Contractor Supplier name Query is"+query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("checkSubLedgerType"))
{
	String subledgercode=request.getParameter("subledgercode");
	String query="SELECT DISTINCT detailtypeid as \"code\" FROM CHARTOFACCOUNTDETAIL WHERE glcodeid=(SELECT id FROM CHARTOFACCOUNTS WHERE glcode='"+subledgercode+"') ";
	logger.info("subledger Query is====="+query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
	
else if(request.getParameter("type").equalsIgnoreCase("getSalaryBill"))
{		
		int j = 0;
		
           
        SalaryBillService salaryBillService = GetEgfManagers.getSalaryBillService();
        
        String fundId=request.getParameter("fundId");
        Fundsource fundSource=null;
        String fundSourceId=request.getParameter("fundSourceId");	
		String deptId=request.getParameter("departmentId");
		String functionary=request.getParameter("functionaryId");
			DepartmentImpl dept=null;
if(deptId!=null)
	{
dept=(DepartmentImpl)departmentServiceImpl.getDepartment(Integer.parseInt(deptId));
	}

         
        if(!fundSourceId.equals(""))
        {
        	fundSource=commonsServiceImpl.getFundSourceById(new Integer(fundSourceId));
        }
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date vhFromDate = null;
        java.util.Date vhToDate = null;
        String voucherFromDate=request.getParameter("voucherFromDate");
        String voucherToDate=request.getParameter("voucherToDate");
        if(!voucherFromDate.equals(""))
        	vhFromDate = dateFormat.parse(voucherFromDate);       
        if(!voucherToDate.equals(""))
        	vhToDate = dateFormat.parse(voucherToDate);
        	
		String month=null;
        if(!request.getParameter("month").equals(""))
        {        
        	month=request.getParameter("month");
        }
        String vhNoFrom=null, vhNoTo=null;
        String salBillVhNoFrom = request.getParameter("salBillVhNoFrom");
        String salBillVhNoTo = request.getParameter("salBillVhNoTo");
        if(!salBillVhNoFrom.equals(""))
        	vhNoFrom=salBillVhNoFrom;
        if(!salBillVhNoTo.equals(""))	
        	vhNoTo=salBillVhNoTo;
		ArrayList salaryBillList=(ArrayList)salaryBillService.getSalarybilldetailFilterBy(new Integer(fundId), fundSource, vhFromDate, vhToDate,month,vhNoFrom,vhNoTo,dept,functionary);
		if(salaryBillList!=null && salaryBillList.size()>0)
		{
			for (Iterator it = salaryBillList.iterator(); it.hasNext(); ) 
			{
				Salarybilldetail salBill=(Salarybilldetail)it.next();
				BigDecimal netAmt=salBill.getGrosspay().subtract(salBill.getPaidamount()).subtract(salBill.getTotaldeductions()); 
				if(j > 0)
				{
					accCode.append("$");
					accCode.append(salBill.getId().toString()+"~"+salBill.getVoucherheader().getVoucherNumber()+"~"+dateFormat.format(salBill.getVoucherheader().getVoucherDate())+"~"+salBill.getGrosspay()+"~"+salBill.getTotaldeductions()+"~"+salBill.getPaidamount()+"~"+netAmt);
				}
				else
				{
					accCode.append(salBill.getId().toString()+"~"+salBill.getVoucherheader().getVoucherNumber()+"~"+dateFormat.format(salBill.getVoucherheader().getVoucherDate())+"~"+salBill.getGrosspay()+"~"+salBill.getTotaldeductions()+"~"+salBill.getPaidamount()+"~"+netAmt);
				}
				j++;
			}
			accCode.append("^");
		}
}
else if(request.getParameter("type").equalsIgnoreCase("getContractorBill"))
{		
		int j = 0;
		
        
        String fundId=request.getParameter("fundId");
        Fundsource fundSource=null;
        String fundSourceId=request.getParameter("fundSourceId");
       	String deptId=request.getParameter("departmentId");
		String functionary=request.getParameter("functionaryId");
			DepartmentImpl dept=null;
if(deptId!=null)
	{
dept=(DepartmentImpl)departmentServiceImpl.getDepartment(Integer.parseInt(deptId));
	}
        if(!fundSourceId.equals(""))
        {
        	fundSource=commonsServiceImpl.getFundSourceById(new Integer(fundSourceId));
        }
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date vhFromDate = null;
        java.util.Date vhToDate = null;
        String voucherFromDate=request.getParameter("voucherFromDate");
        String voucherToDate=request.getParameter("voucherToDate");
        if(!voucherFromDate.equals(""))
        	vhFromDate = dateFormat.parse(voucherFromDate);       
        if(!voucherToDate.equals(""))
        	vhToDate = dateFormat.parse(voucherToDate);
        	
		Relation relation=null;
        String contractorId=request.getParameter("contractorId");         
        if(!contractorId.equals(""))
        {        
        	relation=commonsServiceImpl.getRelationById(new Integer(contractorId));
        }
        String vhNoFrom=null, vhNoTo=null;
        String conBillVhNoFrom = request.getParameter("conBillVhNoFrom");
        String conBillVhNoTo = request.getParameter("conBillVhNoTo");

        if(!conBillVhNoFrom.equals(""))
        	vhNoFrom=conBillVhNoFrom;
        if(!conBillVhNoTo.equals(""))	
        	vhNoTo=conBillVhNoTo;
		ArrayList contractorBillList=(ArrayList)worksBillService.getContractorBillDetailFilterBy(new Integer(fundId), fundSource,vhFromDate,vhToDate,relation,vhNoFrom,vhNoTo,dept,functionary);
		if(contractorBillList!=null && contractorBillList.size()>0)
		{
			for (Iterator it = contractorBillList.iterator(); it.hasNext(); ) 
			{
				Contractorbilldetail conBill=(Contractorbilldetail)it.next();
				CVoucherHeader vh=commonsServiceImpl.findVoucherHeaderById(conBill.getVoucherHeaderId());
				BigDecimal netAmt=conBill.getPassedamount().subtract(conBill.getPaidamount()).subtract(conBill.getTdsamount()).subtract(conBill.getAdvadjamt()).subtract(conBill.getOtherrecoveries()); 	
				BigDecimal dedAmt= conBill.getTdsamount().add(conBill.getOtherrecoveries());
				if(j > 0)
				{
					accCode.append("$");
					accCode.append(conBill.getId().toString()+"~"+vh.getVoucherNumber()+"~"+dateFormat.format(vh.getVoucherDate())+"~"+conBill.getPassedamount()+"~"+dedAmt+"~"+conBill.getPaidamount()+"~"+netAmt);
				}
				else
				{
					accCode.append(conBill.getId().toString()+"~"+vh.getVoucherNumber()+"~"+dateFormat.format(vh.getVoucherDate())+"~"+conBill.getPassedamount()+"~"+dedAmt+"~"+conBill.getPaidamount()+"~"+netAmt);
				}
				j++;
			}
			accCode.append("^");
		}
}
else if(request.getParameter("type").equalsIgnoreCase("getSupplierBill"))
{		
		int j = 0;
	    String fundId=request.getParameter("fundId");
        Fundsource fundSource=null;
        String fundSourceId=request.getParameter("fundSourceId");
         	String deptId=request.getParameter("departmentId");
		String functionary=request.getParameter("functionaryId");
			DepartmentImpl dept=null;
if(deptId!=null)
	{
dept=(DepartmentImpl)departmentServiceImpl.getDepartment(Integer.parseInt(deptId));
	}
        if(!fundSourceId.equals(""))
        {
        	fundSource=commonsServiceImpl.getFundSourceById(new Integer(fundSourceId));
        }
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date vhFromDate = null;
        java.util.Date vhToDate = null;
        String voucherFromDate=request.getParameter("voucherFromDate");
        String voucherToDate=request.getParameter("voucherToDate");
        if(!voucherFromDate.equals(""))
        	vhFromDate = dateFormat.parse(voucherFromDate);       
        if(!voucherToDate.equals(""))
        	vhToDate = dateFormat.parse(voucherToDate);
        	
		Relation relation=null;
        String supplierId=request.getParameter("supplierId");         
        if(!supplierId.equals(""))
        {        
        	relation=commonsServiceImpl.getRelationById(new Integer(supplierId));
        }
        String vhNoFrom=null, vhNoTo=null;
        String supBillVhNoFrom = request.getParameter("supBillVhNoFrom");
        String supBillVhNoTo = request.getParameter("supBillVhNoTo");

        if(!supBillVhNoFrom.equals(""))
        	vhNoFrom=supBillVhNoFrom;
        if(!supBillVhNoTo.equals(""))	
        	vhNoTo=supBillVhNoTo;
		ArrayList supplierBillList=(ArrayList)worksBillService.getSupplierBillDetailFilterBy(new Integer(fundId), fundSource, vhFromDate, vhToDate,relation,vhNoFrom,vhNoTo,dept,functionary);
		if(supplierBillList!=null && supplierBillList.size()>0)
		{
			for (Iterator it = supplierBillList.iterator(); it.hasNext(); ) 
			{
				Supplierbilldetail supBill=(Supplierbilldetail)it.next();
				BigDecimal netAmt=supBill.getPassedamount().subtract(supBill.getPaidamount()).subtract(supBill.getTdsamount()).subtract(supBill.getAdvadjamt()).subtract(supBill.getOtherrecoveries()); 	
				BigDecimal dedAmt= supBill.getTdsamount().add(supBill.getOtherrecoveries());
				if(j > 0)
				{
					accCode.append("$");
					accCode.append(supBill.getId().toString()+"~"+supBill.getVoucherheader().getVoucherNumber()+"~"+dateFormat.format(supBill.getVoucherheader().getVoucherDate())+"~"+supBill.getPassedamount()+"~"+dedAmt+"~"+supBill.getPaidamount()+"~"+netAmt);
				}
				else
				{
					accCode.append(supBill.getId().toString()+"~"+supBill.getVoucherheader().getVoucherNumber()+"~"+dateFormat.format(supBill.getVoucherheader().getVoucherDate())+"~"+supBill.getPassedamount()+"~"+dedAmt+"~"+supBill.getPaidamount()+"~"+netAmt);
				}
				j++;
			}
			accCode.append("^");
		}
}

else if(request.getParameter("type").equalsIgnoreCase("getBankAccountBalance"))
{
	String accountId =request.getParameter("accountId");
	/* SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	java.util.Date dt = sdf.parse(request.getParameter("vhDate"));
	String vhDate=formatter.format(dt);
	com.exilant.eGov.src.common.EGovernCommon ecm = new com.exilant.eGov.src.common.EGovernCommon();
	java.math.BigDecimal balAvail = ecm.getAccountBalance(vhDate, accountId);
	logger.info("balAvail  "+balAvail);
	Bankaccount ba=commonsServiceImpl.getBankaccountById(new Integer(accountId));
	if(ba.getFund().getChartofaccountsByPayglcodeid()!=null)
		accCode.append(balAvail.toString()+"$"+ba.getFund().getId()+"$"+ba.getFund().getName()+"$"+ba.getFund().getChartofaccountsByPayglcodeid().getGlcode()+"$"+ba.getFund().getChartofaccountsByPayglcodeid().getName());
	else
		accCode.append(balAvail.toString()+"$"+ba.getFund().getId()+"$"+ba.getFund().getName()+"$"+"0"+"$"+"0");
	accCode.append("^"); */
}
else if(request.getParameter("type").equalsIgnoreCase("contractorCode")){
 	String query="select distinct r.code||'`-`'||r.name||'`-`'||r.id AS \"code\" from relation r,contractorbilldetail c  where c.contractorid=r.ID "+
 				" group by r.id,r.code,r.name,c.PASSEDAMOUNT,c.PAIDAMOUNT HAVING c.PASSEDAMOUNT-c.PAIDAMOUNT>0 ORDER BY UPPER( \"code\" )";
 	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("supplierCode")){
 	String query="select distinct r.code||'`-`'||r.name||'`-`'||r.id AS \"code\" from relation r,supplierbilldetail s  where s.supplierid=r.ID "+
		" group by r.id,r.code,r.name,s.PASSEDAMOUNT,s.PAIDAMOUNT HAVING s.PASSEDAMOUNT-s.PAIDAMOUNT>0 ORDER BY UPPER( \"code\" )";
	logger.info(query);
	resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("validateGLcode")){
 	String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where glcode='"+request.getParameter("glcode")+"' and classification=4 and isactiveforposting = 1";
logger.info(query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("validateFunctionName")){
 	String query="SELECT name||'`-`'||id AS \"code\" from function where  isactive = 1 AND isnotleaf=0 and name='"+request.getParameter("functionName")+"' ";
logger.info(query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("validaUniquePanTinno"))
{
	String appQry="";
	if(request.getParameter("panno").trim().equals(""))
		appQry = " panno is null " ;
	else
		appQry = " panno='"+request.getParameter("panno")+"' ";
		
	if(request.getParameter("tinno").trim().equals(""))
		appQry = appQry+" and tinno is null " 	;
	else
		appQry = appQry+" and tinno='"+request.getParameter("tinno")+"' " 	;
 	String query="select code AS \"code\" from relation where "+appQry+" and code!='"+request.getParameter("code")+"' ";
logger.info(query);
resultList=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
if(resultList!=null)
{
	for(int i=0;i<resultList.size();i++)
	{
		if(i==0)
		{
			accCode.append(resultList.get(i));
		}
		else
		{
			accCode.append("+");
			accCode.append(resultList.get(i));
		}
	}
	accCode.append("^");
}
String codeValues=accCode.toString();

response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(codeValues);
}
catch(Exception e){
	logger.debug(e.getMessage());
}
%>
<html>
<body>
</body>
</html>

