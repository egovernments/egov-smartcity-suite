<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.pims.utils.*,
		 org.egov.pims.empLeave.dao.*,
		  org.egov.pims.empLeave.model.*,
		  org.egov.pims.empLeave.service.*,

		 org.egov.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.lib.address.dao.AddressDAO,
		 java.util.StringTokenizer,
		 org.egov.infstr.utils.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Employee Attendance Reports</title>

    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>


<%!
	EmployeeServiceImpl employeeServiceImpl ;
%>

<%
	try
	{
		employeeServiceImpl=new EmployeeServiceImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List l =EisManagersUtill.getEmpLeaveService().getNoOfWorkingDaysBweenTwoDates(sdf.parse("2008-01-01"),sdf.parse("2008-01-26"));
		System.out.println("llllllllllllllllllllllllllll"+l);
		AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory().getAttendenceDAO();

		Attendence attendence =  adtendence.checkAttendenceByEmpAndDte(new Integer(108),sdf.parse("2008-02-16"));
		System.out.println("attendence"+attendence);


	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
int m = 12;
int noOfDaysInMonth=EisManagersUtill.getMonthsVsDays(m-1).intValue();


%>



<script>
function execute()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	 if(target!="null")
	 {
		<% if(request.getParameter("ess")==null ){%>
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	}else if(request.getParameter("ess").trim().equals("1")){%>
		document.getElementById('funcAndDeptRow').style.display="none";
		<%
		}
		request.setAttribute("alertMessage",null);	%>
	 }

}
function chkOnSubmit(arg)
 {
 
	if(document.getElementById("monthId").value =="0" )
	{
		alert('<bean:message key="alertChooseMon"/>');
		document.getElementById("monthId").focus();
		return false;
	}
	if(document.getElementById("finYear").value == "" )
	{
		alert('<bean:message key="alertChoosefYr"/>');
		document.getElementById("finYear").focus();
		return false;
	}
 	if(arg == "sub")
 	{
 		var essVal = <%=request.getParameter(EisConstants.ESS)%>;
 		if(! validateForMandatory())
	  	{
	  	return false;
	  	}
 		if(!(essVal==1))
 		{		
			if(!checkDeptMandatory(document.getElementById("departmentId").value)){
					return false;
			}
 		}	
		var ess=(essVal!=null && (essVal==1))?("ess="+essVal+"&"):""; 
 		document.forms[0].action = "${pageContext.request.contextPath}/pims/AfterSearchAction.do?"+ess+"submitType=executeAttReport";
 		document.forms[0].submit();
 	}

   
}

</script>  
</head>
	<body onload = "execute();">


		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div>
			</div>
		  </div>
			  <div class="rbcontent2">
<!-- Header Section Begins -->

<!-- Header Section Ends -->
<table width="94%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterSearchAction.do?submitType=executeAttReport" >
<input type=hidden name="Id" id="Id" value="<%= new String("0") %> " />
<table  width="95%" border="0" cellspacing="0" cellpadding="0" >
<tbody>
<tr>
  <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>

 <div class="headplacer"><bean:message key="searchAttReports"/></div></td>
  </tr>

<tr id="funcAndDeptRow">
<td   class="whiteboxwk" ><bean:message key="Designation"/></td>
<td class="whitebox2wk" >
<html:select  property="designationId"   styleId="designationId" styleClass="selectwk">
 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
	<c:forEach var="designation" items="${designationMasterList}">
	<html:option value="${designation.designationId}" > ${designation.designationName}</html:option>
	</c:forEach>
	</html:select>
</td>
			
</tr>
<tr>
    <td  class="greyboxwk" ><bean:message key="EmployeeCode"/></td>
    <td   class="greybox2wk">
    <c:choose>
<c:when test="${(empty ess) and (empty param.ess)}">
<html:text styleId="code" property="code" size="10" styleClass="selectwk" value = '<%=(request.getParameter("code")==null?"":request.getParameter("code"))%>' />
</c:when>
<c:otherwise>

<html:text styleClass="selectwk" property="code" styleId="code" readonly="true"/>
 
 </c:otherwise>
 </c:choose>
