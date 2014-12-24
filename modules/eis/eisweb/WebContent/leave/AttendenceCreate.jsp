

		<%@ page import="java.util.*,
		org.hibernate.LockMode,
		org.egov.commons.CFinancialYear,
		org.egov.pims.empLeave.model.*,
		org.egov.pims.empLeave.service.*,
		org.egov.pims.model.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.egov.infstr.utils.*,
		java.text.SimpleDateFormat,
		org.egov.pims.service.EmployeeServiceImpl"
		%>

		<html>
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:message key="PersonalInfoSys"/></title>
		<link href="../common/css/eispayroll.css" rel="stylesheet" type="text/css" />
		<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />


		<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>


		<script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

		</head>


		<%

	try{

		Map mp = new HashMap();
		session.setAttribute("AttendenceMapModify",mp);

		Map mpCompOff = new HashMap();
		session.setAttribute("AttendenceMapCompOff",mpCompOff);
		Map mpCompOffModify = new HashMap();
		session.setAttribute("AttendenceMapCompOffModify",mpCompOffModify);


		Set delset = new HashSet();
		session.setAttribute("AttendencesetDelete",delset);
		Map mpCre = new HashMap();
		session.setAttribute("AttendenceMapCreate",mpCre);
		EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
		EmployeeServiceImpl eisServiceImpl=new EmployeeServiceImpl();
		int yer = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		Calendar calendarHol = Calendar.getInstance();
		int dayOfMonth = 0;
		int month = 0;

		String  finYear ="";
		CFinancialYear financial =null;

		Boolean compOff = new Boolean(false);
		session.setAttribute("compOff",compOff);
		String  cufinYear = request.getParameter("finYear");
		CFinancialYear cufinancial =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(cufinYear));
		Map mpFYMap = EisManagersUtill.getFYMap();
		Map mpFY =new HashMap();
		Map mpcuFY = (Map)mpFYMap.get(new Long(cufinYear));
		int cuyer = new Integer((String)mpcuFY.get(new Integer(request.getParameter("monthId")))).intValue();
		if(!request.getParameter("monthId").equals("0") && !request.getParameter("finYear").equals("0") )
		{
		String  monthId = request.getParameter("monthId");
		month=new Integer(monthId).intValue();
		if(Calendar.getInstance().get(Calendar.MONTH) + 1 == month && Calendar.getInstance().get(Calendar.YEAR)==cuyer)
		{
		month=Calendar.getInstance().get(Calendar.MONTH) + 1;
		finYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
		financial =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(finYear));
		dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		mpFY = (Map)mpFYMap.get(new Long(finYear));
		yer = new Integer((String)mpFY.get(new Integer(month))).intValue();
		}
		else
		{
		finYear = request.getParameter("finYear");
		financial =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(finYear));
		//dayOfMonth = ((Integer)EisManagersUtill.getMonthsVsDays(month-1)).intValue();
		//Getting no. of days in  a month for a given year from Calender API
		Calendar cal = new GregorianCalendar(cuyer, month-1, 1);
		dayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		mpFY = (Map)mpFYMap.get(new Long(finYear));
		yer = new Integer((String)mpFY.get(new Integer(month))).intValue();
		}

		}

		Set setOfHol = EisManagersUtill.getHolidaySetForMonth(yer,month);

		Map employeeMap = (Map)request.getAttribute("employeeMap");
		Set employeeSet  = employeeMap.keySet();



		%>
		<script>
		function ButtonPressNew(arg, obj)
		{
		if(arg == "savenew")
		{
		document.forms[0].action = "${pageContext.request.contextPath}/leave/AfterAttendenceMasterAction.do?submitType=saveOrUpdateAttendence";
		obj.disabled=true;
		document.forms[0].submit();
		}

		}

		function goindex(arg)
		{

		if(arg == "Index")
		{

		document.forms[0].action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms[0].submit();
		}


		}
		function markPresent(value,obj)
		{


		var pHeaderTable= document.getElementById("TPTable");
		<%
		if(employeeSet!=null&&!employeeSet.isEmpty())
		{
		int length = employeeSet.size();
		int dayinMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int year= Calendar.getInstance().get(Calendar.YEAR);
		int m =Calendar.getInstance().get(Calendar.MONTH) + 1;


		%>
		var crrM= <%=m%>;
		var givenM= <%=month%>;
		var dayinM= <%=dayinMonth%>;
		var finYer= <%=yer%>;
		var currYer= <%=year%>;
		var length = <%=length%>;
		var rowCount = 2;
		for(i=0;i<length;i++)
		{
		var str = "attendence" + value;
		var row = pHeaderTable.rows[rowCount];
		var att = getControlInBranch(row,str);
		if(att.value == "")
		{
			if(obj.checked)
			{
				att.value = "P";
				if(value != dayinM)
				{

				populateMap(att);
				}
				else if(value == dayinM && crrM != givenM)
				{

				populateMap(att);
				}
				else if(value == dayinM && crrM == givenM && finYer!=currYer )
				{

				populateMap(att);
				}
			}
		}
		else if(!obj.checked && att.value=="P")
		{
			att.value = "";
			populateMap(att);

		}

		rowCount++;
		}

		<%
		}
		%>

		}
		
	function populateMap(obj)
	{
		var row=getRow(obj);

		var attString = obj.id;
		var s = attString.substring(10);
		//alert(obj.id);
		var finalStr = "attId"+s;
		//alert(finalStr);
		var actionValueObj = getControlInBranch(row,finalStr);
		//alert(actionValueObj.value);
		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/leave/getPopulateMapAjax.jsp?type="+obj.value+"&att="+actionValueObj.value;
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
		if (http.readyState == 4)
		{
		if (http.status == 200)
		{
		var statusString =http.responseText.split("^");


		}
		}
		};
		http.send(null);

		}


		function populateCompOffMap(obj)
		{

		var row=getRow(obj);

		var attString = obj.id;
		var s = attString.substring(10);
		var finalStr = "attId"+s;
		var actionValueObj = getControlInBranch(row,finalStr);
		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/leave/getPopulateMapComOffAjax.jsp?type="+obj.value+"&att="+actionValueObj.value;
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
		if (http.readyState == 4)
		{
		if (http.status == 200)
		{
		var statusString =http.responseText.split("^");


		}
		}
		};
		http.send(null);

		}

		function checkForCompOffApprove(value,obj)
		{
			if(obj.value!="CE")
			{
				var row=getRow(obj);
				var attString = obj.id;
				var s = attString.substring(10);
				var finalStr = "attId"+s;
				var actionValueObj = getControlInBranch(row,finalStr);
				
				var http = initiateRequest();
				var url = "<%=request.getContextPath()%>/leave/checkComOffAjax.jsp?attType="+actionValueObj.value;
				http.open("GET", url, true);
				http.onreadystatechange = function()
				{
				if (http.readyState == 4)
				{
				if (http.status == 200)
				{
				var statusString =http.responseText.split("^");
				if(statusString[0]=="true")
				{
					var popup = statusString[0];
					if(popup!="")
						{
								alert('CompOff exist. cannot be changed from CE to A,P,H or HP');
								obj.focus();
								obj.value ="CE";
								return false;
						}
				}
				
				}
				}
				};
				http.send(null);
			}
			return true;
			
		}

		function gOtOurl()
		{
		
		var http = initiateRequest();
		var url = "${pageContext.request.contextPath}/leave/setSession.jsp";
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
		if (http.readyState == 4)
		{
		if (http.status == 200)
		{
		var statusString =http.responseText.split("^");


		}
		}
		};
		http.send(null);

		}
		
		//fucntion to avoid backspace and delete 
		function IEKeyCap(value,obj){
      
	  if(value!="")
			{
		if (window.event.keyCode == 8 || event.keyCode == 46){      
            alert("Please do not clear the text box value");
			obj.focus();
			obj.value=value;
			return false;
		}
			
      }
}


	function checkAttendence(value,day,obj)
	{
		if(value != "" && obj.value == "")
		{
			alert('<bean:message key="alertEnterPAHP"/>');
			obj.focus();
		}
		<%
		int dayinMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int m =Calendar.getInstance().get(Calendar.MONTH) + 1;
		%>
		var crrM= <%=m%>;
		var givenM= <%=month%>;
		var dayinM= <%=dayinMonth%>;
		var day = day.substring(5);
		var finYer= <%=yer%>;
		var currYer= <%=year%>;	
		//alert(value);	
		if(value == "")
		{
			if(obj.value!="")
			{
				if(obj.value.length>=1)
				{
					if(obj.value=="P"||obj.value=="A" ||obj.value=="HP" || obj.value=="H")
					{
						if(day != dayinM)
						{				
							populateMap(obj);
						}
						else if(day == dayinM && crrM != givenM)
						{				
							populateMap(obj);
						}
						else if(day == dayinM && crrM == givenM && finYer!=currYer )
						{				
							populateMap(obj);
						}
						
						return true;
					}
					<% if ("Y".equals((String)request.getAttribute("CompOffEnabled")))
						{
					%>
					else if(obj.value=="CE"){
						var answer = confirm ('<bean:message key="alertProvideCompOff"/>')
						if (answer)
						{	
							populateCompOffMap(obj);	
						}
						else
						{
							obj.value="H";
							return true;
						}	
						return true;
					}
					<% 
						}
					%>
					else
					{
						alert('<bean:message key="alertEnterPAHP"/>');
						obj.focus();
						obj.value="";
						return false;
					}		
				}
				else
				{		
					alert('<bean:message key="alertEnterPAHP"/>');
					obj.focus();
					obj.value="";
					return false;		
				}	
			}	
		}
		else if(value.length>=1)
		{
			if(value == "H")
			{			
			<% if (!request.getAttribute("OTEnabled").equals("Y") && !request.getAttribute("CompOffEnabled").equals("Y")) 
				{
			%>
					if(obj.value !="P" && obj.value !="H" && obj.value !="A" )
					{
						alert('Holiday can be only Present, Absent');
						obj.value =value;
					}
			<%
				} else {
			%>
			if(obj.value !="P" && obj.value !="H" && obj.value !="A" && obj.value !="OT" && obj.value !="CE")
			{
				alert('Holiday can be only Present, Absent, Comp-off eligible, Over Time');
				obj.value =value;
			}
			<% 
				}
			%>

					else if (obj.value=="A")
					{
						populateMap(obj);
					}
					<% if (request.getAttribute("OTEnabled").equals("Y")) 
						{
					%>
					else if(obj.value=="OT")
					{
						populateMap(obj);
					}
					<%
						}
					%>					
					else if(obj.value=="P")
					{
						populateMap(obj);
					}
					<% if (request.getAttribute("CompOffEnabled").equals("Y")) 
						{
					%>
					else if(obj.value =="CE")
					{
						var answer = confirm ('<bean:message key="alertProvideCompOff"/>')
						if (answer)
						{	
							populateCompOffMap(obj);	
						}
						else
						{
							obj.value="H";
							return true;
						}	
						return true;
					}
					<%
						}
					%>
			}
			else if(value == "L" || value == "HL" || value == "THL" )
			{	
				if(obj.value !="P" && obj.value !="L" && obj.value !="THL")
				{
					alert('<bean:message key="alertOnlyChangedToP"/>');
					obj.value =value;
				}
				else if(obj.value =="P"  )
				{		
					if(value == "L" )
					{
						if(day != dayinM)
						{				
							populateMap(obj);
						}
						else if(day == dayinM && crrM != givenM)
						{				
							populateMap(obj);
						}
						else if(day == dayinM && crrM == givenM && finYer!=currYer )
						{				
							populateMap(obj);
						}
						return true;
					}
					else if(value == "THL" )
					{
						if(day != dayinM)
						{				
							populateMap(obj);
						}
						else if(day == dayinM && crrM != givenM)
						{				
							populateMap(obj);
						}
						else if(day == dayinM && crrM == givenM && finYer!=currYer )
						{				
							populateMap(obj);
						}
						return true;			
					}
					else
					{
						alert('<bean:message key="alertCannotLToHP"/>');
						return true;
					}
				}
				else if(obj.value =="HP")
				{		
					if(value == "HL")
					{
						if(day != dayinM)
						{			
							populateMap(obj);
						}
						else if(day == dayinM && crrM != givenM)
						{			
							populateMap(obj);
						}
						else if(day == dayinM && crrM == givenM && finYer!=currYer )
						{			
							populateMap(obj);
						}
						return true;
					}
					else if(value == "THL" )
					{
						if(day != dayinM)
						{			
							populateMap(obj);
						}
						else if(day == dayinM && crrM != givenM)
						{			
							populateMap(obj);
						}
						else if(day == dayinM && crrM == givenM && finYer!=currYer )
						{			
							populateMap(obj);
						}
						return true;		
					}
					else
					{
						alert('<bean:message key="alertConvHLtoPresent"/>');
						return true;
					}		
				}	
			}
			else
			{	
				if(obj.value!="")
				{		
					if(obj.value.length>=1)
					{	
						
						if(obj.value=="P"||obj.value =="A"||obj.value =="HP" || obj.value=="H")
						{
							if(obj.value=="A" || obj.value =="HP" || obj.value =="P" || obj.value =="H")
							{
								var boolval=checkForCompOffApprove(value,obj);
							}
							if(day != dayinM)
							{								
								populateMap(obj);
							}
							else if(day == dayinM && crrM != givenM)
							{								
								populateMap(obj);
							}
							else if(day == dayinM && crrM == givenM && finYer!=currYer )
							{	
								populateMap(obj);
							}								
							return true;
						}
						<% if (request.getAttribute("OTEnabled") == "Y") 
							{
						%>
						else if(value=="OT")
						{		
							if(obj.value!="A" && obj.value!="OT")
							{
								alert('OT can be changed only to A ');
								obj.value=value;
								return false;
							}
							else
							{
								populateMap(obj);
							}
						}
						<%
							}
						%>
						<% if (request.getAttribute("CompOffEnabled") == "Y") 
							{
						%>
						else if(obj.value =="CE")
						{
							var answer = confirm ('<bean:message key="alertProvideCompOff"/>')
							if (answer)
							{	
								populateCompOffMap(obj);	
							}
							else
							{
								obj.value="H";
								return true;
							}	
							return true;
						}	
						<%
							}
						%>
						else
						{	
							alert("test");		
							alert('<bean:message key="alertEnterPAHP"/>');
							obj.focus();
							obj.value="";
							return false;
						}			
					}			
					else
					{			
						alert('<bean:message key="alertEnterPAHP"/>');
						obj.focus();
						obj.value="";
						return false;			
					}		
				}
			}
		}
	}
	
		function goToShow(value)
		{

		if(value!=0)
		{
		window.location ="<%=request.getContextPath()%>/leave/Show.jsp?leavevalue="+value;
		}
		}


		</script>


		<%
		if(employeeSet!=null && !employeeSet.isEmpty())
		{
		%>
		<div align="center">
		<center>
		<html:form  action="/leave/AfterAttendenceMasterAction.do?submitType=saveOrUpdateAttendence" >
		<input type=hidden name="month" id="month" value="<%=new Integer(month).toString()%> " />
		<input type=hidden name="finYear" id="finYear" value="<%=finYear.trim()%> " />

		<table  width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tbody>



		<tr>
		<td class="headingwk">
		<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>

		<div align="left" style="margin-top:4px;"><bean:message key="attendanceForMon"/> : <%=(String)EisManagersUtill.getMonthsStrVsDaysMap().get(new Integer(month))%>-<%=yer%></div></td>
		</tr>

		</table>

		<table  align="center" cellpadding="0" cellspacing="0" width="100%" style="border:1px solid #D1D9E1" border="1" id="TPTable" name="TPTable" >
		<tbody>


		<tr style="background-color:#E8EDF1;">
		<td width="3%">Serial No</td>
		<td class="tablesubheadwknew" >&nbsp;</td>
		<%
		String dOfMonth = "";
		String dateStr = "";
		for(int day =1; day <= dayOfMonth; day++)
		{
		calendar.set(yer,month-1,day);
		dOfMonth = calendar.getTime().toString().substring(0, 3);
		dateStr = new java.sql.Date(calendar.getTime().getTime()).toString();
		String value = "";
		if(setOfHol.contains(dateStr))
		{
		%>
		<tc>
		<a  href="/leave/Show.jsp?holvalue=<%=value%>"><td   class="tablesubheadwknew" style="color: rgb(255, 0, 0);"><%=new Integer(day).toString()+"-"+dOfMonth%></td></a>
		</tc>
		<%

		}
		else
		{
		%>
		<tc>
		<td   class="tablesubheadwknew" align="center" ><%=new Integer(day).toString()+"-"+dOfMonth%>
		</tc>

		<%
		}

		}
		%>
		</tr>
		<tr align="center" background-color:#E8EDF1;">
		<td>&nbsp;</td>

		<td   class="tablesubheadwknew" >&nbsp;</td>
		<%
		for(int day =1; day <= dayOfMonth; day++)
		{
		String check = new Integer(day).toString();
		calendar.set(yer,month-1,day);
		dOfMonth = calendar.getTime().toString().substring(0, 3);
		dateStr = new java.sql.Date(calendar.getTime().getTime()).toString();
		String value = "";
		if(setOfHol.contains(dateStr))
		{
		%>
		<tc>
		<td align="center" class="tablesubheadwknew">&nbsp;<input type="checkbox"  class="inputattendancestyle" disabled=true > </td>

		</tc>
		<%
		}
		else
		{
		%>
		<tc>
		<td align="center" class="tablesubheadwknew">&nbsp;<input type="checkbox"  class="inputattendancestyle" onclick=markPresent("<%=check%>",this) > </td>

		</tc>
		<%
		}
		}
		%>

		</tr>


		<%
		int empCount=1;
		for(Iterator iter =employeeSet.iterator();iter.hasNext(); )
		{

		//Integer id = (Integer)iter.next();
		// PersonalInformation personalInformation = eisManager.getEmloyeeById(id);
		PersonalInformation personalInformation =(PersonalInformation)iter.next();
		Integer id = personalInformation.getIdPersonalInformation();
		Map attmp = (Map)employeeMap.get(personalInformation);
		Date dateOfRetirement=null;
		Assignment  empAssignment=null;
		Date LastAssignmentTodate = null;
		if(eisServiceImpl.getEmloyeeById(personalInformation.getIdPersonalInformation())!=null)
		{
			dateOfRetirement=eisServiceImpl.getEmloyeeById(personalInformation.getIdPersonalInformation()).getRetirementDate();
			personalInformation.getIdPersonalInformation();
	        empAssignment = eisServiceImpl.getLastAssignmentByEmp(personalInformation.getIdPersonalInformation());
	        LastAssignmentTodate= empAssignment.getAssignmentPrd().getToDate();
	        System.out.println("LastAssignmentTodate-----------"+LastAssignmentTodate);
	    }
	    
	    Map startEndDateMap = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(month,financial);	    
	    Set holidaySet = EisManagersUtill.getEmpLeaveService().getHolidaySetForEmployeInDateRange((Date)startEndDateMap.get("startDate"),
																				(Date)startEndDateMap.get("endDate"),personalInformation);
	    
		%>
		
		<input type=hidden name="employeeId" id="employeeId" value="<%= personalInformation.getIdPersonalInformation()%> " />
		
		<tr style="background-color:#E8EDF1;">
		<td> <%=empCount %></td>
		<td   width="150px;" style="padding:5px;border-bottom:1px solid #D1D9E1;border-left:1px solid #D1D9E1;"><%=personalInformation.getEmployeeCode()%>-<%=personalInformation.getEmployeeFirstName()%> &nbsp;<%=personalInformation.getEmployeeLastName()%></td>
		<%
		Integer lveValue = new Integer(0);
		for(int i=1; i<= dayOfMonth; i++)
		{
		String attIdName = "attendence" + new Integer(i).toString();
		lveValue = new Integer(0);

		String attId = "attId" + new Integer(i).toString();
		String value ="";
		String dateStr1 ="";

		Attendence attendence =null;
		/*
		This part of code compares the date of appointment with selected date and displays
		NA (Not Available) and makes that box as read only, if the date of appointment is somewhere
		in the middle of that month.
		*/
		Date dateOfFirstApp = (Date)personalInformation.getDateOfFirstAppointment();
	    
		if(dateOfFirstApp != null)
		{
		//first appointment
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateOfFirstApp);
		int app_dayinMonth = cl.get(Calendar.DAY_OF_MONTH);
		int app_year = cl.get(Calendar.YEAR);
		int app_month =cl.get(Calendar.MONTH) + 1;

		Calendar caldr = Calendar.getInstance();
		caldr.set(yer,month-1,i);

       if(LastAssignmentTodate!=null)
		{
		  
         Calendar lastAppTodate = Calendar.getInstance();
		 lastAppTodate.setTime(LastAssignmentTodate);
		 int last_Ass_dayinMonth=lastAppTodate.get(Calendar.DAY_OF_MONTH);
		 int last_Ass_year=lastAppTodate.get(Calendar.YEAR);
		 int last_Ass_day = lastAppTodate.get(Calendar.MONTH)+1;
		 
		 Calendar todateLastApp = Calendar.getInstance();
		 todateLastApp.set(yer,month-1,i);
		 
		if((cl.get(Calendar.YEAR) == caldr.get(Calendar.YEAR) && cl.get(Calendar.MONTH)== caldr.get(Calendar.MONTH) && i<cl.get(Calendar.DAY_OF_MONTH)) 
		 || (lastAppTodate.get(Calendar.YEAR) == todateLastApp.get(Calendar.YEAR) && lastAppTodate.get(Calendar.MONTH)== todateLastApp.get(Calendar.MONTH)) && i > lastAppTodate.get(Calendar.DAY_OF_MONTH))
		{
			value="NA";
		}
		
		}
		
		//retirement date
		if(dateOfRetirement!=null)
		{
		    Calendar retcl = Calendar.getInstance();
			retcl.setTime(dateOfRetirement);
			int ret_dayinMonth = retcl.get(Calendar.DAY_OF_MONTH);
			int ret_year = retcl.get(Calendar.YEAR);
			int ret_month =retcl.get(Calendar.MONTH) + 1;
	
			Calendar calRet = Calendar.getInstance();
			calRet.set(yer,month-1,i);
			
		if((retcl.get(Calendar.YEAR) == calRet.get(Calendar.YEAR) && retcl.get(Calendar.MONTH)== calRet.get(Calendar.MONTH) && i>retcl.get(Calendar.DAY_OF_MONTH)) )
		{
			value="NA";
		}
		
		}
		
		}

		/***********/
		if(value != "NA")
		{
		if(attmp!=null)
		attendence = (Attendence)attmp.get(new Integer(i));
		if(attendence!=null)
		{
			HibernateUtil.getCurrentSession().lock(attendence,LockMode.NONE);
			if(attendence.getAttendenceType().getName().equals(EisConstants.ABSENT))
			value ="A" ;
			else if(attendence.getAttendenceType().getName().equals(EisConstants.PRESENT))
			value ="P";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.TWOHALFLEAVE_PAID) || attendence.getAttendenceType().getName().equals(EisConstants.TWOHALFLEAVE_UNPAID))
			value ="THL";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.HALFPRESENT))
			value ="HP";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.HALFLEAVE_PAID) || attendence.getAttendenceType().getName().equals(EisConstants.HALFLEAVE_UNPAID))
			value ="HL";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.LEAVE_PAID) || attendence.getAttendenceType().getName().equals(EisConstants.LEAVE_UNPAID))
			value ="L";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.COMP_OFF))
			value ="C";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.OVER_TIME))
			value ="OT";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.COMPOFF_ELIG))
			value="CE";
			else if(attendence.getAttendenceType().getName().equals(EisConstants.HOLIDAY))
			value="H";
		}
		else
		{
		calendarHol.set(yer,month-1,i);
		dateStr1 = new java.sql.Date(calendarHol.getTime().getTime()).toString();
		if(holidaySet.contains(dateStr1))
		{
		value = "H";
		}
		}
		}
		//Populating AttendenceMapCreate map when loading the page
		Map mapCreate = (Map)session.getAttribute("AttendenceMapCreate");		
		Map mapModify = (Map)session.getAttribute("AttendenceMapModify");
		if(attendence == null){
			if(value != "" && value != "NA")//no need to take these values into attendeance table
				mapCreate.put(i+" "+personalInformation.getIdPersonalInformation(),value);
		}
		
		/*else{
			if(value != "")
				mapModify.put(attendence.getId().toString(),value);
		}*/
			
		
		%>
		<input type=hidden name="<%=attId%>" id="<%=attId%>" value="<%=attendence==null? i+" "+personalInformation.getIdPersonalInformation():attendence.getId().toString()%>" />
		<tc>
		<%
		if(value.equals("C") || value.equals("NA") || value.equals("L")  || value.equals("HL")  || value.equals("THL"))
		{
		%>
		<td align="center" class="tablesubheadwknew">
		<input type="text"  style="width: 20px;margin:2px;" class="selectwk" name="<%=attIdName.trim()%>" id="<%=attIdName.trim()%>"  value ="<%=value%>"   readOnly = "true" >
		</td>
		<%
		}
		else
		{
		%>
		<td align="center" class="tablesubheadwknew">
		<input type="text"  style="width: 20px;margin:2px;" class="selectwk" name="<%=attIdName.trim()%>" id="<%=attIdName.trim()%>"  value ="<%=value%>" ondblclick=goToShow("<%=lveValue%>"); onBlur=checkAttendence("<%=value%>","<%=attId%>",this);>
		</td>
		<%
		}
		%>
		</tc>
		<%
		}
		%>
		<%
		empCount++;
		}
		%>
		</tr>


		</tbody>
		</table>
		<br>
		<table id = "submit" border="0" cellpadding="0" cellspacing="0" width="100%" value = "submit">
		<tr align = "center" >
		<td><html:button styleClass="buttonfinal" value="Save" property="b2" onclick="ButtonPressNew('savenew',this);" /></td>


		</tr>
		</table>
		</html:form>
		</table>

		<table border="0" cellpadding="0" cellspacing="0" width="100%" >
	<tbody>
	<tr>
	<td bgcolor="#ffffff" align="left">
	<div style="background-color:#FFFFEE;border:1px solid #EEEE00;color:#000000;margin-top:10px;padding:1px;width:1210px" align="left" >
