
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import=
"java.util.*,
 org.egov.infstr.utils.*,
org.egov.budget.services.*,
 org.apache.log4j.Logger,
 org.egov.pims.*,
 org.egov.pims.empLeave.dao.*,
 org.egov.pims.empLeave.model.*,
 org.egov.pims.empLeave.service.*,
 org.egov.pims.dao.*,
 org.egov.pims.model.*,
 org.egov.pims.service.*,
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
    <title><bean:message key="PersonalInfoSys"/></title>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
   <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>
<%



PersonalInformation personalInformation = null;
TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory().getTypeOfLeaveMasterDAO();
EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
DisciplinaryPunishment  disciplinaryPunishment = null;
String disiplinaryId =request.getParameter("disiplinaryId");
disciplinaryPunishment = employeeServiceImpl.getDisciplinaryPunishmentById(new Integer(disiplinaryId));
personalInformation = disciplinaryPunishment.getEmployeeId();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

<script>


function ButtonPressNew(arg)
{
	if(arg == "savenew")
	{

		document.forms("pIMSForm").action = "${pageContext.request.contextPath}/pims/AfterPIMSAction.do?submitType=saveDisciplinaryApproval";
		document.forms("pIMSForm").submit();
	}
	if(arg == "close")
	{
		window.close();
	}

}

function goindex(arg)
{

	if(arg == "Index")
	{
	alert('2');
		document.forms("pIMSForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("pIMSForm").submit();
	}


}


</script>


<div align="center">
<table align='center'>
<body onload = "setLength()"/>


<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">

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

<table align='center' id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDisciplinaryApproval" >
<input type = hidden name="dispId" id="dispId" value="<%=disciplinaryPunishment.getDisciplinaryPunishmentId()==null?"0":disciplinaryPunishment.getDisciplinaryPunishmentId().toString()%>" />

<table  style="width: 800;" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" style="width: 785;" colspan="5" >
<tbody>
<tr>
  <td colspan="8" height=20 bgcolor=#dddddd align=middle  class="tableheader">
<p><bean:message key="TrainingParticulars"/>&nbsp;&nbsp;&nbsp;</td>
  </tr>
<tr>
<td colspan="8"  class = "labelcellmedium"><bean:message key="LeaveApproval"/></td>
</tr>
<tr>
	  		<td class="labelcell" >
	  		<bean:message key="EmployeeName"/> </td>

	  		<td  class="labelcell" ><%=personalInformation.getEmployeeFirstName()%></td>
	  		<td class="labelcell" >
				  		<bean:message key="EmployeeCode"/> </td>

	  		<td  class="labelcell" ><%=personalInformation.getEmployeeCode()%></td>
</tr>
</table>

 <table  style="width: 800;" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" id="TPTable" name="TPTable" >
    <tbody>
    <tr>

    	  		<td class="labelcell" >
    				  		<bean:message key="ChargeMemoNo"/> </td>

    	  		<td  class="labelcell" ><%=disciplinaryPunishment.getChargeMemoNo()%></td>

    	  		<td class="labelcell" >
			  <bean:message key="ChargeMemoDate"/> </td>

    	  		<td  class="labelcell" ><%=disciplinaryPunishment.getChargeMemoDate()%></td>

</tr>


<tr>
    	  		<td class="labelcell" >
    	  		<bean:message key="ApplicationNum"/></td>

 	<td  class="labelcell" ><%=disciplinaryPunishment.getApplicationNumber()%></td>

 	<td class="labelcell" ><bean:message key="Status"/></td>
    <td   class="labelcell" width="305" >
        <select style = "width:170px" name="statusId" id="statusId" >
        	<option value='0'><bean:message key="Choose"/></option>
        		<%


        		Map statusMap =(Map)session.getAttribute("statusMap");
        		for (Iterator it = statusMap.entrySet().iterator(); it.hasNext(); )
        		{
        			Map.Entry entry = (Map.Entry) it.next();
        		%>
        		<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue() == 1? "selected":"")%>><%= entry.getValue() %></option>

        		<%
        		}
        		%>



    	</select>
</td>

</tr>


 </tbody>
</table>


<table id = "submit" style="width: 810;"  cellpadding ="0" cellspacing ="0" border = "1" value = "submit">
<tr >
	<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPressNew('savenew')" /></td>
	<td><html:button styleClass="button" value="Index" property="b4" onclick="goindex('Index')" /></td>
	<td><html:button styleClass="button" value="Close" property="b3" onclick="ButtonPressNew('close')" /></td>
</tr>
</table>
 </html:form>
</table>
</center>
</div>
</div>
</td>
</tr>
<!-- Body Section Ends -->
</<table>
</body>