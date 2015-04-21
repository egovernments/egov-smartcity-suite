<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %>
<c:catch var ="catchException">
<%@ page import="java.util.*,org.egov.pims.*,org.egov.exceptions.EGOVRuntimeException"%>


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
	if(document.forms[0].Id.options[document.forms[0].Id.selectedIndex].value == 0)
	{

		alert('<bean:message key="alertSelValue"/>');
		document.forms[0].Id.focus();
		return false;
	}
}
</script>
</head>
<body onload="checkMode()">
<html:form action="/pims/BeforeGenericMasterAction?submitType=setIdForDetails" onsubmit="return checkInput()">

		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
			
<!-- Header Section Begins -->

<!-- Header Section Ends -->


<table align=center>
<table align='center' id="table2">
<tr>
<td>



<table align='center' class="tableStyle" id="table3">
 <tr>
 <td class="whiteboxwk" colspan="2">&nbsp;</td>
</tr>
<tr>
<%if("Employee Status".equals(nameOfMaster)) {%>
						<td class="whiteboxwk"  ><span class="mandatory">*</span>Employee Type <bean:message key="EmpName"/></td>
						<% }else{%>
						<td class="whiteboxwk"  ><span class="mandatory">*</span><%=nameOfMaster%>  <bean:message key="EmpName"/></td>
						<% }%>
<td class="greybox2wk" >
	<select  name="Id" id="Id" class="selectwk">
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
<tr><td >&nbsp;</td></tr>



</table>




</table>
<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>

<div>

<html:submit value="Submit" styleClass="buttonfinal"/>
<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/></div>


</html:form>
</body>

</html>
</c:catch>
<c:if test = "${catchException!=null}">
The error is : ${catchException}<br>
</c:if>
