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
<%@page import="org.egov.infstr.utils.StringUtils"%>
<%@page import="java.util.*,
		org.egov.lib.admbndry.*,
		org.egov.lib.rjbac.role.*,
		org.egov.infstr.commons.Module,
		org.egov.lib.rrbac.model.Action" %>

<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
	<title>Role Action Setup</title>	
	
		<%
				Module module = null;
				if(request.getAttribute("module") != null)
					module = (Module)request.getAttribute("module");
				System.out.println("------- Module Name = "+module.getModuleName());
				Set actionsSet = module.getActions();
				System.out.println("------- actionsSet = "+actionsSet.size());
				Role role = null;
				if(request.getAttribute("role") != null)
					role = (Role)request.getAttribute("role");
				Set delActions = new HashSet();
				session.setAttribute("delActions", delActions);
		%>

	<script type="text/javascript">

	
	function setDelSet(obj)
	{
		if(!obj.checked)
		{
			populateDeleteSet("delActions",obj.value);
		}
	}

	function populateDeleteSet(setName, delId)
	{
		//alert("Inside populateDeleteSet : setName = "+setName+"delId="+delId);
		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>"+"/commons/updateDelSets.jsp?type="+setName+"&id="+delId;
		//alert("url = "+url);
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4)
			{
				if(http.status == 200)
				{
					   var statusString =http.responseText.split("^");

				 }
			}
		};
		http.send(null);
	}


	</script>

	<html:javascript formName="roleActionForm"/>
</head>

<body >
	<html:form action="/admin/CreateOrUpdateRoleAction" onsubmit="return validateRoleActionForm(this);">
	<table width="700px" id="signup" align="center">
	<tr>
	<td >	
	<table  align="center"  width="700">
	<tbody>
	<tr><td>

	<center>

	<table align="center" id="action_table" >
	  <tr>
	    <td  bgcolor="#dddddd" align="center" colspan="2">
		Role Action Setup</td>
	  </tr>
	  <input type="hidden" name="moduleName" value="<%= StringUtils.emptyIfNull(module.getModuleName())%>" />
	
	<tr style="width:700" >
			<td  class="labelcell" height="34" align="center" colspan="2">
			  <bean:message key="moduleName" />: <%=StringUtils.emptyIfNull(module.getModuleName())%></td>
			</tr>
			<tr>
			  <td  class="labelcell" height="34" align="center" colspan="2">
			<bean:message key="moduleDesc" />: <%=StringUtils.emptyIfNull(module.getModuleDescription())%>
			  </td>

		</tr>
	        <tr>
	          <td class="labelcell" height="34" align="center" colspan="2">
	          		<html:hidden property="roleId" value="<%=role.getId().toString()%>" />
				  <bean:message key="RoleName" />:<%=StringUtils.emptyIfNull(role.getRoleName())%></td>
				  </tr>
	        <tr>

			<%
				if(actionsSet == null || actionsSet.size()==0)
				{
			%>
					<tr><td colspan="2" align="center">Actions does not exist for this Module.</td></tr>
								<tr>
								  <td class="button" align="center" colspan="2">
									<input type="button" name="button" value="Go Back" onclick="history.go( -1 );return true;"/></td>
					</tr>
			<%
				}
				else
				{
					for(Iterator itr=actionsSet.iterator(); itr.hasNext();)
					{
						Action action = (Action)itr.next();
						Set rolesSet = (Set)action.getRoles();
						System.out.println("------- rolesSet.size()="+rolesSet.size());
						if(action != null && action.getName()!=null)
						{
							System.out.println("------- action.getName()="+action.getName());
			%>
							<tr>
							<td class="labelcell" height="34" align="center">
							<%=action.getName()%></td>
						  <td class="labelcell" height="34" align="center"  >
			<%
							if(rolesSet != null && !rolesSet.isEmpty() && rolesSet.contains(role))
							{
			%>

								<input type="checkbox" name="actionId" value="<%=action.getId().toString()%>" checked=true onclick="setDelSet(this)"/>
			<%
							}
							else
							{
			%>

							 	<input type="checkbox" name="actionId" value="<%=action.getId().toString()%>" onclick="setDelSet(this)" />
			<%
							}

			%>
							</td>
						</tr>

			<%
						}

					}

			%>
					</table>
					<table style="width:100%">
						<tr>
						  <td class="button" align="center" colspan="2">
							<html:submit value="Save"/></td>
				</tr>
			<%
				}
			%>



	</table>
			</center>


	   </tr></td>
	   <tbody>
   </table>
   </tr></td>
    </table>
   
</html:form>
</body>
</html>
