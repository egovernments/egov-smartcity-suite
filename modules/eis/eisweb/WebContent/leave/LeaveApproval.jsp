<%@ include file="/includes/taglibs.jsp" %>
<%@ page import=
"java.util.*,
org.apache.log4j.Logger,
 org.egov.pims.*,
 org.egov.pims.utils.*,
 org.egov.pims.empLeave.dao.*,
 org.egov.pims.empLeave.model.*,
 org.egov.pims.empLeave.service.*,
 org.egov.pims.dao.*,
 org.egov.pims.model.*,
 org.egov.pims.service.*,
 org.egov.infstr.utils.*,
 org.egov.commons.CFinancialYear,
 org.egov.commons.dao.CommonsDaoFactory,
 org.egov.commons.dao.FinancialYearDAO,
 org.egov.infstr.commons.*,
 org.egov.pims.commons.client.*,
 org.egov.infstr.commons.dao.*,
 org.egov.infstr.commons.service.*,
 org.egov.lib.address.dao.AddressDAO,
 org.egov.lib.address.dao.AddressTypeDAO,
 org.egov.lib.address.model.*,
 org.egov.lib.address.dao.*,
 java.text.SimpleDateFormat,
 org.egov.pims.client.*"
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Leave Approval/Reject</title>

    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>

     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>
<%
PersonalInformation personalInformation = null;
EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();

//personalInformation = eisManager.getEmloyeeById(new Integer(id));
LeaveApplication  leaveApplication = null;
String appId =request.getParameter("leaveapplicationId");


//String appId ="4";
leaveApplication = empLeaveServiceImpl.getLeaveApplicationById(new Integer(appId));

if(leaveApplication!=null)
{
    personalInformation = employeeServiceImpl.getEmloyeeById(leaveApplication.getEmployeeId().getIdPersonalInformation());
}
/*
	get El id 
*/
String eisLeaveType=null;
eisLeaveType=EGovConfig.getProperty("eis_egov_config.xml", "LEAVE_TYPE_NAME_FOR_EL", "","EIS.LEAVE");
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
String strTypeOfLeave=null;
%>

<script>


function ButtonPressNew(arg)
{
	if(arg == "savenew")
	{
		<%
			
				
			if(leaveApplication.getIsEncashment()!=null){
		if(leaveApplication.getIsEncashment().intValue()==1 && leaveApplication.getIsEncashment().intValue()!=0)
		{%>
		if(document.getElementById("workingDays").value > <%=leaveApplication.getNoOfLeavesAvai()!=null?leaveApplication.getNoOfLeavesAvai().toString():""%>)
		{
			alert("Applied days should be Less than Available Leaves");
			document.leaveForm.workingDays.focus();
			document.leaveForm.workingDays.value="";
			return false;
		}
		<%}%><%}%>
		document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterLeaveAction.do?submitType=saveLeaveApproval&status=Approved";
		document.leaveForm.submit();
	}
	if(arg == "close")
	{
		window.close();
	}

	if(arg == "reject")
	{
	   document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterLeaveAction.do?submitType=rejectLeaveApplication&LeaveapplicationId="+<%=appId%>;
	   document.leaveForm.submit();
	}

}