</td>
 <td  class="greyboxwk" ><bean:message key="EmployeeName"/></td>
      <td   class="greybox2wk" >
      <c:choose>
<c:when test="${(empty ess) and (empty param.ess)}">
  <html:text styleId="name" property="name" size="10"  styleClass="selectwk" value = '<%=(request.getParameter("name")==null?"":request.getParameter("name"))%>' />
  </c:when>
<c:otherwise>

<html:text styleClass="selectwk" property="name" styleId="code" readonly="true"/>
 
 </c:otherwise>
 </c:choose>
  </td>
  </tr>
  <tr id = "modifyAttendence">
  <%
          String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
          CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(currentfinYear));
          int currentmonth=Calendar.getInstance().get(Calendar.MONTH) + 1;
          String mon  = (String)EisManagersUtill.getMonthsStrVsDaysMap().get(new Integer(currentmonth));
          int monReq = currentmonth;
          String monthStr = request.getParameter("monthId");
          if(monthStr!=null)
          	monReq = Integer.parseInt(monthStr.trim());
	  int fYearReq = Integer.parseInt(currentfinYear.trim());
	  String fYearStr = request.getParameter("finYear");
	  if(fYearStr!=null)
			fYearReq = Integer.parseInt(fYearStr.trim());

          %>

 <td  class="whiteboxwk"><bean:message key="Month"/></td>
  <td class="whitebox2wk" width="100%" >
    				<select  name="monthId"   id="monthId" style = "width:120px" >
    				

 					<%

 						Map mMap =EisManagersUtill.getMonthsStrVsDaysMap();
 						TreeSet set = new TreeSet(mMap.keySet());
 						for (Iterator it = set.iterator(); it.hasNext(); )
 						{
 								Integer id = (Integer) it.next();


						/*
						remove duplicate current month
						*/						
 						%>
 						<option  value = "<%= id.toString() %>"<%=((((Integer)id).intValue() == monReq)? "selected":"")%>><%= (String)mMap.get(id) %></option>

 						<%
 						}

 						%>
   				</select>
				</td>
  </td> <td  class="whiteboxwk"><bean:message key="FinancialYear"/></td>
  <td class="whitebox2wk" >
  <span class="whitebox2wk">

  				<select  name="finYear"   id="finYear" class="selectwk">
  				
  					<%

  						Map finMap =(Map)session.getAttribute("finMap");
  						for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
  						{
  								Map.Entry entry = (Map.Entry) it.next();
								/*
									remove duplicate current Financial year
								*/
						

  						%>
  						<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(fYearReq).intValue())? "selected":"")%>><%= entry.getValue() %></option>

  						<%
  						}

  						%>
  				</span></select>
				</td>

  </td>

  </tr>
</tbody>

</table>
  <%@include file="/reports/mastersByAppconfig.jsp" %>
<table id = "submit" width="100%" border="0" cellspacing="0" cellpadding="0" >
<tr >
  		<td align="center"></td>
<tr>
<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
          
