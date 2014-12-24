<%@ page language="java" import="java.sql.*,java.text.SimpleDateFormat,org.egov.infstr.utils.HibernateUtil,java.text.*,java.util.Date,
					org.egov.payroll.utils.PayrollConstants,
					org.egov.infstr.commons.dao.*,
					org.egov.pims.service.*,
					java.util.*,
					java.lang.*,
					org.egov.pims.model.*,
					org.egov.infstr.utils.*,org.hibernate.jdbc.*,com.exilant.exility.*" %>
<c:catch var ="catchException">
<%
	final HttpServletRequest req=request;
	final HttpServletResponse resp=response;
   final String filterByDept=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EIS-PAYROLL","FILTERBYDEPT",new Date()).
    		getValue().toUpperCase();
	String userName =(String)req.getSession().getAttribute("com.egov.user.LoginUserName");
	final EmployeeService empService=new EmployeeServiceImpl();
	final List deptBasedOnLoginList=empService.getListOfDeptBasedOnHodLogin(userName);
	int userId =(Integer)req.getSession().getAttribute("com.egov.user.LoginUserId");

	final PersonalInformation  ps= empService.getEmpForUserId(userId);
	
	
	
	
	
	 HibernateUtil.getCurrentSession().doWork(new Work() {
		 	ResultSet rs=null;
			Statement stmt=null;
			String empModule = PayrollConstants.EMPLOYEE_MODULE;
			
			StringBuffer accCode=new StringBuffer();
			Assignment assignment=null;
			String empCode=null;
			Integer assId=null;
			Integer empId=null;
			
				public void execute(Connection con)
				{
					try
					{
					stmt=con.createStatement();
					if(ps!=null)
					{
					 	empId =ps.getIdPersonalInformation();
					 	empCode = ps.getEmployeeCode();
					}

					if(req.getParameter("type").equalsIgnoreCase("getAllEmployeeCodes") || req.getParameter("type").equalsIgnoreCase("getAllPensionEligibleEmployee")
						|| req.getParameter("type").equalsIgnoreCase("getAllGratuityDisbursableEmployee") || req.getParameter("type").equalsIgnoreCase("getGratuityDisbursableEmployeeByCode") 
						|| req.getParameter("type").equalsIgnoreCase("getAllActiveEmployeeCodes") || req.getParameter("type").equalsIgnoreCase("getActiveEmployeeByCode") 
						|| req.getParameter("type").equalsIgnoreCase("getEmployeeByCode") || req.getParameter("type").equalsIgnoreCase("getEmpCodes") 
						|| req.getParameter("type").equalsIgnoreCase("getEmployedEmpCodes"))
					{
						Date date = new Date();
						assignment=  empService.getAssignmentByEmpAndDate(date,empId);
						if(assignment!=null)
						{
							assId = assignment.getId();
						}
					}	
					if(req.getParameter("type").equalsIgnoreCase("getAllSalCategoryE")){
						String query="select name as \"code\" from EGPAY_CATEGORY_MASTER where CAT_TYPE='E' order by name ";
						rs=stmt.executeQuery(query);
					}

					else if(req.getParameter("type").equalsIgnoreCase("getAllSalCategoryD")){
						String query="select name as \"code\" from EGPAY_CATEGORY_MASTER where CAT_TYPE='D' order by name ";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllEmployeeCodes"))
					{
						if("YES".equals(filterByDept))
						{
							if(deptBasedOnLoginList.size()>0)
							{
								String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||DG.DESIGNATION_NAME||'`-`'||D.DEPT_NAME||'`-`'||EV.DATE_OF_FA  || '`-`'|| ph.NAME "+
								"AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EGPAY_PAYSCALE_EMPLOYEE pe,EGPAY_PAYSCALE_HEADER ph,EG_DESIGNATION DG "+
								"WHERE D.ID_DEPT =   EV.DEPT_ID and ph.id = pe.ID_PAYHEADER and pe.ID in (select max(s.ID) from egpay_payscale_employee s where s.EFFECTIVEFROM<=to_date(SYSDATE,'dd-MM-yyy') and s.ID_EMPLOYEE=e.ID) "+
								"AND pe.ID_EMPLOYEE = e.ID and DG.DESIGNATIONID = EV.DESIGNATIONID AND d.id_dept IN(Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') AND"+
								"((EV.TO_DATE IS NULL and EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') ) OR (EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') AND EV.TO_DATE >= to_date(SYSDATE,'dd-MM-yyy'))) AND EV.isActive=1 AND EV.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') AND E.ID = EV.ID(+)  ORDER BY EV.CODE";
								rs=stmt.executeQuery(query);
							}
							else
							{
								String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||EV.DATE_OF_FA AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV  WHERE EV.CODE="+empCode;
								rs=stmt.executeQuery(query);
							}

						}
						else
						{
							String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||DG.DESIGNATION_NAME||'`-`'||D.DEPT_NAME||'`-`'||EV.DATE_OF_FA  || '`-`'|| ph.NAME "+
							"AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EGPAY_PAYSCALE_EMPLOYEE pe,EGPAY_PAYSCALE_HEADER ph,EG_DESIGNATION DG "+
							"WHERE D.ID_DEPT =   EV.DEPT_ID and ph.id = pe.ID_PAYHEADER and pe.ID in (select max(s.ID) from egpay_payscale_employee s where s.EFFECTIVEFROM<=to_date(SYSDATE,'dd-MM-yyy') and s.ID_EMPLOYEE=e.ID) "+
							"AND pe.ID_EMPLOYEE = e.ID and DG.DESIGNATIONID = EV.DESIGNATIONID AND "+
							"((EV.TO_DATE IS NULL and EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') ) OR (EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') AND EV.TO_DATE >= to_date(SYSDATE,'dd-MM-yyy'))) AND EV.isActive=1 AND EV.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') AND E.ID = EV.ID(+)  ORDER BY EV.CODE";
							rs=stmt.executeQuery(query);
						}

					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllActiveOrInactiveEmployeeCodes")){
						String query="SELECT EMP.CODE||'`-`'||EMP.NAME||'`-`'||EMP.ID  AS code FROM EG_EMPLOYEE EMP ORDER BY EMP.CODE";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllPensionEligibleEmployee")){	
						if("YES".equals(filterByDept))
						{
							String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID||'`-`'||d.dept_name AS \"code\" FROM EG_EMPLOYEE E ,eg_eis_employeeinfo ev,eg_department d where E.id = ev.id and ev.DEPT_ID=d.ID_DEPT and d.ID_DEPT IN(Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') and "+
								"E.status in(select s.id from egw_status s where s.MODULETYPE='"+empModule+"' and (s.DESCRIPTION='Retired' or s.DESCRIPTION='Deceased'))  ORDER BY E.CODE";
							rs=stmt.executeQuery(query);
						}
						else
						{
							String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID "+
							"AS \"code\" FROM EG_EMPLOYEE E where E.status "+
							"in(select s.id from egw_status s where s.MODULETYPE='"+empModule+"' and (s.DESCRIPTION='Retired' or s.DESCRIPTION='Deceased')) ORDER BY E.CODE";
							rs=stmt.executeQuery(query);
						}
					}
					else if(req.getParameter("type").equalsIgnoreCase("getPensionEligibleEmployeeByCode")){
						String code = req.getParameter("code");
						System.out.println("code-------------heree cmpute "+code);
						String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID "+
					"AS \"code\" FROM EG_EMPLOYEE E where E.code='"+code+"' and (E.status in(select s.id from egw_status s where s.MODULETYPE='"+empModule+"' and (s.DESCRIPTION='Retired' or s.DESCRIPTION='Deceased'))) ORDER BY E.CODE";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllGratuityDisbursableEmployee")){
					
						if("YES".equals(filterByDept))
						{
							String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID||'`-`'||d.dept_name AS \"code\" FROM EG_EMPLOYEE E,EGPAY_PENSION_DETAILS pd, EGPAY_PENSION_HEADER ph,eg_eis_employeeinfo ev,eg_department d "+
						    "WHERE e.id= ev.id and ev.DEPT_ID=d.ID_DEPT and e.ID = ph.ID_EMP and ph.ID = pd.ID_PENSION_HEADER and d.ID_DEPT IN(Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') and "+
						    "pd.STATUS in(select s.id from egw_status s where s.MODULETYPE='PensionDetails' and (s.DESCRIPTION='Approved' or s.DESCRIPTION='GratuityDisbursed')) ORDER BY E.CODE";
							rs=stmt.executeQuery(query);
				        }
						else
						{
							String code = req.getParameter("code");
							String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID "+
							"AS \"code\" FROM EG_EMPLOYEE E,EGPAY_PENSION_DETAILS pd, EGPAY_PENSION_HEADER ph where  e.ID = ph.ID_EMP and ph.ID = pd.ID_PENSION_HEADER and "+
							"pd.STATUS in(select s.id from egw_status s where s.MODULETYPE='PensionDetails' and (s.DESCRIPTION='Approved' or s.DESCRIPTION='GratuityDisbursed')) ORDER BY E.CODE";
							rs=stmt.executeQuery(query);
						}
					}
					else if(req.getParameter("type").equalsIgnoreCase("getGratuityDisbursableEmployeeByCode")){
						String code = req.getParameter("code");
						System.out.println("code-------------gratuity "+code);
						String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID||'`-`'||d.dept_name "+
						"AS \"code\" FROM EG_EMPLOYEE E,EGPAY_PENSION_DETAILS pd, EGPAY_PENSION_HEADER ph,eg_eis_employeeinfo ev,eg_department d where E.code='"+code+"' and "+
						"e.ID = ph.ID_EMP and ph.ID = pd.ID_PENSION_HEADER and e.ID=ev.ID and d.ID_DEPT= e.ID and d.ID_DEPT IN(Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') and  "+
						"pd.STATUS in(select s.id from egw_status s where s.MODULETYPE='PensionDetails' and s.DESCRIPTION='Approved') ORDER BY E.CODE";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllActiveEmployeeCodes")){
						String tmpQuery="";
						if("YES".equals(filterByDept))
						{
							if(deptBasedOnLoginList.size()>0)
							{
								tmpQuery="SELECT e.CODE||'`-`'||e.NAME||'`-`'||E.ID AS \"code\" from "+
								"eg_employee e,eg_eis_employeeinfo ev,eg_department d where e.ID=ev.ID and d.ID_DEPT= ev.DEPT_ID and "+
								"e.isactive=1 and d.id_dept IN (Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') and "+
								"e.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') order by e.code ";
							}
							else
							{
								tmpQuery="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV  WHERE EV.CODE="+empCode;
							}
						}
						else
						{
							tmpQuery="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID AS \"code\" FROM EG_EMPLOYEE E where E.isActive=1 and E.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed')  ORDER BY E.CODE "; 
						}

						rs=stmt.executeQuery(tmpQuery);
						
					}
					else if(req.getParameter("type").equalsIgnoreCase("getActiveEmployeeByCode"))
					{
						String code = req.getParameter("code");

						if("YES".equals(filterByDept))
						{
							if(deptBasedOnLoginList.size()>0)
							{
								String query="select e.code||'`-`'||e.name||'`-`'||e.id||'`-`'||d.dept_name as code from eg_employee e,eg_eis_employeeinfo ev,eg_department d where e.ID=ev.ID and d.ID_DEPT= ev.DEPT_ID and "+
								"e.isactive=1 and d.id_dept IN (Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') and "+
								"e.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') order by code ";
								rs=stmt.executeQuery(query);
							}
							else
							{
								String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||EV.DATE_OF_FA AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV  WHERE EV.CODE="+empCode;
								rs=stmt.executeQuery(query);
							}
						}
						else
						{
							String query="SELECT E.CODE||'`-`'||E.NAME||'`-`'||E.ID "+
							"AS \"code\" FROM EG_EMPLOYEE E where E.isActive=1 and E.code ="+code+" and E.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') "; 
							rs=stmt.executeQuery(query);
						}
					}
					else if(req.getParameter("type").equalsIgnoreCase("getEmployeeByCode"))
					{
					    String code = req.getParameter("code");
						if("YES".equals(filterByDept))
						{
							if(deptBasedOnLoginList.size()>0)
							{
								String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||DG.DESIGNATION_NAME||'`-`'||D.DEPT_NAME||'`-`'||EV.DATE_OF_FA  || '`-`'|| ph.NAME "+
								"AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EGPAY_PAYSCALE_EMPLOYEE pe,EGPAY_PAYSCALE_HEADER ph,EG_DESIGNATION DG "+
								"WHERE EV.code ='"+code+"' and D.ID_DEPT =   EV.DEPT_ID and ph.id = pe.ID_PAYHEADER and pe.EFFECTIVEFROM in (select max(s.effectivefrom) from egpay_payscale_employee s where s.EFFECTIVEFROM<=to_date(SYSDATE,'dd-MM-yyy') and s.ID_EMPLOYEE=e.ID) "+
								"AND pe.ID_EMPLOYEE = e.ID and DG.DESIGNATIONID = EV.DESIGNATIONID AND d.id_dept IN (Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') AND "+
								"((EV.TO_DATE IS NULL and EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') ) OR (EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') AND EV.TO_DATE >= to_date(SYSDATE,'dd-MM-yyy'))) AND EV.isActive=1 AND EV.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') AND E.ID = EV.ID(+) ORDER BY EV.CODE";
								rs=stmt.executeQuery(query);
							}
							else
							{
								String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||EV.DATE_OF_FA AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV  WHERE EV.CODE="+empCode;
								rs=stmt.executeQuery(query);
							}
						}
						else
						{
							String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||DG.DESIGNATION_NAME||'`-`'||D.DEPT_NAME||'`-`'||EV.DATE_OF_FA  || '`-`'|| ph.NAME "+
							"AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EGPAY_PAYSCALE_EMPLOYEE pe,EGPAY_PAYSCALE_HEADER ph,EG_DESIGNATION DG "+
							"WHERE EV.code ='"+code+"' and D.ID_DEPT =   EV.DEPT_ID and ph.id = pe.ID_PAYHEADER and pe.EFFECTIVEFROM in (select max(s.effectivefrom) from egpay_payscale_employee s where s.EFFECTIVEFROM<=to_date(SYSDATE,'dd-MM-yyy') and s.ID_EMPLOYEE=e.ID) "+
							"AND pe.ID_EMPLOYEE = e.ID and DG.DESIGNATIONID = EV.DESIGNATIONID AND "+
							"((EV.TO_DATE IS NULL and EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') ) OR (EV.FROM_DATE <= to_date(SYSDATE,'dd-MM-yyy') AND EV.TO_DATE >= to_date(SYSDATE,'dd-MM-yyy'))) AND EV.isActive=1 AND EV.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') AND E.ID = EV.ID(+) ORDER BY EV.CODE";
							rs=stmt.executeQuery(query);
					
						}
					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllGlcodesFromAccount")){
						String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where classification=4  and ISACTIVEFORPOSTING=1 order by glcode ";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getAllIncomeGlcodesFromAccount")){
						String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where classification=4  and ISACTIVEFORPOSTING=1 and type='I' order by glcode ";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getBankAccountGlcode"))
					{
						String accountId =req.getParameter("accountId");
						String query="select ca.glcode || '`-`' || ca.name || '`-`' || ca.id as \"code\" "
						+" from bankaccount ba,Chartofaccounts ca "
						+" WHERE ba.glcodeid=ca.id and ba.id='"+accountId+"' ";
						rs=stmt.executeQuery(query);
					}
					/*else if(req.getParameter("type").equalsIgnoreCase("getBankAccountBalance"))
					{
						String accountId =req.getParameter("accountId");
						SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						Date dt = sdf.parse(req.getParameter("vhDate"));
						String vhDate=formatter.format(dt);
						com.exilant.eGov.src.common.EGovernCommon cm = new com.exilant.eGov.src.common.EGovernCommon();
						java.math.BigDecimal balAvail = cm.getAccountBalance(vhDate, con,accountId);
						accCode.append(balAvail.toString());
					}*/
					else if(req.getParameter("type").equalsIgnoreCase("getPFAccountCodes")){
						String query="select coa.glcode||'`-`'||coa.name||'`-`'||coa.id as \"code\" from chartofaccounts coa,chartofaccountdetail cod where coa.classification=4 and coa.isactiveforposting = 1 and coa.type = 'L' and coa.id=cod.glcodeid order by coa.glcode ";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getPFExpAccountCodes")){
						String query="select glcode||'`-`'||name||'`-`'||id as \"code\" from chartofaccounts where classification=4 and isactiveforposting = 1 and type = 'E' order by glcode ";
						rs=stmt.executeQuery(query);
					}
					else if(req.getParameter("type").equalsIgnoreCase("getEmpCodes")){
					  	String query="";
					  	if("YES".equals(filterByDept))
						{
					 		query="select e.code||'`-`'||e.name||'`-`'||e.id||'`-`'||d.dept_name as \"code\" from eg_employee e,eg_eis_employeeinfo ev,eg_department d  "+ 
							"where e.ID=ev.ID and d.ID_DEPT= ev.DEPT_ID and e.isactive=1 and d.id_dept IN (Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') order by ev.code";
					 		rs=stmt.executeQuery(query);
						}
						else
						{
						   query="select code||'`-`'||name||'`-`'||id as \"code\" from eg_employee where isactive=1 order by code ";
						   rs=stmt.executeQuery(query);
						}
					}
					else if(req.getParameter("type").equalsIgnoreCase("getEmployedEmpCodes"))
					{
						if("YES".equals(filterByDept))
						{
							if(deptBasedOnLoginList.size()>0)
							{
								String query="select e.code||'`-`'||e.name||'`-`'||e.id||'`-`'||d.dept_name as \"code\" from eg_employee e,eg_eis_employeeinfo ev,eg_department d  "+ 
								"where e.ID=ev.ID and d.ID_DEPT= ev.DEPT_ID and e.isactive=1 and d.id_dept IN (Select HOD from EG_EMPLOYEE_DEPT ehod  where ehod.ASSIGNMENT_ID= '"+assId+"') and "+
								"e.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') order by ev.code";
								rs=stmt.executeQuery(query);
					        }
							else
							{
								String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||EV.DATE_OF_FA AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV  WHERE EV.CODE="+empCode;
								rs=stmt.executeQuery(query);
							}
						}
						else
						{
							String query="select code||'`-`'||name||'`-`'||id as \"code\" from eg_employee where isactive=1 and status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') order by code ";
					        rs=stmt.executeQuery(query);
						}
								
					}
					else if(req.getParameter("type").equalsIgnoreCase("getFinancialYearStartDate"))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
						String query="select startingdate as \"code\" from financialyear where startingdate<='"+sdf.format(new Date())+"' and endingdate>='"+sdf.format(new Date())+"' and isactive=1";
						rs=stmt.executeQuery(query);
					}

					int i = 0;
					
						if(rs!=null)
						{
							while(rs.next()){
								if(i > 0){
									accCode.append("+");
									accCode.append(rs.getString("code"));
								}
								else {
									accCode.append(rs.getString("code"));
								}
								i++;
							}
							accCode.append("^");
						}
						String codeValues=accCode.toString();
						resp.setContentType("text/xml");
						resp.setHeader("Cache-Control", "no-cache");
						resp.getWriter().write(codeValues);
					}
					
					
					catch(Exception e){
						e.printStackTrace(System.out);
						System.out.println(e.getMessage());
					}
				}
				});
		
	
%>
</c:catch>
<c:if test = "${catchException!=null}">
${catchException}
</c:if>