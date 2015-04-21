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
		org.egov.infstr.commons.Module,
		org.egov.lib.rrbac.model.Action" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
	<title>Action Set Up</title>
	
		<%
				Module module = null;
				if(request.getAttribute("module") != null)
					module = (Module)request.getAttribute("module");
				System.out.println("------- Module Name = "+module.getModuleName());
				Set actionsSet = module.getActions();
				System.out.println("------- actionsSet = "+actionsSet.size());

	%>

	<script type="text/javascript">

	var delLength=new Array();


	/**
	*  Set the isEnabled checkbox to true on onload event.
	*/
	function setIsEnabled()
	{
		document.actionnForm.isEnabled.checked = true;
		document.actionnForm.isEnabled.value = "row0";
	}

	/**
	*  To check whether given Action Name is unique under a selected module.
	*/
	var b="";
	function checkUniqueActionNameDB(actionName)
	{

	var type='checkUniqueActionNameDB';
	var http = initiateRequest();
	var url = "<%=request.getContextPath()%>"+"/commons/Process.jsp?type=" +type+ "&actionName=" +actionName+ " ";
	var req2 = initiateRequest();
		//alert("333333333333333333");
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200)
		{
			var result=req2.responseText;
			result = result.split("/");
			if(result[0]!= null && result[0]!= "")
			 {
				b=result[0];
			}
		}

		return b;

	}
	function CheckUniqueActionName(obj)
	{

	var checking = checkUniqueActionNameDB(obj.value);
	var rowObj=getRow(obj);
	//alert("rowObj >>  " +rowObj);
	var tbl= document.getElementById("action_table");
	//alert("actionId >> " +getControlInBranch(tbl.rows[rowObj.rowIndex],'actionId').value);
	var lastRow = tbl.rows.length;

		if(obj.value == null || obj.value=="")
		{
			alert("Action Name is required !");
			obj.focus();
			return false;
		}

		else if(checking=='true' &&
		getControlInBranch(tbl.rows[rowObj.rowIndex],'actionId').value=="row"+lastRow)
		{
			alert("Action Name already Exists !!");
			obj.value="";
			obj.focus();
			return false;

		}
		else
		{
			//alert("Inside CheckUniqueActionName");
			var tbl = document.getElementById('action_table');
			var lastRow = tbl.rows.length;

			//alert("lastRow="+lastRow);
			var rowObj=getRow(obj);
			var actionId = getControlInBranch(tbl.rows[rowObj.rowIndex],'actionId').value;
			var actionName = obj.value;
			for(var i=0;i<lastRow-1;i++)
			{
				if(tbl.rows[i].cells[0] != null && tbl.rows[i].cells[0].childNodes[2] != null && actionId != tbl.rows[i].cells[0].childNodes[0].value)
				{
					var name1 = tbl.rows[i].cells[0].childNodes[2].value;
					if(trimText(name1).toUpperCase() == trimText(actionName).toUpperCase())
					{
						alert("Action Name already Exists !!");
						obj.value="";
						obj.focus();
						return false;
					}

				}
			}
		}
		return true;
	}


	/**
	*  This method is used to add a row. The value of "actionId" and "isEnabled" fields of new row
	*  is set to the row number prefixed with the word "row" like "row1", "row2", etc.
	*  for identification. The id of "Delete" field is set to row number.
	*/
	function addRow()
	{

		var tbl = document.getElementById('action_table');
		var lastRow = tbl.rows.length;
		var iteration = lastRow;
		var row = tbl.insertRow(lastRow);
		var len= tbl.rows[lastRow-1].cells.length;

		  for(var i=0;i<len;i++)
		  {

			 var rowObj = tbl.rows[lastRow-1].cells[i].cloneNode(true);
			 //alert("nodename = "+tbl.rows[lastRow-1].cells[i].childNodes[0].name );
			 tbl.rows[lastRow].appendChild(rowObj);
	 		if(tbl.rows[lastRow].cells[i].childNodes[0].name == "actionId")
			{
				tbl.rows[lastRow].cells[i].childNodes[0].value="row"+lastRow;
			}
			if(tbl.rows[lastRow].cells[i].childNodes[0].name == "isEnabled")
			{
				tbl.rows[lastRow].cells[i].childNodes[0].checked = true;
				tbl.rows[lastRow].cells[i].childNodes[0].value="row"+lastRow;
			}
	 		if(tbl.rows[lastRow].cells[i].childNodes[0].value == "Delete")
	 		{
				tbl.rows[lastRow].cells[i].childNodes[0].id=lastRow;
			}
			else
			{
				// set all its children to empty
				var children = rowObj.childNodes;
				var child;
				if (children && children.length > 0)
				{
					for(var j=0; j<children.length; j++)
					{
						child=tbl.rows[lastRow].cells[i].childNodes[j];
						if(child && child.value)
						{
						  // The values of fields isEnabled and actionId are excluded
						  // from being set to empty value.
						  if(child.name != "isEnabled" && child.name !="actionId")
						   	child.value="";
					  	}
					}
				}

			}

		}


	}

	/**
	*	This method deletes a row
	*/
	var i=0;

	function deleteTableRow(obj)
	{
	  //alert("obj.id="+obj.id);
	  var rowId = obj.id;

	  var tbl = document.getElementById('action_table');
	  var rowNumber=getRow(obj).rowIndex;
	  var rowo=tbl.rows.length;
	  //alert("rowo  " +rowo)
	  if(rowo <=5)
	  {
		  delLength[i] = getControlInBranch(tbl.rows[rowNumber],'actionId').value;
		  i++;
		  document.actionnForm.actionName.value="";
		  document.actionnForm.baseURL.value="";
		  document.actionnForm.queryParams.value="";
		  document.actionnForm.urlOrderId.value="";
		  document.actionnForm.orderNumber.value="";
		  document.actionnForm.displayName.value="";
		  document.actionnForm.helpURL.value="";
		  document.actionnForm.isEnabled.checked=false;
		  return true
	  }
	  else
	  {
		delLength[i] = getControlInBranch(tbl.rows[rowNumber],'actionId').value;
		i++;
		tbl.deleteRow(rowNumber)

		return true

	  }

	}



	/**
	*     To set the value of checkbox on click.
	*/
	function setValue(obj)
	{
		if(obj.value == "yes")
		{
			obj.value = "no";
			obj.checked = false;
		}
		else if(obj.value == "no")
		{
			obj.value = "yes";
			obj.checked = true;
		}

	}
	var a="";
	function checkBaseUrlwithQueryParam(baseURLObj1,queryParamsObj1)
	{
		var type='checkBaseUrlwithQueryParam';
		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>"+"/commons/Process.jsp?type=" +type+ "&baseURLObj1="+baseURLObj1+"&queryParamsObj1="+queryParamsObj1+" ";
		var req2 = initiateRequest();
		//alert("333333333333333333");
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200)
		{
			var result=req2.responseText;
			result = result.split("/");
			if(result[0]!= null && result[0]!= "")
			 {
				a=result[0];
			}
		}

		return a;

	}
	/**
	*  Checks whether all mandatory field values exists.
	*  Action Name and Base URL are mandatory fields in this form.
	*/
	function validate()
	{

		//alert("Inside validate");
		var tbl = document.getElementById('action_table');
		var lastRow = tbl.rows.length;
		//alert("lastRow="+lastRow);
		for(var i=0;i<lastRow;i++)
		{
			//alert("i="+i);

			var rowObj = getRow(tbl.rows[i].cells[0].childNodes[0]);
			var actionName = "";
			var obj=getControlInBranch(tbl.rows[rowObj.rowIndex],'actionName');
			var baseURL = getControlInBranch(tbl.rows[rowObj.rowIndex],'baseURL');
			//alert("obj="+obj);
			//alert("baseURL="+baseURL);
			if(obj != null && obj.value != null )
			{
				actionName = obj.value;
				//alert("actionName="+actionName);
				if((actionName == "") && (baseURL.value != ""))
				{
					alert("Action Name is Required.");
					obj.focus();
					return false;
				}
				else
				{
					//alert("else baseURL.value="+baseURL.value);
					if((baseURL.value == "" ) && (actionName != ""))
					{
						alert("Base URL is Required.");
						baseURL.focus();
						return false;
					}
					if(!CheckCombo(tbl,rowObj,lastRow))
						return false;

				}
			}


		}
		var len = 	delLength.length
		//alert("len >>> " + len);
		for(var i=0;i<len;i++)
		{
			if(delLength[i]!=null && delLength[i]!="" && document.forms[0].deleteActionSet.length>0)
			{
				//alert("delLength[i] >>> " + delLength[i]);
				document.forms[0].deleteActionSet[i].value=delLength[i];


			}
			else if(delLength[i]!=null && delLength[i]!="")
			{
				document.forms[0].deleteActionSet.value=delLength[i];

			}
		}

		return true;
	}
	function allowOnlyNumeric(obj)
	{

	 var isNotAlphaNumric="false";
	 var str=obj.value;
	 var len=str.length;
	 var i=0,j=0;
	 var character;
	 var finalStr;
	 var validchars="0123456789";

	 //alert("LengthfromSASvalidation="+len);
	 //if(trimAll(obj.value)!="" && obj.value!=null)
	 if(obj.value!=null || obj.value!="")
	 {
	 	for(i=0;i<len && isNotAlphaNumric=="false";i++)
	 	{
	 		//alert("Str()="+str.charAt(i));
	 		if(str.charAt(0)=="" || str.charAt(0)==null)
	 		{
	 			//alert("Hii");
	 			str=trimAll(obj.value);
	 		}
	 		character=str.charAt(i);

	 		//if(isNaN(character))
	 		if(validchars.indexOf(str.charAt(i))!=-1)
	 		{
	 			//isnumber="false";
	 			//finalStr=character;
	 			j++;
	 			//alert("finalStr="+finalStr);
	 		}
	 		else
	 		{
	 			isNotAlphaNumric="true";
	 		}
	 	}

	 	if(isNotAlphaNumric=="true")
	 	{
	 		alert("Please enter a valid character!!");
	 		//obj.value=trimAll(str.substr(0,j));
	 		obj.value=str.substr(0,j);
	 		//alert("SubString="+obj.value);
	 		obj.focus();
	 		return false;
	 	}
	 	//obj.value=trimAll(obj.value);
	 	//obj.value=obj.value;
		 }
		 return;
		}

	/**
	* This method is used to check whether the combination of Base URL and Query Params are unique.
	*/
	function CheckCombo(tbl,rowObj,lastRow)
	{
		var baseURLObj1 = getControlInBranch(tbl.rows[rowObj.rowIndex],'baseURL');
		var queryParamsObj1 = getControlInBranch(tbl.rows[rowObj.rowIndex],'queryParams');
		var actionId1 =  getControlInBranch(tbl.rows[rowObj.rowIndex],'actionId');
		//alert("actionId1="+actionId1);
		if(baseURLObj1 != null && baseURLObj1 == "")
		{
			alert("Base URL is Required.");
			baseURLObj1.focus();
			return false;
		}
		else if(baseURLObj1 != "" || (queryParamsObj1 != null && queryParamsObj1 != ""))
		{
		var combo1 = getCombo(baseURLObj1,queryParamsObj1);

		if(isNaN(actionId1.value) && combo1 != null && combo1 != "")
		{
		var checking = checkBaseUrlwithQueryParam(baseURLObj1.value,queryParamsObj1.value);

		if(checking=='true')
		{
		alert("Combination of Base URL and Query Parameters should be unique.\n This Combination already exists!!");

		}
		}
			//alert("combo1="+combo1);
			if(combo1 != null && combo1 != "")
			{
				for(var j=1;j<lastRow;j++)
				{
					var rowObj2 = getRow(tbl.rows[j].cells[0].childNodes[0]);
					var baseURLObj2 = getControlInBranch(tbl.rows[rowObj2.rowIndex],'baseURL');
					var queryParamsObj2 = getControlInBranch(tbl.rows[rowObj2.rowIndex],'queryParams');
					var actionId2 =  getControlInBranch(tbl.rows[rowObj2.rowIndex],'actionId');
					//alert("actionId2="+actionId2);
					if(actionId1 != actionId2)
					{
						//alert("actionIds are not equal");
						var combo2 = getCombo(baseURLObj2,queryParamsObj2);
						if(combo2 != null && combo2 != "")
						{
							if(combo1 == combo2 && combo1)
							{

								alert("Combination of Base URL and Query Parameters should be unique.\n This Combination already exists!");
								baseURLObj2.focus();
								return false;
							}

						}
					}
				}
			}
		}
		return true;
	}

	/**
	*	This method takes two objects as input and concatenates their values.
	*   Returns the concatenated value.
	*/
	function getCombo(obj1, obj2)
	{
		//alert("Inside getCombo");

		var combo = "";

		if(obj1 !=null && obj1.value != null)
		{
				combo = combo + trimText(obj1.value).toUpperCase();
				//alert("obj1.value="+obj1.value);
		}
		if(obj2 != null && obj2.value != null)
		{
			combo = combo + trimText(obj2.value).toUpperCase();
			//alert("obj2.value="+obj2.value);
		}
		if(combo != null)
		{
			//alert("combo = "+combo);
			return combo;
		}
		else
			return null;


	}

	</script>


