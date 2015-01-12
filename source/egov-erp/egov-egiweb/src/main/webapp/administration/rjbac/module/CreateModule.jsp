<%@page import="java.util.*,
		org.egov.lib.admbndry.*" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Create Module</title>
	
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

			window.showModalDialog("<%=request.getContextPath()%>/commonyui/egov/genericScreenWithLeaf.jsp"+"?xmlconfigname="+xmlconfigname+"&categoryname="+categoryname,window,'dialogHeight:500px;dialogWidth:600px');

			//Once the window is closed you will have values for the above variables. Its your responsibility to store it in respective html field inputs.

			if(nameValue != "")

			{
				var rowObj=getRow(obj);
				var table= document.getElementById("mod_table");

				getControlInBranch(table.rows[rowObj.rowIndex],'parentModuleId').value = idValue;
				getControlInBranch(table.rows[rowObj.rowIndex],'parentModuleName').value = nameValue;


				//alert("idValue " + idValue);
				//alert("descValue " + descValue);

			} nameValue="";idValue="", descValue = "";
		}

	function setIsEnabled()
	{
		document.moduleForm.isEnabled.checked = true;
	}

	function CheckUnique(obj)
	{
		var chk = true;
		if(obj != null && obj.value != "")
		{
			chk = uniqueCheckingBoolean("<%=request.getContextPath()%>/commonyui/egov/uniqueCheckAjax.jsp","EG_MODULE","MODULE_NAME","moduleName","no","no");
			if(!chk)
			{
				alert("Module Name already exists!!");
				document.moduleForm.moduleName.value="";
				document.moduleForm.moduleName.focus();
			}
		}
		return chk;
	}
	</script>

	<html:javascript formName="moduleForm"/>
</head>

<body >
	<html:form action="/admin/CreateModule" onsubmit="return validateModuleForm(this);">

	<table width="700" align="center" id="signup">
	<tr><td>
	
	<center>

	<table width="700" align="center" id="mod_table" >
	  <tr>
	    <td width="700" height="20" bgcolor="#dddddd" align="middle" class="tableheader" colspan="3">
		Module Set up</td>
	  </tr>

	        <tr>
	          <td width="20%"  class="labelcell" height="34" align="center">
				  <bean:message key="moduleName" /><font color="#FF0000">*</font></td>
	          <td width="80%" class="labelcell" height="34" align="center" colspan="2" >
				  <html:text property="moduleName" styleClass="ControlText" onblur="return CheckUnique(this);"/></td>
	          </tr>

	        <tr>
	          <td width="20%" class="labelcell" height="34" align="center">
				  <bean:message key="moduleNameLocal" /></td>
	          <td width="80%" class="labelcell" height="34" align="center" colspan="2"  >
				  <html:text property="moduleNameLocal" styleClass="ControlText"  /></td>
	          </tr>

	        <tr>
	          <td width="20%" class="labelcell" height="34" align="center">
				  <bean:message key="moduleDesc" /><font color="#FF0000">*</font></td>
	          <td width="80%" class="labelcell" height="34" align="center" colspan="2" >
				  <html:text property="moduleDescription" styleClass="ControlText"  /> </td>
	          </tr>
	        <tr>
	          <td width="20%" class="labelcell" height="34" align="center">
				  <bean:message key="parentModule" /></td>
	          <td width="80%" class="labelcell" height="34" align="center">
	          	  <html:hidden property="parentModuleId" value=""/>
				  <input type="text" name="parentModuleName" value=""  readonly="true">
				  </td>
				  <td>
				  <input type="button" value="..." id="ModuleTreeMenu" onclick="callGenericScreenModule(this);">
			  </td>
	          </tr>

	        <tr>
	          <td width="20%" class="labelcell" height="34" align="center">
				  <bean:message key="isEnabled" /></td>
	          <td width="80%" class="labelcell" height="34" align="center"  colspan="2">
				 <html:checkbox property="isEnabled"></html:checkbox>
	          </tr>

	        <tr>
	          <td width="20%" class="labelcell" height="34" align="center">
				  <bean:message key="baseURL" /></td>
	          <td width="80%" class="labelcell" height="34" align="center"  colspan="2">
				  <html:text property="baseURL" styleClass="ControlText"/> </td>
	          </tr>
	        <tr>
	          <td width="20%" class="labelcell" height="34" align="center">
				  <bean:message key="orderNum" /></td>
	          <td width="80%" class="labelcell" height="34" align="center" colspan="2">
				  <html:text property="orderNumber" styleClass="ControlText"/> </td>
	          </tr>

		<tr>

	          <td  align="center" colspan="3">
				<html:submit value="Create" /></td>
        </tr>
	</table>
			</center>

		
	   </tr></td>
   </table>
</html:form>
</body>
</html>