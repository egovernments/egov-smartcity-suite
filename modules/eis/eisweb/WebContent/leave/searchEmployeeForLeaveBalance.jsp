
<%@ include file="/includes/taglibs.jsp" %>

<%@ page import=
"java.util.*,
 org.egov.pims.*,
 org.egov.pims.empLeave.dao.*,
 org.egov.pims.empLeave.model.*,
 org.egov.pims.empLeave.service.*,
 org.egov.pims.dao.*,
 org.egov.pims.model.*,
 org.egov.pims.service.*,
 org.egov.infstr.utils.*,
 org.egov.pims.utils.*,
 org.egov.pims.empLeave.model.*,
 org.egov.commons.CFinancialYear,
 org.egov.pims.client.*"
%>
<html>
<head>
    
    <title><bean:message key="PersonalInfoSys"/></title>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/commoneis.js" type="text/javascript"></SCRIPT>


<%	
	String  currentfinYear=(String)session.getAttribute("currentfinYear");
	CFinancialYear financialY=(CFinancialYear)session.getAttribute("financialY");

	String  calendarYearId=(String)session.getAttribute("calYearId");
	CalendarYear calendarYr=(CalendarYear)session.getAttribute("calendarYr");

	int fYearReq = Integer.parseInt(currentfinYear.trim());
	int calYearReq =  Integer.parseInt(calendarYearId.trim());

%>

<script>

function execute()
{
	
	var target="<%=(request.getAttribute("alertMessage"))%>";

	 if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }

}

function checkAlphaNumeric(obj)
	{
		
			if(obj.value!="")
			{
  				var num=obj.value;
  				var objRegExp  = /^[a-zA-Z0-9]+$/;
  				if(!objRegExp.test(num))
					{
						alert('<bean:message key="alertPropEmpCode"/>');
  						obj.value="";
  						obj.focus();
  					}
  			}
	}

	function checkNumeric(obj,value)
	{
		
			if(obj.value!="")
			{
  				var num=obj.value;
  				var objRegExp  = /^[0-9]+$/;
  				if(!objRegExp.test(num))
					{
						alert('<bean:message key="alertEnterNumLeaveAvailable"/>');
  						obj.value=value;
  						obj.focus();
  					}
  			}
	}
	function showMsg()
  {

  	if(document.LeaveBalanceForm.designationId.options.selectedIndex ==0 && document.LeaveBalanceForm.departmentId.options.selectedIndex ==0 && document.LeaveBalanceForm.code.value == "" && document.LeaveBalanceForm.name.value == "")
  	{
		  alert('<bean:message key="alertChooseParameter"/>');
		  return false;
  	}
	else if(document.LeaveBalanceForm.typeOfLeaveMstr.options.selectedIndex ==0)
	  {
					 
				alert('<bean:message key="alertSelLeaveType"/>');
				return false;
		 
	  }
	else
  	{
		
		document.getElementById("finYear").disabled=false;
		document.LeaveBalanceForm.action = "${pageContext.request.contextPath}/leave/AfterOpeningBalanceAction.do?submitType=executeSearch";
 		document.LeaveBalanceForm.submit();

  		return true;
  	}
  	
 }

 function saveMsg()
 {
	 document.LeaveBalanceForm.action="${pageContext.request.contextPath}/leave/SaveOpeningBalanceAction.do?submitType=executeSearch";
	 document.LeaveBalanceForm.submit();

 }


</script>
</head>
	<body onLoad ="execute()" >
	
		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table id="table2" width="95%" cellpadding ="0" cellspacing ="0" border = "0">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/leave/AfterOpeningBalanceAction.do?submitType=executeSearch" >
<table   cellpadding ="0" cellspacing ="0" border = "0" width="100%" >
<tbody>
<tr>
  <td colspan="4" class="headingwk" >
<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div><bean:message key="searchLeaveOpenBalance"/></td>
  </tr>

<tr>
<td   class="whiteboxwk" ><bean:message key="Designation"/></td>
<td class="whitebox2wk" >
<select  name="designationId"   id="designationId" class="selectwk" onMouseOver="addTitleAttributes(this)">
 <option value='0' selected="selected"><bean:message key="chooseType"/></option>

			<%
			
			
			Map desMap =(Map)session.getAttribute("mapOfDesignation");
			for (Iterator it = desMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%= entry.getValue()  %></option>

		<%
		}

		%>
	</select>
</td>
				<td   class="whiteboxwk" ><bean:message key="Department"/></td>
				<td class="whitebox2wk" >
				<select  name="departmentId"   id="departmentId" class="selectwk" onMouseOver="addTitleAttributes(this)">
				 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
					<%

						Map deptmap =(Map)session.getAttribute("deptmap");
						for (Iterator it = deptmap.entrySet().iterator(); it.hasNext(); )
						{
								Map.Entry entry = (Map.Entry) it.next();

						%>
						<option value='<%= entry.getKey().toString() %>'><%= entry.getValue()  %></option>

						<%
						}

						%>
				</select>
				</td>
</tr>

<tr>
    <td  class="greyboxwk" ><bean:message key="EmployeeCode"/></td>
    <td   class="greybox2wk" >
