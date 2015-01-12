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