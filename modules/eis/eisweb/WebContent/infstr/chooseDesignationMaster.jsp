
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
org.egov.infstr.commons.dao.*,
		 org.egov.infstr.commons.client.*"

		  	
		  				  		
%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Choose Asset Category</title>
	
	<LINK rel="stylesheet" type="text/css" href="/css/egov.css">
		<LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">
		<SCRIPT type="text/javascript" src="../javascript/calender.js" ></SCRIPT>
		<SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js" type="text/javascript"></SCRIPT>
		<SCRIPT type="text/javascript" src="../javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
		<SCRIPT type="text/javascript" src="../script/jsCommonMethods.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
	
<script>
function checkMode()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");
		
	}
}
function checkInput()
{
	if(document.desigForm.id.options[document.desigForm.id.selectedIndex].value == 0)
	{
		alert("Please Select Designation");
		return false;
	}
}
</script>
</head>
<%@ include file="/staff/egovHeader.jsp" %>
<!-- Header Section Ends -->

<body onload="checkMode()" >
<table align=center>
<table align='center' id="table2">
<tr>
<td>

<div id="main"><div id="m2"><div id="m3">

<br>
<table align=center>
<tr><td>
<div id="main"><div id="m2"><div id="m3">
<html:form  action="/infstr/BeforeDesignationMasterAction?submitType=setIdForDetails" onsubmit="return checkInput()">
<table align='center' class="tableStyle" id="table3"> 
 <tr>
 <td class="labelcell" colspan="2">&nbsp;</td> 
</tr>
<tr>
<td class="labelcellforsingletd" align="right">Choose Designation <SPAN class="leadon">*</SPAN>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="smallfieldcell"  align="center">
	<select  name="Id" id="Id">
	<option value='0'>----choose----</option>
		<%
		DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			Map desMap = designationMasterDAO.getAllDesignationMaster();
		for (Iterator it = desMap.entrySet().iterator(); it.hasNext(); ) 
		{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%=entry.getValue()%></option>

		<%
		}
		%>
		
		
		
	</select>
	<input type=hidden name="viewMode" id="viewMode" value="<%= session.getAttribute("mode") %> " />
</td>
</tr>
<tr><td class="labelcell5" colspan=2>&nbsp;</td></tr> 
<tr ><td  align="right"><html:submit value="Submit" styleClass="button"/></td>
<td  align="center">&nbsp;</td>
</tr>
</table>
</html:form>
</div></div></div>
</td></tr>
</table>
</center>
</body>
</html>