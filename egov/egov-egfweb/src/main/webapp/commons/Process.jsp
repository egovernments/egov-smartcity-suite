<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@ page language="java" import="java.sql.*,java.util.HashMap,java.util.*,java.text.*,
org.egov.infstr.utils.HibernateUtil,org.egov.infra.admin.master.entity.Department,
org.egov.commons.CVoucherHeader,java.text.*,java.math.BigDecimal,
org.egov.commons.Fundsource,org.egov.commons.Relation,org.egov.commons.dao.FundSourceHibernateDAO,
org.egov.infstr.utils.EGovConfig,org.egov.infra.admin.master.service.DepartmentService,
org.egov.commons.Bankaccount,org.springframework.beans.factory.annotation.Autowired" %>

<%
System.out.println("process");

List<Object[]> rs=null;
StringBuffer accCode=new StringBuffer();



//Based on the type we will execute the query

if(request.getParameter("type").equalsIgnoreCase("coaSubMinorCode")){

String accountCode=request.getParameter("id");
String classValue=request.getParameter("classification");
String query="select glcode as \"code\" from chartofaccounts where classification='"+classValue+"' and glcode like '"+accountCode+"'|| '%' order by glcode ";
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();

}
else if(request.getParameter("type").equalsIgnoreCase("getAllCoaCodes")){
String query="select glcode||'`-`'||name||'`~`'||ID as \"code\" from chartofaccounts where classification=4 and isactiveforposting = true order by glcode ";
//System.out.println("query :"+query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
//System.out.println("rs.size :"+rs.size());
}
else if(request.getParameter("type").equalsIgnoreCase("getAllCoaCodesExceptCashBank")){
String query="SELECT glcode ||'`-`'||NAME ||'`~`'||ID AS \"code\" FROM chartofaccounts   WHERE classification = 4 AND isactiveforposting = true AND parentid  not in(select id  from chartofaccounts where purposeid in ( SELECT ID FROM egf_accountcode_purpose WHERE UPPER (NAME) = UPPER ('Cash In Hand') OR UPPER (NAME) = UPPER ('Bank Codes') OR UPPER (NAME) = UPPER ('Cheque In Hand')) ) "+
			" and id  not in(select id  from chartofaccounts where purposeid in ( SELECT ID FROM egf_accountcode_purpose WHERE UPPER (NAME) = UPPER ('Cash In Hand') OR UPPER (NAME) = UPPER ('Bank Codes') OR UPPER (NAME) = UPPER ('Cheque In Hand')) ) and glcode not like '471%' ORDER BY glcode ";
//System.out.println("query :"+query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllAssetCodes")){
String query="select glcode||'`-`'||name|| '`-`' || ID as \"code\" from chartofaccounts where classification=4 and isactiveforposting = true and type = 'A' order by glcode ";
//String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'A' order by glcode ";
//System.out.println("query :"+query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllLiabCodes")){
String query="select glcode||'`-`'||name|| '`-`' || ID as \"code\" from chartofaccounts where classification=4 and isactiveforposting = true and type = 'L' order by glcode ";
//String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'L' order by glcode ";
//System.out.println("query :"+query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("coaDetailCode")){

String accountCode=request.getParameter("glCode");
String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = true and glcode like '"+accountCode+"'|| '%' order by glcode ";
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllBankName")){
String query="select name||'`-`'||id as \"code\" from bank where  isactive = 1 order by name ";
rs = HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}	
else if(request.getParameter("type").equalsIgnoreCase("coaDetailCodeType")){

String accountCode=request.getParameter("glCode");
String typeClass=request.getParameter("typeClass");
String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and glcode like '"+accountCode+"'|| '%' and type = '"+typeClass+"' order by glcode ";
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("functionName")){

String functionCode=request.getParameter("name");
String query="select name as \"code\" from function where  isactive = 1 AND isnotleaf=0 and upper(name) like upper('"+functionCode+"%')  order by name ";
//System.out.println(query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllFunctionName")){
String query="select code||'`-`'||name||'`~`'||id as \"code\" from function where  isactive = 1 AND isnotleaf=0 order by name ";
//System.out.println(query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}

else if(request.getParameter("type").equalsIgnoreCase("contractorName")){
 	String query="select name||'`-`'||code as \"code\" from relation where id in(select relationid from worksdetail where totalvalue>0 and isactive=1) and isactive=1 and relationTypeid=2  order by upper(\"code\") ";
 	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("supplierName")){
	String query="select name||'`-`'||code as \"code\" from relation where id in(select relationid from worksdetail where totalvalue>0 and isactive=1) and isactive=1 and relationTypeid=1  order by upper(\"code\") ";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("workDetailName")){
	String name=request.getParameter("name");
	String relTypeId=request.getParameter("relationTypeId");
	String relationId=request.getParameter("relationId");
	name=name+'%';
	String query="select ' '||wd.name||'`-`'||wd.code as \"code\" from worksdetail wd,relation r where wd.relationid=r.id and r.relationtypeid="+relTypeId+" and r.isactive=1 and wd.relationid="+relationId+"  order by upper(wd.name) ";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getGLreportCodes")){ 
	////System.out.println("inside common function "+type);
	String query="SELECT concat(concat(concat(concat(glCode,'`-`'), name),'-$-'), ID) as \"code\" FROM chartofaccounts WHERE glcode not in (select glcode from chartofaccounts where glcode like '47%' AND glcode not like '471%' AND glcode !='4741') "+
		" AND glcode not in (select glcode from chartofaccounts where glcode='471%') AND isactiveforposting=1 AND classification=4 ORDER BY glcode ";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}

else if(request.getParameter("type").equalsIgnoreCase("getActiveContractorListwithCode")){ 
	//System.out.println("inside common function "+request.getParameter("type"));
	String query="SELECT   NAME || '`--`' || code  || '`-`' ||ID  AS \"code\"  FROM relation  WHERE 1 = 1 AND relationtypeid = 2 AND isactive = 1 ORDER BY NAME";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getActiveSupplierListwithCode")){ 
	//System.out.println("inside common function "+request.getParameter("type"));
	String query="SELECT   NAME || '`--`' || code  || '`-`' ||ID  AS \"code\"  FROM relation  WHERE 1 = 1 AND relationtypeid = 1 AND isactive = 1 ORDER BY NAME";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
// For Procurement Order-get all Party Name and Id based on the relationtypeid 
else if(request.getParameter("type").equalsIgnoreCase("getAllActivePartyName")){ 

	////System.out.println("inside common function "+request.getParameter("type"));
	String relTypeId=request.getParameter("relationTypeId");
	
	String query="SELECT   NAME || '`-`' ||ID  AS \"code\"  FROM relation  WHERE relationtypeid="+relTypeId+" AND isactive = 1 ORDER BY upper(NAME)";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
// For Dishonored cheque fill Bank Charges glcode,Receipt Reversal and Payment Reversal Chque glcode
else if(request.getParameter("type").equalsIgnoreCase("getBankGlcode")){ 
	//System.out.println("inside common function "+request.getParameter("type"));
	String query="select ca.glcode || '`--`' || ca.name || '`--`' || ca.id as \"code\" "
	+" from Chartofaccounts ca where purposeid=30 ORDER BY ca.glcode";
	
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getChqueGlcode")){ 
	//System.out.println("inside common function "+request.getParameter("type"));
	
	String vouchHeaderId =request.getParameter("vouchHeaderId");
	
	String query="select gl.glcode || '`-`' || ca.name || '`-`' || gl.glcodeid || '`-`' || gl.creditamount as \"code\" "
	+" from generalledger gl,Chartofaccounts ca "
	+" WHERE gl.glcodeid=ca.id and gl.creditamount>0 and gl.VOUCHERHEADERID='"+vouchHeaderId+"' "
	+" ORDER BY gl.glcode";
		
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getGlcodeForPayReversal")){ 
	//System.out.println("inside common function "+request.getParameter("type"));
	
	String glcodeParam =request.getParameter("glcodeParam");
	
	String query="select ca.glcode || '`--`' || ca.name || '`--`' || ca.id as \"code\" "
	+" from Chartofaccounts ca where ca.isactiveforposting = 1 and ca.classification=4 and ca.glcode='"+glcodeParam+"' ";
	
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getBillNo")){
	////System.out.println("inside common function "+type);
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
		  { //System.out.println(query);
		    rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
	      }
	      else
	       { //System.out.println(query+" union "+queryedit);
		  	 rs=HibernateUtil.getCurrentSession().createSQLQuery(query+" union "+queryedit).list();
       }


}else 
if(request.getParameter("type").equalsIgnoreCase("payeeDetailCode")){
String query = null;

String detailCode=request.getParameter("detailCode");
String colName=request.getParameter("colName");
String tableName=request.getParameter("tableName");
//System.out.println("tableName" + tableName);
if(tableName.equalsIgnoreCase("eg_employee"))
{
//System.out.println("tableName inside process 2" + tableName);
 query="select name||'`-`'||id  as \"code\" from "+tableName+" where isactive=1 and code like '"+detailCode+"'  order by name  " ;
}else if(tableName.equalsIgnoreCase("eg_item"))
{
	
	query="select itemno||'`-`'||id  as \"code\" from "+tableName+" where itemno like '"+detailCode+"'  order by itemno  " ;
}
else
 query="select name||'`-`'||id  as \"code\" from "+tableName+" where isactive=1 and code like '"+detailCode+"'  order by name  " ;
//System.out.println(query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getBankAccountGlcode"))
{ 
	//System.out.println("inside common function "+request.getParameter("type"));
	
	String accountId =request.getParameter("accountId");
	
	String query="select ca.glcode || '`-`' || ca.name || '`-`' || ca.id as \"code\" "
	+" from bankaccount ba,Chartofaccounts ca "
	+" WHERE ba.glcodeid=ca.id and ba.id='"+accountId+"' ";	
		
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
 else if(request.getParameter("type").equalsIgnoreCase("getEntityDetail")){
	 
	 String glCode =request.getParameter("glCode");
	 String accEntityKey =request.getParameter("accEntityKey");
	////System.out.println("inside common function "+type);
	String query="SELECT distinct concat ( concat ( concat ( concat ( case when  adk.DETAILTYPEID  when (select id from accountdetailtype where UPPER(name)= UPPER('Creditor') ) then (select code from relation where id = adk.detailkey) when (select id from accountdetailtype where UPPER(name) = UPPER('Employee') ) then (select code from eg_employee where id = adk.detailkey ) else (select code from accountentitymaster where ID =adk.detailkey   AND detailtypeid = adk.detailtypeid ) end, '`-`' )," 
	+" case when adk.DETAILTYPEID when (select id from accountdetailtype where UPPER(name) = UPPER('Creditor') ) then (select name from relation where id = adk.detailkey ) when (select id from accountdetailtype where UPPER(name) = UPPER('Employee') ) then (select emp_firstname from eg_employee where id= adk.detailkey ) else (select name from accountentitymaster where  detailtypeid = adk . detailtypeid AND ID = adk.detailkey ) end), '-$-' ),adk.detailkey ) as \"code\" "
	+" from generalledgerdetail gld, accountdetailkey adk where gld.generalledgerid in (select id from generalledger where glcode = '"+glCode+"' ) and gld.detailkeyid = adk.detailkey and gld.detailtypeid = adk.detailtypeid and adk.DETAILTYPEID ="+accEntityKey+""
	+" UNION "
	+" SELECT distinct concat ( concat ( concat ( concat ( case when  adk.DETAILTYPEID when (select id from accountdetailtype where UPPER(name)=UPPER('Creditor')) then (select code from relation where id = adk.detailkey) when (select id from accountdetailtype where UPPER(name) = UPPER('Employee') ) then (select code from eg_employee where id = adk.detailkey ) else (select code from accountentitymaster where detailtypeid = adk.detailtypeid AND ID = adk.detailkey) end , '`-`' ),"
	+" case when  adk.DETAILTYPEID when (select id from accountdetailtype where UPPER(name) = UPPER('Creditor') ) then (select name from relation where id = adk.detailkey ) when (select id from accountdetailtype where UPPER(name) = UPPER('Employee') ) then (select emp_firstname from eg_employee where id= adk . detailkey ) else (select name from accountentitymaster where detailtypeid = adk . detailtypeid  AND ID = adk.detailkey) end), '-$-' ),adk.detailkey ) as \"code\" "
	+" from transactionsummary ts, accountdetailkey adk where ts.GLCODEID=(select id from chartofaccounts where glcode='"+glCode+"') and ts.ACCOUNTDETAILTYPEID=adk.detailtypeid and adk.detailtypeid="+accEntityKey+" and ts.accountdetailkey=adk.detailkey";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();   
}
else if(request.getParameter("type").equalsIgnoreCase("getSubLedgerScheduleCodes"))
{
		 ////System.out.println("inside common function "+type);
		String query="SELECT UNIQUE CONCAT(CONCAT(CONCAT(CONCAT(coa.glCode,'`-`'), coa.name),'-$-'), coa.ID) AS \"code\" FROM" 
		+" chartOfAccounts coa,chartOfAccountDetail cod  WHERE coa.id = cod.glCodeId AND"
		+" coa.classification=4";
	   //System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list(); 
}

else if(request.getParameter("type").equalsIgnoreCase("getAllTdsCodes"))
	{   String accountCode=request.getParameter("id");
String query=" select t.id||'`-`'||t.type||'`-`'||ca.glcode as\"code\" from tds t,egw_works_deductions ed,chartofaccounts ca,worksdetail w  where t.id=ed.tdsid and ed.worksdetailid="+accountCode+" AND w.id="+accountCode+"  and t.glcodeid=ca.id" ;

//System.out.println("query :"+query);
try{
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}catch(Exception e)
		{
	System.out.println(e.getMessage());
		}

}


else if(request.getParameter("type").equalsIgnoreCase("getTdsAmount"))
	{  
String tdsid=request.getParameter("id");
String amount=request.getParameter("amt");


String query="SELECT DISTINCT   case when dt.incometax=NULL then 0.0 else dt.incometax end+case when dt.surcharge = NULL then 0.0 else dt.surcharge + case when dt.education= NULL then 0.0 else dt.education end ||'`-`'||case when dt.amount =NULL then '0' else dt.amount end ||'`-`'||b.glcode ||'`-`'||b.name AS \"code\" FROM tds t, eg_deduction_details dt, chartofaccounts b WHERE t.ID = dt.tdsid AND t.glcodeid = b.ID AND SYSDATE BETWEEN dt.datefrom AND case when dt.dateto = NULL then SYSDATE else dt.dateto end AND "+amount+" BETWEEN dt.lowlimit                          AND case when dt.highlimit=NULL then 99999999999999999999999 else dt.highlimit end AND t.id="+tdsid;
//System.out.println("query :"+query);
try{
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}catch(Exception e)
		{
	System.out.println(e.getMessage());
		}

}
else if(request.getParameter("type").equalsIgnoreCase("getConSupName"))
{
String worksDetId=request.getParameter("worksdetailid");
String query="SELECT r.id||'`-`'||r.name  as \"code\" FROM relation r ,worksdetail w WHERE r.id=w.relationid AND w.id="+worksDetId;
//System.out.println("===================================The Contractor Supplier name Query is"+query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("checkSubLedgerType"))
{
	String subledgercode=request.getParameter("subledgercode");
	String query="SELECT DISTINCT detailtypeid as \"code\" FROM CHARTOFACCOUNTDETAIL WHERE glcodeid=(SELECT id FROM CHARTOFACCOUNTS WHERE glcode='"+subledgercode+"') ";
	//System.out.println("subledger Query is====="+query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("contractorCode")){
 	String query="select distinct r.code||'`-`'||r.name||'`-`'||r.id AS \"code\" from relation r,contractorbilldetail c  where c.contractorid=r.ID "+
 				" group by r.id,r.code,r.name,c.PASSEDAMOUNT,c.PAIDAMOUNT HAVING c.PASSEDAMOUNT-c.PAIDAMOUNT>0 ORDER BY UPPER( \"code\" )";
 	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("supplierCode")){
 	String query="select distinct r.code||'`-`'||r.name||'`-`'||r.id AS \"code\" from relation r,supplierbilldetail s  where s.supplierid=r.ID "+
		" group by r.id,r.code,r.name,s.PASSEDAMOUNT,s.PAIDAMOUNT HAVING s.PASSEDAMOUNT-s.PAIDAMOUNT>0 ORDER BY UPPER( \"code\" )";
	//System.out.println(query);
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("validateGLcode")){
 	String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where glcode='"+request.getParameter("glcode")+"' and classification=4 and isactiveforposting = 1";
//System.out.println(query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("validateFunctionName")){
 	String query="SELECT name||'`-`'||id AS \"code\" from function where  isactive = 1 AND isnotleaf=0 and name='"+request.getParameter("functionName")+"' ";
//System.out.println(query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
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
//System.out.println(query);
rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllCoaNames"))
{//TESTED
	final String query="select name||'`-`'||glcode||'`-`'||ID as \"code\" from chartofaccounts where classification=4 and isactiveforposting = '1' order by glcode ";
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
else if(request.getParameter("type").equalsIgnoreCase("getAllFunctionCode")){
	final String query="select code||'`~`'||name||'`~~`'||id as \"code\" from function where  isactive = 1 AND isnotleaf=0 order by name ";
	rs=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
}
int i = 0;
try{
//System.out.println("resultset"+rs);
if(rs!=null)
{
//System.out.println("resultset size:"+rs.size());
//System.out.println("resultset first value:"+rs.get(0));
int size = rs.size();
while(size!=0){
if(i > 0)
{
accCode.append("+");
accCode.append(rs.get(i));
}
else
{
accCode.append(rs.get(i));

}
i++;
size--;
}
accCode.append("^");
}
String codeValues=accCode.toString();

response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(codeValues);
}
catch(Exception e){
//System.out.println(e.getMessage());
}
%>
