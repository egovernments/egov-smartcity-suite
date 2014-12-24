
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,org.egov.pims.model.*"


%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Choose Asset Category</title>

	<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/egov.css">

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
		alert('<bean:message key="alertSelRecruit"/>');
		return false;
	}
}
</script>
</head>
<body onload="checkMode()">
<center>
<br>
<table align='center' id="table2">
<tr><td>

<html:form  action="/pims/BeforeSkillAndGradeAction?submitType=setIdForDetails" onsubmit="return checkInput()">
<table align='center' class="tableStyle" id="table3">
 <tr>
 <td class="labelcell" colspan="2">&nbsp;</td>
</tr>
<tr>
<td class="labelcellforsingletd" align="right"><bean:message key="ChooseSkills"/><SPAN class="leadon">*</SPAN>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="smallfieldcell"  align="center">
	<select  name="Id" id="Id">
	<option value='0'><bean:message key="Choose"/></option>
		<%
		Map skillMap =(Map)session.getAttribute("skillMap");
		for (Iterator it = skillMap.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%= entry.getValue()  %></option>

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
</div>
</td></tr>
</table>
</center>
</body>
</html>