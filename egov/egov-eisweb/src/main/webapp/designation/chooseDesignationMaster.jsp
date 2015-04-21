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
<%@ page import="java.util.*,
org.egov.pims.commons.dao.*,
		 org.egov.pims.commons.client.*"



%>


<html>
<head>

<% 
    String mode = (String)session.getAttribute("mode");
%>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Choose Designation</title>

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
	if(document.getElementById("Id").value == 0)
	{
		alert("Please Select Designation");
		return false;
	}
}
</script>
</head>


<body onload="checkMode()" >

<html:form  action="/commons/BeforeDesignationMasterAction?submitType=setIdForDetails">

<div class="formmainbox"><div class="insidecontent">
<div class="rbroundbox2">
<div class="rbtop2"><div></div></div>
<div class="rbcontent2">

<table width ="95%" cellspacing="0" border="0" cellpadding ="0">
<tr>
<td>
<table width="95%" class="tableStyle" id="table3">
 <tr>
 <td class="whiteboxwk" >&nbsp;</td>
</tr>
<tr>
<td class="whiteboxwk" ><span class="mandatory">*</span>Choose Designation&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="whitebox2wk"  >
	<select  name="Id" id="Id" class="selectwk">
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
	</td>

	<input type=hidden name="viewMode" id="viewMode" value="<%=mode%> " />
</td></tr>
<tr><td class="whiteboxwk" >&nbsp;</td></tr>
<tr >

<td  align="center">&nbsp;</td>
</tr>
</table>
</td>
</table>
<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
  </div>
	<div class="rbbot2">
	<div></div></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
	<%if(mode.equals("modify")){%>

<td  align="right"><html:submit value="Modify" styleClass="buttonfinal" onclick="checkInput();"/>
</td>
<%}else if(mode.equals("view")){%>
<td  align="right"><html:submit value="View" styleClass="buttonfinal" onclick="checkInput();"/>
</td>
<%}%>
	<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()" />
	</div>
	
</html:form>

</body>
</html>