</head>

<body >
	<html:form action="/admin/CreateOrUpdateAction" >


	<table align="center" id="action_table">
	  <tr>
	    <td  bgcolor="#dddddd" align="center" colspan="8">
		Action Set up</td>
	  </tr>
	<tr style="width:700" >
			<td  class="labelcell" height="34" align="center" colspan="9">
			  <bean:message key="moduleName" />: <%=StringUtils.emptyIfNull(module.getModuleName())%></td>
			</tr>
			<tr>
			  <td  class="labelcell" height="34" align="center" colspan="9">
			<bean:message key="moduleDesc" />: <%=StringUtils.emptyIfNull(module.getModuleDescription())%>
			  </td>

		</tr>
	        <tr>
	          <td class="labelcell" height="34" align="center">
				  <bean:message key="actionName" /><font color="#FF0000">*</font></td>
	             <td class="labelcell" height="34" align="center">
				  <bean:message key="baseURL" /><font color="#FF0000">*</font></td>
	            <td class="labelcell" height="34" align="center">
				  <bean:message key="queryParams" /></td>
	          <td class="labelcell" height="34" align="center">
				  <bean:message key="urlOrderId" /></td>
	           <td  class="labelcell" height="34" align="center">
				  <bean:message key="orderNumber" /></td>
	          <td  class="labelcell" height="34" align="center">
				  <bean:message key="displayName" /></td>
		 <td  class="labelcell" height="34" align="center">
				  <bean:message key="helpURL" /></td>
				 <td  class="labelcell" height="34" align="center">
				  <bean:message key="isEnabled" /></td>
			</tr>
			<%
			int i=0;
			if(actionsSet != null && actionsSet.size()>0)
				{
					for(Iterator itr=actionsSet.iterator(); itr.hasNext();)
					{
						Action action = (Action)itr.next();
						if(action != null)
						{
							
			%>
						<tr>

							<html:hidden property="moduleId" value="<%=action.getModule().getId().toString()%>"/>
						  <td  class="smallfieldcell" height="34" align="center"  >
						  <html:hidden property="actionId" value="<%= action.getId().toString()%>"/>
							  <html:text property="actionName" styleId="actionName" styleClass="ControlText" value="<%=StringUtils.emptyIfNull(action.getName())%>" onblur="return CheckUniqueActionName(this);"/></td>
						  <td  class="smallfieldcell" height="34" align="center"  >
							  <html:text property="baseURL" styleClass="ControlText"  value="<%=StringUtils.emptyIfNull(action.getUrl())%>" /></td>
						  <td  class="smallfieldcell" height="34" align="center"  >
							  <html:text property="queryParams" styleClass="ControlText" value="<%=StringUtils.emptyIfNull(action.getQueryParams())%>" onblur="return validate();"/> </td>
						  <td class="smallfieldcell" height="34" align="center">
							  <html:text property="urlOrderId" styleClass="ControlText" value="<%=action.getUrlOrderId()!=null?action.getUrlOrderId().toString():""%>"onchange="return allowOnlyNumeric(this);"/></td>
						  <td  class="smallfieldcell" height="34" align="center"  >
							  <html:text property="orderNumber" styleClass="ControlText" value="<%=action.getOrderNumber()!=null?action.getOrderNumber().toString():""%>"onchange="return allowOnlyNumeric(this);"/> </td>
						  <td  class="smallfieldcell" height="34" align="center" >
							  <html:text property="displayName" styleClass="ControlText" value="<%=StringUtils.emptyIfNull(action.getDisplayName())%>"/></td>
					  <td  class="smallfieldcell" height="34" align="center" >
					 <html:text property="helpURL" styleClass="ControlText" /></td>


							<td width="15%" height="34" align="center"  >
						<%
								System.out.println("action.getIsEnabled()="+action.getIsEnabled());
								if(action.getIsEnabled()!=null && action.getIsEnabled().intValue() == 1)
								{
							%>
									<input type="checkbox" name="isEnabled" value="<%= action.getId().toString()%>" checked=true  onclick="setValue(this);"/>
							 <%
								}
								else
								{
							 %>
									<input type="checkbox" name="isEnabled" value="<%= action.getId().toString()%>" onclick="setValue(this);"/>
							 <%
								}
							 %>
							 	</td>
							  <td class="button" align="center">
								<input type="button" value="Delete" id="<%=i++%>" onclick="deleteTableRow(this)" /></td>

						  </tr>

			<%
						}%>
						<input type="hidden" id="deleteActionSet" name="deleteActionSet" />
					<%}
				}
				else
				{

			%>
	        <tr>

				<html:hidden property="moduleId" value="<%=module.getId().toString()%>"/>
	          <td  class="smallfieldcell" height="34" align="center"  >
	          		<html:hidden property="actionId" value="row0" />
				  <html:text property="actionName" styleClass="ControlText" onblur="return CheckUniqueActionName(this);"/></td>
	          <td  class="smallfieldcell" height="34" align="center"  >
				  <html:text property="baseURL" styleClass="ControlText"  /></td>
	          <td  class="smallfieldcell" height="34" align="center"  >
				  <html:text property="queryParams" styleClass="ControlText"  onblur="return validate();"/> </td>
	          <td class="smallfieldcell" height="34" align="center">
	          	  <html:text property="urlOrderId" styleClass="ControlText" /></td>
	          <td  class="smallfieldcell" height="34" align="center"  >
				  <html:text property="orderNumber" styleClass="ControlText"/> </td>
	          <td  class="smallfieldcell" height="34" align="center" >
				  <html:text property="displayName" styleClass="ControlText"/></td>
		  <td  class="smallfieldcell" height="34" align="center" >
				  <html:text property="helpURL" styleClass="ControlText"/></td>
		<td width="15%" height="34" align="center"  >
				 <html:checkbox property="isEnabled" value="row0" onclick="setValue(this);"/></td>
				  <td class="button" align="center">
					<input type="button" value="Delete" id="0" onclick="deleteTableRow(this)" /></td>
	          </tr>
	         <%
		 		}
	         %>
		</table>
		<table style="width:100%">
			<tr>
				  <td class="button" align="center" colspan="9">
					<input type="button" value="Add" onclick="addRow();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:submit  value="Save" onclick="return validate();"/></td>
			</tr>
	</table>


</html:form>
</body>
</html>
