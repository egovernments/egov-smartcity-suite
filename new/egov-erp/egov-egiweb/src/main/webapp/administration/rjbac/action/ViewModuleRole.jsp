<%@page import="java.util.*,
		org.egov.lib.admbndry.*,org.egov.exceptions.EGOVRuntimeException" %>
<%@ include file="/includes/taglibs.jsp" %>

<SCRIPT type="text/javascript" src="./script/jsCommonMethods.js"></Script>
<SCRIPT type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></Script>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Role Action Setup</title>
	<LINK rel="stylesheet" type="text/css" href="../css/egov.css">
	<link rel="stylesheet" href="<%=request.getContextPath() +"/css/egov.css"%>" type="text/css" />
		<link rel="stylesheet" href="<%=request.getContextPath() +"/css/ccMenu.css"%>" type="text/css" />
		<script type="text/javascript" src="<%=request.getContextPath() +"/css/curvy/rounded_corners.inc.js"%>"></Script>
	<script>

		function resetRole()
		{
			document.roleActionForm.roleId.value = 0;
		}

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
	function validate()
	{
		if(document.roleActionForm.moduleName.value == "")
		{
			alert("Please Select a Module.");
			document.roleActionForm.moduleName.focus();
			return false;
		}
		else if(document.roleActionForm.roleId.value == "0")
		{
			alert("Please Select a Role.");
			document.roleActionForm.roleId.focus();
			return false;
		}
		if(document.roleActionForm.moduleId.value == "")
		{
			//alert("Please Select a moduleId.");
			<%Integer moduleId =(Integer) session.getAttribute("moduleId");
			System.out.println("the value of the module id >>>>>>>>>>>   " + moduleId);%>
			document.roleActionForm.moduleId.value="<%=moduleId%>";
			return true;
		}
		return true;
	}

	</script>

<html:javascript formName="roleActionForm"/>
</head>
<body onload="resetRole()">
<html:form action="/admin/BeforeCreateOrUpdateRoleAction" onsubmit="return validateRoleActionForm(this);">

	<table width="700px" id="signup" align="center">
	<tr><td>

	<center>

	<table width="700" align="center" id="mod_table" >
	  <tr>
	    <td width="700" height="20" bgcolor="#dddddd" align="middle" class="tableheader" colspan="2">
		Role Action Setup</td>
	  </tr>
		<%
			List lstRoles = null;
			if(request.getAttribute("lstRoles") != null)
				lstRoles = (List)request.getAttribute("lstRoles");
			if(lstRoles == null)
				throw new EGOVRuntimeException("List of Roles is null");
		%>

	          <tr>
				  <td width="40%"  class="labelcell" height="34" align="center">
					  <bean:message key="moduleName" /><font color="#FF0000">*</font></td>
				  <td width="40%" class="labelcell" height="34" align="left">
					  <html:hidden property="moduleId" />
					  <html:text property="moduleName" styleClass="ControlText" readonly="true" />
					  <input type="button" value="..."  id="ModuleTreeMenu" onclick="callGenericScreenModule(this);"></td>
	          </tr>
	          <tr>
	            <td width="40%"  class="labelcell" height="34" align="center">
						  <bean:message key="RoleName" /><font color="#FF0000">*</font></td>
					  <td width="40%" class="labelcell" height="34" align="left">
						<html:select property="roleId" >
							<html:option  value="0">Choose</html:option>
						 <html:options collection="lstRoles" property="id" labelProperty="roleName" />
						 </html:select>
					</td>
	          </tr>
			<tr>
				  <td width="50%"  align="center"  colspan="2">
					<html:submit value="Setup Action" onclick="return validate();"/></td>
			</tr>
	</table>
	</center>


	   </tr></td>
   </table>
</html:form>
</body>
</html>