function goindex(arg)
{

	if(arg == "Index")
	{
	
		document.forms("leaveForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("leaveForm").submit();
	}


}

</script>

<body>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
		<div align="center">

<table align='center' width='95%'>


<tr>
<td>

</table>
</div>

<Center>



			<!-- Tab Navigation Begins -->
				<table align='center'>
				<tr>
				<td align="center">
				<!-- Tab Navigation Begins -->

<center>
</center>
<!-- Tab Navigation Ends -->
				</td>
				</tr>
				</table>
		<!-- Tab Navigation Ends -->




<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table align='center' id="table2" width="95%">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/leave/AfterLeaveAction.do?submitType=saveLeaveApproval" >
<input type = hidden name="leaveAppId" id="leaveAppId" value="<%=leaveApplication.getId()==null?"0":leaveApplication.getId().toString()%>" />

<table  cellpadding ="0" cellspacing ="0" border = "0" width="95%" >
<tbody>

<tr>
  								<td colspan="6" class="headingwk">
  									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>

									<div class="headplacer"><bean:message key="LeaveApproval"/>&nbsp;&nbsp;&nbsp;</div></td>
  							</tr>

<%
		float unpaidLeaves = 0.0f;
		if(leaveApplication.getNoOfLeavesAvai()!=null)
		{
			if(leaveApplication.getNoOfLeavesAvai().floatValue()<=0)
			{
				unpaidLeaves = leaveApplication.getWorkingDays().floatValue();
			}
			else if(leaveApplication.getNoOfLeavesAvai()!=null)
			{
				if(leaveApplication.getWorkingDays()!=null)
				{
				if(leaveApplication.getWorkingDays().floatValue() > leaveApplication.getNoOfLeavesAvai().floatValue())
				{
					unpaidLeaves =leaveApplication.getWorkingDays().floatValue()-leaveApplication.getNoOfLeavesAvai().floatValue();			
				}
				}
			}
		}
			
%>
</tr>
<tr>
	  		<td class="whiteboxwk" >
	  		<bean:message key="EmployeeName"/> </td>

	  		<td  class="whitebox2wk" ><%=personalInformation.getEmployeeFirstName()%></td>
	  		<td class="whiteboxwk" >
				  		<bean:message key="EmployeeCode"/> </td>

	  		<td  class="whitebox2wk" colspan="3"><%=personalInformation.getEmployeeCode()%></td>
</tr>
<tr>
	  		<td class="greyboxwk" >
	  		<bean:message key="TypeOfLv"/></td>
			
	  		<td  class="greybox2wk"><%=leaveApplication.getTypeOfLeaveMstr().getName()%></td>
	  		<td class="greyboxwk" >
				  		<bean:message key="datesApplied"/></td>
			<%

			if(leaveApplication.getIsEncashment()!=null){
			if(leaveApplication.getIsEncashment().intValue()!=1 && leaveApplication.getIsEncashment().intValue()==0){%>
	  		<td  class="greybox2wk" colspan="3"><%=sdf.format(leaveApplication.getFromDate())%> <b>to</b> <%=sdf.format(leaveApplication.getToDate())%></td>
			<%} else {%>

			<td  class="greybox2wk" colspan="3">N/A</td>

			<%}%><%}%>
</tr>
<tr>
	  		<td class="whiteboxwk" >

			<%
				
			if(leaveApplication.getIsEncashment()!=null){
			if(leaveApplication.getIsEncashment().intValue()!=1 && leaveApplication.getIsEncashment().intValue()==0){%>
	  		Working Days</td>

	  		<td  class="whitebox2wk" ><%=leaveApplication.getWorkingDays()!=null?leaveApplication.getWorkingDays().toString():""%></td>
			<%} else {%>
			Applied Days</td>
			<td class="whitebox2wk" ><input  type="text"  style = "width:170px"  name="workingDays" id="workingDays" value="<%=leaveApplication.getWorkingDays()!=null?leaveApplication.getWorkingDays().toString():""%>" ></td>

			<%}%><%}%>
	  		<td class="whiteboxwk" >
				  		<bean:message key="LeavesAvailable"/> </td>
			
	  		<td  class="whitebox2wk" ><%=leaveApplication.getNoOfLeavesAvai()!=null?leaveApplication.getNoOfLeavesAvai().toString():""%></td>
	  		<td class="whiteboxwk" >
				  		<bean:message key="UnpaidLeaves"/></td>
			
				  		<td  class="whitebox2wk" ><%=unpaidLeaves%></td>
				  		
</tr>

<%
		strTypeOfLeave=leaveApplication.getTypeOfLeaveMstr().getName();
		
		//EGovConfig.getProperty("payroll_egov_config.xml","APPROVED_STATUS","",EGOVThreadLocals.getDomainName()+".PaySlip")
		if(eisLeaveType!=null){
		if(strTypeOfLeave.equalsIgnoreCase(eisLeaveType))
				{


				if(leaveApplication.getIsEncashment()!=null){
			if(leaveApplication.getIsEncashment().intValue()==1 && leaveApplication.getIsEncashment().intValue()!=0){%>
<tr>
<td  class="whiteboxwk" id="IsEncashment">Is Encashment?</td>
	<td   class="whitebox2wk"  >
      <input   type="checkbox"  name="encashment" id="encashment" checked disabled >
	</td>
  </tr>

<%}else{%>


<tr>
<td  class="whiteboxwk" id="IsEncashment">Is Encashment?</td>
	<td   class="whitebox2wk" >
      <input type="checkbox"  name="encashment" id="encashment"  disabled >
	</td>
  </tr>
  <%}}%><%}%><%}%>

</table>

 <table   border = "0" id="TPTable" name="TPTable" width="95%" >
    <tbody>
    <tr>

    	  		<td class="greyboxwk" >
    				  		<bean:message key="Designation"/> </td>

    	  		<td  class="greybox2wk"  colspan="3"><%=leaveApplication.getDesigId().getDesignationName()%></td>
</tr>
<tr>
    	  		<td class="whiteboxwk" >
    	  		<bean:message key="PayElegible"/></td>
    <%

	Character ch = leaveApplication.getTypeOfLeaveMstr().getPayElegible();


	%>
<%        
			
			if(leaveApplication.getIsEncashment()!=null){
			if(leaveApplication.getIsEncashment().intValue()!=1 && leaveApplication.getIsEncashment().intValue()==0){%>
 	<td class="whitebox2wk"><input   type="text"   name="payElegible" id="payElegible" value ="<%=ch.toString().equals("1")?"Y":"N"%>" ></td>

	<%} else {%>

	<td class="whitebox2wk">Y</td>
	<%}%><%}%>

 	<td class="whiteboxwk" >Status </td>
    <td   class="whitebox2wk"  >
        <select class="selectwk" name="statusName" id="statusName" >
        	<option value='0'><bean:message key="Choose"/></option>
        		<%


        		Map statusMap =(Map)session.getAttribute("statusMap");
        		for (Iterator it = statusMap.entrySet().iterator(); it.hasNext(); )
        		{
        			Map.Entry entry = (Map.Entry) it.next();
        			System.out.println(entry.getValue());
        		%>
        		<option  value = "<%= entry.getValue().toString() %>"<%=(((Integer)entry.getKey()).intValue() == 1? "selected":"")%>><%= entry.getValue() %></option>

        		<%
        		}
        		%>



    	</select>
</td>

</tr>

<tr>
	<td  class="greyboxwk" >
		<bean:message key="Reason"/>
	</td>
	<td class="greybox2wk" colspan="3">
		 <TEXTAREA  name="reason" id="reason" maxlength="256" onkeyup="return ismaxlength(this)" wrap="soft"><%=leaveApplication.getReason()==null? " " : leaveApplication.getReason().toString().trim() %></TEXTAREA>
	</td>
</tr>


 </tbody>
</table>



 
 <tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
</table>

<div align="center">
  		<span></span>
</div>


</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>


</center>
</div>
</div>
<td></td>
</tr>
<!-- Body Section Ends -->

</<table>

<div>
<html:button styleClass="buttonfinal" value="Approve" property="b2" onclick="ButtonPressNew('savenew')" />
	<html:button styleClass="buttonfinal" value="Reject" property="b4" onclick="ButtonPressNew('reject')" />
	<html:button styleClass="buttonfinal" value="Close" property="b3" onclick="ButtonPressNew('close')" />
</div>
</html:form>
</body>
