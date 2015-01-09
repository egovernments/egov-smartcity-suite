<%@page import="org.egov.infstr.utils.StringUtils"%>
<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page contentType="text/html" %>
<%@page import="java.util.*,
		org.hibernate.LockMode,
		org.egov.lib.admbndry.*,
		org.egov.infstr.commons.Module,
 org.egov.infstr.client.administration.rjbac.module.UpdateModuleAction" %>

<%@ include file="/includes/taglibs.jsp" %>
<SCRIPT type="text/javascript" src="./script/jsCommonMethods.js"></Script>
<SCRIPT type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></Script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Update Module</title>
	<link rel="stylesheet" href="<%=request.getContextPath() +"/css/egov.css"%>" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath() +"/css/ccMenu.css"%>" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath() +"/css/curvy/rounded_corners.inc.js"%>"></script>
	
	<%

			Module module = (Module)request.getAttribute("module");
			if(module == null)
				throw new EGOVRuntimeException("Could not get Module");
			System.out.println("------- Module Name = "+module.getModuleName());
	%>
	<script>
	function trimText(obj,value)
		{
		    value = value;
		    if(value!=undefined)
		   {
			   while (value.charAt(value.length-1) == " ")
			   {
				value = value.substring(0,value.length-1);
			   }
			   while(value.substring(0,1) ==" ")
			   {
				value = value.substring(1,value.length);
			   }
			   obj.value = value;
			}
		   return value ;

		}
		function getRow(obj)
		{
			if(!obj)return null;
			tag = obj.nodeName.toUpperCase();
			while(tag != 'BODY'){
				if (tag == 'TR') return obj;
				obj=obj.parentNode;
				tag = obj.nodeName.toUpperCase();
			}
			return null;
		}
		function getControlInBranch(obj,controlName)
		{
			if (!obj || !(obj.getAttribute)) return null;
			// check if the object itself has the name
			if (obj.getAttribute('name') == controlName) return obj;

			// try its children
			var children = obj.childNodes;
			var child;
			if (children && children.length > 0){
				for(var i=0; i<children.length; i++){
					child=this.getControlInBranch(children[i],controlName);
					if(child) return child;
				}
			}
			return null;
		}


		var idValue = ""; var nameValue = ""; var descValue = "";

		//All you need to do is to pass the category name which you have mentioned in the applname-config.xml.

		function callGenericScreenParentModule(obj)
		{
			//alert("Inside callGenericScreenModule: obj11111="+obj.value);

			//Need to pass the application config file name
			var xmlconfigname = "egov_config.xml";
			var categoryname = "SelectModule";

			// Opens the Generic Search Screen. We need to send the name of the Category (we have to write a category in the applname-config.xml file)

			window.showModalDialog("<%=request.getContextPath()%>/commonyui/egov/genericScreenWithLeaf.jsp"+"?xmlconfigname="+xmlconfigname+"&categoryname="+categoryname,window,'dialogHeight:500px;dialogWidth:600px');

			//Once the window is closed you will have values for the above variables. Its your responsibility to store it in respective html field inputs.

			if(nameValue != "")

			{
				var rowObj=getRow(obj);
				var table= document.getElementById("mod_table");

				if(idValue == <%=module.getId()%>)
					alert("Cannot set same module as its Parent !!");
				else
				{
					getControlInBranch(table.rows[rowObj.rowIndex],'parentModuleId').value = idValue;
					getControlInBranch(table.rows[rowObj.rowIndex],'parentModuleName').value = nameValue;
				}

				//alert("idValue " + idValue);
				//alert("descValue " + descValue);

			} nameValue="";idValue="", descValue = "";
		}

	function bodyOnLoad()
	{
		<%
			if(module.getIsEnabled().booleanValue() == true)
			{
		%>
				document.moduleForm.isEnabled.checked = true;
		<%
			}

		%>
	}


	</script>

	<html:javascript formName="moduleForm"/>
</head>

<body onload="bodyOnLoad()">
	<html:form action="/admin/UpdateModule" onsubmit="return validateModuleForm(this);">

	<table width="700" align="center" id="signup">
	<tr><td>
	<center>
	<table width="700" align="center" id="mod_table" >
	<%
		if(module == null)
		{
	%>

		  <tr>
		    <td width="600" height="20" bgcolor="#dddddd" align="middle" class="tableheader" colspan="2">
			Please Select a Module for Updation.</td>
	  	 </tr>

	 <%
		}
	 	else
	 	{
	 		
	 	%>
	  <tr>
	    <td width="600" height="20" bgcolor="#dddddd" align="middle" class="tableheader" colspan="2">
		Module Set up</td>
	  </tr>

	        <tr>
	          <td   class="labelcell" height="34" align="center">
				  <bean:message key="moduleName" /><font color="#FF0000">*</font></td>
	          <td  class="smallfieldcell" height="34" align="center" >
	          	  <html:hidden property="moduleId" value="<%=module.getId().toString()%>"/>
				  <html:text property="moduleName" styleClass="ControlText" value="<%=StringUtils.emptyIfNull(module.getModuleName())%>" readonly="true"/></td>
	          </tr>
	        <tr>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="moduleNameLocal" /></td>
	          <td  class="smallfieldcell" height="34" align="center" >
				  <html:text property="moduleNameLocal" styleClass="ControlText" value="<%=StringUtils.emptyIfNull(module.getModuleNameLocal())%>" /></td>
	          </tr>

	        <tr>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="moduleDesc" /><font color="#FF0000">*</font></td>
	          <td  class="smallfieldcell" height="34" align="center"  >
				  <html:text property="moduleDescription" styleClass="ControlText" value="<%=StringUtils.emptyIfNull(module.getModuleDescription())%>" /> </td>
	          </tr>
	        <tr>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="parentModule" /></td>
	          <td  class="smallfieldcell" height="34" align="center">
	          	  <html:hidden property="parentModuleId" value="<%=module.getParent()!=null?module.getParent().getId().toString():""%>"/>
				  <input type="text" name="parentModuleName" value="<%=module.getParent()!=null?module.getParent().getModuleName():""%>"  readonly="true" />
				</td>
				<td>
				  <input type="button" value="..." id="ModuleTreeMenu" onclick="callGenericScreenParentModule(this);">
			  </td>
	          </tr>

	        <tr>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="isEnabled" /></td>
	          <td  class="smallfieldcell" height="34" align="center" >
				 <html:checkbox property="isEnabled"></html:checkbox>
	          </tr>

	        <tr>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="baseURL" /></td>
	          <td  class="smallfieldcell" height="34" align="center"  >
				  <html:text property="baseURL" styleClass="ControlText" value="<%=module.getBaseUrl()%>" /> </td>
	          </tr>
	        <tr>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="orderNum" /></td>
	          <td  class="smallfieldcell" height="34" align="center" >
				  <html:text property="orderNumber" styleClass="ControlText" value="<%=module.getOrderNumber()!=null?module.getOrderNumber().toString():""%>" /> </td>
	          </tr>

			<tr>
				  <td  class="button" align="center" colspan="2">
					<html:submit value="Update" /></td>
			</tr>
        <%
			}

		%>
	</table>
		</center>
	   </tr></td>
   </table>
</html:form>
</body>
</html>