</table>
<%
		
   	 try{

   	 	List attendenceReportList = (List)request.getAttribute("EmployeeAttendenceReportList");

   	 	  	LinkedList links = new LinkedList();
		  	request.setAttribute("links",links);
		  	if(attendenceReportList!= null && !attendenceReportList.isEmpty())
		  	{
		  		Iterator iter = attendenceReportList.iterator();
		  		String absent = "";
		  		String present = "";
		  		String noOfPaidDays = "";
		  		String daysInMonth = "";
		  		String noOfOverTimes = "";
		  		String noOfWorkingDaysInMonth = "";
		  		String employee = "";
		  		String paidL = "";
		  		String unPaidL = "";
		  		String paidDays = "";
		  		String compoff = "";
				String noOfOT = "";
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		StringBuffer realname =null;
		  		while (iter.hasNext())
		  		{
		  			Hashtable map=new Hashtable();
		  			EmployeeAttendenceReport cataEl = (EmployeeAttendenceReport)iter.next();

		  			present = cataEl.getNoOfPresents().toString();

		  			employee = employeeServiceImpl.getEmloyeeById(cataEl.getEmployeeId()).getEmployeeName();

		  			Float absentVal=cataEl.getNoOfAbsents().floatValue();
		  			absent = absentVal.toString();

		  			compoff = cataEl.getNoOfCompOff().toString();

		  	 		paidL=cataEl.getNoOfPaidleaves().toString();

		  	 		unPaidL=cataEl.getNoOfUnPaidleaves().toString();

		  	 		daysInMonth = cataEl.getDaysInMonth().toString();

		  	 		noOfWorkingDaysInMonth = cataEl.getNoOfWorkingDaysInMonth().toString();

					noOfOT = cataEl.getNoOfOverTime().toString();

					float fltPaidDays = 0.0f;
					Integer intDaysInMonth = new Integer(0);
					float fltAbsentDays = 0.0f;
					float fltUnPaidL = 0.0f;
					Integer intDaysFromDateOfJoin = new Integer(0);
					float fltNoOfOT = 0.0f;
					if(cataEl.getDaysInMonth() != null)
					{
						intDaysInMonth = cataEl.getDaysInMonth();
					}
					if(cataEl.getNoOfAbsents() != null)
					{
						fltAbsentDays = cataEl.getNoOfAbsents().floatValue();
					}
					if(cataEl.getNoOfUnPaidleaves() != null)
					{
						fltUnPaidL = cataEl.getNoOfUnPaidleaves().floatValue();
					}
//					if(cataEl.getNoOfDaysfromDateOfJoin()!=null)
//					{
//						intDaysFromDateOfJoin=cataEl.getNoOfDaysfromDateOfJoin();
//						fltPaidDays =intDaysFromDateOfJoin.floatValue()-fltAbsentDays-fltUnPaidL;
//					}
//					else
//						fltPaidDays = intDaysInMonth.floatValue()-fltAbsentDays-fltUnPaidL;

					if(cataEl.getNoOfPaidDays()!=null)
					{
						fltPaidDays=cataEl.getNoOfPaidDays().floatValue();
					}
					if(cataEl.getNoOfOverTime()!=null)
					{
							fltNoOfOT =cataEl.getNoOfOverTime().floatValue();
					}

		  	 		map.put("employee",employee);
		  	 		map.put("emppresent",present);
		  	 		map.put("paidDays",(new Float(fltPaidDays)).toString());
					map.put("absent",absent);
					map.put("paidL",paidL);
					map.put("unPaidL",unPaidL);
					map.put("compoff",compoff);
					map.put("daysInMonth",daysInMonth);
					map.put("noOfWorkingDaysInMonth",noOfWorkingDaysInMonth);
					map.put("noOfOT",(new Float(noOfOT)).toString());
					links.add(map);

		  		}

		  	}
		}
		catch(Exception e){}
  %>


 <display:table name="links" id="eid" cellspacing="0" style="width: 100%" export="false" defaultsort="2" pagesize = "25" sort="list"  class="its" requestURI="/pims/AfterSearchAction.do" >
	<display:setProperty name="paging.banner.placement" value="bottom"/>
	 <display:column style="width:5%"   property="employee" title="Employee Name" />
	  <display:column style="width:5%"   property="daysInMonth" title="No. of Days in Month" />
	  <display:column style="width:5%"   property="absent" title="Absent Days" />
	   <display:column style="width:5%"   property="unPaidL" title="No of UnPaid Leaves" />
	  <display:column style="width:5%"   property="paidDays" title="No of Paid days" />
	 <display:column style="width:5%"   property="emppresent" title="Present Days" />
	 <display:column style="width:5%"   property="paidL" title="No of Paid Leaves" />
	 <display:column style="width:5%"   property="compoff" title="No of CompOffs" />
	 <display:column style="width:5%"   property="noOfWorkingDaysInMonth" title="Working Days in Month" />
	 <display:column style="width:5%"   property="noOfOT" title="No of OTs" />
</display:table>


</div>

</table>

</center>

</div>
<div class="rbbot2" ><div></div></div>
</div>
</div>
</div>

<div><input type="button" class="buttonfinal" value="Submit" name="b4" onclick="chkOnSubmit('sub');" />
<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/></div>
</html:form>
</body>
