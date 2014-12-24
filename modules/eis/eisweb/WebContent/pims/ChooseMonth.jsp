<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,org.egov.pims.*,org.egov.exceptions.EGOVRuntimeException"


%>
<%@ include file="/staff/egovHeader.jsp" %>
<%

String className ="";
if(request.getParameter("className")!=null)
	className = request.getParameter("className").trim();
else
	className = ((String)session.getAttribute("className")).trim();
Map genericMap  = (Map)session.getAttribute("genericMap");
Map genericName  = (Map)genericMap.get("genericName");
String nameOfMaster = (String)genericName.get(className);
System.out.println("1"+genericMap);
System.out.println("1"+className);
Map genMap = (Map)genericMap.get(className.trim());
System.out.println("1"+genMap.entrySet());
		
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title><%=nameOfMaster%> <bean:message key="EmpName"/></title>

	<LINK rel="stylesheet" type="text/css" HREF="<%=request.getContextPath()%>/css/egov.css">
	<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ccMenu.css">

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
	if(document.genericBeforeForm.id.options[document.genericBeforeForm.id.selectedIndex].value == 0)
	{
		alert('<bean:message key="alertSel"/>');
		return false;
	}
}
</script>
</head>
<body onload="checkMode()">

<!-- Header Section Begins -->

<!-- Header Section Ends -->


<table align=center>
<table align='center' id="table2">
<tr>
<td>

<div id="main"><div id="m2"><div id="m3">
<html:form  action="/pims/BeforeGenericMasterAction?submitType=setIdForDetails" onsubmit="return checkInput()">
<table align='center' class="tableStyle" id="table3">
 <tr>
 <td class="labelcell" colspan="2">&nbsp;</td>
</tr>
<tr>
<td class="labelcellforsingletd" align="right"><%=nameOfMaster%> <bean:message key="EmpName"/><SPAN class="leadon">*</SPAN>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="smallfieldcell"  align="center">
	<select  name="Id" id="Id">
	<option value='0'><bean:message key="Choose"/></option>
		<%
		
		for (Iterator it = genMap.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%= entry.getValue()  %></option>

		<%
		}
		%>



	</select>
	<input type = hidden name="className" id="className" value="<%=className%>" />
	<input type=hidden name="viewMode" id="viewMode" value="<%= session.getAttribute("mode") %> " />
</td>
</tr>
<tr><td class="labelcell5" colspan=2>&nbsp;</td></tr>
<tr ><td  align="right"><html:submit value="Submit" styleClass="button"/></td>
<td><html:button styleClass="button" value="Search" property="b3" onclick="window.open('/staff/index.jsp')" /></td>
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