<input type="text" id="code" name="code" onBlur="checkAlphaNumericForCode(this);" size="30" align = "center" >
</td>
 <td  class="greyboxwk"  ><bean:message key="EmployeeName"/></td>
      <td   class="greybox2wk" >
  <input type="text"id="name" name="name" size="30" align = "center">
  </td>
  </tr>
  <tr>
  <%
    List lstAccLeaveTypes =	EisManagersUtill.getEmpLeaveService().getListOfAccumulativeTypeOfLeaves();
 %>


        <td  class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="LeaveTypeApp"/><td> 
		 <select  name="typeOfLeaveMstr" id="typeOfLeaveMstr" class="selectwk">
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>	
				<%
 
			  if(lstAccLeaveTypes!=null && !lstAccLeaveTypes.isEmpty())
			  {
				  for (Iterator iter = lstAccLeaveTypes.iterator();iter.hasNext();) 
				  {
					TypeOfLeaveMaster accumulativeLeaveType=(TypeOfLeaveMaster)iter.next();
				%>
				<option  value = "<%=accumulativeLeaveType.getId()%>"><%=accumulativeLeaveType.getName()%></option>
				
			<%}%></select><%}%></td>
	
<%if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
{%>
<td  class="whiteboxwk"  >Calendar Year<td> 
<select  name="finYear"   disabled id="finYear" class="selectwk">
<option value="<%= calendarYearId %>" selected="selected"><%= calendarYr.getCalendarYear() %></option>
			<%

			Map calendarMap =(Map)session.getAttribute("calendarMap");
			for (Iterator it = calendarMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
		
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(calYearReq).intValue())? "selected":"")%>><%= entry.getValue() %></option>

			<%
			}

			%>
</select>

<%}else{%>
	<td  class="whiteboxwk" ><bean:message key="FinancialYear"/><td> 
  <select  name="finYear"   disabled id="finYear" class="selectwk">
			<option value="<%= currentfinYear %>" selected="selected"><%= financialY.getFinYearRange() %></option>
			<%

			Map finMap =(Map)session.getAttribute("finMap");
			for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
		
			if( entry.getValue().toString()!=financialY.getFinYearRange())
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(fYearReq).intValue())? "selected":"")%>><%= entry.getValue() %></option>

			<%
			}

			%>
			</select>
	</td>
<%}%>

  </tr>
  
</table>

<br>
<table id = "submit" style="width: 931;"  cellpadding ="0" cellspacing ="0" border = "0" value = "submit" onclick="return showMsg();">

		
			<tr>
			<td align="center"><html:button styleClass="buttonfinal" value="Submit" property="b2"/></td>
			</tr>
 		

</table>

<%
 
   	 try
		 {
   	 	    List employeeList = (List)request.getAttribute("employeeList");
   	        //LinkedList links=(LinkedList)request.getAttribute("links");					  
		}
			
	catch(Exception e){
		System.out.println("Exception "+e.getMessage());
		throw e;
	}
  %>
<c:if test="${employeeList != null}">
 <display:table name="${employeeList}" id="EmpLeaveopenBalanceDto" cellspacing="0" style="width: 750;" 
  export="false" defaultsort="2" pagesize = "20" sort="list"  class="its" requestURI="/leave/AfterOpeningBalanceAction.do">

		 <display:column style="width:5%"   title="Employee Code" property="empCode"> 
			
		</display:column>	
		 <display:column style="width:5%"  title="Employee Name" property="empName">
			 
		</display:column>
		<display:column style="width: 5%" title="Leave Available" >		
		<% 
		String code=(((EmpLeaveOpenbalanceDTO)pageContext.getAttribute("EmpLeaveopenBalanceDto")).getAvailableLeaves());
		
		%>
			<input type="text" name="availableLeaves" value="<%=code%>" type="number" onBlur="checkNumeric(this,'<%=code%>');"/>
				<%
			Integer leaveId=(((EmpLeaveOpenbalanceDTO)pageContext.getAttribute("EmpLeaveopenBalanceDto")).getLeaveId());
			
		%>
			<input type="hidden" name="leaveid" value="<%=leaveId%>">

			<%
			String empId=(((EmpLeaveOpenbalanceDTO)pageContext.getAttribute("EmpLeaveopenBalanceDto")).getEmpId());
			
			%>
			<input type="hidden" name="empId" value="<%=empId%>">
			
					
		</display:column>
		
	
	
		
	 </display:table>
	 <%
	    List employeeList = (List)request.getAttribute("employeeList");
		if(employeeList != null && !employeeList.isEmpty()){%>
	 <table id = "submit" style="width: 931;"  cellpadding ="0" cellspacing ="0" border = "0" value = "submit" onclick="return saveMsg();">

		
			<tr>
			<td align="center"><html:button styleClass="buttonfinal" value="Save" property="b2"/></td>
			</tr>
 		

</table>
<%}%>
	 
	</c:if>



</center>

</div>
</td>
</tr>

<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
          
</table>


</div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>



<table>
<tr></tr>
</table>

</html:form>
 <input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
</body>
</html>