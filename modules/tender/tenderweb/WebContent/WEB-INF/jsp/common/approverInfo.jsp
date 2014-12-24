<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<script>
function getUsersByDeisgnation(value)
{
populateapproverName({approverDepartmentId:document.getElementById("approverDepartmentId").value,designationId:document.getElementById("approverDesg").value});
	
}
function callAlertForDepartment()
{
    var value=document.getElementById("approverDepartmentId").value;
		if(value=="-1")
		{
			alert("Please select the Approver Department");
			document.getElementById("approverDepartmentId").focus();
			return false;
		}
}
	function callAlertForDesignation()
	{
		var value=document.getElementById("approverDesg").value;
		if(value=="")
		{
			alert("Please select the approver designation");
			document.getElementById("approverDesg").focus();
			return false;
		}
	}
	function loadDesignationByDeptId()
	{
		  var designationObj =document.getElementById('approverDesg');
	 	  designationObj.options.length = 0;
	 	  designationObj.options[0] = new Option("----Choose----","");
	 	//  alert(document.getElementById("paramDate").value);
		  populateapproverDesg({approverDepartmentId:document.getElementById("approverDepartmentId").value,paramDate:document.getElementById("paramDate").value});
	
	}
	
	
	
</script>
<div class="blueshadow"></div>
 	<h1 class="subhead"  align="center"><s:text name="approverInfo"></s:text></h1>
	  	 <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  	 	
			 	 <tr>   <td class="bluebox" width="16%">&nbsp;</td>
			  		<td class="bluebox" id="deptLabel">Approver Department</td>
					<td class="bluebox"><s:select name="approverDepartmentId" id="approverDepartmentId" list="dropdownData.approverDepartmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{approverDepartmentId}"  onchange="loadDesignationByDeptId();"/>
					<egov:ajaxdropdown fields="['Text','Value']" url="common/ajaxCommon!getDesignationByDeptId.action" id="approverDesg" dropdownId="approverDesg"/>
					</td>
			  		 <td class="bluebox" width="10%">&nbsp;</td>	  
			  		<td class="bluebox" width="14%">Approver Designation:</td>
			  		<td class="bluebox" width="33%">
			  		<s:select id="approverDesg" name="approverDesg" list="dropdownData.desgnationList" listKey="designationId" headerKey="" listValue="designationName" headerValue="----Choose----" onchange="getUsersByDeisgnation(this.value)" onfocus="callAlertForDepartment();"/>
			  		<egov:ajaxdropdown id="approverName" fields="['Text','Value']" dropdownId="approverName" url="common/ajaxCommon!getPositionByPassingDesigId.action" />
			  		</td>
			  		<td class="bluebox" width="10%">Approver:</td>
			  		<td class="bluebox" width="16%">
			  		<s:select id="approverName"  name="approverName" list="dropdownData.approverList" headerKey="-1" headerValue="----------Choose----------" listKey="position.id" listValue='userMaster.firstName+"~"+position.name'  onfocus="callAlertForDesignation();" value="%{approverName}" /> </td>
			 <td class="bluebox" width="16%">&nbsp;</td>
			 </tr>
			
		</table>