<table border="0" >
<tr>
<td style="float: left; width: 10px; margin-right: 30px;margin-top:20px;" align="center">  
<img height="40" width="40" alt="Help" src="../common/image/help-new.gif" align="absmiddle"/>

</td>
<td>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="HolidayType"/>&nbsp;&nbsp;<bean:message key="AttLeaveType"/>&nbsp;&nbsp;
<bean:message key="AttPresent"/>&nbsp;&nbsp;<bean:message key="AttHalfpresent"/>&nbsp;&nbsp;<bean:message key="AttAbsent"/>&nbsp;&nbsp;
<bean:message key="AttHalfLeave"/>&nbsp;&nbsp;<bean:message key="Att2HalfLv"/>&nbsp;&nbsp;<bean:message key="AttNotApplicable"/>
&nbsp;&nbsp; <bean:message key="CompoffEligible"/>&nbsp;&nbsp;<bean:message key="CompensatoryHoliday"/>&nbsp;&nbsp;<br>




<bean:message key="helpNoteForAttendence"/>

		<!-- <bean:message key="FirstPoint"/> <br>
		<bean:message key="SecondPoint"/> <br>
		<bean:message key="ThirdPoint"/> <br>
		4) We can change Holidays to Present. A compOff is provided if we change Holiday to Present. <br>
		<bean:message key="fifthPoint"/><br>
		6) We can also change Holidays to OT(Over Time)-->
</td>
</tr>
</table>

		</div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
  </table>
  </div>
<div class="buttonholderwk">&nbsp;
		</center>
		</div>
		</div>
		<%

		}
}catch(Exception e)
{
	e.printStackTrace();
	throw e;
}
		%>
		</td>
		</tr>
		<!-- Body Section Ends -->
		</table>
		
		</center>
		
		</div>
		<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
		</html>
		
		</body>
