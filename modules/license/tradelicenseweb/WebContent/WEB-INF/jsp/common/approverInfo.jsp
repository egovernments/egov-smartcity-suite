<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>

	function getUsersByDeisgnation(value) {
		populateapproverName({departmentId:document.getElementById("departmentid").value,designationId:document.getElementById("approverDesg").value});
	}

	function callAlertForDepartment() {
    	var value=document.getElementById("departmentid").value;
		if(value=="-1"){
			alert("Please select a Approver Department");
			document.getElementById("departmentid").focus();
			return false;
		}
	}
	
	function callAlertForDesignation() {
		var value=document.getElementById("approverDesg").value;
		if(value=="") {
			alert("Please select a Approver Designation");
			document.getElementById("approverDesg").focus();
			return false;
		}
	}
	
	function loadDesignationByDeptId() {
		  var designationObj =document.getElementById('approverDesg');
	 	  designationObj.options.length = 0;
	 	  designationObj.options[0] = new Option("----Choose----","");
	 	  populateapproverDesg({departmentId:document.getElementById("departmentid").value});	
	}	
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="bluebox" width="16%">
			&nbsp;
		</td>
		<td class="bluebox" id="deptLabel">
			<s:text name="workflow.approver.department" />
		</td>
		<td class="bluebox">
			<s:select name="departmentid" id="departmentid" list="dropdownData.departmentIdList" listKey="id" listValue="deptName" value="%{departmentId}" onchange="loadDesignationByDeptId();" />
			<egov:ajaxdropdown fields="['Text','Value']" url="../transactions/ajaxTradeLicense!getDesignationByDeptId.action" id="approverDesg" dropdownId="approverDesg" />
		</td>
		<td class="bluebox" width="10%">
			&nbsp;
		</td>
		<td class="bluebox" width="14%">
			Approver Designation:
		</td>
		<td class="bluebox" width="33%">
			<s:select id="approverDesg" name="approverDesg" list="dropdownData.desgnationList" listKey="designationId" listValue="designationName" onchange="getUsersByDeisgnation(this.value)" onfocus="callAlertForDepartment();" />
			<egov:ajaxdropdown id="approverName" fields="['Text','Value']" dropdownId="approverName" url="../transactions/ajaxTradeLicense!getUsersByPassingDesigId.action" />
		</td>
		<td class="bluebox" width="10%">
			Approver:
			<span class="mandatory">*</span>
		</td>
		<td class="bluebox" width="16%">
			<s:select id="approverName" name="approverName" list="dropdownData.approverList" headerKey="-1" headerValue="----Choose----" listKey="id" listValue="userName" value="%{approverName}" />
		</td>
		<td class="bluebox" width="16%">
			&nbsp;
		</td>
	</tr>

</table>
