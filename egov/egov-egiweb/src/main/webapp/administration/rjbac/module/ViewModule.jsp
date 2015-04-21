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
<%@page import="java.util.*,
		org.egov.lib.admbndry.*,
		org.egov.infstr.commons.Module" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>View Module</title>
	<script>


		var idValue = ""; var nameValue = ""; var descValue = "";

		//All you need to do is to pass the category name which you have mentioned in the applname-config.xml.

		function callGenericScreenModule(obj)
		{
			//alert("Inside callGenericScreenModule: obj11111="+obj.value);

			//Need to pass the application config file name
			var xmlconfigname = "egov_config.xml";
			var categoryname = "SelectModule";

			// Opens the Generic Search Screen. We need to send the name of the Category (we have to write a category in the applname-config.xml file)

			var mozillaFirefox=document.getElementById&&!document.all;
			if(mozillaFirefox)
			{window.open("<%=request.getContextPath()+"/commonyui/egov/genericScreenWithLeaf.jsp"%>"+"?xmlconfigname="+xmlconfigname+"&categoryname="+categoryname,"","height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");}
			else
			{window.showModalDialog("<%=request.getContextPath()%>/commonyui/egov/genericScreenWithLeaf.jsp"+"?xmlconfigname="+xmlconfigname+"&categoryname="+categoryname,window,'dialogHeight:500px;dialogWidth:600px');

			//Once the window is closed you will have values for the above variables. Its your responsibility to store it in respective html field inputs.

				if(nameValue != "")

				{
				
				assignValues(obj);
				} 
			}
		}
		function assignValues(obj){
				var rowObj=getRow(obj);
				var table= document.getElementById("mod_table");

				getControlInBranch(table.rows[rowObj.rowIndex],'moduleId').value = idValue;
				getControlInBranch(table.rows[rowObj.rowIndex],'moduleName').value = nameValue;
				nameValue="";idValue="", descValue = "";
		} 
		function submitForm(obj)
		{
			if(document.moduleForm.moduleId.value == "")
				alert("Please select a module");
			else
			{
				if(obj.id == "updateModule")
				{
					window.location="BeforeUpdateModule.do?moduleId="+document.moduleForm.moduleId.value;
				}
				else
				{
					window.location="BeforeCreateOrUpdateAction.do?moduleId="+document.moduleForm.moduleId.value;
				}
			}
		}

	</script>

	<html:javascript formName="moduleForm"/>
</head>
<%
		Module module = null;
		Integer moduleId = null;
		String moduleName = "";
		String msg = "";
		if(request.getAttribute("module") != null)
		{
			module=(Module)request.getAttribute("module");
			if(module != null)
			{
				moduleId = module.getId();
				moduleName = module.getModuleName();
			}
		}
		if(request.getAttribute("MESSAGE") != null)
		{
			msg = (String)request.getAttribute("MESSAGE");
		}
%>

<script>
function showMsg()
{
	if(document.moduleForm.msg != null && document.moduleForm.msg.value != "")
	{
		alert(document.moduleForm.msg.value);
		document.moduleForm.msg.value = "";
	}
}
</script>
<body onload="showMsg();">
<html:form action="/admin/BeforeUpdateModule">



	<table width="600" align="center" id="mod_table" >
	  <tr>
	    <td width="600" height="20" bgcolor="#dddddd" align="middle" class="tableheader" colspan="2">
		Update Module OR Create/Update Action</td>
	  </tr>
			<input type="hidden" name="msg" value="<%=msg%>" />
	        <tr>
	          <td width="20%"  class="fieldcellwithinput" height="34" align="center">
				  <bean:message key="moduleName" /><font color="#FF0000">*</font></td>
	          <td width="80%" class="fieldcellwithinput" height="34" align="center" colspan="2" >
				  <html:hidden property="moduleId" value="<%=moduleId!=null?moduleId.toString():""%>"/>
				  <html:text property="moduleName" value="<%=moduleName%>"styleClass="ControlText" readonly="true" />
				  <input type="button" value="..."  id="ModuleTreeMenu" onclick="callGenericScreenModule(this);"></td>
	          </tr>
		<tr>
	          <td width="50%"  align="right" >
				<input  type="button" id="updateModule" class="button" value="Update Module" onclick="submitForm(this);"></td>
				<td width="50%" align="left" >
				<input type="button" id="updateAction" class="button" value="Create/Update Action"  onclick="submitForm(this);"></td>
        </tr>
	</table>

   <%
   		request.removeAttribute("MESSAGE");
   %>
</html:form>
</body>
</html>
