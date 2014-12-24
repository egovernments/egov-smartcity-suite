/** * Created on May 02, 2007 * @author Lokesh */

<%@ page language="java"
	import="java.sql.*,java.util.*,org.egov.infstr.utils.HibernateUtil,org.egov.payroll.model.SalaryCodes,
                                 org.egov.model.recoveries.*,org.egov.payroll.services.payslip.PayRollServiceImpl,
                                 org.egov.payroll.utils.PayrollExternalInterface,org.egov.payroll.utils.PayrollExternalImpl,
                                 org.egov.payroll.utils.PayrollManagersUtill,java.text.SimpleDateFormat,
                                 java.math.BigDecimal, org.egov.payroll.utils.PayrollConstants,
                                 java.math.BigDecimal,org.egov.infstr.commons.*,
                                 java.util.GregorianCalendar,java.util.Calendar,                                 
                                 org.egov.infstr.utils.EGovConfig,org.egov.commons.CFinancialYear,
                                 org.egov.infstr.client.filter.EGOVThreadLocals,
                                 org.egov.pims.empLeave.model.TypeOfLeaveMaster,
				org.egov.infstr.utils.*,org.egov.payroll.services.payslip.PayRollService,org.hibernate.jdbc.*"%>
<%
	final HttpServletRequest req=request;
	final HttpServletResponse res=response;
	final HttpSession ses=session;
	HibernateUtil.getCurrentSession().doWork(new Work() {
		ResultSet rs=null;
		Statement stmt=null;
		String result = "";
		PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
		Connection con=null;
		public void execute(Connection con)
		{
			
			
			
			try
			{
			stmt=con.createStatement();
			
			//Based on the type we will execute the query

			if(req.getParameter("type").equalsIgnoreCase("checkUniqueness"))
			{
				String tablename=req.getParameter("tablename");
				String fieldname=req.getParameter("fieldname");
				String fieldvalue=req.getParameter("fieldvalue");
				String fieldname1=req.getParameter("fieldname1");
				String fieldvalue1=req.getParameter("fieldvalue1");
				String fieldname2=req.getParameter("fieldname2");
				String fieldvalue2=req.getParameter("fieldvalue2");
				Integer status=(payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE,
						org.egov.infstr.commons.dao.GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus",new java.util.Date()).getValue())).getId();
				String query="SELECT "+fieldname+" FROM "+tablename+" where upper("+fieldname+") = '"+fieldvalue.toUpperCase()+"' and upper("+fieldname1+") = '"+fieldvalue1.toUpperCase()+"' and upper("+fieldname2+") = '"+fieldvalue2.toUpperCase()+"' and status != "+status.intValue();
				System.out.println("query"+query);
				try 
				{
				rs=stmt.executeQuery(query);

					if(rs.next())
					{
						result = "true";
					}
					else
					{
						result = "false";
					}
				}

				catch(Exception e)
				{
				System.out.println(e.getMessage());
				throw e;
				}
				

			}
			else if (req.getParameter("type").equalsIgnoreCase("checkingPayDates"))
			// this block will execute for batchgenpayslips screen to check,already payslips are generated for the entered date or not
			// if it generated it will return true otherwise it will return false
			{

				String fromdate=(String)req.getParameter("fromdate");
				String deptid=(String)req.getParameter("deptid");
				String functionaryId=(String)req.getParameter("functionaryId");
				System.out.println("date from jsp"+fromdate);
				String month=(String)req.getParameter("month");
				String functionId = (String)req.getParameter("functionId");

				String query="select status from egpay_batchgendetails where todate>=to_date('"+fromdate+"','dd/mmm/yyyy') and status != "+ PayrollConstants.BATCH_GEN_STATUS_FAILED.intValue() +" and status != "+ PayrollConstants.BATCH_GEN_STATUS_JOBDELETED.intValue() ;
				if(deptid!=null && !deptid.equals("null"))
					query=query+" and (id_dept is null or id_dept="+deptid+")";
				if(functionaryId!=null && !functionaryId.equals("null"))
					query=query+" and (id_functionary is null or id_functionary="+functionaryId+")";
				if(functionId!=null && !functionId.equals("null"))
					query=query+" and (id_function is null or id_function="+functionId+")";	
				query=query+" order by id desc";
				System.out.println("Checking="+query);

				try
				{
				rs=stmt.executeQuery(query);

					if(rs.next())
					{
						result = String.valueOf(rs.getInt("status"));
					}
					else
					{
						result = "false";
					}
					System.out.println("result="+result);
				}

				catch(Exception e)
				{
				System.out.println(e.getMessage());
				e.printStackTrace();
				throw e;
				}
				
			}
			else if(req.getParameter("type").equalsIgnoreCase("reComputeDeductionAmts"))
			{
			
			/**this will recompute the deduction amount based on basic salary
			  * it will return the string of deduction taxes and deduction others separated by delimeter '@'
		  	  * and each amount is separated by delimeter '~'
		  	  **/	
		  	  try{
			  List taxlist=(List)ses.getAttribute("dedTaxlist");
			  List otherlist=(List)ses.getAttribute("dedOtherlist");
			  int basicsalary =Integer.parseInt((String)req.getParameter("basicSal"));
			  double grossamt =Double.parseDouble((String)req.getParameter("grossamt"));
			  String month=(String)req.getParameter("month");
			  String finyr=(String)req.getParameter("finyr");
			  double totaldays=Double.parseDouble((String)req.getParameter("totaldays"));
			  double paiddays=Double.parseDouble((String)req.getParameter("paiddays"));
			  java.util.Date date=PayrollManagersUtill.getPayRollService().getStartDateOfMonthByMonthAndFinYear(Integer.parseInt(month),Integer.parseInt(finyr));
			  
			  SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
			  StringBuffer dedTaxamountlist=new StringBuffer();
			  StringBuffer dedOtheramountlist=new StringBuffer(); 
			  if(taxlist == null){
				taxlist = PayrollManagersUtill.getPayheadService().getAllSalarycodesByCategoryId(new Integer(3));
			  }
			  if(otherlist == null){
				otherlist = PayrollManagersUtill.getPayheadService().getAllSalarycodesByCategoryId(new Integer(5));
			  }	
			  result="true";
			  
			  Iterator itr=taxlist.iterator();
			  int j=0,k=0;
			  double amount;
			  PayRollService payRollService = PayrollManagersUtill.getPayRollService();
			  // it will load the deductin taxes amount
			  while(itr.hasNext())
			  {
				SalaryCodes salaryCode=(SalaryCodes)itr.next();
					
				amount=0;
				if(salaryCode.getTdsId()!=null && salaryCode.getTdsId().getBank()==null)
				{	
					if(payRollService.isPayHeadGrossBased(salaryCode.getHead()))
						amount=payRollService.getSlabBasedAmount(salaryCode,new BigDecimal(grossamt),fmt.format(date));
					else
					    amount=payRollService.getSlabBasedAmount(salaryCode,new BigDecimal(basicsalary),fmt.format(date)); 
					if(salaryCode.getIsAttendanceBased()=='Y')
					{
					   amount=Math.round((amount*paiddays)/totaldays); 
					}
					if(j==0)
					{						
						dedTaxamountlist.append(amount);
						j=1;
					}
					else
					{											
						dedTaxamountlist.append("~"+amount);											
					}

				}else
				{
					amount=0;
					if(j==0)
					{						
						dedTaxamountlist.append(amount);
						j=1;
					}
					else
					{											
						dedTaxamountlist.append("~"+amount);											
					}
				}
				//dedTaxamountlist.add(amount);

					
				
				System.out.println("dedTaxamountlist=="+dedTaxamountlist.toString());
			}
			 
			 
			// it will load the deduction others amount
			itr=otherlist.iterator();
			while(itr.hasNext())
			{
				SalaryCodes salaryCode=(SalaryCodes)itr.next();
				if(salaryCode.getTdsId()!=null && salaryCode.getTdsId().getBank()==null)
				{	  
					if(payRollService.isPayHeadGrossBased(salaryCode.getHead()))
					   amount=payRollService.getSlabBasedAmount(salaryCode,new BigDecimal(grossamt),fmt.format(date));
					else
					   amount=payRollService.getSlabBasedAmount(salaryCode,new BigDecimal(basicsalary),fmt.format(date));	
					
					if(salaryCode.getIsAttendanceBased()=='Y')
					{
					   amount=Math.round((amount*paiddays)/totaldays); 
					}
					if(k==0)
					{						
						dedOtheramountlist.append(amount);
						k=1;
					}
					else
					{											
						dedOtheramountlist.append("~"+amount);											
					}

				}else
				{
					amount=0;
					if(k==0)
					{						
						dedOtheramountlist.append(amount);
						k=1;
					}
					else
					{											
						dedOtheramountlist.append("~"+amount);											
					}
				}		
				
			//  System.out.println("dedOtheramountlist=="+dedOtheramountlist.toString());
			  }	
			  dedTaxamountlist.append("@"+dedOtheramountlist.toString());
			  /*System.out.println("dedTaxamountlist=="+dedTaxamountlist.toString());
			  System.out.println("dedOtheramountlist=="+dedOtheramountlist.toString());
			  ses.setAttribute("dedTaxamountlist", dedTaxamountlist.toString().split("~"));
			  ses.setAttribute("dedOtheramountlist", dedOtheramountlist.toString().split("~"));	 */ 
			  result=dedTaxamountlist.toString();
			  }catch(Exception e)
			  {
			  e.printStackTrace();
			  result="false";
			  throw e;
			  }
			  
			}
			else if(req.getParameter("type").equalsIgnoreCase("checkPaidLeave")){
				String empId = req.getParameter("empId");
				System.out.println("empid-------"+empId);
				String month = req.getParameter("month");
				String finYearId = req.getParameter("year");
				System.out.println("empid-------"+empId);
				System.out.println("month-------"+month);
				System.out.println("year-------"+finYearId);
				CFinancialYear finYear = payrollExternalInterface.findFinancialYearById(new Long(finYearId));
				java.util.Date curDate = PayrollManagersUtill.getPayRollService().getStartDateOfMonthByMonthAndFinYear(Integer.parseInt(month),Integer.parseInt(finYearId));
				System.out.println(curDate);
				String leavetype = EGovConfig.getProperty("eis_egov_config.xml","LEAVE_TYPE_NAME_FOR_EL","","EIS.LEAVE");
				TypeOfLeaveMaster typeMst=(TypeOfLeaveMaster)payrollExternalInterface.getTypeOfLeaveMasterByName(leavetype);
				Float leaves = payrollExternalInterface.getAvailableLeavs(new Integer(empId),typeMst.getId(),curDate);
				System.out.println(leaves);
				int tempLeave = leaves.intValue();
				result = String.valueOf(tempLeave);
			}else if(req.getParameter("type").equalsIgnoreCase("checkPaySlipDateTOAPMDT"))
			{
				String empId = req.getParameter("empid");
				System.out.println("empid-------"+empId);
				String month = req.getParameter("month");
				String finYearId = req.getParameter("year");
				System.out.println("empid-------"+empId);
				System.out.println("month-------"+month);
				System.out.println("year-------"+finYearId);
				
				CFinancialYear finYear = payrollExternalInterface.findFinancialYearById(new Long(finYearId));
				java.util.Date curDate = PayrollManagersUtill.getPayRollService().getStartDateOfMonthByMonthAndFinYear(Integer.parseInt(month),Integer.parseInt(finYearId));
				GregorianCalendar cal=new GregorianCalendar();
				cal.setTime(curDate);
				cal.set(Calendar.DATE,cal.getActualMaximum(Calendar.DATE));
				SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
				String date= fmt.format(cal.getTime());
				System.out.println(curDate);
				
				String query="select * from eg_eis_employeeinfo where id="+empId+" and date_of_fa < '"+date+"' order by id desc";
				try
				{
					rs=stmt.executeQuery(query);

					if(rs.next())
					{
						result = "true";
					}
					else
					{
						result = "false";
					}
					System.out.println("result checking payslip dates to FAD="+result);
				}

				catch(Exception e)
				{
				System.out.println(e.getMessage());
				e.printStackTrace();		
				throw e;
				}
				
			
			}
			else if(req.getParameter("type").equalsIgnoreCase("checkEmpAssignment"))
			{
			try
			{
				String empId = req.getParameter("empid");
				System.out.println("assinmen check-----------empid-------"+empId);
				String month = req.getParameter("month");
				String finYearId = req.getParameter("year");
				System.out.println("empid-------"+empId);
				System.out.println("month-------"+month);
				System.out.println("year-------"+finYearId);	
				CFinancialYear finYear = payrollExternalInterface.findFinancialYearById(new Long(finYearId));
				java.util.Date curDate = PayrollManagersUtill.getPayRollService().getEndDateOfMonthByMonthAndFinYear(Integer.parseInt(month),Integer.parseInt(finYearId));
				SimpleDateFormat sdf =new SimpleDateFormat("dd-MM-yyyy");
		     	String date = sdf.format(curDate);
				String query="SELECT EV.CODE||'-'||EV.NAME||'-'||EV.ID||'-'||DG.DESIGNATION_NAME||'-'||D.DEPT_NAME||'-'||to_char(EV.DATE_OF_FA,'dd/mm/yyyy' ) "+
			    "AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EG_DESIGNATION DG "+
			    "WHERE DG.DESIGNATIONID = EV.DESIGNATIONID AND " +	    
			    "D.ID_DEPT = EV.DEPT_ID AND "+
			    "((EV.TO_DATE IS NULL and EV.FROM_DATE <= to_date('"+date+"','dd-MM-yyyy') )" +
			    " OR (EV.FROM_DATE <= to_date('"+date+"','dd-MM-yyyy') AND " +
			    "EV.TO_DATE >= to_date('"+date+"','dd-MM-yyyy'))) AND " +
			    "E.ID = EV.ID(+) and " +
			    "ev.id="+empId+" ORDER BY EV.CODE";		
					rs=stmt.executeQuery(query);

					if(rs.next())
					{
						result = "true";
					}
					else
					{
						result = "false";
					}
					System.out.println("result checking payslip dates to FAD="+result);
				}

				catch(Exception e)
				{
				System.out.println(e.getMessage());
				e.printStackTrace();		
				throw e;
				}
				
			}
			else if(req.getParameter("type").equalsIgnoreCase("checkEmpPayscale"))
			{
			try
			{
				String empId = req.getParameter("empid");
				System.out.println("payscale check-----------empid-------"+empId);
				String month = req.getParameter("month");
				String finYearId = req.getParameter("year");
				System.out.println("empid-------"+empId);
				System.out.println("month-------"+month);
				System.out.println("year-------"+finYearId);	
				CFinancialYear finYear = payrollExternalInterface.findFinancialYearById(new Long(finYearId));
				java.util.Date curDate = PayrollManagersUtill.getPayRollService().getEndDateOfMonthByMonthAndFinYear(Integer.parseInt(month),Integer.parseInt(finYearId));
				SimpleDateFormat sdf =new SimpleDateFormat("dd-MM-yyyy");
		     	String date = sdf.format(curDate);
						System.out.println("date-------"+date);	
				String query="SELECT s.ID  FROM egpay_payscale_employee s WHERE effectivefrom IN (SELECT MAX (stemp.effectivefrom) "+
				            "FROM egpay_payscale_employee stemp WHERE stemp.effectivefrom <= to_date('"+date+"','dd-MM-yyyy') AND "+
							"stemp.id_employee ="+empId+") AND s.id_employee ="+empId;		
					rs=stmt.executeQuery(query);

					if(rs.next())
					{
						result = "true";
					}
					else
					{
						result = "false";
					}
					System.out.println("result check payscale="+result);
				}

				catch(Exception e)
				{
				System.out.println(e.getMessage());
				e.printStackTrace();		
				throw e;
				}
				
			}
			else if(req.getParameter("type").equalsIgnoreCase("reComputeEarningAmt"))
			{
			
			/**this will recompute the earning amount based on basic salary
			  * it will return the string of earnings separated by delimeter '@'
		  	  * and each amount is separated by delimeter '~'
		  	  **/	
		  	  result="0";
		  	  
			   	  
			  
			  try{
				  int basicsalary =Integer.parseInt((String)req.getParameter("basicSal"));
				  String month=(String)req.getParameter("month");
				  String finyr=(String)req.getParameter("finyr");
				  double totaldays=Double.parseDouble((String)req.getParameter("totaldays"));
				  double paiddays=Double.parseDouble((String)req.getParameter("paiddays"));
				  String salcodeid=(String)req.getParameter("salcodeid");
				  java.util.Date date=PayrollManagersUtill.getPayRollService().getEndDateOfMonthByMonthAndFinYear(Integer.parseInt(month),Integer.parseInt(finyr));
				  double amount=0; 
				  SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");	

				  SalaryCodes salaryCode=(SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById(new Integer(salcodeid));
				  System.out.println("tdsid="+salaryCode.getTdsId().getId()+" basicsal= "+basicsalary+" date "+fmt.format(date));
				  if(salaryCode.getTdsId()!=null)
				  {	  					
					amount=PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,new BigDecimal(basicsalary),fmt.format(date));
					if(salaryCode.getIsAttendanceBased()=='Y')
					{
					   amount=Math.round((amount*paiddays)/totaldays);  			   			
					}
					System.out.println("inside="+amount);

				   }else
				   {
					amount=0;		
				   }
				   System.out.println("amount="+amount);
				   result=amount+"";
				}catch(Exception e)
				{
				e.printStackTrace();
				throw e;
				}
				
			}else if(req.getParameter("type").equalsIgnoreCase("validPaySlipExists"))
			{
					String tablename=req.getParameter("tablename");
					String fieldname=req.getParameter("fieldname");
					String fieldvalue=req.getParameter("fieldvalue");
					try
					{
					
					Integer status=(payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE,
							org.egov.infstr.commons.dao.GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus",new java.util.Date()).getValue())).getId();
					
					String query="SELECT "+fieldname+" FROM "+tablename+" where upper("+fieldname+") = '"+fieldvalue.toUpperCase()+ "' and status != "+status.intValue();
					System.out.println("query === "+query);
					
					rs=stmt.executeQuery(query);
			
						if(rs.next())
						{
							result = "true";
						}
						else
						{
							result = "false";
						}
					}
			
					catch(Exception e)
					{
					System.out.println(e.getMessage());
					e.printStackTrace();
					throw e;
					}
					

			}
			res.setContentType("text/xml");
			res.setHeader("Cache-Control", "no-cache");
			res.getWriter().write(result);
			
			}
			catch(Exception e)
			{	
			System.out.println(e.getMessage());
			}
			
		}
	});
	
	

	%>
<html>
<body>
</body>
</html>

